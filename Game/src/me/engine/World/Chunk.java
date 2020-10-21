package me.engine.World;

import java.util.Arrays;

import me.engine.Main;
import me.engine.Utils.Texture;
import me.engine.Utils.VertexBuffer;

/**
 * @author Christian
 * 
 */
public class Chunk {
	public static int SIZE=40;
	Tile[][] tiles;
	
	public Chunk() {
		tiles=new Tile[SIZE][SIZE];
	}
	
	public Tile[][] getTiles() {
		return tiles;
	}
	
	/**
	 * Renders the Chunk onto an VertexBuffer
	 * @param xx The X Coordinate
	 * @param yy The Y Coordinate
	 * @param size The Scale
	 * @return The Buffer
	 */
	public VertexBuffer renderChunk(int xx,int yy,int size) {
		VertexBuffer vb=new VertexBuffer(true);
		float[] vertecies=new float[SIZE*SIZE*18];
		float[] txt=new float[SIZE*SIZE*18];
		int vi=0,ti=0;
		for(int cx=0;cx<SIZE;cx++) {
			for(int cy=0;cy<SIZE;cy++) {
				float tx=Texture.getx(tiles[cx][cy].texid);
				float ty=Texture.gety(tiles[cx][cy].texid);
				float tx2=Texture.getx(tiles[cx][cy].texid)+Texture.getdx(tiles[cx][cy].texid);
				float ty2=Texture.gety(tiles[cx][cy].texid)+Texture.getdy(tiles[cx][cy].texid);
				int atlas=Texture.getatlas(tiles[cx][cy].texid);
				tx/=Main.getTex().getMsize();
				ty/=Main.getTex().getMsize();
				tx2/=Main.getTex().getMsize();
				ty2/=Main.getTex().getMsize();
				float x=cx*size+xx;
				float y=cy*size+yy;
				
				vertecies[vi++]=x;
				vertecies[vi++]=y+size;
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
				
				vertecies[vi++]=x+size;
				vertecies[vi++]=y;
				vertecies[vi++]=1;
				txt[ti++]=tx2;
				txt[ti++]=ty;
				txt[ti++]=atlas;
				
				vertecies[vi++]=x;
				vertecies[vi++]=y+size;
				vertecies[vi++]=1;
				txt[ti++]=tx;
				txt[ti++]=ty2;
				txt[ti++]=atlas;
				
				vertecies[vi++]=x+size;
				vertecies[vi++]=y+size;
				vertecies[vi++]=1;
				txt[ti++]=tx2;
				txt[ti++]=ty2;
				txt[ti++]=atlas;
				
				vertecies[vi++]=x+size;
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
