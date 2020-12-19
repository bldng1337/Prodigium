package me.engine.World;

import java.util.Arrays;

import org.joml.Vector2f;
import org.joml.Vector2i;

import me.engine.Utils.ChunkRenderer;
import me.engine.Utils.VertexBuffer;
import me.engine.World.Tiles.ITile;
import me.engine.World.Tiles.Tile;

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
	ITile[][] tiles;
	/**
	 * The Buffer where the Tiles get saved onto if the Chunk is loaded
	 */
	VertexBuffer render;
	GameLevel l;
	
	public Chunk(int x,int y,GameLevel lvl) {
		l=lvl;
		pos=new Vector2i(x, y);
		tiles=new ITile[SIZE][SIZE];
	}
	
	/**
	 * @return A Array of all Tiles in this Chunk
	 */
	public ITile[][] getTiles() {
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
		float[] vertecies=new float[(SIZE*SIZE*6*3)*4];
		float[] txt=new float[(SIZE*SIZE*6*3)*4];
		float[] col=new float[(SIZE*SIZE*6*4)*4];
		int i=0;
		for(int cx=0;cx<SIZE;cx++) {
			for(int cy=0;cy<SIZE;cy++) {
				float x=cx*Tile.SIZE+(pos.x*SIZE*Tile.SIZE);
				float y=cy*Tile.SIZE+(pos.y*SIZE*Tile.SIZE);
				i=tiles[cx][cy].render(new Vector2f(x,y),vertecies,txt,col,i,l);
			}
		}
		
		vb.createBuffer(Arrays.copyOfRange(vertecies, 0, i*(3*6)), 0, 3);
		vb.createBuffer(Arrays.copyOfRange(txt, 0, i*(3*6)), 1, 3);
		vb.createBuffer(Arrays.copyOfRange(col, 0, i*(4*6)), 2, 4);
		return vb;
	}
}
