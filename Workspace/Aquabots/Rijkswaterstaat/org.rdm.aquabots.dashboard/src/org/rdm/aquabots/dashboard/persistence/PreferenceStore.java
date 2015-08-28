package org.rdm.aquabots.dashboard.persistence;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.service.prefs.Preferences;
import org.rdm.aquabots.dashboard.utils.StringStyler;

public class PreferenceStore {

	private static final String S_PREFERENCE_ROOT = "org.rdm.aquabots.root";
	
	public enum Categories{
		LOCATION,
		BOAT;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}	
	}

	private Preferences preferences;
	private Map<Categories, Map<String,String>> inits;
	
	private static PreferenceStore store = new PreferenceStore();
	
	private PreferenceStore() {
		preferences = InstanceScope.INSTANCE.getNode( S_PREFERENCE_ROOT);
		inits = new HashMap<Categories, Map<String,String>>();
	}

	public static PreferenceStore getInstance(){
		return store;
	}
	
	public String getValue( Categories option, String key ){
		Preferences prefs = preferences.node( option.toString() );
		Map<String,String> init = inits.get( option );
		String def = init.get(key);
		return prefs.get(key, def);
	}
	
	public void setValue( Categories option, String key, String value ){
		Preferences prefs = preferences.node( option.toString() );
		prefs.put(key, value);
	}

	public void initValue( Categories option, String key, String value ){
		Map<String,String> init = inits.get( option );
		if( init == null ){
			init = new HashMap<String, String>();
			inits.put(option, init);
		}
		init.put(key, value);
	}
	
	/**
	 * Get a category
	 * @param option
	 * @return
	 */
	public Preferences getPreferences( Categories option ){
		return preferences.node( option.toString() );
	}
}
