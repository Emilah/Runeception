package org.runeception.client.plugins.impl;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.runeception.client.RSClient;

@SuppressWarnings("serial")
public class Calculator extends JPanel {
	
	private enum Operator {
		ADD,
		SUBTRACT,
		DIVIDE,
		MULTIPLY
	}
	
	private double answer = 0.0d;
	
	private String valueA = "";
	
	private String value = "";
	
	private JTextField outputField;
	
	private Operator operator;

	public Calculator() {
		
		setPreferredSize(new Dimension(300, RSClient.screenHeight));
		setLayout(null);
		
		outputField = new JTextField();
		outputField.setBounds(12, 13, 256, 99);
		outputField.setEditable(false);
		add(outputField);
		outputField.setColumns(10);
		
		JButton oneButton = new JButton("1");
		oneButton.setBounds(12, 201, 55, 25);
		oneButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				value += 1;
				outputField.setText(value);
			}
		});
		add(oneButton);
		
		JButton twoButton = new JButton("2");
		twoButton.setBounds(79, 201, 55, 25);
		twoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				value += 2;
				outputField.setText(value);
			}
		});
		add(twoButton);
		
		JButton threeButton = new JButton("3");
		threeButton.setBounds(146, 201, 55, 25);
		threeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				value += 3;
				outputField.setText(value);
			}
		});
		add(threeButton);
		
		JButton fourButton = new JButton("4");
		fourButton.setBounds(12, 163, 55, 25);
		fourButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				value += 4;
				outputField.setText(value);
			}
		});
		add(fourButton);
		
		JButton fiveButton = new JButton("5");
		fiveButton.setBounds(79, 163, 55, 25);
		fiveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				value += 5;
				outputField.setText(value);
			}
		});
		add(fiveButton);
		
		JButton sixButton = new JButton("6");
		sixButton.setBounds(146, 163, 55, 25);
		sixButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				value += 6;
				outputField.setText(value);
			}
		});
		add(sixButton);
		
		JButton nineButton = new JButton("9");
		nineButton.setBounds(146, 125, 55, 25);
		nineButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				value += 9;
				outputField.setText(value);
			}
		});
		add(nineButton);
		
		JButton eightButton = new JButton("8");
		eightButton.setBounds(79, 125, 55, 25);
		eightButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				value += 8;
				outputField.setText(value);
			}
		});
		add(eightButton);
		
		JButton sevenButton = new JButton("7");
		sevenButton.setBounds(12, 125, 55, 25);
		sevenButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				value += 7;
				outputField.setText(value);
			}
		});
		add(sevenButton);
		
		JButton zeroButton = new JButton("0");
		zeroButton.setBounds(79, 239, 55, 25);
		zeroButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				value += 0;
				outputField.setText(value);
			}
		});
		add(zeroButton);
		
		JButton addButton = new JButton("+");
		addButton.setBounds(213, 125, 55, 25);
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				operator = Operator.ADD;
				if(valueA.isEmpty()) {
					valueA = value;
					value = "";
					outputField.setText("");
				} else 
					process(valueA, value);
			};
		});
		add(addButton);
		
		JButton minusButton = new JButton("-");
		minusButton.setBounds(213, 163, 55, 25);
		minusButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				operator = Operator.SUBTRACT;
				if(valueA.isEmpty()) {
					valueA = value;
					value = "";
					outputField.setText("");
				} else 
					process(valueA, value);
			};
		});
		add(minusButton);
		
		JButton multiplyButton = new JButton("*");
		multiplyButton.setBounds(213, 201, 55, 25);
		multiplyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				operator = Operator.MULTIPLY;
				if(valueA.isEmpty()) {
					valueA = value;
					value = "";
					outputField.setText("");
				} else 
					process(valueA, value);
			};
		});
		add(multiplyButton);
		
		JButton divideButton = new JButton("/");
		divideButton.setBounds(213, 239, 55, 25);
		divideButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				operator = Operator.DIVIDE;
				if(valueA.isEmpty()) {
					valueA = value;
					value = "";
					outputField.setText("");
				} else 
					process(valueA, value);
			};
		});
		add(divideButton);
		
		JButton equalButton = new JButton("=");
		equalButton.setBounds(213, 277, 55, 25);
		equalButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(valueA.isEmpty()) {
					valueA = value;
					value = "";
					outputField.setText("");
				} else 
					process(valueA, value);
			};
		});
		add(equalButton);
		
		JButton clearButton = new JButton("Clear");
		clearButton.setBounds(12, 277, 189, 25);
		clearButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				outputField.setText("");
				operator = null;
				valueA = "";
				value = "";
				answer = 0.0d;
			}
		});
		add(clearButton);
		
		JButton decimalButton = new JButton(".");
		decimalButton.setBounds(12, 239, 55, 25);
		decimalButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				value = value + ".";
				outputField.setText(value);
			}
		});
		add(decimalButton);
		
		JButton negativeButton = new JButton("-");
		negativeButton.setBounds(146, 239, 55, 25);
		negativeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				value = "-" + value;
				outputField.setText(value);
			}
		});
		add(negativeButton);
	}
	
	public void process(String valueA, String value) {
		switch(operator) {
		case ADD:
			add(valueA, value);
			break;
		case SUBTRACT:
			subtract(valueA, value);
			break;
		case MULTIPLY:
			multiply(valueA, value);
			break;
		case DIVIDE:
			divide(valueA, value);
			break;
		}
		outputField.setText(""+answer);
	}
	
	public void add(String valueA, String value) {
		answer = (convertString(valueA) + convertString(value));
	}
	
	public void subtract(String valueA, String value) {
		answer = (convertString(valueA) - convertString(value));
	}
	
	public void multiply(String valueA, String value) {
		answer = (convertString(valueA) * convertString(value));
	}
	
	public void divide(String valueA, String value) {
		answer = (convertString(valueA) / convertString(value));
	}
	
	private double convertString(String string) {
		return Double.parseDouble(string);
	}
}
