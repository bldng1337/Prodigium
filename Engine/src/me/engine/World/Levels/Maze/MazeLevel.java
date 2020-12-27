package me.engine.World.Levels.Maze;

import me.engine.Engine;
import me.engine.World.GameLevel;

public class MazeLevel extends GameLevel {
	public MazeLevel(int size) {
		super(size);
		for(int x=0;x<size;x++) {
			for(int y=0;y<size;y++) {
				this.chunks[x][y]=new MazeChunk(x,y,this);
			}
		}
	}

	@Override
	public void spawnEnemy(int x, int y) {
		if(rand.nextBoolean()) {
			addEntity(Engine.getEngine().getEntityManager().newEntity("Entities.Enemies.Skeleton:json",x,y));
		}else {
			addEntity(Engine.getEngine().getEntityManager().newEntity("Entities.Enemies.Slime:json",x,y));
		}
	}
}
