package me.engine.Utils.Event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;

import me.engine.Main;

/**
 * @author Christian
 * Manages Events
 */
public class EventManager {
	/**
	 * List of Methods currently registered to the EventManager
	 */
	static ArrayList<MethodType> clist=new ArrayList<>();
	
	/**
	 * Registers an Class to use Events
	 * @param o The Class that should be registered
	 */
	public static void register(Object o) {
		for(Method m: o.getClass().getMethods()) {
			if(m.isAnnotationPresent(EventTarget.class)&&m.getParameterTypes().length==1) {
				m.setAccessible(true);
				clist.add(new MethodType(m, o,m.getAnnotationsByType(EventTarget.class)[0].p().getpri()));
			}
		}
		Collections.sort(clist, (a,b)->{return a.p()-b.p();});
	}
	
	/**
	 * Calls an Event
	 * @param e The Event that should be called
	 */
	public static void call(Event e) {
		for(int i = (clist.size() - 1); i >= 0; i--) {
			MethodType m=clist.get(i);
			if(m.m.getParameterTypes()[0].getSimpleName().equals("Event")||(m.m.getParameterTypes()[0].equals(e.getClass()))) {
				try {
					m.m.invoke(m.c, e);
				} catch (IllegalAccessException|IllegalArgumentException|InvocationTargetException e1) {
					Main.log.severe(e1.getMessage()+" "+e.getClass().getSimpleName()+" at "+m.c.getClass().getSimpleName());
				}
			}
		}
		
	}

	/**
	 * Unregisters an Event
	 * @param o The Event which should be unregistered
	 */
	public static void unregister(Object o) {
		for(int i = (clist.size() - 1); i >= 0; i--) {
			if(clist.get(i).c.equals(o)) {
				clist.remove(i);
			}
		}
	}
	
	
	/**
	 * @author Christian
	 * Saves the Data to invoke the registered Methods/Classes
	 */
	public static class MethodType{
		public Method m;
		public Object c;
		public byte priority;
		public MethodType(Method _m,Object _c,byte p) {
			m=_m;
			c=_c;
			priority=p;
		}
		public byte p() {
			return priority;
		}
	}
	
}
