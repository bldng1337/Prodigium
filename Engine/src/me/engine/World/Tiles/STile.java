package me.engine.World.Tiles;

import org.joml.Vector2f;
import org.joml.Vector4f;

import me.engine.Engine;
import me.engine.World.GameLevel;

/**
 * @author Christian
 *
 */
public class STile extends Tile {
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
	
	public STile(String tex) {
		texid=Engine.getEngine().getTex().getTexture(tex);
	}
	
	public STile(String tex,boolean collideable) {
		this.collideable=collideable;
		texid=Engine.getEngine().getTex().getTexture(tex);
	}
	@Override
	public boolean isCollideable() {
		return collideable;
	}
	@Override
	public long getPrimaryTex() {
		return texid;
	}

	public int render(Vector2f pos,float[] vertecies, float[] txt, float[] col, int i,GameLevel l) {
		return render(new Vector4f(pos.x,pos.y,pos.x+STile.SIZE,pos.y+STile.SIZE), texid, new Vector4f(1), i, new float[][] {vertecies,txt,col});
	}
	

	@Override
	public Vector4f getBB(Vector2f pos) {
		return new Vector4f(pos.x,pos.y,pos.x+1,pos.y+1);
	}

	@Override
	public Vector4f getLightBB(Vector2f pos) {
		return new Vector4f(pos.x,pos.y,pos.x+1,pos.y+1);
	}
}
