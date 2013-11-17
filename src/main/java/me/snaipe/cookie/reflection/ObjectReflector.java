package me.snaipe.cookie.reflection;

import me.snaipe.cookie.reflection.proxy.InterfaceFitter;
import me.snaipe.cookie.reflection.proxy.view.ObjectView;

public class ObjectReflector extends ClassReflector {

	private Object object;

	protected ObjectReflector(Object o) {
		super(o.getClass());
		this.object = o;
	}
	
	public <T> T fit(Class<T> clazz) {
		if (!clazz.isInterface()) {
			throw new IllegalArgumentException("Parameter must be an interface.");
		}
		return new InterfaceFitter<T>(clazz, object).build();
	}

	public <T> T view(Class<T> clazz) {
		if (!clazz.isInterface()) {
			throw new IllegalArgumentException("Parameter must be an interface.");
		}
		return new ObjectView<T>(clazz, object).build();
	}
}
