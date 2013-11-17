package me.snaipe.cookie.reflection;

/**
 * The main reflection util class.
 * 
 */
public final class Reflect {
	private Reflect() {}

	/**
	 * Builds a new reflector working on the passed instance. 
	 * Calling subsequent methods on the reflector will target said instance and it's class.
	 * @param o the target instance
	 * @return the object reflector
	 */
	public static ObjectReflector on(Object o) {
		return new ObjectReflector(o);
	}
	
	/**
	 * Builds a new reflector working on the passed class. 
	 * Calling subsequent methods on the reflector will target said class.
	 * @param c the targeted class
	 * @return the class reflector
	 */
	public static ClassReflector on(Class<?> c) {
		return new ClassReflector(c);
	}
}
