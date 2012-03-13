package org.runeception.client.plugins;

import java.io.File;
import java.net.URI;

@SuppressWarnings("serial")
public abstract class Plugin extends File {
	
	public Plugin(URI uri) {
		super(uri);
	} 

	public abstract String getPluginName();
	
	public abstract String getAuthor();
	
	public abstract String getName();
	

}
