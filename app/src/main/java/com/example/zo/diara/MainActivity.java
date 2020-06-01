package com.example.zo.diara;

import android.Manifest;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.zo.diara.ModelClasses.Item;
import com.example.zo.diara.RecyclableFunctions.GPSTracker;
import com.example.zo.diara.RecyclableFunctions.LocationClass;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private TextView tLogin;
    private ConfigPref configPref;
    private final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
    private LocationClass locationClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configPref = new ConfigPref(this);
        locationClass = new LocationClass(this);

        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Intent toEnableLoc = new Intent(MainActivity.this, PermissionsActivity.class);
            startActivity(toEnableLoc);
            finish();
        }else{
            GPSTracker gpsTracker = new GPSTracker(this);
            Location location = gpsTracker.getLocation();

            configPref.setLoc((float)location.getLatitude(), (float) location.getLongitude());

            if(configPref.getUserID() != 0){
                updateItemCoordinates(String.valueOf(configPref.getLat()), String.valueOf(configPref.getLong()));
            }

        }
        BottomNavigationView bottomNavigationView = findViewById(R.id.botNavigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        bottomNavigationView.setSelectedItemId(R.id.navSettings);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new ProfileFragment(this)).commit();

    }
    public void updateItemCoordinates(String lat, String lon){
        Call<Item> call = ApiClient
                .getInstance()
                .getApi()
                .updateItemCoord(lat, lon, configPref.getUserID());
        call.enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                if(response.isSuccessful()){
                    if(response.body() != null){
                        Log.i("UpdateCoord","Updated Item Coordinates Successfully");
                    }else{
                        Log.i("ERROR_DIARA","No response returned. &&retrofit2");
                    }
                }else{
                    Log.i("ERROR_DIARA","Response is not successfull.");
                }
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                Log.i("ERROR_DIARA", t.getLocalizedMessage());
            }
        });
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFrag = null;

            switch (menuItem.getItemId()){
                case R.id.navSettings:
                    selectedFrag = new ProfileFragment(MainActivity.this);
                    break;
                case R.id.navArchive:
                    selectedFrag = new ArchivedItems(MainActivity.this);
                    break;
                case R.id.navItems:
                    selectedFrag = new ItemFragment(MainActivity.this);
                    break;
                case R.id.navChats:
                    selectedFrag = new ChatFragment(MainActivity.this);
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, selectedFrag).commit();

            return true;
        }
    };
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onStart() {
        super.onStart();

        if(!configPref.readLoginStatus()){
            Intent toLogin = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(toLogin);
            finish();
        }
    }
}
