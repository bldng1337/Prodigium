package me.engine.Gui.Menu;

import me.engine.Engine;
import me.engine.Utils.MathUtils;
import me.engine.Utils.Renderer;

public class CharacterPreview extends Button{
	final long prev;
	float hovertime=0;
	public CharacterPreview(Runnable onPress, float x, float y, float width, float height, String Name, String prev) {
		super(onPress, x, y, width, height, Name, 0);
		this.prev=Engine.getEngine().getTex().getTexture(prev);
	}
	
	@Override
	public void render(Renderer r) {
		final float speed=0.01f;
		if(hovered) {
			hovertime=Math.min(0.3f, hovertime+speed);
		}else {
			hovertime=Math.max(0.1f, hovertime-speed);
		}
		r.renderVGradient(x, y, width, height, MathUtils.torgba(hovertime, hovertime, hovertime, 1f),0xFF000000);
		Engine.getEngine().getFontRenderer().draw(Name, x+width/2-Engine.getEngine().getFontRenderer().getWidth(Name, 40)/2, y+Engine.getEngine().getFontRenderer().getHeight("Hy", 40), 40);
		r.renderRectAR(x+width*0.1f, y+height*0.1f, width*0.9f, height*0.9f, prev,0);
	}
}
