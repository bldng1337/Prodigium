package me.engine.Utils;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

public class LoggerOutputStream extends OutputStream{
	
	private final ByteArrayOutputStream baos = new ByteArrayOutputStream(1000);
	private Logger Log;
	
	public LoggerOutputStream(Logger l) {
		Log=l;
	}
	
	@Override
	public void write(byte[] bl,int off,int len) {
		for(byte b:bl) {
			if (b == '\n') {
				String line = baos.toString();
				baos.reset();
				Log.info(line);
			}else {
				baos.write(b);
			}
		}
	}
	
	@Override
	public void write(int b) {
		if (b == '\n') {
			String line = baos.toString();
			baos.reset();
			Log.info(line);
		}else {
			baos.write(b);
		}
	}
	
}
