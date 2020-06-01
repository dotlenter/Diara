package com.example.zo.diara;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zo.diara.ModelClasses.Item;
import com.example.zo.diara.RecyclableFunctions.Glide4Engine;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddItem extends AppCompatActivity implements View.OnClickListener{

    private ImageView imageView1, imageView2;
    private Button btnSubmitItem;
    private ConfigPref configPref;
    private static final int REQUEST_TAKE_PHOTO = 0;
    private static final int REQUEST_PICK_PHOTO = 2222;
    private Bitmap bitmap, bitmap2;
    private Uri mMediaUri;
    private static final int CAMERA_PIC_REQUEST = 1111;
    private int whereToPutImage = 1;
    private EditText textImageURL, textItemTitle, textItemDescription, textPrice;
    private android.app.AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        imageView1 = findViewById(R.id.itemImage1);
        imageView2 = findViewById(R.id.itemImage2);
        btnSubmitItem = findViewById(R.id.btnSubmitItem);
        //textImageURL = findViewById(R.id.textImageURL);
        textItemTitle = findViewById(R.id.textItemTitle);
        textItemDescription = findViewById(R.id.textDescription);
        textPrice = findViewById(R.id.textItemPrice);
        alertDialog = new SpotsDialog.Builder().setContext(this).build();
        configPref = new ConfigPref(this);


        try {
            Log.i("Location[LAT]", String.valueOf(configPref.getLat()));
            Log.i("Location[LONG]", String.valueOf(configPref.getLong()));
        }catch (Exception ex){
            Toast.makeText(this, ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }

        imageView1.setOnClickListener(this);
        imageView2.setOnClickListener(this);
        btnSubmitItem.setOnClickListener(this);

    }
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("PermissionSuccess","Permission is granted");
                return true;
            } else {

                Log.v("RevokedPermission","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("PermissionSuccess","Permission is granted");
            return true;
        }
    }
    private void chooseFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.itemImage1:
                whereToPutImage = 1;
                if(isStoragePermissionGranted()) {
                   chooseFile();
                }else{
                    Log.v("RevokedPermission","Permission is revoked");
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
                break;
            case R.id.itemImage2:
                whereToPutImage = 2;
                if(isStoragePermissionGranted()) {
                    chooseFile();
                }else{
                    Log.v("RevokedPermission","Permission is revoked");
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
                break;

            case R.id.btnSubmitItem:
                alertDialog.show();
                int flag=0;
                double price = 0;
                String desc = "";
                if(textItemTitle.getText().toString().isEmpty()){
                    textItemTitle.setError("Please enter a title for your listing.");
                    flag++;
                }
                if(!textItemDescription.getText().toString().isEmpty()){
                    desc = textItemDescription.getText().toString();
                }else{
                    desc="...";
                }
                if(!textPrice.getText().toString().isEmpty()){
                   price = doubleTryParse(textPrice.getText().toString(), 0);
                }
                if(flag==0) {
                    String pic1, pic2;

                    if(bitmap == null){
                        pic1 = "";
                    }else{
                        pic1 = getStringImage(bitmap);
                    }

                    addItem(configPref.getUserID(),
                            price,
                            pic1,
                            textItemTitle.getText().toString(),
                            desc,
                            String.valueOf(configPref.getLong()),
                            String.valueOf(configPref.getLat()));
                }
                break;
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 60, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public double doubleTryParse(String value, double defaultVal) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }
    public void addItem(long id, double price, String url, String title, String desc, String lon, String lat){
        Call<Item> call = ApiClient
                .getInstance()
                .getApi()
                .addItem(id,title,desc,price,url,lon,lat);
        call.enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                if(response.isSuccessful()){
                    if(response.body()!=null){
                        alertDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"ITEM SUBMITTED SUCCESSFULLY!", Toast.LENGTH_LONG).show();
                    }else{
                        alertDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"NULL RESPONSE", Toast.LENGTH_LONG).show();
                    }
                }else{
                    alertDialog.dismiss();
                    Toast.makeText(getApplicationContext(),response.errorBody().toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                alertDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void resetFields(){
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Log.v("PermissionResult","Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView1.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView1.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    @Override
    protected void onStart() {
        super.onStart();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageView1.setClipToOutline(true);
            imageView2.setClipToOutline(true);
        }

    }
}
