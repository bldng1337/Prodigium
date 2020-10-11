package me.engine.Utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL45;

import me.engine.Main;

public class Texture {
	private final static String TPPATH="";
	private int[] atlases=new int[7];
	private int atlas=0;
	HashMap<String, Long> texturemap=new HashMap<>();
	int cx=0;
	int cy=0;
	int maxheight=0;
	
	public Texture() {
		int maxSize = Math.min(GL45.glGetInteger(GL45.GL_MAX_TEXTURE_SIZE), 4096);
		int maxUnits= GL45.glGetInteger(GL45.GL_MAX_TEXTURE_IMAGE_UNITS);
		
		
	}
	
	public long registerTexture(String id) throws IOException {
		GlStateManager.Enable(GL45.GL_TEXTURE_2D);
		if(texturemap.containsKey(id))
			return texturemap.get(id);
		String p=id.replaceAll("\\.", "\\");
		p=p.replaceAll(":", "\\");
		File f=new File(TPPATH+p);
		
		if(f.getName().endsWith(".gif")) {
			ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
		    ImageInputStream ciis = ImageIO.createImageInputStream(f);
		    reader.setInput(ciis, false);
		    int noi = reader.getNumImages(true);
		    for(int i=0;i<noi;i++) {
		    	BufferedImage bfi=reader.read(i);
		    	
		    }
		    return gentexid(1, 1, 1, 1, 1, noi, id);
		}else {
			BufferedImage bfi=ImageIO.read(f);
			
			
			return gentexid(1, 1, 1, 1, 1, 1, id);
		}
		//TODO: Add to an sprite sheet and give an id back
	}
	
	
	private void createtexture() {
		atlases[atlas]=GL45.glGenTextures();
		GlStateManager.bindTexture2D(atlases[atlas]);
		GL45.glPixelStorei(GL45.GL_UNPACK_ALIGNMENT, 1);
		
		//Setup filtering, i.e. how OpenGL will interpolate the pixels when scaling up or down
		GL45.glTexParameteri(GL45.GL_TEXTURE_2D, GL45.GL_TEXTURE_MIN_FILTER, GL45.GL_NEAREST);
		GL45.glTexParameteri(GL45.GL_TEXTURE_2D, GL45.GL_TEXTURE_MAG_FILTER, GL45.GL_NEAREST);

		//Setup wrap mode, i.e. how OpenGL will handle pixels outside of the expected range
		GL45.glTexParameteri(GL45.GL_TEXTURE_2D, GL45.GL_TEXTURE_WRAP_S, GL45.GL_REPEAT);
		GL45.glTexParameteri(GL45.GL_TEXTURE_2D, GL45.GL_TEXTURE_WRAP_T, GL45.GL_REPEAT);
		
		
		atlas++;
	}
	
	/*
	 	Unpacking
		x: ((id>>(13*1+2))&0xFFF) max 4095
		y: ((id>>(13*2+2))&0xFFF) max 4095
		dx: ((id>>(13*3+2))&0xFFF) max 4095
		dy: ((id>>(13*4+2))&0xFFF) max 4095
		Atlas: (id&7) 7
		aniframes: (id>>(13*5+2)&0x3F) 63
	 */
	
	boolean isanimated(long id) {
		return getaniframes(id)>1;
	}
	
	int getx(long id) {
		return (int) ((id>>(13*1+2))&0xFFF);
	}
	
	int gety(long id) {
		return (int) ((id>>(13*2+2))&0xFFF);
	}
	
	int getdx(long id) {
		return (int) ((id>>(13*3+2))&0xFFF);
	}
	
	int getdy(long id) {
		return (int) ((id>>(13*4+2))&0xFFF);
	}
	
	int getatlas(long id) {
		return (int) (id&7);
	}
	
	int getaniframes(long id) {
		return (int) ((id>>(13*5+2))&0xFFF);
	}
	
	long gentexid(int x,int y,int dx,int dy,int atlas,int animframe, String id) {
		if(atlas>7||x>4095||y>4095||dx>4095||dy>4095||animframe>63) {
			Main.log.severe("Error creating Texture ["+id+"] size out of bounds!");
			System.exit(1);
		}
		return ((long)atlas|((long)x<<(13*1+2))|((long)y<<(13*2+2))|((long)dx<<(13*3+2))|((long)dy<<(13*4+2))|((long)animframe<<(13*5+2)));
	} 

}
