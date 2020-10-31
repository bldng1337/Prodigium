package me.engine.Entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

import org.joml.Vector2i;

import me.engine.Engine;

public class Pathfinder {
	
	public static Vector2i[] AStar(Vector2i begin,Vector2i end) {
		PriorityQueue<Searchtile> open = new PriorityQueue<>((o1,o2)->o1.compare(o2));
		ArrayList<Searchtile> closed=new ArrayList<>();
		open.add(new Searchtile(begin,dist(begin,end),0));
		Searchtile current;
		while(true) {
			current=open.remove();
			closed.add(current);
			if(current.pos.equals(end.x, end.y))
				break;
			for(int x=-1;x<=1;x++) {
				for(int y=-1;y<=1;y++) {
					if(current.pos.x+x<0||current.pos.y+y<0||
					   search(closed, current.pos.x+x, current.pos.y+y)!=null||
					   (x==0&&y==0)||
					   Engine.getEngine().getCurrlevel().getTile(current.pos.x+x, current.pos.y+y).isCollideable())
						continue;
					
					if(x!=0&&y!=0) {
						if(Engine.getEngine().getCurrlevel().getTile(current.pos.x, current.pos.y+y).isCollideable()&&
							Engine.getEngine().getCurrlevel().getTile(current.pos.x+x, current.pos.y).isCollideable())
							continue;
					}
					
					
					
					Searchtile neighbour=search(open, current.pos.x+x, current.pos.y+y);
					int path=current.gcost+((x!=0&&y!=0)?14:10);
					if(neighbour==null) {
						Vector2i npos=new Vector2i(current.pos.x+x, current.pos.y+y);
						neighbour=new Searchtile(npos, dist(begin, npos), path);
						neighbour.parent=current;
						open.add(neighbour);
					}else {
						if(path<neighbour.gcost) {
							neighbour.gcost=path;
							neighbour.parent=current;
						}
					}
				}
			}
		}
		ArrayList<Vector2i> ret=new ArrayList<>();
		while(current!=null) {
			ret.add(current.pos);
			current=current.parent;
		}
		Collections.reverse(ret);
		return ret.toArray(new Vector2i[0]);
	}
	
	static Searchtile search(PriorityQueue<Searchtile> s,int x,int y) {
		for(Searchtile st:s)
			if(st.pos.equals(x, y))
				return st;
		return null;
	}
	
	static Searchtile search(List<Searchtile> s,int x,int y) {
		for(Searchtile st:s)
			if(st.pos.equals(x, y))
				return st;
		return null;
	}
	
	static class Searchtile{
		Searchtile parent;
		int hcost,//EndDist
		gcost;//StartDist
		Vector2i pos;
		
		public Searchtile(Vector2i pos,int hcost,int gcost) {
			this.pos=new Vector2i(pos);
			this.hcost=hcost;
			this.gcost=gcost;
		}
		
		public Searchtile(int x,int y,int hcost,int gcost) {
			this.pos=new Vector2i(x,y);
			this.hcost=hcost;
			this.gcost=gcost;
		}
		
		
		public int fcost() {
			return gcost+hcost;
		}
		
		public int compare(Searchtile other) {
			int val=fcost()-other.fcost();
			if(val==0)
				val=hcost-other.hcost;
			return val;
		}
	}
	
	static int dist(Vector2i a,Vector2i b) {
		int distX=(int) Math.abs(a.x-b.x);
		int distY=(int) Math.abs(a.y-b.y);
		if(distX>distY)
			return 14*distY+10*(distX-distY);
		return 14*distX+10*(distY-distX);
	}

}
