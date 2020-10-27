package me.engine.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import me.engine.Main;

public class FileUtils {
	
	private FileUtils() {throw new UnsupportedOperationException();}

	public static File getFilefromID(String id) {
		return new File(Main.dir.getAbsolutePath()+"\\Assets\\"+id.replace(".", "\\").replace(":", "."));
	}
	
	//TODO: Do that conversion better
	public static String getIDfromFile(File f) {
		return f.getPath().split("Assets")[1].replace(".", ":")
				.replace("\\", ".").substring(1);
	}
	
	/**
	 * Gets a Files Content
	 * @param f The File to read from
	 * @return The Content from the File
	 */
	public static String stringfromFile(File f) {
		StringBuilder s = new StringBuilder();
		try(BufferedReader reader = new BufferedReader(new FileReader(f));){
			Iterator<String> i=reader.lines().iterator();
			while(i.hasNext()) {
				s.append(i.next());
				s.append("\n");
			}
		} catch (IOException e) {
			Main.log.severe("Failed to read File: "+f.getAbsolutePath());
		}
		return s.toString();
	}
	
	/**
	 * Gets a Files Content
	 * @param id The String ID of the File to read from
	 * The String ID consists of the Path of the Texture in the Textures folder with the \ replaced with . and the . replaced with :
	 * e.g. Assets\Textures\Test\testpng.png becomes Assets.Textures.Test.testpng:png
	 * @return The Content of the File
	 */
	public static String stringfromFile(String id) {
		return stringfromFile(getFilefromID(id));
	}
}
