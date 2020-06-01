package com.example.zo.diara;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.zo.diara.ModelClasses.Item;
import com.example.zo.diara.ModelClasses.ItemListModel;
import com.example.zo.diara.ModelClasses.ItemsNearbyModel;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class UserItemsAdapter extends RecyclerView.Adapter<UserItemsAdapter.UserItemViewHolder> {

    private Context context;
    private List<ItemsNearbyModel> ItemList;
    NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("en-PH", "PH"));

    public UserItemsAdapter(Context context, List<ItemsNearbyModel> itemList) {
        this.context = context;
        ItemList = itemList;
    }


    public class UserItemViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle, textDescription, textPrice;
        ImageView itemImage;

        public UserItemViewHolder(View view){
            super(view);

            textTitle = view.findViewById(R.id.textItemTitle);
            textDescription = view.findViewById(R.id.textItemDescription);
            textPrice = view.findViewById(R.id.textItemPrice);
            itemImage = view.findViewById(R.id.imageContainer);
        }
    }

    @NonNull
    @Override
    public UserItemsAdapter.UserItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.user_items_card, null);
        return new UserItemsAdapter.UserItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserItemsAdapter.UserItemViewHolder holder, int position) {
        ItemsNearbyModel itemListModel = ItemList.get(position);

        holder.textTitle.setText(itemListModel.getItemTitle());
        holder.textDescription.setText(itemListModel.getItemDescription());
        holder.textPrice.setText(String.valueOf(format.format(itemListModel.getItemPrice())));

        Glide.with(context)
                .load(itemListModel.getItemImageURL())
                .centerCrop()
                .into(holder.itemImage);
    }

    @Override
    public int getItemCount() {
        return ItemList.size();
    }
}
