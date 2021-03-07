package me.engine.World.Tiles;

import org.joml.Vector2f;
import org.joml.Vector4f;

import me.engine.World.GameLevel;

public class Empty extends Tile{

	@Override
	public int render(Vector2f pos, float[] vertecies, float[] txt, float[] col, int i, GameLevel l) {
		return i;
	}

	@Override
	public Vector4f getBB(Vector2f pos) {
		return new Vector4f(pos.x,pos.y,pos.x+1,pos.y+1);
	}

	@Override
	public Vector4f getLightBB(Vector2f pos) {
		return new Vector4f(pos.x,pos.y,pos.x+1,pos.y+1);
	}

	@Override
	public boolean isCollideable() {
		return true;
	}

	@Override
	public long getPrimaryTex() {
		return 0;
	}

}
