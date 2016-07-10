package org.liujingyu.test.compile;

import java.net.URI;

import javax.tools.SimpleJavaFileObject;

public class CompileTest {
	
	public static Class<?> compile(String name,String content){
		//JavaCompiler = compile q 
		return null;
	}
	
	public static void main(String[] args) {
		Class<?> cls = compile("com.teasp.compile.Test",  
	            "package com.teasp.compile; public class Test{ public static void main(String[] args){System.out.println(\"compile test.\");} }");
		
	}
	
	
	private static class StrSrcJavaObject extends SimpleJavaFileObject{

		protected StrSrcJavaObject(URI uri, Kind kind) {
			super(uri, kind);
			// TODO Auto-generated constructor stub
		}
			
	}
}
