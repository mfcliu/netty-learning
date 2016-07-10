package org.liujingyu.buffer_test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BufferTest {
	
	private static final Logger logger = LoggerFactory.getLogger(BufferTest.class.getName());
	@Test
	public void test1() throws IOException {
		RandomAccessFile f = new RandomAccessFile("c.dat", "rw");
		FileChannel inch = f.getChannel();
		
		ByteBuffer bf = ByteBuffer.allocate(48);
		int bytesRead = inch.read(bf);
		while(bytesRead != -1){
			bf.flip();
			while(bf.hasRemaining()){
				System.out.print((char) bf.getChar());
			}
			bf.clear();
			bytesRead = inch.read(bf);
		}
		f.close();
	}
	
	@Test
	public void test2() throws IOException {
		String test1 = "Hello, world !";
		String test2 = "Hello, world ";
		String test3 = "Helle, world ";
		
		ByteBuffer bf = ByteBuffer.allocate(48);
		ByteBuffer bf1 = ByteBuffer.allocate(48);
		ByteBuffer bf2 = ByteBuffer.allocate(48);
		ByteBuffer bf3 = ByteBuffer.allocate(48);
		ByteBuffer bf4 = ByteBuffer.allocate(48);
		logger.debug(String.format("new ====== capacity : %d , position : %d , limit : %d ", bf.capacity(),bf.position(),bf.limit()));
		bf.put(test1.getBytes());
		logger.debug(String.format("put ====== capacity : %d , position : %d , limit : %d ", bf.capacity(),bf.position(),bf.limit()));
		bf.flip();
		logger.debug(String.format("flip ====== capacity : %d , position : %d , limit : %d ", bf.capacity(),bf.position(),bf.limit()));
		bf.getChar(5);
		logger.debug(String.format("getchar(5) five times ====== capacity : %d , position : %d , limit : %d ", bf.capacity(),bf.position(),bf.limit()));
		bf.getChar();
		bf.getChar();
		bf.getChar();
		bf.getChar();
		bf.getChar();
		logger.debug(String.format("getchar() five times ====== capacity : %d , position : %d , limit : %d ", bf.capacity(),bf.position(),bf.limit()));
		bf.mark();
		logger.debug(String.format("mark ====== capacity : %d , position : %d , limit : %d ", bf.capacity(),bf.position(),bf.limit()));
		bf.getChar();
		bf.getChar();
		logger.debug(String.format("getchar() tow times ====== capacity : %d , position : %d , limit : %d ", bf.capacity(),bf.position(),bf.limit()));
		bf.reset();
		logger.debug(String.format("reset ====== capacity : %d , position : %d , limit : %d ", bf.capacity(),bf.position(),bf.limit()));
		bf.rewind();
		logger.debug(String.format("rewind ====== capacity : %d , position : %d , limit : %d ", bf.capacity(),bf.position(),bf.limit()));
		bf.getChar();
		bf.getChar();
		logger.debug(String.format("getchar() tow times ====== capacity : %d , position : %d , limit : %d ", bf.capacity(),bf.position(),bf.limit()));
		bf.compact();
		logger.debug(String.format("compact ====== capacity : %d , position : %d , limit : %d ", bf.capacity(),bf.position(),bf.limit()));
		bf.clear();
		logger.debug(String.format("clear ====== capacity : %d , position : %d , limit : %d ", bf.capacity(),bf.position(),bf.limit()));
		
		bf.put(test1.getBytes());
		bf1.put(test1.getBytes());
		bf2.put(test2.getBytes());
		bf3.put(test3.getBytes());
		bf4.putChar('H');
		bf4.putChar('e');
		bf4.putChar('l');
		bf4.putChar('l');
		bf4.putChar('o');
		bf4.putChar(',');
		bf4.putChar(' ');
		bf4.putChar('w');
		bf4.putChar('o');
		bf4.putChar('r');
		bf4.putChar('l');
		bf4.putChar('d');
		bf4.putChar('!');
		logger.debug("=======================================================");
		logger.debug(String.format("bf1.equals(bf) : %b , bf1.equals(bf2) : %b , bf1.equals(bf3) : %b , bf1.equals(bf4) : %b",bf1.equals(bf),bf1.equals(bf2),bf1.equals(bf3),bf1.equals(bf4)));
		logger.debug("=======================================================");
		logger.debug(String.format("bf1.compareTo(bf) : %d , bf1.compareTo(bf2) : %d , bf1.compareTo(bf3) : %d , bf1.compareTo(bf4) : %d",bf1.compareTo(bf),bf1.compareTo(bf2),bf1.compareTo(bf3),bf1.compareTo(bf4)));
		
		
	}
	
	@Test
	public void testDirect(){
		ByteBuffer bf = ByteBuffer.allocate(48);
		ByteBuffer bf1 = ByteBuffer.allocateDirect(48);
	}
	
}
