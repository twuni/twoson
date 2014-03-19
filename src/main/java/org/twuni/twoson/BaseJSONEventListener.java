package org.twuni.twoson;

public class BaseJSONEventListener implements JSONEventListener {

	@Override
	public void onBeginArray() {
		// By default, do nothing.
	}

	@Override
	public void onBeginObject() {
		// By default, do nothing.
	}

	@Override
	public void onBoolean( boolean value ) {
		// By default, do nothing.
	}

	@Override
	public void onDouble( double value ) {
		// By default, do nothing.
	}

	@Override
	public void onEndArray() {
		// By default, do nothing.
	}

	@Override
	public void onEndObject() {
		// By default, do nothing.
	}

	@Override
	public void onInteger( int value ) {
		// By default, do nothing.
	}

	@Override
	public void onNull() {
		// By default, do nothing.
	}

	@Override
	public void onObjectKey( byte [] value ) {
		// By default, do nothing.
	}

	@Override
	public void onString( byte [] value ) {
		// By default, do nothing.
	}

}
