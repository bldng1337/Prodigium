package me.engine.World;

import org.joml.Vector2f;

import me.engine.Main;

/**
 * @author Christian
 *
 */
public class Tile {
	/**
	 * Size of one Tile
	 */
	public static final int SIZE=150;
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
	
	
	/**
	 * @param tile The pos of the tile
	 * @param pos The pos of the Entity
	 * @param size The Size of the Entity
	 * @param motion The motion of the Entity
	 * @return The Modified motion
	 */
	public Vector2f solve(Vector2f tile,Vector2f pos,Vector2f size,Vector2f motion) {
		//TODO: make Collision System
		return null;
	}
	
}
