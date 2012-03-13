package org.runeception.toolbox;

import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.runeception.client.RSClient;
import org.runeception.client.Shell;

@SuppressWarnings("serial")
public class TabButton extends JPanel implements ActionListener {

	private JTabbedPane parent;
	
	public TabButton(String title, JTabbedPane parent){
		super(new FlowLayout());
		add(new JLabel(title));
		JButton button = new JButton();
		button.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(System.getProperty("user.home") + "//Runeception/cache/exitIcon.png")));
		button.addActionListener(this);
		button.setMargin(new Insets(0, 0, 0, 0));
		button.setRolloverEnabled(true);
		button.setFocusable(false);
		button.setBorderPainted(false);
		
		add(button);
		this.parent = parent;
	}

	public void actionPerformed(ActionEvent e) {
		int tabIndex = parent.indexOfTabComponent(this);
		if(tabIndex != -1)
			parent.remove(tabIndex);
		if(parent.getTabCount() == 0) {
			RSClient.getClient().setSize(RSClient.screenWidth - 20, RSClient.screenHeight - 130);
			Shell.getApplet().setSize(RSClient.getClient().getWidth(), RSClient.getClient().getHeight());
			parent.setVisible(false);
		}
	}
	
}