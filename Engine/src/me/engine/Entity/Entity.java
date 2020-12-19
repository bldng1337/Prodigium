package me.engine.Entity;

import java.util.concurrent.CompletableFuture;

import javax.script.ScriptEngine;

import org.joml.Vector2f;
import org.joml.Vector2i;

import me.engine.Engine;
import me.engine.Scripting.ScriptManager;
import me.engine.Utils.MathUtils;
import me.engine.Utils.Renderer;
import me.engine.Utils.Space;
import me.engine.Utils.Texture;
import me.engine.World.GameLevel;
import me.engine.World.Tiles.Tile;

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
	
	long animationstamp;
	
	GameLevel l;
	float hurttime;
	
	boolean renderflipped;
	
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
	
	public void update() {
		ScriptManager.invoke(script, "update");
	}
	
	public void render(Renderer r) {
		if(renderflipped)
			r.setTexCoords(1, 0, 0, 1);
		long elapsed=System.currentTimeMillis()-animationstamp;
		elapsed/=framedelay;
		Vector2f pos=Space.worldtoscreen(new Vector2f(x, y));
//		Engine.getEngine().getFontRenderer().draw(this.name, pos.x+width/2-Engine.getEngine().getFontRenderer().getWidth(this.name, 15)/2, pos.y-10-Engine.getEngine().getFontRenderer().getHeight(this.name, 15), 15);
//		r.renderRect(x, y, width, height, 0x22FFFFFF);
		Engine.getEngine().getUIrender().renderRect(pos.x, pos.y-10, width*(Math.max(health,0)/maxhealth), 5, MathUtils.torgba(1-(Math.max(health,0)/maxhealth), (Math.max(health,0)/maxhealth), 0f, 2f));
		if(hurttime>0)
			hurttime-=0.1f;
		else hurttime=0;
		r.setColor(255,(int)(255-hurttime*255f),(int)(255-hurttime*255f),255);
		r.renderRect(x, y, width, height, getTextureid(), (int)elapsed%Texture.getaniframes(getTextureid()));
		if(elapsed>Texture.getaniframes(getTextureid()))
			finishedanimation();
		if(renderflipped)
			r.resetTexCoords();
	}
	
	public void finishedanimation() {
		if(currTexture.equals(Animation.ATTACKING)) {
			currTexture=Animation.IDLE;
		}
		if(currTexture.equals(Animation.DEATH))
			l.removeEntity(this);
		animationstamp=System.currentTimeMillis();	
	}
	
	public void attackEntity(Entity other) {
		float angle=(float) Math.atan2(x-other.x, y-other.y);
		float knockback=20f;
		other.motionX-=Math.sin(angle)*knockback;
		other.motionY-=Math.cos(angle)*knockback;
		motionX+=Math.sin(angle)*knockback/4;
		motionY+=Math.cos(angle)*knockback/4;
	}
	
	public void damageEntity(float dmg) {
		hurttime=1;
		health-=dmg;
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
		if(dist(x, y)<Tile.SIZE*2) {
			path=new Vector2i[]{new Vector2i(x/Tile.SIZE,y/Tile.SIZE)};
			return;
		}
		if(!ispathfinding()) {
			pathfind = CompletableFuture.supplyAsync(() -> Pathfinder.AStar(new Vector2i((int)(this.x/Tile.SIZE),(int)(this.y/Tile.SIZE)), new Vector2i(x/Tile.SIZE,y/Tile.SIZE)));
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
				if(Math.abs(pos[posindex].x*Tile.SIZE-x)>10){
					motionX=pos[posindex].x*Tile.SIZE-x<0?-1:1;
					motionX*=speed;
				}
				if(Math.abs(pos[posindex].y*Tile.SIZE-y)>10){
					motionY=pos[posindex].y*Tile.SIZE-y<0?-1:1;
					motionY*=speed;
				}
				if(Math.abs((x-pos[posindex].x*Tile.SIZE)+(y-pos[posindex].y*Tile.SIZE))<50)
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
		return (int)elapsed%Texture.getaniframes(getTextureid());
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

