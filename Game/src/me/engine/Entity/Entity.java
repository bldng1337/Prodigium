package me.engine.Entity;

import java.io.IOException;

import me.engine.Main;

public class Entity {
	private long textureid;
	int x,y;
	public Entity(String texture) throws IOException {
		this.x=0;
		this.y=0;
	}
	
	public Entity(int x,int y,String texture) throws IOException {
		this.x=x;
		this.y=y;
	}
	
	public long getTextureid() {
		return textureid;
	}

	public void setTextureid(long textureid) {
		this.textureid = textureid;
	}
}
