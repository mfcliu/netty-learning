package org.liujingyu.unsafe_test;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import sun.misc.Unsafe;

public class TestObjectAddress {
	@SuppressWarnings("restriction")
	static final Unsafe unsafe = getUnsafe();
	static boolean is64bit = true;
	
	private static volatile Instrumentation instru;
	 
    public static void premain(String args, Instrumentation inst) {
        instru = inst;
    }

	private static Unsafe getUnsafe() {
		try {
			Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
			theUnsafe.setAccessible(true);
			return (Unsafe) theUnsafe.get(null);
		} catch (Exception e) {
			throw new AssertionError(e);
		}
	}

	public static void main(String[] args) {
		if(!System.getProperty("sun.arch.data.model").equals("64")){
			is64bit = false;
		}
		if(!is64bit){
			System.out.println("jvm type not support,only support x64 !");
		}
		TestObjectAddress t = new TestObjectAddress();
		Object[] oa = new Object[1];
		oa[0] = t;
		long baseOffset = unsafe.arrayBaseOffset(Object[].class);
		long addressOfObject = unsafe.getLong(oa,baseOffset);
		//long addressOfObject = unsafe.getLong(oa,1);
		
		System.out.println("address : " + addressOfObject);
		System.out.println("hash code : " + System.identityHashCode(t));
		
		
		String password = new String("123!@#qwe");
		String fake = new String(password.replaceAll(".", "?"));
		System.out.println(password);
		System.out.println(fake);
		unsafe.copyMemory(fake, 0L,null, getAddress(password),getSizeOf(password));
		System.out.println(password);
		System.out.println(fake);
		
		
	}
	
	static long getAddress(Object o){
		Object[] oa = new Object[1];
		oa[0] = o;
		long baseOffset = unsafe.arrayBaseOffset(Object[].class);
		long addressOfObject = unsafe.getLong(oa,baseOffset);
		return addressOfObject;
	}
	
	public static long getSizeOf(Object object) {
        if (instru == null) {
            throw new IllegalStateException("Instrumentation is null");
        }
        return instru.getObjectSize(object);
    }
	
	
	
}
