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

	public void read() throws IOException {
		byte [] buffer = new byte [64 * 1024];
		Stack<Event> scope = new Stack<Event>();
		for( int size = in.read( buffer, 0, buffer.length ); size > 0; size = in.read( buffer, 0, buffer.length ) ) {
			for( int i = 0; i < buffer.length; i++ ) {
				char c = (char) buffer[i];
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
						// Should only happen if our most recent event was an ARRAY or an OBJECT
						break;
					case ':':
						// Should only happen if our most recent event was an OBJECT_KEY.
						break;
					case ' ':
					case '\t':
					case '\r':
					case '\n':
						// Ignore whitespace.
						break;
					case '"':
						int j;
						for( j = i + 1; j < buffer.length; j++ ) {
							if( buffer[j] == '\\' ) {
								j++;
								continue;
							}
							if( buffer[j] == '"' ) {
								break;
							}
						}
						char [] string = new char [j - i - 1];
						for( int a = 0; a < string.length; a++ ) {
							string[a] = (char) buffer[i + 1 + a];
						}
						i = j;
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
						// Assume this is true.
						i += 3;
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
						// Assume this is false.
						i += 4;
						listener.onBoolean( false );
						switch( scope.peek() ) {
							case OBJECT_KEY:
								scope.pop();
								break;
							default:
								break;
						}
					case 'n':
						// Assume this is null.
						i += 3;
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
						int k;
						boolean isFloat = false;
						for( k = i + 1; k < buffer.length; k++ ) {
							char z = (char) buffer[k];
							if( z == '.' ) {
								isFloat = true;
								continue;
							}
							if( z < '0' || z > '9' ) {
								break;
							}
						}
						char [] number = new char [k - i];
						for( int b = 0; b < number.length; b++ ) {
							number[b] = (char) buffer[i + b];
						}
						if( isFloat ) {
							float f = (float) atof( number );
							burn( number );
							listener.onFloat( f );
						} else {
							int g = atoi( number );
							burn( number );
							listener.onInteger( g );
						}
						i = k - 1;
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
		}
		burn( buffer );
	}

	private static boolean isDigit( char c ) {
		return '0' <= c && c <= '9';
	}

	public static int atoi( char [] s ) {
		int a = 0;
		for( int i = 0; i < s.length; i++ ) {
			a = a * 10 + ( s[i] - '0' );
		}
		return a;
	}

	public static double atof( char [] s ) {

		double a = 0;
		int e = 0;
		int i;

		for( i = 0; i < s.length; i++ ) {
			if( !isDigit( s[i] ) ) {
				break;
			}
			a = a * 10 + ( s[i] - '0' );
		}

		if( s[i] == '.' ) {
			i++;
			while( i < s.length ) {
				if( !isDigit( s[i] ) ) {
					break;
				}
				a = a * 10 + ( s[i] - '0' );
				e--;
				i++;
			}
		}

		if( i < s.length && ( s[i] == 'e' || s[i] == 'E' ) ) {

			int sign = 1;
			int x = 0;
			i++;
			if( s[i] == '+' ) {
				i++;
			} else if( s[i] == '-' ) {
				i++;
				sign = -1;
			}
			while( isDigit( s[i] ) ) {
				x = x * 10 + ( s[i] - '0' );
				i++;
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
		return a;
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
