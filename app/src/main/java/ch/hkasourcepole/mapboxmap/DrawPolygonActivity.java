package ch.hkasourcepole.mapboxmap;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolygonOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import java.util.ArrayList;
import java.util.List;

public class DrawPolygonActivity extends AppCompatActivity {

    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapView = findViewById(R.id.mapView);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {

                mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {

                    @Override
                    public void onMapClick(@NonNull LatLng point) {
                        mapboxMap.addMarker(new MarkerOptions()
                                    .position(point)
                                    .snippet("coordinates: " + point));

                        if (mapboxMap.getMarkers().size() > 2) {

                            mapboxMap.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
                                @Override
                                public boolean onMarkerClick(@NonNull Marker marker) {
                                    return false;
                                }
                            });

                            List<Marker> markers = mapboxMap.getMarkers();
                            List<LatLng> polygon = new ArrayList<>();
                            for (int i=0; i<markers.size(); i++){
                                polygon.add(new LatLng(markers.get(i).getPosition().getLatitude(), markers.get(i).getPosition().getLongitude()));
                            }
                            mapboxMap.addPolygon(new PolygonOptions()
                                    .addAll(polygon)
                                    .fillColor(Color.parseColor("#3bb2d0")));
                        }
                    }
                });
            }
        });

        FloatingActionButton add_point = (FloatingActionButton) findViewById(R.id.add_point);
        add_point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DrawPolygonActivity.this, DrawPointActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton add_line = (FloatingActionButton) findViewById(R.id.add_line);
        add_line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DrawPolygonActivity.this, DrawLineActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton add_polygon = (FloatingActionButton) findViewById(R.id.add_polygon);
        add_polygon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DrawPolygonActivity.this, DrawPolygonActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton routing = (FloatingActionButton) findViewById(R.id.routing);
        routing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DrawPolygonActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
