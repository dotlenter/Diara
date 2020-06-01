package com.example.zo.diara;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRatingBar;

import com.bumptech.glide.Glide;
import com.example.zo.diara.ModelClasses.ItemsNearbyModel;
import com.example.zo.diara.ModelClasses.nearItems;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ItemAdapter extends CardStackView.Adapter<ItemAdapter.ItemViewHolder> {

    private Context context;
    private List<ItemsNearbyModel> ItemList;

    NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("en-PH", "PH"));
    public ItemAdapter(Context context, List<ItemsNearbyModel> itemList) {

        this.context = context;
        this.ItemList = itemList;

    }

    class ItemViewHolder extends CardStackView.ViewHolder{
        ImageView imageContainer, sellerImage;
        TextView textSellerName, textVerificationStatus,
                textNumLikes, textArchiveCount,
                textItemTitle, textItemPrice,
                textDistance, textItemDescription;
        AppCompatRatingBar sellerRating;

        public ItemViewHolder(View view){
            super(view);

            textArchiveCount = view.findViewById(R.id.textNumArchive);
            textItemTitle = view.findViewById(R.id.textItemName);
            textDistance = view.findViewById(R.id.textDistance);
            textItemDescription = view.findViewById(R.id.textDescription);
            textItemPrice = view.findViewById(R.id.textItemPrice);
            textNumLikes = view.findViewById(R.id.textNumLikes);
            imageContainer = view.findViewById(R.id.imageContainer);
            sellerImage = view.findViewById(R.id.sellerImage);
        }
    }
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_card_des, null);
        return new ItemViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ItemsNearbyModel item = ItemList.get(position);

        holder.textItemTitle.setText(item.getItemTitle());
        holder.textDistance.setText(Math.round(item.getItemDistanceInKM()) + context.getResources().getString(R.string.Kilometers));
        holder.textItemDescription.setText(item.getItemDescription());
        holder.textItemPrice.setText(String.valueOf(format.format(item.getItemPrice())));
        holder.textNumLikes.setText(String.valueOf(item.getItemLikes()));
        holder.textArchiveCount.setText(String.valueOf(item.getArchiveCount()));
        Glide.with(context)
                .load(item.getItemImageURL())
                .centerCrop()
                .into(holder.imageContainer);

        Glide.with(context)
                .load(item.getUserImage())
                .centerCrop()
                .into(holder.sellerImage);
    }

    @Override
    public int getItemCount() {
        return ItemList.size();
    }

}
