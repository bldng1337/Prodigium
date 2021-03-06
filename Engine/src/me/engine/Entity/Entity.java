package me.engine.Entity;

import java.util.concurrent.CompletableFuture;

import javax.script.ScriptEngine;

import org.joml.Vector2f;
import org.joml.Vector2i;

import me.engine.Engine;
import me.engine.Scripting.ScriptManager;
import me.engine.Utils.FileUtils;
import me.engine.Utils.MathUtils;
import me.engine.Utils.ParticleManager;
import me.engine.Utils.ParticleManager.ParticleSystem;
import me.engine.Utils.Renderer;
import me.engine.Utils.Space;
import me.engine.Utils.TextureAtlas;
import me.engine.Utils.Event.EventTarget;
import me.engine.Utils.Event.Events.KeyPressed;
import me.engine.Utils.Event.Events.MouseMoved;
import me.engine.Utils.Event.Events.MousePressed;
import me.engine.World.GameLevel;
import me.engine.World.Tiles.STile;

/**
 * @author Christian 3BHET
 * @version 03.12.2020
 */
public class Entity{
	/**
	 * Array of Textures of that Entity
	 */
	protected long[] textureids;
	/**
	 * The Current Texture
	 */
	public Animation currTexture;
	/**
	 * Stats of that Entity
	 */
	public float x,y,health,maxhealth,speed,motionX,motionY;
	/**
	 * The Dimension of the Entity
	 */
	protected float width,height;
	/**
	 * The Delay between Frames of the Animation
	 */
	protected int framedelay;
	/**
	 * The Name of this Entity
	 */
	protected String name;
	/**
	 * The Script for this Entity
	 */
	protected ScriptEngine script;
	
	private static ParticleSystem bloodpart=Engine.getEngine().getPm().addParticleSystem(ParticleManager.GRAVITY).addSpawningLogic((a)->{a.setSize(3);a.getMotion().set((Math.random()-0.5)*10, Math.random()*-10);return new Vector2f();}).disableNaturalSpawning().nonpersistant().setColor(0xFF0000FF).setMaxLifetime(20);
	
	long animationstamp;
	
	GameLevel l;
	float hurttime;
	
	boolean renderflipped;
	
	public boolean isRenderflipped() {
		return renderflipped;
	}
	CompletableFuture<Vector2i[]> pathfind;
	Vector2i[] path;

	protected Entity() {
		
	}

	/**
	 * @return TextureID
	 */
	public long getTextureid() {
		return textureids[currTexture.gettextureindex()];
	}
	
	
	
	@EventTarget
	public void onMousePressed(MousePressed m) {
		ScriptManager.invoke(script, "mousepressed",m.getX()+Engine.getEngine().getRender().c.getTranslate().x,m.getY()+Engine.getEngine().getRender().c.getTranslate().y,m.getAction());
	}
	
	
	@EventTarget
	public void onMouseMoved(MouseMoved m) {
		ScriptManager.invoke(script, "mousemoved",m.getX()+Engine.getEngine().getRender().c.getTranslate().x,m.getY()+Engine.getEngine().getRender().c.getTranslate().y);
	}
	
	@EventTarget
	public void onKeyPressed(KeyPressed k) {
		ScriptManager.invoke(script, "keypressed",k.getKey(),k.getAction());
	}
	
	public void update() {
		ScriptManager.invoke(script, "update");
	}
	
	public void render(Renderer r) {
		
		long elapsed=System.currentTimeMillis()-animationstamp;
		elapsed/=framedelay;
		Vector2f pos=Space.worldtoscreen(new Vector2f(x, y));
		Engine.getEngine().getUIrender().renderRect(pos.x, pos.y-10, width*(Math.max(health,0)/maxhealth), 5, MathUtils.torgba(1-(Math.max(health,0)/maxhealth), (Math.max(health,0)/maxhealth), 0f, 2f));
		if(renderflipped)
			r.setTexCoords(1, 0, 0, 1);
		if(hurttime>0)
			hurttime-=0.1f;
		else hurttime=0;
		r.setColor(255,(int)(255-hurttime*255f),(int)(255-hurttime*255f),255);
		r.renderRect(x, y, width, height, getTextureid(), (int)elapsed%TextureAtlas.getaniframes(getTextureid()));
		if(elapsed>TextureAtlas.getaniframes(getTextureid()))
			finishedanimation();
		if(renderflipped)
			r.resetTexCoords();
	}
	
	
	public Object get(String name) {
		return script.get(name);
	}
	
	public void finishedanimation() {
		if(currTexture.equals(Animation.ATTACKING)) {
			currTexture=Animation.IDLE;
		}
		if(currTexture.equals(Animation.DEATH)) {
			ScriptManager.invoke(script, "death");
			
			
			l.removeEntity(this);
		}
		animationstamp=System.currentTimeMillis();	
	}
	
	public void include(String id) {
		ScriptManager.append(script, FileUtils.stringfromFile(id));
	}
	
	public void attackEntity(Entity other) {
		float angle=(float) Math.atan2(x-other.x, y-other.y);
		float knockback=40f;
		other.motionX-=Math.sin(angle)*knockback;
		other.motionY-=Math.cos(angle)*knockback;
		motionX+=Math.sin(angle)*knockback/5;
		motionY+=Math.cos(angle)*knockback/5;
	}
	
	public void damageEntity(float dmg) {
		for(int i=0;i<10;i++)
			bloodpart.addParticle(new Vector2f(this.x+this.width/2+(float)(Math.random()-0.5f)*5,this.y+this.height/2+(float)(Math.random()-0.5f)*5));
		hurttime=1;
		health-=dmg;
	}
	
	public Object getField(String Name) {
		return script.get(Name);
	}
	
	public Animation getAnimation() {
		return currTexture;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}
	
	public void pathfind(int x,int y) {
		if(dist(x, y)<STile.SIZE*2) {
			path=new Vector2i[]{new Vector2i(x/STile.SIZE,y/STile.SIZE)};
			return;
		}
		if(!ispathfinding()) {
			pathfind = CompletableFuture.supplyAsync(() -> Pathfinder.AStar(new Vector2i((int)(this.x/STile.SIZE),(int)(this.y/STile.SIZE)), new Vector2i(x/STile.SIZE,y/STile.SIZE)));
			pathfind.whenComplete((a,b)->{
				path=a;
				if(path==null)
					path=new Vector2i[0];
			});
		}
	}
	
	public void setAnimation(Animation a) {
		if(currTexture.equals(a))
			return;
		animationstamp=System.currentTimeMillis();
		currTexture=a;
	}
	int posindex;
	
	public void gotowards(Entity en) {
		Vector2i[] pos=getPath();
		if(!ispathfinding()){
			if(posindex>=pos.length){
				posindex=0;
				resetPath();
			}
			if(pos.length==0){
				if(Math.abs(x-en.x)<100&&Math.abs(y-en.y)<100) {
					motionX=en.x-x<0?-1:1;
					motionY=en.y-y<0?-1:1;
				}
				pathfind((int)en.x,(int)en.y);
				posindex=0;
			}else{
				if(Math.abs(pos[posindex].x*STile.SIZE-x)>10){
					motionX=pos[posindex].x*STile.SIZE-x<0?-1:1;
					motionX*=speed;
				}
				if(Math.abs(pos[posindex].y*STile.SIZE-y)>10){
					motionY=pos[posindex].y*STile.SIZE-y<0?-1:1;
					motionY*=speed;
				}
				if(Math.abs((x-pos[posindex].x*STile.SIZE)+(y-pos[posindex].y*STile.SIZE))<50)
					posindex++;
			}
		}
	}
	
	public void resetPath() {
		path=new Vector2i[0];
	}
	
	public Vector2i[] getPath() {
		if(ispathfinding()||path==null)
			return new Vector2i[0];
		return path;
	}
	
	public boolean ispathfinding() {
		return pathfind!=null&&!pathfind.isDone();
	}
	
	public GameLevel getLevel() {
		return l;
	}
	
	public long getelapsed() {
		return System.currentTimeMillis()-animationstamp;
	}
	
	public int getFrame() {
		long elapsed=getelapsed();
		elapsed/=framedelay;
		return (int)elapsed%TextureAtlas.getaniframes(getTextureid());
	}

	public int getFramedelay() {
		return framedelay;
	}

	public String getName() {
		return name;
	}

	public ScriptEngine getScript() {
		return script;
	}
	
	public void setRenderflipped(boolean renderflipped) {
		this.renderflipped = renderflipped;
	}

	public void destroy() {
		if(pathfind!=null)
			pathfind.cancel(true);
	}

	public void setLevel(GameLevel gameLevel) {
		l=gameLevel;
	}
	
	public boolean intersects(Entity other) {
		return x < other.x + other.width &&
				   x + width > other.x &&
				   y < other.y + other.height &&
				   y + height > other.y;
	}
	
	public float dist(int x,int y) {
		return (float) Math.sqrt(Math.pow(Math.abs(x-this.x),2)+Math.pow(Math.abs(y-this.y),2));
	}
}

