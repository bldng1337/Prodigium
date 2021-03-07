package me.engine.Utils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL45;

import me.engine.Engine;
import me.engine.Utils.GlStateManager.Func;
import me.engine.Utils.Event.EventManager;
import me.engine.World.Tiles.Tile;

public class LightRenderer extends Renderer{
	ArrayList<Light> lights;
	Shader lightshader;
	FrameBuffer fb;
	int LightID;
	
	public LightRenderer() {
		c=Engine.getEngine().getRender().c;
		s=Engine.getEngine().getRender().s;
		EventManager.register(this);
		lights=new ArrayList<>();
		try {
			LightID=registerTexture(ImageIO.read(FileUtils.getFilefromID("Textures.noatlas.light:png")));
		} catch (IOException e) {
			LightID=-1;
			e.printStackTrace();
		}
		fb=new FrameBuffer(1920, 1080);
		lightshader=new Shader(FileUtils.getFilefromID("Shader.light:frag"), FileUtils.getFilefromID("Shader.std:vert"));
		lights.add(new Light(new Vector2f(400,400), 600, 0xFF13FFFF));
	}
	
	public Light createLight(Vector2f v,float size,int argb) {
		lights.add(new Light(v, size, argb));
		return lights.get(lights.size()-1);
	}
	public void removeLight(Light l)
	{
		lights.remove(l);
	}
	
	public class Light {
		float size;
		int argb;
		Vector2f pos;
		Light(Vector2f v,float size,int argb) {
			pos=new Vector2f(v);
			this.size=size;
			this.argb=argb;
		}
		public float getSize() {
			return size;
		}
		public void setSize(float size) {
			this.size = size;
		}
		public int getArgb() {
			return argb;
		}
		public void setArgb(int argb) {
			this.argb = argb;
		}
		public Vector2f getPos() {
			return pos;
		}
		public void setPos(Vector2f pos) {
			this.pos = pos;
		}
	}
	
	public void updateLightmap() {
		fb.clear();
		fb.bind();
		for(Light l:lights) {
			if(!Engine.getEngine().getRender().c.shouldberendered(l.pos.x-l.size/2, l.pos.y-l.size/2, l.size, l.size))
				continue;
			GlStateManager.makeMask();
			MathUtils.drawCircle((v)->{
				if(v.x<0||v.y<0)
					return;
				Tile t=Engine.getEngine().getCurrlevel().getTile((int)v.x, (int)v.y);
				if(t.isCollideable()) {
					float[][] vl=t.getLightEdges(v.floor());
					for(int i=0;i<4;i++)
						for(int a=0;a<2;a++)
							vl[i][a]*=Tile.SIZE;
					Arrays.sort(vl, (a,b)->{
						return (int) (l.pos.distance(a[0], a[1])-l.pos.distance(b[0], b[1]));
					});
					for(int i=1;i<3;i++) {
						addVertex(vl[0][0], vl[0][1]);
						addVertex(vl[i][0], vl[i][1]);
						vl[i][0]=vl[i][0]-l.pos.x;
						vl[i][1]=vl[i][1]-l.pos.y;
						float length=(float) Math.sqrt(vl[i][0]*vl[i][0]+vl[i][1]*vl[i][1]);
						vl[i][0]/=length;
						vl[i][1]/=length;
						addVertex(l.pos.x+vl[i][0]*(l.size+20), l.pos.y+vl[i][1]*(l.size+20));
					}
					addVertex(vl[0][0], vl[0][1]);
					addVertex(l.pos.x+vl[1][0]*(l.size+20), l.pos.y+vl[1][1]*(l.size+20));
					addVertex(l.pos.x+vl[2][0]*(l.size+20), l.pos.y+vl[2][1]*(l.size+20));
				}
			}, (int)(l.pos.x/Tile.SIZE), (int)(l.pos.y/Tile.SIZE), (int)l.size/Tile.SIZE+2);
			this.flush();
			GlStateManager.useMask(Func.LESS);
			this.drawTexturedRect(l.pos.x-l.size/2, l.pos.y-l.size/2, l.size, l.size, l.argb,LightID);
			this.flush();
			GlStateManager.disableMask();
		}
		fb.unbind();
	}
	
	public void drawLightmap(int col) {
		this.setTexCoords(0, 1, 1, 0);
		this.drawTexturedRect(0, 0, 1920, 1080, col,fb.getTxtID());
		this.resetTexCoords();
	}
	
	@Override
	public void flush() {
		if(fb.isbound()) {
			if(vindex<18)
				return;
			if(v==null) {
				v=new VertexBuffer(false);
				v.createBuffer(vertices, 0, 3);
				v.createBuffer(txt, 1, 3);
				v.createBuffer(col, 2, 4);
				v.createBuffer(ligh, 3, 1);
			}else {
				v.updateBuffer(vertices, 0, 3);
				v.updateBuffer(txt, 1, 3);
				v.updateBuffer(col, 2, 4);
				v.updateBuffer(ligh, 3, 1);
			}
			s.bind();
			s.useUniform("projection", fb.projection);
			s.useUniform("scale", fb.scale);
			s.useUniform("u_Textures", 0, 1, 2, 3, 4, 5, 6, 7);
			s.useUniform("u_Transform", c.translate);
			s.useUniform("u_Resolution", Engine.getEngine().getWindowwidth(),Engine.getEngine().getWindowheight());
			Engine.getEngine().getTex().bind();
			GL45.glBindTextureUnit(7, lightmap);
			v.bind(0);
			v.bind(1);
			v.bind(2);
			v.bind(3);
			GL45.glDrawArrays(GL45.GL_TRIANGLES, 0, vindex);
			v.unbind();
			s.unbind();
			cindex=0;
			vindex=0;
			tindex=0;
			lindex=0;
			vertices=new float[MAXDRAW];
			txt=new float[MAXDRAW];
			col=new float[MAXDRAW/3*4];
		}else {
			super.flush();
		}
	}
	
	
	
	private void addVertex(float x,float y) {
		vertices[vindex++]=x;
		vertices[vindex++]=y;
		vertices[vindex++]=1;
		txt[tindex++]=0;
		txt[tindex++]=0;
		txt[tindex++]=8;
		col[cindex++]=1f;//b
		col[cindex++]=1f;//g
		col[cindex++]=1f;//r
		col[cindex++]=1f;//a
		if(vindex==Renderer.MAXDRAW)
			this.flush();
	}
	
	private void drawTexturedRect(float x, float y, float width, float height, int rgba,int txtid) {
		int blue = rgba & 0xff;
		int green = (rgba & 0xff00) >> 8;
		int red = (rgba & 0xff0000) >> 16;
		int alpha = (rgba & 0xff000000) >>> 24;
		
		vertices[vindex++]=x;
		vertices[vindex++]=y+height;
		vertices[vindex++]=1;
		txt[tindex++]=texcoords.x;
		txt[tindex++]=texcoords.w;
		txt[tindex++]=8;
		col[cindex++]=blue/255f;
		col[cindex++]=green/255f;
		col[cindex++]=red/255f;
		col[cindex++]=alpha/255f;
		
		vertices[vindex++]=x;
		vertices[vindex++]=y;
		vertices[vindex++]=1;
		txt[tindex++]=texcoords.x;
		txt[tindex++]=texcoords.y;
		txt[tindex++]=8;
		col[cindex++]=blue/255f;
		col[cindex++]=green/255f;
		col[cindex++]=red/255f;
		col[cindex++]=alpha/255f;
		
		vertices[vindex++]=x+width;
		vertices[vindex++]=y;
		vertices[vindex++]=1;
		txt[tindex++]=texcoords.z;
		txt[tindex++]=texcoords.y;
		txt[tindex++]=8;
		col[cindex++]=blue/255f;
		col[cindex++]=green/255f;
		col[cindex++]=red/255f;
		col[cindex++]=alpha/255f;
		
		vertices[vindex++]=x;
		vertices[vindex++]=y+height;
		vertices[vindex++]=1;
		txt[tindex++]=texcoords.x;
		txt[tindex++]=texcoords.w;
		txt[tindex++]=8;
		col[cindex++]=blue/255f;
		col[cindex++]=green/255f;
		col[cindex++]=red/255f;
		col[cindex++]=alpha/255f;
		
		vertices[vindex++]=x+width;
		vertices[vindex++]=y+height;
		vertices[vindex++]=1;
		txt[tindex++]=texcoords.z;
		txt[tindex++]=texcoords.w;
		txt[tindex++]=8;
		col[cindex++]=blue/255f;
		col[cindex++]=green/255f;
		col[cindex++]=red/255f;
		col[cindex++]=alpha/255f;
		
		vertices[vindex++]=x+width;
		vertices[vindex++]=y;
		vertices[vindex++]=1;
		txt[tindex++]=texcoords.z;
		txt[tindex++]=texcoords.y;
		txt[tindex++]=8;
		col[cindex++]=blue/255f;
		col[cindex++]=green/255f;
		col[cindex++]=red/255f;
		col[cindex++]=alpha/255f;
		
		if(v==null) {
			v=new VertexBuffer(false);
			v.createBuffer(vertices, 0, 3);
			v.createBuffer(txt, 1, 3);
			v.createBuffer(col, 2, 4);
		}else {
			v.updateBuffer(vertices, 0, 3);
			v.updateBuffer(txt, 1, 3);
			v.updateBuffer(col, 2, 4);
		}
		lightshader.bind();
		if(fb.isbound()) {
			lightshader.useUniform("projection", fb.projection);
			lightshader.useUniform("scale", fb.scale);
		}else {
			lightshader.useUniform("projection", projection);
			lightshader.useUniform("scale", scale);
		}
		lightshader.useUniform("u_Texture", 0);
		if(fb.isbound()) {
			lightshader.useUniform("u_Transform", c.translate);
		}else {
			lightshader.useUniform("u_Transform", new Vector2f());
		}
		GL45.glBindTextureUnit(0, txtid);
		v.bind(0);
		v.bind(1);
		v.bind(2);
		GL45.glDrawArrays(GL45.GL_TRIANGLES, 0, vindex);
		v.unbind();
		lightshader.unbind();
		cindex=0;
		vindex=0;
		tindex=0;
		vertices=new float[MAXDRAW];
		txt=new float[MAXDRAW];
		col=new float[MAXDRAW/3*4];
	}
	
	public int registerTexture(BufferedImage bfi) {
		int[] pixels = new int[bfi.getWidth() * bfi.getHeight()];
		bfi.getRGB(0, 0, bfi.getWidth(), bfi.getHeight(), pixels, 0, bfi.getWidth());
		
		IntBuffer ib=BufferUtils.createIntBuffer(bfi.getWidth() * bfi.getHeight());
		for (int i = 0; i < bfi.getWidth() * bfi.getHeight(); i++) {
			int a = (pixels[i] & 0xff000000) >> 24;
			int r = (pixels[i] & 0xff0000) >> 16;
			int g = (pixels[i] & 0xff00) >> 8;
			int b = (pixels[i] & 0xff);
			ib.put(a << 24 | b << 16 | g << 8 | r);
		}
		ib.flip();
//		try {
//			ImageIO.write(catlas, "png", new File(Engine.dir.getParentFile()+"\\y"+atlas+".png"));
//			System.out.println(Engine.dir+"y.png");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		int tid=GL45.glGenTextures();
		GL45.glBindTexture(GL45.GL_TEXTURE_2D, tid);
		GL45.glTexParameteri(GL45.GL_TEXTURE_2D, GL45.GL_TEXTURE_MIN_FILTER, GL45.GL_NEAREST);
		GL45.glTexParameteri(GL45.GL_TEXTURE_2D, GL45.GL_TEXTURE_MAG_FILTER, GL45.GL_NEAREST);
		GL45.glTexImage2D(GL45.GL_TEXTURE_2D, 0, GL45.GL_RGBA, bfi.getWidth(), bfi.getHeight(), 0, GL45.GL_RGBA, GL45.GL_UNSIGNED_BYTE, ib);
		GL45.glBindTexture(GL45.GL_TEXTURE_2D, 0);
		GlStateManager.unbindTexture2D();
		return tid;
	}

	public int getLightMap() {
		return fb.getTxtID();
	}
}

