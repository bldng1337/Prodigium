package me.engine.Gui.Menu;

import me.engine.Utils.Event.EventTarget;
import me.engine.Utils.Event.Events.Render2D;

public interface IGuiScreen
{
	public void initGui();
	
	public void performAction(int action);
	
	@EventTarget
	public void drawScreen(Render2D update);
}
