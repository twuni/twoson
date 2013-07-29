package org.twuni.twoson;

public interface JSONEventListener {

	public void onBeginObject();

	public void onEndObject();

	public void onBeginArray();

	public void onEndArray();

	public void onObjectKey( char [] value );

	public void onString( char [] value );

	public void onInteger( int value );

	public void onFloat( float value );

	public void onBoolean( boolean value );

	public void onNull();

}
