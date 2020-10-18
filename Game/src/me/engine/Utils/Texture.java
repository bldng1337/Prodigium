package me.engine.Utils;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL45;

import me.engine.Main;

public class Texture {
	private static final String TPPATH=Main.dir.getAbsolutePath()+"\\Assets\\Textures\\";
	private int[] atlases;
	private int atlas=0;
	HashMap<String, Long> texturemap=new HashMap<>();
	int msize;
	BufferedImage catlas;
	int cx=0;
	int cy=0;
	int maxheight=0;
	
	public Texture() {
		int maxSize = Math.min(GL45.glGetInteger(GL45.GL_MAX_TEXTURE_SIZE), 4096);
		int maxUnits= Math.min(7,GL45.glGetInteger(GL45.GL_MAX_TEXTURE_IMAGE_UNITS));
		atlases=new int[maxUnits];
		catlas=new BufferedImage(maxSize, maxSize, BufferedImage.TYPE_4BYTE_ABGR);
		msize=maxSize;
	}
	
	public long registerTexturesafe(String id) {
		try {
			return registerTexture(id);
		}catch(IOException e) {
			GlStateManager.disable(GL45.GL_TEXTURE_2D);
			Main.log.warning(e.getMessage());
		}
		return 0;
	}
	
	public long registerTexture(String id) throws IOException {
		GlStateManager.enable(GL45.GL_TEXTURE_2D);
		if(texturemap.containsKey(id))
			return texturemap.get(id);
		String p=id.replace(".", "\\")
				   .replace(":", ".");
		p=p.replaceAll(":", "\\");
		File f=new File(TPPATH+p);
		if(!f.exists())
			throw new RuntimeException("Texture missing "+f.getAbsolutePath());
		Graphics g=catlas.getGraphics();
		if(f.getName().endsWith(".gif")) {
			ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
		    ImageInputStream ciis = ImageIO.createImageInputStream(f);
		    reader.setInput(ciis, false);
		    int noi = reader.getNumImages(true);
		    BufferedImage bfi = reader.read(0);
		    if(cx+bfi.getWidth()*noi>msize) {
		    	cy+=maxheight;
		    	maxheight=0;
		    	cx=0;
		    }
		    if(cy+bfi.getHeight()>msize) {
		    	flush();
		    	return registerTexture(id);
		    }
		    for(int i=0;i<noi;i++) {
		    	bfi=reader.read(i);
		    	g.drawImage(bfi, cx+bfi.getWidth()*i, cy, null);
		    }
		    int x=cx;
		    cx+=bfi.getWidth()*noi;
		    if(maxheight<bfi.getHeight())
		    	maxheight=bfi.getHeight();
		    return gentexid(x, cy, bfi.getWidth(), bfi.getHeight(), atlas, noi, id);
		}else {
			BufferedImage bfi=ImageIO.read(f);
			if(cx+bfi.getWidth()>msize) {
		    	cy+=maxheight;
		    	maxheight=0;
		    	cx=0;
		    }
		    if(cy+bfi.getHeight()>msize) {
		    	flush();
		    	return registerTexture(id);
		    }
			g.drawImage(bfi, cx, cy, null);
			int x=cx;
			cx+=bfi.getWidth();
			if(maxheight<bfi.getHeight())
		    	maxheight=bfi.getHeight();
			return gentexid(x, cy, bfi.getWidth(), bfi.getHeight(), atlas, 1, id);
		}
	}
	
	public void bind() {
		for(int i=0;i<atlas;i++) {
			GL45.glBindTextureUnit(i, atlases[i]);
		}
	}
	
	
	public void flush() {
		
		int[] pixels = new int[msize * msize];
		catlas.getRGB(0, 0, msize, msize, pixels, 0, msize);
		
		IntBuffer ib=BufferUtils.createIntBuffer(msize * msize);
		for (int i = 0; i < msize * msize; i++) {
			int a = (pixels[i] & 0xff000000) >> 24;
			int r = (pixels[i] & 0xff0000) >> 16;
			int g = (pixels[i] & 0xff00) >> 8;
			int b = (pixels[i] & 0xff);
			ib.put(a << 24 | b << 16 | g << 8 | r);
		}
		ib.flip();
		
		cy+=maxheight;
		atlases[atlas]=GL45.glGenTextures();
		GL45.glBindTexture(GL45.GL_TEXTURE_2D, atlases[atlas]);
		GL45.glTexParameteri(GL45.GL_TEXTURE_2D, GL45.GL_TEXTURE_MIN_FILTER, GL45.GL_NEAREST);
		GL45.glTexParameteri(GL45.GL_TEXTURE_2D, GL45.GL_TEXTURE_MAG_FILTER, GL45.GL_NEAREST);
		GL45.glTexImage2D(GL45.GL_TEXTURE_2D, 0, GL45.GL_RGBA, msize, msize, 0, GL45.GL_RGBA, GL45.GL_UNSIGNED_BYTE, ib);
		GL45.glBindTexture(GL45.GL_TEXTURE_2D, 0);
		GlStateManager.unbindTexture2D();
		atlas++;
		cx=0;
		cy=0;
		maxheight=0;
		catlas=new BufferedImage(msize, msize, BufferedImage.TYPE_4BYTE_ABGR);
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
	
	public static boolean isanimated(long id) {
		return getaniframes(id)>1;
	}
	
	public static int getx(long id) {
		return (int) ((id>>(13*1+2))&0xFFF);
	}
	
	public static int gety(long id) {
		return (int) ((id>>(13*2+2))&0xFFF);
	}
	
	public static int getdx(long id) {
		return (int) ((id>>(13*3+2))&0xFFF);
	}
	
	public static int getdy(long id) {
		return (int) ((id>>(13*4+2))&0xFFF);
	}
	
	public static int getatlas(long id) {
		return (int) (id&7);
	}
	
	public static int getaniframes(long id) {
		return (int) ((id>>(13*5+2))&0xFFF);
	}
	
	public int getMsize() {
		return msize;
	}
	
	long gentexid(int x,int y,int dx,int dy,int atlas,int animframe, String id) {
		if(atlas>7||x>4095||y>4095||dx>4095||dy>4095||animframe>63) {
			Main.log.severe(()->"Error creating Texture ["+id+"] size out of bounds!");
			System.exit(1);
		}
		return ((long)atlas|((long)x<<(13*1+2))|((long)y<<(13*2+2))|((long)dx<<(13*3+2))|((long)dy<<(13*4+2))|((long)animframe<<(13*5+2)));
	} 

}
