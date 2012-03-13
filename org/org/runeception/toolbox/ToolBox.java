package org.runeception.toolbox;

import java.awt.Component;
import java.awt.Dimension;
import java.util.HashMap;

import javax.swing.JTabbedPane;

@SuppressWarnings("serial")
public class ToolBox extends JTabbedPane {

	
	private static HashMap<Integer, Component> tools = new HashMap<Integer, Component>();
	
	public static HashMap<Integer, Component> getTools() {
		return tools;
	}
	
	public static int toolIndex = 0;

	public ToolBox() {
		setSize(new Dimension(300, 503));
	}
	
	public void addTool(String name, Component component) {
		addTab(name, component);
		setTabComponentAt(getTabCount() - 1, new TabButton(name, this));
		tools.put(indexOfComponent(component), component);
	}
	
	public void update() {
		for(int index = 0; index < tools.size(); index++) {
			setTabComponentAt(index, new TabButton(tools.get(index).getName(), this));
		}
	}

	public JTabbedPane getToolPane() {
		return this;
	}
	
}
