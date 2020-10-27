import me.engine.Main;
import me.engine.Utils.Event.EventManager;
import me.engine.Utils.Event.EventTarget;
import me.engine.Utils.Event.EventTarget.priority;
import me.engine.Utils.Event.Events.Initialization;
import me.engine.Utils.Event.Events.Render2D;

public class ImHook {
	
	ImGuiComponent imcomp;
	EntityEditor enedit;

	/**
	 * Starts the Game Engine and Renders an Editor ontop of it
	 */
	public ImHook() {
		EventManager.register(this);
		new Main();
	}
	
	public static void main(String[] args) {
		new ImHook();
	}
	
	@EventTarget
	public void onInit(Initialization i) {
		imcomp=new ImGuiComponent();
		imcomp.initImGui();
		enedit=new EntityEditor();
	}
	
	@EventTarget(p=priority.LAST)
	public void onRender(Render2D r) {
		enedit.render(1920/2, 1080/2);
		imcomp.update(r.getDeltatime(), enedit::imGui);
	}

}
