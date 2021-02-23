package me.engine.Utils;

import java.util.ArrayList;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL45;

import me.engine.Engine;

/**
 * @author Christian
 * An Renderer that Renders the Tiles of Chunks
 */
public class ChunkRenderer {
	/**
	 * The Shader Program used for Rendering
	 */
	Shader s;
	/**
	 * Scale and Projection Matrices
	 */
	Matrix4f projection,scale;
	/**
	 * Camera System for offset
	 */
	Camera c;
	/**
	 * List of static VertexBuffers that get Renderd
	 */
	ArrayList<VertexBuffer>renderlist;
	
	public ChunkRenderer() {
		renderlist=new ArrayList<>();
		projection=Renderer.projection;
		scale=Renderer.scale;
		c=Engine.getEngine().getRender().c;
		s=Engine.getEngine().getRender().s;
//		s=new Shader(new File(Engine.dir.getAbsolutePath()+"\\Assets\\Shader\\chunk.frag"), new File(Engine.dir.getAbsolutePath()+"\\Assets\\Shader\\chunk.vert"));
	}
	
	/**
	 * Renders all Buffer in the Renderlist
	 */
	public void render() {
		for(VertexBuffer vb:renderlist) {
			s.bind();
			Engine.getEngine().getRender().s.useUniform("projection", Renderer.projection);
			Engine.getEngine().getRender().s.useUniform("scale", Renderer.scale);
			Engine.getEngine().getRender().s.useUniform("u_Textures", 0, 1, 2, 3, 4, 5, 6, 7);
			Engine.getEngine().getRender().s.useUniform("u_Transform", Engine.getEngine().getRender().c.translate);
			Engine.getEngine().getRender().s.useUniform("u_Resolution", Engine.getEngine().getWindowwidth(),Engine.getEngine().getWindowheight());Engine.getEngine().getTex().bind();
			GL45.glBindTextureUnit(7, Engine.getEngine().getRender().lightmap);
			vb.bind(0);
			vb.bind(1);
			vb.bind(2);
			vb.bind(3);
			GL45.glDrawArrays(GL45.GL_TRIANGLES, 0, vb.getbuffersize(0));
			vb.unbind();
			s.unbind();
		}
		
	}

	/**
	 * @param renderChunk Adds an Buffer to the Renderlist
	 */
	public void add(VertexBuffer renderChunk) {
		renderlist.add(renderChunk);
	}

	public void remove(VertexBuffer renderChunk) {
		renderlist.remove(renderChunk);
	}
	
	public int loadedChunks() {
		return renderlist.size();
	}
	

}
