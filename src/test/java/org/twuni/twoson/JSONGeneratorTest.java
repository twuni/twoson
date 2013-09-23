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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class JSONGeneratorTest {

	@Test
	public void testEverything() throws IOException {

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		JSONGenerator generator = new JSONGenerator( out );

		generator.openObject();

		generator.writeKey( "shouldBeTrue" );
		generator.write( true );
		generator.next();

		generator.writeKey( "shouldBeFalse" );
		generator.write( false );
		generator.next();

		generator.writeKey( "shouldBeNull" );
		generator.writeNull();
		generator.next();

		generator.writeKey( "shouldBe123" );
		generator.write( 123 );
		generator.next();

		generator.writeKey( "shouldBe123.456" );
		generator.write( 123.456 );
		generator.next();

		generator.writeKey( "shouldBeAnArray" );
		generator.openArray();
		generator.writeString( "this is a string" );
		generator.next();
		generator.write( 192837465 );
		generator.next();
		generator.writeNull();
		generator.closeArray();

		generator.closeObject();

		String expected = "{\"shouldBeTrue\":true,\"shouldBeFalse\":false,\"shouldBeNull\":null,\"shouldBe123\":123,\"shouldBe123.456\":123.456,\"shouldBeAnArray\":[\"this is a string\",192837465,null]}";
		String actual = out.toString();

		Assert.assertEquals( expected, actual );

	}

	@Test
	public void testPrettyPrint() throws IOException {

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		JSONGenerator generator = new JSONGenerator( out, true );

		generator.openObject();
		generator.writeKey( "open_object" );
		generator.openObject();
		generator.writeKey( "number" );
		generator.write( 12345 );
		generator.next();
		generator.writeKey( "letters" );
		generator.writeString( "This is a good string." );
		generator.closeObject();
		generator.closeObject();

		String expected = "{\n    \"open_object\": {\n        \"number\": 12345,\n        \"letters\": \"This is a good string.\"\n    }\n}";
		String actual = out.toString();
		Assert.assertEquals( expected, actual );

	}

}
