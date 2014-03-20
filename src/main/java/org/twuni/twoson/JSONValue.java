package org.twuni.twoson;

import java.util.Arrays;

public class JSONValue {

	public static enum Type {

		NULL,
		BOOLEAN,
		INTEGER,
		LONG,
		DOUBLE,
		STRING,
		ARRAY,
		OBJECT;

	}

	public final Type type;
	public final boolean booleanValue;
	public final int intValue;
	public final long longValue;
	public final double doubleValue;
	public final byte [] stringValue;
	public final JSONObjectProperty [] objectValue;
	public final JSONValue [] arrayValue;

	public JSONValue() {
		this( Type.NULL, false, 0, 0, 0, null, null, null );
	}

	public JSONValue( boolean value ) {
		this( Type.BOOLEAN, value, 0, 0, 0, null, null, null );
	}

	public JSONValue( byte [] value ) {
		this( Type.STRING, false, 0, 0, 0, value, null, null );
	}

	public JSONValue( double value ) {
		this( Type.DOUBLE, false, 0, 0, value, null, null, null );
	}

	public JSONValue( int value ) {
		this( Type.INTEGER, false, value, 0, 0, null, null, null );
	}

	public JSONValue( JSONObjectProperty [] value ) {
		this( Type.OBJECT, false, 0, 0, 0, null, value, null );
	}

	public JSONValue( JSONValue previousState, JSONObjectProperty... value ) {
		this( Type.OBJECT, false, 0, 0, 0, null, JSONUtils.concat( previousState.objectValue, value ), null );
	}

	public JSONValue( JSONValue previousState, JSONValue... value ) {
		this( Type.ARRAY, false, 0, 0, 0, null, null, JSONUtils.concat( previousState.arrayValue, value ) );
	}

	public JSONValue( JSONValue [] value ) {
		this( Type.ARRAY, false, 0, 0, 0, null, null, value );
	}

	public JSONValue( long value ) {
		this( Type.LONG, false, 0, value, 0, null, null, null );
	}

	private JSONValue( Type type, boolean booleanValue, int intValue, long longValue, double doubleValue, byte [] stringValue, JSONObjectProperty [] objectValue, JSONValue [] arrayValue ) {
		this.type = type;
		this.booleanValue = booleanValue;
		this.intValue = intValue;
		this.longValue = longValue;
		this.doubleValue = doubleValue;
		this.stringValue = stringValue;
		this.objectValue = objectValue;
		this.arrayValue = arrayValue;
	}

	private void expect( Type expectedType ) {
		if( !expectedType.equals( type ) ) {
			throw new IllegalStateException( String.format( "Expected type %s, was %s", expectedType, type ) );
		}
	}

	public JSONValue get( byte [] key ) {
		expect( Type.OBJECT );
		for( int i = 0; i < objectValue.length; i++ ) {
			JSONObjectProperty property = objectValue[i];
			if( property.is( key ) ) {
				return property.value;
			}
		}
		return null;
	}

	public JSONValue get( int index ) {
		expect( Type.ARRAY );
		return arrayValue[index];
	}

	public JSONValue get( String key ) {
		return get( JSONUtils.toByteArray( key ) );
	}

	public byte [] keyAt( int index ) {
		expect( Type.OBJECT );
		return objectValue[index].key;
	}

	public int length() {
		switch( type ) {
			case OBJECT:
				return objectValue.length;
			case ARRAY:
				return arrayValue.length;
			case STRING:
				return stringValue.length;
			default:
				return 1;
		}
	}

	public void reset() {
		if( stringValue != null ) {
			Arrays.fill( stringValue, (byte) 0 );
		}
		if( objectValue != null ) {
			for( int i = 0; i < objectValue.length; i++ ) {
				objectValue[i].reset();
				objectValue[i] = null;
			}
		}
		if( arrayValue != null ) {
			for( int i = 0; i < arrayValue.length; i++ ) {
				arrayValue[i].reset();
				arrayValue[i] = null;
			}
		}
	}

	@Override
	public String toString() {
		switch( type ) {
			case ARRAY:
				return Arrays.toString( arrayValue );
			case OBJECT:
				return Arrays.toString( objectValue ).replaceAll( "^\\[(.+)\\]$", "{$1}" );
			case STRING:
				return String.format( "\"%s\"", new String( stringValue ) );
			case BOOLEAN:
				return Boolean.toString( booleanValue );
			case DOUBLE:
				return Double.toString( doubleValue );
			case INTEGER:
				return Integer.toString( intValue );
			case LONG:
				return Long.toString( longValue );
			case NULL:
				return "null";
		}
		return "";
	}

}
