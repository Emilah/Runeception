package org.runeception.client.plugins.impl;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.AttributedString;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.runeception.client.RSClient;
/**
 * High Score Default Plugin
 * @author Emily Perkins (emilah@live.com)
 * @since 2/23/2012
 * @version 1.0.2
 *
 */
@SuppressWarnings("serial") 
public class HighScore extends JPanel { 
	
	private final String defaultPage = "http://hiscore.runescape.com/index_lite.ws?player=";
	
	private final String defaultIconPage = "http://www.runescape.com/img/hiscore/compare/skills/";
	
	private final String miscMinigamePage = "http://images1.wikia.nocookie.net/__cb20120220234906/runescape/images/5/51/TokHaar-Kal.png";
	
	private final String gameMiniPage = "http://images4.wikia.nocookie.net/__cb20111121013622/runescape/images/d/d5/Fighter_torso.png";
	
	private String[] skills = {"Attack", "Defence", "Strength", "Constit", "Ranged", "Prayer", "Magic", "Cooking", 
			"Woodcutting", "Fletching", "Fishing", "Firemaking", "Crafting", "Smithing", "Mining", "Herblore",
			"Agility", "Thieving", "Slayer", "Farming", "Runecraft", "Hunter", "Construct", "Summoning", "Dungeon"
	};
	
	private JTextField usernameField;
	
	private boolean initalized = false;
	
	public HighScore() {
		setPreferredSize(new Dimension(300, RSClient.screenHeight));
		setBackground(Color.BLACK);
		setLayout(null);
		
		usernameField = new JTextField();
		usernameField.setBounds(10, 11, 280, 20);
		usernameField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				data = getHighScoreData();
				repaint();
			}
		});
		add(usernameField);
		usernameField.setColumns(10);
		setVisible(true);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(initalized) {
			try {
				drawIconImages(g);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

	private JButton[] skillButtons = new JButton[31];
	
	public void drawIconImages(Graphics g) throws InterruptedException, IOException {
		MediaTracker tracker = new MediaTracker(this);
		for(String skillName : skills) {
			tracker.addImage(Toolkit.getDefaultToolkit().getImage(new URL(defaultIconPage+"xp_"+skillName+"_total.png")), 0);
		}
		tracker.addImage(Toolkit.getDefaultToolkit().getImage(new URL(defaultIconPage+"overall.png")), 0);
		tracker.addImage(Toolkit.getDefaultToolkit().getImage(new URL(miscMinigamePage)), 0);
		tracker.addImage(Toolkit.getDefaultToolkit().getImage(new URL(gameMiniPage)), 0);
		drawLevelInformation(g);
		tracker.waitForAll();
		int baseX = 10, baseY = 40;
		int skillCount = 1;
		for(String skill : skills) {
			if(skillCount == 14)
				baseY = 40;
			if(skillCount >= 14)
				baseX = 100;
			else
				baseX = 10;
			try {
				Image icon = Toolkit.getDefaultToolkit().getImage(new URL(defaultIconPage+"xp_"+skill+"_total.png"));
				skillButtons[skillCount] = new JButton();
				skillButtons[skillCount].setIcon(new ImageIcon(icon));
				skillButtons[skillCount].setSize(icon.getWidth(this), icon.getHeight(this));
				skillButtons[skillCount].setOpaque(false);
				skillButtons[skillCount].setBorderPainted(false);
				skillButtons[skillCount].setContentAreaFilled(false);
				skillButtons[skillCount].setToolTipText("<html>" +
						"<center>"+ (skill.equals("Constit") ? "Constitution" : (skill.equals("Dungeon") ? "Dungeoneering" : (skill.equals("Construct") ? "Construction" : skill))) + "</center>" + 
						"Rank: "+ getRank(skillCount) + "<br/>" +
						"Level: "+ getLevel(skillCount) + "<br/>" +
						"Xp: "+ getExperience(skillCount) +
						"</html>");
				skillButtons[skillCount].setLocation(baseX, baseY);
				skillButtons[skillCount].setFocusPainted(false);
				add(skillButtons[skillCount]);
				skillButtons[skillCount].setVisible(true);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			baseY += 30;
			skillCount++;
		}
		final Image overallIcon = Toolkit.getDefaultToolkit().getImage(new URL(defaultIconPage+"overall.png"));
		skillButtons[28] = new JButton() {
			{setIcon(new ImageIcon(overallIcon));}
			{setSize(overallIcon.getWidth(this), overallIcon.getHeight(this));}
			{setOpaque(false);}
			{setBorderPainted(false);}
			{setContentAreaFilled(false);}
			{setToolTipText("<html>" +
					"<center>Overall</center>" + 
					"Rank: "+ data.get(0) + "<br/>" +
					"Level: "+ data.get(1) + "<br/>" +
					"Xp: "+ data.get(2).split("\n")[0] +
					"</html>");}
			{setLocation(100, 400);}
			{setFocusPainted(false);}
			{setVisible(true);}
		};
		add(skillButtons[28]);
		final Image minigameIcon = Toolkit.getDefaultToolkit().getImage(new URL(miscMinigamePage));
		skillButtons[29] = new JButton() {
			{setIcon(new ImageIcon(minigameIcon));}
			{setSize(minigameIcon.getWidth(this), minigameIcon.getHeight(this));}
			{setOpaque(false);}
			{setContentAreaFilled(false);}
			{setToolTipText("<html>" +
					"<center>Minigame Ranks</center>" + 
					"Duel Tournament:" + data.get(52).split("\n")[1] + "<br/>" +
					"Bounty Hunters: "+ data.get(53).split("\n")[1] +"<br/>" +
					"Bounty Hunter Rogues: "+ data.get(54).split("\n")[1] +"<br/>" +
					"Conquest: " + data.get(62).split("\n")[1] + "<br/>" +
					"Dominion Tower: "+ data.get(63).split("\n")[1] + "<br/>" + 
					"</html>");};
			{setLocation(10, 438);}
			{setFocusPainted(false);}
		
			@Override
			public Point getToolTipLocation(MouseEvent event) {
				return new Point(event.getX(), event.getY() - 40);
			}
		};
		add(skillButtons[29]);
		skillButtons[29].setVisible(true);
		final Image gameIcon = Toolkit.getDefaultToolkit().getImage(new URL(gameMiniPage));
		skillButtons[30] = new JButton() {
			{setIcon(new ImageIcon(gameIcon));}
			{setSize(gameIcon.getWidth(this), gameIcon.getHeight(this));};
			{setOpaque(false);}
			{setContentAreaFilled(false);}
			{setBorderPainted(false);}
			{setToolTipText("<html>" +
					"<center>Minigame Ranks</center>" +  
					"Fist of Guthix: "+ data.get(55).split("\n")[1] +"<br/>" +
					"Mobilising Armies: "+ data.get(56).split("\n")[1] +"<br/>" +
					"B.A Attackers: "+ data.get(57).split("\n")[1] +"<br/>" +
					"B.A Defenders: "+ data.get(58).split("\n")[1] +"<br/>" +
					"B.A Collectors: "+ data.get(59).split("\n")[1] +"<br/>" +
					"B.A Healers: "+ data.get(60).split("\n")[1] +"<br/>" + 
					"Castle Wars Games: "+ data.get(61).split("\n")[1] +"<br/>" +
					"</html>");}
			{setLocation(100, 438);}
			{setFocusPainted(false);}
			
			@Override
			public Point getToolTipLocation(MouseEvent event) {
				return new Point(event.getX() - 110, event.getY() - 140);
			}
		};
		add(skillButtons[30]);
		skillButtons[30].setVisible(true);
		
	}
	
	private HashMap<Integer, String> data;
	
	public void drawLevelInformation(Graphics g) throws MalformedURLException, IOException {
		int baseX = 50, baseY = 60;
		for(int skillIndex = 3; skillIndex < 52; skillIndex += 2) {
			g.setColor(Color.WHITE);
			if(skillIndex == 29) {
				baseX = 140;
				baseY = 60;
			}
			g.drawString(getSpecialString(data.get(skillIndex)).getIterator(), baseX, baseY);
			baseY += 30;
		}
		g.drawString(getSpecialString(data.get(1)).getIterator(), 140, baseY);
	}
	
	public AttributedString getSpecialString(String data) {
		AttributedString special = new AttributedString(data);
		special.addAttribute(TextAttribute.FOREGROUND, Color.WHITE);
		special.addAttribute(TextAttribute.SIZE, 14);
		return special;
	}
	
	public HashMap<Integer, String> getHighScoreData()  {
		try {
			HashMap<Integer, String> scoreData = new HashMap<Integer, String>();
			HttpURLConnection connection = (HttpURLConnection) new URL(defaultPage + usernameField.getText().toString()).openConnection();
			connection.setRequestMethod("GET");
			InputStream input = connection.getInputStream();
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			int bytesRead = 0;
			while((bytesRead = input.read()) != -1) {
				output.write(bytesRead);
			}
			output.close();
			input.close();
			String pageSource = String.valueOf(output.toString());
			String[] userData = pageSource.split(",");
			int index = 0;
			for(String piece : userData) {
				scoreData.put(index, piece);
				index++;
			}
			initalized = true;
			connection.disconnect();
			return scoreData;	
		} catch(Exception e) {
			initalized = false;
		}
		return null;
	}
	
	public String getRank(int skillNumber) {
		return data.get(2 * skillNumber).split("\n")[1];
	}
	
	public String getLevel(int skillNumber) {
		if(skillNumber == 1)
			return data.get(3 * skillNumber);
		else
			return data.get(1 + (2 * skillNumber));
	}
	
	public String getExperience(int skillNumber) {
		if(skillNumber == 1) 
			return data.get(4).split("\n")[0];
		else
			return data.get(2 * (skillNumber + 1)).split("\n")[0];
	}
	
}
