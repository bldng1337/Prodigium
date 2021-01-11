package me.engine.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.joml.Vector3f;
import org.joml.Vector2f;

import me.engine.Engine;
import me.engine.Utils.Event.EventManager;
import me.engine.Utils.Event.EventTarget;
import me.engine.Utils.Event.Events.Render;
import me.engine.Utils.Event.Events.Update;

public class ParticleManager {
	List<ParticleSystem> l;
	
	public ParticleManager() {
		EventManager.register(this);
		l=new ArrayList<>();
	}
	
	@EventTarget
	public void onRender(Render r) {
		l.forEach(a->a.render());
	}
	
	@EventTarget
	public void onUpdate(Update u) {
		l.forEach(a->a.update());
	}
	
	public ParticleSystem addParticleSystem(Consumer<Particle> update) {
		l.add(new ParticleSystem(update));
		return l.get(l.size()-1);
	}
	public void removeParticleSystem(ParticleSystem psys) {
		l.remove(psys);
	}
	public static final Consumer<Particle> NOTHING=new Consumer<ParticleManager.Particle>() {
		@Override
		public void accept(Particle t) {}
	};
	public static final Consumer<Particle> GRAVITY=new Consumer<ParticleManager.Particle>() {
		@Override
		public void accept(Particle t) {
			t.getMotion().add(0, 0.981f/2f);
		}
	};
	static final Function<Particle,Vector2f> stdspawn=new Function<ParticleManager.Particle, Vector2f>() {
		@Override
		public Vector2f apply(Particle t) {
			return new Vector2f(Engine.getEngine().getRender().c.getTranslate()).add((float)Math.random()*1920, (float)Math.random()*1080);
		}
	};
	public class ParticleSystem {
		List<Particle> l=new ArrayList<>();
		int lifetime,spawnrate,time,max,col;
		boolean persistant;
		Consumer<Particle> func;
		Function<Particle,Vector2f> spawnfunc;
		long txt;
		
		public ParticleSystem(Consumer<Particle> update) {
			this.lifetime=30;
			txt=0;
			persistant=false;
			col=0xFFFFFFFF;
			func=update;
			spawnfunc=stdspawn;
			this.spawnrate=2;
			this.max=30;
		}
		void update() {
			l.forEach(a->a.update(func));
			if(l.size()>0&&l.get(0).lifetime>lifetime)
				l.remove(0);
			if(spawnrate>0&&time>spawnrate&&l.size()<max) {
				time=0;
				Particle p=new Particle();
				Vector2f sl=spawnfunc.apply(p);
				if(sl!=null)
					l.add(p.setPos(sl));
			}
			time++;
		}
		void render() {
			for(int i=l.size()-1;i>=0;i--) {
				Particle a=l.get(i);
				if(Engine.getEngine().getRender().c.shouldberendered(a.pos.x, a.pos.y, a.size, a.size)) {
					if(txt>0) {
						Engine.getEngine().getRender().setColor(col);
						Engine.getEngine().getRender().renderRect(a.pos.x, a.pos.y, a.size, a.size, txt,1);
						Engine.getEngine().getRender().resetColor();
					}else {
						Engine.getEngine().getRender().renderRect(a.pos.x, a.pos.y, a.size, a.size, col);
					}
				}else if(!persistant) {
					l.remove(i);
				}
			}
		}
		public void addParticle() {
			if(l.size()<max) {
				time=0;
				Particle p=new Particle();
				Vector2f sl=spawnfunc.apply(p);
				if(sl!=null)
					l.add(p.setPos(sl));
			}
		}
		public void addParticle(Vector2f pos) {
			if(l.size()<max) {
				time=0;
				Particle p=new Particle();
				Vector2f sl=spawnfunc.apply(p);
				if(sl!=null&&pos!=null)
					l.add(p.setPos(pos));
			}
			
		}
		public ParticleSystem disableNaturalSpawning() {
			spawnrate=-1;
			return this;
		}
		public ParticleSystem addSpawningLogic(Function<Particle,Vector2f> f) {
			spawnfunc=f;
			return this;
		}
		public ParticleSystem setMaxLifetime(int lifetime) {
			this.lifetime=lifetime;
			return this;
		}
		public ParticleSystem setSpawndelay(int spawndelay) {
			this.spawnrate=spawndelay;
			return this;
		}
		public ParticleSystem setMax(int max) {
			this.max=max;
			return this;
		}
		public ParticleSystem setColor(int col) {
			this.col=col;
			return this;
		}
		public ParticleSystem setTexture(long txt) {
			this.txt=txt;
			return this;
		}
		public ParticleSystem disableTexture() {
			this.txt=-1;
			return this;
		}
		public ParticleSystem nonpersistant() {
			persistant=false;
			return this;
		}
		public ParticleSystem persistant() {
			persistant=true;
			return this;
		}
	}
	
	public class Particle {
		int lifetime;
		float size;
		Vector2f motion,pos;
		Particle(){
			motion=new Vector2f();
			size=5f;
		}
		void update(Consumer<Particle> func) {
			func.accept(this);
			pos.add(motion);
			lifetime++;
		}
		public void setSize(float size) {
			this.size=size;
		}
		Particle setPos(Vector2f v) {
			pos=v;
			return this;
		}
		public Vector2f getMotion() {
			return motion;
		}
	}

}
