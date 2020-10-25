package me.engine.World.Levels.SimpleLevel;

import me.engine.Utils.ChunkRenderer;
import me.engine.Utils.Renderer;
import me.engine.World.GameLevel;

public class SimpleLevel extends GameLevel{

	public SimpleLevel(int size, Renderer r, ChunkRenderer cr) {
		super(size, r, cr);
		for(int x=0;x<size;x++) {
			for(int y=0;y<size;y++) {
				this.chunks[x][y]=new SimpleChunk(x,y);
			}
		}
		
	}

}
