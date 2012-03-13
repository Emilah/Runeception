package org.runeception.client.plugins;

import java.awt.MenuItem;
import java.io.File;
import java.util.HashMap;
import java.util.logging.Logger;

import org.runeception.client.components.ComponentManager;

public class PluginManager {
	
	private static Logger logger = Logger.getLogger(PluginManager.class.getName());

	public static HashMap<String, File> plugins = new HashMap<String, File>();
	
	public static void fetchAllPlugins() {
		File availablePlugins = new File(System.getProperty("user.home") + "//runeception/plugins/");
		for(File file : availablePlugins.listFiles()) {
			ComponentManager.menuItems.put(ComponentManager.menuItems.size(), new MenuItem(file.getName().replace(".replug", "")));
			plugins.put(file.getName().replace(".replug", ""), file);
		}
		logger.info("Fetched #"+plugins.size()+" plugins for usage...");
		updateMenuItemListing();
	}
	
	public static void updateMenuItemListing() {
		/*for(MenuItem item : ComponentManager.menuItems.values()) {
			Shell.pluginMenu.add(item);
		}*/
		logger.info("Updated Menu Item Listing...");
	}
	
	public static boolean usingToolBox = false;
	
	public static boolean usingHighscore = false;
}
