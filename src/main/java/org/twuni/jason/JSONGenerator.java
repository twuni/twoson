package org.twuni.jason;

import java.io.IOException;
import java.io.OutputStream;

public class JSONGenerator {

	private static final byte [] TRUE = "true".getBytes();
	private static final byte [] FALSE = "false".getBytes();
	private static final byte [] NULL = "null".getBytes();
	private OutputStream out;

	public JSONGenerator( OutputStream out ) {
		this.out = out;
	}

	public void openObject() throws IOException {
		out.write( '{' );
	}

	public void openArray() throws IOException {
		out.write( '[' );
	}

	public void closeArray() throws IOException {
		out.write( ']' );
	}

	public void writeKey( char [] key ) throws IOException {
		writeCharArray( key );
		out.write( ':' );
	}

	public void write( int value ) throws IOException {
		out.write( Integer.toString( value ).getBytes() );
	}

	public void writeKey( String key ) throws IOException {
		writeKey( key.toCharArray() );
	}

	public void writeString( String value ) throws IOException {
		writeCharArray( value.toCharArray() );
	}

	public void write( long value ) throws IOException {
		out.write( Long.toString( value ).getBytes() );
	}

	public void write( double value ) throws IOException {
		out.write( Double.toString( value ).getBytes() );
	}

	public void write( float value ) throws IOException {
		out.write( Float.toString( value ).getBytes() );
	}

	public void writeCharArray( char [] value ) throws IOException {
		writeCharArray( value, true );
	}

	public void writeCharArray( char [] value, boolean burnAfterwards ) throws IOException {
		if( value == null ) {
			writeNull();
			return;
		}
		out.write( '"' );
		for( int i = 0; i < value.length; i++ ) {
			char c = value[i];
			switch( c ) {
				case '"':
				case '\\':
					out.write( '\\' );
					break;
			}
			out.write( c );
		}
		out.write( '"' );
		if( burnAfterwards ) {
			burn( value );
		}
	}

	public void writeNull() throws IOException {
		out.write( NULL );
	}

	public void write( boolean value ) throws IOException {
		out.write( value ? TRUE : FALSE );
	}

	public void next() throws IOException {
		out.write( ',' );
	}

	public void closeObject() throws IOException {
		out.write( '}' );
	}

	private static void burn( char [] buffer ) {
		for( int i = 0; i < buffer.length; i++ ) {
			buffer[i] = 0;
		}
	}

	private static void burn( byte [] buffer ) {
		for( int i = 0; i < buffer.length; i++ ) {
			buffer[i] = 0;
		}
	}

}
