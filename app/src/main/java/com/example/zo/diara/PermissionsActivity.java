package com.example.zo.diara;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PermissionsActivity extends AppCompatActivity {
    private Button btnEnableLoc;
    private final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);
        btnEnableLoc = findViewById(R.id.btnEnableLoc);

        btnEnableLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(PermissionsActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent toMain = new Intent(PermissionsActivity.this, MainActivity.class);
                    startActivity(toMain);
                    finishAffinity();
                } else {
                    ActivityCompat.requestPermissions(PermissionsActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                }
                return;
            }
        }
    }
}
