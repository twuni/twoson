package org.twuni.twoson;

import java.util.Arrays;

public class JSONObjectProperty {

	public final byte [] key;
	public final JSONValue value;

	public JSONObjectProperty( byte [] key, JSONValue value ) {
		if( key == null || value == null ) {
			throw new NullPointerException();
		}
		this.key = key;
		this.value = value;
	}

	@Override
	public boolean equals( Object o ) {
		return o != null && hashCode() == o.hashCode();
	}

	@Override
	public int hashCode() {
		return key != null ? Arrays.hashCode( key ) : 0;
	}

	public boolean is( byte [] key ) {
		return Arrays.equals( this.key, key );
	}

	public boolean is( String key ) {
		return is( JSONUtils.toByteArray( key ) );
	}

	public void reset() {
		Arrays.fill( key, (byte) 0 );
		value.reset();
	}

	@Override
	public String toString() {
		return String.format( "\"%s\": %s", new String( key ), value );
	}

}
