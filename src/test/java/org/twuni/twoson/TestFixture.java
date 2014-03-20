package org.twuni.twoson;

public class TestFixture extends Assert {

	public static JSONStringExpectation expectString( byte [] expected ) {
		return new JSONStringExpectation( expected );
	}

	public static JSONStringExpectation expectString( String expected ) {
		return new JSONStringExpectation( expected );
	}

	public static void expectString( String json, String expected ) {
		JSONStringExpectation expectation = expectString( expected );
		JSONParser.parse( json, expectation );
		assertArrayEquals( expectation.expectation.expected, expectation.expectation.actual );
	}

}
