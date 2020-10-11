package me.engine.Utils;

import java.util.HashMap;

import org.lwjgl.opengl.GL45;

public class GlStateManager {
	
	static HashMap<Integer, Boolean> states=new HashMap<>();
	static int txt=-1,shader=0,vao=-1;
	
	public static void Enable(int flag) {
		if(states.containsKey(flag)) {
			if(!states.get(flag))
				GL45.glEnable(flag);
		}else {
			states.put(flag, true);
			GL45.glEnable(flag);
		}
	}
	
	public static void Disable(int flag) {
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
	
	public static void bindShader(Shader s) {
		if(shader!=s.program)
			GL45.glUseProgram(s.program);
	}
	
	public static void unbindShader() {
		if(shader!=0)
			GL45.glUseProgram(0);
	}
	
	public static void bindVArray(int vao_) {
		if(vao!=vao_)
			GL45.glBindVertexArray(vao_);
		vao=vao_;
	}
	public static void unbindVArray() {
		if(vao!=0)
			GL45.glBindVertexArray(0);
		vao=0;
	}
	
	
	
	
}
