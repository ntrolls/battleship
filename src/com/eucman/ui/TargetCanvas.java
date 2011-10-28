/**
 * 
 */
package com.eucman.ui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

/**
 * @author ntrolls
 *
 */
public class TargetCanvas extends Canvas implements MouseMotionListener, MouseListener
{
	private static final long serialVersionUID = 1L;
	private Vector<Point> hits = null;
	
	private ArrayList<LaunchEventListener> _listeners = new ArrayList<LaunchEventListener>();
	
	private boolean isTargeting = false;
	float x = 0;
	float y = 0;

	public TargetCanvas()
	{
		super();
		hits = new Vector<Point>();
		setSize(440, 440);
		
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}

	
	public float getXCoord()
	{
		return x;
	}

	public float getYCoord()
	{
		return y;
	}

	
	public void addPoint(int x, int y)
	{
		hits.add(new Point(x, y));
	}

	@Override
	public void paint(Graphics g)
	{
		g.setColor(Color.BLUE);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.RED);
		for(Point p: this.hits)
		{
			g.drawOval(p.x, p.y, 5, 5);
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseMoved(MouseEvent arg0)
	{
		int _x = arg0.getX();
		int _y = arg0.getY();
		
		this.x = ((float)_x / (float)this.getWidth() - 50.0f) * 2.0f;
		this.y = ((float)_y / (float)this.getHeight() - 50.0f) * 2.0f;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseDragged(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent arg0)
	{
		System.err.println("clicked");
		if(isTargeting())
		{
			fireLaunchEvent(new LaunchEvent(this, this.getXCoord(), this.getYCoord()));
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	/**
	 * @return the isTargeting
	 */
	public boolean isTargeting()
	{
		return isTargeting;
	}

	/**
	 * @param isTargeting the isTargeting to set
	 */
	public void setTargeting(boolean isTargeting)
	{
		this.isTargeting = isTargeting;
	}
	
	public void addLaunchEventLisneter(LaunchEventListener listener)
	{
		_listeners.add(listener);
	}
	
	public void removeLaunchEventListener(LaunchEventListener listener)
	{
		_listeners.remove(listener);
	}
	
	public void fireLaunchEvent(LaunchEvent event)
	{
		Iterator<LaunchEventListener> it = _listeners.iterator();
		while(it.hasNext())
		{
			it.next().launchEventReceived(event);
		}
	}

}
