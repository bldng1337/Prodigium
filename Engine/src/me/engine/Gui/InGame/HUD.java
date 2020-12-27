package me.engine.Gui.InGame;

import java.util.ArrayList;

import me.engine.Engine;
import me.engine.Utils.Event.EventManager;
import me.engine.Utils.Event.EventTarget;
import me.engine.Utils.Event.Events.MousePressed;
import me.engine.Utils.Event.Events.Render2D;

public class HUD {
	private ArrayList<UIElement> elements=new ArrayList<>();
	
	public HUD() {
		EventManager.register(this);
	}
	
	@EventTarget
	public void render(Render2D r) {
		elements.forEach(a->a.render(Engine.getEngine().getUIrender()));
	}
	
	@EventTarget
	public void onClick(MousePressed ms) {
		elements.forEach(a->a.onClicked(ms));
	}
	
	public void add(UIElement a) {
		elements.add(a);
	}
	
	public void remove(UIElement a) {
		elements.remove(a);
	}
	
}
