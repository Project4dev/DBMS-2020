package com.example.login.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.login.Model.UserImageModel;
import com.example.login.R;

import java.util.List;

public class UserImageAdapter extends BaseAdapter {

    Context context;
    List<UserImageModel> uiModel;

    public UserImageAdapter(Context context, List<UserImageModel> uiModel) {
        this.context = context;
        this.uiModel = uiModel;
    }

    @Override
    public int getCount() {
        return uiModel.size();
    }

    @Override
    public Object getItem(int position) {
        return uiModel.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserImageModel userImageModel = uiModel.get(position);
        ViewHolder viewHolder;
        Bitmap bitmap = null;
//
//        if(userImageModel == null)
//        {
//            viewHolder = new ViewHolder();
//            convertView = LayoutInflater.from(context).inflate(R.layout.user_post_item,parent,false);
//            viewHolder.imageView = convertView.findViewById(R.id.user_post_item_img);
//
//            String img = userImageModel.getImages();
//            byte[] bytes = Base64.decode(img,Base64.DEFAULT);
//            bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
////        holder.get_image_view.setImageBitmap(bitmap);
//
//            convertView.setTag(viewHolder);
//        }
//        else{
//            viewHolder = (ViewHolder) convertView.getTag();
//        }

        viewHolder = new ViewHolder();
        convertView = LayoutInflater.from(context).inflate(R.layout.user_post_item,parent,false);
        viewHolder.imageView = convertView.findViewById(R.id.user_post_item_img);

        String img = userImageModel.getImages();
        byte[] bytes = Base64.decode(img,Base64.DEFAULT);
        bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
//        holder.get_image_view.setImageBitmap(bitmap);

//        convertView.setTag(viewHolder);
        Glide.with(context)
                .load(bitmap)
                .into(viewHolder.imageView);
        return convertView;
    }

    private static class ViewHolder{
        ImageView imageView;
    }
}
