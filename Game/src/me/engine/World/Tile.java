package me.engine.World;

import me.engine.Main;

/**
 * @author Christian
 *
 */
public class Tile {
	boolean collideable;
	long texid;
	public Tile(String tex) {
		texid=Main.getTex().getTexture(tex);
	}
	
	
	
	
}
