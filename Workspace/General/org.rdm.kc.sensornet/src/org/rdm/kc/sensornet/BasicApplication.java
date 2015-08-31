package org.rdm.kc.sensornet;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.rap.rwt.application.Application;
import org.eclipse.rap.rwt.application.ApplicationConfiguration;
import org.eclipse.rap.rwt.application.Application.OperationMode;
import org.eclipse.rap.rwt.client.WebClient;

public class BasicApplication implements ApplicationConfiguration {

    public void configure(Application application) {
        Map<String, String> properties = new HashMap<String, String>();
        application.addStyleSheet( "sensors.theme", "web/theme/aquabots.css" );
        
        properties.put(WebClient.PAGE_TITLE, "Hello KC SensorNet");
        properties.put( WebClient.THEME_ID, "sensors.theme" );
        properties.put(WebClient.PAGE_TITLE, "Kenniscentrum RDM Turbility Sensor");

        application.setOperationMode( OperationMode.SWT_COMPATIBILITY );       
 
        application.addEntryPoint("/sensors", BasicEntryPoint.class, properties);
    }
}
