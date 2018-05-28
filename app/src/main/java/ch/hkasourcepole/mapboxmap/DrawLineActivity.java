package ch.hkasourcepole.mapboxmap;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

public class DrawLineActivity extends AppCompatActivity {

    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_draw_line);
        setContentView(R.layout.activity_main);

        mapView = findViewById(R.id.mapView);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {

                    @Override
                    public void onMapClick(@NonNull LatLng point) {
                        if (mapboxMap.getMarkers().size() < 3) {

                            mapboxMap.addMarker(new MarkerOptions()
                                    .position(point)
                                    .snippet("coordinates: " + point));
                        }

                        if (mapboxMap.getMarkers().size() == 2) {

                            String latitude_start = String.valueOf(mapboxMap.getMarkers().get(0).getPosition().getLatitude());
                            String longtitude_start = String.valueOf(mapboxMap.getMarkers().get(0).getPosition().getLongitude());
                            String latitude_end = String.valueOf(mapboxMap.getMarkers().get(1).getPosition().getLatitude());
                            String longtitude_end = String.valueOf(mapboxMap.getMarkers().get(1).getPosition().getLongitude());

                            String geoJsonString = String.format("{\n" +
                                    "  \"type\": \"FeatureCollection\",\n" +
                                    "  \"features\": [\n" +
                                    "    {\n" +
                                    "      \"type\": \"Feature\",\n" +
                                    "      \"properties\": {\n" +
                                    "        \"name\": \"some json\"\n" +
                                    "      },\n" +
                                    "      \"geometry\": {\n" +
                                    "        \"type\": \"LineString\",\n" +
                                    "        \"coordinates\": [\n" +
                                    "          [\n" +
                                    "            %s,\n" +
                                    "            %s\n" +
                                    "          ],\n" +
                                    "          [\n" +
                                    "            %s,\n" +
                                    "            %s\n" +
                                    "          ]\n" +
                                    "        ]\n" +
                                    "      }\n" +
                                    "    }\n" +
                                    "  ]\n" +
                                    "}", longtitude_start, latitude_start, longtitude_end, latitude_end);

                            GeoJsonSource source = new GeoJsonSource("jsonsource", geoJsonString);
                            mapboxMap.addSource(source);
                            LineLayer lineLayer = new LineLayer("jsonlayer", "jsonsource");
                            lineLayer.setProperties(
                                    PropertyFactory.lineWidth(4f),
                                    PropertyFactory.lineColor(Color.parseColor("#FF0000"))
                            );
                            mapboxMap.addLayer(lineLayer);
                        } else if (mapboxMap.getMarkers().size() == 3) {

                            mapboxMap.clear();
                            mapboxMap.removeLayer("jsonlayer");
                            mapboxMap.removeSource("jsonsource");
                            mapboxMap.addMarker(new MarkerOptions()
                                    .position(point)
                                    .snippet("coordinates: " + point));
                        }
                    }
                });
            }
        });

        FloatingActionButton add_point = (FloatingActionButton) findViewById(R.id.add_point);
        add_point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DrawLineActivity.this, DrawPointActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton add_line = (FloatingActionButton) findViewById(R.id.add_line);
        add_line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DrawLineActivity.this, DrawLineActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton add_polygon = (FloatingActionButton) findViewById(R.id.add_polygon);
        add_polygon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DrawLineActivity.this, DrawPolygonActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton routing = (FloatingActionButton) findViewById(R.id.routing);
        routing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DrawLineActivity.this, MainActivity.class);
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
