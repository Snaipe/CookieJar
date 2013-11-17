package me.snaipe.cookie.reflection.proxy.view;

import java.lang.reflect.Field;

final class Accessor {
	
	private Object instance;
	private Field field;

	public Accessor(Object instance, Field field) {
		this.instance = instance;
		this.field = field;
		this.field.setAccessible(true);
	}
	
	public Object call() throws IllegalArgumentException, IllegalAccessException {
		return field.get(instance);
	}
}
