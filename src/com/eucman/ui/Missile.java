package com.eucman.ui;

import com.eucman.data.Coordinate;

public class Missile
{
	public Coordinate position = null;

	public static final int UP = 0;
	public static final int DOWN = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;

	public Missile(Coordinate _pos)
	{
		this.position = new Coordinate();
		this.position.setTo(_pos);
	}

	public void move(int direction)
	{
		if (direction == UP)
			position.setRow(position.getRow() - 1);
		else if (direction == DOWN)
			position.setRow(position.getRow() + 1);
		else if (direction == LEFT)
			position.setCol(position.getCol() - 1);
		else if (direction == RIGHT)
			position.setCol(position.getCol() + 1);
	}
}
