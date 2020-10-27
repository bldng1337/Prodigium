package me.engine.Utils.Event.Events;

import me.engine.Utils.Event.Event;

/**
 * @author Christian
 * RenderEvent
 */
public class Render extends Event {
	float deltatime;
	public Render(float dt) {
		deltatime=dt;
	}
	
	public float getDeltatime() {
		return deltatime;
	}

}
