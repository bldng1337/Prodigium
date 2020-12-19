package me.engine.World.Tiles;

import org.joml.Vector2f;
import org.joml.Vector4f;

import me.engine.Engine;
import me.engine.Utils.Texture;
import me.engine.World.GameLevel;

/**
 * @author Christian
 *
 */
public class Tile implements ITile {
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
		return render(new Vector4f(pos.x,pos.y,pos.x+Tile.SIZE,pos.y+Tile.SIZE), texid, new Vector4f(1), i, new float[][] {vertecies,txt,col});
	}
	
	private int render(Vector4f pos,long txt,Vector4f col,int i,float[][] ren) {
		int vi=i*(3*6),ti=i*(3*6),ci=i*(4*6);
		float tx=Texture.getx(txt);
		float ty=Texture.gety(txt);
		float tx2=Texture.getx(txt)+Texture.getdx(txt);
		float ty2=Texture.gety(txt)+Texture.getdy(txt);
		int atlas=Texture.getatlas(txt);
		tx/=Engine.getEngine().getTex().getMsize();
		ty/=Engine.getEngine().getTex().getMsize();
		tx2/=Engine.getEngine().getTex().getMsize();
		ty2/=Engine.getEngine().getTex().getMsize();
		ren[0][vi++]=pos.x;//0
		ren[0][vi++]=pos.w;//1
		ren[0][vi++]=1;
		ren[1][ti++]=tx;
		ren[1][ti++]=ty2;
		ren[1][ti++]=atlas;
		ren[2][ci++]=1f;
		ren[2][ci++]=1f;
		ren[2][ci++]=1f;
		ren[2][ci++]=1f;
		
		ren[0][vi++]=pos.x;//0
		ren[0][vi++]=pos.y;//0
		ren[0][vi++]=1;
		ren[1][ti++]=tx;
		ren[1][ti++]=ty;
		ren[1][ti++]=atlas;
		ren[2][ci++]=1f;
		ren[2][ci++]=1f;
		ren[2][ci++]=1f;
		ren[2][ci++]=1f;
		
		ren[0][vi++]=pos.z;//1
		ren[0][vi++]=pos.y;//0
		ren[0][vi++]=1;
		ren[1][ti++]=tx2;
		ren[1][ti++]=ty;
		ren[1][ti++]=atlas;
		ren[2][ci++]=1f;
		ren[2][ci++]=1f;
		ren[2][ci++]=1f;
		ren[2][ci++]=1f;
		
		ren[0][vi++]=pos.x;//0
		ren[0][vi++]=pos.w;//1
		ren[0][vi++]=1;
		ren[1][ti++]=tx;
		ren[1][ti++]=ty2;
		ren[1][ti++]=atlas;
		ren[2][ci++]=1f;
		ren[2][ci++]=1f;
		ren[2][ci++]=1f;
		ren[2][ci++]=1f;
		
		ren[0][vi++]=pos.z;//1
		ren[0][vi++]=pos.w;//1
		ren[0][vi++]=1;
		ren[1][ti++]=tx2;
		ren[1][ti++]=ty2;
		ren[1][ti++]=atlas;
		ren[2][ci++]=1f;
		ren[2][ci++]=1f;
		ren[2][ci++]=1f;
		ren[2][ci++]=1f;
		
		ren[0][vi++]=pos.z;//1
		ren[0][vi++]=pos.y;//0
		ren[0][vi++]=1;
		ren[1][ti++]=tx2;
		ren[1][ti++]=ty;
		ren[1][ti++]=atlas;
		ren[2][ci++]=1f;
		ren[2][ci++]=1f;
		ren[2][ci++]=1f;
		ren[2][ci++]=1f;
		return vi/(3*6);
	}

	@Override
	public Vector4f getBB(Vector2f pos) {
		return new Vector4f(pos.x,pos.y,pos.x+1,pos.y+1);
	}
}
