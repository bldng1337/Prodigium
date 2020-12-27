package me.engine.Gui.InGame;

import me.engine.Utils.Renderer;
import me.engine.Utils.Event.Events.MousePressed;

public abstract class UIElement {
	
	protected int x,y,width,height;
	
	public UIElement(int x,int y,int width,int height) {
		
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
	}
	
	public boolean onClicked(MousePressed a) {return false;}
	public abstract void render(Renderer r);
	
}
