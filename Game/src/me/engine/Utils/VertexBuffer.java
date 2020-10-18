package me.engine.Utils;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL45;

import me.engine.Main;

public class VertexBuffer {
	private int vao,vbo[][];
	private FloatBuffer[] vb;
	private boolean staticdata,bound;
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
	
	public FloatBuffer toBuffer(float[] data) {
		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(data.length);
		verticesBuffer.put(data);
		verticesBuffer.flip();
		return verticesBuffer;
	}
	
	public static FloatBuffer updateBuffer(FloatBuffer buffer, float[] data)
	{
	    buffer.clear();
	    for(int i = 0; i < data.length; i ++)
	        buffer.put(data[i]);
	    buffer.flip();
	    return buffer;
	}
	
	public int getvbo(int id) {
		return vbo[id][0];
	}
	
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
	
	public int getbuffersize(int id) {
		return vbo[id][1];
	}
	
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
