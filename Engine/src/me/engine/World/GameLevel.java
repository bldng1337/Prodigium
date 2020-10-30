package me.engine.World;

import java.util.ArrayList;

import org.joml.Rectanglef;
import org.joml.Vector2f;
import org.joml.Vector2i;

import me.engine.Engine;
import me.engine.Entity.Entity;

public class GameLevel {
	/**
	 * Loaded Entities of this Level
	 */
	ArrayList<Entity> entitylist=new ArrayList<>();
	
	protected Chunk[][] chunks;
	
	public GameLevel(int size) {
		chunks=new Chunk[size][size];
	}
	
	public void render() {
		Vector2i cpos=new Vector2i((int)Engine.getEngine().getRender().c.getTranslate().x/(Chunk.SIZE*Tile.SIZE),(int)Engine.getEngine().getRender().c.getTranslate().y/(Chunk.SIZE*Tile.SIZE));
		final int scansize=5;
		for(int x=-scansize;x<scansize;x++) {
			for(int y=-scansize;y<scansize;y++) {
				if(cpos.x+x<0||cpos.y+y<0||cpos.x+x>chunks.length||cpos.y+y>chunks.length)
					continue;
				Chunk c=chunks[cpos.x+x][cpos.y+y];
				if(c.shouldunload(cpos))
					c.unloadChunk(Engine.getEngine().getChunkrenderer());
				else
					c.loadChunk(Engine.getEngine().getChunkrenderer());
			}
		}
		for(Entity e:entitylist) {
			if(Engine.getEngine().getRender().c.getdistfromcamera(e.x, e.y)>3000)
				return;
			if(Engine.getEngine().getRender().c.shouldberendered(e.x, e.y, e.getWidth(), e.getHeight()))
				e.render(Engine.getEngine().getRender());
		}
	}
	
	public void addEntity(Entity e) {
		entitylist.add(e);
	}
	
	public void update() {
		for(Entity e:entitylist) {
			if(Engine.getEngine().getRender().c.getdistfromcamera(e.x, e.y)>3000)
				return;
			e.update();
			updatemotion(e);
		}
	}
	
	public Tile getTile(int x,int y) {
		int cx=x/Chunk.SIZE;
		int cy=y/Chunk.SIZE;
		return chunks[cx][cy].getTiles()[x-cx*Chunk.SIZE][y-cy*Chunk.SIZE];
	}
	

	private void updatemotion(Entity e) {
		e.x+=e.motionX;
		e.y+=e.motionY;
		e.motionX/=1.4f;
		e.motionY/=1.4f;
		if(e.motionX<0.1&&e.motionX>-0.1)
			e.motionX=0;
		if(e.motionY<0.1&&e.motionY>-0.1)
			e.motionY=0;
		if(e.motionX!=0||e.motionY!=0)
			resolveCollision(e);
	}
	
	private void resolveCollision(Entity e) {
		Rectanglef entity=new Rectanglef(e.x,e.y,e.x+e.getWidth(), e.y+e.getHeight());
		for(int x=(int) (entity.minX/Tile.SIZE-1);x<entity.maxX/Tile.SIZE+1;x++) {
			for(int y=(int) (entity.minY/Tile.SIZE-1);y<entity.maxY/Tile.SIZE+1;y++) {
				if((x<0||y<0||y>Chunk.SIZE*chunks.length*Tile.SIZE||x>Chunk.SIZE*chunks.length*Tile.SIZE)||!getTile(x,y).collideable)
					continue;
				Rectanglef tile=new Rectanglef(x*(float)Tile.SIZE, y*(float)Tile.SIZE, (x+1)*(float)Tile.SIZE, (y+1)*(float)Tile.SIZE);
				if(tile.intersectsRectangle(entity)) {
					Rectanglef collision =new Rectanglef();
					entity.intersection(tile, collision);
					if(!collision.isValid())
						continue;
					Vector2f ce=center(entity);
					Vector2f ct=center(tile);
					float intersectx=collision.lengthX()*((ce.x-ct.x)>0?1:-1);
					float intersecty=collision.lengthY()*((ce.y-ct.y)>0?1:-1);
					if(Math.abs(intersectx)<Math.abs(intersecty)) {
						e.motionX=0;
						e.x+=intersectx;
					}else {
						e.motionY=0;
						e.y+=intersecty;
					}
				}
			}
		}
	}
	
	
	private Vector2f center(Rectanglef rect) {
		return new Vector2f((rect.maxX+rect.minX)/2, (rect.maxY+rect.minY)/2);
	}
	
	
	

}
