var layer = new ol.layer.Tile({source: new ol.source.OSM()});

var view = new ol.View({
	  //projection: 'EPSG:4326',	
	  center: [4000, 3000],
   	  zoom: 4
   	}
);

var map = new ol.Map({ 
	layers: [layer],
	target: 'map',
	view: view
});


function jump( lon, lat, zoom) {
	try{
		var lonlat = ol.proj.transform( [lon, lat], 'EPSG:4326', 'EPSG:3857' );
		view.setCenter( lonlat );
	}
	catch( err ){
		alert( err );
	}
}
 
function movex( step ) {
	var center = map.getView().getCenter();
	alert( center);
	center[0] = center[0] + step;
	alert( center[0]);
	view.setCenter( center[0], center[1] );
}

function movey( step ) {
	var center = map.getView().getCenter();
	center[1] = center[1] + step;
	view.setCenter( center[0], center[1] );
}

function zoomin(){
    view.setZoom( view.getZoom() - 1);
}

function zoomout(){
    view.setZoom( view.getZoom() + 1);
}