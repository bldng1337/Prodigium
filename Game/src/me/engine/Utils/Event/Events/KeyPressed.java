package me.engine.Utils.Event.Events;

import me.engine.Utils.Event.Event;

public class KeyPressed extends Event{
	String k;
	public KeyPressed(String key) {
		k=key;
	}

	public String getKey() {
		return k;
	}
}
