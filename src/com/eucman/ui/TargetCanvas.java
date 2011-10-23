/**
 * 
 */
package com.eucman.ui;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Vector;

/**
 * @author ntrolls
 *
 */
public class TargetCanvas extends Canvas
{
	private static final long serialVersionUID = 1L;
	private Vector<Point> hits = null;
	
	public TargetCanvas()
	{
		super();
		hits = new Vector<Point>();
	}
	
	public void addPoint(int x, int y)
	{
		hits.add(new Point(x, y));
	}

	@Override
	public void paint(Graphics g)
	{
		for(Point p: this.hits)
		{
			g.drawOval(p.x, p.y, 5, 5);
		}
	}
	
	

}
