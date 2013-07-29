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

}
