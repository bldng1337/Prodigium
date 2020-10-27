package me.game.Gui;

import me.engine.Gui.Button;
import me.engine.Gui.GuiScreen;

public class Gui extends GuiScreen
{
	public Gui() {
		initGui();
	}
	
	@Override
	public void performAction(int action) {
		switch (action) {
			case 0:
				System.out.println("button clicked");
				break;
		}
	}
	
	@Override
	public void initGui() {
		buttonlist.add(new Button(0, 500, 500, 100, 50, "test"));
		
	}
}
