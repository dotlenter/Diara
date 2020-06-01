package com.example.zo.diara;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.zo.diara.ModelClasses.ItemCommentsModel;
import com.example.zo.diara.ModelClasses.StatMessageModel;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewItem extends AppCompatActivity {

    private TextView textItemPrice, textItemName,
                    textItemDescription, textNumLikes,
                textNumArchive, textSellerName,
                textSellerEmail, commentText;
    private ImageView itemImgContainer;
    private GifImageView commentLoading;
    private CircleImageView sellerImage;
    private RelativeLayout btnAddComment;
    private EditText textComment;
    private ConfigPref configPref;
    private List<ItemCommentsModel> itemList;
    private RecyclerView commentRecyclerView;
    private LinearLayoutManager linearLayoutManager;

    private long userID = 0, itemID = 0;
    NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("en-PH", "PH"));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);

        getSupportActionBar().setTitle("");

        configPref = new ConfigPref(this);
        commentLoading = findViewById(R.id.commentLoading);
        commentText = findViewById(R.id.commentText);
        textComment = findViewById(R.id.textComment);
        textItemPrice = findViewById(R.id.textItemPrice);
        textItemDescription = findViewById(R.id.textDescription);
        textItemName = findViewById(R.id.textItemName);
        textNumLikes = findViewById(R.id.textNumLikes);
        textNumArchive = findViewById(R.id.textNumArchive);
        textSellerEmail = findViewById(R.id.textSellerEmail);
        textSellerName = findViewById(R.id.textSellerName);
        itemImgContainer = findViewById(R.id.imageContainer);
        sellerImage = findViewById(R.id.sellerImage);
        btnAddComment = findViewById(R.id.btnAddComment);
        commentText = findViewById(R.id.commentText);
        commentRecyclerView = findViewById(R.id.userCommentsRecycler);
        linearLayoutManager = new LinearLayoutManager(this);
        commentRecyclerView.setLayoutManager(linearLayoutManager);

        Bundle itemExtras = getIntent().getExtras();
        String itemImageURL, userImageURL, itemTitle, itemDesc, userEmail;
        Double itemPrice;
        long ArchiveCount, NumLikes;

        if(itemExtras != null){

            itemImageURL = itemExtras.getString("itemImgURL");
            userImageURL = itemExtras.getString("userImgURL");
            itemTitle = itemExtras.getString("itemTitle");
            ArchiveCount = itemExtras.getLong("itemArchived");
            NumLikes = itemExtras.getLong("itemLikes");
            itemDesc = itemExtras.getString("itemDesc");
            itemPrice = itemExtras.getDouble("itemPrice");
            userEmail = itemExtras.getString("userEmail");

            Glide.with(this)
                    .load(itemImageURL)
                    .centerCrop()
                    .into(itemImgContainer);

            Glide.with(this)
                    .load(userImageURL)
                    .centerCrop()
                    .into(sellerImage);


            getSupportActionBar().setTitle(itemTitle);
            textItemName.setText(itemTitle);
            textNumArchive.setText(String.valueOf(ArchiveCount));
            textNumLikes.setText(String.valueOf(NumLikes));
            textItemDescription.setText(itemDesc);
            textItemPrice.setText(String.valueOf(format.format(itemPrice)));
            textSellerName.setText(userEmail);
            textSellerEmail.setText(userEmail);
            userID = itemExtras.getLong("userID");
            itemID = itemExtras.getLong("itemID");

            getComments();
        }

        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!textComment.getText().toString().isEmpty()){
                    textComment.setEnabled(false);
                    startLoadButtonAnimation(commentText, commentLoading);
                    createItemComment();
                }else{
                    textComment.setError("Please insert a message");
                }
            }
        });
        textComment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                    hideKeyboard(v);
            }
        });

    }
    public void startLoadButtonAnimation(TextView textView, GifImageView imageView){
        textView.setVisibility(View.GONE);
        imageView.setVisibility(View.VISIBLE);
    }
    public void stopLoadButtonAnimation(TextView textView, GifImageView imageView){
        textView.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.GONE);
    }
    public void createItemComment(){
        Call<StatMessageModel> statMessageModelCall = ApiClient
                .getInstance()
                .getApi()
                .createComment(configPref.getUserID(), itemID, textComment.getText().toString());

        statMessageModelCall.enqueue(new Callback<StatMessageModel>() {
            @Override
            public void onResponse(Call<StatMessageModel> call, Response<StatMessageModel> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(response.body().getStatus() != null){
                        stopLoadButtonAnimation(commentText, commentLoading);
                        textComment.setText("");
                        textComment.setEnabled(true);
                        Toast.makeText(ViewItem.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        getComments();
                    }else{
                        stopLoadButtonAnimation(commentText, commentLoading);
                        textComment.setEnabled(true);
                        Toast.makeText(ViewItem.this, "Message error.", Toast.LENGTH_LONG).show();
                    }
                }else{
                    stopLoadButtonAnimation(commentText, commentLoading);
                    textComment.setEnabled(true);
                    Toast.makeText(ViewItem.this, response.errorBody().toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<StatMessageModel> call, Throwable t) {
                stopLoadButtonAnimation(commentText, commentLoading);
                Toast.makeText(ViewItem.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getComments(){
        Call<List<ItemCommentsModel>> listCall = ApiClient
                .getInstance()
                .getApi()
                .getItemComments(itemID);

        listCall.enqueue(new Callback<List<ItemCommentsModel>>() {
            @Override
            public void onResponse(Call<List<ItemCommentsModel>> call, Response<List<ItemCommentsModel>> response) {
                if(response.isSuccessful() && response.body() != null){
                    itemList = response.body();
                    ItemCommentsAdapter itemCommentsAdapter = new ItemCommentsAdapter(ViewItem.this, itemList);
                    commentRecyclerView.setAdapter(itemCommentsAdapter);
                    itemCommentsAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(ViewItem.this, response.errorBody().toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<ItemCommentsModel>> call, Throwable t) {
                Toast.makeText(ViewItem.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLoadButtonAnimation(commentText, commentLoading);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
