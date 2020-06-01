package com.example.zo.diara;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.zo.diara.ModelClasses.ItemsNearbyModel;
import com.example.zo.diara.RecyclableFunctions.ItemClickSupport;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatFragment extends Fragment {

    private Context context;
    private RecyclerView likedItemsRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ConfigPref configPref;
    private List<ItemsNearbyModel> itemList;
    public ChatFragment() {
    }

    public ChatFragment(Context context){
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View tMainView =  inflater.inflate(R.layout.fragment_chat, container, false);
        configPref = new ConfigPref(context);

        //SETUP HORIZONTAL RECYCLERVIEW
        likedItemsRecyclerView = tMainView.findViewById(R.id.likedItemsRecyclerView);
        linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        linearLayoutManager.setReverseLayout(false);
        likedItemsRecyclerView.setLayoutManager(linearLayoutManager);

        getLikedHeadItems();

        ItemClickSupport.addTo(likedItemsRecyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent itemIntent = new Intent(context, ViewItem.class);
                itemIntent.putExtra("itemID", itemList.get(position).getItemID());
                itemIntent.putExtra("userID", itemList.get(position).getUserID());
                itemIntent.putExtra("itemTitle", itemList.get(position).getItemTitle());
                itemIntent.putExtra("itemDesc", itemList.get(position).getItemDescription());
                itemIntent.putExtra("itemImgURL", itemList.get(position).getItemImageURL());
                itemIntent.putExtra("userImgURL", itemList.get(position).getUserImage());
                itemIntent.putExtra("itemPrice", itemList.get(position).getItemPrice());
                itemIntent.putExtra("itemLikes", itemList.get(position).getItemLikes());
                itemIntent.putExtra("itemArchived", itemList.get(position).getArchiveCount());
                itemIntent.putExtra("userEmail", itemList.get(position).getMessage());
                startActivity(itemIntent);
            }
        });

        return tMainView;
    }

    public void getLikedHeadItems(){
        Call<List<ItemsNearbyModel>> listCall = ApiClient
                .getInstance()
                .getApi()
                .getLikedItems(configPref.getUserID());

        listCall.enqueue(new Callback<List<ItemsNearbyModel>>() {
            @Override
            public void onResponse(Call<List<ItemsNearbyModel>> call, Response<List<ItemsNearbyModel>> response) {
                if(response.isSuccessful() && response.body() != null){
                    itemList = response.body();
                    ItemHeadAdapter itemHeadAdapter = new ItemHeadAdapter(context, itemList);
                    likedItemsRecyclerView.setAdapter(itemHeadAdapter);
                    itemHeadAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(context, response.errorBody().toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<ItemsNearbyModel>> call, Throwable t) {
                Log.i("onFailureResponse", t.getLocalizedMessage());
                Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
