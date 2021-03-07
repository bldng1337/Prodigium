

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

public class Main {
	Entity e;
	public Main() {
		EventManager.register(this);
		new Engine();
	}
	
	public static void main(String[] args) {
		new Main();
	}
	int posindex;
	long txt;
	Profiler p;
	Light l;
	@EventTarget
	public void oninit(Initialization i) {
//		imcomp=new ImGuiComponent();
//		imcomp.initImGui();
		
//		p=new Profiler();
//		Engine.getEngine().setProfiler(p);
//		p.startTimer("FrameTime");
		
		
		
		Engine.getEngine().setGuiscreen(new CharacterMenu(this::startGame));
//		startGame("Entities.TestPlayer:json");
	}
	
	public void startGame(String player) {
		e=Engine.getEngine().getEntityManager().newEntity(player);
		e.x=500;
		e.y=500;
		Engine.getEngine().setCurrlevel(new MazeLevel(50));
		Engine.getEngine().getCurrlevel().setPlayer(e);
		Engine.getEngine().getCurrlevel().addEntity(e);
		Engine.getEngine().getRender().c.setP(()->new Vector2f(e.x,e.y));
		posindex=0;
		Engine.getEngine().getHud().add(new Minimap(0, 0, 200));
		Engine.getEngine().getHud().add(new Statbar(-60,1080-140,800,140,e));
		Engine.getEngine().getPm().addParticleSystem((a)->{a.getMotion().set(0, -0.1);a.setSize(4);}).setColor(0x35FFFFFF).setMax(20).setMaxLifetime(6000).setSpawndelay(30);
		l=Engine.getEngine().getLightRenderer().createLight(new Vector2f(400,400), 900, 0xFF3c4ce7);
		Engine.getEngine().setGuiscreen(null);
	}
	
	@EventTarget
	public void onMouse(MousePressed mp) {
		if(mp.getPressed()==GLFW.GLFW_RELEASE)
			return;
		Engine.getEngine().getLightRenderer().createLight(Space.screentoworldr(new Vector2f((float)mp.getX(),(float)mp.getY())), 1400, 0xFF000000|(int)(Math.random()*0x00FFFFFF));
	}
	
	@EventTarget(p=priority.HIGH)
	public void onUpdate(Update u) {
		if(l!=null)
			l.getPos().set(e.x+e.getWidth()/2,e.y+e.getHeight()/2);
	}
	
}

