package me.snaipe.cookie.reflection.proxy.view;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import me.snaipe.cookie.reflection.proxy.InterfaceFitter;

public class ObjectView<T> extends InterfaceFitter<T> {
	
	private Map<Method, Accessor> accessors;
	private Map<Method, Mutator> mutators;

	public ObjectView(Class<T> clazz, Object target) {
		this(clazz, target.getClass(), target);
	}
	
	public <Q> ObjectView(Class<T> clazz, Class<? extends Q> targetClass, Q target) {
		super(clazz, target, targetClass.getDeclaredMethods(), clazz.getDeclaredMethods());
		
		this.accessors = new HashMap<Method, Accessor>();
		this.mutators = new HashMap<Method, Mutator>();
		
		for (Method method : clazz.getDeclaredMethods()) {
			me.snaipe.cookie.reflection.proxy.view.annotated.Accessor accessor = method.getAnnotation(me.snaipe.cookie.reflection.proxy.view.annotated.Accessor.class);
			me.snaipe.cookie.reflection.proxy.view.annotated.Mutator mutator   = method.getAnnotation(me.snaipe.cookie.reflection.proxy.view.annotated.Mutator.class);
			
			if (accessor != null && mutator == null) {
				String name = accessor.value();
				
				if (method.getParameterTypes().length != 0) {
					throw new IllegalArgumentException("On accessor to \"" + name + "\" : an accessor must not have any parameters.");
				}
				if (method.getReturnType().equals(Void.TYPE)) {
					throw new IllegalArgumentException("On accessor to \"" + name + "\" : an accessor must return a value.");
				}
				
				Class<?> type = method.getReturnType();
				
				Field field;
				try {
					field = clazz.getDeclaredField(name);
				} catch (NoSuchFieldException ex) {
					throw new IllegalArgumentException("On accessor to \"" + name + "\" : the referenced field does not exist.");
				}
				
				if (!field.getType().equals(type)) {
					throw new IllegalArgumentException("On accessor to \"" + name + "\" : the referenced field is of type " + field.getType().getName() + " instead of " + type.getName() + ".");
				}
				
				this.accessors.put(method, new Accessor(target, field));
			} else if (mutator != null && accessor == null) {
				String name = mutator.value();
				
				if (method.getParameterTypes().length != 1) {
					throw new IllegalArgumentException("On mutator to \"" + name + "\" : a mutator must have exactly one parameter.");
				}
				if (!method.getReturnType().equals(Void.TYPE)) {
					throw new IllegalArgumentException("On mutator to \"" + name + "\" : a mutator cannot return a value.");
				}
				
				Class<?> type = method.getParameterTypes()[0];
				
				Field field;
				try {
					field = clazz.getDeclaredField(name);
				} catch (NoSuchFieldException ex) {
					throw new IllegalArgumentException("On mutator to \"" + name + "\" : the referenced field does not exist.");
				}
				
				if (!field.getType().equals(type)) {
					throw new IllegalArgumentException("On mutator to \"" + name + "\" : the referenced field is of type " + field.getType().getName() + " instead of " + type.getName() + ".");
				}
				
				this.mutators.put(method, new Mutator(target, field));
			} else {
				throw new IllegalArgumentException("A method cannot be both an accessor and a mutator.");
			}
		}
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (method.isAnnotationPresent(me.snaipe.cookie.reflection.proxy.view.annotated.Accessor.class)) {
			Accessor accessor = this.accessors.get(method);
			if (accessor != null) {
				return accessor.call();
			}
		} else if (method.isAnnotationPresent(me.snaipe.cookie.reflection.proxy.view.annotated.Mutator.class)) {
			Mutator mutator = this.mutators.get(method);
			if (mutator != null) {
				mutator.call(args[0]);
				return null;
			}
		}
		return super.invoke(proxy, method, args);
	}

}
