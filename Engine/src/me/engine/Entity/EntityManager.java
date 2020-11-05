package me.engine.Entity;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import me.engine.Engine;
import me.engine.Scripting.ScriptManager;
import me.engine.Utils.FileUtils;

public class EntityManager {
	private HashMap<String, EntityData> entitylist=new HashMap<>();
	ScriptManager sm;
	
	public EntityManager(ScriptManager s) {
		sm=s;
		registerEntitys();
	}
	
	private void registerEntitys() {
		registerinPath(new File(Engine.dir+"\\Assets\\Entities"));
	}
	
	public void registerinPath(File path) {
		for(File f:path.listFiles()) {
			if(f.isDirectory()) {
				registerinPath(f);
				continue;
			}
			registerEntity(f);
		}
	}
	
	private void registerEntity(File f) {
		EntityData ndata=new EntityData();
		ndata.textureids=new long[Animation.values().length];
		try(JsonReader jr=new JsonReader(new FileReader(f))){
			jr.beginObject();
			while(jr.hasNext()) {
				String s=jr.nextName();
				switch(s) {
				case "Name":
					ndata.name=jr.nextString();
					break;
				case "Animation":
					if(jr.peek()==JsonToken.NULL)
						continue;
					jr.beginObject();
					while(jr.hasNext())
						ndata.textureids[Animation.valueOf(jr.nextName()).gettextureindex()]=Engine.getEngine().getTex().getTexture(jr.nextString());
					jr.endObject();
					break;
				case "Health":
					ndata.health=(float) jr.nextDouble();
					break;
				case "Width":
					ndata.width=(float) jr.nextDouble();
					break;
				case "Height":
					ndata.height=(float) jr.nextDouble();
					break;
				case "Speed":
					ndata.speed=(float) jr.nextDouble();
					break;
				case "Script":
					ndata.scriptID=jr.nextString();
					break;
				case "FrameDelay":
					ndata.framedelay=jr.nextInt();
					break;
				default:
					Engine.log.warning(()->"Error reading JSON "+f.getName()+" Unknown keyword: "+s);
					break;
				}
			}
			entitylist.put(FileUtils.getIDfromFile(f), ndata);
		} catch (IOException e) {
			Engine.log.severe(()->"Error loading Entity "+f.getName()+" "+e.toString());
		}
	}
	
	public Entity newEntity(String id) {
		Entity e=new Entity();
		EntityData ndata=entitylist.get(id);
		if(ndata==null) {
			Engine.log.warning(()->"Couldnt find Entity "+id);
			return null;
		}
		for(Field n:ndata.getClass().getDeclaredFields()) {
			if(n.getName().equals("scriptID")||n.getName().equals("this$0"))
				continue;
			try {
				Field efield=e.getClass().getDeclaredField(n.getName());
				efield.setAccessible(true);
				efield.set(e, n.get(ndata));
			} catch (IllegalArgumentException|IllegalAccessException|NoSuchFieldException|SecurityException e1) {
				Engine.log.warning(()->"Error creating Entity "+e1.toString());
			}
		}
		e.speed*=1+(Math.random()-0.5f)/10f;
		e.currTexture=Animation.IDLE;
		e.script=sm.getScript(ndata.scriptID, sc->{sc.put("Entity", e);sc.put("e", e);});
		return e;
	}
	
	public Entity newEntity(String id,int x,int y) {
		Entity ret=newEntity(id);
		ret.x=x;
		ret.y=y;
		return ret;
	}

}
