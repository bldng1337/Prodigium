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
	float[] txt;
	int vindex=0,vbindex,tindex;
	private VertexBuffer[] v;
	static Matrix4f scale,projection;
	
	public Renderer() {
		vertecies=new float[MAXDRAW];
		txt=new float[MAXDRAW];
		v=new VertexBuffer[MAXCALLS];
		vbindex=-1;
	}
	public void renderQuad(float x, float y, float width, float height, long texid,int frame) {
		float tx=Texture.getx(texid)+Texture.getdx(texid)*frame;
		float ty=Texture.gety(texid);
		float tx2=Texture.getx(texid)+Texture.getdx(texid)+Texture.getdx(texid)*frame;
		float ty2=Texture.gety(texid)+Texture.getdy(texid);
		int atlas=Texture.getatlas(texid);
		tx/=Main.tex.msize;
		ty/=Main.tex.msize;
		tx2/=Main.tex.msize;
		ty2/=Main.tex.msize;
//		tx=0;
//		ty=0;
//		tx2=1;
//		ty2=1;
		vertecies[vindex++]=x;
		vertecies[vindex++]=y+height;
		vertecies[vindex++]=1;
		txt[tindex++]=tx;
		txt[tindex++]=ty2;
		txt[tindex++]=atlas;
		
		vertecies[vindex++]=x;
		vertecies[vindex++]=y;
		vertecies[vindex++]=1;
		txt[tindex++]=tx;
		txt[tindex++]=ty;
		txt[tindex++]=atlas;
		
		vertecies[vindex++]=x+width;
		vertecies[vindex++]=y;
		vertecies[vindex++]=1;
		txt[tindex++]=tx2;
		txt[tindex++]=ty;
		txt[tindex++]=atlas;
		
		vertecies[vindex++]=x;
		vertecies[vindex++]=y+height;
		vertecies[vindex++]=1;
		txt[tindex++]=tx;
		txt[tindex++]=ty2;
		txt[tindex++]=atlas;
		
		vertecies[vindex++]=x+width;
		vertecies[vindex++]=y+height;
		vertecies[vindex++]=1;
		txt[tindex++]=tx2;
		txt[tindex++]=ty2;
		txt[tindex++]=atlas;
		
		vertecies[vindex++]=x+width;
		vertecies[vindex++]=y;
		vertecies[vindex++]=1;
		txt[tindex++]=tx2;
		txt[tindex++]=ty;
		txt[tindex++]=atlas;
		
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
			v[vbindex].createBuffer(Arrays.copyOfRange(txt, 0,tindex), 1, 3);
		}else {
			v[vbindex].updateBuffer(Arrays.copyOfRange(vertecies, 0,vindex), 0, 3);
			v[vbindex].updateBuffer(Arrays.copyOfRange(txt, 0,tindex), 1, 3);
		}
		vindex=0;
		tindex=0;
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
		s.useUniform("u_Textures", 0, 1, 2, 3, 4, 5, 6);
		Main.tex.bind();
		for(int i=0;i<=vbindex;i++) {
			VertexBuffer vb=v[i];
			vb.bind(0);
			vb.bind(1);
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
