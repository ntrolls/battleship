/**
 * 
 */
package com.eucman.ui;

import java.util.EventObject;

import com.eucman.data.Coordinate;

/**
 * @author ntrolls
 *
 */
@SuppressWarnings("serial")
public class AimEvent extends EventObject
{
	Coordinate aim = new Coordinate();
	
	/**
	 * @param source
	 */
	public AimEvent(Object source)
	{
		super(source);
	}
	
	public AimEvent(Object source, Coordinate c)
	{
		super(source);
		this.aim.setTo(c);
	}
	
	public Coordinate getAimCoordinate()
	{
		return aim;
	}
}
