package com.example.login.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.login.CommentActivity;
import com.example.login.Interface.ItemClickListener;
import com.example.login.Model.StoreImageModel;
import com.example.login.R;
import com.example.login.Utils.Constant;

import java.util.List;

import static com.example.login.CommentActivity.commentsList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    List<StoreImageModel> storeImageModelList;
    Context context;
    Bitmap bitmap;



    public ImageAdapter(List<StoreImageModel> storeImageModelList, Context context) {
        this.storeImageModelList = storeImageModelList;
        this.context = context;
    }


    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.show_image_layout,parent,false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageViewHolder holder, final int position) {
        StoreImageModel storeImageModel = storeImageModelList.get(position);
//        holder.get_image_view.setImageBitmap(storeImageModel.getBitmap());
        String img = storeImageModel.getImg();
        byte[] bytes = Base64.decode(img,Base64.DEFAULT);
        bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
//        holder.get_image_view.setImageBitmap(bitmap);

        Glide.with(context)
                .load(bitmap)
                .into(holder.get_image_view);

//        holder.post_user_name.setText(storeImageModel.get);
        holder.post_title.setText(storeImageModel.getTitle());
        holder.post_description.setText(storeImageModel.getDescription());
        holder.post_user_name.setText(storeImageModel.getUsername());
        holder.post_date.setText(storeImageModel.getDateString());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View v) {
                Constant.currentStoreImageModel = storeImageModelList.get(position);
                context.startActivity(new Intent(context,CommentActivity.class));

//                CommentActivity commentActivity = new CommentActivity();
//                commentActivity.getDataFromComment();
////                commentActivity.new Task().execute();
//                commentActivity.callTaskMethod();

            }
        });


    }

    @Override
    public int getItemCount() {
        return storeImageModelList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView get_image_view,comment_box;
        TextView post_user_name,post_title,post_description,post_date;

        ItemClickListener itemClickListener;

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            get_image_view = itemView.findViewById(R.id.get_image_view);
            post_title = itemView.findViewById(R.id.post_title);
            post_user_name = itemView.findViewById(R.id.post_user_name);
            post_description = itemView.findViewById(R.id.post_description);
            post_date = itemView.findViewById(R.id.post_date);
            comment_box = itemView.findViewById(R.id.comment_box);
//            itemView.setOnClickListener(this);
            comment_box.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v);
        }
    }
}

