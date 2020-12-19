package me.engine.Gui.Menu;


import me.engine.Engine;

public class Button
{
	int id;
	public float posX, posY;
	public final float width, height;
	final long TEXTURE, HOVER_TEXTURE;
	boolean hovering = false;
	String Name;
	
	//Custom textured button
	public Button(int id, float x, float y, float width, float height, String Name) {
		this.id = id;
		this.width = width;
		this.height = height;
		this.Name = Name;
		posX = x;
		posY = y;
		TEXTURE = Engine.getEngine().getTex().getTexture("Textures.Gui.Button:png");
		HOVER_TEXTURE = Engine.getEngine().getTex().getTexture("Textures.Gui.Button_hover:png");
	}

	public void drawButton() {
		Engine.getEngine().getUIrender().renderRect(posX, posY, width, height, hovering ? HOVER_TEXTURE : TEXTURE, 0);
		try {
			float f=height/4;
			if(hovering)
				f/=1.1;
			Engine.getEngine().getFontRenderer().draw(Name, posX+width/2-Engine.getEngine().getFontRenderer().getWidth(Name, f)/2, posY+height/2-Engine.getEngine().getFontRenderer().getHeight(Name, f)/2, f);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isHovered(double x, double y) {
		return ((x >= posX && x <= posX+width)
				&& (y >= posY && y <= posY+height));
	}
}
