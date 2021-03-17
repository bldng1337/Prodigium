package UI;


import me.engine.Engine;
import me.engine.Entity.Entity;
import me.engine.Gui.UIElement;
import me.engine.Utils.MathUtils;
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
		drawHealthbar(e,r,x,y,width,height);
	}
	
	
	private void drawHealthbar(Entity e,Renderer r,float x,float y,float width,float height) {
		long m=Engine.getEngine().getTex().getTexture("Textures.Gui.healthbar_clear:png");
		r.renderRect(x, y, width, height, m, 0);
		drawBar(r, (float)x+width*0.267f, (float)y+height*0.35f, width*0.67f, height*0.11f, Math.max(e.health,0)/e.maxhealth, MathUtils.torgba((int)((1-(Math.max(e.health,0)/e.maxhealth))*255), (int) ((Math.max(e.health,0)/e.maxhealth)*255), 0,255));
		if(e.get("mana")!=null)
			drawBar(r, (float)x+width*0.267f, (float)y+height*0.53f, width*0.67f, height*0.11f, (float)((double)e.get("mana")/100f), MathUtils.torgba(189,185,246,255));
		r.resetColor();
		r.resetTexCoords();
	}
	
	private void drawBar(Renderer r,float x,float y,float width,float height,float full,int col) {
		long t=Engine.getEngine().getTex().getTexture("Textures.Gui.Healthbar:png");
		r.setColor(col);
		r.setTexCoords(0, 0, full, 1);
		r.renderRect(x, y, width*full, height, t, (int)((System.currentTimeMillis()/100)%TextureAtlas.getaniframes(t)));
	
	}

}
