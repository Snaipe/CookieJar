package me.snaipe.cookie.misc;

public class HashCodeBuilder {
	
	int prime = 43;
	int hash  = 17;
	
	public HashCodeBuilder append(byte b) {
		hash = hash * prime + b;
		return this;
	}
	
	public HashCodeBuilder append(char c) {
		hash = hash * prime + c;
		return this;
	}
	
	public HashCodeBuilder append(short s) {
		hash = hash * prime + s;
		return this;
	}
	
	public HashCodeBuilder append(int i) {
		hash = hash * prime + i;
		return this;
	}
	
	public HashCodeBuilder append(long l) {
		hash = hash * prime + ((int) (l >>> 32)) ^ ((int) l);
		return this;
	}
	
	public HashCodeBuilder append(float f) {
		return append(Float.floatToIntBits(f));
	}
	
	public HashCodeBuilder append(double d) {
		return append(Double.doubleToLongBits(d));
	}
	
	public HashCodeBuilder append(boolean b) {
		hash = hash * prime + (b ? 1 : 0);
		return this;
	}
	
	public HashCodeBuilder append(byte[] array) {
		if (array == null) {
			hash *= prime;
		} else {
			for (int i = 0; i < array.length; ++i) {
				append(array[i]);
			}
		}
		return this;
	}
	
	public HashCodeBuilder append(char[] array) {
		if (array == null) {
			hash *= prime;
		} else {
			for (int i = 0; i < array.length; ++i) {
				append(array[i]);
			}
		}
		return this;
	}
	
	public HashCodeBuilder append(short[] array) {
		if (array == null) {
			hash *= prime;
		} else {
			for (int i = 0; i < array.length; ++i) {
				append(array[i]);
			}
		}
		return this;
	}
	
	public HashCodeBuilder append(int[] array) {
		if (array == null) {
			hash *= prime;
		} else {
			for (int i = 0; i < array.length; ++i) {
				append(array[i]);
			}
		}
		return this;
	}
	
	public HashCodeBuilder append(long[] array) {
		if (array == null) {
			hash *= prime;
		} else {
			for (int i = 0; i < array.length; ++i) {
				append(array[i]);
			}
		}
		return this;
	}
	
	public HashCodeBuilder append(float[] array) {
		if (array == null) {
			hash *= prime;
		} else {
			for (int i = 0; i < array.length; ++i) {
				append(array[i]);
			}
		}
		return this;
	}
	
	public HashCodeBuilder append(double[] array) {
		if (array == null) {
			hash *= prime;
		} else {
			for (int i = 0; i < array.length; ++i) {
				append(array[i]);
			}
		}
		return this;
	}
	
	public HashCodeBuilder append(boolean[] array) {
		if (array == null) {
			hash *= prime;
		} else {
			for (int i = 0; i < array.length; ++i) {
				append(array[i]);
			}
		}
		return this;
	}
	
	public HashCodeBuilder append(Object[] array) {
		if (array == null) {
			hash *= prime;
		} else {
			for (int i = 0; i < array.length; ++i) {
				append(array[i]);
			}
		}
		return this;
	}
	
	public HashCodeBuilder append(Object other) {
		if (other == null) {
			hash *= prime;
		} else if (other.getClass().isArray()) {
			if (other instanceof byte[]) {
				append((byte[]) other);
			} else if (other instanceof char[]) {
				append((char[]) other);
			} else if (other instanceof short[]) {
				append((short[]) other);
			} else if (other instanceof int[]) {
				append((int[]) other);
			} else if (other instanceof long[]) {
				append((long[]) other);
			} else if (other instanceof float[]) {
				append((float[]) other);
			} else if (other instanceof double[]) {
				append((double[]) other);
			} else if (other instanceof boolean[]) {
				append((boolean[]) other);
			} else {
				append((Object[]) other);
			}
		} else {
			hash = hash * prime + other.hashCode();
		}
		return this;
	}
	
	@Override
	public int hashCode() {
		return hash;
	}
}
