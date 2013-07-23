package org.twuni.jason;

import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;

public class JSONParser {

	public static interface JSONEventListener {

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

	private final InputStream in;
	private final JSONEventListener listener;

	public JSONParser( InputStream in, JSONEventListener listener ) {
		this.in = in;
		this.listener = listener;
	}

	private static enum Event {
		NONE,
		OBJECT,
		ARRAY,
		OBJECT_KEY
	}

	private byte [] buffer = new byte [64 * 1024];
	private int offset;
	private int size;
	private Stack<Event> scope = new Stack<Event>();

	private char readNext() throws IOException {
		if( offset >= size ) {
			offset = 0;
			size = in.read( buffer, 0, buffer.length );
		}
		if( isFinished() ) {
			return '\0';
		}
		char c = (char) buffer[offset];
		offset++;
		return c;
	}

	private boolean isFinished() {
		return size <= 0;
	}

	public void read() throws IOException {

		size = in.read( buffer, 0, buffer.length );
		for( char c = readNext(); c != '\0'; c = readNext() ) {

			switch( c ) {

				case '{':
					scope.push( Event.OBJECT );
					listener.onBeginObject();
					break;

				case '}':
					scope.pop();
					listener.onEndObject();
					break;

				case '[':
					scope.push( Event.ARRAY );
					listener.onBeginArray();
					break;

				case ']':
					scope.pop();
					listener.onEndArray();
					break;

				case ',':
					switch( scope.peek() ) {
						case ARRAY:
						case OBJECT:
							break;
						default:
							throw new IllegalStateException();
					}
					break;

				case ':':
					switch( scope.peek() ) {
						case OBJECT_KEY:
							break;
						default:
							throw new IllegalStateException();
					}
					break;

				case ' ':
				case '\t':
				case '\r':
				case '\n':
					// Ignore whitespace.
					break;

				case '"':
					int j;
					// FIXME: Read-ahead only works properly if the right anchor exists within the
					// buffer
					for( j = offset; j < buffer.length; j++ ) {
						if( buffer[j] == '\\' ) {
							j++;
							continue;
						}
						if( buffer[j] == '"' ) {
							break;
						}
					}
					char [] string = new char [j - offset];
					for( int a = 0; a < string.length; a++ ) {
						string[a] = (char) buffer[offset + a];
					}
					offset = j + 1;
					switch( scope.peek() ) {
						case OBJECT:
							listener.onObjectKey( string );
							burn( string );
							scope.push( Event.OBJECT_KEY );
							break;
						case OBJECT_KEY:
							listener.onString( string );
							burn( string );
							scope.pop();
							break;
						default:
							listener.onString( string );
							burn( string );
							break;
					}
					break;

				case 't':
					readNext();// 'r'
					readNext();// 'u'
					readNext();// 'e'
					listener.onBoolean( true );
					switch( scope.peek() ) {
						case OBJECT_KEY:
							scope.pop();
							break;
						default:
							break;
					}
					break;

				case 'f':
					readNext();// 'a'
					readNext();// 'l'
					readNext();// 's'
					readNext();// 'e'
					listener.onBoolean( false );
					switch( scope.peek() ) {
						case OBJECT_KEY:
							scope.pop();
							break;
						default:
							break;
					}

				case 'n':
					readNext();// 'u'
					readNext();// 'l'
					readNext();// 'l'
					listener.onNull();
					switch( scope.peek() ) {
						case OBJECT_KEY:
							scope.pop();
							break;
						default:
							break;
					}

				case '-':
				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':
					double f = atof( c );
					if( f - (int) f < 0.00001 ) {
						listener.onInteger( (int) f );
					} else {
						listener.onFloat( (float) f );
					}
					switch( scope.peek() ) {
						case OBJECT_KEY:
							scope.pop();
							break;
						default:
							break;
					}
					break;

				default:
					// Not sure what this is.
					break;

			}

		}

		burn( buffer );

	}

	private static boolean isDigit( char c ) {
		return '0' <= c && c <= '9';
	}

	private double atof( char firstDigit ) throws IOException {
		double a = 0;
		int e = 0;
		char c = firstDigit;
		boolean negative = false;
		if( c == '-' ) {
			negative = true;
			c = readNext();
		}
		while( isDigit( c ) ) {
			a = a * 10 + ( c - '0' );
			c = readNext();
		}
		if( c == '.' ) {
			c = readNext();
			while( isDigit( c ) ) {
				a = a * 10 + ( c - '0' );
				e--;
				c = readNext();
			}
		}
		if( c == 'e' || c == 'E' ) {
			int sign = 1;
			int x = 0;
			c = readNext();
			if( c == '+' ) {
				// Valid
			} else if( c == '-' ) {
				sign = -1;
			} else {
				throw new IOException( "Invalid number format" );
			}
			c = readNext();
			while( isDigit( c ) ) {
				x = x * 10 + ( c - '0' );
				c = readNext();
			}
			e += x * sign;
		}
		while( e > 0 ) {
			a *= 10;
			e--;
		}
		while( e < 0 ) {
			a *= 0.1;
			e++;
		}
		offset--;
		return negative ? -a : a;
	}

	private static void burn( char [] buffer ) {
		for( int i = 0; i < buffer.length; i++ ) {
			buffer[i] = 0;
		}
	}

	private static void burn( byte [] buffer ) {
		for( int i = 0; i < buffer.length; i++ ) {
			buffer[i] = 0;
		}
	}

}
