package org.runeception.client;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.runeception.Boot;
import org.runeception.client.audio.MusicPlayer;
import org.runeception.client.plugins.Script;
import org.runeception.client.plugins.impl.Calculator;
import org.runeception.client.plugins.impl.ExperienceCalc;
import org.runeception.client.plugins.impl.HighScore;
import org.runeception.toolbox.ToolBox;

@SuppressWarnings("serial")
public class RSClient extends JFrame {
	
	public RSClient(final Boot boot) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, MalformedURLException {
		super("Runeception");
		toolbox = new ToolBox();
		GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice device = environment.getDefaultScreenDevice();
		screenWidth = device.getDisplayMode().getWidth();
		screenHeight = device.getDisplayMode().getHeight();
		localInstance = this; 
		setIconImage(Toolkit.getDefaultToolkit().getImage(System.getProperty("user.home") +"//Runeception/cache/icon.png"));
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		menuBar = new JMenuBar();
		toolBar = new JToolBar();
		fileMenu = new JMenu("File");
		pluginMenu = new JMenu("Plugins");
		utilityMenu = new JMenu("Utilities");
		shutDownItem = new JMenuItem("Shut Down");
		shutDownItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(-1);
			}
		});
		highScoreItem = new JMenuItem("HighScores");
		highScoreItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
					if(!toolbox.isVisible())
						toolbox.getToolPane().setVisible(true);
					toolbox.addTool("HighScores", new HighScore());
					client.setSize(screenWidth - (toolbox.getTabCount() > 0 ? 300 : 0), client.getHeight());
					Shell.setAppletSize(client.getWidth());
			}
		});
		calculatorItem = new JMenuItem("Calculator");
		calculatorItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!toolbox.isVisible())
					toolbox.getToolPane().setVisible(true);
				toolbox.addTool("Calculator", new Calculator());
				client.setSize(screenWidth - (toolbox.getTabCount() > 0 ? 300 : 0), client.getHeight());
				Shell.setAppletSize(client.getWidth());
			}
		});
		experienceCalcItem = new JMenuItem("Skill Calculator");
		experienceCalcItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!toolbox.isVisible())
					toolbox.getToolPane().setVisible(true);
				toolbox.addTool("Skill Calc", new ExperienceCalc());
				client.setSize(screenWidth - (toolbox.getTabCount() > 0 ? 300 : 0), client.getHeight());
				Shell.setAppletSize(client.getWidth());
			}
		});
		guideItem = new JMenuItem("Guides");
		guideItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!toolbox.isVisible())
					toolbox.getToolPane().setVisible(true);
				Script.initalize();
				toolbox.addTool("Scripts", Script.getScriptPane());
				client.setSize(screenWidth - (toolbox.getTabCount() > 0 ? 300 : 0), client.getHeight());
				Shell.setAppletSize(client.getWidth());
			}
		});
		
		musicPlayerItem = new JMenuItem("Music Player");
		musicPlayerItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new MusicPlayer().fetchDirectory(localInstance);
			}
		});
		utilityMenu.add(musicPlayerItem);
		utilityMenu.add(calculatorItem);
		pluginMenu.add(highScoreItem);
		pluginMenu.add(guideItem);
		pluginMenu.add(experienceCalcItem);
		fileMenu.add(shutDownItem);
		menuBar.add(fileMenu);
		menuBar.add(pluginMenu);
		menuBar.add(utilityMenu);
		setJMenuBar(menuBar);
		toolBar.setMaximumSize(toolBar.getMaximumSize());
		add(toolBar, BorderLayout.NORTH);
		add(toolbox, BorderLayout.EAST);
		
		client = new Shell(boot, screenWidth, screenHeight);
		add(client, BorderLayout.WEST);
		toolbox.getToolPane().setBounds(client.getX() + client.getWidth() + 1, 0, 200, 503);
		setMinimumSize(client.getSize());
		pack();
	}
	
	public void init() {
		setResizable(true);
		setVisible(true);
	}
	
	public static Component getClient() {
		return client;
	}
	
	private JMenuBar menuBar;
	
	public static JToolBar toolBar;
	
	private JMenu fileMenu;

	private JMenu pluginMenu;
	private JMenuItem shutDownItem;
	private JMenuItem highScoreItem;
	private JMenuItem calculatorItem;
	private JMenuItem guideItem;
	private JMenuItem experienceCalcItem;

	private JMenu utilityMenu;
	private JMenuItem musicPlayerItem;
	
	private static Component client;

	private JFrame localInstance;
	
	private ToolBox toolbox;
	
	public static int screenWidth = 0;
	public static int screenHeight = 0;
	
}
