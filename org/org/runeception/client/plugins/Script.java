package org.runeception.client.plugins;

import java.awt.Color;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;

public abstract class Script {

	static Logger logger = Logger.getLogger(Script.class.getName());
	
	public abstract String getAuthor();
	
	public abstract URL getHtmlURL();
	
	public static void initalize() {
		scriptPane = new JTextPane();
		scriptPane.setBackground(Color.GRAY);
		scrollPane = new JScrollPane(scriptPane);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		try {
			scriptPane.setPage("http://runeception.netne.net/test.html");
		} catch (IOException e) {
			e.printStackTrace();
		}
		scriptPane.setPreferredSize(new Dimension(300, 503));
		scrollPane.setPreferredSize(new Dimension(300, 503));
	}
	
	private static JTextPane scriptPane;
	
	private static JScrollPane scrollPane;
	
	public static JScrollPane getScrollPane() {
		return scrollPane;
	}
	
	public static JTextPane getScriptPane() {
		return scriptPane;
	}
	
	public ArrayList<String> fetchScripts() throws IOException {
		ArrayList<String> pageSource = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(new URL("http://runeception.netne.net/test.html").openStream()));
		String line;
		while((line = reader.readLine()) != null)
			pageSource.add(line);
		reader.close();
		return pageSource;
	}
	
}
