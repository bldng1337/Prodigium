package me.engine.Utils.Event.Events;

import org.lwjgl.glfw.GLFW;

import me.engine.Utils.Event.Event;

/**
 * @author Christian
 * Event for KeyPresses
 */
public class KeyPressed extends Event{

	private int key,scancode,action,mods;

	public KeyPressed(int key, int scancode, int action, int mods) {
		this.key=key;
		this.scancode=scancode;
		this.action=action;
		this.mods=mods;
	}

	public String getKey() {
		return GLFW.glfwGetKeyName(key, scancode);
	}
	public Action getAction() {
		return action==GLFW.GLFW_PRESS?Action.PRESSED:Action.RELEASED;
	}
	
	public int getMods() {
		return mods;
	}

	enum Action{
		PRESSED,RELEASED;
	}
}
