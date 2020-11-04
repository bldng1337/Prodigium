package me.engine.World.Levels.Maze;

import me.engine.World.GameLevel;
import me.engine.World.Levels.Maze.MazeChunk;

public class MazeLevel extends GameLevel {
	public MazeLevel(int size) {
		super(size);
		for(int x=0;x<size;x++) {
			for(int y=0;y<size;y++) {
				this.chunks[x][y]=new MazeChunk(x,y);
			}
		}
	}
}
