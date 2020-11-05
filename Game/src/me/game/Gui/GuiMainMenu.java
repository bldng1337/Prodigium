package me.game.Gui;

import me.engine.Engine;
import me.engine.Gui.Button;
import me.engine.Gui.GuiScreen;

public class GuiMainMenu extends GuiScreen
{
	public GuiMainMenu() {
		initGui();
	}
	
	@Override
	public void performAction(int action) {
		switch (action) {
			case 0:
				//
				break;
		}
	}
	
	@Override
	public void initGui() {
		Engine.getEngine().getUIrender().setTexCoords(8/591f, 7/591f, 223/591f, 108/591f);
		buttonlist.add(new Button(0, 500, 500, 223, 108, "buttons"));
		Engine.getEngine().getUIrender().resetTexCoords();
	}
}
