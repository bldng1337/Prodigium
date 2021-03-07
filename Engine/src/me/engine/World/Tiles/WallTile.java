package me.engine.World.Tiles;

import org.joml.Vector2f;
import org.joml.Vector4f;

import me.engine.Engine;
import me.engine.World.GameLevel;

public class WallTile extends Tile{
	long[] hwtex;
	long[] vwtex;
	long[] ftex;
	long hwend;
	long vwend;
	Vector4f bb,lbb;
	
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
		pos.div(Tile.SIZE, tpos);
		final float shade=0.5f;
		float wthick=6;
		if(tpos.x>0&&tpos.x<lvl.getsize()-1&&lvl.getTile((int)tpos.x-1, (int)tpos.y) instanceof WallTile&&lvl.getTile((int)tpos.x+1, (int)tpos.y) instanceof WallTile) {
			// X-T-X
			i=render(new Vector4f(pos.x,pos.y,pos.x+STile.SIZE,pos.y+STile.SIZE),new Vector4f(0,0,1,1),hwtex[0],new Vector4f(shade,1f,1f,shade),i,ren);
		}else if(tpos.x>0&&lvl.getTile((int)tpos.x-1, (int)tpos.y) instanceof WallTile){
			// X-T-.
			i=render(new Vector4f(pos.x,pos.y,pos.x+STile.SIZE,pos.y+STile.SIZE),ftex[0],new Vector4f(1f),i,ren);
			i=render(new Vector4f(pos.x,pos.y,pos.x+STile.SIZE/2f,pos.y+STile.SIZE),new Vector4f(0,0,0.5f,1),hwtex[0],new Vector4f(shade,1f,1f,shade),i,ren);
			if(!(tpos.y<lvl.getsize()-1&&lvl.getTile((int)tpos.x, (int)tpos.y+1) instanceof WallTile))
				i=render(new Vector4f(pos.x+STile.SIZE/2f-wthick,pos.y,pos.x+STile.SIZE/2f,pos.y+STile.SIZE),hwend,new Vector4f(shade,1f,1f,shade),i,ren);
		}else if(tpos.x<lvl.getsize()-1&&lvl.getTile((int)tpos.x+1, (int)tpos.y) instanceof WallTile){
			// .-T-X
			i=render(new Vector4f(pos.x,pos.y,pos.x+STile.SIZE,pos.y+STile.SIZE),ftex[0],new Vector4f(1f),i,ren);//floor
			
			i=render(new Vector4f(pos.x+STile.SIZE,pos.y,pos.x+STile.SIZE/2f,pos.y+STile.SIZE),new Vector4f(0,0,0.5f,1),hwtex[0],new Vector4f(shade,1f,1f,shade),i,ren);
			if(!(tpos.y<lvl.getsize()-1&&lvl.getTile((int)tpos.x, (int)tpos.y+1) instanceof WallTile))
				i=render(new Vector4f(pos.x+STile.SIZE/2f,pos.y,pos.x+STile.SIZE/2f-wthick,pos.y+STile.SIZE),hwend,new Vector4f(shade,1f,1f,shade),i,ren);
		}else {
			i=render(new Vector4f(pos.x,pos.y,pos.x+STile.SIZE,pos.y+STile.SIZE),ftex[0],new Vector4f(1f),i,ren);
			i=render(new Vector4f(pos.x+STile.SIZE/2f-wthick/2,pos.y,pos.x+STile.SIZE/2f+wthick/2,pos.y+STile.SIZE),vwend,new Vector4f(shade,1f,1f,shade),i,ren);
		}
		if(tpos.y<lvl.getsize()-1&&lvl.getTile((int)tpos.x, (int)tpos.y+1) instanceof WallTile) {
			i=render(new Vector4f(pos.x+STile.SIZE/2f-wthick/2,pos.y,pos.x+STile.SIZE/2f+wthick/2,pos.y+STile.SIZE),vwtex[0],new Vector4f(1f),i,ren);
		}
		return i;
	}
	
	public Vector4f getBB(Vector2f pos) {
		if(bb==null) {
			final float extend=0.1f;
			GameLevel lvl=Engine.getEngine().getCurrlevel();
			if(pos.x>0&&pos.x<lvl.getsize()-1&&
					lvl.getTile((int)pos.x-1, 
							(int)pos.y) instanceof WallTile&&
					lvl.getTile((int)pos.x+1, (int)pos.y) instanceof WallTile) {
				// X-T-X
				bb=new Vector4f(pos.x-extend,pos.y,pos.x+1+extend,pos.y+1);
			}else if(pos.x>0&&lvl.getTile((int)pos.x-1, (int)pos.y) instanceof WallTile){
				// X-T-.
				bb=new Vector4f(pos.x-extend,pos.y,pos.x+0.5f+3f/STile.SIZE,pos.y+1);
			}else if(pos.x<lvl.getsize()-1&&lvl.getTile((int)pos.x+1, (int)pos.y) instanceof WallTile){
				// .-T-X
				bb=new Vector4f(pos.x+0.5f-3f/STile.SIZE,pos.y,pos.x+1+extend,pos.y+1);
			}else if(pos.y<lvl.getsize()-1&&lvl.getTile((int)pos.x, (int)pos.y+1) instanceof WallTile) {
				bb=new Vector4f(pos.x+1/2f-3f/STile.SIZE,pos.y,pos.x+1/2f+3f/STile.SIZE,pos.y+1+extend);
			}else {
				bb=new Vector4f(pos.x+1/2f-3f/STile.SIZE,pos.y-extend/10,pos.x+1/2f+3f/STile.SIZE+extend/10,pos.y+1);
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

	@Override
	public Vector4f getLightBB(Vector2f pos) {
		if(lbb==null) {
			final float extend=0.1f;
			GameLevel lvl=Engine.getEngine().getCurrlevel();
			if(pos.x>0&&pos.x<lvl.getsize()-1&&
					lvl.getTile((int)pos.x-1, 
							(int)pos.y) instanceof WallTile&&
					lvl.getTile((int)pos.x+1, (int)pos.y) instanceof WallTile) {
				// X-T-X
				lbb=new Vector4f(pos.x-extend,pos.y,pos.x+1+extend,pos.y+1f);
			}else if(pos.x>0&&lvl.getTile((int)pos.x-1, (int)pos.y) instanceof WallTile){
				// X-T-.
				lbb=new Vector4f(pos.x-extend,pos.y,pos.x+0.5f+3f/STile.SIZE,pos.y+1f);
			}else if(pos.x<lvl.getsize()-1&&lvl.getTile((int)pos.x+1, (int)pos.y) instanceof WallTile){
				// .-T-X
				lbb=new Vector4f(pos.x+0.5f-3f/STile.SIZE,pos.y,pos.x+1+extend,pos.y+1f);
			}else if(pos.y<lvl.getsize()-1&&lvl.getTile((int)pos.x, (int)pos.y+1) instanceof WallTile) {
				lbb=new Vector4f(pos.x+1/2f-3f/STile.SIZE,pos.y,pos.x+1/2f+3f/STile.SIZE,pos.y+1+extend);
			}else {
				lbb=new Vector4f(pos.x+1/2f-3f/STile.SIZE,pos.y-extend/10,pos.x+1/2f+3f/STile.SIZE+extend/10,pos.y+1);
			}
		}
		return lbb;
	}

}
