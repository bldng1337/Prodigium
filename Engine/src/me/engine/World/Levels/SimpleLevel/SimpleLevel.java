package me.engine.World.Levels.SimpleLevel;

import me.engine.Engine;
import me.engine.World.GameLevel;

public class SimpleLevel extends GameLevel{

	public SimpleLevel(int size,String... tiletexture) {
		super(size);
		for(int x=0;x<size;x++) {
			for(int y=0;y<size;y++) {
				this.chunks[x][y]=new SimpleChunk(x,y,this,tiletexture);
			}
		}
	}

	@Override
	public void spawnEnemy(int x, int y) {
		addEntity(Engine.getEngine().getEntityManager().newEntity("Entities.Test.Testentity:json",x,y));
	}

}
