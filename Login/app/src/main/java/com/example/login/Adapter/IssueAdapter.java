package com.example.login.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.login.Interface.ItemClickListener;
import com.example.login.Model.IssueItems;
import com.example.login.R;
import com.example.login.UserIssue;
import com.example.login.Utils.Constant;

import java.util.List;

public class IssueAdapter extends RecyclerView.Adapter<IssueAdapter.IssueViewHolder> {

    List<IssueItems> listIssueItems;
    Context context;

    public IssueAdapter(List<IssueItems> issueItems, Context context) {
        this.listIssueItems = issueItems;
        this.context = context;
    }

    @NonNull
    @Override
    public IssueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(context).inflate(R.layout.list_issue,parent,false);
        return new IssueViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull IssueViewHolder holder, final int position) {
        IssueItems currentItem = listIssueItems.get(position);

//        holder.img_issue.setImageResource(currentItem.getItemImages());
        Glide.with(context)
                .load(currentItem.getItemImages())
                .into(holder.img_issue);
        holder.txt_isuue.setText(currentItem.getItemName());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View v) {
                Constant.currentItem = listIssueItems.get(position);

                context.startActivity(new Intent(context,UserIssue.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listIssueItems.size();
    }


    public class IssueViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView img_issue;
        public TextView txt_isuue;

        ItemClickListener itemClickListener;

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        public IssueViewHolder(@NonNull View itemView) {
            super(itemView);

            img_issue = (ImageView)itemView.findViewById(R.id.img_issue);
            txt_isuue = (TextView)itemView.findViewById(R.id.txt_isuue);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v);
        }
    }
}
