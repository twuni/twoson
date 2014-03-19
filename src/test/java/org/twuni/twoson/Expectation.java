package org.twuni.twoson;

public class Expectation<T> {

	public final T expected;
	public T actual;

	public Expectation( T expected ) {
		this.expected = expected;
	}

}