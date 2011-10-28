/**
 * 
 */
package com.eucman;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.eucman.ui.LaunchEvent;
import com.eucman.ui.LaunchEventListener;
import com.eucman.ui.TargetCanvas;

/**
 * @author ntrolls
 * 
 */

public class Main extends JApplet implements ActionListener, LaunchEventListener
{

	private static final long serialVersionUID = 1L;
	private TargetCanvas targetCanvas;
	private JPanel mainPanel;
	private JPanel scorePanel;
	private JLabel scoreLabel;
	private JLabel totalLabel;
	private JPanel leftPanel;
	private JLabel missileLabel;
	private JButton newCoordinateButton;
	private JButton keepCoordinateButton;
	private JButton launchButton;
	private JButton patrolButton;
	private JLabel coordinateLabel;

	public void init()
	{
		super.init();
		this.setLayout(new BorderLayout());
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		
		targetCanvas = new TargetCanvas();
		targetCanvas.addLaunchEventLisneter(this);
		mainPanel.add(targetCanvas, BorderLayout.CENTER);
		
		scorePanel = new JPanel();
		scorePanel.setLayout(new GridLayout(1, 2));
		
		scoreLabel = new JLabel("Score: 0/100");
		scorePanel.add(scoreLabel);
		totalLabel = new JLabel("Total: 0");
		scorePanel.add(totalLabel);
		
		mainPanel.add(scorePanel, BorderLayout.SOUTH);
		
		leftPanel = new JPanel();
		leftPanel.setLayout(new GridLayout(7,1));
		
		leftPanel.add(new JLabel("Missile Remaining"));
		
		missileLabel = new JLabel("30/30");
		missileLabel.setHorizontalAlignment(JLabel.RIGHT);
		leftPanel.add(missileLabel);
		
		coordinateLabel = new JLabel("Coordinate:");
		leftPanel.add(coordinateLabel);
		
		newCoordinateButton = new JButton("New Coordinate");
		newCoordinateButton.setActionCommand("new");
		newCoordinateButton.addActionListener(this);
		leftPanel.add(newCoordinateButton);
		
		keepCoordinateButton = new JButton("Keep Coordinate");
		keepCoordinateButton.setActionCommand("keep");
		keepCoordinateButton.addActionListener(this);
		leftPanel.add(keepCoordinateButton);
		
		launchButton = new JButton("Order Launch");
		launchButton.setActionCommand("launch");
		launchButton.addActionListener(this);
		launchButton.setEnabled(false);
		leftPanel.add(launchButton);
		
		patrolButton = new JButton("Order Patrol");
		patrolButton.setActionCommand("patrol");
		patrolButton.addActionListener(this);
		leftPanel.add(patrolButton);
		
		mainPanel.add(leftPanel, BorderLayout.EAST);
		
		this.add(mainPanel, BorderLayout.CENTER);
		this.setSize(640, 480);
		this.setVisible(true);
		System.err.println("hey");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent action)
	{
		if(action.getActionCommand().equals("new"))
		{
			System.err.println("new");
			targetCanvas.setTargeting(true);
		}
	}

	/* (non-Javadoc)
	 * @see com.eucman.ui.FireEventListener#fireEventReceived(com.eucman.ui.FireEvent)
	 */
	@Override
	public void launchEventReceived(LaunchEvent event)
	{
		coordinateLabel.setText(String.format("X:%02f Y:%02f", event.getX(), event.getY()));
		System.err.println(String.format("X:%02f Y:%02f", event.getX(), event.getY()));
		launchButton.setEnabled(true);
		targetCanvas.setTargeting(false);
		
	}
}
