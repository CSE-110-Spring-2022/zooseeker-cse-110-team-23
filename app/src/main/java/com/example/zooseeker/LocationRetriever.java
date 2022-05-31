package com.example.zooseeker;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;


import androidx.annotation.NonNull;

public class LocationRetriever extends FragmentActivity{
    private Location currentLocation;
    private final PermissionChecker permissionChecker = new PermissionChecker(this);

    public void onGetLocation() {


        /* Permissions setup */
        if (permissionChecker.ensurePermissions()) return;

        // listen for location updates
        {
            String provider = LocationManager.GPS_PROVIDER;
            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    Log.d("ZooSeeker", String.format("Location changed: %s", location));
                    currentLocation = location;
                }
            };
            locationManager.requestLocationUpdates(provider, 0, 0f, locationListener);
        }
    }
    private boolean ensurePermissions() {
        return permissionChecker.ensurePermissions();
    }
}
