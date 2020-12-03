package me.engine.World.Levels.Maze;


import java.util.Random;

import org.joml.Rectanglei;
import org.joml.Vector2i;

import me.engine.World.Chunk;
import me.engine.World.GameLevel;
import me.engine.World.Tile;
import me.engine.World.Generation.MazeGenerator;

public class MazeChunk extends Chunk
{
	public static MazeGenerator maze = new MazeGenerator(Chunk.SIZE,Chunk.SIZE);
	Random r=new Random();
	public MazeChunk(int x, int y,GameLevel l) {
		super(x, y,l);
		try {
		for(int xx=0;xx<Chunk.SIZE;xx++) {
			for(int yy=0;yy<Chunk.SIZE;yy++) {
				getTiles()[xx][yy] = maze.tiles[xx][yy];
			}
		}
		Rectanglei[] rl=new Rectanglei[(int) (Math.random()*40)];
		int d=0;
		int minsize=3;
		for(int i=0;i<rl.length;i++) {
			Vector2i v1=new Vector2i(r.nextInt(Chunk.SIZE-(minsize+1)),r.nextInt(Chunk.SIZE-(minsize+1)));
			Vector2i v2=new Vector2i(r.nextInt((int)(Chunk.SIZE-(v1.x+minsize+1)))+minsize,minsize+r.nextInt((int)(Chunk.SIZE-(v1.y+minsize+1))));
			v2.add(v1);
			Rectanglei room=new Rectanglei(v1, v2);
			boolean b=false;
			for(int m=0;m<i-d;m++) 
				if(rl[m].intersectsRectangle(room)) {
					b=true;
					d++;
					break;
				}
			if(!b) {
				genRoom(v1.x,v1.y,v2.x,v2.y);
				rl[i-d]=room;
			}
			
		}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	private void genRoom(int x,int y,int x2,int y2) {
		String[] txt = {"Textures.Wand.wand_Banner:png"};
		String[] s= {"Textures.Test.testground:png"};
		
		if(x>1) {
			for(int yy=y;yy<=y2;yy++) {
				if((yy>Chunk.SIZE&&yy<1)||((getTiles()[x-1][yy-1].isCollideable()^getTiles()[x-1][yy+1].isCollideable())&&r.nextBoolean()))
					getTiles()[x][yy] = new Tile(txt[r.nextInt(txt.length)],true);
			}
		}else {
			for(int yy=y;yy<=y2;yy++) {
				getTiles()[x][yy] = new Tile(txt[r.nextInt(txt.length)],true);
			}
		}
	
		if(x2<Chunk.SIZE-2) {
			for(int yy=y;yy<=y2;yy++) {
				if((yy>Chunk.SIZE&&yy<1)||(getTiles()[x2+1][yy-1].isCollideable()^getTiles()[x2+1][yy+1].isCollideable())&&r.nextBoolean())
					getTiles()[x2][yy] = new Tile(txt[r.nextInt(txt.length)],true);
			}
		}else {
			for(int yy=y;yy<=y2;yy++) {
				getTiles()[x2][yy] = new Tile(txt[r.nextInt(txt.length)],true);
			}
		}
		
		if(y>1) {
			for(int xx=x;xx<=x2;xx++) {
				if((xx>Chunk.SIZE&&xx<1)||(getTiles()[xx-1][y-1].isCollideable()^getTiles()[xx+1][y-1].isCollideable())&&r.nextBoolean())
					getTiles()[xx][y] = new Tile(txt[r.nextInt(txt.length)],true);
			}
		}else {
			for(int xx=x;xx<=x2;xx++) {
				getTiles()[xx][y] = new Tile(txt[r.nextInt(txt.length)],true);
			}
		}
		
		if(y2<Chunk.SIZE-2) {
			for(int xx=x;xx<=x2;xx++) {
				if((xx>Chunk.SIZE&&xx<1)||(getTiles()[xx-1][y2+1].isCollideable()^getTiles()[xx+1][y2+1].isCollideable())&&r.nextBoolean())
					getTiles()[xx][y2] = new Tile(txt[r.nextInt(txt.length)],true);
			}
		}else {
			for(int xx=x;xx<=x2;xx++) {
				getTiles()[xx][y2] = new Tile(txt[r.nextInt(txt.length)],true);
			}
		}
		
		for(int xx=x+1;xx<x2;xx++) {
			for(int yy=y+1;yy<y2;yy++) {
				getTiles()[xx][yy] =new Tile(s[r.nextInt(s.length)],false);
			}
		}
	}
}
