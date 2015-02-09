var ws = new WebSocket("ws://nr25.de:9000/aquatic-drone");

ws.onopen = function() {
	console.log("Opened!");
    ws.send("Hello Server");
};

ws.onmessage = function (evt) {
	console.log("Message: " + evt.data);
};

ws.onclose = function() {
	console.log("Closed!");
};

ws.onerror = function(err) {
	console.log("Error: " + err);
};

function wsSend( msg ){
	ws.send( msg );
}