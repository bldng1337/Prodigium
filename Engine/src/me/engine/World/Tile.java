package me.engine.World;

import org.joml.Vector2f;

import me.engine.Engine;
import me.engine.Utils.Texture;

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
		texid=Engine.getEngine().getTex().getTexture(tex);
	}
	
	public Tile(String tex,boolean collideable) {
		texid=Engine.getEngine().getTex().getTexture(tex);
		this.collideable=collideable;
	}
	
	public boolean isCollideable() {
		return collideable;
	}
	public long getTexid() {
		return texid;
	}

	public void render(Vector2f pos,float[] vertecies, float[] txt, float[] col, int i) {
		int vi=i*(3*6),ti=i*(3*6),ci=i*(4*6);
		float tx=Texture.getx(texid);
		float ty=Texture.gety(texid);
		float tx2=Texture.getx(texid)+Texture.getdx(texid);
		float ty2=Texture.gety(texid)+Texture.getdy(texid);
		int atlas=Texture.getatlas(texid);
		tx/=Engine.getEngine().getTex().getMsize();
		ty/=Engine.getEngine().getTex().getMsize();
		tx2/=Engine.getEngine().getTex().getMsize();
		ty2/=Engine.getEngine().getTex().getMsize();
		vertecies[vi++]=pos.x;
		vertecies[vi++]=pos.y+Tile.SIZE;
		vertecies[vi++]=1;
		txt[ti++]=tx;
		txt[ti++]=ty2;
		txt[ti++]=atlas;
		col[ci++]=0.5f;
		col[ci++]=0.5f;
		col[ci++]=0.5f;
		col[ci++]=1f;
		
		vertecies[vi++]=pos.x;
		vertecies[vi++]=pos.y;
		vertecies[vi++]=1;
		txt[ti++]=tx;
		txt[ti++]=ty;
		txt[ti++]=atlas;
		col[ci++]=1f;
		col[ci++]=1f;
		col[ci++]=1f;
		col[ci++]=1f;
		
		vertecies[vi++]=pos.x+Tile.SIZE;
		vertecies[vi++]=pos.y;
		vertecies[vi++]=1;
		txt[ti++]=tx2;
		txt[ti++]=ty;
		txt[ti++]=atlas;
		col[ci++]=1f;
		col[ci++]=1f;
		col[ci++]=1f;
		col[ci++]=1f;
		
		vertecies[vi++]=pos.x;
		vertecies[vi++]=pos.y+Tile.SIZE;
		vertecies[vi++]=1;
		txt[ti++]=tx;
		txt[ti++]=ty2;
		txt[ti++]=atlas;
		col[ci++]=0.5f;
		col[ci++]=0.5f;
		col[ci++]=0.5f;
		col[ci++]=1f;
		
		vertecies[vi++]=pos.x+Tile.SIZE;
		vertecies[vi++]=pos.y+Tile.SIZE;
		vertecies[vi++]=1;
		txt[ti++]=tx2;
		txt[ti++]=ty2;
		txt[ti++]=atlas;
		col[ci++]=0.5f;
		col[ci++]=0.5f;
		col[ci++]=0.5f;
		col[ci++]=1f;
		
		vertecies[vi++]=pos.x+Tile.SIZE;
		vertecies[vi++]=pos.y;
		vertecies[vi++]=1;
		txt[ti++]=tx2;
		txt[ti++]=ty;
		txt[ti++]=atlas;
		col[ci++]=1f;
		col[ci++]=1f;
		col[ci++]=1f;
		col[ci++]=1f;
	}
}
