package org.runeception.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import org.runeception.Boot;

/**
 * Fetches & Generates Current Protocol Version of Runescape
 * @author Emily (emilah@live.com)
 * @version 1.0.1
 *
 */
public class RSUpdater implements Runnable {
	 
	public RSUpdater() {
		File params = new File(System.getProperty("user.home") + "//Runeception/params.txt");
		if(!params.exists()) {
			try {
				Boot.generateParameters();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static boolean fetchLocalVersion() {
		logger.info("Looking for Version file...");
		if(version.exists()) {
			try {
				logger.info("File found... reading version");
				fetchCurrentVersion();
				BufferedReader reader = new BufferedReader(new FileReader(version));
				String versionData = reader.readLine();
				if(Integer.valueOf(versionData.split("_")[0]) != revision ||
						Integer.valueOf(versionData.split("_")[1]) != subVersion) {
					generateLocalVersion(revision, subVersion);
					return true;
				} else
					return false;
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else {
			try {
				logger.info("Could not find version... grabbing current...");
				fetchCurrentVersion();
				generateLocalVersion(revision, subVersion);
				return false;
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} 
		return false;
	}
	
	public static String fetchEncryptionKey() {
		File params = new File(System.getProperty("user.home") + "//Runeception/params.txt");
		try {
			BufferedReader reader = new BufferedReader(new FileReader(params));
			String line;
			while((line = reader.readLine()) != null) {
				if(line.startsWith("11<value>")) {
					return encryptionKey = line.split("11<value>")[1];
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static int fetchLastRevision() {
		if(version.exists()) {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(version));
				return Integer.valueOf(reader.readLine().split("_")[0]);
			} catch (FileNotFoundException e) {
				return 709;
			} catch (NumberFormatException e) {
				return 709;
			} catch (IOException e) {
				return 709;
			}
		}
		return 709;
	}
	
	public static boolean fetchCurrentVersion() throws UnknownHostException, IOException {
		fetchEncryptionKey();
		while(true) {
			for(int version = fetchLastRevision(); version < 900; version++) {
				for(int subRevision = 1; subRevision < 5; subRevision++) {
					Socket socket = new Socket("world1.runescape.com", 43594);
					DataInputStream input = new DataInputStream(socket.getInputStream());
					DataOutputStream output = new DataOutputStream(socket.getOutputStream());
					output.writeByte(15);
					output.writeByte(encryptionKey.length() + 9);
					output.writeInt(version);
					output.writeInt(subRevision);
					output.write(encryptionKey.getBytes());
					output.write(10);
					int returnCode = input.readByte();
					if(returnCode == 48) {
						revision = version;
						subVersion = subRevision;
						logger.info("Version: "+version+"_"+subRevision);
						return false;
					} else {
						logger.info("Trying[SV]: "+version+"_"+subRevision);
					}
					input.close();
					output.close();
					socket.close();
				}
				logger.info("Trying[MV]: "+(version+1)+"_1");
			}		
		}
	}
	
	public static void generateLocalVersion(int revision, int subVersion) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(version));
			writer.write(revision+"_"+subVersion);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void run() {
		fetchLocalVersion();
	}
	
	static Logger logger = Logger.getLogger(RSUpdater.class.getName());
	
	private static File version = new File(System.getProperty("user.home") + "//Runeception/version.txt");
	
	private static int revision = 0;
	
	private static int subVersion = 0;
	
	private static String encryptionKey;
	
}
