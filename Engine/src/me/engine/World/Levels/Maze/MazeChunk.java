package me.engine.World.Levels.Maze;

import me.engine.World.Chunk;
import me.engine.World.Tile;
import me.engine.World.Generation.MazeGenerator;

public class MazeChunk extends Chunk
{
	public static MazeGenerator maze = new MazeGenerator(Chunk.SIZE,Chunk.SIZE);
	public MazeChunk(int x, int y) {
		super(x, y);
		for(int xx=0;xx<Chunk.SIZE;xx++) {
			for(int yy=0;yy<Chunk.SIZE;yy++) {
				getTiles()[xx][yy] = maze.tiles[xx][yy];
			}
		}
	}
}
