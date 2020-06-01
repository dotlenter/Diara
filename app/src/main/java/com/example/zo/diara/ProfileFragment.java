package com.example.zo.diara;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zo.diara.ModelClasses.ItemListModel;
import com.example.zo.diara.ModelClasses.ItemsNearbyModel;
import com.example.zo.diara.ModelClasses.StatMessageModel;
import com.example.zo.diara.RecyclableFunctions.ItemClickSupport;
import com.example.zo.diara.RecyclableFunctions.LocationClass;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment {

    private FloatingActionButton btnSetting;
    private Context context;
    private LinearLayout btnAddItem;
    private ConfigPref configPref;
    private TextView textFullName, textAddress, textUsername, textFirstLetter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.OnClickListener clickListener;
    private FloatingActionButton btnChoosePicture;
    private Bitmap bitmap;
    private ImageView profileImage;
    private GifImageView loadGif;
    private String image = "";
    private List<ItemsNearbyModel> itemListModels;
    Geocoder geocoder;
    List<Address> addresses;
    public ProfileFragment(){

    }
    public ProfileFragment(Context context){
        this.context=context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View mainView = inflater.inflate(R.layout.fragment_profile, container, false);
        btnSetting = mainView.findViewById(R.id.btnSettings);
        btnAddItem = mainView.findViewById(R.id.btnAddItem);
        configPref = new ConfigPref(context);
        recyclerView = mainView.findViewById(R.id.recycListings);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        profileImage = mainView.findViewById(R.id.imgUserImage);
        btnChoosePicture = mainView.findViewById(R.id.btnChoosePicture);
        loadGif = mainView.findViewById(R.id.imgLoad);

        textFullName = mainView.findViewById(R.id.textFullName);
        textAddress = mainView.findViewById(R.id.textAddress);
        textUsername = mainView.findViewById(R.id.textUsername);
        textFirstLetter = mainView.findViewById(R.id.textFirstLetter);
        geocoder = new Geocoder(context, Locale.getDefault());


        getUserItems();

        ItemClickSupport.addTo(recyclerView)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Intent itemIntent = new Intent(context, ViewItem.class);
                        itemIntent.putExtra("itemID", itemListModels.get(position).getItemID());
                        itemIntent.putExtra("userID", itemListModels.get(position).getUserID());
                        itemIntent.putExtra("itemTitle", itemListModels.get(position).getItemTitle());
                        itemIntent.putExtra("itemDesc", itemListModels.get(position).getItemDescription());
                        itemIntent.putExtra("itemImgURL", itemListModels.get(position).getItemImageURL());
                        itemIntent.putExtra("userImgURL", itemListModels.get(position).getUserImage());
                        itemIntent.putExtra("itemPrice", itemListModels.get(position).getItemPrice());
                        itemIntent.putExtra("itemLikes", itemListModels.get(position).getItemLikes());
                        itemIntent.putExtra("itemArchived", itemListModels.get(position).getArchiveCount());
                        itemIntent.putExtra("userEmail", itemListModels.get(position).getMessage());
                        startActivity(itemIntent);
                    }
                });

        btnChoosePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isStoragePermissionGranted()){
                    chooseFile();
                }else{
                    Log.v("RevokedPermission","Permission is revoked");
                    ActivityCompat.requestPermissions(new MainActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
            }
        });

        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(new MainActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                    } else {
                        // No explanation needed; request the permission

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                } else {
                    Intent toAddItem = new Intent(context, AddItem.class);
                    toAddItem.putExtra("LAT", configPref.getLat());
                    toAddItem.putExtra("LONG", configPref.getLong());
                    startActivity(toAddItem);
                }
            }
        });

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startSetting = new Intent(context, Settings.class);
                startSetting.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(startSetting);
            }
        });

        Glide.with(context)
                .load(configPref.getUserImageURL())
                .centerCrop()
                .into(profileImage);

        return mainView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onStart() {
        super.onStart();
        textFullName.setText(configPref.getUsername());

        try {
            addresses = geocoder.getFromLocation(configPref.getLat(), configPref.getLong(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

        String country = "", city = "";
        try {
            country = addresses.get(0).getCountryName();
            city = addresses.get(0).getLocality();
        }catch(Exception ex){

        }
        if(city != null)
            textAddress.setText(city + ", " + country);
        else
            textAddress.setText(country);

        textUsername.setText(configPref.getUsername());

    }

    public void getUserItems(){
        Call<List<ItemsNearbyModel>> call = ApiClient
                .getInstance()
                .getApi()
                .getUserListedItems(configPref.getUserID());

        call.enqueue(new Callback<List<ItemsNearbyModel>>() {
            @Override
            public void onResponse(Call<List<ItemsNearbyModel>> call, Response<List<ItemsNearbyModel>> response) {
                if(response.isSuccessful() && response.body() != null){
                    itemListModels = response.body();
                    Log.i("itemListModel", response.body().toString());

                    UserItemsAdapter userItemsAdapter = new UserItemsAdapter(context, itemListModels);
                    recyclerView.setAdapter(userItemsAdapter);
                    userItemsAdapter.notifyDataSetChanged();
                    loadGif.setVisibility(View.INVISIBLE);
                }else{
                    Toast.makeText(context, response.errorBody().toString(), Toast.LENGTH_LONG).show();
                    loadGif.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<ItemsNearbyModel>> call, Throwable t) {
                Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                loadGif.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void updateUserImage(){
        Call<StatMessageModel> modelCall = ApiClient
                .getInstance()
                .getApi()
                .updateUserImage(image, configPref.getUserID());
        modelCall.enqueue(new Callback<StatMessageModel>() {
            @Override
            public void onResponse(Call<StatMessageModel> call, Response<StatMessageModel> response) {
                if(response.isSuccessful() && response.body() != null){
                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    configPref.setUserImageURL(response.body().getMessage());
                }else{
                    Toast.makeText(context, response.errorBody().toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<StatMessageModel> call, Throwable t) {
                Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {

                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), filePath);
                profileImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                profileImage.setImageBitmap(bitmap);

                if(bitmap != null) {
                    image = getStringImage(bitmap);
                }else{
                    image = "";
                }

                updateUserImage();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 60, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("PermissionSuccess","Permission is granted");
                return true;
            } else {

                Log.v("RevokedPermission","Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
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
}
