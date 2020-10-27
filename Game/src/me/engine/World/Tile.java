package me.engine.World;

import org.joml.Rectanglef;
import org.joml.Vector2f;

import me.engine.Main;

/**
 * @author Christian
 *
 */
public class Tile {
	public static final int SIZE=150;
	boolean collideable;
	long texid;
	public Tile(String tex) {
		texid=Main.getTex().getTexture(tex);
	}
	
	
	public Vector2f solve(Rectanglef tile,Rectanglef t,Vector2f motion) {
		Rectanglef r=new Rectanglef();
		t.translate(motion,r);
		r.intersection(tile);
		motion.add(r.maxX-r.minX, r.maxY-r.minY);
		return motion;
	}
	
}
