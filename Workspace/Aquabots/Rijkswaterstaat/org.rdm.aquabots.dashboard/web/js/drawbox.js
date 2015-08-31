var source = new ol.source.Vector();
var interaction = new ol.interaction.DragBox({
    condition: ol.events.condition.noModifierKeys,
    style: new ol.style.Style({
        stroke: new ol.style.Stroke({
            color: [0, 0, 255, 1]
        })
    })
});

interaction.on('boxend', function (evt) {
    var geom = evt.target.getGeometry();
    console.log(geom);
    var feat = new ol.Feature({
        geometry: geom
    });
    source.addFeature(feat);
});

var map = new ol.Map({
    target: 'map',
    layers: [
        new ol.layer.Tile({
            source: new ol.source.OSM()
        }),
        new ol.layer.Vector({
            source: source
        })
    ],
    view: new ol.View({
        center: [0, 0],
        zoom: 2,
        projection: 'EPSG:3857',
    }),
    interactions: [
        interaction
    ]
});