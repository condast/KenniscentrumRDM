package org.rdm.aquabots.dashboard.persistence;

import org.rdm.aquabots.dashboard.model.GeoView;
import org.rdm.aquabots.dashboard.persistence.PreferenceStore.Categories;
import org.rdm.aquabots.dashboard.utils.StringStyler;

public class LocationStore {

	public enum Settings{
		LOCATION,
		LONGTITUDE,
		LATITUDE,
		ZOOM;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}	
	}
	
	public LocationStore() {
		GeoView geo = GeoView.getInstance();
		initValue( Settings.LOCATION, GeoView.Location.HEIJPLAAT.toString());
		initValue( Settings.LONGTITUDE, String.valueOf( geo.getLongtitude() ));
		initValue( Settings.LATITUDE, String.valueOf( geo.getLatitude() ));
		initValue( Settings.ZOOM, String.valueOf( geo.getZoom() ));
		load();
		geo.jump();
	}
	
	protected void initValue( Settings key, String def ){
		PreferenceStore store = PreferenceStore.getInstance();
		store.initValue( Categories.LOCATION, key.toString(), def );	
	}

	protected String get( Settings key ){
		PreferenceStore store = PreferenceStore.getInstance();
		return store.getValue( Categories.LOCATION, key.toString() );	
	}

	protected void set( Settings key, String value ){
		PreferenceStore store = PreferenceStore.getInstance();
		store.setValue( Categories.LOCATION, key.toString(), value );	
	}

	public GeoView load(){
		GeoView geo = GeoView.getInstance();
		geo.setLocation( get( Settings.LOCATION ));
		geo.setLongtitude( Float.valueOf( get( Settings.LONGTITUDE )));
		geo.setLatitude( Float.valueOf( get( Settings.LATITUDE )));
		geo.setZoom( Integer.valueOf( get( Settings.ZOOM )));
		return geo;
	}

	public void save(){
		GeoView geo = GeoView.getInstance();
		set( Settings.LOCATION, geo.getLocation() );
		set( Settings.LONGTITUDE, String.valueOf( geo.getLongtitude() ));
		set( Settings.LATITUDE, String.valueOf( geo.getLatitude() ));
		set( Settings.ZOOM, String.valueOf( geo.getZoom() ));
	}
}
