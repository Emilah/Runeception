package org.runeception.client.plugins.impl;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.runeception.client.RSClient;

@SuppressWarnings("serial")
public class Calculator extends JPanel {

	public Calculator() {
		setPreferredSize(new Dimension(300, RSClient.screenHeight));
		setBackground(Color.BLACK);
	}
	
	public void initialize() {
		addButton = new JButton("+");
		subtractButton = new JButton("-");
	}
	
	private JTextField textField;
	
	private JLabel answer;
	
	private JButton addButton;
	private JButton subtractButton;
	private JButton multiplyButton;
	private JButton divideButton;
	private JButton equalsButton;
}
