package me.engine.Utils;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;


import me.engine.Engine;

public class FontRenderer {
	
	HashMap<Character,Long> fontmap=new HashMap<>();
	int width,height;
	
	public FontRenderer() {
		File f=FileUtils.getFilefromID("Textures.Quintet:ttf");
		String abc="QWERTZUIOPÜASDFGHJKLÖÄYXCVBNMqwertzuiopüasdfghjklöäyxcvbnm1234567890öäüÖÄÜ";
		Font font=null;
		try {
			font=Font.createFont(Font.PLAIN, new FileInputStream(f));
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
		for(int i=48;i<124;i++)
			System.out.println((char)i);
		System.out.println((int)'ä');
		System.out.println((int)'ö');
		System.out.println((int)'ü');
		System.out.println((int)'Ä');
		System.out.println((int)'Ö');
		System.out.println((int)'Ü');
		
		for(char c:abc.toCharArray()) {
			BufferedImage bfi=new BufferedImage(200, 200, BufferedImage.TYPE_4BYTE_ABGR);
			Graphics g=bfi.getGraphics();
			g.setFont(font);
			g.drawString(""+c, 0, g.getFontMetrics().getHeight());
			bfi=bfi.getSubimage(0, 0, 50, 50);
			Engine.getEngine().getTex().registerTexture(bfi, "Texture.Quintet."+c);
		}
	}
	
	
	
	public void draw(String s,float x,float y,float size) {
		float dx=0;
		for(char c:s.toCharArray()) {
			if(fontmap.containsKey(c)) {
				Engine.getEngine().getUIrender().renderRect(x+dx, y, size, size, fontmap.get(c),0);
			}else {
				Engine.getEngine().getUIrender().renderRect(x+dx, y, size, size, fontmap.get('?'),0);
			}
			dx+=size;
		}
	}
}
