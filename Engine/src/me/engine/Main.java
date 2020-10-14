package me.engine;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Date;
import java.util.logging.FileHandler;
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

import me.engine.Utils.GlStateManager;
import me.engine.Utils.Renderer;
import me.engine.Utils.Shader;
import me.engine.Utils.VertexBuffer;
import me.engine.Utils.Event.EventManager;
import me.engine.Utils.Event.Events.KeyPressed;

public class Main {
	public static Logger log;
	long window;
	public static File dir=new File(System.getProperty("user.dir"));
	Renderer render;
	int windowwidth,windowheight;
	float aspectratio=16/9;
	
	public Main() {
		init();
	}

	public static void main(String[] args) {
		setupLogger();
		new Main();
	}
	
	public void init() {
		//TODO: Use Logger
		GLFWErrorCallback.createPrint(System.err).set();
		if (!GLFW.glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");
		// Configure GLFW
		GLFW.glfwDefaultWindowHints(); // optional, the current window hints are already the default
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE); // the window will stay hidden after creation
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE); // the window will be resizable
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_DEBUG_CONTEXT, GLFW.GLFW_TRUE);//GLError
		
		window = GLFW.glfwCreateWindow(300, 300, "Hello World!", MemoryUtil.NULL, MemoryUtil.NULL);
		if (window==MemoryUtil.NULL)
			throw new RuntimeException("Failed to create the GLFW window");
		GLFW.glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
				EventManager.call(new KeyPressed(GLFW.glfwGetKeyName(key, scancode)));
		});
		
		try ( MemoryStack stack = MemoryStack.stackPush() ) {
			   IntBuffer w = stack.mallocInt(1);
			   IntBuffer h = stack.mallocInt(1);
			   IntBuffer comp = stack.mallocInt(1);

			   ByteBuffer icon = STBImage.stbi_load("C:\\Users\\Christian\\Documents\\Unbenannt.png", w, h, comp, 4);

			   GLFW.glfwSetWindowIcon(window, GLFWImage.mallocStack(1, stack)
			      .width(w.get(0))
			      .height(h.get(0))
			      .pixels(icon)
			   );

			   STBImage.stbi_image_free(icon);
		}
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
		GL.createCapabilities();
		SetAspectRatio(windowwidth, windowheight);
		
		Callbacks();
		// Set the clear color
		GL45.glClearColor(0.239f, 0.239f, 0.239f, 0.0f);
		
		//Error Callback
		Callback debugProc = GLUtil.setupDebugMessageCallback();
		
		render=new Renderer();
		VertexBuffer bf=new VertexBuffer(false);
		float[] vert= {
				-2f,2f,1f,
				-2f,-2f,1f,
				2f,-2f,1f,
				-2f,2f,1f,
				2f,2f,1f,
				2f,-2f,1f,
		};
		
		bf.createBuffer(vert, 0, 3);
		Shader s=new Shader(new File(Main.dir.getAbsolutePath()+"\\Assets\\Shader\\std.frag"), new File(Main.dir.getAbsolutePath()+"\\Assets\\Shader\\std.vert"));
		while ( !GLFW.glfwWindowShouldClose(window) ) {
			GL45.glClear(GL45.GL_COLOR_BUFFER_BIT | GL45.GL_DEPTH_BUFFER_BIT); // clear the framebuffer
			long time=System.nanoTime();
			loop();
			render.render();
			render.clear();
			System.out.println((System.nanoTime()-time)/1000000f+"ms");
//			GL11.glColor4f(1, 0, 0, 1);
//			GL11.glBegin(GL11.GL_POLYGON);
//			GL11.glVertex2d(-0.5, -0.5);
//			GL11.glVertex2d(-0.5, 0.5);
//			GL11.glVertex2d(0.5, 0.5);
//			GL11.glVertex2d(0.5, -0.5);
//			GL11.glEnd();
//			float dir=0.005f;
//			if((System.currentTimeMillis()/1000)%2==0)
//				dir*=-1;
//			for(int i=0;i<vert.length;i++) 
//				if((i+1)%3!=0)
//					vert[i]=(float) (vert[i]+dir);
//			bf.updateBuffer(vert, 0, 3);
			//GL45.glTranslatef(0.5f, 0.5f, 0);
			
//			GlStateManager.bindShader(s);
//			bf.bind(0);
//			GL45.glDrawArrays(GL45.GL_TRIANGLES, 0, bf.getbuffersize(0));
//			bf.unbind();
//			GlStateManager.unbindShader();
			GLFW.glfwSwapBuffers(window); // swap the color buffers
			// Poll for window events. The key callback above will only be
			// invoked during this call.
			GLFW.glfwPollEvents();
		}
		if(debugProc!=null)
			debugProc.free();
	}
	
	public void loop() {
		for(int i=0;i<15000;i++) {
			render.renderQuad(((float)Math.random())*1920, ((float)Math.random())*1080, 10, 10, 0);
		}
	}
	
	public void Callbacks() {
		/*
		 * 
		  GLFW.glfwSetMouseButtonCallback(window, (window,key,pressed,args)->{
			if(pressed!=0) {
				onMousePressed(key);
			}else {
				onMouseReleased();
			}
		});
		GLFW.glfwSetCursorPosCallback(window, (window,x,y)->{
			mx=(float)x;
			my=h-(float)y;
		});
		 * 
		 */
		GLFW.glfwSetWindowSizeCallback(window, (long window, int width, int height)->{
			windowwidth=width;
			windowheight=height;
			// This pops those matrices for the scale transformations.
			GL45.glMatrixMode(GL45.GL_MODELVIEW);
			GL45.glLoadIdentity();
			GL45.glPopMatrix();
			GL45.glMatrixMode(GL45.GL_PROJECTION);
			GL45.glPopMatrix();   
			GL45.glMatrixMode(GL45.GL_MODELVIEW);
			GL45.glPopMatrix();
			SetAspectRatio(windowwidth, windowheight);
		});
	}
	
	public void SetAspectRatio(int width,int height) {
		// This is your target virtual resolution for the game, the size you built your game to
		int virtual_width=1920;
		int virtual_height=1080;
		 
		float targetAspectRatio = virtual_width/(float)virtual_height;
		 
		// figure out the largest area that fits in this resolution at the desired aspect ratio
		height = (int)(width / targetAspectRatio + 0.5f);
		 
		if (height > windowheight )
		{
		   //It doesn't fit our height, we must switch to pillarbox then
		    height = windowheight ;
		    width = (int)(height * targetAspectRatio + 0.5f);
		}
		 
		// set up the new viewport centered in the backbuffer
		int vp_x = (windowwidth  / 2) - (width / 2);
		int vp_y = (windowheight / 2) - (height/ 2);
		 
		GL45.glViewport(vp_x,vp_y,width,height);
		// Now we use glOrtho
		GL45.glMatrixMode(GL45.GL_PROJECTION);
		GL45.glPushMatrix();
		GL45.glLoadIdentity();
		GL45.glOrtho(0, windowwidth, windowheight, 0, -1, 1);
		GL45.glMatrixMode(GL45.GL_MODELVIEW);
		GL45.glPushMatrix();
		GL45.glLoadIdentity();
		
		// Push in scale transformations
		GL45.glMatrixMode(GL45.GL_MODELVIEW);
		GL45.glPushMatrix();
	
		//Now to calculate the scale considering the screen size and virtual size
		float scale_x = (float)((float)(windowwidth) / (float)virtual_width);
		float scale_y = (float)((float)(windowheight) / (float)virtual_height);
		GL45.glScalef(scale_x, scale_y, 1.0f);
	}
	
	

	public static void setupLogger() {
		log=Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		File logfile=new File(dir.getAbsolutePath()+"\\logs\\log_"+new Date().toString().replaceAll(" ", "_").replaceAll(":", "-")+".txt");
		logfile.getParentFile().mkdirs();
		if(!logfile.exists())
			try {
				if(!logfile.createNewFile())
					throw new RuntimeException("Failed Initialising Logger: Cant create file at: "+logfile.getPath());
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("Failed Initialising Logger: Cant create file at: "+logfile.getPath());
			}
		
		try {
			FileHandler fh=new FileHandler(logfile.getAbsolutePath(), true);
			fh.setFormatter(new SimpleFormatter());
			log.addHandler(fh);
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Failed Initialising Logger: Error accsesing File at: "+logfile.getPath());
		}
	}
	
}
