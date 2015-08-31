package org.rdm.kc.sensornet.servlet;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.rdm.kc.sensornet.core.DataStore;
import org.rdm.kc.sensornet.utils.Utils;

public class ProviderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final String S_ID = "id";
	
	private DataStore store = DataStore.getInstance();
	
	public ProviderServlet() {
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String id = req.getParameter(S_ID );
		if( Utils.isNull( id ))
			return;
		Enumeration<String> enm = req.getParameterNames();
		Map<String, String> data = new HashMap<String, String>();
		while( enm.hasMoreElements() ){
			String name = enm.nextElement();
			if( S_ID.equals( name ))
				continue;
			data.put(name, req.getParameter(name));
		}
		store.storeData( id, data );
		resp.getWriter().println( DataStore.ENTRY + ":" + id );
	}

	
}
