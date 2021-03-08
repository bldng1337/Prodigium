package me.engine.Utils.Event.Events;

import org.lwjgl.glfw.GLFW;

import me.engine.Utils.Event.Event;
import me.engine.Utils.Event.Events.KeyPressed.Action;

/**
 * @author Christian
 * Event for MousePresses
 */
public class MousePressed extends Event {
	double x,y,sx,sy;
	int key,pressed;
	
	public MousePressed(double mx, double my, double sx_, double sy_, int key, int pressed) {
		x=mx;
		y=my;
		sx=sx_;
		sy=sy_;
		this.key=key;
		this.pressed=pressed;
	}
	
	public double getSx() {
		return sx;
	}
	public double getSy() {
		return sy;
	}
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public int getKey() {
		return key;
	}
	public int getPressed() {
		return pressed;
	}
	public Action getAction() {
		for(Action a:Action.values())
			if(a.action==pressed)
				return a;
		return Action.UNKOWN;
	}
	
	public enum Action{
		PRESSED(GLFW.GLFW_PRESS),RELEASE(GLFW.GLFW_RELEASE),UNKOWN(-1);
		int action;
		Action(int id){
			this.action=id;
		};
	}
}
