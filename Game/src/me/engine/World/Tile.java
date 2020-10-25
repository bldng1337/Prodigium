package me.engine.World;

import me.engine.Main;

/**
 * @author Christian
 *
 */
public class Tile {
	public static final int SIZE=150;
	boolean collideable;
	long texid;
	public Tile(String tex) {
		texid=Main.getTex().getTexture(tex);
	}
	
	
	
	
}
