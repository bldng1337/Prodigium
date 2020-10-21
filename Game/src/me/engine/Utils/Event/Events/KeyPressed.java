package me.engine.Utils.Event.Events;

import me.engine.Utils.Event.Event;

/**
 * @author Christian
 * Event for KeyPresses
 */
public class KeyPressed extends Event{
	String k;
	public KeyPressed(String key) {
		k=key;
	}

	public String getKey() {
		return k;
	}
}
