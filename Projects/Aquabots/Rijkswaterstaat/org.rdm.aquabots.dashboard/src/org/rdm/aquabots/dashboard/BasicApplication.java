package org.rdm.aquabots.dashboard;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.rap.rwt.application.Application;
import org.eclipse.rap.rwt.application.ApplicationConfiguration;
import org.eclipse.rap.rwt.client.WebClient;


public class BasicApplication implements ApplicationConfiguration {

    public void configure(Application application) {
        application.addStyleSheet( "aquabots.theme", "web/theme/aquabots.css" );
        Map<String, String> properties = new HashMap<String, String>();
        properties.put(WebClient.PAGE_TITLE, "Hello Aquabots");
        properties.put( WebClient.THEME_ID, "aquabots.theme" );
        application.addEntryPoint("/aquabots", BasicEntryPoint.class, properties);
     }
}
