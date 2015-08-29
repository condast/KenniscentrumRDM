var layer = new ol.layer.Tile({source: new ol.source.OSM()});

var view = new ol.View({	
	  center: [6.15388, 52.24966],
   	  zoom: 6
   	}
);

var map = new ol.Map({ 
	controls: ol.control.defaults().extend([
	    new ol.control.FullScreen()
	]),
	layers: [layer],
	target: 'map',
	view: view
});

//The features are not added to a regular vector layer/source,
//but to a feature overlay which holds a collection of features.
//This collection is passed to the modify and also the draw
//interaction, so that both can add or modify features.
var featureOverlay = new ol.FeatureOverlay({
	style: new ol.style.Style({
		fill: new ol.style.Fill({
			color: 'rgba(255, 255, 255, 0.2)'
		}),
		stroke: new ol.style.Stroke({
			color: '#ffcc33',
			width: 2
		}),
		image: new ol.style.Circle({
			radius: 7,
			fill: new ol.style.Fill({
				color: '#ffcc33'
			})
		})
	})
	}
);
featureOverlay.setMap(map);

//the SHIFT key must be pressed to delete vertices, so
//that new vertices can be drawn at the same position
//of existing vertices
var modify = new ol.interaction.Modify({
	features: featureOverlay.getFeatures(),
	deleteCondition: function(event) {
		return ol.events.condition.shiftKeyOnly(event) &&
			ol.events.condition.singleClick(event);
		}
	}
);
map.addInteraction(modify);

//global so we can remove it later
var draw; 
var bBox;
var pointDraw;
var pointer = 0;

/**
 * Send the given coordinates to the servlet
 * @param coordinates
 */
function sendCoordinates( servlet, tp, e ){
	var geometry = e.feature.getGeometry();
	var coords = geometry.getCoordinates();  
	var str = ol.proj.transform( coords, 'EPSG:3857', 'EPSG:4326');
	$.get( servlet, { type: tp, style: geometry.getType(), coordinates: escape( str )}).done( function(data) {
		  //log(data);
	});
	//log( tp + ":" + str );	
}

function addDrawInteraction( tp ) {
	var drw = new ol.interaction.Draw({
		features: featureOverlay.getFeatures(),
		type: (tp)
	    }
    );
    map.addInteraction(drw);
    return drw;
}

//Bounding boxes allow to select a portion of a screen. We use this to limit the
//area the boats can travel in.
function addExtentInteraction(){
	var boundingBox = new ol.interaction.DragBox({
	  condition: ol.events.condition.always,
	  style: new ol.style.Style({
	      stroke: new ol.style.Stroke({
	          color: [0,0,255,1]
	      })
	  })
	});

	map.addInteraction(boundingBox);
	return boundingBox;
}

/**
 * Activate the interactions with the map
 */
function initInteraction() {
	
	//Functions needed to set coordinates for the GPS
	draw = addDrawInteraction( 'LineString' );
	draw.on('drawend', function(e) {
		sendCoordinates( "MapServlet", 'drawend', e );
	});
	//draw.on('drawstart', function(e) {
	//	sendCoordinates( "MapServlet",'drawstart', e );
	//});

	pointDraw = addInteraction('Point');
	pointDraw.on('drawend', function(e) {
		sendCoordinates( "MapServlet",'drawend', e );
	});
	
	//Activate the bounds
	bBox = addExtentInteraction();
	bBox.on('boxend', function(e){
		sendCoordinates("BoundsServlet",'boxend', e );
		//map.removeInteraction(boundingBox);  
	});	
	bBox.on('boxstart', function(e){
		sendCoordinates("BoundsServlet",'boxstart', e );
	});	
}

/**
* Let user change the geometry type.
*/
function typeSelect( type ){
 try{
	map.removeInteraction(draw);
	draw = addDrawInteraction( type );
  }
  catch( err ){
	  alert( err );
  }
}

function jump( lon, lat, zoom) {
	try{
		var lonlat = ol.proj.transform( [lon, lat], 'EPSG:4326', 'EPSG:3857' );
		view.setCenter( lonlat );
	}
	catch( err ){
		alert( err );
	}
}
 
function zoom( zoom ){
    view.setZoom( zoom );
}

function zoomin(){
    view.setZoom( view.getZoom() - 1);
}

function zoomout(){
    view.setZoom( view.getZoom() + 1);
} 

function drawBorder( n, s, w, e ){
	//var boxes  = ol.layer.Boxes("Boxes");
	//var coordinates = [n, s, w, e ];
	//var bounds = OpenLayers.Bounds.fromArray(coordinates).transform(new OpenLayers.Projection("EPSG:4326"), new OpenLayers.Projection("EPSG:900913"));
    //var box = new OpenLayers.Marker.Box(bounds);
    //box.setBorder("blue");
    //boxes.addMarker(box);
    //map.addLayers([boxes]);
    alert('hoi');
    log('OK');
}

//ZoomToExtent
var myExtentButton = new ol.control.ZoomToExtent({
    extent:undefined
});
map.addControl(myExtentButton);


function log( msg ){
	if ( window.console && window.console.log ) {
		  console.log( msg );
	}	
}

initInteraction();