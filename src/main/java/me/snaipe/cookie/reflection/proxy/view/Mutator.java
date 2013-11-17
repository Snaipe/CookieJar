package me.snaipe.cookie.reflection.proxy.view;

import java.lang.reflect.Field;

final class Mutator {
	
	private Object instance;
	private Field field;

	public Mutator(Object instance, Field field) {
		this.instance = instance;
		this.field = field;
		this.field.setAccessible(true);
	}
	
	public void call(Object value) throws IllegalArgumentException, IllegalAccessException {
		field.set(instance, value);
	}
}
