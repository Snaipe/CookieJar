package me.snaipe.cookie.reflection;

import me.snaipe.cookie.reflection.matching.FieldMatcher;
import me.snaipe.cookie.reflection.matching.MethodMatcher;

public class ClassReflector {

	private Class<?> clazz;

	protected ClassReflector(Class<?> clazz) {
		if (clazz == null) {
			throw new IllegalArgumentException("Cannot reflect on a null class.");
		}
		this.clazz = clazz;
	}
	
	public ClassReflector superClass() {
		return new ClassReflector(clazz.getSuperclass());
	}
	
	public MethodMatcher method() {
		return new MethodMatcher(this.clazz);
	}
	
	public FieldMatcher field() {
		return new FieldMatcher(this.clazz);
	}

}
