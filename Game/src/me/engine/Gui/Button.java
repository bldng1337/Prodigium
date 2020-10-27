package me.engine.Gui;

import me.engine.Main;

public class Button
{
	int id;
	public float posX, posY;
	public final float width, height;
	final long TEXTURE, HOVER_TEXTURE;
	boolean hovering = false;
	
	public Button(int id, float x, float y, float width, float height, String textureName) {
		this.id = id;
		this.width = width;
		this.height = height;
		posX = x;
		posY = y;
		TEXTURE = Main.getTex().getTexture("Textures.Gui." + textureName + ":png");
		HOVER_TEXTURE = Main.getTex().getTexture("Textures.Gui." + textureName + "_hover:png");
	}

	public void drawButton() {
		Main.getUIrender().renderRect(posX, posY, width, height, hovering ? HOVER_TEXTURE : TEXTURE, 0);
	}

	public boolean isHovered(double x, double y) {
		if ((x >= posX && x <= posX+width)
			&& (y >= posY && y <= posY+height))
				return true;
		return false;
	}
}
