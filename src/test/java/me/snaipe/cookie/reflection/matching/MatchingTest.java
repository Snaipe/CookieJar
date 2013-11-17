package me.snaipe.cookie.reflection.matching;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;

import me.snaipe.cookie.reflection.Reflect;

import org.junit.Assert;
import org.junit.Test;

public class MatchingTest {

	@Test
	public void testMethodMatch() throws Throwable {
		Method sureMatch1 = A.class.getMethod("someMethod");
		Method sureMatch2 = A.class.getDeclaredMethod("someOtherMethod", Object.class);
		
		Collection<Method> match1 = Reflect.on(A.class).method().withName("someMethod").all();
		Collection<Method> match2 = Reflect.on(A.class).method().withModifiers(Modifier.PRIVATE).all();
		Collection<Method> match3 = Reflect.on(A.class).method().withAnnotations(SampleAnnotation.class).all();
		Collection<Method> match4 = Reflect.on(A.class).method().withModifiers(Modifier.PUBLIC).usingStrictSearch().all();
		Collection<Method> match5 = Reflect.on(A.class).method().ofReturnType(Integer.TYPE).all();
		Collection<Method> match6 = Reflect.on(A.class).method().throwing(IllegalArgumentException.class).all();
		
		Assert.assertEquals(match1.size(), 1);
		Assert.assertEquals(sureMatch1, match1.toArray()[0]);
		
		Assert.assertEquals(match2.size(), 1);
		Assert.assertEquals(sureMatch2, match2.toArray()[0]);
		
		Assert.assertEquals(match3.size(), 1);
		Assert.assertEquals(sureMatch1, match3.toArray()[0]);
		
		Assert.assertEquals(match4.size(), 0);
		
		Assert.assertEquals(match5.size(), 1);
		Assert.assertEquals(sureMatch2, match5.toArray()[0]);
		
		Assert.assertEquals(match6.size(), 1);
		Assert.assertEquals(sureMatch1, match6.toArray()[0]);
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface SampleAnnotation {}
	
	public static class A {
		
		@SampleAnnotation
		public static void someMethod() throws IllegalArgumentException {
			
		}
		
		@SuppressWarnings("unused")
		private int someOtherMethod(Object anArg) {
			return 0;
		}
	}
}
