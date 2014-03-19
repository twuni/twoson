package org.twuni.twoson;

import java.util.Arrays;

public class JSONStringExpectation extends BaseJSONEventListener {

	public final Expectation<byte []> expectation;

	public JSONStringExpectation( byte [] expected ) {
		expectation = new Expectation<byte []>( expected );
	}

	public JSONStringExpectation( String expected ) {
		this( JSONUtils.toByteArray( expected ) );
	}

	@Override
	public void onString( byte [] value ) {
		expectation.actual = Arrays.copyOf( value, value.length );
	}

}
