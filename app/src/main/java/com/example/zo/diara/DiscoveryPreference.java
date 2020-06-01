package com.example.zo.diara;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DiscoveryPreference extends AppCompatActivity {

    private TextView textNearFirst,
            textFarFirst, textMostExpensive,
            textLeastExpensive, textMostLikes,
            textLeastLikes;
    private LinearLayout btnNearFirst,
            btnFarFirst, btnMostExpensive,
            btnLeastExpensive, btnMostLikes,
            btnLeastLikes;

    private ConfigPref configPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery_preference);

        getSupportActionBar().setTitle("Item Discovery");

        textNearFirst = findViewById(R.id.textNearFirst);
        textFarFirst = findViewById(R.id.textFarFirst);
        textMostExpensive = findViewById(R.id.textMostExpensive);
        textLeastExpensive = findViewById(R.id.textLeastExpensive);
        textMostLikes = findViewById(R.id.textMostLikes);
        textLeastLikes = findViewById(R.id.textLeastLikes);

        configPref = new ConfigPref(this);

        btnNearFirst = findViewById(R.id.setNearFirst);
        btnFarFirst = findViewById(R.id.setFarFirst);
        btnMostExpensive = findViewById(R.id.setMostExpensive);
        btnLeastExpensive = findViewById(R.id.setLeastExpensive);
        btnMostLikes = findViewById(R.id.setMostLikes);
        btnLeastLikes = findViewById(R.id.setLeastLikes);


        btnNearFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDiscoveryPref(textNearFirst);
                configPref.setDiscoveryFilter(1111);
            }
        });

        btnFarFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDiscoveryPref(textFarFirst);
                configPref.setDiscoveryFilter(2222);
            }
        });

        btnMostExpensive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDiscoveryPref(textMostExpensive);
                configPref.setDiscoveryFilter(3333);
            }
        });

        btnLeastExpensive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDiscoveryPref(textLeastExpensive);
                configPref.setDiscoveryFilter(4444);
            }
        });

        btnMostLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDiscoveryPref(textMostLikes);
                configPref.setDiscoveryFilter(5555);
            }
        });

        btnLeastLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDiscoveryPref(textLeastLikes);
                configPref.setDiscoveryFilter(6666);
            }
        });

    }

    public void setDiscoveryPref(TextView textView) {
        resetTextViewDrawable(textNearFirst);
        resetTextViewDrawable(textFarFirst);
        resetTextViewDrawable(textMostExpensive);
        resetTextViewDrawable(textLeastExpensive);
        resetTextViewDrawable(textMostLikes);
        resetTextViewDrawable(textLeastLikes);
        textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_black_24dp, 0);
    }

    public void resetTextViewDrawable(TextView textView) {
        textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
    }

    @Override
    protected void onStart() {
        super.onStart();

        switch (configPref.getDiscoveryFilter()){
            case 1111:
                setDiscoveryPref(textNearFirst);
                break;
            case 2222:
                setDiscoveryPref(textFarFirst);
                break;
            case 3333:
                setDiscoveryPref(textMostExpensive);
                break;
            case 4444:
                setDiscoveryPref(textLeastExpensive);
                break;
            case 5555:
                setDiscoveryPref(textMostLikes);
                break;
            case 6666:
                setDiscoveryPref(textLeastLikes);
                break;
        }

    }
}
