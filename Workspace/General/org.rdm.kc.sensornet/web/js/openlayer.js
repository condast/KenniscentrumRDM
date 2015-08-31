var layer;

var view;

var map;

//The features are not added to a regular vector layer/source,
//but to a feature overlay which holds a collection of features.
//This collection is passed to the modify and also the draw
//interaction, so that both can add or modify features.
var featureOverlay;
var features;

//the SHIFT key must be pressed to delete vertices, so
//that new vertices can be drawn at the same position
//of existing vertices
var modify;

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
	try{
		var geometry = e.feature.getGeometry();
		var coords = geometry.getCoordinates();  
		var transform = [];
		var lonlat;
		var i = 0;
		for( i=0; i<coords.length; i++ ){
		  lonlat = ol.proj.transform( coords[i], "EPSG:3857",  "EPSG:4326" );
		  transform[i] = lonlat;
		}
		$.get( servlet, { type: tp, style: geometry.getType(), coordinates: escape( transform )}).done( function(data) {
			console.log(data);
		});
		console.log( servlet );
	}catch( e ){
		alert(servlet + ": " + e);
	}
}

/**
 * Send the given coordinates to the servlet
 * @param coordinates
 */
function sendBox( box, tp, e ){
	try{
		var geometry = box.getGeometry();
		var extent = geometry.getExtent();  
		var str = ol.proj.transform( extent, 'EPSG:3857', 'EPSG:4326');
		$.get( "BoundsServlet", { type: tp, style: geometry.getType(), extent: escape( str )}).done( function(data) {
			console.log(data);
		});
		console.log( servlet );
	}catch( e ){
		alert(servlet + ": " + e);
	}
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
	var boundingBox;
	try{
		boundingBox = new ol.interaction.DragBox({
			condition: ol.events.condition.shiftKeyOnly,
			persist: true,
			style: new ol.style.Style({
				stroke: new ol.style.Stroke({
					color: [255,0,255,1]
				})
			})
		});

		map.addInteraction(boundingBox)
	}
	catch( e ){
		alert( e );
		console.log( e );
	}
	return boundingBox;
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
		console.log( err );
	}
}

function jump( lon, lat, zoom) {
	try{
		var lonlat = ol.proj.transform( [lon, lat], 'EPSG:4326', 'EPSG:3857' );
		view.setCenter( lonlat );
	}
	catch( err ){
		console.log( err );
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

function drawBorder( n, w, s, et ){
	try{
		var nt = 685205.44;
		var st = 685274.44;
		var es = 6845483.0;
		var ws = 6845416.0;
		var stroke = new ol.style.Stroke({color: 'black', width: 2});
		var fill = new ol.style.Fill({color: 'red'});
		
		var square = new ol.style.Style({
		    image: new ol.style.RegularShape({
		        fill: fill,
		        stroke: stroke,
		        points: 1000,
		        radius: 1000,
		        angle: Math.PI / 4
		    })
		});
		var lon = 492107;
		var lat = 5597427995;
		var lonlat = ol.proj.transform( [lon, lat], 'EPSG:4326', 'EPSG:3857' );
		var feature = new ol.Feature({
			geometry: new ol.geom.Point( lonlat ),
			style: square,
			name: 'Null Island Two'
		});

		featureOverlay.addFeature( feature );
	}
	catch( e ){
		alert( e );
	}
}

function log( msg ){
	if ( window.console && window.console.log ) {
		console.log( msg );
	}	
}

/**
 * Activate the interactions with the map
 */
function initInteraction() {
	try{
		layer = new ol.layer.Tile({source: new ol.source.OSM()});

		view = new ol.View({	
			center: [6.15388, 52.24966],
			zoom: 6
		}
		);

		map = new ol.Map({ 
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
		features = new ol.Collection();
		featureOverlay = new ol.source.Vector({
			source: new ol.source.Vector({features: features}),
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
		});
		//featureOverlay.setMap(map);

		//the SHIFT key must be pressed to delete vertices, so
		//that new vertices can be drawn at the same position
		//of existing vertices
		modify = new ol.interaction.Modify({
			features: featureOverlay.getFeatures(),
			deleteCondition: function(event) {
				return ol.events.condition.shiftKeyOnly(event) &&
				ol.events.condition.singleClick(event);
			}
		}
		);
		map.addInteraction(modify);	

	} catch( e ){
		console.log(e);
	}

	try{
		//Functions needed to set coordinates for the GPS
		draw = addDrawInteraction( 'LineString' );
		draw.on('drawend', function(e) {
			sendCoordinates( "MapServlet", 'drawend', e );
		});
	}
	catch( e ){
		console.log( e );
	}
	try{
		//Activate the bounds
		bBox = addExtentInteraction();
		bBox.on('boxend', function( e ){
			sendBox(bBox, 'boxend', e );
			bBox.deactivate();
		});	

	} catch( e ){
		console.log( e );
	}
}