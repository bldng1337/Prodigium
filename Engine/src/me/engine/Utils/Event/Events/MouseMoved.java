package me.engine.Utils.Event.Events;

import me.engine.Utils.Event.Event;

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
