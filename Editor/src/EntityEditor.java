
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL45;

import imgui.ImGui;
import imgui.flag.ImGuiInputTextFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImString;
import me.engine.Main;
import me.engine.Entity.Animation;
import me.engine.Entity.Entity;
import me.engine.Scripting.ScriptManager;
import me.engine.Utils.FileUtils;
import me.engine.Utils.GlStateManager;
import me.engine.Utils.Renderer;
import me.engine.Utils.Shader;
import me.engine.Utils.VertexBuffer;

public class EntityEditor extends Entity{
	int[] textureids;
	String[] textureSaveid;
	float[] texturewidth;
	Animation currTexture;
	float health,speed;
	float width,height;
	int framedelay;
	String name;
	ScriptEngine script;
	int missingtxt;
	boolean unifiedsize;
	
	//Stuff for Rendering the Sprite
	private VertexBuffer v;
	private Shader s;
	static Matrix4f scale,projection;
	
	ImString code;
	
	public EntityEditor() {
		name="Unamed Entity";
		textureids=new int[Animation.values().length];
		texturewidth=new float[Animation.values().length];
		textureSaveid=new String[Animation.values().length];
		try {
			toTexture(new File(Main.dir+"\\Assets\\Textures\\missing.png"), 0);
		} catch (IOException e) {
			Main.log.warning(e.toString());
		}
		textureSaveid[0]="";
		missingtxt=textureids[0];
		for(int i=1;i<textureids.length;i++) {
			textureids[i]=textureids[0];
			texturewidth[i]=texturewidth[0];
			textureSaveid[i]="";
		}
		code=new ImString(6000);
		framedelay=1;
		width=140f;
		unifiedsize=true;
		height=140f;
		currTexture=Animation.IDLE;
		v=new VertexBuffer(false);
		v.createBuffer(new float[] {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, 0, 3);
		v.createBuffer(new float[] {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, 1, 3);
		s=new Shader(new File(Main.dir.getAbsolutePath()+"\\Assets\\Shader\\std.frag"), new File(Main.dir.getAbsolutePath()+"\\Assets\\Shader\\std.vert"));
	}
	
	
	
	public void render(int x,int y) {
		projection=Renderer.getProjection();
		scale=Renderer.getScale();
		float[] vertecies=new float[18];
		float[] txt=new float[18];
		Vector4f v4f=getTexCoords(currTexture.gettextureindex(),(int)(System.currentTimeMillis()/framedelay)
				%getmaxframe(currTexture.gettextureindex()));
		float tx=v4f.x;
		float ty=v4f.y;
		float tx2=v4f.w;//texturewidth[currTexture.gettextureindex()]
		float ty2=v4f.z;
		vertecies[0]=x;
		vertecies[1]=y+height;
		vertecies[2]=1;
		txt[0]=tx;
		txt[1]=ty2;
		txt[2]=0;
		
		vertecies[3]=x;
		vertecies[4]=y;
		vertecies[5]=1;
		txt[3]=tx;
		txt[4]=ty;
		txt[5]=0;
		
		vertecies[6]=x+width;
		vertecies[7]=y;
		vertecies[8]=1;
		txt[6]=tx2;
		txt[7]=ty;
		txt[8]=0;
		
		vertecies[9]=x;
		vertecies[10]=y+height;
		vertecies[11]=1;
		txt[9]=tx;
		txt[10]=ty2;
		txt[11]=0;
		
		vertecies[12]=x+width;
		vertecies[13]=y+height;
		vertecies[14]=1;
		txt[12]=tx2;
		txt[13]=ty2;
		txt[14]=0;
		
		vertecies[15]=x+width;
		vertecies[16]=y;
		vertecies[17]=1;
		txt[15]=tx2;
		txt[16]=ty;
		txt[17]=0;
		v.updateBuffer(vertecies, 0, 3);
		v.updateBuffer(txt, 1, 3);
		s.bind();
		s.useUniform("projection", projection);
		s.useUniform("scale", scale);
		s.useUniform("u_Textures", 0,1);
		s.useUniform("u_Transform", new Vector2f(width/2, height/2));
		GL45.glBindTextureUnit(0, textureids[currTexture.gettextureindex()]);
		v.bind(0);
		v.bind(1);
		GL45.glDrawArrays(GL45.GL_TRIANGLES, 0, 18);
		v.unbind();
		s.unbind();
	}
	
	
	private int getmaxframe(int slot) {
		return (int) (1/texturewidth[slot]);
	}
	
	private Vector4f getTexCoords(int slot,int frame) {
		Vector4f v=new Vector4f();
		v.x=texturewidth[slot]*frame;
		v.w=texturewidth[slot]*(frame+1);
		v.y=0f;
		v.z=1f;
		return v;
	}
	
    String error="";
    int line=-1;
    int editorflags=0;

	
	public void imGui() {
		ImGui.setNextWindowSize(400, Main.getM().getWindowheight());
		ImGui.begin("Editor");
		if(ImGui.collapsingHeader("Attributes")) {
			ImString imstr=new ImString(name,50);
			if(ImGui.inputText("Name", imstr))
				name=imstr.get();
	        int[] imframe= {framedelay};
	        if(ImGui.sliderInt("AnimationDelay", imframe, 1, 500))
	        	framedelay=imframe[0];
	        float[] imhealth= {health};
	        if(ImGui.sliderFloat("Health", imhealth, 1, 3000))
	        	health=imhealth[0];
	        float[] imspeed= {speed};
	        if(ImGui.sliderFloat("Speed", imspeed, 1, 1000))
	        	speed=imspeed[0];
	        if(ImGui.selectable("Unified Size", unifiedsize))
	        	unifiedsize=!unifiedsize;
	        if(unifiedsize) {
	        	float[] imsize= {height};
		        if(ImGui.sliderFloat("Size", imsize, 50, 500)) {
		        	height=imsize[0];
		        	width=imsize[0];
		        }
	        }else {
		        float[] imheight= {height};
		        if(ImGui.sliderFloat("Width", imheight, 50, 500))
		        	height=imheight[0];
		        float[] imwidth= {width};
		        if(ImGui.sliderFloat("Height", imwidth, 50, 500))
		        	width=imwidth[0];
	        }
		}
        if(ImGui.collapsingHeader("Textures")) {
        	Animation[] a=Animation.values();
	        for(int i=0;i<a.length;i++) {
	        	Animation curr=a[i];
	        	if(ImGui.treeNode(curr.name())) {
	        		Vector4f v=getTexCoords(curr.gettextureindex(),(int)(System.currentTimeMillis()/framedelay)
	        				%getmaxframe(curr.gettextureindex()));
		        	if(ImGui.imageButton(textureids[curr.gettextureindex()], 50, 50, v.x, v.y, v.w, v.z, 0))
		        		try {
							toTexture(showFilePicker(), i);
						} catch (IOException e) {
							Main.log.severe(e.toString());
						}
		        	ImGui.textDisabled("(?)");
		        	if(ImGui.isItemHovered()) {
			        	ImGui.beginTooltip();
			        	ImGui.text("The TextureID consists of the Path of the Texture in the Textures folder with the \"\\\" replaced with \".\" and the \".\" replaced with \":\"\r\n" + 
			        			   "e.g. Assets\\Textures\\Test\\testpng.png becomes Assets.Textures.Test.testpng:png");
			        	ImGui.endTooltip();
		        	}
		        	ImString imtxtid=new ImString(textureSaveid[i],50);
		    		if(ImGui.inputText("TextureID", imtxtid))
		    			textureSaveid[i]=imtxtid.get();
		        	ImGui.treePop();
	        	}
	        }
        }
        if(ImGui.collapsingHeader("Code")) {
        	
        }
        if(ImGui.collapsingHeader("Preview")) {
        	if(ImGui.treeNode("Select Texture to Preview")) {
	        	for(Animation a:Animation.values()) {
	        		if(ImGui.selectable(a.name(), a.equals(currTexture)))
	        			currTexture=a;
	        	}
	        	ImGui.treePop();
        	}
        }
        ImGui.end();
        ImGui.setNextWindowBgAlpha(1f);
        //Code Editor
        int sx=-1;
        int sy=-1;
        ImGui.begin("Code",editorflags);
        sx=(int) ImGui.getWindowPosX();
        sy=(int) ImGui.getWindowPosY();
        if(ImGui.button("Compile")) {
        	try {
				script=Main.getM().getScriptManager().compileScript(code.get(), "ECMAScript", (sc)->{sc.put("Entity", this);sc.put("e", this);});
				editorflags=0;
			} catch (ScriptException e) {
				line=0;
				line=e.getLineNumber();
				error=e.getMessage();
			}
        }
        if(ImGui.inputTextMultiline("Code", code, (float)Main.getM().getWindowwidth(),(float)Main.getM().getWindowheight(),ImGuiInputTextFlags.AllowTabInput))
        	editorflags=editorflags|ImGuiWindowFlags.UnsavedDocument;
        ImGui.end();
        if(!error.isEmpty()) {
        	ImGui.setNextWindowPos(sx, sy+ImGui.getFontSize()*(line+1f));
        	int flags=ImGuiWindowFlags.NoDecoration|ImGuiWindowFlags.NoDocking|ImGuiWindowFlags.NoMove|
        			  ImGuiWindowFlags.NoSavedSettings|ImGuiWindowFlags.NoTitleBar;
        	ImGui.begin("ERROR",flags);
        	ImGui.text(error);
        	if(ImGui.isItemHovered()) {
        		error="";
        	    line=-1;
        	    sx=-1;
        	    sy=-1;
        	}
        	ImGui.end();
        }
	}
	
	private File showFilePicker() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		JFrame frame = new JFrame();
		final JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(Main.dir);
		fc.addChoosableFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "png", "gif"));
		fc.removeChoosableFileFilter(fc.getChoosableFileFilters()[0]);
		frame.setBounds(fc.getBounds());
		frame.setBounds(-100, -100, -100, -100);
		frame.setVisible(true);
		frame.setAlwaysOnTop(true);
		frame.setVisible(false);
		fc.showDialog(null, "Choose a Image");
		fc.setVisible(true);
		fc.grabFocus();
		return fc.getSelectedFile();
	}
	
	
	private void toTexture(File image,int slot) throws IOException {
		if(image==null) {
			textureids[slot]=missingtxt;
			texturewidth[slot]=1f;
			return;
		}
		if(image.getAbsolutePath().contains("Assets"))
			textureSaveid[slot]=FileUtils.getIDfromFile(image);
		if(image.getName().endsWith(".gif")) {
			ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
		    ImageInputStream ciis = ImageIO.createImageInputStream(image);
		    reader.setInput(ciis, false);
		    int noi = reader.getNumImages(true);
		    BufferedImage bfi = reader.read(0);
		    BufferedImage texture=new BufferedImage(bfi.getWidth()*noi, bfi.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		    Graphics g=texture.getGraphics();
		    for(int i=0;i<noi;i++)
		    	g.drawImage(reader.read(i), bfi.getWidth()*i, 0, null);
		    texturewidth[slot]=bfi.getWidth()/(float)texture.getWidth();
		    toTexture(texture, slot);
		}else {
			BufferedImage bfi=ImageIO.read(image);
			texturewidth[slot]=1f;
			toTexture(bfi, slot);
		}
	}
	
	private void toTexture(BufferedImage bf,int slot) {
		if(textureids[slot]!=0&&textureids[slot]!=missingtxt) {
			GL45.glDeleteTextures(textureids[slot]);
			textureids[slot]=0;
		}
		int[] pixels = new int[bf.getWidth() * bf.getHeight()];
		bf.getRGB(0, 0, bf.getWidth(), bf.getHeight(), pixels, 0, bf.getWidth());
		
		IntBuffer ib=BufferUtils.createIntBuffer(bf.getWidth() * bf.getHeight());
		for (int i = 0; i < bf.getWidth() * bf.getHeight(); i++) {
			int a = (pixels[i] & 0xff000000) >> 24;
			int r = (pixels[i] & 0xff0000) >> 16;
			int g = (pixels[i] & 0xff00) >> 8;
			int b = (pixels[i] & 0xff);
			ib.put(a << 24 | b << 16 | g << 8 | r);
		}
		ib.flip();
		
		int textureid=GL45.glGenTextures();
		GL45.glBindTexture(GL45.GL_TEXTURE_2D, textureid);
		GL45.glTexParameteri(GL45.GL_TEXTURE_2D, GL45.GL_TEXTURE_MIN_FILTER, GL45.GL_NEAREST);
		GL45.glTexParameteri(GL45.GL_TEXTURE_2D, GL45.GL_TEXTURE_MAG_FILTER, GL45.GL_NEAREST);
		GL45.glTexImage2D(GL45.GL_TEXTURE_2D, 0, GL45.GL_RGBA, bf.getWidth(), bf.getHeight(), 0, GL45.GL_RGBA, GL45.GL_UNSIGNED_BYTE, ib);
		GL45.glBindTexture(GL45.GL_TEXTURE_2D, 0);
		GlStateManager.unbindTexture2D();
		textureids[slot]=textureid;
	}
}
