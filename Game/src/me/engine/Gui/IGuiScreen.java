package me.engine.Gui;

import me.engine.Utils.Event.EventTarget;
import me.engine.Utils.Event.Events.Update;

public interface IGuiScreen
{
	public void initGui();
	
	public void performAction(int action);
	
	@EventTarget
	public void drawScreen(Update update);
}
