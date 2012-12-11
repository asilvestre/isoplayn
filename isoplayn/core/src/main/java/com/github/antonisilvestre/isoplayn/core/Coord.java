package com.github.antonisilvestre.isoplayn.core;

public class Coord {

	/**
	 * @param x
	 * @param y
	 */
	public Coord(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * X coord
	 */
	private int x;
	
	/**
	 * Y coord
	 */
	private int y;
}
