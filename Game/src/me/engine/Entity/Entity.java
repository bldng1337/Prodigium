package me.engine.Entity;

import javax.script.ScriptEngine;

import me.engine.Scripting.ScriptManager;
import me.engine.Utils.Renderer;
import me.engine.Utils.Texture;

public class Entity{
	protected long[] textureids;
	public Animation currTexture;
	public float x,y,health,speed;
	protected float width,height;
	protected int framedelay;
	protected String name;
	protected ScriptEngine script;
	
	protected Entity() {
		super();
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

