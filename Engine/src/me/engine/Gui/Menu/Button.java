package me.engine.Gui.Menu;


import org.lwjgl.glfw.GLFW;

import me.engine.Engine;
import me.engine.Gui.UIElement;
import me.engine.Utils.Renderer;
import me.engine.Utils.Event.Events.MousePressed;

public class Button extends UIElement
{
	final long TEXTURE, HOVER_TEXTURE;
	int rgb;
	Runnable pressev;
	String Name;
	
	public Button(Runnable onPress, float x, float y, float width, float height,String Name, int abgr) {
		super(x,y,width,height);
		this.Name = Name;
		pressev=onPress;
		TEXTURE = 0;
		this.HOVER_TEXTURE = 0;
		rgb=abgr;
	}
	
	public Button(Runnable onPress, float x, float y, float width, float height, String Name,String Texture) {
		super(x,y,width,height);
		this.Name = Name;
		pressev=onPress;
		TEXTURE = Engine.getEngine().getTex().getTexture(Texture);
		HOVER_TEXTURE = Engine.getEngine().getTex().getTexture(Texture.replace("_button", "_hovered"));
	}
	
	public Button(Runnable onPress, float x, float y, float width, float height, String Name,String Texture,boolean hover) {
		super(x,y,width,height);
		this.Name = Name;
		pressev=onPress;
		TEXTURE = Engine.getEngine().getTex().getTexture(Texture);
		if(hover)
			HOVER_TEXTURE = Engine.getEngine().getTex().getTexture(Texture.replace("_button", "_hovered"));
		else
			HOVER_TEXTURE=0;
	}
	
	@Override
	public boolean onClicked(MousePressed a) {
		if(a.getPressed()!=GLFW.GLFW_PRESS)
			return false;
		if(hovered) {
			pressev.run();
			return true;
		}
		return false;
	}

	@Override
	public void render(Renderer r) {
		if(TEXTURE==0) {
			Engine.getEngine().getUIrender().renderRect(x, y, width, height, rgb);
		}else {
			Engine.getEngine().getUIrender().renderRect(x, y, width, height, (hovered && HOVER_TEXTURE!=0) ? HOVER_TEXTURE : TEXTURE, 0);
		}
		if(hovered)
			Engine.getEngine().getUIrender().setColor(180, 180, 180, 255);
		Engine.getEngine().getFontRenderer().drawCentred(Name, x+width/2, y+height/2+5, Engine.getEngine().getFontRenderer().getSize(width*0.9f, height*0.9f,Name));
		Engine.getEngine().getUIrender().resetColor();
	}
}
