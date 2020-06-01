package com.example.zo.diara;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zo.diara.ModelClasses.Item;
import com.example.zo.diara.ModelClasses.ItemListModel;
import com.example.zo.diara.ModelClasses.ItemsNearbyModel;
import com.example.zo.diara.ModelClasses.StatMessageModel;
import com.example.zo.diara.ModelClasses.nearItems;
import com.example.zo.diara.RecyclableFunctions.ItemClickSupport;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemFragment extends Fragment implements CardStackListener{

    private View tMainView;
    private Context context;
    private CardStackView cardStackView;
    private ItemAdapter itemAdapter;
    private List<ItemsNearbyModel> itemsNearbyModels;
    private FloatingActionButton btnLike, btnSkip, btnBookMark, btnRewind;
    private SwipeAnimationSetting settingSwipeLeft, settingSwipeRight, settingSwipeUp;
    private CardStackLayoutManager cardStackLayoutManager;
    private ConfigPref configPref;
    private GifImageView imgLoad;

    /**
     * @
     *  ARCHIVE ITEMS
     * @
     * */

    private LayoutInflater archiveLayoutInflater;
    private View viewArchive;
    private AlertDialog archivedAlertDialog;

    private TextView textItemPrice, textItemLikes,
                    textArchiveCount, textItemTitle,
                    textItemDescription, textDistance;
    private Button btnOkArchived;

    public ItemFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        tMainView = inflater.inflate(R.layout.fragment_item, container, false);

        btnLike = tMainView.findViewById(R.id.btnLike);
        btnSkip = tMainView.findViewById(R.id.btnSkip);
        btnBookMark = tMainView.findViewById(R.id.btnBookMark);
        btnRewind = tMainView.findViewById(R.id.btnRewind);
        configPref = new ConfigPref(context);
        cardStackView = tMainView.findViewById(R.id.cardStack);
        imgLoad = tMainView.findViewById(R.id.imgLoadGif);

        //SETUP ITEM ARCHIVED DIALOG
        archiveLayoutInflater = LayoutInflater.from(context);
        viewArchive = archiveLayoutInflater.inflate(R.layout.item_added_to_archive_prompt, null);
        textItemTitle = viewArchive.findViewById(R.id.textItemTitle);
        textItemDescription = viewArchive.findViewById(R.id.textItemDescription);
        textArchiveCount = viewArchive.findViewById(R.id.textNumArchive);
        textItemLikes = viewArchive.findViewById(R.id.textNumLikes);
        textItemPrice = viewArchive.findViewById(R.id.textItemPrice);
        btnOkArchived = viewArchive.findViewById(R.id.btnOKArchived);

        archivedAlertDialog = new AlertDialog.Builder(context)
                .setView(viewArchive)
                .create();
        archivedAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        btnOkArchived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                archivedAlertDialog.dismiss();
            }
        });

        //SETUP CARDSTACK
        cardStackLayoutManager = new CardStackLayoutManager(context, this);
        cardStackLayoutManager.setVisibleCount(3);
        cardStackLayoutManager.setTranslationInterval(3.0f);
        cardStackLayoutManager.setCanScrollHorizontal(true);
        cardStackLayoutManager.setCanScrollVertical(true);
        cardStackLayoutManager.setStackFrom(StackFrom.Bottom);
        cardStackLayoutManager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual);
        cardStackLayoutManager.setOverlayInterpolator(new LinearInterpolator());
        cardStackView.setLayoutManager(cardStackLayoutManager);

        settingSwipeLeft = new SwipeAnimationSetting.Builder().setDirection(Direction.Left)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(new AccelerateInterpolator())
                .build();
        settingSwipeRight = new SwipeAnimationSetting.Builder().setDirection(Direction.Right)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(new AccelerateInterpolator())
                .build();
        settingSwipeUp = new SwipeAnimationSetting.Builder().setDirection(Direction.Top)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(new AccelerateInterpolator())
                .build();

        btnBookMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardStackLayoutManager.setSwipeAnimationSetting(settingSwipeUp);
                cardStackView.swipe();
            }
        });

        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardStackLayoutManager.setSwipeAnimationSetting(settingSwipeRight);
                cardStackView.swipe();
            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardStackLayoutManager.setSwipeAnimationSetting(settingSwipeLeft);
                cardStackView.swipe();
            }
        });

        ItemClickSupport.addTo(cardStackView)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Toast.makeText(context, itemsNearbyModels.get(position).getItemTitle(), Toast.LENGTH_LONG).show();
                    }
                });

        getItems();
        return tMainView;
    }

    public void getItems(){
        Call<List<ItemsNearbyModel>> listCall = ApiClient
                .getInstance()
                .getApi()
                .getItemsNearby(
                        configPref.getUserID(),
                        configPref.getLong(),
                        configPref.getLat(),
                        configPref.getDistance(),
                        configPref.getDiscoveryFilter(),
                        configPref.getPriceRangeMin(),
                        configPref.getPriceRangeMax());

        listCall.enqueue(new Callback<List<ItemsNearbyModel>>() {
            @Override
            public void onResponse(Call<List<ItemsNearbyModel>> call, Response<List<ItemsNearbyModel>> response) {
                if(response.isSuccessful() && response.body() !=null){
                    itemsNearbyModels = response.body();
                    if(itemsNearbyModels.get(0).getStatus() != null) {
                        if (itemsNearbyModels.get(0).getStatus().toLowerCase() != "no") {
                            ItemAdapter itemAdapter = new ItemAdapter(context, itemsNearbyModels);
                            cardStackView.setAdapter(itemAdapter);
                            imgLoad.setVisibility(View.INVISIBLE);
                        } else {
                            Log.i("onEmptyResponse", itemsNearbyModels.get(0).getMessage());
                            Toast.makeText(context, itemsNearbyModels.get(0).getMessage(), Toast.LENGTH_LONG).show();
                            imgLoad.setVisibility(View.INVISIBLE);
                        }
                    }
                }else{
                    Log.i("onEmptyResponse", response.errorBody().toString());
                    Toast.makeText(context, response.errorBody().toString(), Toast.LENGTH_LONG).show();
                    imgLoad.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<ItemsNearbyModel>> call, Throwable t) {
                Log.d("onFailureSwipe", t.getLocalizedMessage());
                Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                imgLoad.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void itemLiked(long itemID){
        Call<StatMessageModel> statMessageModelCall = ApiClient
                .getInstance()
                .getApi()
                .itemLiked(configPref.getUserID(), itemID);
        statMessageModelCall.enqueue(new Callback<StatMessageModel>() {
            @Override
            public void onResponse(Call<StatMessageModel> call, Response<StatMessageModel> response) {
                if(response.isSuccessful() && response.body() != null){
                    Log.i("onSuccessSwipe", response.body().getMessage());
                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                }else{
                    Log.i("onEmptyResponse", response.errorBody().toString());
                    Toast.makeText(context, response.errorBody().toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<StatMessageModel> call, Throwable t) {
                Log.d("onFailureSwipe", t.getLocalizedMessage());
                Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public void itemSkipped(long itemID){
        Call<StatMessageModel> statMessageModelCall = ApiClient
                .getInstance()
                .getApi()
                .itemSkipped(configPref.getUserID(), itemID);
        statMessageModelCall.enqueue(new Callback<StatMessageModel>() {
            @Override
            public void onResponse(Call<StatMessageModel> call, Response<StatMessageModel> response) {
                if(response.isSuccessful() && response.body() != null){
                    Log.i("onSuccessSwipe", response.body().getMessage());
                }else{
                    Log.i("onEmptyResponse", response.errorBody().toString());
                    Toast.makeText(context, response.errorBody().toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<StatMessageModel> call, Throwable t) {
                Log.d("onFailureSwipe", t.getLocalizedMessage());
                Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void itemArchived(long itemID){
        Call<StatMessageModel> statMessageModelCall = ApiClient
                .getInstance()
                .getApi()
                .itemArchived(configPref.getUserID(), itemID);
        statMessageModelCall.enqueue(new Callback<StatMessageModel>() {
            @Override
            public void onResponse(Call<StatMessageModel> call, Response<StatMessageModel> response) {
                if(response.isSuccessful() && response.body() != null){
                    Log.i("onSuccessSwipe", response.body().getMessage());
                }else{
                    Log.i("onEmptyResponse", response.errorBody().toString());
                    Toast.makeText(context, response.errorBody().toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<StatMessageModel> call, Throwable t) {
                Log.d("onFailureSwipe", t.getLocalizedMessage());
                Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        //fetchItems(configPref.getUserID(), configPref.getLat(),configPref.getLong(), configPref.getDistance());
    }

    @Override
    public void onCardDragging(Direction direction, float ratio) {

    }

    long itemID = 0;
    String itemTitle, itemDescription, itemLikes, itemArchiveCount, itemDistance;
    @Override
    public void onCardSwiped(Direction direction) {
        if(cardStackLayoutManager.getTopPosition() == itemsNearbyModels.size()){
            imgLoad.setVisibility(View.VISIBLE);
            getItems();
        }

        switch (direction){
            case Right:
                itemLiked(itemID);
                break;
            case Left:
                itemSkipped(itemID);
                break;
            case Top:
                itemArchived(itemID);
                archivedAlertDialog.show();
                break;
        }
    }

    @Override
    public void onCardRewound() {

    }

    @Override
    public void onCardCanceled() {

    }

    @Override
    public void onCardAppeared(View view, int position) {

    }

    @Override
    public void onCardDisappeared(View view, int position) {
        itemID = itemsNearbyModels.get(position).getItemID();
        itemTitle = itemsNearbyModels.get(position).getItemTitle();
        itemDescription = itemsNearbyModels.get(position).getItemDescription();
        itemLikes = String.valueOf(itemsNearbyModels.get(position).getItemLikes());
        itemArchiveCount = String.valueOf(itemsNearbyModels.get(position).getArchiveCount());
    }
}
