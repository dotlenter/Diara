package com.example.zo.diara;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.zo.diara.ModelClasses.ItemsNearbyModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ItemHeadAdapter extends RecyclerView.Adapter<ItemHeadAdapter.ItemHeadViewHolder> {

    private Context context;
    private List<ItemsNearbyModel> itemsNearbyModelList;

    public ItemHeadAdapter(Context context, List<ItemsNearbyModel> itemList){
        this.context = context;
        this.itemsNearbyModelList = itemList;
    }

    public class ItemHeadViewHolder extends RecyclerView.ViewHolder{

        TextView textItemTitle;
        CircleImageView itemImage;

        public ItemHeadViewHolder(View view){
            super(view);

            textItemTitle = view.findViewById(R.id.textItemTitle);
            itemImage = view.findViewById(R.id.itemHeadImgContainer);

        }

    }

    @NonNull
    @Override
    public ItemHeadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_liked_layout, null);
        return new ItemHeadAdapter.ItemHeadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHeadViewHolder holder, int position) {
        ItemsNearbyModel itemsNearbyModel = itemsNearbyModelList.get(position);

        holder.textItemTitle.setText(itemsNearbyModel.getItemTitle());
        Glide.with(context)
                .load(itemsNearbyModel.getItemImageURL())
                .centerCrop()
                .into(holder.itemImage);
    }

    @Override
    public int getItemCount() {
        return itemsNearbyModelList.size();
    }


}
