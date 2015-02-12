package org.rdm.aquabots.dashboard.servlet;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.rdm.aquabots.dashboard.json.JsonUtils;
import org.rdm.aquabots.dashboard.model.TrajectoryModel;

public class BoatServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public final String S_DATA = "data";
	
	private Logger logger = Logger.getLogger( this.getClass().getName() );
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Enumeration<String> attrs = req.getParameterNames();
		logger.info("DO GET " );

		Map<String, String> map = new HashMap<String, String>();
		while( attrs.hasMoreElements()){
			String attr = attrs.nextElement();
			map.put( attr, URLDecoder.decode( req.getParameter(attr), "UTF-8"));
		}
		
		String data = map.get( S_DATA );
		if( !JsonUtils.validateJson( data ))
			return;
		
		JsonObject object = JsonUtils.convertToJson( data );
		//for( WayPoint waypoint: model.createWayPoints( map, Append.LAST )){
		//	String str = map.get( Parameters.STYLE.toString().toLowerCase());
		//	if( WayPoint.Styles.POINT.equals( WayPoint.Styles.valueOf( StringStyler.styleToEnum( str )) ))
		//		model.addWayPoint(waypoint);
		//}
		super.doGet(req, resp);
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
	}

}
