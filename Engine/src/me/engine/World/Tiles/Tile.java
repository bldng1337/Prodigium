package me.engine.World.Tiles;

import org.joml.AABBf;
import org.joml.Intersectionf;
import org.joml.Rayf;
import org.joml.Rectanglef;
import org.joml.Vector2f;
import org.joml.Vector4f;

import me.engine.Engine;
import me.engine.Utils.Texture;
import me.engine.World.GameLevel;

public abstract class Tile {
	/**
	 * Size of one Tile
	 */
	public static final int SIZE=100;
	
	public abstract int render(Vector2f pos,float[] vertecies, float[] txt, float[] col, int i,GameLevel l);

	public abstract Vector4f getBB(Vector2f pos);
	
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
	
	
	public Vector2f raycast(Vector2f s,Vector2f e,Vector2f pos) {
		Vector4f AABB=getBB(pos);
		Vector2f r=new Vector2f();
		if(Intersectionf.intersectRayAab(new Rayf(s.x,s.y,0.5f,e.x-s.x,e.y-s.y,0.5f), new AABBf(AABB.x, AABB.y, 0, AABB.z, AABB.w,1), r)) {
//			System.out.println(r);
			return r;
		}
		return null;
	}

	protected int render(Vector4f pos,Vector4f txtc,long txt,Vector4f b,int i,float[][] ren) {
		int vi=i*(3*6),ti=i*(3*6),ci=i*(4*6);
		float tx=Texture.getx(txt)+Texture.getdx(txt)*txtc.x;
		float ty=Texture.gety(txt)+Texture.getdy(txt)*txtc.y;
		float tx2=Texture.getx(txt)+Texture.getdx(txt)*txtc.z;
		float ty2=Texture.gety(txt)+Texture.getdy(txt)*txtc.w;
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
