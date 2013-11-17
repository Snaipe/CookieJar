package me.snaipe.cookie.reflection.methods;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import me.snaipe.cookie.reflection.ReflectException;

public class MethodAccessor<T> {
	
	protected Object instance;
	protected Method method;
	
	public MethodAccessor(Method method) {
		this.method = method;
	}
	
	public MethodAccessor(Method method, Object instance) {
		this.method = method;
		this.instance = instance;
	}
	
	@SuppressWarnings("unchecked")
	public T call(Object... params) {
		try {
			return (T) method.invoke(instance, params);
		} catch (InvocationTargetException e) {
			Throwable t = e.getCause();
			if (t instanceof RuntimeException) {
				throw (RuntimeException) t;
			} else if (t instanceof Error) {
				throw (Error) t;
			}
			throw new ReflectException(t);
		} catch (Exception e) {
			throw new ReflectException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public T callException(Object... params) throws Throwable {
		try {
			return (T) method.invoke(instance, params);
		} catch (InvocationTargetException e) {
			Throwable t = e.getCause();
			if (t instanceof RuntimeException) {
				throw (RuntimeException) t;
			} else if (t instanceof Error) {
				throw (Error) t;
			}
			throw t;
		} catch (Exception e) {
			throw new ReflectException(e);
		}
	}
}
