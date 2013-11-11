package org.twuni.twoson;

public class LoggingJSONEventListener implements JSONEventListener {

	protected void log( String format, Object... args ) {
		System.out.println( String.format( format, args ) );
	}

	@Override
	public void onBeginArray() {
		log( "#onBeginArray" );
	}

	@Override
	public void onBeginObject() {
		log( "#onBeginObject" );
	}

	@Override
	public void onBoolean( boolean value ) {
		log( "#onBoolean value:%b", Boolean.valueOf( value ) );
	}

	@Override
	public void onDouble( double value ) {
		log( "#onDouble value:%f", Double.valueOf( value ) );
	}

	@Override
	public void onEndArray() {
		log( "#onEndArray" );
	}

	@Override
	public void onEndObject() {
		log( "#onEndObject" );
	}

	@Override
	public void onInteger( int value ) {
		log( "#onInteger value:%d", Integer.valueOf( value ) );
	}

	@Override
	public void onNull() {
		log( "#onNull" );
	}

	@Override
	public void onObjectKey( char [] value ) {
		log( "#onObjectKey key:%s", String.valueOf( value ) );
	}

	@Override
	public void onString( char [] value ) {
		log( "#onString value:%s", String.valueOf( value ) );
	}

}
