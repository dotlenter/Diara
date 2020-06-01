package com.example.zo.diara;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zo.diara.ModelClasses.ItemsNearbyModel;

import java.util.List;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ArchivedItems extends Fragment {

    private Context context;
    private ConfigPref configPref;
    private List<ItemsNearbyModel> itemList;
    private RecyclerView recycArchivedItems;
    private LinearLayoutManager linearLayoutManager;
    private GifImageView loadGif;

    public ArchivedItems() {
        // Required empty public constructor
    }

    public ArchivedItems(Context cont){
       this.context = cont;
       configPref = new ConfigPref(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_archived_items, container, false);
        recycArchivedItems = view.findViewById(R.id.recycArchivedItems);
        linearLayoutManager = new LinearLayoutManager(context);
        recycArchivedItems.setLayoutManager(linearLayoutManager);
        loadGif = view.findViewById(R.id.imgLoad);

        getArchivedItems();
        return view;
    }

    public void getArchivedItems(){
        Call<List<ItemsNearbyModel>> listCall = ApiClient
                .getInstance()
                .getApi()
                .getArchivedItems(configPref.getUserID());
        listCall.enqueue(new Callback<List<ItemsNearbyModel>>() {
            @Override
            public void onResponse(Call<List<ItemsNearbyModel>> call, Response<List<ItemsNearbyModel>> response) {
                if(response.isSuccessful() && response.body() != null){
                    itemList = response.body();
                    if(itemList.get(0).getStatus() != null){
                        if(itemList.get(0).getStatus().toLowerCase().equals("yes")){
                            ArchivedItemAdapter archivedItemAdapter = new ArchivedItemAdapter(context, itemList);
                            recycArchivedItems.setAdapter(archivedItemAdapter);
                            archivedItemAdapter.notifyDataSetChanged();
                            loadGif.setVisibility(View.INVISIBLE);
                        }else{
                            Log.d("onNullStatus", "No items.");
                            Toast.makeText(context, itemList.get(0).getMessage(), Toast.LENGTH_LONG).show();
                            loadGif.setVisibility(View.INVISIBLE);
                        }
                    }else{
                        Log.d("onNullStatus", "No items.");
                        Toast.makeText(context, "Can't establish an error, please report to our customer service", Toast.LENGTH_LONG).show();
                        loadGif.setVisibility(View.INVISIBLE);
                    }
                }else{
                    Log.i("onEmptyResponse", response.errorBody().toString());
                    Toast.makeText(context, response.errorBody().toString(), Toast.LENGTH_LONG).show();
                    loadGif.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<ItemsNearbyModel>> call, Throwable t) {
                Log.d("onFailureSwipe", t.getLocalizedMessage());
                Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                loadGif.setVisibility(View.INVISIBLE);
            }
        });
    }
}
