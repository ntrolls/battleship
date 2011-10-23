/**
 * 
 */
package com.eucman;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.JApplet;

import org.swixml.SwingEngine;

import com.eucman.ui.MySwixMLEngine;

/**
 * @author ntrolls
 * 
 */

public class Main extends JApplet implements ActionListener
{

	private static final long serialVersionUID = 1L;

	public void init()
	{
		super.init();
		try
		{
			String descriptorfile = this.getParameter("xml");
			if (descriptorfile == null)
			{
				descriptorfile = "ui.xml";
			}
			new MySwixMLEngine(this).insert(new URL(getCodeBase(), descriptorfile), this);
			this.setVisible(true);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		// TODO Auto-generated method stub

	}
}
