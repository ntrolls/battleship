/**
 * 
 */
package com.eucman.data;

/**
 * @author ntrolls
 * 
 */
public class Coordinate
{
	int _col;
	int _row;

	public Coordinate()
	{
		_col = 0;
		_row = 0;
	}

	public Coordinate(int row, int col)
	{
		_col = col;
		_row = row;
	}

	public int getCol()
	{
		return _col;
	}
	public void setCol(int _x)
	{
		this._col = _x;
	}
	public int getRow()
	{
		return _row;
	}
	public void setRow(int _y)
	{
		this._row = _y;
	}

	public void setTo(int x, int y)
	{
		_col = x;
		_row = y;
	}

	public boolean equals(Coordinate other)
	{
		return _col == other._col && _row == other._row;
	}

	public void setTo(Coordinate other)
	{
		this._col = other._col;
		this._row = other._row;
	}

	public Coordinate add(Coordinate other)
	{
		return new Coordinate(this._col + other._col, this._row + other._row);
	}

	public Coordinate subtract(Coordinate other)
	{
		return new Coordinate(this._col - other._col, this._row - other._row);
	}

	public double distanceTo(Coordinate other)
	{
		return Math.sqrt(Math.pow(_col - other._col, 2.0) + Math.pow(_row - other._row, 2.0));
	}

	public String toString()
	{
		return String.format("(%d, %d)", _col, _row);
	}
}