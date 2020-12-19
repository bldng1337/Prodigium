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
		for(Action a:Action.values())
			if(a.action==action)
				return a;
		return Action.UNKOWN;
	}
	
	public boolean isKeyDown() {
		return action==GLFW.GLFW_PRESS||action==GLFW.GLFW_REPEAT;
	}
	
	public int getMods() {
		return mods;
	}

	public enum Action{
		PRESSED(GLFW.GLFW_PRESS),RELEASED(GLFW.GLFW_RELEASE),REPEAT(GLFW.GLFW_REPEAT),UNKOWN(-1);
		int action;
		Action(int i){
			action=i;
		}
	}
}
