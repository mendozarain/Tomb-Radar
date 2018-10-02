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
import android.widget.TextView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import android.view.View;
import android.widget.Button;

import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class search_map extends AppCompatActivity implements OnMapReadyCallback,LocationEngineListener,PermissionsListener{


    private MapView mapView;
    private MapboxMap map;
    private PermissionsManager permissionsManager;
    private LocationEngine locationEngine;
    private LocationLayerPlugin locationLayerPlugin;
    private Location originLocation;

    private Marker destinationmarker;
    private Marker destinationMarker;
    private Button startbutton;
    private String Fullname, bdate, ddate, area, blk, lot;
    private NavigationMapRoute navigationMapRoute;
    private  static final String TAG ="search_map";

    private double lat,lang;
    private DirectionsRoute currentRoute;

    private Point originposition;
    private Point destinationpostion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_search_map);
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        getSupportActionBar().setTitle("Grave Navigation");

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        startbutton = findViewById(R.id.navigate);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        Fullname = intent.getStringExtra("name");
        lat = Double.parseDouble(intent.getStringExtra("lat"));
        lang = Double.parseDouble(intent.getStringExtra("lang"));
        bdate = intent.getStringExtra("bdate");
        ddate = intent.getStringExtra("ddate");
        area = intent.getStringExtra("area");
        blk = intent.getStringExtra("blk");
        lot = intent.getStringExtra("lot");

        TextView name = (TextView) findViewById(R.id.fullname);
        TextView bdatef = (TextView) findViewById(R.id.bdate);
       TextView ddatef = (TextView) findViewById(R.id.ddate);
        TextView placef = (TextView) findViewById(R.id.place);
        name.setText(Fullname);
        bdatef.setText("Date of Birth:  " + bdate);
        ddatef.setText("Date of Death: " + ddate);
        placef.setText("Area: " + area + " " + "Block: " + blk + " " + "Lot: " + lot);

        startbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean simulateRoute = false;
                NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                        .directionsRoute(currentRoute)
                        .shouldSimulateRoute(simulateRoute)
                        .build();

                // Call this method with Context from within an Activity
                NavigationLauncher.startNavigation(search_map.this, options);
            }
        });

    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
       map = mapboxMap;

        enablelocation();

        LatLng grave = new LatLng(lat,lang);
        destinationMarker= map.addMarker(new MarkerOptions().position(grave));


        destinationpostion = Point.fromLngLat(grave.getLongitude(),grave.getLatitude());
        originposition = Point.fromLngLat(originLocation.getLongitude(),originLocation.getLatitude());



        getRoute(originposition,destinationpostion);
        getRoute(originposition,destinationpostion);
        getRoute(originposition,destinationpostion);
        getRoute(originposition,destinationpostion);
        getRoute(originposition,destinationpostion);
        getRoute(originposition,destinationpostion);
    }

    public MapView getView() {
        return mapView.getRootView() instanceof MapView ? (MapView) mapView.getRootView() : null;
    }

    private void getRoute(Point origin, Point destination){
       NavigationRoute.builder(this)
               .accessToken(Mapbox.getAccessToken())
               .origin(origin)
               .destination(destination)
               .build()
               .getRoute(new Callback<DirectionsResponse>() {
                   @Override
                   public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                       if(response.body() == null){
                           Log.e(TAG,"No routes found");
                           return;

                       }else if(response.body().routes().size() == 0)
                       {
                           Log.e(TAG,"No routes found");
                           return;
                       }

                        currentRoute = response.body().routes().get(0);
                       if(navigationMapRoute != null){
                           navigationMapRoute.removeRoute();
                       }else{

                           navigationMapRoute= new NavigationMapRoute(null,mapView,map);
                       }

                       navigationMapRoute.addRoute(currentRoute);
                   }

                   @Override
                   public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                       Log.e(TAG,"Oh no " + t.getMessage());

                   }
               });

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
            locationLayerPlugin = new LocationLayerPlugin(mapView, map,locationEngine);
            locationLayerPlugin.setLocationLayerEnabled(true);
            locationLayerPlugin.setCameraMode(CameraMode.TRACKING);
            locationLayerPlugin.setRenderMode(RenderMode.COMPASS);

     }

     private  void setCameraPosition(Location location){
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()),15));

     }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
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
        if(locationEngine != null){
        locationEngine.removeLocationUpdates();
        }

        if(locationLayerPlugin != null){
            locationLayerPlugin.onStop();
        }
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
        if(locationEngine != null){
            locationEngine.deactivate();
        }
        mapView.onDestroy();
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }



}
