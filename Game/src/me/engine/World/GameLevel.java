package me.engine.World;

import java.util.ArrayList;

import org.joml.Vector2i;

import me.engine.Entity.Entity;
import me.engine.Utils.ChunkRenderer;
import me.engine.Utils.Renderer;

public class GameLevel {
	Renderer r;
	ChunkRenderer cr;
	ArrayList<Entity> entitylist=new ArrayList<>();
	protected Chunk[][] chunks;
	
	public GameLevel(int size,Renderer r,ChunkRenderer cr) {
		this.r=r;
		this.cr=cr;
		chunks=new Chunk[size][size];
	}
	
	public void render() {
		Vector2i cpos=new Vector2i((int)r.c.getTranslate().x/(Chunk.SIZE*Tile.SIZE),(int)r.c.getTranslate().y/(Chunk.SIZE*Tile.SIZE));
		final int scansize=5;
		for(int x=-scansize;x<scansize;x++) {
			for(int y=-scansize;y<scansize;y++) {
				if(cpos.x+x<0||cpos.y+y<0||cpos.x+x>chunks.length||cpos.y+y>chunks.length)
					continue;
				Chunk c=chunks[cpos.x+x][cpos.y+y];
				if(c.shouldunload(cpos))
					c.unloadChunk(cr);
				else
					c.loadChunk(cr);
			}
		}
		for(Entity e:entitylist) {
			if(r.c.getdistfromcamera(e.x, e.y)>3000)
				return;
			if(r.c.shouldberendered(e.x, e.y, e.getWidth(), e.getHeight()))
				e.render(r);
		}
	}
	
	public void update() {
		for(Entity e:entitylist) {
			if(r.c.getdistfromcamera(e.x, e.y)>3000)
				return;
			e.update();
		}
	}

}
