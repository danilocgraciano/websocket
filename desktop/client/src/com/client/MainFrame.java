package com.client;

import javax.swing.JFrame;

public class MainFrame extends JFrame {

	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 500, 300);
		setLocationRelativeTo(null);
		setTitle("WS - Comunicador - 1.0.0");
		setLayout(null);
		setContentPane(new MainPanel(this));
		revalidate();

	}

}
