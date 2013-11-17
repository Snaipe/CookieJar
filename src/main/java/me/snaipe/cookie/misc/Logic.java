package me.snaipe.cookie.misc;

public final class Logic {
	private Logic() {}
	
	public static boolean equals(byte[] a1, byte[] a2) {
		if (a1.length != a2.length) {
			return false;
		} else {
			boolean result = true;
			for (int i = 0; result && i < a1.length; ++i) {
				result = result && a1[i] == a2[i];
			}
			return result;
		}
	}
	
	public static boolean equals(char[] a1, char[] a2) {
		if (a1.length != a2.length) {
			return false;
		} else {
			boolean result = true;
			for (int i = 0; result && i < a1.length; ++i) {
				result = result && a1[i] == a2[i];
			}
			return result;
		}
	}
	
	public static boolean equals(short[] a1, short[] a2) {
		if (a1.length != a2.length) {
			return false;
		} else {
			boolean result = true;
			for (int i = 0; result && i < a1.length; ++i) {
				result = result && a1[i] == a2[i];
			}
			return result;
		}
	}
	
	public static boolean equals(int[] a1, int[] a2) {
		if (a1.length != a2.length) {
			return false;
		} else {
			boolean result = true;
			for (int i = 0; result && i < a1.length; ++i) {
				result = result && a1[i] == a2[i];
			}
			return result;
		}
	}
	
	public static boolean equals(long[] a1, long[] a2) {
		if (a1.length != a2.length) {
			return false;
		} else {
			boolean result = true;
			for (int i = 0; result && i < a1.length; ++i) {
				result = result && a1[i] == a2[i];
			}
			return result;
		}
	}
	
	public static boolean equals(float[] a1, float[] a2) {
		if (a1.length != a2.length) {
			return false;
		} else {
			boolean result = true;
			for (int i = 0; result && i < a1.length; ++i) {
				result = result && a1[i] == a2[i];
			}
			return result;
		}
	}
	
	public static boolean equals(double[] a1, double[] a2) {
		if (a1.length != a2.length) {
			return false;
		} else {
			boolean result = true;
			for (int i = 0; result && i < a1.length; ++i) {
				result = result && a1[i] == a2[i];
			}
			return result;
		}
	}
	
	public static boolean equals(boolean[] a1, boolean[] a2) {
		if (a1.length != a2.length) {
			return false;
		} else {
			boolean result = true;
			for (int i = 0; result && i < a1.length; ++i) {
				result = result && a1[i] == a2[i];
			}
			return result;
		}
	}
	
	public static boolean equals(Object[] a1, Object[] a2) {
		if (a1.length != a2.length) {
			return false;
		} else {
			boolean result = true;
			for (int i = 0; result && i < a1.length; ++i) {
				result = result && equals(a1[i], a2[i]);
			}
			return result;
		}
	}
	
	public static boolean equals(Object o1, Object o2) {
		if (o1 == null) {
			return o2 == null;
		} else if (o1.getClass().isArray() && o1.getClass().equals(o2.getClass())) {
			if (o1 instanceof byte[]) {
				return equals((byte[]) o1, (byte[]) o2);
			} else if (o1 instanceof char[]) {
				return equals((char[]) o1, (char[]) o2);
			} else if (o1 instanceof short[]) {
				return equals((short[]) o1, (short[]) o2);
			} else if (o1 instanceof int[]) {
				return equals((int[]) o1, (int[]) o2);
			} else if (o1 instanceof long[]) {
				return equals((long[]) o1, (long[]) o2);
			} else if (o1 instanceof float[]) {
				return equals((float[]) o1, (float[]) o2);
			} else if (o1 instanceof double[]) {
				return equals((double[]) o1, (double[]) o2);
			} else if (o1 instanceof boolean[]) {
				return equals((boolean[]) o1, (boolean[]) o2);
			} else {
				return equals((Object[]) o1, (Object[]) o2);
			}
		} else {
			return o1.equals(o2);
		}
	}
	
	public static <T> boolean greaterThan(Comparable<T> c, T o) {
		if (c == null) {
			return false;
		} else {
			return c.compareTo(o) > 0;
		}
	}
	
	public static <T> boolean lessThan(Comparable<T> c, T o) {
		if (c == null) {
			return false;
		} else {
			return c.compareTo(o) < 0;
		}
	}
	
	public static boolean and(boolean... bs) {
		boolean result = true;
		for (int i = 0; result && i < bs.length; i++) {
			result = result && bs[i];
		}
		return result;
	}
	
	public static boolean or(boolean... bs) {
		boolean result = false;
		for (int i = 0; !result && i < bs.length; i++) {
			result = result || bs[i];
		}
		return result;
	}
}
