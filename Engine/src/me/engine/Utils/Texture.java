package me.engine.Utils;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL45;

import me.engine.Engine;

/**
 * @author Christian
 * Abstracts Textures in a Class and manages an Texture Atlas
 */
public class Texture {
	/**
	 * Array of TextureAtlasIDs
	 */
	private int[] atlases;
	/**
	 * Which Atlas gets drawn while Registering
	 */
	private int atlas=0;
	/**
	 * Tracks the Texture IDs used for Rendering
	 */
	HashMap<String, Long> texturemap=new HashMap<>();
	/**
	 * The Size of the TextureAtlas
	 */
	int msize;
	/**
	 * The Texture Atlas which is currently getting constructed
	 */
	BufferedImage catlas;
	
	/**
	 * The Pos where The Texture should get drawn on the atlas
	 */
	int cx=0,cy=0;
	/**
	 * The Height of the Tallest Texture in the Row of the Atlas
	 */
	int maxheight=0;
	
	public Texture() {
		int maxSize = Math.min(GL45.glGetInteger(GL45.GL_MAX_TEXTURE_SIZE), 4096);
		int maxUnits= Math.min(7,GL45.glGetInteger(GL45.GL_MAX_TEXTURE_IMAGE_UNITS));
		atlases=new int[maxUnits];
		catlas=new BufferedImage(maxSize, maxSize, BufferedImage.TYPE_4BYTE_ABGR);
		msize=maxSize;
		registerTextures();
	}
	
	/**
	 * Register every Texture found in the Assets/Texture Folder for use in the Game
	 */
	public void registerTextures() {
		registerinPath(new File(Engine.dir.getAbsolutePath()+"\\Assets\\Textures\\"));
	}
	
	/**
	 * Registers every Texture in an Path
	 * @param path The Path to search for Textures
	 */
	public void registerinPath(File path) {
		for(File f:path.listFiles()) {
			if(f.isDirectory()) {
				registerinPath(f);
				continue;
			}
			try {
				registerTexture(f);
			} catch (IOException e) {
				Engine.log.warning(()->"Failed to load "+f.getAbsolutePath()+" "+e.getMessage());
			}
		}
	}
	
	/**
	 * Converts the String Texture ID to an Numeric one to be used in Rendering
	 * The String ID consists of the Path of the Texture in the Textures folder with the \ replaced with . and the . replaced with :
	 * e.g. Assets\Textures\Test\testpng.png becomes Assets.Textures.Test.testpng:png
	 * @param string The String ID
	 * @return The Numeric ID used in Rendering
	 */
	public long getTexture(String string) {
		if(texturemap.containsKey(string))
			return texturemap.get(string);
		Engine.log.warning(()->"Cant find texture "+string);
		return 0;
	} 
	
	public long registerTexture(BufferedImage bfi,String ID) {
		Graphics g=catlas.getGraphics();
		if(cx+bfi.getWidth()>msize) {
	    	cy+=maxheight;
	    	maxheight=0;
	    	cx=0;
	    }
	    if(cy+bfi.getHeight()>msize) {
	    	flush();
	    	return registerTexture(bfi,ID);
	    }
		g.drawImage(bfi, cx, cy, null);
		int x=cx;
		cx+=bfi.getWidth()+1;
		if(maxheight<bfi.getHeight())
	    	maxheight=bfi.getHeight();
		long lid=gentexid(x, cy, bfi.getWidth(), bfi.getHeight(), atlas, 1, ID);
		texturemap.put(ID, lid);
		return lid;
	}
	
	/**
	 * Registers an Texture from an path
	 * @param f
	 * @throws IOException
	 */
	public void registerTexture(File f) throws IOException {
		GlStateManager.enable(GL45.GL_TEXTURE_2D);
		String p=FileUtils.getIDfromFile(f);
		if(f.getName().endsWith(".ttf"))
			return;
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
		    	registerTexture(f);
		    	return;
		    }
		    for(int i=0;i<noi;i++) {
		    	bfi=reader.read(i);
		    	g.drawImage(bfi, cx+bfi.getWidth()*i, cy, null);
		    }
		    int x=cx;
		    cx+=bfi.getWidth()*noi+1;
		    if(maxheight<bfi.getHeight())
		    	maxheight=bfi.getHeight();
		    texturemap.put(p, gentexid(x, cy, bfi.getWidth(), bfi.getHeight(), atlas, noi, p));
		}else {
			BufferedImage bfi=ImageIO.read(f);
			if(cx+bfi.getWidth()>msize) {
		    	cy+=maxheight;
		    	maxheight=0;
		    	cx=0;
		    }
		    if(cy+bfi.getHeight()>msize) {
		    	flush();
		    	registerTexture(f);
		    	return;
		    }
			g.drawImage(bfi, cx, cy, null);
			int x=cx;
			cx+=bfi.getWidth()+1;
			if(maxheight<bfi.getHeight())
		    	maxheight=bfi.getHeight();
			texturemap.put(p, gentexid(x, cy, bfi.getWidth(), bfi.getHeight(), atlas, 1, p));
		}
	}
	
	/**
	 * Binds the Textures
	 */
	public void bind() {
		for(int i=0;i<atlas;i++) {
			GL45.glBindTextureUnit(i, atlases[i]);
		}
	}
	
	
	/**
	 * Flushes the Texture to the GPU
	 */
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
		try {
			ImageIO.write(catlas, "png", new File(Engine.dir.getParentFile()+"\\y"+atlas+".png"));
			System.out.println(Engine.dir+"y.png");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
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
	    How to unpack variables from the TextureID
	 	Unpacking
		x: ((id>>(13*1+2))&0xFFF) max 4095
		y: ((id>>(13*2+2))&0xFFF) max 4095
		dx: ((id>>(13*3+2))&0xFFF) max 4095
		dy: ((id>>(13*4+2))&0xFFF) max 4095
		Atlas: (id&7) 7
		aniframes: (id>>(13*5+2)&0x3F) 63
	 */
	
	/**
	 * @param id Texture ID
	 * @return If the Texture is animated
	 */
	public static boolean isanimated(long id) {
		return getaniframes(id)>1;
	}
	
	/**
	 * @param id Texture ID
	 * @return The X Position in the Texture Atlas
	 */
	public static int getx(long id) {
		return (int) ((id>>(13*1+2))&0xFFF);
	}
	
	/**
	 * @param id Texture ID
	 * @return The Y Position in the Texture Atlas
	 */
	public static int gety(long id) {
		return (int) ((id>>(13*2+2))&0xFFF);
	}
	
	/**
	 * @param id Texture ID
	 * @return The width in the Texture Atlas
	 */
	public static int getdx(long id) {
		return (int) ((id>>(13*3+2))&0xFFF);
	}
	
	/**
	 * @param id Texture ID
	 * @return The height  in the Texture Atlas
	 */
	public static int getdy(long id) {
		return (int) ((id>>(13*4+2))&0xFFF);
	}
	
	/**
	 * @param id Texture ID
	 * @return The Texture Atlas
	 */
	public static int getatlas(long id) {
		return (int) (id&7);
	}
	
	/**
	 * @param id Texture ID
	 * @return Number of Animated Frames
	 */
	public static int getaniframes(long id) {
		return (int) ((id>>(13*5+2))&0xFFF);
	}
	
	/**
	 * @return The Atlas Size
	 */
	public int getMsize() {
		return msize;
	}
	
	/**
	 * Generates the TextureID
	 * @param x The x coordinate in the Texture Atlas
	 * @param y The y coordinate in the Texture Atlas
	 * @param dx The width in the Texture Atlas
	 * @param dy The height in the Texture Atlas
	 * @param atlas The Texture Atlas its located
	 * @param animframe How many frames the Animation is long if static Image this becomes 0
	 * @param id The String ID for ErrorLogging
	 * @return The Numeric ID
	 */
	long gentexid(int x,int y,int dx,int dy,int atlas,int animframe, String id) {
		if(atlas>7||x>4095||y>4095||dx>4095||dy>4095||animframe>63) {
			Engine.log.severe(()->"Error creating Texture ["+id+"] size out of bounds!");
			System.exit(1);
		}
		return ((long)atlas|((long)x<<(13*1+2))|((long)y<<(13*2+2))|((long)dx<<(13*3+2))|((long)dy<<(13*4+2))|((long)animframe<<(13*5+2)));
	}


}
