package me.snaipe.cookie.misc;

public class EqualsBuilder {
	
	boolean status = true;
	
	public EqualsBuilder append(byte first, byte other) {
		status = status && first == other;
		return this;
	}
	
	public EqualsBuilder append(short first, short other) {
		status = status && first == other;
		return this;
	}
	
	public EqualsBuilder append(char first, char other) {
		status = status && first == other;
		return this;
	}
	
	public EqualsBuilder append(int first, int other) {
		status = status && first == other;
		return this;
	}
	
	public EqualsBuilder append(long first, long other) {
		status = status && first == other;
		return this;
	}
	
	public EqualsBuilder append(float first, float other) {
		status = status && first == other;
		return this;
	}
	
	public EqualsBuilder append(double first, double other) {
		status = status && first == other;
		return this;
	}
	
	public EqualsBuilder append(boolean first, boolean other) {
		status = status && first == other;
		return this;
	}
	
	public EqualsBuilder append(Object first, Object other) {
		status = status && Logic.equals(first, other);
		return this;
	}
	
	public boolean equals() {
		return status;
	}
}
