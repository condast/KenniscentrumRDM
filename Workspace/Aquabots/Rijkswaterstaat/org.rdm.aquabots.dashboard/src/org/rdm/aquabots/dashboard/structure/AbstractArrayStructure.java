package org.rdm.aquabots.dashboard.structure;

import java.util.Collection;
import java.util.logging.Logger;

import javax.json.stream.JsonParser;

public abstract class AbstractArrayStructure<T extends Object> {

	protected static final String S_WRN_INVALID_SEQUENCE = "The Json Sequence is incorrect: ";
	private static final String S_WRN_ALREADY_ACTIVE = "The array creation process is already active: ";
	private static final String S_WRN_VALUE_NOT_ADDED = "The given value was not added: ";

	private static final String S_ERR_NOT_ACTIVE = "The array creation process is not activated: ";

	private static Logger logger = Logger.getLogger( AbstractStatusStructure.class.getName() );

	//The name of the array
	private String name;
	public boolean active;
	
	private Collection<T> arr;
	
	protected AbstractArrayStructure( String name, Collection<T> array ) {
		this.name = name;
		this.active = false;
		arr = array;
	}

	public String getName() {
		return name;
	}

	protected Collection<T> getArray(){
		return arr;
	}
	/**
	 * Create a new element
	 * @return
	 */
	public abstract T createElement( String key );

	/**
	 * Fill the new element with a value
	 * @return
	 */
	public abstract boolean fillValue( T element, String key, JsonParser parser );

	/**
	 * Create a status structure from a JSON string.
	 * @param object
	 * @return
	 */
	public boolean createArray( JsonParser parser, boolean started ){
		JsonParser.Event event;
		String key = null;
		T element = null;
		boolean retval = false;
		if( started )
			this.active = true;
		while( !retval && parser.hasNext() ){
			event = parser.next();
			switch (event) {
			case KEY_NAME:
				if( !this.active )
					throw new IllegalArgumentException( S_ERR_NOT_ACTIVE + key );
				key = parser.getString();
				break;
			case VALUE_STRING:
			case VALUE_NUMBER:
			case VALUE_FALSE:
			case VALUE_TRUE:
			case VALUE_NULL:
				if( !this.active )
					throw new IllegalArgumentException( S_ERR_NOT_ACTIVE + key );
				if( !this.fillValue(element, key, parser))
					logger.warning( S_WRN_VALUE_NOT_ADDED + "{" + key + ", " + parser.getString() + "}");
				break;
			case START_OBJECT:
				if( !this.active )
					throw new IllegalArgumentException( S_ERR_NOT_ACTIVE + key );
				element = this.createElement( key );
				arr.add(element);
				break;
			case START_ARRAY:
				if( this.active )
					logger.warning( S_WRN_ALREADY_ACTIVE + this.name );
				this.active = true;
				break;
			case END_ARRAY:
				this.active = false;
				key = null;
				retval = true;
				break;
			case END_OBJECT:
				if( !this.active )
					throw new IllegalArgumentException( S_ERR_NOT_ACTIVE + key );
				element = null;
				break;
			default:
				logger.warning( S_WRN_INVALID_SEQUENCE + event );
				break;
			}						
		}	
		return retval;
	}

}
