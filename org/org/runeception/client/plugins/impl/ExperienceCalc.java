package org.runeception.client.plugins.impl;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
/**
 * Calculates Experience & Level Changes in Runescape
 * @author Emily Perkins (emilah@live.com)
 * @since 3/13/2012 3:30PM
 * @version 1.0.0
 */
@SuppressWarnings("serial")
public class ExperienceCalc extends JPanel {
	
	private HashMap<Integer, Integer> xpTable = new HashMap<Integer, Integer>();
	private JTextField inputField;
	private JTextField goalField;
	
	public ExperienceCalc() {
		setLayout(null);
		setPreferredSize(new Dimension(300, 503));
		
		JButton submitButton = new JButton("Submit");
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int input = Integer.parseInt(inputField.getText());
				int goal = Integer.parseInt(goalField.getText());
				if(toggleBar.isSelected()) {
					outputArea.setText("Levels: "+((goal - input) > 0 ? (goal - input) : "0") + System.getProperty("line.separator") + 
							"Experience: "+ (xpTable.get(goal) - (xpTable.get(input)) > 0 
									? insertCommas((xpTable.get(goal) - xpTable.get(input))) : "0"));
				} else {
					outputArea.setText("Levels: "+((goal - getLevel(input)) > 0 ? (goal - getLevel(input)) : "0") +
							System.getProperty("line.separator") +  "Experience: "+ ((xpTable.get(goal) - input) > 0
									? insertCommas((xpTable.get(goal) - input)) : "0"));
				}
			}
		});
		submitButton.setBounds(12, 270, 276, 34);
		add(submitButton);
		
		inputField = new JTextField();
		inputField.setBounds(89, 13, 199, 22);
		add(inputField);
		inputField.setColumns(10);
		
		goalField = new JTextField();
		goalField.setBounds(89, 99, 199, 22);
		add(goalField);
		goalField.setColumns(10);
		
		toggleBar = new JToggleButton("");
		try {
			toggleBar.setSelectedIcon(new ImageIcon(ImageIO.read(new File(System.getProperty("user.home") + "//Runeception/cache/levelBar.png"))));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			toggleBar.setIcon(new ImageIcon(ImageIO.read(new File(System.getProperty("user.home") + "//Runeception/cache/experienceBar.png"))));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		toggleBar.setBounds(12, 48, 276, 25);
		add(toggleBar);
		
		JLabel lblInput = new JLabel("Input:");
		lblInput.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblInput.setBounds(12, 16, 65, 16);
		add(lblInput);
		
		JLabel lblGoal = new JLabel("Goal:");
		lblGoal.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblGoal.setBounds(12, 102, 65, 16);
		add(lblGoal);
		
		outputArea = new JTextArea();
		outputArea.setEditable(false);
		outputArea.setBounds(12, 131, 276, 117);
		add(outputArea);
		populateTable();
	}
	
	private String insertCommas(Integer value) {
		String string = String.valueOf(value);
        if(string.length() < 4){
            return string;
        }
        return insertCommas(string.substring(0, string.length() - 3)) + "," + string.substring(string.length() - 3, string.length());
    }
	
	private String insertCommas(String string) {
        if(string.length() < 4){
            return string;
        }
        return insertCommas(string.substring(0, string.length() - 3)) + "," + string.substring(string.length() - 3, string.length());
    }
	
	public int getLevel(int experience) {
		for(int i = 1; i < xpTable.size(); i++) {
			if(xpTable.get(i) < experience) {
			  if((i + 1 <= xpTable.size()) && xpTable.get(i + 1) > experience) {
				  
					return i;
			  }
			}
		}
		return 1;
	}
	
	public void populateTable() {
		int output = 0;
		int experience = 0;
		for(int level = 1; level <= 120; level++) {
			experience += Math.floor(level + 300 * Math.pow(2, level / 7.));
			xpTable.put(level, output);
			output = (int) Math.floor(experience / 4);
		}
	}
	
	private JToggleButton toggleBar;
	
	private JTextArea outputArea;
	
	public JTextArea getOutputArea() {
		return outputArea;
	}
}
