package me.engine.Utils;

import java.nio.ByteBuffer;

import org.joml.Matrix4f;
import org.joml.Vector4i;
import org.lwjgl.opengl.GL45;

public class FrameBuffer {
	private int buffer;
	private int txt,txt2;
	private int width,height;
	private boolean bound;
	Matrix4f scale,projection;
	
	public FrameBuffer(int width,int height) {
		scale=new Matrix4f();
		projection=new Matrix4f();
		bound=true;
		this.width=width;
		this.height=height;
		buffer = createFrameBuffer();
		txt = createTextureAttachment(GL45.GL_COLOR_ATTACHMENT0,GL45.GL_RGB,GL45.GL_RGB,GL45.GL_UNSIGNED_BYTE);
		txt2 = createTextureAttachment(GL45.GL_DEPTH_STENCIL_ATTACHMENT,GL45.GL_DEPTH24_STENCIL8,GL45.GL_DEPTH_STENCIL,GL45.GL_UNSIGNED_INT_24_8);
		vtemp=GlStateManager.getcViewport();
		setAspectRatio(width,height);
		unbind();
	}
	Vector4i viewport,vtemp;
	
	/**
	 * sets the Aspect Ratio after the Window has been resized
	 * @param width The new width
	 * @param height The new height
	 */
	public void setAspectRatio(int wwidth,int wheight) {
		// This is your target virtual resolution for the game, the size you built your game to
				int virtualwidth=1920;
				int virtualheight=1080;
				float targetAspectRatio = virtualwidth/(float)virtualheight;
				 
				// figure out the largest area that fits in this resolution at the desired aspect ratio
				wheight = (int)(wwidth / targetAspectRatio + 0.5f);
				 
				if (wheight > height )
				{
				   //It doesn't fit our height, we must switch to pillarbox then
				    wheight = height;
				    wwidth = (int)(wheight * targetAspectRatio + 0.5f);
				}
				 
				// set up the new viewport centered in the backbuffer
				int offsetx = (width  / 2) - (wwidth / 2);
				int offsety = (height / 2) - (wheight / 2);
				viewport=new Vector4i(offsetx,offsety,wwidth,wheight);
				GL45.glViewport(offsetx,offsety,wwidth,wheight);
				// Now we use Ortho
				projection.ortho(0, width, height, 0, -1, 1);
				
				//Now to calculate the scale considering the screen size and virtual size
				float scalex = ((float)(width) / (float)virtualwidth);
				float scaley = ((float)(height) / (float)virtualheight);
				scale.scale(scalex, scaley, 1.0f);
	}
	
	public void clear() {
		boolean b=bound;
		if(!bound)
			bind();
		GL45.glClear(GL45.GL_COLOR_BUFFER_BIT|GL45.GL_STENCIL_BUFFER_BIT|GL45.GL_DEPTH_BUFFER_BIT);
		if(!b)
			unbind();
	}

	public void destroy() {
		GL45.glDeleteFramebuffers(buffer);
		GL45.glDeleteTextures(txt);
		txt=-1;
		buffer=-1;
	}

	private int createFrameBuffer() {
		int frameBuffer = GL45.glGenFramebuffers();
		GL45.glBindFramebuffer(GL45.GL_FRAMEBUFFER, frameBuffer);
		GL45.glDrawBuffer(GL45.GL_COLOR_ATTACHMENT0);
		return frameBuffer;
	}

	private int createTextureAttachment(int type,int internal_format,int format,int datatype) {
		int texture = GL45.glGenTextures();
		GL45.glBindTexture(GL45.GL_TEXTURE_2D, texture);
		GL45.glTexImage2D(GL45.GL_TEXTURE_2D, 0, internal_format, (int) width, (int) height, 0,
				format, datatype, (ByteBuffer) null);
		GL45.glTexParameteri(GL45.GL_TEXTURE_2D, GL45.GL_TEXTURE_MAG_FILTER, GL45.GL_LINEAR);
		GL45.glTexParameteri(GL45.GL_TEXTURE_2D, GL45.GL_TEXTURE_MIN_FILTER, GL45.GL_LINEAR);
		GL45.glFramebufferTexture(GL45.GL_FRAMEBUFFER, type, texture, 0);
		return texture;
	}

	public void bind() {
		if(!bound) {
			GL45.glBindFramebuffer(GL45.GL_FRAMEBUFFER, buffer);
			vtemp=GlStateManager.getcViewport();
			GlStateManager.Viewport(viewport);
			bound=true;
		}
	}
	
	public float getDepth(int x,int y) {
		float[] p=new float[1];
		GL45.glReadnPixels(x, y, 1, 1, GL45.GL_DEPTH_COMPONENT, GL45.GL_FLOAT, p);
		return p[0];
	}

	public void unbind() {
		if(bound) {
			GlStateManager.Viewport(vtemp);
			GL45.glBindFramebuffer(GL45.GL_FRAMEBUFFER, 0);
			bound=false;
		}
	}
	
	public boolean isbound() {
		return bound;
	}
	
	public int getTxtID() {
		return txt;
	}
	
	public Matrix4f getScale() {
		return scale;
	}

	public Matrix4f getProjection() {
		return projection;
	}
}
