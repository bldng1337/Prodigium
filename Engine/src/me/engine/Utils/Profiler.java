package me.engine.Utils;
import java.util.ArrayList;
import java.util.HashMap;

public class Profiler {
	
	private HashMap<String,ArrayList<Long>> timemap=new HashMap<>();
	public static final int BUFSIZ=5000;
	long maxtime=0;
	
	public void startTimer(String timername) {
		put(System.nanoTime(),timername);
	}
	
	public void stopTimer(String timername) {
		
		long time=System.nanoTime()-getlast(timername);
		if(maxtime<time)
			maxtime=time;
		replace(time, timername);
	}
	
	public void time(Timing f,String name) {
		long t=System.nanoTime();
		f.func();
		t=System.nanoTime()-t;
		if(maxtime<t)
			maxtime=t;
		put(t,name);
	}
	
	public <T> T time(Timingreturn<T> f,String name) {
		long t=System.nanoTime();
		T ret=f.func();
		t=System.nanoTime()-t;
		if(maxtime<t)
			maxtime=t;
		put(t,name);
		return ret;
	}
	
	private void replace(long d,String s) {
		ArrayList<Long> l=timemap.get(s);
		l.remove(l.size()-1);
		l.add(d);
		timemap.replace(s, l);
	}
	
	private void put(long d,String s) {
		if(timemap.containsKey(s)) {
			ArrayList<Long> l=timemap.get(s);
			if(l.size()>BUFSIZ)
				l.remove(0);
			l.add(d);
			timemap.replace(s, l);
		} else {
			ArrayList<Long> l=new ArrayList<>();
			l.add(d);
			timemap.put(s, l);
		}
	}
	
	private long getlast(String s) {
		ArrayList<Long> l=timemap.get(s);
		return l.get(l.size()-1);
	}
	
	public interface Timing {
		public void func();
	}
	
	public interface Timingreturn<T> {
		public T func();
	}
	public HashMap<String,ArrayList<Long>> gettimemap(){
		return timemap;
	}
	
}
