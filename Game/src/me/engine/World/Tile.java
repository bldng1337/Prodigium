package me.engine.World;

import me.engine.Main;

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
	
	public Tile(String tex) {
		texid=Main.getM().getTex().getTexture(tex);
	}
	public Tile(String tex,boolean collideable) {
		texid=Main.getM().getTex().getTexture(tex);
		this.collideable=collideable;
	}
	
}
