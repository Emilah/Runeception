package org.runeception;

import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.swing.UnsupportedLookAndFeelException;

import org.runeception.client.RSClient;
import org.runeception.client.RSSplash;
import org.runeception.client.RSUpdater;
import org.runeception.utilities.Decompressor;

/**
 * Runescape Client Booter
 * Fetches, Downloads, and Initializes
 * @author Emily (emilah@live.com)
 * @since 2/21/2012
 * @version 1.1.3
 *
 */
public class Boot {
	/**
	 * Boot Constructor
	 * Checks for needed updates, missing time stamp, and initializes as needed
	 * @throws IOException
	 * @throws ClassNotFoundException Class File Not Found
	 * @throws InstantiationException Invalid Instantiation
	 * @throws IllegalAccessException Illegal Access
	 * @throws IllegalArgumentException Illegal Argument Values
	 * @throws InvocationTargetException Invocation Target Invalid
	 * @throws NoSuchMethodException No Method Available
	 * @throws SecurityException Security Issue
	 * @throws UnsupportedLookAndFeelException Unsupported Look and Feel
	 */
	public Boot() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, UnsupportedLookAndFeelException {
		File directory = new File(System.getProperty("user.home") + "//Runeception/");
		directory.mkdir();
		File pluginDirectory = new File(System.getProperty("user.home") + "//Runeception/plugins/");
		pluginDirectory.mkdir();
		File params = new File(System.getProperty("user.home") + "//Runeception/params.txt");
		if(!params.exists()) {
			try {
				Boot.generateParameters();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(needsUpdate()) {
			RSSplash.setLoadText("Generating Game Pack...");
			logger.info("Generating Game Pack...");
			fetchGamePack(loader);
			generateParameters();
		}
		if(!timeStamp.exists())
			generateTimeStamp();
		File cacheDirectory = new File(System.getProperty("user.home") + "//Runeception/cache/");
		if(!cacheDirectory.exists()) {
			cacheDirectory.mkdir();
			fetchCacheFiles();
		}
		RSSplash.setLoadPercentage(20);
		logger.info("Fetching client parameters...");
		fetchParameters();
		RSSplash.setLoadPercentage(40);
		RSSplash.setLoadText("Initalizing Client...");
		logger.info("Initalizing Client...");
		initalizeClient();
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
	
	
	/**
	 * Checks for Updates dealing with both Runeception and Runescape
	 * @return Needs Update
	 * @throws IOException Invalid Reader Issue
	 */
	public boolean needsUpdate() throws IOException {
		RSSplash.setLoadText("Checking for updates...");
		logger.info("Checking for updates...");
		if(RSUpdater.fetchLocalVersion())
			return true;
		if(!timeStamp.exists()) {
			generateTimeStamp();
			return true; 
		} else {
			BufferedReader reader = new BufferedReader(new FileReader(timeStamp));
			String[] date = reader.readLine().split(":");
			Calendar stamp = Calendar.getInstance();
			Calendar oldStamp = Calendar.getInstance();
			oldStamp.set(Integer.valueOf(date[0]), Integer.valueOf(date[1]), Integer.valueOf(date[2]),
					Integer.valueOf(date[3]), Integer.valueOf(date[4]));
			if(oldStamp.get(Calendar.DAY_OF_MONTH) != stamp.get(Calendar.DAY_OF_MONTH))
				return true;
			if(oldStamp.get(Calendar.MONTH) != stamp.get(Calendar.MONTH))
				return true;
			if(oldStamp.get(Calendar.HOUR) + 6 < stamp.get(Calendar.HOUR))
				return true;
		}
		if(!loader.exists())
			return true;
		return false;
	}
	/**
	 * Generates Time Stamp used for Bypassing Runescape Time out Keys
	 * @throws IOException Invalid Writer Issue
	 */
	public static void generateTimeStamp() throws IOException {
		RSSplash.setLoadText("Generating Time Stamp File...");
		logger.info("Generating Time Stamp file...");
		BufferedWriter writer = new BufferedWriter(new FileWriter(timeStamp));
		Calendar current = Calendar.getInstance();
		writer.write(current.get(Calendar.YEAR)+":"+current.get(Calendar.MONTH)+":"+current.get(Calendar.DATE)+":"+
				current.get(Calendar.HOUR_OF_DAY)+":"+current.get(Calendar.MINUTE));
		writer.flush();
		writer.close();
	}
	/**
	 * Generates Parameters passed into Applet from Page Source
	 * @throws IOException Invalid Writer Issue
	 */
	public static void generateParameters() throws IOException {
		RSSplash.setLoadText("Generating Client Parameters...");
		logger.info("Generating client parameters...");
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(System.getProperty("user.home") + "//Runeception/params.txt")));
		for(String line : fetchPageDetails()) {
			if(line.contains("<param name=")) {
				String key = line.split("<param name=\"")[1].split("\" ")[0];
				String value = line.split("value=\"")[1].split("\">'")[0];
				if(value.isEmpty())
					value = " ";
				writer.write(key+"<value>"+value);
				writer.newLine();
			}
		}
		writer.close();
	}
	
	public String getActivationKey() {
		return new StringBuilder(parameters.get("0")).toString();
	}
	
	public String getConfirmationKey() {
		return new StringBuilder(parameters.get("-1")).toString();
	}
	
	public void fetchParameters() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(new File(System.getProperty("user.home") + "//Runeception/params.txt")));
		String detail;
		while((detail = reader.readLine()) != null) {
			parameters.put(detail.split("<value>")[0], detail.split("<value>")[1]);
		}
		reader.close();
	}
	
	public void initalizeClient() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, UnsupportedLookAndFeelException {
		RSSplash.setLoadText("Fetching Runescape Game Pack...");
		logger.info("Fetching Runescape Game Pack...");
		URLClassLoader classLoader = new URLClassLoader(new URL[] {new File(System.getProperty("user.home") + "//Runeception/loader.jar").toURI().toURL()});
		RSSplash.setLoadText("Loading RS2Applet class instance...");
		client = (Applet) classLoader.loadClass("Rs2Applet").newInstance();
		logger.info("Loaded RS2Applet class instance...");
		client.setBackground(Color.BLACK);
		client.setStub(new AppletStub() {
			@Override
			public void appletResize(int width, int height) {
				
			}

			@Override
			public AppletContext getAppletContext() {
				return null;
			}
			
			@Override
			public URL getCodeBase() {
				try {
					return new URL(worldPage);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public URL getDocumentBase() {
				try {
					return new URL(worldPage);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public String getParameter(String name) {
				return parameters.get(name);
			}
			
			@Override
			public boolean isActive() {
				return true;
			}
				
		});
		RSSplash.setLoadText("Initalizing Game Loader Applet...");
		logger.info("Initalizing Loader Applet...");
		RSSplash.setLoadPercentage(60);
		client.init();
		RSSplash.setLoadText("Starting Game Loader Applet...");
		logger.info("Starting Loader Applet...");
		RSSplash.setLoadPercentage(80);
		client.start();
		client.setSize(765, 503);
		RSSplash.setLoadText("Finishing up - Loading Runeception Frame...");
		logger.info("Initalizing Loader Frame...");
		RSSplash.setLoadPercentage(100);
		RSSplash.setLoading(false);
		new RSClient(this).init();
	}
	
	public void fetchGamePack(File loader) throws IOException {
		String fileLocation = fetchPageSource().split("archive=")[1].split(" ")[0];
		BufferedInputStream input = new BufferedInputStream(new URL(worldPage+fileLocation).openStream());
		BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(loader));
		int bytesRead = 0;
		while((bytesRead = input.read()) != -1) {
			output.write(bytesRead);
		}
		RSSplash.setLoadText("Fetching Game Pack Files...");
		logger.info("Grabbed Game Pack files...");
		input.close();
		output.close();
	}
	
	public static ArrayList<String> fetchPageDetails() throws MalformedURLException, IOException {
		ArrayList<String> pageSource = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(worldPage).openStream()));
		String line;
		while((line = reader.readLine()) != null)
			pageSource.add(line);
		reader.close();
		return pageSource;
	}
	
	public String fetchPageSource() throws IOException {
		URL website = new URL(worldPage);
		HttpURLConnection connection = (HttpURLConnection) website.openConnection();
		connection.setRequestMethod("GET");
		InputStream input = connection.getInputStream();
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		byte[] pageBuffer = new byte[1024];
		int bytesRead = 0;
		while((bytesRead = input.read(pageBuffer)) != -1)
			output.write(pageBuffer, 0, bytesRead);
		input.close();
		output.close();
		return new StringBuilder().append(output).toString();
	}
	
	public Applet getApplet() {
		return client;
	}
	
	private static Logger logger = Logger.getLogger(Boot.class.getName());
	
	private Map<String, String> parameters = new HashMap<String, String>();
	
	private Applet client;
	
	private static String worldPage = "http://world13.runescape.com/";
	
	static File timeStamp =  new File(System.getProperty("user.home") + "//Runeception/timestamp.txt");
	
	static File loader = new File(System.getProperty("user.home") + "//Runeception/loader.jar");
	
}
