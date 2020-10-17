package me.engine.Utils.Event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;

import me.engine.Main;

public class EventManager {
	static ArrayList<MethodType> clist=new ArrayList<>();
	
	public static void register(Object o) {
		for(Method m: o.getClass().getMethods()) {
			if(m.isAnnotationPresent(EventTarget.class)&&m.getParameterTypes().length==1) {
				m.setAccessible(true);
				clist.add(new MethodType(m, o,m.getAnnotationsByType(EventTarget.class)[0].p().getpri()));
			}
		}
		Collections.sort(clist, (a,b)->{return a.p()-b.p();});
	}
	
	public static void call(Event e) {
		for(int i = (clist.size() - 1); i >= 0; i--) {
			MethodType m=clist.get(i);
			if(m.m.getParameterTypes()[0].getSimpleName().equals("Event")||(m.m.getParameterTypes()[0].equals(e.getClass()))) {
				try {
					m.m.invoke(m.c, e);
				} catch (IllegalAccessException|IllegalArgumentException|InvocationTargetException e1) {
					Main.log.severe(e1.toString());
				}
			}
		}
		
	}

	public static void unregister(Object o) {
		for(int i = (clist.size() - 1); i >= 0; i--) {
			if(clist.get(i).c.equals(o)) {
				clist.remove(i);
			}
		}
	}
	
	
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
