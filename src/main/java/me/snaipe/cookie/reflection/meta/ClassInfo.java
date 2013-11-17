package me.snaipe.cookie.reflection.meta;

import java.util.Map;

import me.snaipe.cookie.reflection.methods.MethodPrototype;

public class ClassInfo {
	
	private Class<?> clazz;
	private Map<MethodPrototype, MethodInfo> methods;
	private Map<String, FieldInfo> fields;

}
