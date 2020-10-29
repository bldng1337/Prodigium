package me.engine.Utils;

import java.util.Deque;
import java.util.LinkedList;

import org.joml.Vector2f;
import org.joml.Vector3f;

import me.engine.Utils.Event.EventTarget;
import me.engine.Utils.Event.Events.Update;

/**
 * @author Christian
 * 2D Camera
 */
public class Camera {
	/**
	 * Translation of this Camera
	 */
	Vector2f translate;
	
	/**
	 * Queue of Scripted Movements for Camera drives
	 */
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
	
	/**
	 * Updates the Camera
	 * @param u Update for the EventSystem
	 */
	@EventTarget
	public void onUpdate(Update u) {
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
					onUpdate(u);
					return;
				}
				topos=new Vector2f(movement.element().x, movement.element().y).sub(stati);
			}
			frompos.lerp(topos, time/movement.element().z, translate);
		}else if(p!=null) {
			topos=null;
			translate.set(p.observe().sub(stati));
		}
	}
	
	public boolean shouldberendered(float x,float y,float width,float height) {
		return (x+width)>translate.x||x<(1920+translate.x)||
			   (y+height)>translate.y||y>(1080+translate.y);
	}
	
	public float getdistfromcamera(float x,float y) {
		return translate.distance(x, y);
	}
	
	/**
	 * @author Christian
	 * Used to control the Camera via an Lambda
	 */
	public abstract interface CameraPos{
		abstract Vector2f observe();
	}
	
	/**
	 * @return The Translation of the Camera
	 */
	public Vector2f getTranslate() {
		return translate;
	}

	/**
	 * @return Returns the Movement Stack for Scripted Camera Movement
	 */
	public Deque<Vector3f> getMovement() {
		return movement;
	}

	/**
	 * Set the CameraPos via callback
	 * @param p The new lambda to Control the Camera Position
	 */
	public void setP(CameraPos p) {
		this.p = p;
		onUpdate(null);
	}
	
	/**
	 * @return The Static Offset to center the Camera on something
	 */
	public Vector2f getStati() {
		return stati;
	}
}
