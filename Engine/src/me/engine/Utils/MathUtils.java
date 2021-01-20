package me.engine.Utils;

import java.util.function.Consumer;

import org.joml.Vector2f;
import org.joml.Vector4f;

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
	
	static int ipart(float x) {
        return (int) x;
    }
 
	static float fpart(float x) {
        return (float) (x - Math.floor(x));
    }
 
    static float rfpart(float x) {
        return (float) (1.0 - fpart(x));
    }
    
    /**
     * https://rosettacode.org/wiki/Xiaolin_Wu%27s_line_algorithm#Java
     * @param x0
     * @param y0
     * @param x1
     * @param y1
     */
    public static void drawLine(Consumer<Vector2f> d,float x0, float y0, float x1, float y1) {
    	final float t=0.45f;
    	final float dt=1-t;
    	if(fpart(x0)>t)
    		x0+=dt;
    	if(fpart(y0)>t)
    		y0+=dt;
    	if(fpart(x1)>t)
    		x1+=dt;
    	if(fpart(y1)>t)
    		y1+=dt;
    	x0=(float) Math.floor(x0);
    	y0=(float) Math.floor(y0);
    	x1=(float) Math.floor(x1);
    	y1=(float) Math.floor(y1);
        boolean steep = Math.abs(y1 - y0) > Math.abs(x1 - x0);
        if (steep)
        	 drawLine(d,y0, x0, y1, x1);
        if (x0 > x1)
        	drawLine(d,x1, y1, x0, y0);
 
        float dx = x1 - x0;
        float dy = y1 - y0;
        float gradient = dy / dx;
 
        // handle first endpoint
        float xend =  Math.round(x0);
        float yend = y0 + gradient * (xend - x0);
        float xpxl1 = xend; // this will be used in the main loop
        float ypxl1 = ipart(yend);
 
        if (steep) {
        	d.accept(new Vector2f(ypxl1, xpxl1));
        	d.accept(new Vector2f(ypxl1 + 1, xpxl1));
        } else {
        	d.accept(new Vector2f(xpxl1, ypxl1));
        	d.accept(new Vector2f(xpxl1, ypxl1 + 1));
        }
 
        // first y-intersection for the main loop
        float intery = yend + gradient;
 
        // handle second endpoint
        xend = Math.round(x1);
        yend = y1 + gradient * (xend - x1);
        float xpxl2 = xend; // this will be used in the main loop
        float ypxl2 = ipart(yend);
 
        if (steep) {
        	d.accept(new Vector2f(ypxl2, xpxl2));
        	d.accept(new Vector2f(ypxl2 + 1, xpxl2));
        } else {
        	d.accept(new Vector2f(xpxl2, ypxl2));
        	d.accept(new Vector2f(xpxl2, ypxl2 + 1));
        }
 
        // main loop
        for (float x = xpxl1 + 1; x <= xpxl2 - 1; x++) {
            if (steep) {
            	d.accept(new Vector2f(ipart(intery), x));
            	d.accept(new Vector2f(ipart(intery) + 1, x));
            } else {
            	d.accept(new Vector2f(x, ipart(intery)));
            	d.accept(new Vector2f(x, ipart(intery) + 1));
            }
            intery = intery + gradient;
        }
    }
    
    public static Vector2f rayrect(Vector4f AABB,Vector4f ray) {
    	float nx=(AABB.x-ray.x)/ray.z;
    	float ny=(AABB.y-ray.y)/ray.w;
    	float fx=(AABB.z-ray.x)/ray.z;
    	float fy=(AABB.w-ray.y)/ray.w;
    	if(nx>fx) {
    		float a=nx;
    		nx=fx;
    		fx=a;
    	}
    	if(ny>fy) {
    		float a=ny;
    		ny=fy;
    		fy=a;
    	}
    	float nhit=Math.max(nx, ny);
    	float fhit=Math.min(fx, fy);
    	if(nx>fy||ny>fx||fhit<0)
    		return null;
    	return new Vector2f(ray.x+ray.z*nhit,ray.y+ray.w*nhit);
    }
}
