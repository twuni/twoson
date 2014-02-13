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

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class JSONParserTest extends Assert {

	private static final byte [] TEST_OBJECT = "{\"a\":\"test\",\"b\":123,\"c\":[1,2,3],\"d\":{\"da\":1,\"db\":\"test\"},\"e\":\"with \\\"quote\\\" in it\",\"f\":\"with\nline\nbreaks\",\"g\":\"with \\u003cb\\u003eUnicode\\u003c/b\\u003e\",\"h\":\"with\\nescaped\\nline\\nbreaks\",\"i\":123.456}".getBytes();

	protected boolean pass;

	private void assertStringValueEquals( String json, final String expected ) throws IOException {

		ByteArrayInputStream in = new ByteArrayInputStream( json.getBytes( "UTF-8" ) );

		JSONEventListener listener = new BaseJSONEventListener() {

			@Override
			public void onString( char [] value ) {
				assertEquals( expected, new String( value ) );
				pass = true;
			}

		};

		pass = false;
		new JSONParser( in, listener ).read();
		assertTrue( pass );

	}

	@Test
	public void read_onString_shouldCorrectlyParseEscapedLineBreaks() throws IOException {
		assertStringValueEquals( "{\"a\":\".\\n.\"}", ".\n." );
	}

	@Test
	public void read_onString_shouldCorrectlyParseUnicodeCharacters() throws IOException {
		assertStringValueEquals( "{\"a\":\"Ǥ😀\"}", "Ǥ😀" );
	}

	@Test
	public void testParser() throws IOException {
		ByteArrayInputStream input = new ByteArrayInputStream( TEST_OBJECT );
		new JSONParser( input, new LoggingJSONEventListener() ).read();
	}

}
