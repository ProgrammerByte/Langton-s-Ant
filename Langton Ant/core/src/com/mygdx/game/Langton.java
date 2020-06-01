package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Langton extends ApplicationAdapter {
	ShapeRenderer sr;
	
	float[][] colours = new float[][] {{0, 0, 0}, {1, 1, 1}, {1, 0, 0}}; 
	//Above corresponds with types: none, wall, player, objective, visited, currently visiting, winning path
	
	float[] tempColour;
	
	int screenWidth = 1920;
	int screenHeight = 1080;
	
	int xCount = 160; //16 / 9
	int yCount = 90;
	
	int[][] movements = new int[][] {{0, 1}, {1, 0}, {0, -1}, {-1, 0}}; //right = clockwise, left = anticlockwise
	int movementIndex = 3;
	int[] antPos = new int[] {xCount / 2, yCount / 2};
	
	float cellWidth = (float)screenWidth / xCount;
	float cellHeight = (float)screenHeight / yCount;
	
	
	Tile[][] tiles = new Tile[xCount][yCount], originalTiles = new Tile[xCount][yCount];
	
	boolean grid = true, clearTime = false, isEditing = true, automatic = true;
	
	int frameCount = 0, timeDelay = 1;
	
	@Override
	public void create() {
		sr = new ShapeRenderer();
		
		for (int i = 0; i < xCount; i++) {
			for (int j = 0; j < yCount; j++) {
				tiles[i][j] = new Tile(colours[0], i * cellWidth, j * cellHeight);
			}
		}
		
		//tiles[1][(int)(yCount / 2)].changeType(2, colours);
		//tiles[xCount - 2][(int)(yCount / 2)].changeType(3, colours);
	}
	
	public void editGrid() {
		int type = -1;
		if (Gdx.input.isButtonPressed(Buttons.RIGHT)) {
			//System.out.println("AAAAAA");
			type = 0;
		}
		else if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
			//System.out.println("AAAAAA");
			type = 1;
		}
		
		if (type != -1) {
			float xMin, xMax, yMin, yMax;
			float mouseX = Gdx.input.getX();
			float mouseY = screenHeight - Gdx.input.getY();
			//System.out.println(mouseX + "   " + mouseY);
			for (int i = 0; i < xCount; i++) {
				for (int j = 0; j < yCount; j++) {
					xMin = tiles[i][j].getX();
					xMax = xMin + cellWidth;
					yMin = tiles[i][j].getY();
					yMax = yMin + cellHeight;
					
					if (xMin <= mouseX && xMax >= mouseX && yMin <= mouseY && yMax >= mouseY) {
						tiles[i][j].changeType(type, colours);
					}
				}
			}
		}
	}
	
	public void input() {
		if (Gdx.input.isKeyJustPressed(Keys.Q)) {
			grid = !grid;
		}
		if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
			isEditing = !isEditing;
		}
		if (Gdx.input.isKeyJustPressed(Keys.A)) {
			automatic = !automatic;
		}
		if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			Gdx.app.exit();
		}
	}
	
	public void moveAnt() {
		tiles[antPos[0]][antPos[1]].setColour(colours[tiles[antPos[0]][antPos[1]].getType()]);
		if (tiles[antPos[0]][antPos[1]].getType() == 0) {
			movementIndex += 1;
			tiles[antPos[0]][antPos[1]].changeType(1, colours);
		}
		else {
			movementIndex -= 1;
			tiles[antPos[0]][antPos[1]].changeType(0, colours);
		}
		
		movementIndex = (movementIndex + 4) % 4;
		
		antPos[0] += movements[movementIndex][0] + xCount;
		antPos[1] += movements[movementIndex][1] + yCount;
		
		antPos[0] %= xCount;
		antPos[1] %= yCount;
		
		tiles[antPos[0]][antPos[1]].setColour(colours[2]);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		input();
		
		if (isEditing == true) {
			editGrid();
		}
		else if (automatic == true) {
			if (frameCount % timeDelay == 0){ //Automatic
				moveAnt();
			}
		}
		
		else if (Gdx.input.isKeyJustPressed(Keys.SPACE)) { //Manual
			moveAnt();
		}
		
		sr.begin(ShapeType.Filled);
		for (int i = 0; i < xCount; i++) {
			for (int j = 0; j < yCount; j++) {
				tempColour = tiles[i][j].getColour();
				sr.setColor(tempColour[0], tempColour[1], tempColour[2], 1);
				sr.rect(tiles[i][j].getX(), tiles[i][j].getY(), cellWidth, cellHeight);
			}
		}
		sr.end();
		
		
		//Follow render block creates a grid for creating a maze
		if (grid == true) {
			sr.begin(ShapeType.Line);
			sr.setColor(0.9f, 0.9f, 0.9f, 1);
			for (int i = 0; i < xCount; i++) {
				for (int j = 0; j < yCount; j++) {
					sr.rect(tiles[i][j].getX(), tiles[i][j].getY(), cellWidth, cellHeight);
				}
			}
			sr.end();
		}
		
		frameCount += 1;
	}
	
	@Override
	public void dispose() {
		sr.dispose();
	}
}
