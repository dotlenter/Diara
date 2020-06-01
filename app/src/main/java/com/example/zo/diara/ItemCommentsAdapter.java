package com.example.zo.diara;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.zo.diara.ModelClasses.ItemCommentsModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ItemCommentsAdapter extends RecyclerView.Adapter<ItemCommentsAdapter.ItemCommentsViewHolder> {

    private Context context;
    private List<ItemCommentsModel> itemList;

    public ItemCommentsAdapter(Context context, List<ItemCommentsModel> list){
        this.context = context;
        this.itemList = list;
    }

    public class ItemCommentsViewHolder extends RecyclerView.ViewHolder{

        TextView textUsername, textCommentDate,
                textCommentBody;
        CircleImageView userImage;

        public ItemCommentsViewHolder(@NonNull View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.userImage);
            textUsername = itemView.findViewById(R.id.textUsername);
            textCommentDate = itemView.findViewById(R.id.textCommentDate);
            textCommentBody = itemView.findViewById(R.id.textCommentBody);
        }
    }

    @NonNull
    @Override
    public ItemCommentsAdapter.ItemCommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_comment, null);
        return new ItemCommentsAdapter.ItemCommentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemCommentsAdapter.ItemCommentsViewHolder holder, int position) {
        ItemCommentsModel item = itemList.get(position);
        holder.textUsername.setText(item.getUserEmail());
        holder.textCommentBody.setText(item.getCommentBody());
        holder.textCommentDate.setText(item.getCommentDate());

        Glide.with(context)
                .load(item.getUserImgUrl())
                .centerCrop()
                .into(holder.userImage);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


}
