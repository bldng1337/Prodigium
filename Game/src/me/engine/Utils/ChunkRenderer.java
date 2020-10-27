package me.engine.Utils;

import java.util.ArrayList;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL45;

import me.engine.Main;

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
		c=Main.getM().getRender().c;
		s=Main.getM().getRender().s;
	}
	
	
	
	
	/**
	 * Renders all Buffer in the Renderlist
	 */
	public void render() {
		for(VertexBuffer vb:renderlist) {
			Main.getM().getRender().s.bind();
			Main.getM().getRender().s.useUniform("projection", Renderer.projection);
			Main.getM().getRender().s.useUniform("scale", Renderer.scale);
			Main.getM().getRender().s.useUniform("u_Textures", 0, 1, 2, 3, 4, 5, 6);
			Main.getM().getRender().s.useUniform("u_Transform", Main.getM().getRender().c.translate);
			Main.getM().getTex().bind();
			vb.bind(0);
			vb.bind(1);
			GL45.glDrawArrays(GL45.GL_TRIANGLES, 0, vb.getbuffersize(0));
			vb.unbind();
			Main.getM().getRender().s.unbind();
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
