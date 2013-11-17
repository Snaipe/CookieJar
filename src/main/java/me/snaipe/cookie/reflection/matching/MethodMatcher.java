package me.snaipe.cookie.reflection.matching;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import me.snaipe.cookie.reflection.util.Classes;
import me.snaipe.cookie.reflection.util.Methods;

public class MethodMatcher {
	
	private final static int ALL_METHODS_MASK = Modifier.ABSTRACT | Modifier.FINAL | Modifier.NATIVE | Modifier.PRIVATE | Modifier.PROTECTED | Modifier.PUBLIC | Modifier.STATIC | Modifier.SYNCHRONIZED | Modifier.STRICT;

	private Class<?> clazz;
	private boolean strict = false;
	private int mask = ALL_METHODS_MASK;
	private String nameRegex;
	
	private Set<Class<? extends Annotation>> annotations = new HashSet<Class<? extends Annotation>>();
	private Set<Class<? extends Throwable>> throwables = new HashSet<Class<? extends Throwable>>();
	private Class<?> returnType;

	private boolean deep;
	private boolean includeObject;

	private Method[] methods;
	
	public MethodMatcher(Class<?> clazz) {
		this.clazz = clazz;
	}
	
	public MethodMatcher withModifiers(int mask) {
		this.mask = mask;
		this.methods = null;
		return this;
	}
	
	public MethodMatcher usingStrictSearch() {
		this.strict = true;
		this.methods = null;
		return this;
	}
	
	@SafeVarargs
	public final MethodMatcher withAnnotations(Class<? extends Annotation>... annotations) {
		this.annotations.clear();
		Collections.addAll(this.annotations, annotations);
		this.methods = null;
		return this;
	}
	
	public MethodMatcher withName(String regex) {
		this.nameRegex = regex;
		this.methods = null;
		return this;
	}
	
	public MethodMatcher ofReturnType(Class<?> returnType) {
		this.returnType = returnType;
		this.methods = null;
		return this;
	}
	
	@SafeVarargs
	public final MethodMatcher throwing(Class<? extends Throwable>... throwables) {
		this.throwables.clear();
		Collections.addAll(this.throwables, throwables);
		this.methods = null;
		return this;
	}
	
	public MethodMatcher notSearchingSuperclasses() {
		this.deep = false;
		this.methods = null;
		return this;
	}
	
	public MethodMatcher includingObject() {
		this.includeObject = true;
		this.methods = null;
		return this;
	}
	
	public Method one() {
		Iterator<Method> it = all().iterator();
		if (it.hasNext()) {
			return all().iterator().next();
		}
		return null;
	}

	public Collection<Method> all() {
		populateMethods();
		
		Set<Method> methods = new HashSet<Method>();
		
		for (Method m : this.methods) {
			addMatchingMethod(m, methods);
		}
		
		return Collections.unmodifiableCollection(methods);
	}
	
	private void populateMethods() {
		if (this.methods == null) {
			this.methods = Classes.getAllMethods(clazz, deep, includeObject);
		}
	}

	private void addMatchingMethod(Method method, Set<Method> methods) {
		if (strict ? (method.getModifiers() ^ mask) == 0 : (method.getModifiers() & mask) != 0) {
			if (annotations.isEmpty() || Classes.hasAnnotations(method, annotations)) {
				if (throwables.isEmpty() || Methods.isThrowing(method, throwables)) {
					if (returnType == null || method.getReturnType().equals(returnType)) {
						if (nameRegex == null || method.getName().matches(nameRegex)) {
							methods.add(method);
						}
					}
				}
			}
		}
	}
}
