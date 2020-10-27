package me.engine.Utils.Event.Events;

import me.engine.Utils.Event.Event;

/**
 * @author Christian
 * Event for MousePresses
 */
public class MousePressed extends Event {
	double x,y;
	int key,pressed;
	
	public MousePressed(double mx, double my, int key, int pressed) {
		x=mx;
		y=my;
		this.key=key;
		this.pressed=pressed;
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
}
