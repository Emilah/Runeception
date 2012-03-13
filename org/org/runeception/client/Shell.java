package org.runeception.client;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JPanel;

import org.runeception.Boot;

@SuppressWarnings("serial")
public class Shell extends JPanel {
	
	public Shell(Boot boot, int screenX, int screenY) {
		applet = boot.getApplet();
		applet.setSize(screenX - 20, screenY - 130);
		add(applet);
		setSize(new Dimension(screenX - 20, screenY - 130));
		setBackground(Color.BLACK);
	}
	
	public static Component getApplet() {
		return applet;
	}
	
	public static void setAppletSize(int x) {
		applet.setSize(x - 20, applet.getHeight());
	}
	
	private static Component applet;
	
}
