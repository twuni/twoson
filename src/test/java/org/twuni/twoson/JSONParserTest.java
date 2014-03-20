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

import org.junit.Test;

public class JSONParserTest extends TestFixture {

	private static void assertArrayLength( int expectedLength, String json ) {
		assertEquals( expectedLength, JSONParser.parse( json ).length() );
	}

	private static void assertType( JSONValue.Type expectedType, JSONValue json ) {
		assertEquals( expectedType, json.type );
	}

	private static void assertType( JSONValue.Type expectedType, String json ) {
		assertType( expectedType, JSONParser.parse( json ) );
	}

	private static void assertType( JSONValue.Type expectedType, String key, String json ) {
		assertType( expectedType, JSONParser.parse( json ).get( key ) );
	}

	@Test
	public void assertTypes() {

		assertType( JSONValue.Type.BOOLEAN, Boolean.toString( true ) );
		assertType( JSONValue.Type.BOOLEAN, Boolean.toString( false ) );

		assertType( JSONValue.Type.NULL, String.valueOf( (Object) null ) );

		assertType( JSONValue.Type.INTEGER, Integer.toString( Integer.MIN_VALUE ) );
		assertType( JSONValue.Type.INTEGER, Integer.toString( Integer.MAX_VALUE ) );

		assertType( JSONValue.Type.DOUBLE, Double.toString( 0.1 ) );
		assertType( JSONValue.Type.DOUBLE, Double.toString( 1.1 ) );

		assertType( JSONValue.Type.STRING, "\"test\"" );
		assertType( JSONValue.Type.STRING, "\"\"" );

		assertType( JSONValue.Type.ARRAY, "[]" );
		assertType( JSONValue.Type.ARRAY, "[{}]" );
		assertType( JSONValue.Type.ARRAY, "[[]]" );
		assertArrayLength( 0, "[]" );
		assertArrayLength( 1, "[[]]" );
		assertArrayLength( 3, "[1,2,3]" );

		assertType( JSONValue.Type.OBJECT, "{}" );
		assertType( JSONValue.Type.OBJECT, "{\"a\":1}" );
		assertType( JSONValue.Type.OBJECT, "{\"a\":[]}" );
		assertType( JSONValue.Type.OBJECT, "{\"a\":{}}" );

		assertType( JSONValue.Type.INTEGER, "a", "{\"a\":1}" );
		assertType( JSONValue.Type.INTEGER, "a", "{\"a\":1,\"b\":true}" );
		assertType( JSONValue.Type.BOOLEAN, "b", "{\"a\":1,\"b\":true}" );

	}

	@Test
	public void read_onString_shouldCorrectlyParseEscapedLineBreaks() {
		expectString( "{\"a\":\".\\n.\"}", ".\n." );
	}

	@Test
	public void read_onString_shouldCorrectlyParseUnicodeCharacters() {
		expectString( "{\"a\":\"Ǥ😀\"}", "Ǥ😀" );
	}

}
