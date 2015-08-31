package org.rdm.aquabots.dashboard.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

	private static Server server = new Server();
	
	private int port;
	
	private Collection<IServerListener> listeners;
	
	private ExecutorService service;
	private Runnable runnable = new Runnable(){

		@Override
		public void run() {
	    	try {
				server.run();
		    	System.out.println("Serving sensors on: " + server.getPort() );
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	};

	private boolean started;
	
	private Server() {
		this.port = 8080;
		this.started = false;
		this.listeners = new ArrayList<IServerListener>();
	}

	public void addListener( IServerListener listener ){
		this.listeners.add( listener );
	}

	public void removeListener( IServerListener listener ){
		this.listeners.remove( listener );
	}

	protected void notifyListeners( ClientEvent event ){
		for( IServerListener listener: this.listeners )
			listener.notifyMessageReceived(event);
	}
	
	public static Server getInstance(){
		return server;
	}
	
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void start (String[]args) throws Exception {
    	service = Executors.newCachedThreadPool();
    	this.started = true;
    	service.execute( runnable);
 	}
	
	public void stop(){
		this.started = false;
		this.service.shutdown();
	}

	public void run() throws Exception {
		ServerSocket socket = new ServerSocket( port );
		while( this.started ){
			Socket sock=socket.accept();
			InputStreamReader IR= new InputStreamReader(sock.getInputStream());
			BufferedReader BR= new BufferedReader(IR);
			String message = BR.readLine();
			System.out.println(message);
			this.notifyListeners( new ClientEvent( this, message ));
			if(message!=null){
				PrintStream ps=new PrintStream(sock.getOutputStream());
				ps.println("received");
			}
		}
		socket.close();
	}
}

	/*
	byte mac[] = { 0x00, 0xAA, 0xBB, 0xCC, 0xDE, 0x03};
	char server[] = "localhost:8080";
	WebSocketClient client;

	void setup() {
		Serial.begin(9600);
		Ethernet.begin(mac);
		client.connect(server);
		client.setDataArrivedDelegate(dataArrived);
		client.send("Hello World!");
	}

	void loop() {
		client.monitor();
	} 
	
	void dataArrived(WebSocketClient client, String data) {
		Serial.println("Data Arrived: " + data);
		}
*/