package me.engine;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

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
import me.engine.Utils.Event.Events.Initialization;
import me.engine.Utils.Event.Events.KeyPressed;
import me.engine.Utils.Event.Events.MouseMoved;
import me.engine.Utils.Event.Events.MousePressed;
import me.engine.Utils.Event.Events.Render;
import me.engine.Utils.Event.Events.Render2D;
import me.engine.Utils.Event.Events.Update;
import me.engine.World.GameLevel;
import me.engine.World.Levels.SimpleLevel.SimpleLevel;
import me.game.Gui.Gui;

public class Main {
	/**
	 * Logger used to log
	 */
	public static Logger log;
	/**
	 * The GLFW Window ID
	 */
	long window;
	/**
	 * Reference to Main
	 */
	private static Main m;
	/**
	 * Working directory
	 */
	public static final File dir=new File(System.getProperty("user.dir"));
	/**
	 * The Renderer used for rendering
	 */
	Renderer render,uirender;
	/**
	 * Height and width of the Window
	 */
	int windowwidth,windowheight;
	/**
	 * TextureManager for loading and rendering Textures
	 */
	Texture tex;
	/**
	 * ChunkRenderer used to Render Chunks
	 */
	ChunkRenderer chunkrenderer;
	/**
	 * ScriptManager used to Compile Scripts for Entitys
	 */
	ScriptManager sm;
	/**
	 * EntityManager to load Entitys
	 */
	EntityManager em;
	
	/**
	 * Mouse Coordinates
	 */
	double mx,my;
	
	/**
	 * Offset from the Window to the Renderspace
	 */
	int offsetx,offsety;
	/**
	 * Placeholder PlayerPos
	 */
	float px=0,py=0;
	
	public Main() {
		//setup Logger
		setupLogger();
		log.setLevel(Level.ALL);
		for(Handler h: log.getHandlers())
			h.setLevel(Level.ALL);
		//init
		init();
	}

	public static void main(String[] args) {
		new Main();
	}
	
	/**
	 * Setups the window, Renderer, Texture and the Main Game Loop 
	 */
	public void init() {
			m=this;
			Renderer.clearTransform();
			GLFWErrorCallback g=GLFWErrorCallback.createPrint(new PrintStream(new LoggerOutputStream(log))).set();
			
			if (!GLFW.glfwInit())
				throw new IllegalStateException("Unable to initialize GLFW");
			// Configure GLFW
			GLFW.glfwDefaultWindowHints(); // optional, the current window hints are already the default
			GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE); // the window will stay hidden after creation
			GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE); // the window will be resizable
			GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_DEBUG_CONTEXT, GLFW.GLFW_TRUE);//GLError
			//CREATE WINDOW
			window = GLFW.glfwCreateWindow(900, 500, "Prodigium", MemoryUtil.NULL, MemoryUtil.NULL);
			if (window==MemoryUtil.NULL)
				throw new RuntimeException("Failed to create the GLFW window");
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
			//Force Aspect Ratio in windowed mode
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
			
			//Setup the Projection and Aspect Ratio
			setAspectRatio(windowwidth, windowheight);
			
			//Setup GLFW Callbacks
			setupCallbacks();
			
			//Blend for Alpha
			GlStateManager.enable(GL45.GL_BLEND);
			// Set the clear color
			GL45.glClearColor(0.239f, 0.239f, 0.239f, 0.0f);
			GL45.glBlendColor(1.0f, 1.0f, 1.0f, 1.0f);
			GL45.glBlendFunc(GL45.GL_SRC_ALPHA, GL45.GL_ONE_MINUS_SRC_ALPHA);  
			//Error Callback
			Callback debugProc = GLUtil.setupDebugMessageCallback();
			
			//Set an static transform on the camera so it centers
			render.c.getStati().set(1920/2f, 1080/2f);
//			render.c.setP(()->new Vector2f(px,py));
			GameLevel glevel=new SimpleLevel(150, render, chunkrenderer);
			//TEST ENTITY
			Entity e=em.newEntity("Entities.Test.Testentity:json");
			//Call Event init
			EventManager.call(new Initialization());
			float dt=0;
			
			new Gui();
			long time=System.nanoTime();//Frametime for debug
			while ( !GLFW.glfwWindowShouldClose(window) ) {
				GL45.glClear(GL45.GL_COLOR_BUFFER_BIT | GL45.GL_DEPTH_BUFFER_BIT); // clear the framebuffer
				//TEMP Entity Test
				e.render(render);
				e.update();
				
				//Level Rendering
				glevel.render();
				glevel.update();
				chunkrenderer.render();
				//Call Event Update
				EventManager.call(new Update());
				//Call Event Render
				EventManager.call(new Render(dt));
				render.flush();
				//Call Event Render2D
				EventManager.call(new Render2D(dt));
				uirender.flush();
				GLFW.glfwSwapBuffers(window); // swap the color buffers
				GLFW.glfwPollEvents(); // Poll for window events.
				final float frametime=(System.nanoTime()-time)/1000000f;
				log.finest(()->"Frametime "+frametime+"ms");// Log Frametime
				dt=(float)(System.nanoTime()-time)/1000000000f;
				time=System.nanoTime();//Frametime for debug
			}
			//Free Resources
			if(debugProc!=null)
				debugProc.free();
			g.close();
	}
	
	/**
	 * @return the GLFW Window ID
	 */
	public long getWindow() {
		return window;
	}

	/**
	 * Initializes Callbacks for Window Clicks, Keyboard Presses
	 */
	public void setupCallbacks() {
		GLFW.glfwSetMouseButtonCallback(window, (wwindow,key,pressed,args)->{
			  EventManager.call(new MousePressed(mx, my, key,pressed));
		});
		GLFW.glfwSetCursorPosCallback(window, (wwindow,x,y)->{
			//Scale the Mouse Coordinates from Screenspace to World/Renderspace
			x=Math.max(x-offsetx, 0);
			y=Math.max(y-offsety, 0);
			x=x/(windowwidth-offsetx*2)*1920;
			y=y/(windowheight-offsety*2)*1080;
			
			mx=x;
			my=y;
			EventManager.call(new MouseMoved(x, y));
		});
		GLFW.glfwSetWindowSizeCallback(window, (wwindow, width, height)->{
			//Set the Window width and
			//Recalculate the Aspect Ratio and the Projection
			windowwidth=width;
			windowheight=height;
			setAspectRatio(windowwidth, windowheight);
		});
		GLFW.glfwSetKeyCallback(window, (long window,int key, int scancode, int action, int mods)->{
			//PlaceHolder PlayerController
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
			EventManager.call(new KeyPressed(key,scancode,action,mods));
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
		offsetx = (windowwidth  / 2) - (width / 2);
		offsety = (windowheight / 2) - (height / 2);
		
		GL45.glViewport(offsetx,offsety,width,height);
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
	
	public Texture getTex() {
		return tex;
	}
	
	public ChunkRenderer getChunkrenderer() {
		return chunkrenderer;
	}

	public ScriptManager getScriptManager() {
		return sm;
	}

	public EntityManager getEntityManager() {
		return em;
	}

	public static Main getM() {
		return m;
	}
	
	public Renderer getRender() {
		return render;
	}
	
	public int getWindowwidth() {
		return windowwidth;
	}

	public int getWindowheight() {
		return windowheight;
	}
	
	public Renderer getUIrender() {
		return uirender;
	}
	
}
