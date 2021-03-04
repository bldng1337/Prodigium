package me.engine.Gui.Menu;


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
	
	@Override
	public boolean onClicked(MousePressed a) {
		if(this.isHovered(a.getX(), a.getX())) {
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
	}
}
