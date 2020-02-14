package com.example.foodsmap;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Location_PermissionActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener {

    private SupportMapFragment mMapFragment;

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;

        if (mLocationPermissionsGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            init();
        }
    }

    private static final String TAG = "MapActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private static final int PLACE_PICKER_REQUEST = 1;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));


    //widgets
    private EditText mSearchText;
    //vars
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location__permission);

        //haritayı gizler.
        mMapFragment = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map));
        mMapFragment.getView().setVisibility(View.INVISIBLE);

        getLocationPermission();
    }

    private void init() {
        Log.d(TAG, "init: initializing");

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
    }

    private void geoLocate(){
        Log.d(TAG, "geoLocate: geolocating");

        String searchString = mSearchText.getText().toString();

        Geocoder geocoder = new Geocoder(Location_PermissionActivity.this);

        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchString, 1);
        }catch (IOException e){
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage() );
        }

        if(list.size() > 0){
            Address address = list.get(0);

            Log.d(TAG, "geoLocate: found a location: " + address.toString());
            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();

            /*moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM,
                    address.getAddressLine(0));*/
        }
    }


    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        try{
            if(mLocationPermissionsGranted){


                ////////

                ///////
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();

                            /////

                            try {
                                 Geocoder gCoder = new Geocoder(Location_PermissionActivity.this);
                                 List<Address> addresses;

                                Double longitude = currentLocation.getLongitude() ;
                                Double latitude = currentLocation.getLatitude();
                                String longit = Double.toString(longitude);
                                String lat = Double.toString(latitude);
                                addresses = gCoder.getFromLocation(latitude, longitude, 1);

                               String a="";
                                if (addresses != null && addresses.size() > 0) {
                                    StringBuilder stringBuilder = new StringBuilder();

                                    if (addresses.get(0).getThoroughfare() != null) {
                                        stringBuilder.append(addresses.get(0).getThoroughfare());
                                        stringBuilder.append(", ");
                                    }

                                    if (addresses.get(0).getSubLocality() != null) {
                                        stringBuilder.append(addresses.get(0).getSubLocality());
                                        stringBuilder.append(", ");
                                    }

                                    if (addresses.get(0).getSubAdminArea() != null) {
                                       a= stringBuilder.append(addresses.get(0).getSubAdminArea()).toString();
                                    }


                                    StringTokenizer tokens = new StringTokenizer(a.toString(), ",");
                                    String first = tokens.nextToken();
                                    String second = tokens.nextToken();
                                    String three = tokens.nextToken();


                                    //et_adress.setText(stringBuilder.toString());
                                    Toast.makeText(Location_PermissionActivity.this, three, Toast.LENGTH_SHORT).show();

                                    konumkaydet(longit,lat,three);
                                    Intent intent = new Intent(Location_PermissionActivity.this, DenemeActivity.class);
                                    intent.putExtra("long", longit);
                                    intent.putExtra("lat", lat);
                                    intent.putExtra("ilce", three.toLowerCase());
                                    startActivity(intent);
                                    finish();

                                }
                            } catch (IOException | NullPointerException e) {
                                e.printStackTrace();
                                Toast.makeText(Location_PermissionActivity.this, "hata burada", Toast.LENGTH_SHORT).show();

                            }
                            /////

                           /* moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM,
                                    "My Location");
                            */

                            /*Double longitude = currentLocation.getLongitude() ;
                            Double latitude = currentLocation.getLatitude();
                            String longit = Double.toString(longitude);
                            String lat = Double.toString(latitude);
                            konumkaydet(longit,lat);
                            Intent intent = new Intent(Location_PermissionActivity.this, DenemeActivity.class);
                            intent.putExtra("long", longit);
                            intent.putExtra("lat", lat);
                            startActivity(intent);
                            finish();*/



                        }else{
                            Log.d(TAG, "onComplete: current location is null");
                            Intent intent=new Intent(Location_PermissionActivity.this,IndexActivity.class);
                            startActivity(intent);

                            Toast.makeText(Location_PermissionActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }


        }catch (SecurityException e){
            Intent intent=new Intent(Location_PermissionActivity.this,IndexActivity.class);
            startActivity(intent);
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }

    }

    private void konumkaydet(String longitude,String latitude,String ilce){
        String currentUser= FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef=database.getReference().child("Konum").child(currentUser).child("longitude");
        DatabaseReference myRef2=database.getReference().child("Konum").child(currentUser).child("latitude");
        DatabaseReference myRef3=database.getReference().child("Konum").child(currentUser).child("ilce");
        myRef.setValue(longitude);
        myRef2.setValue(latitude);
        myRef3.setValue(ilce);
    }

    public String deneme(){
        String currentUser= FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("Konum").child(currentUser).child("ilce");
        final String[] ilce={""};
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ilce[0]=dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return ilce[0];
    }
    /*private void moveCamera(LatLng latLng, float zoom, String title){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if(!title.equals("My Location")){
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title);
            mMap.addMarker(options);
        }

       // hideSoftKeyboard();

        Intent intent = new Intent(MapActivity.this, DenemeActivity.class);
        Double longitude = latLng.longitude ;
        Double latitude = latLng.latitude;
        String longit = Double.toString(longitude);
        String lat = Double.toString(latitude);
        intent.putExtra("long", longit);
        intent.putExtra("lat", lat);
        startActivity(intent);
    }*/

    private void initMap(){
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(Location_PermissionActivity.this);
    }

    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }
}
