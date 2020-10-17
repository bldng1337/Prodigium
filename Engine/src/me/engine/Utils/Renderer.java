package me.engine.Utils;

import java.io.File;
import java.util.Arrays;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL45;

import me.engine.Main;

public class Renderer {

	public static final int MAXDRAW=5000;
	public static final int MAXCALLS=10;
	float[] vertecies;
	int vindex=0,vbindex;
	private VertexBuffer[] v;
	static Matrix4f scale,projection;
	
	public Renderer() {
		vertecies=new float[MAXDRAW];
		v=new VertexBuffer[MAXCALLS];
		vbindex=-1;
	}
	
	public void renderQuad(float x, float y, float width, float height, long texid) {
		vertecies[vindex++]=x;
		vertecies[vindex++]=y+height;
		vertecies[vindex++]=1;
		
		vertecies[vindex++]=x;
		vertecies[vindex++]=y;
		vertecies[vindex++]=1;
		
		vertecies[vindex++]=x+width;
		vertecies[vindex++]=y;
		vertecies[vindex++]=1;
		
		vertecies[vindex++]=x;
		vertecies[vindex++]=y+height;
		vertecies[vindex++]=1;
		
		vertecies[vindex++]=x+width;
		vertecies[vindex++]=y+height;
		vertecies[vindex++]=1;
		
		vertecies[vindex++]=x+width;
		vertecies[vindex++]=y;
		vertecies[vindex++]=1;
		
		if(vindex>MAXDRAW-18)
			flush();
	}
	
	public void flush() {
		if(vindex<18)
			return;
		vbindex++;
		if(vbindex>=MAXCALLS) {
			Main.log.severe("Rendered to much triangles ");
			vbindex=-1;
			return;
		}
		if(v[vbindex]==null) {
			v[vbindex]=new VertexBuffer(false);
			v[vbindex].createBuffer(Arrays.copyOfRange(vertecies, 0,vindex), 0, 3);
		}else {
			v[vbindex].updateBuffer(Arrays.copyOfRange(vertecies, 0,vindex), 0, 3);
		}
		vindex=0;
	}
	
	Shader s=new Shader(new File(Main.dir.getAbsolutePath()+"\\Assets\\Shader\\std.frag"), new File(Main.dir.getAbsolutePath()+"\\Assets\\Shader\\std.vert"));
	public void render() {
		flush();
		vindex=0;
		//TODO: Maybe format
		Main.log.finest(()->vbindex+" DrawCalls");
		s.bind();
		s.useUniform("projection", projection);
		s.useUniform("scale", scale);
		for(int i=0;i<=vbindex;i++) {
			VertexBuffer vb=v[i];
			vb.bind(0);
			GL45.glDrawArrays(GL45.GL_TRIANGLES, 0, vb.getbuffersize(0));
			vb.unbind();
		}
		s.unbind();
	}
	
	public void clear() {
		vbindex=-1;
	}
	
	
	public void destroy() {
		for(VertexBuffer vb:v)
			vb.destroy();
		vertecies=null;
	}
	
	public static void clearTransform() {
		//TODO: Clear them more efficiently
		scale=new Matrix4f();
		projection=new Matrix4f();
	}
	
	public void transform(float x,float y,float z) {
		scale.translate(x, y, z);
	}
	
	public void ortho(float left, float right, float bottom, float top, float zNear, float zFar) {
		projection.ortho(left, right, bottom, top, zNear, zFar);
	}
	
	public void scale(float x,float y,float z) {
		scale.scale(x, y, z);
	}
	
	
}
