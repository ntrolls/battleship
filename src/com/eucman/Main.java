/**
 * 
 */
package com.eucman;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;

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
	private JTextPane messageView;

	private Random rand = new Random();
	Font countFont = new Font("Dialog", Font.PLAIN, 18);

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
	private JLabel remaining;

	public void init()
	{
		super.init();
		this.setLayout(new BorderLayout());

		mainPanel = new JPanel();
		mainPanel.setSize(420, 660);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		aimCanvas = new AimCanvas();
		aimCanvas.setPreferredSize(new Dimension(420, 420));
		aimCanvas.addLaunchEventLisneter(this);
		mainPanel.add(aimCanvas, BorderLayout.CENTER);

		scorePanel = new JPanel();
		scorePanel.setLayout(new GridLayout(1, 2));

		scoreLabel = new JLabel(String.format("Score: %d/100", currentScore));
		scoreLabel.setFont(countFont);
		scorePanel.add(scoreLabel);
		totalLabel = new JLabel(String.format("Total: %d", totalScore));
		totalLabel.setFont(countFont);
		scorePanel.add(totalLabel);

		mainPanel.add(scorePanel, BorderLayout.SOUTH);

		messageView = new JTextPane();
		messageView.setEditable(false);
		messageView.setPreferredSize(new Dimension(420, 200));
		mainPanel.add(messageView, BorderLayout.SOUTH);

		leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

		remaining = new JLabel("Missiles Remaining");
		remaining.setFont(countFont);
		leftPanel.add(remaining);
		
		maxMissiles = Integer.parseInt(this.getParameter("missiles"));
		missilesRemaining = maxMissiles;
		missileLabel = new JLabel();
		missileLabel.setFont(countFont);
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
		this.setSize(650, 480);

		this.setVisible(true);
	}
	public void start()
	{
		initialiseData();
		updateDisplay();

		setMessage("Operator: Missile Ready, Sir! Give me an order.");
		requestFocus();
	}

	/**
	 * 
	 */
	private void initialiseData()
	{
		mean = Double.parseDouble(getParameter("mean"));
		sd = Double.parseDouble(getParameter("sd"));
	}

	private void updateDisplay()
	{
		//		coordinateLabel.setText(String.format("X:%.02f Y:%.02f", current.get_x(), current.get_y()));
		scoreLabel.setText(String.format("Score: %d/100", currentScore));
		totalLabel.setText(String.format("Total: %d", totalScore));
		missileLabel.setText(String.format("%d/%d", missilesRemaining, maxMissiles));;
	}

	private void setMessage(String message)
	{
		messageView.setText(message);
		java.awt.Toolkit.getDefaultToolkit().beep();
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
		if (action.getActionCommand().equals("new"))
		{
			aimCanvas.setTargeting(true);
			newCoordinateButton.setEnabled(false);

			setMessage("Operator: Choose the target location");
		}
		else if (action.getActionCommand().equals("launch"))
		{
			setMessage("Operator: Firing missile, sir");
			launchMisile();
		}
		else if (action.getActionCommand().equals("keep"))
		{
			current.setTo(old);
			aimCanvas.aim = new Coordinate();
			aimCanvas.aim.setTo(aimCanvas.lastAim);
			aimCanvas.repaint();
			launchButton.setEnabled(true);
			updateDisplay();

			setMessage("Operator: Coordinate kept: you can order missile launch");
		}
		else if (action.getActionCommand().equals("patrol"))
		{
			patrol();
		}

		if (missilesRemaining == 0)
		{
			setMessage("Operator: Game Over");
			Gson gson = new Gson();
			ResultFrame rf = new ResultFrame(String.format("Aims:%s\nErrors:%s", gson.toJson(aims), gson.toJson(errors)));
			rf.setModal(true);
			rf.setVisible(true);
		}
	}

	private void patrol()
	{
		// process patrol
		setMessage("Operator: Sending out patrol ship to locate the missie landing position");

		aimCanvas.setHasPatrolled(true);
		aimCanvas.repaint();

		setMessage("Operator: We located the landing position but the patrol ship was sunk by the enemy");

		// disable patrol button
		patrolButton.setEnabled(false);

		// subtract score
		totalScore -= 10;
		updateDisplay();
	}

	private void launchMisile()
	{
		// disable launch button
		launchButton.setEnabled(false);

		// enable patrol button
		patrolButton.setEnabled(true);

		// first store launch coordinate to (oldX, oldY)
		old.setTo(current);

		// process launch
		Coordinate error = new Coordinate((int) (mean + sd * rand.nextGaussian()), (int) (mean + sd * rand.nextGaussian()));
		Coordinate landed = current.add(error);
		this.aims.add(current);
		this.errors.add(error);

		aimCanvas.lastAim.setTo(current);
		aimCanvas.lastHit.setTo(landed);

		aimCanvas.setHasPatrolled(false);
		aimCanvas.repaint();

		missilesRemaining -= 1;

		// start animation
		aimCanvas.startAnimation();

		// calculate score
		currentScore = calculateScore(enemy, landed);
		totalScore += currentScore;
		if (currentScore == 0)
		{
			setMessage("Operator: We have completely missed the target");
		}
		else
		{
			setMessage("Operator: Smoke has been observed but we have no clear visual. Sending a patrol ship will give us a visual but you will lose 10 points.");
		}

		// update display
		updateDisplay();
	}

	private int calculateScore(Coordinate _enemy, Coordinate _landed)
	{
		double delta = _landed.distanceTo(_enemy);
		return 100 - (int) (Math.min(delta, 100));
	}

	@Override
	public void aimEventReceived(AimEvent event)
	{
		current.setTo(event.getAimCoordinate());
		updateDisplay();

		newCoordinateButton.setEnabled(true);
		launchButton.setEnabled(true);
		aimCanvas.setTargeting(false);

		setMessage("Operator: You can order missile launch");

	}
}
