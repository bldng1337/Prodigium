package me.engine.World;

import me.engine.Engine;

/**
 * @author Christian
 *
 */
public class Tile {
	/**
	 * Size of one Tile
	 */
	public static final int SIZE=100;
	/**
	 * If this Tile should be Collidable
	 */
	boolean collideable;
	/**
	 * Texture ID of this Tile
	 */
	long texid;
	
	boolean spawn;
	
	public Tile(String tex) {
		texid=Engine.getEngine().getTex().getTexture(tex);
	}
	
	public Tile(String tex,boolean collideable) {
		texid=Engine.getEngine().getTex().getTexture(tex);
		this.collideable=collideable;
	}
	
	public Tile(String tex,boolean collideable,boolean spawn) {
		texid=Engine.getEngine().getTex().getTexture(tex);
		this.collideable=collideable;
		this.spawn=spawn;
	}
	
	public boolean isCollideable() {
		return collideable;
	}
	public long getTexid() {
		return texid;
	}
}
