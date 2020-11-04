package me.engine.World.Generation;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Random;

import org.joml.Vector2i;

import me.engine.World.Tile;

public class MazeGenerator
{
	public Tile[][] tiles;
	public final int width, height;
	
	public MazeGenerator(int tileRows, int tileColumns) {
		width = tileRows;
		height = tileColumns;
		generate();
	}

	//Generate maze using recursive backtracking
	public Tile[][] generate() {
		tiles = new Tile[width][height];
		Deque<Vector2i> stack = new LinkedList<>();
		
		Random r = new Random();
		stack.push(new Vector2i(0, width/2));
		Vector2i currentCell = stack.peek();
		while (!stack.isEmpty()) {
			byte c = 0;
			Vector2i[] n = new Vector2i[4];
			
			//Get unvisited neighbors
			for (int x = -2; x <= 2; x+=4) {
				if (currentCell.x+x < 0 || currentCell.x+x >= width) continue;
				if (tiles[currentCell.x+x][currentCell.y] == null
					&& tiles[currentCell.x+x][Math.max(currentCell.y-1, 0)] == null
					&& tiles[currentCell.x+x][Math.min(currentCell.y+1, width-1)] == null) //check if visited
					n[c++] = new Vector2i(currentCell.x+x, currentCell.y); //add to neighbors pool
			}
			//repeat vertically
			for (int y = -2; y <= 2; y+=4) {
				if (currentCell.y+y < 0 || currentCell.y+y >= height) continue;
				if (tiles[currentCell.x][currentCell.y+y] == null
					&& tiles[Math.max(currentCell.x-1, 0)][currentCell.y+y] == null
					&& tiles[Math.min(currentCell.x+1, height-1)][currentCell.y+y] == null)
					n[c++] = new Vector2i(currentCell.x, currentCell.y+y);
			}

			if (c == 0) { //If no unvisited neighbors found, pop the stack and use it as the current cell
				currentCell = stack.pop();
				continue;
			}
			
			//Else select a random neighbor and push in on the stack
			Vector2i randN = n[(int) (r.nextInt(c))];
			stack.push(randN);
			//Get the wall between the random neighbor and the current cell and make it a path
			Vector2i median = new Vector2i();
			currentCell.add(randN, median);
			median.set(median.x/2, median.y/2);
			tiles[median.x][median.y] = new Tile("Textures.Boden.Bodenplatte_1:png", false);
			tiles[currentCell.x][currentCell.y] = new Tile("Textures.Boden.Bodenplatte_1:png", false);
			
			//Set the current cell for the next cycle
			currentCell = stack.peek();
		}
		tiles[currentCell.x][currentCell.y] = new Tile("Textures.Boden.Bodenplatte_1:png", false);
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (tiles[x][y] == null) {
					String txt = "Textures.Wand.wand_leer:png";
					if (Math.random() > 0.5)
						txt = "Textures.Wand.wand_moos:png";
					tiles[x][y] = new Tile(txt, true);
				}
			}
		}
		return tiles;
	}
}
