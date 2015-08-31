package org.rdm.aquabots.dashboard.structure;

import java.io.StringReader;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonException;
import javax.json.stream.JsonParser;

import org.rdm.aquabots.dashboard.utils.StringStyler;

public abstract class AbstractStatusStructure implements IStatusStructure {

	public static final String ID = "ID";
	
	private static final String S_WRN_INVALID_SEQUENCE = "The Json Sequence is incorrect: ";
	private static final String S_ERR_ARRAY_NOT_IMPLEMENTED = "The given array is not implemented: ";

	private IStatusStructure parent;

	private Map<String, Object> attributes;

	private Map<String, IStatusStructure> children;

	protected AbstractStatusStructure( String id ) {
		this( null, id );
	}
	
	private static Logger logger = Logger.getLogger( AbstractStatusStructure.class.getName() );
	
	protected AbstractStatusStructure( IStatusStructure parent, String id ) {
		this.parent = parent;	
		this.attributes = new TreeMap<String, Object>();
		this.attributes.put( ID, id );
		this.children = new TreeMap<String, IStatusStructure>();
	}

	/* (non-Javadoc)
	 * @see org.rdm.aquabots.dashboard.structure.IStatusStructure#getId()
	 */
	@Override
	public final String getId() {
		return (String) this.attributes.get( ID );
	}

	protected final Object getAttribute( String key ) {
		return this.attributes.get( key );
	}

	public void setAttribute(String key, Object value ) {
		this.attributes.put(key, value);
	}

	public IStatusStructure getParent(){
		return parent;
	}
	
	public void addChild( IStatusStructure child ){
		this.children.put( child.getId(), child );
	}
	
	protected void removeChild( IStatusStructure child ){
		this.children.remove( child );
	}

	protected void removeChild( String id ){
		this.children.remove( id );
	}

	/**
	 * Get the child with the given id.
	 * @param id
	 * @return
	 */
	public IStatusStructure getChild( String id ){
		return this.children.get( id );
	}
	
	protected IStatusStructure[] getChildren(){
		Collection<IStatusStructure> struct = this.children.values();
		return struct.toArray(new AbstractStatusStructure[ struct.size()] );
	}

	/**
	 * Create a status structure from a JSON string
	 * @param object
	 * @return
	 */
	protected static void createStructure( IStatusStructure root, String jsonString ){
		StringReader reader = new StringReader( jsonString );
		try {
			JsonParser parser = Json.createParser(reader);
			if( createStructure(root, parser))
				return;
		} 
		catch (JsonException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * The start object can be overridden, in order to change the structure
	 * In that case, the method should return true;
	 * @param key
	 * @return
	 */
	@Override
	public boolean onStartObject( String key, JsonParser parser ){
		return false;
	}

	/**
	 * The start object can be overridden, in order to change the structure
	 * In that case, the method should return true;
	 * @param key
	 * @return
	 */
	@Override
	public boolean onEndObject( String key, JsonParser parser ){
		return true;
	}

	/**
	 * Create an array 
	 * @param arrayName
	 * @return
	 */
	@Override
	public AbstractArrayStructure<?> createArray( String arrayName, JsonParser parser ){
		return null;
	}
	
	/**
	 * Create a status structure from a JSON string
	 * @param object
	 * @return
	 */
	protected static boolean createStructure( IStatusStructure structure, JsonParser parser ){
		JsonParser.Event event;
		String key = null;
		boolean retval = false;
		while( !retval && parser.hasNext() ){
			event = parser.next();
            switch (event) {
            case KEY_NAME:
                key = parser.getString();
                break;
            case VALUE_STRING:
                structure.setAttribute( StringStyler.styleToEnum( key ), parser.getString() );
                break;
            case VALUE_NUMBER:
                structure.setAttribute( StringStyler.styleToEnum( key ), parser.getInt() );
                break;
            case START_OBJECT:
            	if( key == null )
            		break;
            	if(!structure.onStartObject( key, parser ))
            		createStructure( structure.getChild( key ), parser);
            	break;
            case START_ARRAY:
            	AbstractArrayStructure<?> array = structure.createArray(key, parser); 
            	if( array == null)
            		throw new IllegalArgumentException( S_ERR_ARRAY_NOT_IMPLEMENTED + key);
            	array.createArray( parser, true );
            	break;
            case END_ARRAY:
            	break;
            case END_OBJECT:
                retval = structure.onEndObject( key, parser);
            	break;
            default:
            	logger.warning( S_WRN_INVALID_SEQUENCE + event );
                break;
            }						
		}	
		return retval;
	}

	/**
	 * Print the structure from the root
	 * @return
	 */
	public String printStructure(){
		return printStructure(0);
	}
	
	/**
	 * Print the structure from the root
	 * @return
	 */
	public String printStructure( int tabs ){
		Iterator<Map.Entry<String, Object>> iterator = attributes.entrySet().iterator();
		StringBuffer buffer = new StringBuffer();
		printTabs( buffer, tabs );
		buffer.append(this.getId());
		buffer.append("\n");
		while( iterator.hasNext() ){
			Map.Entry<String, Object> entry = iterator.next();
			printTabs( buffer, tabs+1 );
			buffer.append( entry.getKey() + ": " + entry.getValue() + ";\n");
		}
		tabs++;
		for( IStatusStructure child: children.values() ){
			buffer.append( child.printStructure( tabs ));
		}
		return buffer.toString();
	}
	
	protected final void printTabs( StringBuffer buffer, int tabs ){
		for( int i=0; i<tabs; i++ )
			buffer.append("\t");
	}
}
