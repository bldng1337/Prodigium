package me.engine.World.Levels.SimpleLevel;

import me.engine.World.Chunk;
import me.engine.World.Tile;

public class SimpleChunk extends Chunk{

	public SimpleChunk(int x, int y) {
		super(x, y);
		for(int xx=0;xx<Chunk.SIZE;xx++) {
			for(int yy=0;yy<Chunk.SIZE;yy++) {
				getTiles()[xx][yy]=new Tile("Textures.Test.testground:png");
			}
		}
	}

}
