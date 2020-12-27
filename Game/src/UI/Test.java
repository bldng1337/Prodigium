package UI;

import me.engine.Engine;
import me.engine.Gui.InGame.UIElement;
import me.engine.Utils.Renderer;

public class Test extends UIElement{

	public Test(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	@Override
	public void render(Renderer r) {
		Engine.getEngine().getFontRenderer().draw("Lorem Ipsum dolor sit amet\\nWelcome to whats fooking cooking yeet", 200, 200, 40);
	}

}
