package me.engine.Utils;

import java.io.File;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL45;

import me.engine.Engine;
import me.engine.Utils.Event.EventManager;

/**
 * @author Christian
 * The 2D Renderer for dynamic Rendering
 */
public class Renderer {

	/**
	 * The Max Number of Vertices that get packed in one Drawcall
	 */
	public static final int MAXDRAW=18*1000;
	/**
	 * Array of Vertices that get drawn next
	 */
	float[] vertices;
	/**
	 * Array of TextureCoordinates
	 */
	float[] txt;
	/**
	 * Array of Color tints
	 */
	float[] col;
	/**
	 * The write pos from the vertices and texcoords Arrays 
	 */
	int vindex=0,tindex=0,cindex=0;
	/**
	 * The VertexBuffer which gets populated with the Vertex data
	 */
	private VertexBuffer v;
	/**
	 * Scale and Projection Matrices
	 */
	static Matrix4f scale,projection;
	/**
	 * Camera System for offset
	 */
	public Camera c;
	/**
	 * The Shader Program used for Rendering
	 */
	Shader s;
	
	int rgba;
	
	Vector4f texcoords;
	
	public Renderer() {
		s=new Shader(new File(Engine.dir.getAbsolutePath()+"\\Assets\\Shader\\std.frag"), new File(Engine.dir.getAbsolutePath()+"\\Assets\\Shader\\std.vert"));
		vertices=new float[MAXDRAW];
		txt=new float[MAXDRAW];
		col=new float[MAXDRAW/3*4];
		c=new Camera();
		EventManager.register(c);
		texcoords=new Vector4f();
		resetTexCoords();
		resetColor();
	}
	
	/**
	 * Adds an Rect to the RenderList
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @param width The width of the Rect
	 * @param height The height of the Rect
	 * @param texid The TextureID
	 * @param frame The Frame of Animation
	 */
	public void renderRect(float x, float y, float width, float height, long texid,int frame) {
		float tx=Texture.getx(texid)+Texture.getdx(texid)*frame+Texture.getdx(texid)*texcoords.x;
		float ty=Texture.gety(texid)+Texture.getdy(texid)*texcoords.y;
		float tx2=Texture.getx(texid)+Texture.getdx(texid)*frame+Texture.getdx(texid)*texcoords.z;
		float ty2=Texture.gety(texid)+Texture.getdy(texid)*texcoords.w;
		int atlas=Texture.getatlas(texid);
		
		tx/=Engine.getEngine().getTex().msize;
		ty/=Engine.getEngine().getTex().msize;
		tx2/=Engine.getEngine().getTex().msize;
		ty2/=Engine.getEngine().getTex().msize;
		
		int blue = rgba & 0xff;
		int green = (rgba & 0xff00) >> 8;
		int red = (rgba & 0xff0000) >> 16;
		int alpha = (rgba & 0xff000000) >>> 24;
		
		vertices[vindex++]=x;
		vertices[vindex++]=y+height;
		vertices[vindex++]=1;
		txt[tindex++]=tx;
		txt[tindex++]=ty2;
		txt[tindex++]=atlas;
		col[cindex++]=blue/255f;
		col[cindex++]=green/255f;
		col[cindex++]=red/255f;
		col[cindex++]=alpha/255f;
		
		vertices[vindex++]=x;
		vertices[vindex++]=y;
		vertices[vindex++]=1;
		txt[tindex++]=tx;
		txt[tindex++]=ty;
		txt[tindex++]=atlas;
		col[cindex++]=blue/255f;
		col[cindex++]=green/255f;
		col[cindex++]=red/255f;
		col[cindex++]=alpha/255f;
		
		vertices[vindex++]=x+width;
		vertices[vindex++]=y;
		vertices[vindex++]=1;
		txt[tindex++]=tx2;
		txt[tindex++]=ty;
		txt[tindex++]=atlas;
		col[cindex++]=blue/255f;
		col[cindex++]=green/255f;
		col[cindex++]=red/255f;
		col[cindex++]=alpha/255f;
		
		vertices[vindex++]=x;
		vertices[vindex++]=y+height;
		vertices[vindex++]=1;
		txt[tindex++]=tx;
		txt[tindex++]=ty2;
		txt[tindex++]=atlas;
		col[cindex++]=blue/255f;
		col[cindex++]=green/255f;
		col[cindex++]=red/255f;
		col[cindex++]=alpha/255f;
		
		vertices[vindex++]=x+width;
		vertices[vindex++]=y+height;
		vertices[vindex++]=1;
		txt[tindex++]=tx2;
		txt[tindex++]=ty2;
		txt[tindex++]=atlas;
		col[cindex++]=blue/255f;
		col[cindex++]=green/255f;
		col[cindex++]=red/255f;
		col[cindex++]=alpha/255f;
		
		vertices[vindex++]=x+width;
		vertices[vindex++]=y;
		vertices[vindex++]=1;
		txt[tindex++]=tx2;
		txt[tindex++]=ty;
		txt[tindex++]=atlas;
		col[cindex++]=blue/255f;
		col[cindex++]=green/255f;
		col[cindex++]=red/255f;
		col[cindex++]=alpha/255f;
		
		if(vindex>MAXDRAW-19)
			flush();
	}
	
	public void renderRect(float x, float y, float width, float height, int rgba) {
		int blue = rgba & 0xff;
		int green = (rgba & 0xff00) >> 8;
		int red = (rgba & 0xff0000) >> 16;
		int alpha = (rgba & 0xff000000) >>> 24;
		
		vertices[vindex++]=x;
		vertices[vindex++]=y+height;
		vertices[vindex++]=1;
		txt[tindex++]=0;
		txt[tindex++]=0;
		txt[tindex++]=8;
		col[cindex++]=blue/255f;
		col[cindex++]=green/255f;
		col[cindex++]=red/255f;
		col[cindex++]=alpha/255f;
		
		vertices[vindex++]=x;
		vertices[vindex++]=y;
		vertices[vindex++]=1;
		txt[tindex++]=0;
		txt[tindex++]=0;
		txt[tindex++]=8;
		col[cindex++]=blue/255f;
		col[cindex++]=green/255f;
		col[cindex++]=red/255f;
		col[cindex++]=alpha/255f;
		
		vertices[vindex++]=x+width;
		vertices[vindex++]=y;
		vertices[vindex++]=1;
		txt[tindex++]=0;
		txt[tindex++]=0;
		txt[tindex++]=8;
		col[cindex++]=blue/255f;
		col[cindex++]=green/255f;
		col[cindex++]=red/255f;
		col[cindex++]=alpha/255f;
		
		vertices[vindex++]=x;
		vertices[vindex++]=y+height;
		vertices[vindex++]=1;
		txt[tindex++]=0;
		txt[tindex++]=0;
		txt[tindex++]=8;
		col[cindex++]=blue/255f;
		col[cindex++]=green/255f;
		col[cindex++]=red/255f;
		col[cindex++]=alpha/255f;
		
		vertices[vindex++]=x+width;
		vertices[vindex++]=y+height;
		vertices[vindex++]=1;
		txt[tindex++]=0;
		txt[tindex++]=0;
		txt[tindex++]=8;
		col[cindex++]=blue/255f;
		col[cindex++]=green/255f;
		col[cindex++]=red/255f;
		col[cindex++]=alpha/255f;
		
		vertices[vindex++]=x+width;
		vertices[vindex++]=y;
		vertices[vindex++]=1;
		txt[tindex++]=0;
		txt[tindex++]=0;
		txt[tindex++]=8;
		col[cindex++]=blue/255f;
		col[cindex++]=green/255f;
		col[cindex++]=red/255f;
		col[cindex++]=alpha/255f;
		
		if(vindex>MAXDRAW-19)
			flush();
	}
	
	public void resetColor() {
		rgba=0xFFFFFFFF;
	}
	
	public void setColor(int argb) {
		int b = (argb)&0xFF;
		int g = (argb>>8)&0xFF;
		int r = (argb>>16)&0xFF;
		int a = (argb>>24)&0xFF;
		this.setColor(r, g, b, a);
	}
	
	public void setColor(int r,int g,int b,int a) {
		this.rgba=(a << 24)
				| (b << 16)
				| (g << 8)
				| (r << 0);
		
		
	}
	
	public void setTexCoords(float x,float y,float x2,float y2) {
		texcoords.set(x,y,x2,y2);
	}
	
	public void resetTexCoords() {
		texcoords.set(0,0,1,1);
	}
	
	/**
	 * Renders All the Quads in one Drawcall
	 */
	public void flush() {
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
		s.useUniform("projection", projection);
		s.useUniform("scale", scale);
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
	}
	
	/**
	 * Destroys the Renderer
	 */
	public void destroy() {
		vertices=null;
	}
	
	public static void createTransforms() {
		scale=new Matrix4f();
		projection=new Matrix4f();
	}
	
	/**
	 * Clears the Transforms
	 */
	public static void clearTransform() {
		scale.set(new float[]  {1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1});
		projection.set(new float[]  {1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1});
	}

	/**
	 * Transforms the Geometry
	 * Use the Transform of {@link Camera#setP(me.engine.Utils.Camera.CameraPos)}
	 * @param x The x coordinate to be transformed
	 * @param y The y coordinate to be transformed
	 * @param z The z coordinate to be transformed
	 */
	public void transform(float x,float y,float z) {
		scale.translate(x, y, z);
	}
	
	/**
	 * Modifies the Orthographic Projection
	 * @param left 
	 * @param right 
	 * @param bottom 
	 * @param top 
	 * @param zNear The zClipping Plane
	 * @param zFar The zClipping Plane
	 */
	public void ortho(float left, float right, float bottom, float top, float zNear, float zFar) {
		projection.ortho(left, right, bottom, top, zNear, zFar);
	}
	
	/**
	 * Scales the Geometry
	 * @param x The x coordinate to be scaled
	 * @param y The y coordinate to be scaled
	 * @param z The z coordinate to be scaled
	 */
	public void scale(float x,float y,float z) {
		scale.scale(x, y, z);
	}
	
	
	public static Matrix4f getScale() {
		return scale;
	}

	public static Matrix4f getProjection() {
		return projection;
	}
	
}
