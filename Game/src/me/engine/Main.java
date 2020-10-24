package me.engine;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Date;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL45;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.Callback;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import me.engine.Entity.Entity;
import me.engine.Entity.EntityManager;
import me.engine.Scripting.ScriptManager;
import me.engine.Utils.ChunkRenderer;
import me.engine.Utils.GlStateManager;
import me.engine.Utils.LoggerOutputStream;
import me.engine.Utils.Renderer;
import me.engine.Utils.Texture;
import me.engine.Utils.Event.EventManager;
import me.engine.Utils.Event.Events.KeyPressed;
import me.engine.Utils.Event.Events.MouseMoved;
import me.engine.Utils.Event.Events.MousePressed;
import me.engine.Utils.Event.Events.Render;
import me.engine.Utils.Event.Events.Update;
import me.engine.World.Chunk;
import me.engine.World.Tile;

public class Main {
	public static Logger log;
	long window;
	private static Main m;
	public static final File dir=new File(System.getProperty("user.dir"));
	static Renderer render;
	int windowwidth,windowheight;
	static Texture tex;
	ChunkRenderer chunkrenderer;
	ScriptManager sm;
	EntityManager em;
	
	public Main() {
		init();
	}

	public static void main(String[] args) {
		setupLogger();
		log.setLevel(Level.ALL);
		m=new Main();
	}
	
	public void init() {
		Renderer.clearTransform();
		GLFWErrorCallback g=GLFWErrorCallback.createPrint(new PrintStream(new LoggerOutputStream(log))).set();
			if (!GLFW.glfwInit())
				throw new IllegalStateException("Unable to initialize GLFW");
			// Configure GLFW
			GLFW.glfwDefaultWindowHints(); // optional, the current window hints are already the default
			GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE); // the window will stay hidden after creation
			GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE); // the window will be resizable
			GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_DEBUG_CONTEXT, GLFW.GLFW_TRUE);//GLError
			//WINDOW
			window = GLFW.glfwCreateWindow(900, 500, "Prodigium", MemoryUtil.NULL, MemoryUtil.NULL);
			if (window==MemoryUtil.NULL)
				throw new RuntimeException("Failed to create the GLFW window");
			GLFW.glfwSetKeyCallback(window, (wwindow, key, scancode, action, mods) -> {
					EventManager.call(new KeyPressed(GLFW.glfwGetKeyName(key, scancode)));
			});
			// ICON
			try ( MemoryStack stack = MemoryStack.stackPush() ) {
				   IntBuffer w = stack.mallocInt(1);
				   IntBuffer h = stack.mallocInt(1);
				   IntBuffer comp = stack.mallocInt(1);
	
				   ByteBuffer icon = STBImage.stbi_load(dir+"\\Assets\\Textures\\Icons\\Game_icon.png", w, h, comp, 4);
	
				   GLFW.glfwSetWindowIcon(window, GLFWImage.mallocStack(1, stack)
				      .width(w.get(0))
				      .height(h.get(0))
				      .pixels(icon)
				   );
	
				   STBImage.stbi_image_free(icon);
			}
			//SIZE
			//http://forum.lwjgl.org/index.php?topic=5548.0
			IntBuffer w = BufferUtils.createIntBuffer(1);
			IntBuffer h = BufferUtils.createIntBuffer(1);
			GLFW.glfwGetWindowSize(window, w, h);
			windowwidth = w.get(0);
			windowheight = h.get(0);
			
			// Make the OpenGL context current
			GLFW.glfwMakeContextCurrent(window);
			// Enable v-sync
			GLFW.glfwSwapInterval(1);
			GLFW.glfwSetWindowAspectRatio(window, 16, 9);
			// Make the window visible
			GLFW.glfwShowWindow(window);
			//Enable GL
			GL.createCapabilities();
			//Setup Texture and Renderer
			tex=new Texture();
			render=new Renderer();
			chunkrenderer=new ChunkRenderer();
			
			//Setup Entity System
			sm=new ScriptManager();
			em=new EntityManager(sm);
			
			setAspectRatio(windowwidth, windowheight);
			
			//Setup Callbacks
			setupCallbacks();
			
			// Set the clear color
			GL45.glClearColor(0.239f, 0.239f, 0.239f, 0.0f);
			GL45.glBlendColor(1.0f, 1.0f, 1.0f, 1.0f);
			//Error Callback
			Callback debugProc = GLUtil.setupDebugMessageCallback();
			
			//Test textures
			long txt=tex.getTexture("Textures.Test.testgif:gif");
			long txt2=tex.getTexture("Textures.Test.testpng:png");
			tex.flush();
			render.c.getStati().set(1920/2f, 1080/2f);
			render.c.setP(()->new Vector2f(px,py));
			//Blend for Alpha
			GlStateManager.enable(GL45.GL_BLEND);
			GL45.glBlendFunc(GL45.GL_SRC_ALPHA, GL45.GL_ONE_MINUS_SRC_ALPHA);  
			Chunk c=new Chunk();
			for(int x=0;x<Chunk.SIZE;x++) {
				for(int y=0;y<Chunk.SIZE;y++) {
					c.getTiles()[x][y]=new Tile("Textures.Test.testground:png");
				}
			}
			Random r=new Random();
			chunkrenderer.add(c.renderChunk(-3000, -3000, 500));
			Entity e=em.newEntity("Entities.Test.Testentity:json");
			while ( !GLFW.glfwWindowShouldClose(window) ) {
				GL45.glClear(GL45.GL_COLOR_BUFFER_BIT | GL45.GL_DEPTH_BUFFER_BIT); // clear the framebuffer
				long time=System.nanoTime();//Frametime for debug
				EventManager.call(new Update());
				EventManager.call(new Render());
				r.setSeed(2);
				e.render(render);
				e.update();
				for(int i=0;i<120;i++) {
					render.renderRect(r.nextInt(4000)-2000f, r.nextInt(4000)-2000, 140f, 140f, txt,(int)((System.currentTimeMillis()+r.nextInt())/120)%Texture.getaniframes(txt));
				}
				chunkrenderer.render();
				render.renderRect(px, py, 140f, 140f, txt2,0);
				render.flush();
				GLFW.glfwSwapBuffers(window); // swap the color buffers
				GLFW.glfwPollEvents(); // Poll for window events.
				log.finest(()->"Frametime "+(System.nanoTime()-time)/1000000f+"ms");// Log Frametime
			}
			//Free Resources
			if(debugProc!=null)
				debugProc.free();
			g.close();
	}
	
	double mx,my;
	
	float px=0,py=0;
	/**
	 * Initializes Callbacks for Window Clicks, Keyboard Presses
	 */
	public void setupCallbacks() {
		GLFW.glfwSetMouseButtonCallback(window, (wwindow,key,pressed,args)->{
			  EventManager.call(new MousePressed(mx, my, key,pressed));
		});
		GLFW.glfwSetCursorPosCallback(window, (wwindow,x,y)->{
			mx=x;
			my=y;
			EventManager.call(new MouseMoved(x, y));
		});
		GLFW.glfwSetWindowSizeCallback(window, (wwindow, width, height)->{
			windowwidth=width;
			windowheight=height;
			setAspectRatio(windowwidth, windowheight);
		});
		GLFW.glfwSetKeyCallback(window, (long window,int key, int scancode, int action, int mods)->{
			switch(key) {
			case GLFW.GLFW_KEY_W:
				py-=10;
				break;
			case GLFW.GLFW_KEY_S:
				py+=10;
				break;
			case GLFW.GLFW_KEY_A:
				px-=10;
				break;
			case GLFW.GLFW_KEY_D:
				px+=10;
				break;
			}
		});
	}
	
	/**
	 * sets the Aspect Ratio after the Window has been resized
	 * @param width The new width
	 * @param height The new height
	 */
	public void setAspectRatio(int width,int height) {
		// This is your target virtual resolution for the game, the size you built your game to
		int virtualwidth=1920;
		int virtualheight=1080;
		 Renderer.clearTransform();
		float targetAspectRatio = virtualwidth/(float)virtualheight;
		 
		// figure out the largest area that fits in this resolution at the desired aspect ratio
		height = (int)(width / targetAspectRatio + 0.5f);
		 
		if (height > windowheight )
		{
		   //It doesn't fit our height, we must switch to pillarbox then
		    height = windowheight ;
		    width = (int)(height * targetAspectRatio + 0.5f);
		}
		 
		// set up the new viewport centered in the backbuffer
		int vpx = (windowwidth  / 2) - (width / 2);
		int vpy = (windowheight / 2) - (height / 2);
		
		GL45.glViewport(vpx,vpy,width,height);
		// Now we use Ortho
		render.ortho(0, windowwidth, windowheight, 0, -1, 1);
		
		//Now to calculate the scale considering the screen size and virtual size
		float scalex = ((float)(windowwidth) / (float)virtualwidth);
		float scaley = ((float)(windowheight) / (float)virtualheight);
		GL45.glScalef(scalex, scaley, 1.0f);
		render.scale(scalex, scaley, 1.0f);
	}
	
	

	/**
	 * Setups the Logger
	 */
	public static void setupLogger() {
		log=Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		File logfile=new File(dir.getAbsolutePath()+"\\logs\\log_"+new Date().toString().replaceAll(" ", "_").replaceAll(":", "-")+".txt");
		logfile.getParentFile().mkdirs();
		if(!logfile.exists())
			try {
				if(!logfile.createNewFile())
					throw new RuntimeException("Failed Initialising Logger: Cant create file at: "+logfile.getPath());
			} catch (IOException e) {
				log.severe(e.getMessage());
				throw new RuntimeException("Failed Initialising Logger: Cant create file at: "+logfile.getPath());
			}
		
		try {
			FileHandler fh=new FileHandler(logfile.getAbsolutePath(), true);
			fh.setFormatter(new SimpleFormatter());
			log.addHandler(fh);
		} catch (SecurityException | IOException e) {
			log.severe(e.getMessage());
			throw new RuntimeException("Failed Initialising Logger: Error accsesing File at: "+logfile.getPath());
		}
	}
	
	public static Texture getTex() {
		return tex;
	}
	
	public static Main getM() {
		return m;
	}
	
	public static Renderer getRender() {
		return render;
	}
	
}
