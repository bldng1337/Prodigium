package UI;


import me.engine.Engine;
import me.engine.Entity.Entity;
import me.engine.Gui.UIElement;
import me.engine.Utils.Renderer;
import me.engine.Utils.TextureAtlas;

public class Statbar extends UIElement{

	Entity e;
	public Statbar(int x, int y, int width, int height,Entity e) {
		super(x, y, width, height);
		this.e=e;
	}

	@Override
	public void render(Renderer r) {
		long m=Engine.getEngine().getTex().getTexture("Textures.UI.Unbenannt:png");
		long t=Engine.getEngine().getTex().getTexture("Textures.UI.Unbenannt:gif");
		r.renderRect(x, y, width, height, m, 0);
		r.setColor((int)((1-(Math.max(e.health,0)/e.maxhealth))*255), (int) ((Math.max(e.health,0)/e.maxhealth)*255), 0,255);
		r.renderRect((float)x+65, (float)y+30, 240f, 22f, t, (int)((System.currentTimeMillis()/100)%TextureAtlas.getaniframes(t)));
		r.resetColor();
		Engine.getEngine().getFontRenderer().draw(e.health+"/"+e.maxhealth, x+66, y+30+Engine.getEngine().getFontRenderer().getHeight(""+e.health, 22)-23, 22);
		
	}

}
