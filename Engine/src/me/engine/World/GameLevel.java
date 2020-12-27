package me.engine.World;

import java.util.ArrayList;
import java.util.Random;

import org.joml.Rectanglef;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector4f;

import me.engine.Engine;
import me.engine.Entity.Entity;
import me.engine.World.Tiles.ITile;
import me.engine.World.Tiles.Tile;

public abstract class GameLevel {
	/**
	 * Loaded Entities of this Level
	 */
	ArrayList<Entity> entitylist=new ArrayList<>();
	
	Entity player;
	
	protected Chunk[][] chunks;
	protected static Random rand=new Random();
	
	public GameLevel(int size) {
		chunks=new Chunk[size][size];
	}
	
	public void render() {
		Vector2i cpos=new Vector2i((int)Engine.getEngine().getRender().c.getTranslate().x/(Chunk.SIZE*Tile.SIZE),(int)Engine.getEngine().getRender().c.getTranslate().y/(Chunk.SIZE*Tile.SIZE));
		final int scansize=5;
		for(int x=-scansize;x<scansize;x++) {
			for(int y=-scansize;y<scansize;y++) {
				if(cpos.x+x<0||cpos.y+y<0||cpos.x+x>chunks.length-1||cpos.y+y>chunks.length-1)
					continue;
				
				Chunk c=chunks[cpos.x+x][cpos.y+y];
				if(c.shouldunload(cpos))
					c.unloadChunk(Engine.getEngine().getChunkrenderer());
				else
					c.loadChunk(Engine.getEngine().getChunkrenderer());
			}
		}
		for(int i=entitylist.size()-1;i>=0;i--) {
			Entity e=entitylist.get(i);
			if(Engine.getEngine().getRender().c.getdistfromcamera(e.x, e.y)>3000)
				return;
			if(Engine.getEngine().getRender().c.shouldberendered(e.x, e.y, e.getWidth(), e.getHeight()))
				e.render(Engine.getEngine().getRender());
		}
	}
	
	public abstract void spawnEnemy(int x, int y);
	
	public void addEntity(Entity e) {
		e.setLevel(this);
		entitylist.add(e);
	}
	
	public void removeEntity(Entity entity) {
		entitylist.remove(entity);
	}
	
	Random r=new Random();
	long time=0;
	public void update() {
		Engine.getEngine().getFontRenderer().draw(""+entitylist.size(),500,500,20);
		for(Entity e:entitylist) {
			if(Engine.getEngine().getRender().c.getdistfromcamera(e.x, e.y)>3000)
				return;
			e.update();
			updatemotion(e);
		}
		if(entitylist.size()<40&&player!=null&&System.currentTimeMillis()-time>5000) {
			time=System.currentTimeMillis();
			int x=(r.nextInt(1000));
			int y=(r.nextInt(1000));
			x*=r.nextInt(1)-1;
			y*=r.nextInt(1)-1;
			x+=player.x;
			y+=player.y;
			if(x<0||y<0)
				return;
			if(!getTile(x/Tile.SIZE, y/Tile.SIZE).isCollideable())
				spawnEnemy(x, y);
		}
	}
	
	public ITile getTile(int x,int y) {
		int cx=x/Chunk.SIZE;
		int cy=y/Chunk.SIZE;
		return chunks[cx][cy].getTiles()[x-cx*Chunk.SIZE][y-cy*Chunk.SIZE];
	}
	

	private void updatemotion(Entity e) {
		e.motionX=Math.max(Math.min(e.motionX, 150), -150);
		e.motionY=Math.max(Math.min(e.motionY, 150), -150);
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
	
	public void setPlayer(Entity e) {
		player=e;
	}
	public int getsize() {
		return chunks.length;
	}
	
	private void resolveCollision(Entity e) {
		Rectanglef entity=new Rectanglef(e.x,e.y,e.x+e.getWidth(), e.y+e.getHeight());
		for(int x=(int) (entity.minX/Tile.SIZE-1);x<entity.maxX/Tile.SIZE+1;x++) {
			for(int y=(int) (entity.minY/Tile.SIZE-1);y<entity.maxY/Tile.SIZE+1;y++) {
				if(x<0||y<0)
					continue;
				ITile t=getTile(x,y);
				if((y>Chunk.SIZE*chunks.length*Tile.SIZE||x>Chunk.SIZE*chunks.length*Tile.SIZE)||!t.isCollideable())
					continue;
				Vector4f v=t.getBB(new Vector2f(x,y));
				Rectanglef tile=new Rectanglef(v.x*Tile.SIZE, v.y*Tile.SIZE, v.z*Tile.SIZE, v.w*Tile.SIZE);
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
	
	
	public Entity getPlayer() {
		return player;
	}

	private Vector2f center(Rectanglef rect) {
		return new Vector2f((rect.maxX+rect.minX)/2, (rect.maxY+rect.minY)/2);
	}

	public void destroy() {
		for(Entity e: entitylist)
			e.destroy();
	}

	public ArrayList<Entity> getEntitys() {
		return entitylist;
	}

	

}
