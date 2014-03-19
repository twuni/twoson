package org.twuni.twoson;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class JSONStringExpectation extends BaseJSONEventListener {

	private static byte [] toByteArray( String string ) {
		byte [] array = null;
		if( string != null ) {
			try {
				array = string.getBytes( "UTF-8" );
			} catch( UnsupportedEncodingException exception ) {
				array = string.getBytes();
			}
		}
		return array;
	}

	public final Expectation<byte []> expectation;

	public JSONStringExpectation( byte [] expected ) {
		expectation = new Expectation<byte []>( expected );
	}

	public JSONStringExpectation( String expected ) {
		this( toByteArray( expected ) );
	}

	@Override
	public void onString( byte [] value ) {
		expectation.actual = Arrays.copyOf( value, value.length );
	}

}
