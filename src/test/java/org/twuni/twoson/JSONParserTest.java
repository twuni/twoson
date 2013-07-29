package org.twuni.twoson;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Test;

public class JSONParserTest {

	private static final byte [] TEST_OBJECT = "{\"a\":\"test\",\"b\":123,\"c\":[1,2,3],\"d\":{\"da\":1,\"db\":\"test\"}}".getBytes();

	@Test
	public void testParser() throws IOException {

		JSONParser parser = new JSONParser( new ByteArrayInputStream( TEST_OBJECT ), new JSONEventListener() {

			public void onBeginObject() {
				System.out.println( "#onBeginObject" );
			}

			public void onEndObject() {
				System.out.println( "#onEndObject" );
			}

			public void onBeginArray() {
				System.out.println( "#onBeginArray" );
			}

			public void onEndArray() {
				System.out.println( "#onEndArray" );
			}

			public void onObjectKey( char [] value ) {
				System.out.println( "#onObjectKey key:" + new String( value ) );
			}

			public void onString( char [] value ) {
				System.out.println( "#onString value:" + new String( value ) );
			}

			public void onInteger( int value ) {
				System.out.println( "#onInteger value:" + Integer.toString( value ) );
			}

			public void onFloat( float value ) {
				System.out.println( "#onFloat value:" + Float.toString( value ) );
			}

			public void onBoolean( boolean value ) {
				System.out.println( "#onBoolean value:" + Boolean.toString( value ) );
			}

			public void onNull() {
				System.out.println( "#onNull" );
			}

		} );

		parser.read();

	}

}