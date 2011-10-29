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
	double _x;
	double _y;
	
	public Coordinate()
	{
		_x = 0;
		_y = 0;
	}
	
	public Coordinate(double x, double y)
	{
		_x = x;
		_y = y;
	}
	
	public double get_x()
	{
		return _x;
	}
	public void set_x(double _x)
	{
		this._x = _x;
	}
	public double get_y()
	{
		return _y;
	}
	public void set_y(double _y)
	{
		this._y = _y;
	}
	
	public void setTo(double x, double y)
	{
		_x = x;
		_y = y;
	}
	
	public void setTo(Coordinate other)
	{
		this._x = other._x;
		this._y = other._y;
	}
	
	public Coordinate add(Coordinate other)
	{
		return new Coordinate(this._x + other._x, this._y + other._y);
	}
	
	public Coordinate subtract(Coordinate other)
	{
		return new Coordinate(this._x - other._x, this._y - other._y);
	}
	
	public double distanceTo(Coordinate other)
	{
		return Math.sqrt(Math.pow(_x - other._x, 2.0) + Math.pow(_y - other._y, 2.0));
	}
	
	public String toString()
	{
		return String.format("(%2.2f, %2.2f)", _x, _y);
	}
}