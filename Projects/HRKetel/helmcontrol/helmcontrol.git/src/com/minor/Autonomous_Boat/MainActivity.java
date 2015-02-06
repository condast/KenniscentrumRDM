package com.minor.Autonomous_Boat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.*;

import java.util.ArrayList;

public class MainActivity extends Activity {
    private GoogleMap googleMap;
    private ArrayList<LatLng> coords = new ArrayList<LatLng>();
    private Marker begin;
    private Marker end;
    private Polyline line;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        try {
            initilizeMap();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * function to load map. If map is not created it will create it for you
     */
    private void initilizeMap() {
        if (this.googleMap == null) {
            this.googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

            // check if map is created successfully or not
            if (this.googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT
                ).show();
            }

            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(51.910065, 4.461865));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

            this.googleMap.moveCamera(center);
            this.googleMap.animateCamera(zoom);
            this.googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    coords.add(latLng);
                    draw();
                }
            });
        }
    }

    public void draw() {
        if (this.line != null) {
            this.line.remove();
        }
        if (this.coords.size() == 0) {
            Toast.makeText(getApplicationContext(),
                    "There are no coordinates!", Toast.LENGTH_SHORT
            ).show();
        }

        PolylineOptions lineOptions = new PolylineOptions();
        for (LatLng coord : this.coords) {
            lineOptions.add(coord);
        }
        line = googleMap.addPolyline(lineOptions);

        MarkerOptions beginOptions;
        if (this.coords.size() == 1) {
            beginOptions = new MarkerOptions().position(this.coords.get(0)).title("Begin").visible(true);
            this.begin = googleMap.addMarker(beginOptions);
        }

        MarkerOptions endOptions = new MarkerOptions();
        if (this.coords.size() > 1) {
            if (this.end != null) {
                this.end.remove();
            }

            endOptions.title("End");
            endOptions = new MarkerOptions().position(this.coords.get(this.coords.size() - 1));
            this.end = googleMap.addMarker(endOptions);
        }
    }

    public ArrayList<LatLng> getCoords() {
        return this.coords;
    }

    private void removeLastMarker() {
        if (this.end != null) {
            this.end.remove();
        }
        if (this.coords.size() > 0) {
            this.coords.remove(coords.size() - 1);
        }

        this.draw();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.undo:
                this.removeLastMarker();
                return true;
            case R.id.start:
                Intent intent = new Intent(this, StartActivity.class);
                intent.putExtra("coords", this.coords);
                this.startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}