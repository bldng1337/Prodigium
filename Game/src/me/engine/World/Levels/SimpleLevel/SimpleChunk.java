package me.engine.World.Levels.SimpleLevel;

import me.engine.World.Chunk;
import me.engine.World.Tile;

public class SimpleChunk extends Chunk{

	public SimpleChunk(int x, int y,String... tiletexture) {
		super(x, y);
		for(int xx=0;xx<Chunk.SIZE;xx++) {
			for(int yy=0;yy<Chunk.SIZE;yy++) {
				if((xx==Chunk.SIZE-1||xx==0||yy==0||y==Chunk.SIZE-1)) {
					if(Math.random()<0.7f) {
						getTiles()[xx][yy]=new Tile(tiletexture[tiletexture.length-1],true);
					}else {
						getTiles()[xx][yy]=new Tile(tiletexture[(int) (Math.random()*(tiletexture.length-1))],false);
					}
				}else {
					int txt=(int) (Math.random()*(tiletexture.length-1));
					getTiles()[xx][yy]=new Tile(tiletexture[txt],false);
				}
			}
		}
	}

}
