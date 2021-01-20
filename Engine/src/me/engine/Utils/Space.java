package me.engine.Utils;

import org.joml.Vector2f;

import me.engine.Engine;

public class Space {

	private Space() {}
	
	public static Vector2f screentoworldr(Vector2f v) {
		Vector2f re=new Vector2f();
		v.add(Engine.getEngine().getRender().c.getTranslate(), re);
		return re;
	}
	public static void screentoworld(Vector2f v) {
		v.add(Engine.getEngine().getRender().c.getTranslate());
	}
	
	public static Vector2f worldtoscreen(Vector2f v) {
		Vector2f re=new Vector2f();
		v.sub(Engine.getEngine().getRender().c.getTranslate(), re);
		return re;
	}
}
