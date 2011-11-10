/**
 * 
 */
package com.eucman.ui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.Timer;

import com.eucman.data.Coordinate;

/**
 * @author ntrolls
 * 
 */
public class AimCanvas extends Canvas implements MouseMotionListener, MouseListener, ActionListener
{
	private static final long serialVersionUID = 1L;
	private ArrayList<AimEventListener> _listeners = new ArrayList<AimEventListener>();

	private ImageIcon mi = new ImageIcon(this.getClass().getResource("missile.png"));
	private Missile missile = null;
	private boolean animationOn = false;
	private Timer timer = null;

	public Coordinate aim = new Coordinate(-1, -1);
	public Coordinate lastAim = new Coordinate(-1, -1);
	public Coordinate lastHit = new Coordinate(-1, -1);
	public Coordinate temp = new Coordinate(-100, -100);

	private boolean isTargeting = false;
	private boolean hasPatrolled = false;

	public AimCanvas()
	{
		super();

		setSize(420, 420);
		this.setPreferredSize(new Dimension(420, 420));
		this.setMaximumSize(new Dimension(420, 420));

		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}

	public void addLaunchEventLisneter(AimEventListener listener)
	{
		_listeners.add(listener);
	}

	public Coordinate convertToCoordinate(int _x, int _y)
	{
		int row = _x / 22;
		int col = _y / 22;
		return new Coordinate(row, col);
	}

	public void fireAimEvent(AimEvent event)
	{
		Iterator<AimEventListener> it = _listeners.iterator();
		while (it.hasNext())
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
		if (isTargeting() && !isAnimationOn())
		{
			aim = convertToCoordinate(e.getY(), e.getX());
			fireAimEvent(new AimEvent(this, this.aim));
			this.isTargeting = false;
			repaint();
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
		if (isTargeting() && !isAnimationOn())
		{
			temp = convertToCoordinate(arg0.getY(), arg0.getX());
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

		g.setColor(Color.black);

		if (isAnimationOn())
		{
			Graphics2D g2d = (Graphics2D) g;
			g2d.drawImage(mi.getImage(), missile.position.getCol() * 21, missile.position.getRow() * 21, this);
		}

		for (int i = 1; i < 20; i++)
		{
			g.drawLine(0, 21 * i, 419, 21 * i);
			g.drawLine(21 * i, 0, 21 * i, 419);
		}

		if (this.isHasPatrolled())
		{
			g.setColor(Color.red);
			g.fillRect(21 * lastHit.getCol() + 1, 21 * lastHit.getRow() + 1, 20, 20);
		}

		if (this.lastAim != null)
		{
			g.setColor(Color.gray);
			g.fillRect(21 * lastAim.getCol() + 1, 21 * lastAim.getRow() + 1, 20, 20);
		}

		if (isTargeting())
		{
			g.setColor(Color.GREEN);
			g.fillRect(21 * temp.getCol() + 1, 21 * temp.getRow() + 1, 20, 20);
		}

		if (aim != null)
		{
			g.setColor(Color.black);
			g.fillRect(21 * aim.getCol() + 1, 21 * aim.getRow() + 1, 20, 20);
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

	public boolean isAnimationOn()
	{
		return animationOn;
	}

	public void setAnimationOn(boolean animationOn)
	{
		this.animationOn = animationOn;
	}

	public void startAnimation()
	{
		Coordinate start = new Coordinate(aim.getRow(), 0);
		missile = new Missile(start);
		timer = new Timer(50, this);
		setAnimationOn(true);
		timer.start();
	}
	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		missile.move(Missile.RIGHT);
		repaint();
		if (missile.position.equals(aim))
		{
			setAnimationOn(false);
			timer.stop();
			aim = null;
		}
	}
}