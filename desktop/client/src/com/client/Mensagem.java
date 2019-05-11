package com.client;

public class Mensagem {

	private String origem;
	private String destino;
	private String mensagem;

	public Mensagem() {
	}

	public Mensagem(String origem, String destino, String mensagem) {
		this.origem = origem;
		this.destino = destino;
		this.mensagem = mensagem;

	}

	public String getOrigem() {
		return origem;
	}

	public void setOrigem(String origem) {
		this.origem = origem;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

}
