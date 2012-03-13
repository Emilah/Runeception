package org.runeception.client.plugins.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.runeception.utilities.Decompressor;

public class RCUpdate {
	
	public static void main(String[] args) throws MalformedURLException, IOException {
		new RCUpdate();
	}
	
	
	public RCUpdate() throws MalformedURLException, IOException {
		checkForUpdate();
	} 
	
	public void checkForUpdate() throws MalformedURLException, IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(new URL("http://runeception.netne.net/version.html").openStream()));
		String line = reader.readLine();
		int version = Integer.valueOf(line);
		File versionFile = new File(System.getProperty("user.home") + "//Runeception/cv.rah");
		if(!versionFile.exists()) {
			FileWriter writer = new FileWriter(versionFile);
			writer.write(line);
			writer.flush();
			writer.close();
			downloadLatestClient();
		}
		BufferedReader read = new BufferedReader(new FileReader(versionFile));
		int lastVersion = Integer.valueOf(read.readLine());
		if(lastVersion != version) {
			FileWriter writer = new FileWriter(versionFile);
			writer.write(line);
			writer.flush();
			writer.close();
			downloadLatestClient();
		}
	}
	
	public void downloadLatestClient() throws MalformedURLException, IOException {
		URLConnection connection = new URL("http://runeception.netne.net/Runeception.jar").openConnection();
		int fileLength = connection.getContentLength();
		InputStream in = connection.getInputStream();
		FileOutputStream out = new FileOutputStream(System.getProperty("user.home") + "//Runeception/Runeception.jar");
		byte[] buffer = new byte[fileLength];
		for(int bytesRead = 0; (bytesRead = in.read(buffer)) != -1;) {
			out.write(buffer, 0, bytesRead);
		}
		in.close();
		out.close();
		fetchCacheFiles();
	}
	
	public void fetchCacheFiles() throws MalformedURLException, IOException {
		URLConnection connection = new URL("http://runeception.netne.net/gcache.zip").openConnection();
		int fileLength = connection.getContentLength();
		InputStream in = connection.getInputStream();
		FileOutputStream out = new FileOutputStream(System.getProperty("user.home") + "//Runeception/cache/gcache.zip");
		byte[] buffer = new byte[fileLength];
		for(int bytesRead = 0; (bytesRead = in.read(buffer)) != -1;) {
			out.write(buffer, 0, bytesRead);
		}
		in.close();
		out.close();
		new Decompressor().decompress(System.getProperty("user.home") + "//Runeception/cache/gcache.zip", 
				System.getProperty("user.home") + "//Runeception/cache/", true);
	}
	
	

}
