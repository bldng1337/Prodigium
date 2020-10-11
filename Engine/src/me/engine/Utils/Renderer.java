package me.engine.Utils;

import java.io.File;

import org.lwjgl.opengl.GL45;

import me.engine.Main;

public class Renderer {

	public static final int MAXDRAW=500;
	public static final int MAXCALLS=10;
	float[] vertecies;
	int vindex=0,vbindex;
	private VertexBuffer[] v;
	public Renderer() {
		vertecies=new float[MAXDRAW];
		v=new VertexBuffer[MAXCALLS];
		vbindex=-1;
	}
	
	
	public void renderQuad(float x, float y, float width, float height, long texid) {
		vertecies[vindex++]=x;
		vertecies[vindex++]=y+height;
		vertecies[vindex++]=0;
		
		vertecies[vindex++]=x;
		vertecies[vindex++]=y;
		vertecies[vindex++]=0;
		
		vertecies[vindex++]=x+width;
		vertecies[vindex++]=y;
		vertecies[vindex++]=0;
		
		vertecies[vindex++]=x+width;
		vertecies[vindex++]=y;
		vertecies[vindex++]=0;
		
		vertecies[vindex++]=x+width;
		vertecies[vindex++]=y+height;
		vertecies[vindex++]=0;
		
		vertecies[vindex++]=x;
		vertecies[vindex++]=y+height;
		vertecies[vindex++]=0;
		
		if(vindex>MAXDRAW-18)
			flush();
	}
	
	public void flush() {
		if(vindex<18)
			return;
		vbindex++;
		if(v[vbindex]==null) {
			v[vbindex]=new VertexBuffer(false);
			v[vbindex].createBuffer(vertecies, 0, 3);
		}else {
			v[vbindex].updateBuffer(vertecies, 0, 3);
		}
		vindex=0;
	}
	Shader s=new Shader(new File(Main.dir.getAbsolutePath()+"\\Assets\\Shader\\std.frag"), new File(Main.dir.getAbsolutePath()+"\\Assets\\Shader\\std.vert"));
	public void render() {
		flush();
		vindex=0;
		vbindex=-1;
		GlStateManager.bindShader(s);
		for(int i=0;i<vbindex;i++) {
			VertexBuffer vb=v[i];
			vb.bind(0);
			GL45.glDrawArrays(GL45.GL_TRIANGLES, 0, vb.getbuffersize(0));
			vb.unbind();
		}
		GlStateManager.unbindShader();
		vbindex=0;
	}
	
	
	public void destroy() {
		for(VertexBuffer vb:v)
			vb.destroy();
		vertecies=null;
	}
	
	
}
