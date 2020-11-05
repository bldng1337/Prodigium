package me.engine.World;

import java.util.Arrays;

import org.joml.Vector2i;

import me.engine.Engine;
import me.engine.Utils.ChunkRenderer;
import me.engine.Utils.Texture;
import me.engine.Utils.VertexBuffer;

/**
 * @author Christian
 * 
 */
public class Chunk {
	/**
	 * ChunkSize
	 */
	public static final int SIZE=40;
	/**
	 * Max distance before the Chunk gets unloaded
	 */
	public static final int UNLOADDIST=2;
	/**
	 * The Position of the Chunk
	 */
	Vector2i pos;
	/**
	 * List of Tiles of the Chunk
	 */
	Tile[][] tiles;
	/**
	 * The Buffer where the Tiles get saved onto if the Chunk is loaded
	 */
	VertexBuffer render;
	GameLevel l;
	
	public Chunk(int x,int y,GameLevel lvl) {
		l=lvl;
		pos=new Vector2i(x, y);
		tiles=new Tile[SIZE][SIZE];
	}
	
	/**
	 * @return A Array of all Tiles in this Chunk
	 */
	public Tile[][] getTiles() {
		return tiles;
	}
	
	public boolean shouldunload(Vector2i cpos) {
		return cpos.distance(pos)>=UNLOADDIST;
	}
	
	public void unloadChunk(ChunkRenderer r) {
		if(render==null)
			return;
		r.remove(render);
		render=null;
	}
	
	public void loadChunk(ChunkRenderer r) {
		if(render!=null)
			return;
		render=renderChunk();
		r.add(render);
	}
	
	/**
	 * Renders the Chunk onto an VertexBuffer
	 * @param size The Scale
	 * @return The Buffer
	 */
	public VertexBuffer renderChunk() {
		VertexBuffer vb=new VertexBuffer(true);
		float[] vertecies=new float[SIZE*SIZE*18];
		float[] txt=new float[SIZE*SIZE*18];
		int vi=0,ti=0;
		for(int cx=0;cx<SIZE;cx++) {
			for(int cy=0;cy<SIZE;cy++) {
				float x=cx*Tile.SIZE+(pos.x*SIZE*Tile.SIZE);
				float y=cy*Tile.SIZE+(pos.y*SIZE*Tile.SIZE);
				float tx=Texture.getx(tiles[cx][cy].texid);
				float ty=Texture.gety(tiles[cx][cy].texid);
				float tx2=Texture.getx(tiles[cx][cy].texid)+Texture.getdx(tiles[cx][cy].texid);
				float ty2=Texture.gety(tiles[cx][cy].texid)+Texture.getdy(tiles[cx][cy].texid);
				int atlas=Texture.getatlas(tiles[cx][cy].texid);
				tx/=Engine.getEngine().getTex().getMsize();
				ty/=Engine.getEngine().getTex().getMsize();
				tx2/=Engine.getEngine().getTex().getMsize();
				ty2/=Engine.getEngine().getTex().getMsize();
				
				vertecies[vi++]=x;
				vertecies[vi++]=y+Tile.SIZE;
				vertecies[vi++]=1;
				txt[ti++]=tx;
				txt[ti++]=ty2;
				txt[ti++]=atlas;
				
				vertecies[vi++]=x;
				vertecies[vi++]=y;
				vertecies[vi++]=1;
				txt[ti++]=tx;
				txt[ti++]=ty;
				txt[ti++]=atlas;
				
				vertecies[vi++]=x+Tile.SIZE;
				vertecies[vi++]=y;
				vertecies[vi++]=1;
				txt[ti++]=tx2;
				txt[ti++]=ty;
				txt[ti++]=atlas;
				
				vertecies[vi++]=x;
				vertecies[vi++]=y+Tile.SIZE;
				vertecies[vi++]=1;
				txt[ti++]=tx;
				txt[ti++]=ty2;
				txt[ti++]=atlas;
				
				vertecies[vi++]=x+Tile.SIZE;
				vertecies[vi++]=y+Tile.SIZE;
				vertecies[vi++]=1;
				txt[ti++]=tx2;
				txt[ti++]=ty2;
				txt[ti++]=atlas;
				
				vertecies[vi++]=x+Tile.SIZE;
				vertecies[vi++]=y;
				vertecies[vi++]=1;
				txt[ti++]=tx2;
				txt[ti++]=ty;
				txt[ti++]=atlas;
			}
		}
		vb.createBuffer(Arrays.copyOfRange(vertecies, 0,vi), 0, 3);
		vb.createBuffer(Arrays.copyOfRange(txt, 0,ti), 1, 3);
		return vb;
	}
}
