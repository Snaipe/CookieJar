package me.snaipe.cookie.reflection.methods;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import me.snaipe.cookie.misc.EqualsBuilder;
import me.snaipe.cookie.misc.HashCodeBuilder;

public final class MethodPrototype {
	
	private Set<Class<?>> parameters;
	private Class<?> returnType;
	private String name;
	
	private int hash;

	public MethodPrototype(Method method) {
		this.returnType = method.getReturnType();
		this.name = method.getName();
		this.parameters = new HashSet<Class<?>>();
		Collections.addAll(this.parameters, method.getParameterTypes());
		
		this.hash = new HashCodeBuilder()
				.append(this.name)
				.append(this.returnType)
				.append(this.parameters)
				.hashCode();
	}
	
	public int hashCode() {
		return this.hash;
	}
	
	public boolean equals(Object o) {
		if (o instanceof MethodPrototype) {
			MethodPrototype other = (MethodPrototype) o;
			
			return new EqualsBuilder()
					.append(this.name, other.name)
					.append(this.returnType, other.returnType)
					.append(this.parameters, other.parameters)
					.equals();
		}
		return false;
	}
}
