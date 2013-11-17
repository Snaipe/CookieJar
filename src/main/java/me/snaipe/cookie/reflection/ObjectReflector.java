package me.snaipe.cookie.reflection;

import me.snaipe.cookie.reflection.proxy.InterfaceFitter;
import me.snaipe.cookie.reflection.proxy.view.ObjectView;

public class ObjectReflector extends ClassReflector {

	private Object object;

	protected ObjectReflector(Object o) {
		super(o.getClass());
		this.object = o;
	}
	
	/**
	 * Creates a new {@link java.lang.reflect.Proxy proxy} instance of the target, fitting the passed interface.
	 * The passed interface may define all of the target's public methods, or a subset of these methods.
	 * If a method exists in the interface but is not a visible method of the target instance nor its superclasses,
	 * the fitting will fail with an {@link IllegalArgumentException}.
	 * 
	 * <p>
	 * This method only works on the visible set of the target instance -- it may not be used to access any
	 * private fields or methods. To achieve these kind of results, take a look into making {@link #view(Class) object views}
	 * @param clazz the fitting interface
	 * @return a fitted view of the target.
	 */
	public <T> T fit(Class<T> clazz) {
		if (!clazz.isInterface()) {
			throw new IllegalArgumentException("Parameter must be an interface.");
		}
		return new InterfaceFitter<T>(clazz, object).build();
	}

	/**
	 * Creates a new {@link java.lang.reflect.Proxy proxy} instance of the target, with the passed interface
	 * acting as a view of a subset of the class.
	 * The passed interface may define all of the target's methods, or a subset of these methods.
	 * Any non-visible methods will be set accessible inside the view -- one may use an object view to access
	 * these methods where {@link #fit(Class) interface fitting} would fail to do so.
	 * 
	 * <p>
	 * By annotating a method with the {@link me.snaipe.cookie.reflect.proxy.view.annotated.Accessor @Accessor} 
	 * or {@link me.snaipe.cookie.reflect.proxy.view.annotated.Mutator @Mutator} annotation, one may use said
	 * method to get or set the value of any field respectively.
	 * 
	 * <p>
	 * Methods annotated with {@link me.snaipe.cookie.reflect.proxy.view.annotated.Accessor @Accessor}
	 * must not take any parameter and its return type must match the field's.
	 * Methods annotated with {@link me.snaipe.cookie.reflect.proxy.view.annotated.Mutator @Mutator}
	 * must not return anything and must take only one parameter, which type must match the field's.
	 * 
	 * <p>
	 * If a method exists in the interface but is not a method of the target instance nor its superclasses,
	 * the view creation will fail with an {@link IllegalArgumentException}.
	 * 
	 * @param clazz the view interface
	 * @return a view of the target.
	 */
	public <T> T view(Class<T> clazz) {
		if (!clazz.isInterface()) {
			throw new IllegalArgumentException("Parameter must be an interface.");
		}
		return new ObjectView<T>(clazz, object).build();
	}
}
