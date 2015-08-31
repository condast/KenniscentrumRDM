var webSocket;

function openSocket( server, title){
	console.log( "preparing to open" );
	// Ensures only one connection is open at a time
	if(webSocket !== undefined && webSocket.readyState !== WebSocket.CLOSED){
		writeResponse("WebSocket is already opened.");
		return;
	}
	// Create a new instance of the websocket
	webSocket = new WebSocket(server, title );
	console.log( "websocket created" );

	/**
	 * Binds functions to the listeners for the websocket.
	 */
	webSocket.onopen = function(event){
		// For reasons I can't determine, onopen gets called twice
		// and the first time event.data is undefined.
		// Leave a comment if you know the answer.
		console.log('open');
		if(event.data === undefined)
			return;

		writeResponse(event.data);
	};

	webSocket.onmessage = function(event){
		writeResponse(event.data);
	};

	webSocket.onclose = function(event){
		writeResponse("Connection closed");
	};
	console.log( "ready for connections" );
}

function openSocket(){
	console.log( "preparing to open" );
	// Ensures only one connection is open at a time
	if(webSocket !== undefined && webSocket.readyState !== WebSocket.CLOSED){
		writeResponse("WebSocket is already opened.");
		return;
	}
	// Create a new instance of the websocket
	webSocket = new WebSocket("ws://nr2k.de:9000", "aquatic-drone");
	console.log( "websocket created" );

	/**
	 * Binds functions to the listeners for the websocket.
	 */
	webSocket.onopen = function(event){
		// For reasons I can't determine, onopen gets called twice
		// and the first time event.data is undefined.
		// Leave a comment if you know the answer.
		console.log('open');
		if(event.data === undefined)
			return;

		writeResponse(event.data);
	};

	webSocket.onmessage = function(event){
		writeResponse(event.data);
	};

	webSocket.onclose = function(event){
		writeResponse("Connection closed");
	};
	console.log( "ready for connections" );
}

/**
 * Sends the value of the text input to the server
 */
function send( msg ){
	console.log( 'sending message: ' + msg );
	if( webSocket == null ){
		console.log('web socket not opened');
		return;
	}
	webSocket.send( msg );
	console.log( 'message sent' );
}

function closeSocket(){
	webSocket.close();
	console.log('web socket closed');
}

function writeResponse(text){
	console.log('Response: ' + text );
	$.get("BoatServlet", {data: escape(text)}).done( function(data) {
		  console.log(data);
	});
}

openSocket();