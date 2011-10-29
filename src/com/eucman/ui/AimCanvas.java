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

import com.eucman.data.Coordinate;

/**
 * @author ntrolls
 *
 */
public class AimCanvas extends Canvas implements MouseMotionListener, MouseListener
{
	private static final long serialVersionUID = 1L;
	private ArrayList<AimEventListener> _listeners = new ArrayList<AimEventListener>();
	Coordinate aim = new Coordinate();
	
	public Point lastAim = new Point();
	public Point lastHit = new Point();
	
	private boolean isTargeting = false;
	private boolean hasPatrolled = false;
	public Point temp = new Point(-100, -100);

	public AimCanvas()
	{
		super();
		
		setSize(440, 440);
		
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}
	
	public void addLaunchEventLisneter(AimEventListener listener)
	{
		_listeners.add(listener);
	}

	public Point convertToPoint(Coordinate c)
	{
		int x = (int)((double)getWidth()  / 100.0 * (c.get_x() / 2.0 + 50.0));
		int y = (int)((double)getHeight() / 100.0 * (c.get_y() / 2.0 + 50.0));
		return new Point(x, y);
	}

	public void fireAimEvent(AimEvent event)
	{
		Iterator<AimEventListener> it = _listeners.iterator();
		while(it.hasNext())
		{
			it.next().aimEventReceived(event);
		}
	}
	
	public boolean isTargeting()
	{
		return isTargeting;
	}
	
	public boolean isHasPatrolled()
	{
		return hasPatrolled;
	}

	public void setHasPatrolled(boolean hasPatrolled)
	{
		this.hasPatrolled = hasPatrolled;
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		if(isTargeting())
		{
			if(temp == null)
			{
				temp = new Point(e.getX(), e.getY());
			}
			temp.x = e.getX();
			temp.y = e.getY();
			fireAimEvent(new AimEvent(this, this.aim));
			this.isTargeting = false;
		}
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		
	}
	
	@Override
	public void mouseEntered(MouseEvent arg0)
	{
		
	}
	
	@Override
	public void mouseExited(MouseEvent arg0)
	{
		
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseMoved(MouseEvent arg0)
	{
		if(isTargeting())
		{
			temp.x = arg0.getX();
			temp.y = arg0.getY();
			
			aim.set_x(((double)temp.x / (double)this.getWidth()  * 100.0 - 50.0) * 2.0);
			aim.set_y(((double)temp.y / (double)this.getHeight() * 100.0 - 50.0) * 2.0);
			
			repaint();
		}
	}
	
	@Override
	public void mousePressed(MouseEvent arg0)
	{
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0)
	{
		
	}

	@Override
	public void paint(Graphics g)
	{
		g.setColor(Color.BLUE);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		g.setColor(Color.GREEN);
		g.drawOval(temp.x, temp.y, 5, 5);
		
		if(lastAim != null)
		{
			g.setColor(Color.BLACK);
			g.drawOval(lastAim.x, lastAim.y, 5, 5);
		}
		
		if(hasPatrolled)
		{
			g.setColor(Color.red);
			g.drawOval(lastHit.x, lastHit.y, 5, 5);
		}	
	}

	public void removeLaunchEventListener(AimEventListener listener)
	{
		_listeners.remove(listener);
	}

	public void setTargeting(boolean isTargeting)
	{
		this.isTargeting = isTargeting;
	}
}