package com.automobile.service.locationServices;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sports.connect.R;
import com.sports.connect.SportsEventAplication;

/**
 * Created by kailash on 21/12/2017.
 */

public class CurrentLocation extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private LocationRequest mLocationRequest;
    private static GoogleApiClient mGoogleApiClient;
    //private static android.location.Location mCurrentLocation;


    private DatabaseReference databaseReference;
    private FirebaseDatabase mFirebaseInstance;

    public void checkPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkLocationPermission()) {

                Log.d("CurrentLocation","CurrentLocation start");


                createLocationRequest();
                mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                        .addApi(LocationServices.API)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this).build();
                mGoogleApiClient.connect();
            }

        } else {
            createLocationRequest();
            mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
            mGoogleApiClient.connect();
        }


    }


    @Override
    public void onCreate() {
        super.onCreate();
        //checkPermission();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        mFirebaseInstance = FirebaseDatabase.getInstance();
        databaseReference = mFirebaseInstance.getReference();


        checkPermission();
        return START_STICKY;

    }





    public boolean checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            //  ActivityCompat.requestPermissions(CurrentLocation.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            return false;
        }
    }


    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (mGoogleApiClient == null) {
                        createLocationRequest();
                        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext()).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
                        mGoogleApiClient.connect();
                    }

                } else {

                    Toast.makeText(CurrentLocation.this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }


    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        // mLocationRequest.setNumUpdates(2);
        // mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }


    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();
    }

    protected void startLocationUpdates() {

        try {

            @SuppressLint("MissingPermission") android.location.Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


            if (mLastLocation != null) {
                Log.d("CurrentLatlong==", "Lat:" + mLastLocation.getLatitude() + " Lng:" + mLastLocation.getLongitude());
                setmCurrentLocation(mLastLocation);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }



    }


    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(android.location.Location location) {


        if (null != location) {
            setmCurrentLocation(location);
        }
    }


    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (com.google.android.gms.location.LocationListener) CurrentLocation.this);
    }



    public void setmCurrentLocation(android.location.Location mCurrentLocation) {

        Log.d("setmCurrentLocation","setmCurrentLocation==="+mCurrentLocation.getLatitude());

        if (mCurrentLocation != null )
        {
            String mLoc=mCurrentLocation.getLatitude()+"";

            if(!mLoc.isEmpty())
            {
                SportsEventAplication.getmInstance().savePreferenceDataString(getString(R.string.preferances_current_lat), mCurrentLocation.getLatitude() + "");
                SportsEventAplication.getmInstance().savePreferenceDataString(getString(R.string.preferances_current_lng), mCurrentLocation.getLongitude() + "");

            }

           }


    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }
}