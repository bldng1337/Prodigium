package me.engine.World.Tiles;

import org.joml.Vector2f;
import org.joml.Vector4f;

import me.engine.World.GameLevel;

public interface ITile {
	/**
	 * Size of one Tile
	 */
	public static final int SIZE=100;
	
	public int render(Vector2f pos,float[] vertecies, float[] txt, float[] col, int i,GameLevel l);

	public Vector4f getBB(Vector2f pos);
	
	public boolean isCollideable();
	
	public long getPrimaryTex();
}
