/**
 * 
 */
package com.eucman;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.eucman.data.Coordinate;
import com.eucman.ui.AimCanvas;
import com.eucman.ui.AimEvent;
import com.eucman.ui.AimEventListener;
import com.eucman.ui.ResultFrame;
import com.google.gson.Gson;

/**
 * @author ntrolls
 * 
 */

public class Main extends JApplet implements ActionListener, AimEventListener
{

	private static final long serialVersionUID = 1L;
	private AimCanvas aimCanvas;
	private Container mainPanel;
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

	private Random rand = new Random();
	
	private Coordinate old = new Coordinate();
	private Coordinate current = new Coordinate();
	private Coordinate enemy = new Coordinate();
	
	double mean = 10.0;
	double sd = 30.0;
	
	int currentScore = 0;
	int totalScore = 0;
	int missilesRemaining = 30;
	
	private Vector<Coordinate> aims = new Vector<Coordinate>();
	private Vector<Coordinate> errors = new Vector<Coordinate>();
	private int maxMissiles;

	public void init()
	{
		super.init();
		this.setLayout(new BorderLayout());
		
		mainPanel = new JPanel();
		mainPanel.setSize(420, 460);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		aimCanvas = new AimCanvas();
		aimCanvas.setPreferredSize(new Dimension(420, 420));
		aimCanvas.addLaunchEventLisneter(this);
		mainPanel.add(aimCanvas, BorderLayout.CENTER);
		
		scorePanel = new JPanel();
		scorePanel.setLayout(new GridLayout(1, 2));
		
		scoreLabel = new JLabel(String.format("Score: %d/100", currentScore));
		scorePanel.add(scoreLabel);
		totalLabel = new JLabel(String.format("Total: %d", totalScore));
		scorePanel.add(totalLabel);
		
		mainPanel.add(scorePanel, BorderLayout.SOUTH);
		
		leftPanel = new JPanel();
		leftPanel.setLayout(new GridLayout(7,1));
		
		leftPanel.add(new JLabel("Missile Remaining"));
		
		maxMissiles = Integer.parseInt(this.getParameter("missiles"));
		missilesRemaining = maxMissiles;
		missileLabel = new JLabel();
		missileLabel.setHorizontalAlignment(JLabel.RIGHT);
		leftPanel.add(missileLabel);
		
		coordinateLabel = new JLabel();
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
		
		this.add(mainPanel, BorderLayout.CENTER);
		this.add(leftPanel, BorderLayout.EAST);
		this.setSize(620, 480);
		
		
		this.setVisible(true);
	}
	
	public void start()
	{	
		initialiseData();
		updateDisplay();
		
		JOptionPane.showMessageDialog(null,
				"Missile Ready, Sir! Give me an order.",
			    "Operator",
			    JOptionPane.PLAIN_MESSAGE);
		requestFocus();
	}
	
	/**
	 * 
	 */
	private void initialiseData()
	{
		mean = Double.parseDouble(getParameter("mean"));
		sd = Double.parseDouble(getParameter("sd"));
//		enemy.setTo(rand.nextFloat() * 200.0f - 100.0f , rand.nextFloat() * 200.0f - 100.0f);
	}

	private void updateDisplay()
	{
//		coordinateLabel.setText(String.format("X:%.02f Y:%.02f", current.get_x(), current.get_y()));
		scoreLabel.setText(String.format("Score: %d/100", currentScore));
		totalLabel.setText(String.format("Total: %d", totalScore));
		missileLabel.setText(String.format("%d/%d", missilesRemaining, maxMissiles));;
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
			aimCanvas.setTargeting(true);
			newCoordinateButton.setEnabled(false);
			
			JOptionPane.showMessageDialog(this,
					"Choose the target location.",
				    "Operator",
				    JOptionPane.PLAIN_MESSAGE);
		}
		else if(action.getActionCommand().equals("launch"))
		{
			JOptionPane.showMessageDialog(this,
					"Firing missile, sir",
				    "Operator",
				    JOptionPane.PLAIN_MESSAGE);
			launchMisile();
		}
		else if(action.getActionCommand().equals("keep"))
		{
			current.setTo(old);
			aimCanvas.aim = new Coordinate();
			aimCanvas.aim.setTo(aimCanvas.lastAim);
			aimCanvas.repaint();
			launchButton.setEnabled(true);
			updateDisplay();
			
			JOptionPane.showMessageDialog(this,
					"Coordinate kept: you can order missile launch",
				    "Operator",
				    JOptionPane.PLAIN_MESSAGE);
		}
		else if(action.getActionCommand().equals("patrol"))
		{
			patrol();
		}
		
		if(missilesRemaining == 0)
		{
			JOptionPane.showMessageDialog(this,
					"Game Over",
				    "Operator",
				    JOptionPane.PLAIN_MESSAGE);
			Gson gson = new Gson();
			ResultFrame rf = new ResultFrame(String.format("Aims:%s\nErrors:%s", gson.toJson(aims), gson.toJson(errors)));
			rf.setModal(true);
			rf.setVisible(true);
		}
	}

	private void patrol()
	{
		// process patrol
		JOptionPane.showMessageDialog(this,
				"Sending out patrol ship to locate the missie landing position",
			    "Operator",
			    JOptionPane.PLAIN_MESSAGE);
		
		aimCanvas.setHasPatrolled(true);
		aimCanvas.repaint();
		
		JOptionPane.showMessageDialog(this,
				"We located the landing position but the patrol ship was sunk by the enemy",
			    "Operator",
			    JOptionPane.PLAIN_MESSAGE);
		
		// disable patrol button
		patrolButton.setEnabled(false);
		
		// subtract score
		totalScore -= 10;
		updateDisplay();
	}

	private void launchMisile()
	{
		// first store launch coordinate to (oldX, oldY)
		old.setTo(current);
		
		// process launch
		Coordinate error = new Coordinate((int)(mean + sd * rand.nextGaussian()), (int)(mean + sd * rand.nextGaussian()));
		Coordinate landed = current.add(error);
		this.aims.add(current);
		this.errors.add(error);
		
		aimCanvas.lastAim.setTo(current);
		aimCanvas.lastHit.setTo(landed);
		aimCanvas.aim = null;
		aimCanvas.setHasPatrolled(false);
		aimCanvas.repaint();
		
		missilesRemaining -= 1;
		
		// calculate score
		currentScore = calculateScore(enemy, landed);
		totalScore += currentScore;
		if(currentScore == 0)
		{
			JOptionPane.showMessageDialog(this,
					"We have completely missed the target",
				    "Operator",
				    JOptionPane.PLAIN_MESSAGE);
		}
		else
		{
			JOptionPane.showMessageDialog(this,
					"Smoke has been observed but we have no clear visual. Sending a patrol ship will give us a visual but you will lose 10 points.",
				    "Operator",
				    JOptionPane.PLAIN_MESSAGE);
		}
		
		// disable launch button
		launchButton.setEnabled(false);
		
		// enable patrol button
		patrolButton.setEnabled(true);
		
		// update display
		updateDisplay();
	}
	
	private int calculateScore(Coordinate _enemy, Coordinate _landed)
	{
		double delta = _landed.distanceTo(_enemy);
		return 100 - (int)(Math.min(delta, 100));
	}

	@Override
	public void aimEventReceived(AimEvent event)
	{
		current.setTo(event.getAimCoordinate());
		updateDisplay();
		
		newCoordinateButton.setEnabled(true);
		launchButton.setEnabled(true);
		aimCanvas.setTargeting(false);
		
		JOptionPane.showMessageDialog(this,
				"You can order missile launch",
			    "Operator",
			    JOptionPane.PLAIN_MESSAGE);
		
	}
}
