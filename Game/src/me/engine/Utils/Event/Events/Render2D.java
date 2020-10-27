package me.engine.Utils.Event.Events;

import me.engine.Utils.Event.Event;

public class Render2D extends Event{
	float deltatime;
	public Render2D(float dt) {
		deltatime=dt;
	}
	
	public float getDeltatime() {
		return deltatime;
	}

}
