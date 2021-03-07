package me.engine.Gui.Menu;

import me.engine.Engine;
import me.engine.Gui.UIElement;
import me.engine.Utils.Renderer;

public class Label extends UIElement{
	boolean centered;
	float size;
	String content;
	public Label(float x, float y, float width, float height,String content,boolean centered) {
		super(x, y, width, height);
		this.centered=centered;
		this.content=content;
		this.size=Engine.getEngine().getFontRenderer().getSize(width, height, content);
	}

	@Override
	public void render(Renderer r) {
		if(centered) {
			Engine.getEngine().getFontRenderer().drawCentred(content,x, y, size);
		}else {
			Engine.getEngine().getFontRenderer().draw(content,x, y, size);
		}
	}

}
