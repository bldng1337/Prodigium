import org.joml.Vector2f;

import me.engine.Engine;
import me.engine.Entity.Entity;
import me.engine.Utils.Event.EventManager;
import me.engine.Utils.Event.EventTarget;
import me.engine.Utils.Event.EventTarget.priority;
import me.engine.Utils.Event.Events.Initialization;
import me.engine.Utils.Event.Events.KeyPressed;
import me.engine.Utils.Event.Events.Update;
import me.engine.World.Levels.Maze.MazeLevel;
import me.game.Gui.GuiMainMenu;

public class Main {
	Entity e;
	public Main() {
		EventManager.register(this);
		new Engine();
	}
	
	public static void main(String[] args) {
		new Main();
	}
	
	@EventTarget
	public void oninit(Initialization i) {
		e=Engine.getEngine().getEntityManager().newEntity("Entities.Test.Testentity:json");
		e.x=1000;
		e.y=1000;
		Engine.getEngine().setCurrlevel(new MazeLevel(150));
		Engine.getEngine().getCurrlevel().addEntity(e);
		Engine.getEngine().getRender().c.setP(()->new Vector2f(e.x,e.y));
		//new GuiMainMenu();
	}
	int xmo=0;
	int ymo=0;
	
	@EventTarget(p=priority.HIGH)
	public void onUpdate(Update u) {
		if(Math.abs(e.motionX)<10)
			e.motionX+=xmo*2;
		if(Math.abs(e.motionY)<10)
			e.motionY+=ymo*2;
//		if(xmo!=0&&ymo!=0) {
//			xmo/=1.4;
//			ymo/=1.4;
//		}
	}
	
	
	@EventTarget
	public void onKeyPressed(KeyPressed k) {
		switch(k.getKey()) {
		case "r":
			if(k.isKeyDown()) {
				
			}
			break;
		case "w":
			if(k.isKeyDown()) {
				ymo=-1;
			}else {
				ymo=0;
			}
			break;
		case "a":
			if(k.isKeyDown()) {
				xmo=-1;
			}else {
				xmo=0;
			}
			e.setRenderflipped(true);
			break;
		case "s":
			if(k.isKeyDown()) {
				ymo=1;
			}else {
				ymo=0;
			}
			break;
		case "d":
			if(k.isKeyDown()) {
				xmo=1;
			}else {
				xmo=0;
			}
			e.setRenderflipped(false);
			break;
		}
	}
	
}
