package org.rdm.aquabots.dashboard.servlet;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.rdm.aquabots.dashboard.model.TrajectoryModel;
import org.rdm.aquabots.dashboard.model.TrajectoryModel.Append;
import org.rdm.aquabots.dashboard.model.TrajectoryModel.Parameters;
import org.rdm.aquabots.dashboard.model.waypoint.WayPoint;
import org.rdm.aquabots.dashboard.utils.StringStyler;

public class MapServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Logger logger = Logger.getLogger( this.getClass().getName() );
	
	private TrajectoryModel model = TrajectoryModel.getInstance();
	private MapSession session = MapSession.getInstance();
	
	@Override
	public void init() throws ServletException {
		session.setModel(model);
		super.init();
	}

	@Override
	public void destroy() {
		session.stop();
		super.destroy();
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Enumeration<String> attrs = req.getParameterNames();
		Map<String, String> map = new HashMap<String, String>();
		while( attrs.hasMoreElements()){
			String attr = attrs.nextElement();
			map.put( attr, URLDecoder.decode( req.getParameter(attr), "UTF-8"));
		}
		logger.info("DO GET " + map.toString());
		for( WayPoint waypoint: model.createWayPoints( map, Append.LAST )){
			String str = map.get( Parameters.STYLE.toString().toLowerCase());
			if( WayPoint.Styles.POINT.equals( WayPoint.Styles.valueOf( StringStyler.styleToEnum( str )) ))
				model.addWayPoint(waypoint);
		}
		super.doGet(req, resp);
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
	}

}
