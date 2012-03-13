package org.runeception;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.UnsupportedLookAndFeelException;

import org.runeception.client.RSSplash;
import org.runeception.client.plugins.impl.RCUpdate;

/**
 * The Main Executable Class
 * @author Emily Perkins (emilah@live.com)
 * @version 1.1.3
 *
 */
public class Execute {
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, UnsupportedLookAndFeelException {
		logger.info(RSSplash.loadingText);
		@SuppressWarnings("serial")
		JFrame splash = new JFrame() {
			{setLayout(null);}
			{setAlwaysOnTop(true);}
		};
		new RSSplash(splash);
		new Boot();
		new RCUpdate();
	}

	static Logger logger = Logger.getLogger(Execute.class.getName());

}
