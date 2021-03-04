package me.engine.Gui;

import me.engine.Utils.Renderer;
import me.engine.Utils.Event.Events.MouseMoved;
import me.engine.Utils.Event.Events.MousePressed;

public abstract class UIElement {
	
	protected float x,y,width,height;
	protected boolean hovered;
	
	public UIElement(float x,float y,float width,float height) {
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
	}
	
	public void onMouseMoved(MouseMoved a) {hovered=isHovered(a.getX(), a.getY());}
	public boolean onClicked(MousePressed a) {return false;}
	public abstract void render(Renderer r);
	
	protected boolean isHovered(double x, double y) {
		return ((x >= this.x && x <= this.x+width)
				&& (y >= this.y && y <= this.y+height));
	}
	
}
