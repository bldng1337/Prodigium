package me.engine.Entity;

import javax.script.ScriptEngine;

import me.engine.Scripting.ScriptManager;
import me.engine.Utils.Renderer;
import me.engine.Utils.Texture;

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
		r.renderRect(x, y, width, height, getTextureid(), 
				(int)(System.currentTimeMillis()/framedelay)
				%Texture.getaniframes(getTextureid()));
	}
	
	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
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
}

