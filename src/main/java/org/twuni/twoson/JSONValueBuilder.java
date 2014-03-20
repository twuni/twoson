package org.twuni.twoson;

import java.util.Arrays;
import java.util.Stack;

public class JSONValueBuilder extends BaseJSONEventListener {

	private final Stack<JSONValue> tree = new Stack<JSONValue>();
	private byte [] key;
	private JSONValue result;

	public JSONValueBuilder() {
		tree.push( new JSONValue() );
	}

	public JSONValue getResult() {
		return result;
	}

	@Override
	public void onBeginArray() {
		JSONValue value = new JSONValue( new JSONValue [0] );
		onJSONValue( value );
		tree.push( value );
	}

	@Override
	public void onBeginObject() {
		JSONValue value = new JSONValue( new JSONObjectProperty [0] );
		onJSONValue( value );
		tree.push( value );
	}

	@Override
	public void onBoolean( boolean value ) {
		onJSONValue( new JSONValue( value ) );
	}

	@Override
	public void onDouble( double value ) {
		onJSONValue( new JSONValue( value ) );
	}

	@Override
	public void onEndArray() {
		result = tree.pop();
		updateReference();
	}

	@Override
	public void onEndObject() {
		result = tree.pop();
		updateReference();
	}

	@Override
	public void onInteger( int value ) {
		onJSONValue( new JSONValue( value ) );
	}

	public void onJSONValue( JSONValue value ) {

		if( tree.isEmpty() ) {
			return;
		}

		JSONValue target = tree.peek();

		switch( target.type ) {

			case ARRAY:

				tree.pop();
				tree.push( new JSONValue( target, value ) );
				break;

			case OBJECT:

				tree.pop();
				tree.push( new JSONValue( target, new JSONObjectProperty( key, value ) ) );
				break;

			case NULL:

				tree.pop();
				tree.push( value );
				result = value;
				break;

			default:

				tree.push( value );
				break;

		}

	}

	@Override
	public void onLong( long value ) {
		onJSONValue( new JSONValue( value ) );
	}

	@Override
	public void onNull() {
		onJSONValue( new JSONValue() );
	}

	@Override
	public void onObjectKey( byte [] value ) {
		key = Arrays.copyOf( value, value.length );
	}

	@Override
	public void onString( byte [] value ) {
		onJSONValue( new JSONValue( Arrays.copyOf( value, value.length ) ) );
	}

	private void updateReference() {
		if( tree.isEmpty() ) {
			return;
		}
		JSONValue target = tree.peek();
		switch( target.type ) {
			case OBJECT:
				if( target.objectValue.length > 0 ) {
					int index = target.objectValue.length - 1;
					JSONObjectProperty property = target.objectValue[index];
					target.objectValue[index] = new JSONObjectProperty( property.key, result );
				}
				break;
			case ARRAY:
				if( target.arrayValue.length > 0 ) {
					target.arrayValue[target.arrayValue.length - 1] = result;
				}
				break;
			default:
				break;
		}
	}

}
