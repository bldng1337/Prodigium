package me.engine.World.Tiles;

import org.joml.Vector2f;
import org.joml.Vector4f;

import me.engine.Engine;
import me.engine.Utils.Texture;
import me.engine.World.GameLevel;

public class WallTile implements ITile{
	long[] hwtex;
	long[] vwtex;
	long[] ftex;
	long hwend;
	long vwend;
	Vector4f bb;
	
	public WallTile(String[] hwalltex,String[] vwalltex,String[] floortex,String hwallend,String vwallend) {
		hwtex=new long[hwalltex.length];
		for(int i=0;i<hwtex.length;i++) {
			hwtex[i]=Engine.getEngine().getTex().getTexture(hwalltex[i]);
		}
		vwtex=new long[vwalltex.length];
		for(int i=0;i<vwtex.length;i++) {
			vwtex[i]=Engine.getEngine().getTex().getTexture(vwalltex[i]);
		}
		ftex=new long[floortex.length];
		for(int i=0;i<ftex.length;i++) {
			ftex[i]=Engine.getEngine().getTex().getTexture(floortex[i]);
		}
		
		hwend=Engine.getEngine().getTex().getTexture(hwallend);
		vwend=Engine.getEngine().getTex().getTexture(vwallend);
		
		
		
	}
	
	@Override
	public int render(Vector2f pos, float[] vertecies, float[] txt, float[] col, int i,GameLevel lvl) {
		float[][] ren=new float[][] {vertecies,txt,col};
		Vector2f tpos=new Vector2f();
		pos.div(ITile.SIZE, tpos);
		int walls=0;
		if(tpos.y>0)
		for(int m=-1;m<1;m++)
			if(tpos.x-1>=0&&lvl.getTile((int)tpos.x+m, (int)tpos.y-1) instanceof WallTile)
				walls++;
		final float shade=0.5f;
		float wthick=6;
		if(tpos.x>0&&tpos.x<lvl.getsize()-1&&lvl.getTile((int)tpos.x-1, (int)tpos.y) instanceof WallTile&&lvl.getTile((int)tpos.x+1, (int)tpos.y) instanceof WallTile) {
			// X-T-X
			i=render(new Vector4f(pos.x,pos.y,pos.x+Tile.SIZE,pos.y+Tile.SIZE),new Vector4f(0,0,1,1),hwtex[0],new Vector4f(shade,1f,1f,shade),i,ren);
		}else if(tpos.x>0&&lvl.getTile((int)tpos.x-1, (int)tpos.y) instanceof WallTile){
			// X-T-.
			i=render(new Vector4f(pos.x,pos.y,pos.x+Tile.SIZE,pos.y+Tile.SIZE),ftex[0],new Vector4f(1f),i,ren);
			i=render(new Vector4f(pos.x,pos.y,pos.x+Tile.SIZE/2f,pos.y+Tile.SIZE),new Vector4f(0,0,0.5f,1),hwtex[0],new Vector4f(shade,1f,1f,shade),i,ren);
			if(!(tpos.y<lvl.getsize()-1&&lvl.getTile((int)tpos.x, (int)tpos.y+1) instanceof WallTile))
				i=render(new Vector4f(pos.x+Tile.SIZE/2f-wthick,pos.y,pos.x+Tile.SIZE/2f,pos.y+Tile.SIZE),hwend,new Vector4f(shade,1f,1f,shade),i,ren);
		}else if(tpos.x<lvl.getsize()-1&&lvl.getTile((int)tpos.x+1, (int)tpos.y) instanceof WallTile){
			// .-T-X
			i=render(new Vector4f(pos.x,pos.y,pos.x+Tile.SIZE,pos.y+Tile.SIZE),ftex[0],new Vector4f(1f),i,ren);//floor
			
			i=render(new Vector4f(pos.x+Tile.SIZE,pos.y,pos.x+Tile.SIZE/2f,pos.y+Tile.SIZE),new Vector4f(0,0,0.5f,1),hwtex[0],new Vector4f(shade,1f,1f,shade),i,ren);
			if(!(tpos.y<lvl.getsize()-1&&lvl.getTile((int)tpos.x, (int)tpos.y+1) instanceof WallTile))
				i=render(new Vector4f(pos.x+Tile.SIZE/2f,pos.y,pos.x+Tile.SIZE/2f-wthick,pos.y+Tile.SIZE),hwend,new Vector4f(shade,1f,1f,shade),i,ren);
		}else {
			i=render(new Vector4f(pos.x,pos.y,pos.x+Tile.SIZE,pos.y+Tile.SIZE),ftex[0],new Vector4f(1f),i,ren);
			i=render(new Vector4f(pos.x+Tile.SIZE/2f-wthick/2,pos.y,pos.x+Tile.SIZE/2f+wthick/2,pos.y+Tile.SIZE),vwend,new Vector4f(shade,1f,1f,shade),i,ren);
		}
		if(tpos.y<lvl.getsize()-1&&lvl.getTile((int)tpos.x, (int)tpos.y+1) instanceof WallTile) {
			i=render(new Vector4f(pos.x+Tile.SIZE/2f-wthick/2,pos.y,pos.x+Tile.SIZE/2f+wthick/2,pos.y+Tile.SIZE),vwtex[0],new Vector4f(1f),i,ren);
		}
		return i;
	}
	
	public Vector4f getBB(Vector2f pos) {
		if(bb==null) {
			GameLevel lvl=Engine.getEngine().getCurrlevel();
			if(pos.x>0&&pos.x<lvl.getsize()-1&&
					lvl.getTile((int)pos.x-1, 
							(int)pos.y) instanceof WallTile&&
					lvl.getTile((int)pos.x+1, (int)pos.y) instanceof WallTile) {
				// X-T-X
				bb=new Vector4f(pos.x,pos.y,pos.x+1,pos.y+1);
			}else if(pos.x>0&&lvl.getTile((int)pos.x-1, (int)pos.y) instanceof WallTile){
				// X-T-.
				bb=new Vector4f(pos.x,pos.y,pos.x+1/2f,pos.y+1);
			}else if(pos.x<lvl.getsize()-1&&lvl.getTile((int)pos.x+1, (int)pos.y) instanceof WallTile){
				// .-T-X
				bb=new Vector4f(pos.x+1/2f,pos.y,pos.x+1,pos.y+1);
			}else {
				bb=new Vector4f(pos.x+1/2f-3f/Tile.SIZE,pos.y,pos.x+1/2f+3f/Tile.SIZE,pos.y+1);
			}
		}
		return bb;
	}
	
	@Override
	public boolean isCollideable() {
		return true;
	}
	
	@Override
	public long getPrimaryTex() {
		return hwtex[0];
	}
	
	private int render(Vector4f pos,long txt,Vector4f brit,int i,float[][] ren) {
			return render(pos,new Vector4f(0,0,1,1), txt, brit, i, ren);
	}
	
	private int render(Vector4f pos,Vector4f txtc,long txt,Vector4f b,int i,float[][] ren) {
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
