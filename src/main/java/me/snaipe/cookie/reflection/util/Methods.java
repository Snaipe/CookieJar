package me.snaipe.cookie.reflection.util;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class Methods {
	private Methods() {}

	public static boolean isThrowing(Method m, Collection<Class<? extends Throwable>> throwables) {
		Set<Class<?>> set = new HashSet<Class<?>>();
		Collections.addAll(set, m.getExceptionTypes());
		
		for (Class<? extends Throwable> throwable : throwables) {
			if (!set.contains(throwable)) {
				return false;
			}
		}
		return true;
	}
}
