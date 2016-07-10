package org.liujingyu.unsafe_test;

import java.lang.reflect.Field;

import sun.misc.Unsafe;

public class TestUnsafe {
	byte[] b = new byte[3];
	private static Unsafe unsafe;
	public static void main(String[] args) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field f = Unsafe.class.getDeclaredField("theUnsafe");
		f.setAccessible(true);
		unsafe = (Unsafe)f.get(null);
		TestUnsafe t = new TestUnsafe();
		t.init();
		
	}
	
	public void init(){
		b[0] = 1;
		b[1] = 2;
		b[2] = 3;
		
		Integer i = new Integer(3);
		
		System.out.print(String.format("%02x ", unsafe.getByte(b, (long)1)));
		System.out.print(String.format("%02x ", unsafe.getByte(b, (long)2)));
		System.out.print(String.format("%02x ", unsafe.getByte(b, (long)3)));
		System.out.println();
		
		System.out.print(String.format("%02x ", unsafe.getByte(i, (long)10)));
		System.out.print(String.format("%02x ", unsafe.getByte(i, (long)15)));
		System.out.print(String.format("%02x ", unsafe.getByte(i, (long)20)));
		
	}
	
	public void replacePass(){
		String password = new String("123!@#qwe");
		String fake = new String(password.replaceAll(".", "?"));
		System.out.println(password);
		System.out.println(fake);
		/*unsafe.copyMemory(fake, 0L,null, toAddress(password),sizeOf(password));
		unsafe.*/
	}
	
}
