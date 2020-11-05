
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
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

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import imgui.ImGui;
import imgui.flag.ImGuiInputTextFlags;
import imgui.flag.ImGuiSliderFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImString;
import me.engine.Engine;
import me.engine.Entity.Animation;
import me.engine.Entity.Entity;
import me.engine.Utils.FileUtils;
import me.engine.Utils.GlStateManager;
import me.engine.Utils.Renderer;
import me.engine.Utils.Shader;
import me.engine.Utils.VertexBuffer;

/**
 * @author Christian
 *
 */
public class EntityEditor extends Entity{
	/**
	 * The TextureIDs for the Textures currently previewed
	 */
	int[] textureids;
	/**
	 * The StringID of the Textures currently previewed
	 */
	BufferedImage[] images;
	String[] textureSaveid;
	/**
	 * The width of the Textures currently previewed
	 */
	float[] texturewidth;
	/**
	 * CurrentTexture
	 */
	Animation currTexture;
	/**
	 * Stats of the Entity
	 */
	float health,speed;
	/**
	 * Dimensions of the Entity
	 */
	float width,height;
	/**
	 * The Delay between Frames of the Animation
	 */
	int framedelay;
	/**
	 * The Name of the Entity
	 */
	String name;
	/**
	 * The script of the Entity
	 */
	ScriptEngine script;
	/**
	 * TextureID for the missingTexture Texture
	 */
	int missingtxt;
	/**
	 * If there should be width and height or size
	 */
	boolean unifiedsize;
	/**
	 * Compilation Error
	 */
	String error="";
    int errorline=-1;
    /**
     * Flags of the ImGuiTab used to show edited and not Compiled Code
     */
    int editorflags=0;
    /**
     * Stores the Code
     */
    ImString code;
    String scriptID;
    String entityID;
	
	//Stuff for Rendering the Sprite
	private VertexBuffer v;
	private Shader s;
	Matrix4f scale,projection;
	
	String exportstatus;
	
	public EntityEditor() {
		name="Unamed_Entity";
		textureids=new int[Animation.values().length];
		images=new BufferedImage[Animation.values().length];
		texturewidth=new float[Animation.values().length];
		textureSaveid=new String[Animation.values().length];
		try {
			toTexture(new File(Engine.dir+"\\Assets\\Textures\\missing.png"), 0);
		} catch (IOException e) {
			Engine.log.warning("Error loading Texture"+e.toString());
		}
		textureSaveid[0]="";
		missingtxt=textureids[0];
		for(int i=1;i<textureids.length;i++) {
			textureids[i]=textureids[0];
			texturewidth[i]=texturewidth[0];
			textureSaveid[i]="";
//			images[i]=images[0];
		}
		code=new ImString(6000);
		framedelay=1;
		width=140f;
		entityID="Entities."+name;
		unifiedsize=true;
		height=140f;
		currTexture=Animation.IDLE;
		exportstatus="Idle...";
		v=new VertexBuffer(false);
		v.createBuffer(new float[] {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, 0, 3);
		v.createBuffer(new float[] {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, 1, 3);
		s=new Shader(new File(Engine.dir.getAbsolutePath()+"\\Assets\\Shader\\chunk.frag"), new File(Engine.dir.getAbsolutePath()+"\\Assets\\Shader\\chunk.vert"));
		scriptID=entityID.replace("Entities", "Scripts")+":js";
		entityID+=":json";
	}
	
	
	
	/**
	 * Render Preview of the Sprite
	 * @param x The X coordinate where it will be rendered
	 * @param y The Y coordinate where it will be rendered
	 */
	public void render(int x,int y) {
		projection=Renderer.getProjection();
		scale=Renderer.getScale();
		float[] vertecies=new float[18];
		float[] txt=new float[18];
		Vector4f v4f=getTexCoords(currTexture.gettextureindex(),(int)(System.currentTimeMillis()/framedelay)
				%getmaxframe(currTexture.gettextureindex()));
		float tx=v4f.x;
		float ty=v4f.y;
		float tx2=v4f.w;
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
	
	
	/**
	 * Gets Max Frame
	 * @param slot The texture slot
	 * @return The Max Frames
	 */
	private int getmaxframe(int slot) {
		return (int) (1/texturewidth[slot]);
	}
	
	
	/**
	 * Returns the Texture Coordinates of the specified Texture and frame
	 * @param slot The Textureslot of the Texture
	 * @param frame The frame of the Texture
	 * @return The Texture Coords
	 */
	private Vector4f getTexCoords(int slot,int frame) {
		Vector4f v=new Vector4f();
		v.x=texturewidth[slot]*frame;
		v.w=texturewidth[slot]*(frame+1);
		v.y=0f;
		v.z=1f;
		return v;
	}
	
	private void exporten() {
		exportstatus="";
		String scode=code.get();
		if(scriptID.isEmpty()&&!scode.isEmpty()) {
			exportstatus+="Script ID is empty\n";
			return;
		}
		if(editorflags!=0) {
			exportstatus+="Compile Script first\n";
			return;
		}
		for(int i=0;i<textureSaveid.length;i++) {
			String s=textureSaveid[i];
			if(s.isEmpty()) {
				exportstatus+="Texture "+Animation.values()[i]+" has no textureID\n";
				return;
			}
			int txtid=textureids[i];
			if(txtid==missingtxt) {
				exportstatus+="Texture "+Animation.values()[i]+" has no texture\n";
				return;
			}
		}
		if(entityID.isEmpty()) {
			exportstatus+="EntityID is empty\n";
			return;
		}
		if(!scode.isEmpty()) {
			try(FileWriter f=new FileWriter(FileUtils.getFilefromID(scriptID))){
				f.write(scode);
			} catch (IOException e) {
				exportstatus+="Error exporting Script "+e.getMessage()+"\n";
			}
		}
		for(int i=0;i<textureSaveid.length;i++) {
			File f=FileUtils.getFilefromID(textureSaveid[i]);
			f.mkdirs();
			if(f.exists()) {
				exportstatus+="Warning Texture already exists "+f.getAbsolutePath()+"\n";
				continue;
			}
			if(texturewidth[i]==1) {
				try {
					ImageIO.write(images[i], "png", f);
				} catch (IOException e) {
					exportstatus+="Error writing file "+e.getMessage()+"\n";
				}
			}else {
				try {
					f.createNewFile();
				} catch (IOException e) {
					exportstatus+="Error writing file "+e.getMessage()+"\n";
				}
				try(FileOutputStream fstream=new FileOutputStream(f);) {
				AnimatedGifEncoder e = new AnimatedGifEncoder();
				e.start(fstream);
				e.setDelay(framedelay);
				float maxframes=getmaxframe(i);
				for(int in=0;in<maxframes;in++) {
					e.addFrame(images[i].getSubimage((int)(in/maxframes*images[i].getWidth()), 0, (int)(texturewidth[i]*images[i].getWidth()), images[i].getHeight()));
				}
				e.finish();
				} catch (IOException e) {
					exportstatus+="Error writing file "+e.getMessage()+"\n";
				}
			}
		}
		
		File f=new File(FileUtils.getFilefromID(entityID).getAbsolutePath());
		f.mkdirs();
		if(f.exists()&&!f.delete()) {
			exportstatus+="Couldnt save Entity at "+f.getAbsolutePath()+"\n";
			return;
		}
		try(JsonWriter jwrite=new JsonWriter(new FileWriter(f))) {
			jwrite.beginObject();
			jwrite.name("Name");
			jwrite.value(name);
			jwrite.name("Animation");
			jwrite.beginObject();
			for(Animation a:Animation.values()) {
				jwrite.name(a.name());
				jwrite.value(textureSaveid[a.gettextureindex()]);
			}
			jwrite.endObject();
			jwrite.name("Health");
			jwrite.value(health);
			jwrite.name("Width");
			jwrite.value(width);
			jwrite.name("Height");
			jwrite.value(height);
			jwrite.name("Speed");
			jwrite.value(speed);
			if(!code.get().isEmpty()) {
				jwrite.name("Script");
				jwrite.value(scriptID);
			}
			jwrite.name("FrameDelay");
			jwrite.value(framedelay);
			jwrite.endObject();
			jwrite.flush();
			exportstatus="Finished exporting "+name;
		} catch (IOException e) {
			exportstatus+="Couldnt save Entity "+e.getMessage()+"\n";
		}
	}
	//TODO: create import
	private void importen(File f) {
		exportstatus="";
		entityID=FileUtils.getIDfromFile(f);
		if(f==null)
			return;
		try(JsonReader jr=new JsonReader(new FileReader(f))){
			jr.beginObject();
			while(jr.hasNext()) {
				String s=jr.nextName();
				switch(s) {
				case "Name":
					name=jr.nextString();
					break;
				case "Animation":
					if(jr.peek()==JsonToken.NULL)
						continue;
					jr.beginObject();
					while(jr.hasNext()) {
						int slot=Animation.valueOf(jr.nextName()).gettextureindex();
						toTexture(FileUtils.getFilefromID(jr.nextString()), slot);
					}
					jr.endObject();
					break;
				case "Health":
					health=(float) jr.nextDouble();
					break;
				case "Width":
					width=(float) jr.nextDouble();
					break;
				case "Height":
					height=(float) jr.nextDouble();
					break;
				case "Speed":
					speed=(float) jr.nextDouble();
					break;
				case "Script":
					code.set(FileUtils.stringfromFile(jr.nextString()));
					break;
				case "FrameDelay":
					framedelay=jr.nextInt();
					break;
				default:
					exportstatus+="Error reading JSON "+f.getName()+" Unknown keyword: "+s;
					break;
				}
			}
			exportstatus="Finished importing "+name;
		}catch(Exception e) {
			exportstatus+=e.toString();
		}
		
	}
	boolean wasempty=false;
	/**
	 * Renders the ImGui Menus
	 */
	public void imGui() {
		ImGui.setNextWindowSize(400, Engine.getEngine().getWindowheight());
		ImGui.begin("Editor");
		if(ImGui.collapsingHeader("Attributes")) {
			ImString imname=new ImString(name,300);
			if(ImGui.inputText("Name", imname)) {
				if(imname.get().isEmpty()) {
					entityID=entityID.replace(name, "Unamed_Entity");
					scriptID=scriptID.replace(name, "Unamed_Entity");
					wasempty=true;
				}else {
					if(wasempty) {
						entityID=entityID.replace("Unamed_Entity", imname.get());
						scriptID=scriptID.replace("Unamed_Entity", imname.get());
					}else {
						entityID=entityID.replace(name, imname.get());
						scriptID=scriptID.replace(name, imname.get());
					}
					wasempty=false;
				}
				name=imname.get();
			}
			ImString imentityID=new ImString(entityID,300);
			if(ImGui.inputText("EntityID", imentityID))
				entityID=imentityID.get();
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
							toTexture(showFilePicker("Textures","png","gif"), i);
						} catch (IOException e) {
							Engine.log.severe(e.toString());
						}
		        	ImGui.textDisabled("(?)");
		        	if(ImGui.isItemHovered()) {
			        	ImGui.beginTooltip();
			        	ImGui.text("The TextureID consists of the Path of the Texture in the Textures folder with the \"\\\" replaced with \".\" and the \".\" replaced with \":\"\r\n" + 
			        			   "e.g. Assets\\Textures\\Test\\testpng.png becomes Assets.Textures.Test.testpng:png");
			        	ImGui.endTooltip();
		        	}
		        	ImString imtxtid=new ImString(textureSaveid[i],300);
		    		if(ImGui.inputText("TextureID", imtxtid))
		    			textureSaveid[i]=imtxtid.get();
		        	ImGui.treePop();
	        	}
	        }
        }
        if(ImGui.collapsingHeader("Code")) {
        	ImString imtxtid=new ImString(scriptID,50);
    		if(ImGui.inputText("ScriptID", imtxtid))
    			scriptID=imtxtid.get();
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
        if(ImGui.collapsingHeader("File")) {
        	ImGui.text(exportstatus);
        	if(ImGui.button("Export"))
        		try {
        		exporten();
        		}catch(Exception e) {
        			e.printStackTrace();
        		}
        	if(ImGui.button("Import"))
        		importen(showFilePicker("Entity","json"));
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
				script=Engine.getEngine().getScriptManager().compileScript(code.get(),"ECMAScript",sc->{sc.put("Entity", this);sc.put("e", this);});
				editorflags=0;
			} catch (ScriptException e) {
				errorline=0;
				errorline=e.getLineNumber();
				error=e.getMessage();
			} catch(Exception e) {
				e.printStackTrace();
			}
        }
        if(ImGui.inputTextMultiline("Code", code, (float)Engine.getEngine().getWindowwidth(),(float)Engine.getEngine().getWindowheight(),ImGuiInputTextFlags.AllowTabInput))
        	editorflags=editorflags|ImGuiWindowFlags.UnsavedDocument;
        ImGui.end();
        if(!error.isEmpty()) {
        	ImGui.setNextWindowPos(sx, sy+ImGui.getFontSize()*(errorline+1f));
        	int flags=ImGuiWindowFlags.NoDecoration|ImGuiWindowFlags.NoDocking|ImGuiWindowFlags.NoMove|
        			  ImGuiWindowFlags.NoSavedSettings|ImGuiWindowFlags.NoTitleBar;
        	ImGui.begin("ERROR",flags);
        	ImGui.text(error);
        	if(ImGui.isItemHovered()) {
        		error="";
        	    errorline=-1;
        	    sx=-1;
        	    sy=-1;
        	}
        	ImGui.end();
        }
	}
	File lastdir=Engine.dir;
	/**
	 * Displays a FilePicker
	 * @return The File the User chose
	 */
	private File showFilePicker(String filtername,String... extensions) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		JFrame frame = new JFrame();
		final JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(lastdir);
		fc.addChoosableFileFilter(new FileNameExtensionFilter(filtername, extensions));
		fc.removeChoosableFileFilter(fc.getChoosableFileFilters()[0]);
		frame.setBounds(fc.getBounds());
		frame.setBounds(-100, -100, -100, -100);
		frame.setVisible(true);
		frame.setAlwaysOnTop(true);
		frame.setVisible(false);
		fc.showDialog(null, "Choose a Image");
		fc.setVisible(true);
		fc.grabFocus();
		if(fc.getSelectedFile()!=null)
			lastdir=fc.getSelectedFile();
		return fc.getSelectedFile();
	}
	
	
	/**
	 * Converts an File to an Texture and puts it in the specified Textureslot
	 * @param image The Filepath of the Image
	 * @param slot The slot to put the Texture in
	 * @throws IOException
	 */
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
		    images[slot]=texture;
		    toTexture(texture, slot);
		}else {
			BufferedImage bfi=ImageIO.read(image);
			images[slot]=bfi;
			texturewidth[slot]=1f;
			toTexture(bfi, slot);
		}
	}
	
	/**
	 * Converts an BufferedImage to an Texture and puts it in the specified Textureslot
	 * @param bf The BufferedImage to be Converted
	 * @param slot The slot to put the Texture in
	 */
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
