
import me.engine.Engine;
import me.engine.Utils.Event.EventManager;
import me.engine.Utils.Event.EventTarget;
import me.engine.Utils.Event.Events.Initialization;
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

