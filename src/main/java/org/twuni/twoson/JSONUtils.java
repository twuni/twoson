package org.twuni.twoson;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class JSONUtils {

	public static <T> T [] concat( T [] a, T [] b ) {
		int al = a.length;
		int bl = b.length;
		T [] c = Arrays.copyOf( a, al + bl );
		for( int i = 0; i < bl; i++ ) {
			c[al + i] = b[i];
		}
		return c;
	}

	public static byte [] toByteArray( String string ) {
		if( string == null ) {
			return null;
		}
		try {
			return string.getBytes( "UTF-8" );
		} catch( UnsupportedEncodingException exception ) {
			return string.getBytes();
		}
	}

}
