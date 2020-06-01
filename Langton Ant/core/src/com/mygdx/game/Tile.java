package com.mygdx.game;

public class Tile {
	protected float[] colour;
	protected float x, y;
	protected int type;
	//Possible types: 0 = empty, 1 = wall, 2 = "player", 3 = objective, 4 = visited, 5 = current
	
	public Tile(float[] colour, float x, float y) {
		this.type = 0;
		this.colour = colour.clone();
		this.x = x;
		this.y = y;
	}
	
	public void changeType(int value, float[][] colours) {
		this.setType(value);
		this.setColour(colours[value]);
	}
	
	
	
	
	
	
	public int getType() {
		return type;
	}
	
	public void setType(int value) {
		type = value;
	}
	
	public float[] getColour() {
		return colour;
	}
	
	public void setColour(float[] value) {
		colour = value.clone();
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
}
