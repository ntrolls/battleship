/**
 * 
 */
package com.eucman.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextPane;

/**
 * @author ntrolls
 *
 */
@SuppressWarnings("serial")
public class ResultFrame extends JDialog implements ActionListener
{
	private String results = null;
	public ResultFrame(String results)
	{
		super();
		
		this.results = results;
		
		setSize(640, 480);
		setLayout(new GridLayout(3, 1));
		
		add(new JLabel("Results"), 0);
		
		JTextPane text = new JTextPane();
		text.setText(this.results);
		add(text, 1);
		
		JButton close = new JButton("Close");
		close.setActionCommand("close");
		close.addActionListener(this);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		if(arg0.getActionCommand().equals("close"))
			this.setVisible(false);
	}
}
