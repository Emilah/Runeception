package org.runeception.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JProgressBar;

@SuppressWarnings("serial")
public class RSSplash extends Window {
	
	public static void main(String[] args) {
		JFrame splash = new JFrame();
		splash.setLayout(null);
		splash.setAlwaysOnTop(true);
		new RSSplash(splash);
	}
	
	public RSSplash(final Frame frame) {
		super(frame);
		frame.addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent e) {
				setVisible(false);
				dispose();
			}
		});
		MediaTracker tracker = new MediaTracker(this);
		tracker.addImage(splashImage, 0);
		try {
			tracker.waitForAll();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		setLayout(null);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(((screenSize.width - splashImage.getWidth(this)) / 2),
				((screenSize.height - splashImage.getHeight(this)) / 2),
				splashImage.getWidth(this), splashImage.getHeight(this));
		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setValue(0);
		progressBar.setBackground(Color.WHITE);
		progressBar.setForeground(Color.CYAN);
		progressBar.setDoubleBuffered(true);
		progressBar.setBounds(28, 172, 390, 30);
		add(progressBar);
		setVisible(true);
		update = new Thread(new Runnable() {
			@Override
			public void run() {
				while(isLoading) {
					progressBar.setValue(loadPercentage);
					progressBar.setString(loadingText);
					try {
						Thread.sleep(600);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} 
				setVisible(false);
			}
		});
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(splashImage, 0, 0, this);
		if(!update.isAlive())
			update.start();
	}
	
	public static String setLoadText(String text) {
		return loadingText = text;
	}
	
	public static boolean setLoading(boolean state) {
		return isLoading = state;
	}

	public static void setLoadPercentage(int i) {
		progressBar.setValue(i);
		
	}

	private Thread update;
	
	private Image splashImage = Toolkit.getDefaultToolkit().getImage(System.getProperty("user.home") +"//Runeception/cache/splashscreen.png");
	
	private static JProgressBar progressBar;
	
	public static boolean isLoading = true;
	
	public static String loadingText = "Runeception is starting up...";
	
	public static int loadPercentage = 0;
}
