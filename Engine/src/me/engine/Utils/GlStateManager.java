package me.engine.Utils;

import java.util.HashMap;

import org.joml.Vector4i;
import org.lwjgl.opengl.GL45;

/**
 * @author Christian
 * Manages OpenGL Calls
 */
public class GlStateManager {
	
	private GlStateManager() {}
	
	/**
	 * Hasmap that keeps Track of the States of the GL Flags
	 */
	static HashMap<Integer, Boolean> states=new HashMap<>();
	
	/**
	 * Keeps Track which texture, shader or Vertexarray is curently bound
	 */
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
	
	public static void makeMask() {
		GL45.glClear(GL45.GL_DEPTH_BUFFER_BIT);
		GL45.glColorMask(false, false, false, false);
		GL45.glDepthFunc(GL45.GL_LESS);
		GL45.glEnable(GL45.GL_DEPTH_TEST);
		GL45.glDepthMask(true);
	}
	public static void disableMask() {
		GL45.glClear(GL45.GL_DEPTH_BUFFER_BIT|GL45.GL_STENCIL_BUFFER_BIT);
		GL45.glDisable(GL45.GL_DEPTH_TEST);
		GL45.glDepthFunc(GL45.GL_LEQUAL);
		GL45.glDepthMask(false);
	}
	public static void useMask(Func func) {
		GL45.glColorMask(true, true, true, true);
		GL45.glDepthMask(true);
		GL45.glDepthFunc(func.getFuncID());
	}
	static int x,y,w,h;
	
	public static void Viewport(int _x,int _y,int _w,int _h) {
		GL45.glViewport(x=_x,y=_y,w=_w,h=_h);
	}
	
	public static void Viewport(Vector4i i) {
		Viewport(i.x, i.y, i.z, i.w);
	}
	
	public static Vector4i getcViewport() {
		return new Vector4i(x,y,w,h);
	}
	
	
	/**
	 * @author Christian
	 * @see <a href="https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/glDepthFunc.xhtml">Khronos Docs</a>
	 */
	enum Func{
		/**
		 * Never passes.
		 */
		NEVER(GL45.GL_NEVER),
		/**
		 * Passes if the incoming depth value is less than the stored depth value.
		 */
		LESS(GL45.GL_LESS),
		/**
		 * Passes if the incoming depth value is equal to the stored depth value.
		 */
		EQUAL(GL45.GL_EQUAL),
		/**
		 * Passes if the incoming depth value is less than or equal to the stored depth value.
		 */
		LEQUAL(GL45.GL_LEQUAL),
		/**
		 * Passes if the incoming depth value is greater than the stored depth value.
		 */
		GREATER(GL45.GL_GREATER),
		/**
		 * Passes if the incoming depth value is not equal to the stored depth value.
		 */
		NOTEQUAL(GL45.GL_NOTEQUAL),
		/**
		 * Passes if the incoming depth value is greater than or equal to the stored depth value.
		 */
		GEQUAL(GL45.GL_GEQUAL),
		/**
		 * Always passes.
		 */
		ALWAYS(GL45.GL_ALWAYS);
		final int fid;
		Func(int fid) {
			this.fid=fid;
		}
		public int getFuncID() {
			return fid;
		}
	}
	
	
}
