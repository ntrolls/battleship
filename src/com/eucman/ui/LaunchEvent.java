/**
 * 
 */
package com.eucman.ui;

import java.util.EventObject;

/**
 * @author ntrolls
 *
 */
@SuppressWarnings("serial")
public class LaunchEvent extends EventObject
{
	float x = 0;
	float y = 0;
	
	/**
	 * @param source
	 */
	public LaunchEvent(Object source)
	{
		super(source);
	}
	
	public LaunchEvent(Object source, float _x, float _y)
	{
		super(source);
		this.x = _x;
		this.y = _y;
	}

	public float getX()
	{
		return x;
	}

	public float getY()
	{
		return y;
	}
}
