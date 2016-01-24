package org.liujingyu.filechannel_test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileClient {
	private FileChannel fileChannel;
	private MappedByteBuffer mappedByteBuffer;
	private File file;

	private static int mapedFileSizeCommitLog = 1024 * 1024 * 1024;

	private static final String fileName = "D:\\temp\\testfilecache\\a.dat";
	private static final String fileName2 = "D:\\temp\\testfilecache\\b.dat";

	private static final Logger logger = LoggerFactory.getLogger(FileClient.class.getName());

	public static void main(String[] args) throws IOException {
		FileClient fclient = new FileClient();
		fclient.start();
		fclient.start2();
	}

	@SuppressWarnings("resource")
	public void start() throws IOException {
		file = new File(fileName);
		String testString = "hello";
		boolean ok = false;
		try {
			this.fileChannel = new RandomAccessFile(this.file, "rw").getChannel();
			this.mappedByteBuffer = this.fileChannel.map(MapMode.READ_WRITE, 0, mapedFileSizeCommitLog);
			int curPos = 0;
			long startTime = System.currentTimeMillis();
			for (int i = 0; i < 1000000; i++) {
				this.mappedByteBuffer.position(curPos);
				this.mappedByteBuffer.put(testString.getBytes());
				curPos += 6;
			}
			this.mappedByteBuffer.force();
			this.fileChannel.close();
			long stopTime = System.currentTimeMillis();
			logger.debug("time :  " + (stopTime - startTime));
			ok = true;
		} catch (FileNotFoundException e) {
			logger.error("create file channel " + fileName + " Failed. ", e);
			throw e;
		} catch (IOException e) {
			logger.error("map file " + fileName + " Failed. ", e);
			throw e;
		} finally {
			if (!ok && this.fileChannel != null) {
				this.fileChannel.close();
			}
		}
	}

	public void start2() throws IOException {
		FileOutputStream fos = new FileOutputStream(fileName2);
		String testString = "hello";

		long startTime = System.currentTimeMillis();
		for (int i = 0; i < 1000000; i++) {
			fos.write(testString.getBytes());
		}
		fos.close();
		long stopTime = System.currentTimeMillis();
		logger.debug("time2 :  " + (stopTime - startTime));

	}

}
