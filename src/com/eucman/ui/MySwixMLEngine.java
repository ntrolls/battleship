/**
 * 
 */
package com.eucman.ui;

import org.swixml.SwingEngine;

/**
 * @author ntrolls
 *
 */
public class MySwixMLEngine extends SwingEngine
{
	public MySwixMLEngine(Object client)
	{
		super(client);
		this.getTaglib().registerTag("targetcanvas", TargetCanvas.class);
	}

}
