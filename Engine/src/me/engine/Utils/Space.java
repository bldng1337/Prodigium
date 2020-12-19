package me.engine.Utils;

import org.joml.Vector2f;

import me.engine.Engine;

public class Space {

	private Space() {}
	
//	public Vector2f screentoworld(Vector2f v) {
//		
//	}
	
	public static Vector2f worldtoscreen(Vector2f v) {
		Vector2f re=new Vector2f();
		v.sub(Engine.getEngine().getRender().c.getTranslate(), re);
		return re;
	}
}
