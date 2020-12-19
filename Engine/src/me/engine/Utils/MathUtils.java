package me.engine.Utils;

public class MathUtils {
	private MathUtils() {}

	public static int torgba(int r,int g,int b,int a) {
		return (a << 24)
				| (b << 16)
				| (g << 8)
				| (r << 0);
	}
	
	public static int torgba(float r,float g,float b,float a) {
		return torgba((int)(r*255), (int)(g*255), (int)(b*255), (int)(a*255));
	}
	
	
}
