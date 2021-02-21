package me.engine.World.Tiles;

import org.joml.AABBf;
import org.joml.Intersectionf;
import org.joml.Rayf;
import org.joml.Rectanglef;
import org.joml.Vector2f;
import org.joml.Vector4f;

import me.engine.Engine;
import me.engine.Utils.MathUtils;
import me.engine.Utils.TextureAtlas;
import me.engine.World.GameLevel;

public abstract class Tile {
	/**
	 * Size of one Tile
	 */
	public static final int SIZE=100;
	
	public abstract int render(Vector2f pos,float[] vertecies, float[] txt, float[] col, int i,GameLevel l);

	public abstract Vector4f getBB(Vector2f pos);
	
	public float[][] getEdges(Vector2f pos) {
		Vector4f bb=getBB(pos);
		float[][] e=new float[4][2];
		e[0]=new float[] {bb.x,bb.y};
		e[1]=new float[] {bb.x,bb.w};
		e[2]=new float[] {bb.z,bb.w};
		e[3]=new float[] {bb.z,bb.y};
		return e;
	}
	
	public abstract boolean isCollideable();
	
	public abstract long getPrimaryTex();
	
	int[] edgeid;
	
	public int getEdge(Edges e) {
		return edgeid[e.b];
	}
	
	public void setEdge(Edges e, int i) {
		edgeid[e.b]=i;
	}
	
	public enum Edges{
		NORTH((byte)0),SOUTH((byte)1),EAST((byte)2),WEST((byte)3);
		byte b;
		Edges(byte b){
			this.b=b;
		}
	}
	
	protected int render(Vector4f pos,long txt,Vector4f brit,int i,float[][] ren) {
		return render(pos,new Vector4f(0,0,1,1), txt, brit, i, ren);
	}
	
	
	public Vector2f raycast(Vector4f ray,Vector2f pos) {
		return MathUtils.rayrect(getBB(pos), ray);
	}

	protected int render(Vector4f pos,Vector4f txtc,long txt,Vector4f b,int i,float[][] ren) {
		int vi=i*(3*6),ti=i*(3*6),ci=i*(4*6);
		float tx=TextureAtlas.getx(txt)+TextureAtlas.getdx(txt)*txtc.x;
		float ty=TextureAtlas.gety(txt)+TextureAtlas.getdy(txt)*txtc.y;
		float tx2=TextureAtlas.getx(txt)+TextureAtlas.getdx(txt)*txtc.z;
		float ty2=TextureAtlas.gety(txt)+TextureAtlas.getdy(txt)*txtc.w;
		int atlas=TextureAtlas.getatlas(txt);
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
		ren[2][ci++]=b.x;
		ren[2][ci++]=b.x;
		ren[2][ci++]=b.x;
		ren[2][ci++]=1f;
		
		ren[0][vi++]=pos.x;//0
		ren[0][vi++]=pos.y;//0
		ren[0][vi++]=1;
		ren[1][ti++]=tx;
		ren[1][ti++]=ty;
		ren[1][ti++]=atlas;
		ren[2][ci++]=b.y;
		ren[2][ci++]=b.y;
		ren[2][ci++]=b.y;
		ren[2][ci++]=1f;
		
		ren[0][vi++]=pos.z;//1
		ren[0][vi++]=pos.y;//0
		ren[0][vi++]=1;
		ren[1][ti++]=tx2;
		ren[1][ti++]=ty;
		ren[1][ti++]=atlas;
		ren[2][ci++]=b.z;
		ren[2][ci++]=b.z;
		ren[2][ci++]=b.z;
		ren[2][ci++]=1f;
		
		ren[0][vi++]=pos.x;//0
		ren[0][vi++]=pos.w;//1
		ren[0][vi++]=1;
		ren[1][ti++]=tx;
		ren[1][ti++]=ty2;
		ren[1][ti++]=atlas;
		ren[2][ci++]=b.x;
		ren[2][ci++]=b.x;
		ren[2][ci++]=b.x;
		ren[2][ci++]=1f;
		
		ren[0][vi++]=pos.z;//1
		ren[0][vi++]=pos.w;//1
		ren[0][vi++]=1;
		ren[1][ti++]=tx2;
		ren[1][ti++]=ty2;
		ren[1][ti++]=atlas;
		ren[2][ci++]=b.w;
		ren[2][ci++]=b.w;
		ren[2][ci++]=b.w;
		ren[2][ci++]=1f;
		
		ren[0][vi++]=pos.z;//1
		ren[0][vi++]=pos.y;//0
		ren[0][vi++]=1;
		ren[1][ti++]=tx2;
		ren[1][ti++]=ty;
		ren[1][ti++]=atlas;
		ren[2][ci++]=b.z;
		ren[2][ci++]=b.z;
		ren[2][ci++]=b.z;
		ren[2][ci++]=1f;
		return vi/(3*6);
	}
	
}
