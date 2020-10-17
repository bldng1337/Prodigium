package me.engine.Utils;

import java.util.Deque;
import java.util.LinkedList;

import org.joml.Vector2f;
import org.joml.Vector3f;

import me.engine.Utils.Event.EventTarget;

public class Camera {
	Vector2f translate;
	Deque<Vector3f> movement;
	Vector2f frompos,topos,stati;
	long timestamp;
	CameraPos p;
	
	public Camera() {
		movement=new LinkedList<>();
		translate=new Vector2f(0,0);
		stati=new Vector2f();
		topos=null;
	}
	
	@EventTarget
	public void Update(me.engine.Utils.Event.Events.Update u) {
		if(!movement.isEmpty()) {
			if(topos==null) {
				timestamp=System.currentTimeMillis();
				topos=new Vector2f(movement.element().x, movement.element().y).sub(stati);
				frompos=new Vector2f();
				frompos.set(translate);
			}
			float time=(int) (System.currentTimeMillis()-timestamp);
			if(time>=movement.element().z) {
				timestamp=System.currentTimeMillis();
				frompos.set(topos);
				movement.pop();
				if(movement.isEmpty()) {
					Update(null);
					return;
				}
				topos=new Vector2f(movement.element().x, movement.element().y).sub(stati);
			}
			System.out.println(topos);
			frompos.lerp(topos, time/movement.element().z, translate);
		}else if(p!=null) {
			topos=null;
			translate.set(p.observe().sub(stati));
		}
	}
	
	
	
	public abstract interface CameraPos{
		abstract Vector2f observe();
	}
	
	public Vector2f getTranslate() {
		return translate;
	}

	public Deque<Vector3f> getMovement() {
		return movement;
	}

	public void setP(CameraPos p) {
		this.p = p;
		Update(null);
	}
	
	public Vector2f getStati() {
		return stati;
	}
}
