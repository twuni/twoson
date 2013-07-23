package org.twuni.jason;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Test;
import org.twuni.jason.JSONParser.JSONEventListener;

public class JSONParserTest {

	private static final byte [] SIREN = "{\n  \"request_resend\": \"iX-1243\",\n  \"message\": \"This is a good message.\",\n  \"shred_after\":60,\"location\":{\"latitude\":100.2345,\"longitude\":60.243}}".getBytes();

	@Test
	public void testParser() throws IOException {

		JSONParser parser = new JSONParser( new ByteArrayInputStream( SIREN ), new JSONEventListener() {

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
