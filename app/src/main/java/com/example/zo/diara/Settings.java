package com.example.zo.diara;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;

import android.app.AlertDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import dmax.dialog.SpotsDialog;

public class Settings extends AppCompatActivity{
    private ConfigPref configPref;
    private Button  btnLogout;
    private AppCompatSeekBar progDistance;
    private CrystalRangeSeekbar progPriceRange;
    private TextView textMaximumDistance, textEmail,
            textLocation, textVerifyStatus,
            textFilter, textPriceRange;
    private FirebaseAuth mAuth;
    private AlertDialog alertDialog;
    private Geocoder geocoder;
    private List<Address> addresses;
    private LinearLayout btnFeedback, btnDiscoveryPreference;
    NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("en-PH", "PH"));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        configPref = new ConfigPref(this);
        btnLogout = findViewById(R.id.btnLogout);
        progDistance = findViewById(R.id.progDistance);
        textMaximumDistance = findViewById(R.id.textMaximumDistance);
        textEmail = findViewById(R.id.textEmailAddress);
        btnFeedback = findViewById(R.id.setFeedback);
        btnDiscoveryPreference = findViewById(R.id.setFilterDistance);
        textLocation = findViewById(R.id.textAddressSimplified);
        textVerifyStatus = findViewById(R.id.textVerifyStat);
        textFilter = findViewById(R.id.textFilter);
        textPriceRange = findViewById(R.id.textPriceRange);
        progPriceRange = findViewById(R.id.progPriceRange);


        progPriceRange.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                String min = String.valueOf(minValue);
                String max = String.valueOf(maxValue);

                if(Double.parseDouble(max) >= 30000){
                    configPref.setPriceRangeMin((float)Double.parseDouble(min));
                    configPref.setPriceRangeMax((float)Double.parseDouble(max));
                    textPriceRange.setText(format.format(minValue) + " - " + format.format(maxValue) + "+");
                }else{
                    configPref.setPriceRangeMin((float)Double.parseDouble(min));
                    configPref.setPriceRangeMax((float)Double.parseDouble(max));
                    textPriceRange.setText(format.format(minValue) + " - " + format.format(maxValue));
                }

            }
        });

        geocoder = new Geocoder(this, Locale.getDefault());

        alertDialog = new SpotsDialog.Builder().setContext(this).build();
        mAuth = FirebaseAuth.getInstance();

        getSupportActionBar().setTitle("Settings");

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.show();
                mAuth.signOut();
                configPref.logoutUser();
                alertDialog.dismiss();
                startActivity(new Intent(Settings.this, LoginActivity.class));
                finishAffinity();
            }
        });




        btnDiscoveryPreference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this, DiscoveryPreference.class));
            }
        });

        btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this, Feedback.class));
            }
        });

        progDistance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                configPref.setDistance(progress);
                textMaximumDistance.setText(progress + "KM");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public String getFilterEquivalent(int filterCode){
        String filterEquiv = "";
        switch (filterCode){
            case 1111:
                filterEquiv = "Near First";
                break;
            case 2222:
                filterEquiv = "Far First";
                break;
            case 3333:
                filterEquiv = "Most Expensive";
                break;
            case 4444:
                filterEquiv = "Least Expensive";
                break;
            case 5555:
                filterEquiv = "Most Likes";
                break;
            case 6666:
                filterEquiv = "Least Likes";
                break;
        }
        return filterEquiv;
    }

    @Override
    protected void onStart() {
        super.onStart();

        textMaximumDistance.setText(configPref.getDistance() + "KM");
        progDistance.setProgress(configPref.getDistance());
        textFilter.setText(getFilterEquivalent(configPref.getDiscoveryFilter()));
        if(configPref.getPriceRangeMax() >= 30000){
            textPriceRange.setText(format.format(configPref.getPriceRangeMin()) + " - " + format.format(configPref.getPriceRangeMax()) + "+");
        }else{
            textPriceRange.setText(format.format(configPref.getPriceRangeMin()) + " - " + format.format(configPref.getPriceRangeMax()));
        }
        progPriceRange.setMinValue(0f);
        progPriceRange.setMaxValue(30000f);
        progPriceRange.setMinStartValue(configPref.getPriceRangeMin());
        progPriceRange.setMaxStartValue(configPref.getPriceRangeMax());
        progPriceRange.setGap(5000);
        progPriceRange.apply();

        try {
            addresses = geocoder.getFromLocation(configPref.getLat(), configPref.getLong(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

        String address="No Location", postalCode="0000", city="No Location", country = "";
        try {
            city = addresses.get(0).getLocality();
            country = addresses.get(0).getCountryName();

        }catch (Exception ex){
            Toast.makeText(this,ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
        if(city != null)
            textLocation.setText(city + ", " + country);
        else
            textLocation.setText(country);
        textEmail.setText(configPref.getUsername());
    }

    public void toLogin(){
        Intent intent = new Intent(Settings.this, LoginActivity.class);
        startActivity(intent);
        finishAffinity();
    }
}
