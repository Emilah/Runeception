package org.runeception.client.audio;

import java.net.URL;

public class AudioClip {
	
	public AudioClip(String fileName, URL path) {
		this.fileName = fileName;
		this.path = path;
	}
	
	public String getName() {
		return fileName;
	}
	
	public URL getPath() {
		return path;
	}
	
	private String fileName;
	
	private URL path;
	
	
}
