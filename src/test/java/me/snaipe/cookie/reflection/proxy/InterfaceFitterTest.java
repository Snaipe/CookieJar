package me.snaipe.cookie.reflection.proxy;

import java.lang.reflect.Method;

import me.snaipe.cookie.reflection.Reflect;

import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("unused")
public class InterfaceFitterTest {
	
	@Test
	public void testFitting() {
		
		Object o = new TestClass();
		
		Fitting fitted1 = Reflect.on(o).fit(Fitting.class);
		
		fitted1.a();
		Assert.assertEquals(TestClass.a, 1);
		
		Fitting2 fitted2 = Reflect.on(o).fit(Fitting2.class);
		
		fitted2.a();
		Assert.assertEquals(TestClass.a, 2);
		Assert.assertEquals(fitted2.b(), 42);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testNoPrivateAccess() {
		NoPrivateAccess fit = Reflect.on(new TestClass()).fit(NoPrivateAccess.class);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testIncompleteImplementation() {
		OverlappingInterface fit = Reflect.on(new TestClass()).fit(OverlappingInterface.class);
	}
	
	public static interface Fitting {
		public void a();
	}
	
	public static interface Fitting2 {
		public void a();
		public int b();
	}
	
	public static interface NoPrivateAccess {
		public void c();
	}
	
	public static interface OverlappingInterface {
		public void a();
		public int b();
		public void someNonImplementedMethod();
	}
	
	public static class TestClass {
		
		public static int a = 0;
		
		public void a() {
			a++;
		}
		
		public int b() {
			return 42;
		}
		
		private void c() {
			
		}
	}
}
