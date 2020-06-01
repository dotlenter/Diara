package com.example.zo.diara.RecyclableFunctions;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.zo.diara.ConfigPref;

public class GPSTracker extends Service implements LocationListener {

    private final Context mContext;
    private ConfigPref configPref;
    // flag for GPS Status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    boolean canGetLocation = false;

    private Location location;
    private double latitude, longitude;

    // The minimum distance to change updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 20;

    // The minimum time beetwen updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 500;

    // Declaring a Location Manager
    protected LocationManager locationManager;
    public GPSTracker(Context context) {
        this.mContext = context;
        getLocation();
    }

    @SuppressLint("MissingPermission")
    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // location service disabled
            } else {
                this.canGetLocation = true;

                // if GPS Enabled get lat/long using GPS Services

                if (isGPSEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    Log.d("GPS Enabled", "GPS Enabled");

                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        updateGPSCoordinates(location);
                    }
                }

                // First get location from Network Provider
                if (isNetworkEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        Log.d("Network", "Network Enabled");

                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            updateGPSCoordinates(location);
                        }
                    }

                }
            }
        } catch (Exception e) {
            // e.printStackTrace();
            Log.e("Error : Location",
                    "Impossible to connect to LocationManager", e);
        }

        return location;
    }
    public void updateGPSCoordinates(Location location) {
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location=location;
        updateGPSCoordinates(location);
        configPref = new ConfigPref(mContext);
        configPref.setLoc((float)latitude,(float)longitude);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
