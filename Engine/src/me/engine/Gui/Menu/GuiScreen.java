package me.engine.Gui.Menu;

import java.util.ArrayList;

import me.engine.Engine;
import me.engine.Gui.UIElement;
import me.engine.Utils.Event.EventManager;
import me.engine.Utils.Event.EventTarget;
import me.engine.Utils.Event.Events.MouseMoved;
import me.engine.Utils.Event.Events.MousePressed;
import me.engine.Utils.Event.Events.Render2D;

public class GuiScreen
{
	public ArrayList<UIElement> elementlist = new ArrayList<>();
	
	public GuiScreen() {
		EventManager.register(this);
		initGui();
	}

	public void initGui() {}
	
	@EventTarget
	public void onMouseMove(MouseMoved event) {
		for (UIElement ele : elementlist)
			ele.onMouseMoved(event);
	}
	
	@EventTarget
	public void onMouseClick(MousePressed event) {
		for (UIElement ele : elementlist)
			ele.onClicked(event);
	}
	
	@EventTarget
	public void drawScreen(Render2D update) {
		for (UIElement ele : elementlist)
			ele.render(Engine.getEngine().getUIrender());
	}
}
