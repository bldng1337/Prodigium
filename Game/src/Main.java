

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import UI.Minimap;
import UI.Statbar;
import me.engine.Engine;
import me.engine.Entity.Animation;
import me.engine.Entity.Entity;
import me.engine.Utils.LightRenderer.Light;
import me.engine.Utils.Profiler;
import me.engine.Utils.Space;
import me.engine.Utils.Event.EventManager;
import me.engine.Utils.Event.EventTarget;
import me.engine.Utils.Event.EventTarget.priority;
import me.engine.Utils.Event.Events.Initialization;
import me.engine.Utils.Event.Events.KeyPressed;
import me.engine.Utils.Event.Events.KeyPressed.Action;
import me.engine.Utils.Event.Events.MousePressed;
import me.engine.Utils.Event.Events.Update;
import me.engine.World.Levels.Maze.MazeLevel;
import me.game.Gui.CharacterMenu;
import me.game.Gui.GuiMainMenu;

public class Main {
	public Main() {
		EventManager.register(this);
		new Engine();
	}
	
	public static void main(String[] args) {
		new Main();
	}
	@EventTarget
	public void oninit(Initialization i) {
		Engine.getEngine().setGuiscreen(new GuiMainMenu());
	}
//	@EventTarget
//	public void onMouse(MousePressed mp) {
//		if(mp.getPressed()==GLFW.GLFW_RELEASE)
//			return;
//		Engine.getEngine().getLightRenderer().createLight(Space.screentoworldr(new Vector2f((float)mp.getX(),(float)mp.getY())), 1400, 0xFF000000|(int)(Math.random()*0x00FFFFFF));
//	}
}

