package UI;

import me.engine.Engine;
import me.engine.Entity.Entity;
import me.engine.Gui.InGame.UIElement;
import me.engine.Utils.Renderer;
import me.engine.World.Tiles.STile;

public class Minimap extends UIElement{

	public Minimap(int x, int y, int size) {
		super(x, y, size, size);
	}

	@Override
	public void render(Renderer r) {
		float rendersize=this.width;
		int mapsize=60;
		mapsize=Math.min(mapsize, (int)rendersize);
		float res=rendersize/mapsize;
		Entity e=Engine.getEngine().getCurrlevel().getPlayer();
		r.renderRect(0, 0, rendersize, rendersize, 0xFF000000);
		int sx=(int) (e.x/STile.SIZE-mapsize/2f);
		int sy=(int) (e.y/STile.SIZE-mapsize/2f);
		r.setTexCoords(0.5f, 0.5f, 0.5f, 0.5f);
		for(int x=sx;x<e.x/STile.SIZE+mapsize/2f;x++) {
			for(int y=sy;y<e.y/STile.SIZE+mapsize/2f;y++) {
				if(x<0||y<0)
					continue;
				long txt=Engine.getEngine().getCurrlevel().getTile(x, y).getPrimaryTex();
				r.renderRect(1+(x-sx)*res, 1+(y-sy)*res, res, res, txt,0);
			}
		}
		r.resetTexCoords();
		r.renderRect(1+mapsize/2f*res-res/2f, 1+mapsize/2f*res-res/2, res, res, 0xFF00FF00);
		for(Entity en:Engine.getEngine().getCurrlevel().getEntitys()) {
			float rx=e.x/STile.SIZE-en.x/STile.SIZE+mapsize/2f;
			float ry=e.y/STile.SIZE-en.y/STile.SIZE+mapsize/2f;
			rx*=res;
			ry*=res;
			if(rx<0||ry<0||rx>rendersize||ry>rendersize)
				r.renderRect(rx, ry, res, res, 0xFF0000FF);
		}
	}

}
