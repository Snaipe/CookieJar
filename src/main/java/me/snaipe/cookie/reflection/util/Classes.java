package me.snaipe.cookie.reflection.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import me.snaipe.cookie.reflection.methods.MethodPrototype;

public final class Classes {
	private Classes() {}

	public static boolean hasAnnotations(AccessibleObject o, Collection<Class<? extends Annotation>> annotations) {
		for (Class<? extends Annotation> annotation : annotations) {
			if (!o.isAnnotationPresent(annotation)) {
				return false;
			}
		}
		return true;
	}
	
	public static Method[] getAllMethods(Class<?> clazz, boolean deep, boolean includeObject) {
		Map<MethodPrototype, Method> methods = new HashMap<MethodPrototype, Method>();
		
		if (clazz != null) {
			for (Method m : clazz.getDeclaredMethods()) {
				methods.put(new MethodPrototype(m), m);
			}
			
			Class<?> c = clazz.getSuperclass();
			while (deep && c != null && (includeObject || !Object.class.equals(c))) {
				for (Method m : c.getDeclaredMethods()) {
					MethodPrototype p = new MethodPrototype(m);
					if (!methods.containsKey(p)) {
						methods.put(p, m);
					}
				}
				c = c.getSuperclass();
			}
		}
		
		return methods.values().toArray(new Method[0]);
	}
	
	public static Field[] getAllFields(Class<?> clazz, boolean deep, boolean includeObject) {
		Set<Field> fields = new HashSet<Field>();
		
		if (clazz != null) {
			Collections.addAll(fields, clazz.getDeclaredFields());
			
			Class<?> c = clazz.getSuperclass();
			while (deep && c != null && (includeObject || !Object.class.equals(c))) {
				Collections.addAll(fields, c.getDeclaredFields());
				c = c.getSuperclass();
			}
		}
		
		return fields.toArray(new Field[0]);
	}
}
