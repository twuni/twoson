/**
 * The MIT License (MIT)
 * 
 * Copyright (c) 2013 Twuni
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.twuni.twoson;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

public class JSONGenerator {

	private static final byte [] TRUE = JSONUtils.toByteArray( "true" );
	private static final byte [] FALSE = JSONUtils.toByteArray( "false" );
	private static final byte [] NULL = JSONUtils.toByteArray( "null" );
	private static final byte [] INDENT = JSONUtils.toByteArray( "    " );
	private static final byte [] NEW_LINE = JSONUtils.toByteArray( "\n" );

	private static void burn( byte [] buffer ) {
		Arrays.fill( buffer, (byte) 0 );
	}

	private boolean pretty = false;
	private int indentationLevel;
	private final OutputStream out;

	public JSONGenerator( OutputStream out ) {
		this.out = out;
	}

	public JSONGenerator( OutputStream out, boolean pretty ) {
		this( out );
		this.pretty = pretty;
	}

	public JSONGenerator( OutputStream out, int indentationLevel ) {
		this( out, true );
		this.indentationLevel = indentationLevel;
	}

	public void closeArray() throws IOException {
		indentationLevel--;
		newLine();
		out.write( ']' );
	}

	public void closeObject() throws IOException {
		indentationLevel--;
		newLine();
		out.write( '}' );
	}

	public void indent() throws IOException {
		if( pretty ) {
			for( int i = 0; i < indentationLevel; i++ ) {
				out.write( INDENT );
			}
		}
	}

	public void newLine() throws IOException {
		if( pretty ) {
			out.write( NEW_LINE );
			indent();
		}
	}

	public void next() throws IOException {
		out.write( ',' );
		newLine();
	}

	public void openArray() throws IOException {
		out.write( '[' );
		indentationLevel++;
		newLine();
	}

	public void openObject() throws IOException {
		out.write( '{' );
		indentationLevel++;
		newLine();
	}

	public void write( boolean value ) throws IOException {
		out.write( value ? TRUE : FALSE );
	}

	public void write( double value ) throws IOException {
		out.write( Double.toString( value ).getBytes() );
	}

	public void write( float value ) throws IOException {
		out.write( Float.toString( value ).getBytes() );
	}

	public void write( int value ) throws IOException {
		out.write( Integer.toString( value ).getBytes() );
	}

	public void write( long value ) throws IOException {
		out.write( Long.toString( value ).getBytes() );
	}

	public void writeKey( byte [] key ) throws IOException {
		writeString( key );
		out.write( ':' );
		if( pretty ) {
			out.write( ' ' );
		}
	}

	public void writeKey( String key ) throws IOException {
		writeKey( JSONUtils.toByteArray( key ) );
	}

	public void writeNull() throws IOException {
		out.write( NULL );
	}

	public void writeString( byte [] value ) throws IOException {
		if( value != null ) {
			writeString( value, false );
		} else {
			writeNull();
		}
	}

	public void writeString( byte [] value, boolean burnAfterwards ) throws IOException {
		if( value != null ) {
			writeString( value, 0, value.length, burnAfterwards );
		} else {
			writeNull();
		}
	}

	public void writeString( byte [] value, int offset, int length ) throws IOException {
		if( value != null ) {
			writeString( value, offset, length, false );
		} else {
			writeNull();
		}
	}

	public void writeString( byte [] value, int offset, int length, boolean burnAfterwards ) throws IOException {
		if( value == null ) {
			writeNull();
			return;
		}
		out.write( '"' );
		for( int i = offset; i < length; i++ ) {
			byte c = value[i];
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

	public void writeString( String value ) throws IOException {
		writeString( JSONUtils.toByteArray( value ) );
	}

}
