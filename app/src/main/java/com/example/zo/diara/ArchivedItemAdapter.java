package com.example.zo.diara;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.zo.diara.ModelClasses.ItemsNearbyModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ArchivedItemAdapter extends RecyclerView.Adapter<ArchivedItemAdapter.ArchiveViewHolder> {

    private Context context;
    private List<ItemsNearbyModel> itemList;
    NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("en-PH", "PH"));

    public ArchivedItemAdapter(Context context, List<ItemsNearbyModel> list){
        this.context = context;
        this.itemList = list;
    }

    public class ArchiveViewHolder extends RecyclerView.ViewHolder {
        TextView textItemPrice, textItemTitle, textItemDescription,
                    textItemLikes;
        ImageView imgSeller, imgItem;
        FloatingActionButton btnLike, btnSkip;

        public ArchiveViewHolder(@NonNull View itemView) {
            super(itemView);

            textItemPrice = itemView.findViewById(R.id.textItemPrice);
            textItemTitle = itemView.findViewById(R.id.textItemTitle);
            textItemDescription = itemView.findViewById(R.id.textItemDescription);
            textItemLikes = itemView.findViewById(R.id.textNumLikes);
            imgSeller = itemView.findViewById(R.id.sellerImage);
            imgItem = itemView.findViewById(R.id.imageContainer);
            btnLike = itemView.findViewById(R.id.btnLike);
            btnSkip = itemView.findViewById(R.id.btnSkip);

        }
    }

    @NonNull
    @Override
    public ArchivedItemAdapter.ArchiveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.archive_item_card, null);
        return new ArchivedItemAdapter.ArchiveViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArchivedItemAdapter.ArchiveViewHolder holder, int position) {
        ItemsNearbyModel item = itemList.get(position);

        holder.textItemTitle.setText(item.getItemTitle());
        holder.textItemDescription.setText(item.getItemDescription());
        holder.textItemPrice.setText(String.valueOf(format.format(item.getItemPrice())));
        holder.textItemLikes.setText(String.valueOf(item.getItemLikes()));

        Glide.with(context)
                .load(item.getUserImage())
                .centerCrop()
                .into(holder.imgSeller);

        Glide.with(context)
                .load(item.getItemImageURL())
                .centerCrop()
                .into(holder.imgItem);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


}
