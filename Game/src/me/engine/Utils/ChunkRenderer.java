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
	Shader s;
	Matrix4f projection,scale;
	Camera c;
	ArrayList<VertexBuffer>renderlist;
	
	public ChunkRenderer() {
		renderlist=new ArrayList<>();
		projection=Renderer.projection;
		scale=Renderer.scale;
		c=Main.getRender().c;
		s=Main.getRender().s;
	}
	
	
	
	
	/**
	 * Renders all Buffer in the Renderlist
	 */
	public void render() {
		for(VertexBuffer vb:renderlist) {
			Main.getRender().s.bind();
			Main.getRender().s.useUniform("projection", Renderer.projection);
			Main.getRender().s.useUniform("scale", Renderer.scale);
			Main.getRender().s.useUniform("u_Textures", 0, 1, 2, 3, 4, 5, 6);
			Main.getRender().s.useUniform("u_Transform", Main.getRender().c.translate);
			Main.getTex().bind();
			vb.bind(0);
			vb.bind(1);
			GL45.glDrawArrays(GL45.GL_TRIANGLES, 0, vb.getbuffersize(0));
			vb.unbind();
			Main.getRender().s.unbind();
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
