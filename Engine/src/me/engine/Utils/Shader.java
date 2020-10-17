package me.engine.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.Iterator;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL45;

import me.engine.Main;

public class Shader {
	
	protected int program;
	private int vertexShader;
	private int fragmentShader;
	private String v,f;
	
	public Shader(File frag,File vert) {
		v=stringfromFile(vert);
		f=stringfromFile(frag);
		compile("","");
	}
	
	public Shader(File frag,String farg,File vert,String varg) {
		v=stringfromFile(vert);
		f=stringfromFile(frag);
		compile(farg,varg);
	}
	
	
	public void destroy() {
		ARBShaderObjects.glDetachObjectARB(program, vertexShader);
		ARBShaderObjects.glDetachObjectARB(program, fragmentShader);
		ARBShaderObjects.glDeleteObjectARB(vertexShader);
		ARBShaderObjects.glDeleteObjectARB(fragmentShader);
		ARBShaderObjects.glDeleteObjectARB(program);
		program=0;
	}
	
	public void compile(String farg,String varg) {
		vertexShader=createShader(v, GL45.GL_VERTEX_SHADER,varg);
		fragmentShader=createShader(f, GL45.GL_FRAGMENT_SHADER,farg);
		
		program = ARBShaderObjects.glCreateProgramObjectARB();

		if (program == 0) {
			Main.log.severe("Shader Program creation failed!");
			return;
		}
		ARBShaderObjects.glAttachObjectARB(program, vertexShader);
		ARBShaderObjects.glAttachObjectARB(program, fragmentShader);

		ARBShaderObjects.glLinkProgramARB(program);
		if (ARBShaderObjects.glGetObjectParameteriARB(program,
				ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL45.GL_FALSE) {
			Main.log.severe(()->"Error glLink\n"+this.toString());
			program=0;
			return;
		}
		ARBShaderObjects.glValidateProgramARB(program);
		if (ARBShaderObjects.glGetObjectParameteriARB(program,
				ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL45.GL_FALSE) {
			Main.log.severe(()->"Error glValidate\n"+this.toString());
			program=0;
		}
	}
	
	private String stringfromFile(File f) {
		StringBuilder s = new StringBuilder();
		try(BufferedReader reader = new BufferedReader(new FileReader(f));){
			Iterator<String> i=reader.lines().iterator();
			while(i.hasNext()) {
				s.append(i.next()+"\n");
			}
		} catch (IOException e) {
			Main.log.severe("Failed to read File: "+f.getAbsolutePath());
		}
		return s.toString();
	}
	
	
	public void useUniform(String name,float... data) {
		int loc=GL45.glGetUniformLocation(this.program, name);
		if(loc!=-1)
			switch(data.length) {
			case 1:
				GL45.glUniform1f(loc, data[0]);
				break;
			case 2:
				GL45.glUniform2f(loc, data[0],data[1]);
				break;
			case 3:
				GL45.glUniform3f(loc, data[0],data[1],data[2]);
				break;
			case 4:
				GL45.glUniform4f(loc, data[0],data[1],data[2],data[3]);
				break;
			default:
				Main.log.warning(()->"Uniform "+name+" is too long "+data.length);
				break;
			}
	}
	
	public void useUniform(String name,Vector2f data) {
		useUniform(name,data.x,data.y);
	}
	
	public void useUniform(String name,int... data) {
		int loc=GL45.glGetUniformLocation(this.program, name);
		if(loc!=-1)
			GL45.glUniform1iv(loc, data);
	}
	
	public void useUniform(String name, Matrix4f data) {
		int loc=GL45.glGetUniformLocation(this.program, name);
		if(loc!=-1) {
			FloatBuffer fb=BufferUtils.createFloatBuffer(16);
			data.get(fb);
			GL45.glUniformMatrix4fv(loc,false, fb);
		}
	}
	
	@Override
	public String toString() {
		return ARBShaderObjects.glGetInfoLogARB(program,
				ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
	}
	
	private int createShader(String shaderSource, int shaderType,String arg) {
		int shader = 0;
		shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);
		if (shader == 0)
			return 0;
		ARBShaderObjects.glShaderSourceARB(shader, arg.replaceAll(":", "\n#define ")+"\n"+shaderSource);
		ARBShaderObjects.glCompileShaderARB(shader);
		if (ARBShaderObjects.glGetObjectParameteriARB(shader,
				ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL45.GL_FALSE) {
			String error = ARBShaderObjects.glGetInfoLogARB(shader, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB);
			Main.log.severe(()->"Failed to create Shader: "+error);
			System.exit(1);
		}
		return shader;
	}
	
	public void bind() {
		GlStateManager.bindShader(program);
	}
	
	public void unbind() {
		GlStateManager.unbindShader();
	}

}
