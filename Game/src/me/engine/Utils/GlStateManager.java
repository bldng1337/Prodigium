package me.engine.Utils;

import java.util.HashMap;

import org.lwjgl.opengl.GL45;

public class GlStateManager {
	
	private GlStateManager() {}
	
	static HashMap<Integer, Boolean> states=new HashMap<>();
	static int txt=-1,shader=0,vao=-1;
	
	public static void enable(int flag) {
		if(states.containsKey(flag)) {
			if(!states.get(flag))
				GL45.glEnable(flag);
		}else {
			states.put(flag, true);
			GL45.glEnable(flag);
		}
	}
	
	public static void disable(int flag) {
		if(states.containsKey(flag)) {
			if(states.get(flag)) 
				GL45.glDisable(flag);
		}else {
			states.put(flag, false);
			GL45.glDisable(flag);
		}
	}
	
	public static void bindTexture2D(int id) {
		if(txt!=id)
			GL45.glBindTexture(GL45.GL_TEXTURE_2D, id);
	}
	
	public static void unbindTexture2D() {
		if(txt!=0)
			GL45.glBindTexture(GL45.GL_TEXTURE_2D, 0);
	}
	
	public static void bindShader(int s) {
		if(shader!=s)
			GL45.glUseProgram(s);
	}
	
	public static void unbindShader() {
		if(shader!=0)
			GL45.glUseProgram(0);
	}
	
	public static void bindVArray(int vvao) {
		if(vao!=vvao)
			GL45.glBindVertexArray(vvao);
		vao=vvao;
	}
	public static void unbindVArray() {
		if(vao!=0)
			GL45.glBindVertexArray(0);
		vao=0;
	}
	
	
	
	
}
