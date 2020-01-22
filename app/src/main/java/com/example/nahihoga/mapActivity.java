package com.example.nahihoga;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SearchRecentSuggestionsProvider;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.List;

public class mapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlacesClient placesClient;
    private List<AutocompletePrediction> predictionList;

    private Location mLastLoc;
    private LocationCallback locationCallback;

    private View mapView;
    private Button btnloc;

    private final float DEFAULT_ZOOM = 18;

    private double latitude,longitude;

    private String url;
    private int rad = 10000;

    private Button bnk;

    private static final String GOOGLE_API_KEY ="AIzaSyB1ktiraoB07EAK7q89ytZ0cBI9qEUaRLQ";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mmap);
        mapFragment.getMapAsync(this);
        mapView=mapFragment.getView();

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mapActivity.this);
        Places.initialize(mapActivity.this,"AIzaSyB1ktiraoB07EAK7q89ytZ0cBI9qEUaRLQ");
        placesClient=Places.createClient(this);
        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        bnk = (Button)findViewById(R.id.bankButton);
/*
        bnk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Hello Javatpoint",Toast.LENGTH_SHORT).show();

                Object transferData[]=new Object[2];
                getNearby gn = new getNearby();
                url = getUrl(latitude,longitude,"State Bank of India");
                transferData[0]=mMap;
                transferData[1]=url;


                gn.execute(transferData);
            }
        });
*/

        bnk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Hello Javatpoint",Toast.LENGTH_SHORT).show();
                StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                googlePlacesUrl.append("location=" + latitude + "," + longitude);
                googlePlacesUrl.append("&radius=" + rad);
                googlePlacesUrl.append("&types=" + "State"+"Bank"+"of"+"India");
                googlePlacesUrl.append("&sensor=true");
                googlePlacesUrl.append("&key=" + GOOGLE_API_KEY);

                Log.d("mapActivity",googlePlacesUrl.toString());

                GooglePlacesReadTask googlePlacesReadTask = new GooglePlacesReadTask();
                Object[] toPass = new Object[2];
                toPass[0] = mMap;
                toPass[1] = googlePlacesUrl.toString();
                googlePlacesReadTask.execute(toPass);
            }
        });



    }


/*
    private String getUrl(double latitude,double longitude,String near)
    {
        StringBuilder gUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/findplacefromtext/json?");
        gUrl.append("location" + latitude + "," + longitude);
        gUrl.append("&radius="+rad);
        gUrl.append("&type="+near);
        gUrl.append("&sensor="+true);
        gUrl.append("&key="+"AIzaSyB1ktiraoB07EAK7q89ytZ0cBI9qEUaRLQ");
       Log.d(mapActivity,"url="+gUrl.toString());

        return gUrl.toString();
    }*/

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        if (mapView!=null && mapView.findViewById(Integer.parseInt("1"))!= null ){
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP,0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
            layoutParams.setMargins(0,0,40,180);

        }

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(mapActivity.this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());
        task.addOnSuccessListener(mapActivity.this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getDeviceLocation();

            }
        });

        task.addOnFailureListener(mapActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException){
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    try {
                        resolvable.startResolutionForResult(mapActivity.this,51);
                    } catch (IntentSender.SendIntentException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==51){
            if (resultCode== RESULT_OK){
                getDeviceLocation();
            }
        }
    }


    private void getDeviceLocation()
    {
        mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if(task.isSuccessful()) {
                    mLastLoc = task.getResult();
                    if (mLastLoc != null)
                    {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLoc.getLatitude(),mLastLoc.getLongitude()),DEFAULT_ZOOM));
                    }else{
                        final LocationRequest locationRequest = LocationRequest.create();
                        locationRequest.setInterval(10000);
                        locationRequest.setFastestInterval(5000);
                        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                        locationCallback = new LocationCallback(){
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                super.onLocationResult(locationResult);
                                if (locationResult == null)
                                {
                                    return;
                                }
                                mLastLoc = locationResult.getLastLocation();
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLoc.getLatitude(),mLastLoc.getLongitude()),DEFAULT_ZOOM));
                                mFusedLocationProviderClient.removeLocationUpdates(locationCallback);

                            }
                        };
                        mFusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,null);
                    }
                }else{
                    Toast.makeText(mapActivity.this,"Unable to get last location",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
