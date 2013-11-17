package me.snaipe.cookie.reflection;

public final class Reflect {
	private Reflect() {}

	public static ObjectReflector on(Object o) {
		return new ObjectReflector(o);
	}
	
	public static ClassReflector on(Class<?> c) {
		return new ClassReflector(c);
	}
}
