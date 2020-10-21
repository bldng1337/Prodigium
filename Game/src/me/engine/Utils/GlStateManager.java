package me.engine.Utils;

import java.util.HashMap;

import org.lwjgl.opengl.GL45;

/**
 * @author Christian
 * Manages OpenGL Calls
 */
public class GlStateManager {
	
	private GlStateManager() {}
	
	static HashMap<Integer, Boolean> states=new HashMap<>();
	static int txt=-1,shader=0,vao=-1;
	
	/**
	 * Enables an flag in the OpenGL Context
	 * @param flag The flag to enable
	 */
	public static void enable(int flag) {
		if(states.containsKey(flag)) {
			if(!states.get(flag))
				GL45.glEnable(flag);
		}else {
			states.put(flag, true);
			GL45.glEnable(flag);
		}
	}
	
	/**
	 * Disables an flag in the OpenGL Context
	 * @param flag The flag to disable
	 */
	public static void disable(int flag) {
		if(states.containsKey(flag)) {
			if(states.get(flag)) 
				GL45.glDisable(flag);
		}else {
			states.put(flag, false);
			GL45.glDisable(flag);
		}
	}
	
	/**
	 * Binds an Texture
	 * @param id The Texture to be bound
	 */
	public static void bindTexture2D(int id) {
		if(txt!=id)
			GL45.glBindTexture(GL45.GL_TEXTURE_2D, id);
	}
	
	
	/**
	 * Unbinds the currently bound Texture
	 */
	public static void unbindTexture2D() {
		if(txt!=0)
			GL45.glBindTexture(GL45.GL_TEXTURE_2D, 0);
	}
	
	/**
	 * Binds an Shader
	 * @param s The ShaderID of the Shader
	 */
	public static void bindShader(int s) {
		if(shader!=s)
			GL45.glUseProgram(s);
	}
	
	/**
	 * Unbinds the currently bound Shader
	 */
	public static void unbindShader() {
		if(shader!=0)
			GL45.glUseProgram(0);
	}
	
	/**
	 * Binds an Vertex Array
	 * @param vvao The Vertex Array that should be bound
	 */
	public static void bindVArray(int vvao) {
		if(vao!=vvao)
			GL45.glBindVertexArray(vvao);
		vao=vvao;
	}
	
	/**
	 * Unbinds the currently bound VertexArray
	 */
	public static void unbindVArray() {
		if(vao!=0)
			GL45.glBindVertexArray(0);
		vao=0;
	}
	
	
	
	
}
