package me.engine.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector4f;

import me.engine.Utils.ChunkRenderer;
import me.engine.Utils.VertexBuffer;
import me.engine.World.Tiles.STile;
import me.engine.World.Tiles.Tile;
import me.engine.World.Tiles.Tile.Edges;

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
	List<Vector4f> edges;
	
	public Chunk(int x,int y,GameLevel lvl) {
		l=lvl;
		edges=new ArrayList<>();
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
		long dt=System.nanoTime();
		edges.clear();
		VertexBuffer vb=new VertexBuffer(true);
		float[] vertecies=new float[(SIZE*SIZE*6*3)*4];
		float[] txt=new float[(SIZE*SIZE*6*3)*4];
		float[] col=new float[(SIZE*SIZE*6*4)*4];
		int i=0;
		for(int cx=0;cx<SIZE;cx++) {
			for(int cy=0;cy<SIZE;cy++) {
				float x=cx*STile.SIZE+(pos.x*SIZE*STile.SIZE);
				float y=cy*STile.SIZE+(pos.y*SIZE*STile.SIZE);
				Tile ctile=tiles[cx][cy];
				i=ctile.render(new Vector2f(x,y),vertecies,txt,col,i,l);
			}
		}
		
		vb.createBuffer(Arrays.copyOfRange(vertecies, 0, i*(3*6)), 0, 3);
		vb.createBuffer(Arrays.copyOfRange(txt, 0, i*(3*6)), 1, 3);
		vb.createBuffer(Arrays.copyOfRange(col, 0, i*(4*6)), 2, 4);
		System.out.println("Rendering took "+(System.nanoTime()-dt)/1000000);
		return vb;
	}
	
	public void batchHitboxes() {
		for(int cx=0;cx<SIZE;cx++) {
			for(int cy=0;cy<SIZE;cy++) {
				
			}
		}
	}
	
	private void mergeHitbox(Vector2f a,Vector2f b,Tile.Edges e) {
		Tile ta=tiles[(int) a.x][(int) a.y];
		Tile tb=tiles[(int) b.x][(int) b.y];
		
		
	}
	
	
}
