package com.example.login.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.Model.Comments;
import com.example.login.R;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    Context context;
    List<Comments> commentsList;

    public CommentAdapter(Context context, List<Comments> commentsList) {
        this.context = context;
        this.commentsList = commentsList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_list_layout,parent,false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comments comments = commentsList.get(position);

        holder.text_comment_description.setText(comments.getDescription());
        holder.text_comment_user.setText(comments.getUsername());
        holder.text_comment_date.setText(comments.getDateString());

    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    class CommentViewHolder extends RecyclerView.ViewHolder{

        TextView text_comment_user,text_comment_description,text_comment_date;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);

            text_comment_user = itemView.findViewById(R.id.text_comment_user);
            text_comment_description = itemView.findViewById(R.id.text_comment_description);
            text_comment_date = itemView.findViewById(R.id.text_comment_date);
        }
    }
}
