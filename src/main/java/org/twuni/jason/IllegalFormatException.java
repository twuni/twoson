package org.twuni.jason;

public class IllegalFormatException extends IllegalStateException {

	private static final long serialVersionUID = 1L;

	public IllegalFormatException( char c, String state ) {
		super( "illegal character '" + c + "' for state " + state );
	}

}
