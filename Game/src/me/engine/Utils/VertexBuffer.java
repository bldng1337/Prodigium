package me.engine.Utils;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL45;

import me.engine.Main;

/**
 * @author Christian
 * Abstracts Buffers in a Class and provides Utility methods
 */
public class VertexBuffer {
	/**
	 * Vao and vbo id list
	 */
	private int vao,vbo[][];
	/**
	 * List of FLoatBuffers
	 */
	private FloatBuffer[] vb;
	/**
	 * If this VertexArrayObject should store static Data
	 */
	private boolean staticdata,bound;
	/**
	 * Which buffers got enabled
	 */
	private int[] enabledbuffers;
	
	public VertexBuffer(boolean staticdata) {
		vao = GL45.glGenVertexArrays();
		vbo = new int[15][2];
		enabledbuffers=new int[15];
		vb = new FloatBuffer[15];
		for(int i=0;i<15;i++) {
			enabledbuffers[i]=-1;
			vbo[i][0] = -1;
			
		}
		this.staticdata=staticdata;
	}
	//TODO:Dont think the size explanation is very good
	/**
	 * Creates an new Buffer in the VertexBuffer
	 * @param data The Data to put into the Buffer
	 * @param id The ID of the Buffer
	 * @param size The size of a single Data
	 */
	public void createBuffer(float[] data,int id,int size) {
		if(vbo[id][0]!=-1) {
			Main.log.warning("Tried to create already existing buffer");
			updateBuffer(data,id,size);
			return;
		}
		if(data.length%size!=0) {
			Main.log.warning(()->"Array incorrect size "+(data.length));
			return;
		}
		GlStateManager.bindVArray(vao);
		vbo[id][0] = GL45.glGenBuffers();
		vbo[id][1] = data.length/size;
		vb[id]=toBuffer(data);
		GL45.glBindBuffer(GL45.GL_ARRAY_BUFFER, vbo[id][0]);
		GL45.glBufferData(GL45.GL_ARRAY_BUFFER, vb[id],  GL45.GL_DYNAMIC_DRAW);
		GL45.glVertexAttribPointer(id, size, GL45.GL_FLOAT, false, 0, 0);
		GL45.glBindBuffer(GL45.GL_ARRAY_BUFFER, 0);
		GlStateManager.unbindVArray();
	}
	
	/**
	 * Updates an dynamic Buffer with Data
	 * @param data The new Data
	 * @param id The BufferID to be Updated
	 * @param size The size of a single Data
	 */
	public void updateBuffer(float[] data,int id,int size) {
		if(staticdata) {
			Main.log.severe("Tried to update an static Buffer");
			return;
		}
		if(data.length%size!=0) {
			Main.log.warning(()->"Array incorrect size "+(data.length));
			return;
		}
		vb[id]=updateBuffer(vb[id], data);
		vbo[id][1] = data.length/size;
		GlStateManager.bindVArray(vao);
		GL45.glBindBuffer(GL45.GL_ARRAY_BUFFER, vbo[id][0]);
		GL45.glBufferSubData(GL45.GL_ARRAY_BUFFER,0, vb[id]);
		GL45.glBindBuffer(GL45.GL_ARRAY_BUFFER, vbo[id][0]);
		GlStateManager.unbindVArray();
	}
	
	/**
	 * Converts an array to an Buffer
	 * @param data The array to be converted
	 * @return The new Buffer
	 */
	public FloatBuffer toBuffer(float[] data) {
		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(data.length);
		verticesBuffer.put(data);
		verticesBuffer.flip();
		return verticesBuffer;
	}
	
	/**
	 * Updates an already existing Buffer with an new array
	 * @param buffer The Buffer to be updated
	 * @param data The new array to Update the Buffer with
	 * @return The FloatBuffer
	 */
	public static FloatBuffer updateBuffer(FloatBuffer buffer, float[] data)
	{
	    buffer.clear();
	    for(int i = 0; i < data.length; i ++)
	        buffer.put(data[i]);
	    buffer.flip();
	    return buffer;
	}
	
	/**
	 * Gets the VBO ID
	 * @param id The ID in the Buffer
	 * @return The ID
	 */
	public int getvbo(int id) {
		return vbo[id][0];
	}
	
	/**
	 * Binds the specified Buffer
	 * @param id The BufferID
	 */
	public void bind(int id) {
		bound=true;
		for(int i=0;i<15;i++) {
			if(enabledbuffers[i]==-1) {
				enabledbuffers[i]=id;
				break;
			}
			if(enabledbuffers[i]==id)
				return;
		}
		GlStateManager.bindVArray(vao);
		GL45.glEnableVertexAttribArray(id);
	}
	
	/**
	 * Unbinds the Buffer
	 */
	public void unbind() {
		if(!bound)
			return;
		bound=false;
		for(int i=0;i<15;i++) {
			if(enabledbuffers[i]!=-1)
				GL45.glDisableVertexAttribArray(enabledbuffers[i]);
			enabledbuffers[i]=-1;
		}
		GlStateManager.unbindVArray();
	}
	
	/**
	 * Gets the Size of the specified Buffer
	 * @param id The BufferID
	 * @return The Size
	 */
	public int getbuffersize(int id) {
		return vbo[id][1];
	}
	
	/**
	 * Destroys this BufferObject
	 */
	public void destroy() {
		GL45.glDeleteVertexArrays(vao);
		vao=-1;
		for(int i=0;i<15;i++) {
			GL45.glDeleteBuffers(vbo[i][0]);
			vbo[i][0]=-1;
			vbo[i][1]=-1;
		}
	}

}
