package me.snaipe.cookie.reflection.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import me.snaipe.cookie.reflection.methods.MethodPrototype;

public class InterfaceFitter<T> implements InvocationHandler {
	
	private Class<T> clazz;
	private Object target;
	
	private Map<MethodPrototype, Method> targetMethods;

	public InterfaceFitter(Class<T> clazz, Object target) {
		this(clazz, target, target.getClass().getMethods(), clazz.getMethods());
	}
	
	protected InterfaceFitter(Class<T> clazz, Object target, Method[] targetMethods, Method[] interfaceMethods) {
		this.clazz = clazz;
		this.target = target;
		
		if (targetMethods.length < interfaceMethods.length) {
			throw new IllegalArgumentException("Object does not implement all methods from the interface.");
		}
		
		this.targetMethods = new HashMap<MethodPrototype, Method>();
		
		Set<MethodPrototype> targetPrototypes = new HashSet<>();
		Set<MethodPrototype> interfacePrototypes = new HashSet<>();
		
		for (Method m : interfaceMethods) {
			interfacePrototypes.add(new MethodPrototype(m));
		}
		
		for (Method m : targetMethods) {
			MethodPrototype prototype = new MethodPrototype(m);
			targetPrototypes.add(prototype);
			m.setAccessible(true);
			this.targetMethods.put(prototype, m);
		}
		
		if (!targetPrototypes.containsAll(interfacePrototypes)) {
			throw new IllegalArgumentException("Object does not implement all methods from the interface.");
		}
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Method targetMethod = this.targetMethods.get(new MethodPrototype(method));
		try {
			return targetMethod.invoke(target, args);
		} catch (InvocationTargetException ex) {
			throw ex.getCause();
		} catch (Throwable t) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public T build() {
		return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] { clazz }, this);
	}
}
