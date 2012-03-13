package org.runeception.client.audio;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.FactoryRegistry;

import org.runeception.client.RSClient;

public class MusicPlayer extends PlaybackListener {
	
	public MusicPlayer() {
		playBackListener = this;
	}
	
	public void fetchDirectory(JFrame parent) {
		JFileChooser fileChooser = new JFileChooser(new File(System.getProperty("user.home") + "//Music/Itunes/"));
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int returnValue = fileChooser.showOpenDialog(parent);
		if(returnValue == JFileChooser.APPROVE_OPTION) {
			itunesFile = fileChooser.getSelectedFile();
			generateButtons();
			new Thread(new Runnable() {
				@Override
				public void run() {
					fetchItunesFile();
				}
			}).start();
		}
	}
	
	private void fetchItunesFile() {
		System.out.println(itunesFile);
		if(!itunesFile.getName().equalsIgnoreCase("iTunes Music Library.xml")) {
			loadOtherFile();
		} 
		try {
			BufferedReader reader = new BufferedReader(new FileReader(itunesFile));
			String line = "";
			String location = null;
			while(audioFiles.size() < 100) {
				 line = reader.readLine();
				 if(line == null)
					 return;
				if(line.contains("file://localhost/") && line.contains(".mp3")) {
					for(int i = 0; i < 6; i++)
						reader.readLine();
					location = new String(line.split("file://localhost/")[1].split("</string>")[0]).replace("%20", " ").replace("%5D", "]").replace("%5B", "[").replace("#38;", "");
					audioFiles.put(audioFiles.size(), new AudioClip(reader.readLine().split("<string>")[1].split("</string>")[0], new File(location).toURI().toURL()));
				}				
			}
			logger.info("Loaded #"+audioFiles.size()+" usable itunes audio files...");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadOtherFile() {
		File files = new File(itunesFile.getPath().replace(itunesFile.getName(), ""));
		for(File file : files.listFiles()) {
			try {
				audioFiles.put(audioFiles.size(), new AudioClip(file.getName(), file.toURI().toURL()));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		logger.info("Loaded #"+audioFiles.size()+" usable audio files...");
	}
	
	@SuppressWarnings("serial")
	public void generateButtons() {
		JButton playButton = new JButton() {
			{setIcon(new ImageIcon(getImage("playIcon.png")));}
			{addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(paused)
						paused = false;
					appendPlayer();
				}
			});}
			{setSize(16, 16);}
		};
		
		JButton pauseButton = new JButton() {
			{setIcon(new ImageIcon(getImage("pauseIcon.png")));}
			{addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					paused = true;
					player.stop();
				}
			});};
		};
		
		JButton stopButton = new JButton() {
			{setIcon(new ImageIcon(getImage("stopIcon.png")));}
			{addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					player.stop();
				}
			});}
			
		};
		
		JButton soundButton = new JButton();
		soundButton.setIcon(new ImageIcon(getImage("soundIcon.png")));
		
		JButton forwardButton = new JButton() {
			{setIcon(new ImageIcon(getImage("forwardIcon.png")));}
			{addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
				player.stop();
				appendPlayer();
				}
			});};
		};
		
		JButton backwardButton = new JButton() {
			{setIcon(new ImageIcon(getImage("backwardIcon.png")));}
			{addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
				player.stop();
				audioIndex -= 2;
				appendPlayer();
				}
			});}
		};
		
		currentSong = new JLabel("No Music Playing");
		
		RSClient.toolBar.add(Box.createHorizontalGlue());
	
		RSClient.toolBar.add(currentSong, BorderLayout.WEST);
		RSClient.toolBar.add(stopButton);
		RSClient.toolBar.add(backwardButton);
		RSClient.toolBar.add(pauseButton);
		RSClient.toolBar.add(playButton);
		RSClient.toolBar.add(forwardButton);
		
		pauseButton.setVisible(true);
		playButton.setVisible(true);
		stopButton.setVisible(true);
		forwardButton.setVisible(true);
		backwardButton.setVisible(true);
		
		RSClient.toolBar.repaint();
	}
	
	public void appendPlayer() {
		soundThread = new Thread(new Runnable() {
			@Override
			public void run() {
			try {
				while(!paused) {
					if(audioIndex >= audioFiles.size())
						audioIndex = 0;
					if(audioIndex < 0)
						audioIndex = audioFiles.size();
					player = new AdvancedPlayer(audioFiles.get(audioIndex).getPath().openStream(), FactoryRegistry.systemRegistry().createAudioDevice());
					if(wasPaused) {
						if(audioIndex >= 1 && audioIndex < audioFiles.size())
							audioIndex -= 1;
						else
							audioIndex += 1;
					}
					player.setStream(audioFiles.get(audioIndex).getPath().openStream());
					currentSong.setText("Now Playing: "+audioFiles.get(audioIndex).getName().replace(".mp3", ""));
					logger.info("Now Playing: "+audioFiles.get(audioIndex).getName());
					player.setPlayBackListener(playBackListener);
					if(wasPaused) {
						player.play(Integer.MAX_VALUE - lastPosition);
						lastPosition = 0;
						wasPaused = false;
					} else
						player.play();
					audioIndex++;
				}
			} catch (JavaLayerException | IOException e1) {
				e1.printStackTrace();
				}
			}
		});
		soundThread.start();
	}
	
	public Image getImage(String fileName) {
		return Toolkit.getDefaultToolkit().getImage(System.getProperty("user.home") + "//runeception/cache/" + fileName);
	}
	
	private JLabel currentSong;
	
	private PlaybackListener playBackListener;
	
	private HashMap<Integer, AudioClip> audioFiles = new HashMap<Integer, AudioClip>();
	
	private int audioIndex = 0;
	
	public File itunesFile;

	private Logger logger = Logger.getLogger(MusicPlayer.class.getName());
	
	private AdvancedPlayer player;
	
	private Thread soundThread;
	
	public static int lastPosition = 0;
	
	public static boolean paused = false;
	
	public static boolean wasPaused = false;
	
}
