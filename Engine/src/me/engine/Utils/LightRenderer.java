package me.engine.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.joml.Vector2f;
import org.lwjgl.opengl.GL45;

import me.engine.Engine;
import me.engine.Utils.GlStateManager.Func;
import me.engine.Utils.Event.EventManager;
import me.engine.Utils.Event.EventTarget;
import me.engine.Utils.Event.EventTarget.priority;
import me.engine.Utils.Event.Events.Render;
import me.engine.World.Tiles.Tile;

public class LightRenderer extends Renderer{
	ArrayList<Light> lights;
	Shader lightshader;
	FrameBuffer fb;
	
	public LightRenderer() {
		c=Engine.getEngine().getRender().c;
		s=Engine.getEngine().getRender().s;
		EventManager.register(this);
		lights=new ArrayList<>();
		
		fb=new FrameBuffer(Engine.getEngine().getWindowwidth(), Engine.getEngine().getWindowheight());
		lightshader=new Shader(FileUtils.getFilefromID("Shader.light:frag"), FileUtils.getFilefromID("Shader.std:vert"));
		lights.add(new Light(new Vector2f(400,400), 600, 0x5F13FFFF));
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
	
	@EventTarget(p = priority.LAST)
	public void update(Render r) {
		fb.clear();
		fb.bind();
		this.renderRect(0, 0, 1920, 1080, 0xFF);
		this.flush();
		for(Light l:lights) {
			GlStateManager.makeMask();
			MathUtils.drawCircle((v)->{
				if(v.x<0||v.y<0)
					return;
				Tile t=Engine.getEngine().getCurrlevel().getTile((int)v.x, (int)v.y);
				if(t.isCollideable()) {
					float[][] vl=t.getEdges(v.floor());
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
			this.renderRect(l.pos.x-l.size/2, l.pos.y-l.size/2, l.size, l.size, l.argb);
			this.flush();
			GlStateManager.disableMask();
		}
		fb.unbind();
		this.setTexCoords(0, 1, 1, 0);
		this.drawTexturedRect(0, 0, 1920, 1080, 0xFFFFFFFF);
		this.resetTexCoords();
		this.flush();
	}
	
	@Override
	public void flush() {
//		super.flush();
		if(fb.isbound()) {
			if(vindex<18)
				return;
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
			s.bind();
			s.useUniform("projection", fb.getProjection());
			s.useUniform("scale", fb.getScale());
			s.useUniform("u_Textures", 0, 1, 2, 3, 4, 5, 6);
			s.useUniform("u_Transform", c.translate);
			Engine.getEngine().getTex().bind();
			v.bind(0);
			v.bind(1);
			v.bind(2);
			GL45.glDrawArrays(GL45.GL_TRIANGLES, 0, vindex);
			v.unbind();
			s.unbind();
			cindex=0;
			vindex=0;
			tindex=0;
			vertices=new float[MAXDRAW];
			txt=new float[MAXDRAW];
			col=new float[MAXDRAW/3*4];
		}else {
			if(vindex<18)
				return;
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
			lightshader.useUniform("projection", projection);
			lightshader.useUniform("scale", scale);
			lightshader.useUniform("u_Texture", 0);
			lightshader.useUniform("u_Transform", new Vector2f());
			GL45.glBindTextureUnit(0, fb.getTxtID());
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
	
	private void drawTexturedRect(float x, float y, float width, float height, int rgba) {
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
		
		if(vindex>MAXDRAW-19)
			flush();
	}
}

