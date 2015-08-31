package org.rdm.aquabots.dashboard.test.json;

import org.rdm.aquabots.dashboard.plan.boat.StatusModel;

public class JsonTest {

	public static final String SYSTEM_STRING = "{ \"Name\": \"Aftica\"," + 
			"\"Compass\": { \"Bearing\": 1825, \"Roll\": 88, \"Pitch\": 1 }," + 
			"\"Gps\": { \"fix\": 1922 },"+
			"\"Sabertooth\": { \"Motor Left\": 1.000000, \"Motor Right\": 1.000000 },"+
			"\"Servo\": { \"Servo Left\": 0.000000, \"Servo Right\": 0.000000 },"+ 
			"\"Path\": { \"Length\": 2, \"Currentwp\": 0, " + 
				"\"Waypoints\": [ {  \"lat\": 5.123456, \"lon\": 42.12345 }, {  \"lat\": 5.123456, \"lon\": 42.12345} ] } }";

	public static final String SYSTEM_STRING_2 = "{ \"Name\": \"Aftica\"," + 
			"\"Path\": { \"Length\": 2, \"Currentwp\": 0, " + 
				"\"Waypoints\": [ {  \"lat\": 5.123456, \"lon\": 42.12345 }, {  \"lat\": 5.123456, \"lon\": 42.12345 } ] } }";

	public boolean testSystenString( String jsonString ) throws Exception{
		StatusModel model = (StatusModel) StatusModel.createBoatStatusModel(jsonString);
		System.out.println( "Model generated: " + ( model != null ));
		System.out.println( model.printStructure());
		return (model != null );
	}
}
