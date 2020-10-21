package me.engine.Utils.Event.Events;

import me.engine.Utils.Event.Event;

/**
 * @author Christian
 * Event for Mouse Movement
 */
public class MouseMoved extends Event {
	double x,y;
	
	public MouseMoved(double x,double y) {
		this.x=x;
		this.y=y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

}
