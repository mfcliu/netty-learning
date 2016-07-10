package org.liujingyu.unsafe_test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import junit.framework.Assert;
import sun.misc.Unsafe;

public class HotspotSizeof {
	public static int OBJ_BASIC_LEN = 8 * 8;
	public static int ARRAY_BASIC_LEN = 12 * 8;
	public static int OBJ_REF_LEN = 4 * 8;
	public static int ALIGN = 8 * 8;

	private static Unsafe UNSAFE;

	static {
		try {
			Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
			theUnsafe.setAccessible(true);
			UNSAFE = (Unsafe) theUnsafe.get(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private enum PType {
		PBool(8), PByte(8), PChar(16), PShort(16), PInt(32), PFloat(32), PLong(64), PDouble(64);
		private int bits;

		private PType(int bits) {
			this.bits = bits;
		}

		public int getBits() {
			return this.bits;
		}
	}

	private static int getObjBits(Object obj, Class<?> clazz, boolean isPapa) {
		int bits = 0;
		if (null == obj) {
			return bits;
		}

		bits += OBJ_BASIC_LEN;
		if (isPapa) {
			bits = 0;
		}

		Field[] fields = clazz.getDeclaredFields();
		if (fields != null) {
			for (Field field : fields) {
				if (Modifier.isStatic(field.getModifiers())) {// 静态变量不占大小
					System.out.println("===== static field : " + field.getName());
					continue;
				}
				Class<?> c = field.getType();
				if (c == boolean.class) {
					bits += PType.PBool.getBits();
				} else if (c == byte.class) {
					bits += PType.PByte.getBits();
				} else if (c == char.class) {
					bits += PType.PChar.getBits();
				} else if (c == short.class) {
					bits += PType.PShort.getBits();
				} else if (c == int.class) {
					bits += PType.PInt.getBits();
				} else if (c == float.class) {
					bits += PType.PFloat.getBits();
				} else if (c == long.class) {
					bits += PType.PLong.getBits();
				} else if (c == double.class) {
					bits += PType.PDouble.getBits();
				} else if (c == void.class) {
					// nothing
				} else if (c.isArray()) {
					Object o = UNSAFE.getObject(obj, UNSAFE.objectFieldOffset(field));
					bits += OBJ_REF_LEN;
					if (o != null) {
						try {
							bits += bitsofArray(o);
						} catch (Exception e) {
							throw new RuntimeException(e);
						}

					}
				} else {// 普通对象
					Object o = UNSAFE.getObject(obj, UNSAFE.objectFieldOffset(field));
					bits += OBJ_REF_LEN;
					if (o != null) {
						try {
							bits += bitsof(o);
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					}
				}
			}
		}

		Class<?> papa = clazz.getSuperclass();
		if (papa != null) {
			bits += getObjBits(obj, papa, true);
		}

		// 补齐，当计算父类属性的时候，因为是对同一个对象进行计算，所以不需要补齐
		// 一个对象只做一次补齐

		if (false == isPapa) {
			if (bits % ALIGN > 0) {
				bits += (ALIGN - (bits % ALIGN));
			}
		}

		return bits;
	}

	// 计算arr在内存中的地址
	private static int bitsofArray(Object arr) {
		int bits = 0;
		if (arr == null) {
			return bits;
		}
		bits = ARRAY_BASIC_LEN;

		Class<?> c = arr.getClass();
		if (c.isArray() == false) {
			throw new RuntimeException("Must be an array");
		}
		if (c == boolean[].class) {
			bits += PType.PBool.getBits() * ((boolean[]) arr).length;
		}else if(c==byte[].class){
			bits += PType.PByte.getBits() * ((byte[]) arr).length;
		}else if(c==char[].class){
			bits += PType.PChar.getBits() * ((char[]) arr).length;
		}else if(c==short[].class){
			bits += PType.PShort.getBits() * ((short[]) arr).length;
		}else if(c==int[].class){
			bits += PType.PInt.getBits() * ((int[]) arr).length;
		}else if(c==float[].class){
			bits += PType.PFloat.getBits() * ((float[]) arr).length;
		}else if(c==long[].class){
			bits += PType.PLong.getBits() * ((long[]) arr).length;
		}else if(c==double[].class){
			bits += PType.PDouble.getBits() * ((double[]) arr).length;
		}else {
			Object[] os = (Object[]) arr;
			for(Object o:os){
				bits+=OBJ_REF_LEN +bitsof(o);
			}
		}
		
		//补齐
		if(bits%ALIGN > 0){
			bits += (ALIGN -bits%ALIGN);
		}
		return bits;
	}

	/*
	 * 计算obj对象在虚拟机中所占的内存，单位为bit 
	 */
	private static int bitsof(Object obj) {
		if(obj == null){
			return 0;
		}
		if(obj.getClass().isArray()){
			return bitsofArray(obj);
		}
		return getObjBits(obj, obj.getClass(), false);
	}
	
	/*
	 * 计算obj对象在虚拟机中所占的内存，单位为byte
	 */
	public static int sizeof(Object obj){
		return bitsof(obj)/8;
	}
	private static void runGC(){
		for(int r=0;r<=4;r++) _runGC();
	}
	private static void _runGC(){
		long usedMem1 = usedMemory(),usedMem2=Long.MAX_VALUE;
		for(int i = 0;(usedMem1 < usedMem2) && (i<500);++i){
			Runtime.getRuntime().runFinalization();
			Runtime.getRuntime().gc();
			Thread.yield();
			usedMem2 = usedMem1;
			usedMem1 = usedMemory();
		}
	}
	
	private static long usedMemory(){
		return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
	}
	
	private static long determineObjSize(Class<?> cls) throws Exception{
		runGC();
		usedMemory();
		final int count = 100000;
		Object[] objects = new Object[count];
		
		long heap1 = 0;
		for(int i= -1;i<count;i++){
			Object object = null;
			object = cls.newInstance();
			if(i>=0){
				objects[i] = object;
			}else{
				object = null;
				runGC();
				heap1 = usedMemory();
			}
		}
		runGC();
		long heap2 = usedMemory();
		
		final int size = Math.round(((float)(heap2-heap1))/count);
		System.out.println("before heap : "+heap1);
		System.out.println("after heap : "+heap2);
		System.out.println("heap delta : "+(heap2 - heap1) + "{"+ objects[0].getClass() + "} size = " + size);
		for(int i = 0;i<count;i++){
			objects[i] = null;
		}
		objects = null;
		return size;
		
	}
	
	public static void main(String[] args) {
		HotspotSizeof hs = new HotspotSizeof();
		hs.test();
	}
	
	@SuppressWarnings("deprecation")
	public void test(){
		try{
			long objsize = determineObjSize(Obj4SizeofTest.class);
			long objsize1 = sizeof(new Obj4SizeofTest());
			System.out.println("======== objsize : "+objsize);
			System.out.println("======== objsize1 : "+objsize1);
			//Assert.assertEquals(determineObjSize(Obj4SizeofTest.class), sizeof(new Obj4SizeofTest()));
		}catch (Exception e){
			throw new RuntimeException(e);
		}
	}
}
