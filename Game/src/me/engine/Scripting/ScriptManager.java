package me.engine.Scripting;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Consumer;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import me.engine.Main;
import me.engine.Utils.FileUtils;

public class ScriptManager {
	private HashMap<String, String> scripts=new HashMap<>();
	ScriptEngineManager sem;
	
	public ScriptManager() {
		sem= new ScriptEngineManager();
		registerScripts();
	}
	
	private void registerScripts() {
		registerScriptsinPath(new File(Main.dir+"\\Assets\\Scripts"));
	}
	
	public void registerScriptsinPath(File path) {
		for(File f:path.listFiles()) {
			if(f.isDirectory()) {
				registerScriptsinPath(f);
				continue;
			}
			registerScript(f);
		}
	}
	
	private void registerScript(File f) {
		scripts.put(FileUtils.getIDfromFile(f), FileUtils.stringfromFile(f));
	}
	
	public ScriptEngine getScript(String id,Consumer<ScriptEngine> bindings) {
		ScriptEngine sce=sem.getEngineByExtension(id.split(":")[1]);
		if(!scripts.containsKey(id)) {
			Main.log.warning(()->"Couldnt find Script: "+id);
			return sce;
		}
		if(bindings!=null)
			bindings.accept(sce);
		try {
			sce.eval(scripts.get(id));
		} catch (ScriptException e) {
			Main.log.warning("Setup of Script "+id+" Failed \n"+e.toString());
		}
		return sce;
	}
	
	
	public static Object invoke(ScriptEngine se,String func,Object... args) {
		try {
			return ((Invocable) se).invokeFunction(func,args);
		} catch (NoSuchMethodException e) {
			Main.log.warning(()->"Method "+func+" not fund");
		} catch (ScriptException e) {
			Main.log.warning(()->"Error invoking function "+func+" with args "+Arrays.toString(args)+" Error: "+e.toString());
		}
		return null;
	}

	public ScriptEngine compileScript(String code, String type,Consumer<ScriptEngine> bindings) throws ScriptException {
		ScriptEngine sce=sem.getEngineByName(type);
		if(bindings!=null)
			bindings.accept(sce);
		sce.eval(scripts.get(code));
		return sce;
	}
}
