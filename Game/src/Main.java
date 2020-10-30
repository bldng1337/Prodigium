import org.joml.Vector2f;

import me.engine.Engine;
import me.engine.Entity.Entity;
import me.engine.Utils.Event.EventManager;
import me.engine.Utils.Event.EventTarget;
import me.engine.Utils.Event.Events.Initialization;
import me.engine.Utils.Event.Events.KeyPressed;
import me.engine.World.Levels.SimpleLevel.SimpleLevel;

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
		Engine.getEngine().setCurrlevel(new SimpleLevel(150, "Textures.Boden.Bodenplatte_1:png","Textures.Boden.Bodenplatte_2:png","Textures.Boden.Bodenplatte_3:png","Textures.Test.testground:png"));
		Engine.getEngine().getCurrlevel().addEntity(e);
		Engine.getEngine().getRender().c.setP(()->new Vector2f(e.x,e.y));
	}
	
	@EventTarget
	public void onKeyPressed(KeyPressed k) {
		switch(k.getKey()) {
		case "w":
			if(e.motionY>-10)
				e.motionY-=5;
			break;
		case "a":
			if(e.motionX>-10)
				e.motionX-=5;
			break;
		case "s":
			if(e.motionY<10)
				e.motionY+=5;
			break;
		case "d":
			if(e.motionX<10)
				e.motionX+=5;
			break;
		}
	}
	
}
