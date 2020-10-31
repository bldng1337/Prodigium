package me.engine.Entity;

import java.util.concurrent.CompletableFuture;

import javax.script.ScriptEngine;

import org.joml.Vector2i;

import me.engine.Scripting.ScriptManager;
import me.engine.Utils.Renderer;
import me.engine.Utils.Texture;
import me.engine.World.Tile;

public class Entity{
	/**
	 * Array of Textures of that Entity
	 */
	protected long[] textureids;
	/**
	 * The Current Texture
	 */
	public Animation currTexture;
	/**
	 * Stats of that Entity
	 */
	public float x,y,health,speed,motionX,motionY;
	/**
	 * The Dimension of the Entity
	 */
	protected float width,height;
	/**
	 * The Delay between Frames of the Animation
	 */
	protected int framedelay;
	/**
	 * The Name of this Entity
	 */
	protected String name;
	/**
	 * The Script for this Entity
	 */
	protected ScriptEngine script;
	
	boolean renderflipped;
	
	CompletableFuture<Vector2i[]> pathfind;
	Vector2i[] path;

	protected Entity() {
		
	}

	/**
	 * @return TextureID
	 */
	public long getTextureid() {
		return textureids[currTexture.gettextureindex()];
	}
	
	public void update() {
		ScriptManager.invoke(script, "update");
	}
	
	public void render(Renderer r) {
		if(renderflipped)
			r.setTexCoords(1, 0, 0, 1);
		r.renderRect(x, y, width, height, getTextureid(), 
				(int)(System.currentTimeMillis()/framedelay)
				%Texture.getaniframes(getTextureid()));
		if(renderflipped)
			r.resetTexCoords();
	}
	
	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}
	
	public void pathfind(int x,int y) {
		if(!ispathfinding()) {
			pathfind = CompletableFuture.supplyAsync(() -> Pathfinder.AStar(new Vector2i((int)(this.x/Tile.SIZE),(int)(this.y/Tile.SIZE)), new Vector2i(x/Tile.SIZE,y/Tile.SIZE)));
			pathfind.whenComplete((a,b)->{
				path=a;
				if(path==null)
					path=new Vector2i[0];
			});
		}
	}
	
	public Vector2i[] getPath() {
		if(ispathfinding())
			return new Vector2i[0];
		return path;
	}
	
	public boolean ispathfinding() {
		return pathfind!=null&&!pathfind.isDone();
	}

	public int getFramedelay() {
		return framedelay;
	}

	public String getName() {
		return name;
	}

	public ScriptEngine getScript() {
		return script;
	}
	
	public void setRenderflipped(boolean renderflipped) {
		this.renderflipped = renderflipped;
	}

	public void destroy() {
		pathfind.cancel(true);
	}
	
	
}

