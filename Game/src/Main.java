import org.joml.Vector2f;

import me.engine.Engine;
import me.engine.Entity.Animation;
import me.engine.Entity.Entity;
import me.engine.Utils.Profiler;
import me.engine.Utils.Event.EventManager;
import me.engine.Utils.Event.EventTarget;
import me.engine.Utils.Event.EventTarget.priority;
import me.engine.Utils.Event.Events.Initialization;
import me.engine.Utils.Event.Events.KeyPressed;
import me.engine.Utils.Event.Events.KeyPressed.Action;
import me.engine.Utils.Event.Events.MousePressed;
import me.engine.Utils.Event.Events.Render;
import me.engine.Utils.Event.Events.Render2D;
import me.engine.Utils.Event.Events.Update;
import me.engine.World.Tile;
import me.engine.World.Levels.Maze.MazeLevel;

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
	@EventTarget
	public void oninit(Initialization i) {
//		imcomp=new ImGuiComponent();
//		imcomp.initImGui();
		e=Engine.getEngine().getEntityManager().newEntity("Entities.TestPlayer:json");
		e.x=500;
		e.y=500;
		Engine.getEngine().setCurrlevel(new MazeLevel(50));
		Engine.getEngine().getCurrlevel().setPlayer(e);
		Engine.getEngine().getCurrlevel().addEntity(e);
		Engine.getEngine().getRender().c.setP(()->new Vector2f(e.x,e.y));
		posindex=0;
//		p=new Profiler();
//		Engine.getEngine().setProfiler(p);
//		p.startTimer("FrameTime");
	}
	
	@EventTarget
	public void onRender(Render r) {
		
	}
	
	@EventTarget
	public void onMouse(MousePressed mp) {
		Vector2f m=new Vector2f((float)mp.getX(),(float)mp.getY());
		m.add(Engine.getEngine().getRender().c.getTranslate());
		e.x=m.x-e.getWidth()/2;
		e.y=m.y-e.getHeight()/2;
	}
	
	boolean AdjustScales=false;
	
	@EventTarget
	public void onRender(Render2D r) {
		Engine.getEngine().getUIrender().setTexCoords(0.5f, 0.5f, 0.5f, 0.5f);
		float rendersize=300;
		int mapsize=60;
		mapsize=Math.min(mapsize, (int)rendersize);
		float res=rendersize/mapsize;
		
		Engine.getEngine().getUIrender().renderRect(0, 0, rendersize, rendersize, 0xFF000000);
		int sx=(int) (e.x/Tile.SIZE-mapsize/2f);
		int sy=(int) (e.y/Tile.SIZE-mapsize/2f);
		for(int x=sx;x<e.x/Tile.SIZE+mapsize/2f;x++) {
			for(int y=sy;y<e.y/Tile.SIZE+mapsize/2f;y++) {
				if(x<0||y<0)
					continue;
				long txt=Engine.getEngine().getCurrlevel().getTile(x, y).getTexid();
				Engine.getEngine().getUIrender().renderRect(1+(x-sx)*res, 1+(y-sy)*res, res, res, txt,0);
			}
		}
		Engine.getEngine().getUIrender().resetTexCoords();
		Engine.getEngine().getUIrender().renderRect(1+mapsize/2f*res-res/2f, 1+mapsize/2f*res-res/2, res, res, 0xFF00FF00);
		for(Entity en:Engine.getEngine().getCurrlevel().getEntitys()) {
			float rx=e.x/Tile.SIZE-en.x/Tile.SIZE+mapsize/2f;
			float ry=e.y/Tile.SIZE-en.y/Tile.SIZE+mapsize/2f;
			if(rx<0||ry<0||rx>rendersize||ry>rendersize) {
				Engine.getEngine().getUIrender().renderRect(rx, ry, res, res, 0xFF0000FF);
			}
		}
//		p.stopTimer("FrameTime");
//		try {
//		imcomp.update(r.getDeltatime(), ()->{
//			ImGui(p.gettimemap());
//		});
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//		p.startTimer("FrameTime");
	}
	int xmo=0;
	int ymo=0;
	
	@EventTarget(p=priority.HIGH)
	public void onUpdate(Update u) {
//		if(posindex>=pos.length)
//			return;
//		e.motionX=pos[posindex].x*Tile.SIZE-e.x<0?-1:1;
//		e.motionX*=e.speed;
//		e.motionY=pos[posindex].y*Tile.SIZE-e.y<0?-1:1;
//		e.motionY*=e.speed;
//		if(Math.abs((e.x-pos[posindex].x*Tile.SIZE)+(e.y-pos[posindex].y*Tile.SIZE))<50)
//			posindex++;
		if(Math.abs(e.motionX)<10)
			e.motionX+=xmo*2;
		if(Math.abs(e.motionY)<10)
			e.motionY+=ymo*2;
		if(xmo!=0&&ymo!=0) {
			xmo/=1.4;
			ymo/=1.4;
		}
	}
	
	
	
	
	@EventTarget
	public void onKeyPressed(KeyPressed k) {
		switch(k.getKey().toLowerCase()) {
		case "r":
			try {
			if(k.getAction().equals(Action.PRESSED)&&e.getAnimation()!=Animation.ATTACKING) {
				e.setAnimation(Animation.ATTACKING);
				for(Entity e: Engine.getEngine().getCurrlevel().getEntitys()) {
					if(e.intersects(this.e)&&e!=this.e) {
						e.damageEntity(10);
						this.e.attackEntity(e);
					}
				}
			}
			}catch(Exception e) {
				e.printStackTrace();
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
