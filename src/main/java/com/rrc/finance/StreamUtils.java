package com.rrc.finance;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * stream工具类 ,copy from spring
 * @author doujinlong
 *
 */
public abstract class StreamUtils {
	public static final int BUFFER_SIZE = 4096;
	public static int copy(InputStream in, OutputStream out) throws IOException {
		int byteCount = 0;
		byte[] buffer = new byte[BUFFER_SIZE];
		int bytesRead = -1;
		while ((bytesRead = in.read(buffer)) != -1) {
			out.write(buffer, 0, bytesRead);
			byteCount += bytesRead;
		}
		out.flush();
		return byteCount;
	}}
