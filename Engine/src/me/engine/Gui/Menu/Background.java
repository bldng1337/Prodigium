package me.engine.Gui.Menu;

import me.engine.Engine;
import me.engine.Gui.UIElement;
import me.engine.Utils.Renderer;

public class Background extends UIElement {
	long txtid;
	public Background(float x, float y, float width, float height,String txtid) {
		super(x, y, width, height);
		this.txtid=Engine.getEngine().getTex().getTexture(txtid);
	}

	@Override
	public void render(Renderer r) {
		r.setTexCoords(0, 0, 1, 0.1899f);
		r.renderRect(x, y, width, height, txtid, 0);
		r.resetTexCoords();
	}
	

}
