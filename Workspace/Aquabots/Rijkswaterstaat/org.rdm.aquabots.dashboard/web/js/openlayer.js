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

var draw; // global so we can remove it later
var pointDraw;
var pointer = 0;

function initInteraction() {
	draw = addInteraction( 'LineString' );
	draw.on('drawend', function(e) {
		sendCoordinates( 'drawend', e );
	});
	draw.on('drawstart', function(e) {
		sendCoordinates( 'drawstart', e );
	});

	pointDraw = addInteraction('Point');
	pointDraw.on('drawend', function(e) {
		sendCoordinates( 'drawend', e );
	});
}

/**
 * Send the given coordinates to the servlet
 * @param coordinates
 */
function sendCoordinates( tp, e ){
	var geometry = e.feature.getGeometry();
	var coords = geometry.getCoordinates();  
	var str = ol.proj.transform( coords, 'EPSG:3857', 'EPSG:4326');
	$.get("MapServlet", { type: tp, style: geometry.getType(), coordinates: escape( str )}).done( function(data) {
		  alert(data);
	});
	console.log( tp, str );	
}

function addInteraction( tp) {
	var drw = new ol.interaction.Draw({
		features: featureOverlay.getFeatures(),
		type: (tp)
	    }
    );
    map.addInteraction(drw);
    return drw;
}

/**
* Let user change the geometry type.
*/
function typeSelect( type ){
 try{
	map.removeInteraction(draw);
	draw = addInteraction( type );
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

function drawBorder( top, bottom){
	var map, bounds, coords, defaults;
	var mapnik = new OpenLayers.Layer.OSM();
	defaults = {
			n : 50.930985,
			s : 50.9301,
			w : 11.58725,
			e : 11.58825
	};
	coords = $.extend(defaults, param);
	bounds = new OpenLayers.Bounds();
	bounds.extend(new OpenLayers.LonLat(coords.w, coords.s));
	bounds.extend(new OpenLayers.LonLat(coords.e, coords.n));
	bounds.transform(new OpenLayers.Projection("EPSG:4326"), new OpenLayers.Projection("EPSG:900913"));
	map = new OpenLayers.Map("map");
	if ((coords.s == coords.n) && (coords.w == coords.e)) {
		var marker = new OpenLayers.Layer.Markers("marker");
		var size = new OpenLayers.Size(21, 25);
		var offset = new OpenLayers.Pixel(-(size.w / 2), -size.h);
		var icon = new OpenLayers.Icon('http://www.openlayers.org/dev/img/marker.png', size, offset);
		marker.addMarker(new OpenLayers.Marker(bounds.getCenterLonLat(), icon));
		map.addLayer(marker);
		// Note that if you pass an icon into the Marker constructor, it will
		// take that icon and use it. This means that you should not share icons
		// between markers -- you use them once, but you should clone() for any
		// additional markers using that same icon.
	} else {
		var boxes = new OpenLayers.Layer.Boxes("Boxes");
		var box = new OpenLayers.Marker.Box(bounds, "#008DCF", 4);
		boxes.addMarker(box);
		map.addLayer(boxes);
	}
	map.addLayer(mapnik);
	map.zoomToExtent(bounds);
}

initInteraction();