package me.engine.Utils;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;


import org.joml.Vector2f;

import me.engine.Engine;

public class FontRenderer {
	class Char{
		Char(long rid,float _width,float _height){
			renderid=rid;
			width=_width;
			height=_height;
		}
		long renderid;
		float width,height;
	}
	
	public enum Format {
		RIGHT, LEFT, CENTERD, CENTERDDOWN, CENTERDUP, LEFTCENTER, LEFTUP, RIGHTCENTER, RIGHTUP
	}
	
	HashMap<Character,Char> fontmap=new HashMap<>();
	
	
	public FontRenderer() {
		File f=FileUtils.getFilefromID("Textures.Quintet:ttf");
		String abc="QWERTZUIOPÜASDFGHJKLYXCVBNMqwertzuiopasdfghklyxcvbnm1234567890? /\\.,";
		Font font=null;
		try {
			font=Font.createFont(Font.PLAIN, f).deriveFont(100f);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
		float maxwidth=1;
		float maxheight=1;
		for(char c:abc.toCharArray()) {
			Vector2f csize=registerchar(c,font);
			if(maxwidth<csize.x)
				maxwidth=csize.x;
			if(maxheight<csize.y)
				maxheight=csize.y;
		}
		for(char c:abc.toCharArray()) {
			Char c1=fontmap.get(c);
			c1.width/=maxwidth;
			c1.height/=maxheight;
		}
	}
	
	private Vector2f registerchar(char c,Font f) {
		BufferedImage bfi=new BufferedImage(1000, 1000, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics g=bfi.getGraphics();
		g.setFont(f);
		g.drawString(""+c, 1, g.getFontMetrics().getMaxAscent() + g.getFontMetrics().getMaxDescent() + g.getFontMetrics().getLeading());
		bfi=bfi.getSubimage(0, 0, g.getFontMetrics().charWidth(c)+2, g.getFontMetrics().getMaxAscent() + g.getFontMetrics().getMaxDescent() + g.getFontMetrics().getLeading()+26);
		fontmap.put(c, new Char(Engine.getEngine().getTex().registerTexture(bfi, "Texture.Quintet."+c),g.getFontMetrics().charWidth(c)+2, g.getFontMetrics().getMaxAscent() + g.getFontMetrics().getMaxDescent() + g.getFontMetrics().getLeading()+26));
		return new Vector2f(g.getFontMetrics().charWidth(c)+2, g.getFontMetrics().getMaxAscent() + g.getFontMetrics().getMaxDescent() + g.getFontMetrics().getLeading()+26);
	}
	
	public float getWidth(String s,float size) {
		float d=0;
		for(char c:s.toCharArray())
			d+=getWidth(c, size);
		return d;
	}
	
	public float getWidth(char c,float size) {
		return getWidth(fontmap.get(c), size);
	}
	
	private float getWidth(Char c,float size) {
		return c.width*size;
	}
	
	public float getHeight(char c,float size) {
		return getHeight(fontmap.get(c), size);
	}
	
	public float getHeight(String s,float size) {
		return s.split("\\\\n").length*getHeight('A', size);
	}
	
	private float getHeight(Char c,float size) {
		return c.height*size;
	}
	
	public void draw(String s,float x,float y,float size) {
		float dx=0;
		float dy=0;
		char[] ca=s.toCharArray();
		for(int i=0;i<ca.length;i++) {
			char c=ca[i];
			if(c=='\\'){
				switch(ca[i+1]) {
					//Color
					case '0':
						i++;
					case '#':
					case 'c':
						Engine.getEngine().getUIrender().setColor(Integer.parseUnsignedInt("FF"+new String(Arrays.copyOfRange(ca, i+2, i+8)), 16));
						i+=7;
						continue;
					//Newline
					case 'n':
						dy+=getHeight(c, size);	
						dx=0;
						i+=1;
						continue;
					case '\\':
						break;
					default:
						Engine.log.warning("Unknown String Identifierer "+ca[i+1]);
						break;
				}
			}
			Char cs=null;
			if(fontmap.containsKey(c)) {
				cs=fontmap.get(c);
			}else {
				cs=fontmap.get('?');
			}
			Engine.getEngine().getUIrender().renderRect(x+dx, y+dy, getWidth(cs, size), getHeight(cs, size), cs.renderid,0);
			dx+=getWidth(cs, size)*1.001f;
		}
		Engine.getEngine().getUIrender().resetColor();
	}
}
