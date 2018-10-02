package com.example.ramshark.tombradar05;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

// classes needed to initialize map
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;

// classes needed to add location layer
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import android.location.Location;

import com.mapbox.mapboxsdk.geometry.LatLng;

import android.support.annotation.NonNull;

import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;

// classes needed to add a marker
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;

// classes to calculate a route
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;

// classes needed to launch navigation UI
import android.view.View;
import android.widget.Button;

import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class add_map extends AppCompatActivity implements OnMapReadyCallback,LocationEngineListener,PermissionsListener,MapboxMap.OnMapClickListener {
    private MapView mapViewadd_map;

    private MapboxMap mapadd;
    private PermissionsManager permissionsManager;
    private LocationEngine locationEngine;
    private Button startbutton;
    private LocationLayerPlugin locationLayerPlugin;
    private Location originLocation;
    private Point originposition;
    private Point destinationpostion;
    private Marker destinationMarker;
    private double lat, lang;
    private NavigationMapRoute navigationMapRoute;
    private  static final String TAG ="search_map";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_add_map);
        mapViewadd_map = (MapView) findViewById(R.id.mapViewAdd);
        mapViewadd_map.onCreate(savedInstanceState);
        mapViewadd_map.getMapAsync(this);

        startbutton = findViewById(R.id.add);
        getSupportActionBar().setTitle("Add Grave Location");

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



    }
    public MapView getView() {
        return mapViewadd_map.getRootView() instanceof MapView ? (MapView) mapViewadd_map.getRootView() : null;
    }


    public void Add(View view) {
        String latp,langp;
        latp=Double.toString(lat);
        langp=Double.toString(lang);

        Intent intent = new Intent(this,add.class);

        intent.putExtra("lat",latp);
        intent.putExtra("lang",langp);

        startActivity(intent);
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        mapadd = mapboxMap;
        mapadd.addOnMapClickListener(this);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                enablelocation();
            }
        }, 1000);

    }

    private  void enablelocation(){
        if(PermissionsManager.areLocationPermissionsGranted(this)){
            initializedLocationEngine();
            initializedLocationLayer();

        }else{
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);

        }

    }
    @SuppressWarnings("MissingPermission")
    private  void initializedLocationEngine(){
        locationEngine = new LocationEngineProvider(this).obtainBestLocationEngineAvailable();
        locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
        locationEngine.requestLocationUpdates();
        locationEngine.activate();


        Location lastLocation = locationEngine.getLastLocation();
        if (lastLocation != null && lastLocation.getLongitude() != 0 && lastLocation.getLatitude() != 0) {
            originLocation = lastLocation;
            setCameraPosition(lastLocation);
        } else {
            locationEngine.addLocationEngineListener(this);
            locationEngine.activate();
        }


    }
    @SuppressWarnings("MissingPermission")
    private void  initializedLocationLayer(){
        locationLayerPlugin = new LocationLayerPlugin(mapViewadd_map, mapadd,locationEngine);
        locationLayerPlugin.setLocationLayerEnabled(true);
        locationLayerPlugin.setCameraMode(CameraMode.TRACKING);
        locationLayerPlugin.setRenderMode(RenderMode.COMPASS);

    }

    private  void setCameraPosition(Location location){
        mapadd.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()),15));

    }

    @Override
    public void onMapClick(@NonNull LatLng point) {
        if(destinationMarker !=null){
            mapadd.removeMarker(destinationMarker);
        }
        destinationMarker= mapadd.addMarker(new MarkerOptions().position(point));
        destinationpostion = Point.fromLngLat(point.getLongitude(),point.getLatitude());

        startbutton.setEnabled(true);
        startbutton.setBackgroundResource(R.drawable.green_button);
        lat=point.getLatitude();
        lang=point.getLongitude();

    }




    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapViewadd_map.onSaveInstanceState(outState);
    }

    @Override
    @SuppressWarnings("MissingPermission")
    public void onConnected() {
        locationEngine.requestLocationUpdates();

    }

    @Override
    public void onLocationChanged(Location location) {

        if(location != null){
            originLocation = location;
            setCameraPosition(location);

        }
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        //Present toast or dialog

    }

    @Override
    public void onPermissionResult(boolean granted) {
        if(granted){
            enablelocation();

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        permissionsManager.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(locationEngine != null){
            locationEngine.removeLocationUpdates();
        }
        if(locationLayerPlugin != null){
            locationLayerPlugin.onStart();

        }
        mapViewadd_map.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapViewadd_map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapViewadd_map.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(locationEngine != null){
            locationEngine.removeLocationUpdates();
        }

        if(locationLayerPlugin != null){
            locationLayerPlugin.onStop();
        }
        mapViewadd_map.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapViewadd_map.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(locationEngine != null){
            locationEngine.deactivate();
        }
        mapViewadd_map.onDestroy();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
