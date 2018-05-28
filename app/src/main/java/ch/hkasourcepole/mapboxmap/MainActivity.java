package ch.hkasourcepole.mapboxmap;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    private MapView mapView;
    private String url = "http://10.0.2.2:7001/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Mapbox.getInstance(this, getString(R.string.access_token));

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

                            OkHttpClient okHttpClient = new OkHttpClient();

                            String latitude_first = String.valueOf(mapboxMap.getMarkers().get(0).getPosition().getLatitude());
                            String longtitude_first = String.valueOf(mapboxMap.getMarkers().get(0).getPosition().getLongitude());
                            String latitude_second = String.valueOf(mapboxMap.getMarkers().get(1).getPosition().getLatitude());
                            String longtitude_second = String.valueOf(mapboxMap.getMarkers().get(1).getPosition().getLongitude());

                            String new_url = String.format(url + "route?from_pos=%s,%s&to_pos=%s,%s", longtitude_first, latitude_first, longtitude_second, latitude_second);

                            Request request = new Request.Builder().url(new_url).build();
                            okHttpClient.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    Log.e("FAILURE WITH GET", e.getMessage());
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    String geoJsonString = response.body().string();

                                    GeoJsonSource source = new GeoJsonSource("jsonsource", geoJsonString);
                                    mapboxMap.addSource(source);
                                    LineLayer lineLayer = new LineLayer("jsonlayer", "jsonsource");
                                    lineLayer.setProperties(
                                            PropertyFactory.lineWidth(4f),
                                            PropertyFactory.lineColor(Color.parseColor("#FF0000"))
                                    );
                                    mapboxMap.addLayer(lineLayer);
                                }
                            });
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
                Intent intent = new Intent(MainActivity.this, DrawPointActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton add_line = (FloatingActionButton) findViewById(R.id.add_line);
        add_line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DrawLineActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton add_polygon = (FloatingActionButton) findViewById(R.id.add_polygon);
        add_polygon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DrawPolygonActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton routing = (FloatingActionButton) findViewById(R.id.routing);
        routing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
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