package com.client;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainPanel extends JPanel {

	private JLabel lblMensagem;
	private JTextArea txaMensagem;

	private JLabel lblOrigem;
	private JTextField txtOrigem;

	private JLabel lblDestino;
	private JTextField txtDestino;

	private JButton btnEnviar;

	private io.socket.client.Socket socket;
	private Window parent;

	private ObjectMapper mapper;

	public MainPanel(Window parent) {

		this.parent = parent;
		this.mapper = new ObjectMapper();

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JPanel panLblMensagem = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panLblMensagem.add(getLblMensagem());
		add(panLblMensagem);
		add(getTxaMensagem());

		JPanel panOrigem = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panOrigem.add(getLblOrigem());
		panOrigem.add(getTxtOrigem());

		JPanel panDestino = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panDestino.add(getLblDestino());
		panDestino.add(getTxtDestino());

		JPanel panOrigemDestino = new JPanel(new GridLayout());
		panOrigemDestino.add(panOrigem);
		panOrigemDestino.add(panDestino);
		add(panOrigemDestino);

		JPanel panBtnEnviar = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panBtnEnviar.add(getBtnEnviar());
		add(panBtnEnviar);

		try {
			initialize();
			getTxaMensagem().setText("DESKTOP - Hello World!");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(parent, e.getMessage());
			e.printStackTrace();
		}

	}

	private void initialize() throws URISyntaxException {

		socket = IO.socket("http://localhost:3000");
		socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

			@Override
			public void call(Object... args) {
				getTxtOrigem().setText(socket.id());
			}

		}).on("message", new Emitter.Listener() {

			@Override
			public void call(Object... args) {
				try {
					Mensagem msg = mapper.readValue(args[0].toString(), Mensagem.class);
					JOptionPane.showMessageDialog(parent,
							"Eu recebi uma mensagem de " + msg.getOrigem() + " que diz " + msg.getMensagem());
				} catch (IOException e) {
					JOptionPane.showMessageDialog(parent, e.getMessage());
					e.printStackTrace();
				}
			}

		}).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

			@Override
			public void call(Object... args) {
				JOptionPane.showMessageDialog(parent, "Desconectado.");
			}

		}).on(Socket.EVENT_RECONNECT, new Emitter.Listener() {

			@Override
			public void call(Object... args) {
				JOptionPane.showMessageDialog(parent, "Reconectado.");
			}

		}).on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {

			@Override
			public void call(Object... args) {
				JOptionPane.showMessageDialog(parent, "Erro na conexão");
			}

		}).on(Socket.EVENT_CONNECT_TIMEOUT, new Emitter.Listener() {

			@Override
			public void call(Object... args) {
				JOptionPane.showMessageDialog(parent, "Timeout na conexão");
			}

		});
		socket.connect();
	}

	private Component getLblMensagem() {
		if (lblMensagem == null) {
			lblMensagem = new JLabel("Mensagem");
		}
		return lblMensagem;
	}

	private JButton getBtnEnviar() {
		if (btnEnviar == null) {
			btnEnviar = new JButton("Enviar");
			btnEnviar.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					if (getTxtOrigem().getText().trim().isEmpty()) {
						JOptionPane.showMessageDialog(parent, "Origem é um campo obrigatório!");
						return;
					}

					if (getTxtDestino().getText().trim().isEmpty()) {
						JOptionPane.showMessageDialog(parent, "Destino é um campo obrigatório!");
						return;
					}

					if (getTxaMensagem().getText().trim().isEmpty()) {
						JOptionPane.showMessageDialog(parent, "Mensagem é um campo obrigatório!");
						return;
					}

					String origem = getTxtOrigem().getText().trim();
					String destino = getTxtDestino().getText().trim();
					String mensagem = getTxaMensagem().getText().trim();

					Mensagem msg = new Mensagem(origem, destino, mensagem);
					try {
						socket.emit("send", mapper.writeValueAsString(msg));
					} catch (JsonProcessingException e1) {
						JOptionPane.showMessageDialog(parent, e1.getMessage());
						e1.printStackTrace();
					}
				}
			});
		}
		return btnEnviar;
	}

	private JTextField getTxtDestino() {
		if (txtDestino == null) {
			txtDestino = new JTextField(15);
			txtDestino.setSelectionStart(0);
		}
		return txtDestino;
	}

	private JLabel getLblDestino() {
		if (lblDestino == null) {
			lblDestino = new JLabel("Destino");
		}
		return lblDestino;
	}

	private JTextField getTxtOrigem() {
		if (txtOrigem == null) {
			txtOrigem = new JTextField(15);
			txtOrigem.setSelectionStart(0);
		}
		return txtOrigem;
	}

	private JLabel getLblOrigem() {
		if (lblOrigem == null) {
			lblOrigem = new JLabel("Origem");
		}
		return lblOrigem;
	}

	private JTextArea getTxaMensagem() {
		if (txaMensagem == null) {
			txaMensagem = new JTextArea(5, 20);
		}
		return txaMensagem;
	}

}
