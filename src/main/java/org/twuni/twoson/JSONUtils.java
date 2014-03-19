package org.twuni.twoson;

import java.io.UnsupportedEncodingException;

public class JSONUtils {

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
