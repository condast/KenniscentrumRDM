package org.rdm.aquabots.dashboard.structure;

import javax.json.stream.JsonParser;

public interface IStatusStructure {

	public abstract String getId();

	/**
	 * Set the attribute with the given key-value pair
	 * @param key
	 * @param value
	 */
	public void setAttribute( String key, Object value );
	
	/**
	 * Get the parent.
	 * @param id
	 * @return
	 */
	public IStatusStructure getParent();

	/**
	 * Get the child with the given id.
	 * @param id
	 * @return
	 */
	public IStatusStructure getChild( String id );

	/**
	 * The start object can be overridden, in order to change the structure
	 * In that case, the method should return true;
	 * @param key
	 * @return
	 */
	public boolean onStartObject( String key, JsonParser parser );

	/**
	 * The start object can be overridden, in order to change the structure
	 * In that case, the method should return true;
	 * @param key
	 * @return
	 */
	public boolean onEndObject( String key, JsonParser parser );

	/**
	 * Print the structure from the root
	 * @return
	 */
	public String printStructure();
	
	/**
	 * Print the structure from the root, and include a number of tabs
	 * @return
	 */
	public String printStructure( int tabs);

	/**
	 * Create an array 
	 * @param arrayName
	 * @param parser
	 * @return
	 */
	AbstractArrayStructure<?> createArray(String arrayName, JsonParser parser);
}