package org.rdm.aquabots.dashboard.bounds;

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

import org.rdm.aquabots.dashboard.active.boat.CurrentBoat;
import org.rdm.aquabots.dashboard.plan.boat.BoatSession;

public class BoundsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Logger logger = Logger.getLogger( this.getClass().getName() );
	
	private CurrentBoat model = CurrentBoat.getInstance();
	private BoatSession session = BoatSession.getInstance();
	
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
		logger.info("DO GET BOUNDS" + map.toString());
		super.doGet(req, resp);
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
	}

}
