package me.engine.Gui.Menu;

import java.util.ArrayList;

import org.lwjgl.glfw.GLFW;

import me.engine.Utils.Event.EventManager;
import me.engine.Utils.Event.EventTarget;
import me.engine.Utils.Event.Events.MouseMoved;
import me.engine.Utils.Event.Events.MousePressed;
import me.engine.Utils.Event.Events.Render2D;

public class GuiScreen implements IGuiScreen
{
	public ArrayList<Button> buttonlist = new ArrayList<>();
	
	public GuiScreen() {
		EventManager.register(this);
		initGui();
	}

	@Override
	public void initGui() { }
	
	@EventTarget
	public void onMouseMove(MouseMoved event) {
		for (Button button : buttonlist)
			button.hovering = button.isHovered(event.getX(), event.getY());
	}
	
	@EventTarget
	public void onMouseClick(MousePressed event) {
		if (event.getKey() == GLFW.GLFW_MOUSE_BUTTON_LEFT
			&& event.getPressed() == GLFW.GLFW_PRESS)
				for (Button button : buttonlist)
					if (button.isHovered(event.getX(), event.getY()))
						performAction(button.id);
	}
	
	@Override
	public void performAction(int action) { }
	
	@EventTarget
	public void drawScreen(Render2D update) {
		for (Button button : buttonlist)
			button.drawButton();
	}
}
