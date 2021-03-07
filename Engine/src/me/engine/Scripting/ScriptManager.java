package me.engine.Scripting;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Consumer;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import me.engine.Engine;
import me.engine.Utils.FileUtils;

public class ScriptManager {
	/**
	 * List of ScriptSources
	 */
	private HashMap<String, String> scripts=new HashMap<>();
	
	/**
	 * Used to compile Scripts using
	 * {@link ScriptEngineManager}
	 */
	ScriptEngineManager sem;
	
	public ScriptManager() {
		sem=new ScriptEngineManager();
		//sem.registerEngineExtension("js", new NashornScriptEngineFactory());
		registerScripts();
	}
	
	private void registerScripts() {
		registerScriptsinPath(new File(Engine.dir+"\\Assets\\Scripts"));
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
		scripts.put(FileUtils.getIDfromFile(f), preprocess(FileUtils.stringfromFile(f)));
	}
	
	public String preprocess(String code) {
		int pos=-1;
		while((pos=code.indexOf("#"))!=-1) {
			int lineend=code.indexOf("\n", pos);
			if(lineend==-1)
				lineend=code.length();
			switch(code.charAt(pos+1)) {
				case 'i':
					code=code.substring(0, pos)+preprocess(FileUtils.stringfromFile(code.substring(pos+3,lineend)))+code.substring(lineend, code.length());
				break;
				default:
					code=code.substring(0, pos)+code.substring(lineend, code.length());
			}
		}
		return code;
	}
	
	public ScriptEngine getScript(String id,Consumer<ScriptEngine> bindings) {
		ScriptEngine sce=sem.getEngineByExtension(id.split(":")[1]);
		if(!scripts.containsKey(id)) {
			Engine.log.warning(()->"Couldnt find Script: "+id);
			return sce;
		}
		if(bindings!=null)
			bindings.accept(sce);
		try {
			sce.eval(scripts.get(id));
		} catch (ScriptException e) {
			Engine.log.warning("Setup of Script "+id+" Failed \n"+e.toString());
		}
		return sce;
	}
	
	
	public static Object invoke(ScriptEngine se,String func,Object... args) {
		
		try {
			return ((Invocable) se).invokeFunction(func,args);
		} catch (NoSuchMethodException e) {
			Engine.log.warning(()->"Method "+func+" not fund");
		} catch (ScriptException e) {
			Engine.log.warning(()->"Error invoking function "+func+" with args "+Arrays.toString(args)+" Error: "+e.toString());
		}
		return null;
	}
	public static Object append(ScriptEngine se,String code) {
		try {
			return se.eval(code);
		} catch (ScriptException e) {
			Engine.log.warning(()->"Thrown Exception "+e.toString());
		}
		return null;
	}

	public ScriptEngine compileScript(String code, String type,Consumer<ScriptEngine> bindings) throws ScriptException {
		ScriptEngine sce=sem.getEngineByName(type);
		if(bindings!=null)
			bindings.accept(sce);
		sce.eval(code);
		return sce;
	}

	public void compileScript(ScriptEngine sce, String id, Consumer<ScriptEngine> bindings) {
		if(!scripts.containsKey(id))
			Engine.log.warning(()->"Couldnt find Script: "+id);
		if(bindings!=null)
			bindings.accept(sce);
		try {
			sce.eval(scripts.get(id));
		} catch (ScriptException e) {
			Engine.log.warning("Setup of Script "+id+" Failed \n"+e.toString());
		}
	}

	public ScriptEngine newScript(String id) {
		return sem.getEngineByExtension(id.split(":")[1]);
	}
}
