package com.example.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.login.Adapter.CommentAdapter;
import com.example.login.Model.Comments;
import com.example.login.UserState.SharedPrefManager;
import com.example.login.Utils.ConnectionJdbc;
import com.example.login.Utils.Constant;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CommentActivity extends AppCompatActivity {

    ImageView img_comment_send;
    EditText edit_comment;
    RecyclerView comment_recyclerview;
    SwipeRefreshLayout swipeRefreshLayout;
    Toolbar toolbar;
    Button btn_comment;
    String userComment = "";
    ProgressDialog progressDialog;
    TextView text_all_id;
    public static List<Comments> commentsList;
//    public List<Comments> commentsList;
    String username;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        getSupportActionBar().setTitle("Comment");

        Log.i("main_list","main list");
        img_comment_send = findViewById(R.id.img_comment_send);
        edit_comment = findViewById(R.id.edit_comment);
        comment_recyclerview = findViewById(R.id.comment_recyclerview);
        swipeRefreshLayout = findViewById(R.id.comment_swipe_refresh);
//        btn_comment = findViewById(R.id.btn_comment);

        i = Constant.currentStoreImageModel.getAll_issue_id();

        img_comment_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkEdittext()){
                    new Task1().execute();
                }else{
                    Toast.makeText(CommentActivity.this, "Comment box can't be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Task().execute();
            }
        });

        comment_recyclerview.setHasFixedSize(true);
        comment_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        comment_recyclerview.setItemViewCacheSize(20);
        comment_recyclerview.setDrawingCacheEnabled(true);
        comment_recyclerview.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        progressDialog = new ProgressDialog(CommentActivity.this);

        new Task().execute();

    }



    private boolean checkEdittext() {
        userComment = edit_comment.getText().toString().trim();
        if(userComment.isEmpty()){
            return false;
        }else{
            return true;
        }
    }





    public class Task extends AsyncTask<Void,Void,Void>
    {
        String error = "";
        int value=0;
        String dateString;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            progressDialog.setMessage("please wait...");
//            progressDialog.show();
            commentsList = new ArrayList<>();
            Log.i("list_Task","list created");
            commentsList.clear();

        }

        @Override
        protected Void doInBackground(Void... voids) {

            Connection connection = ConnectionJdbc.getConnection();
            try {

                String getValue = "select description,username,date_time from comments c,users u where c.all_issue_id = '"+i+"' and c.user_id = u.id order by comment_id desc";
                Log.i("data receved","Task");
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(getValue);
                while (resultSet.next()){
                    String description = resultSet.getString(1);
                    String username = resultSet.getString(2);
                    Timestamp timestamp = resultSet.getTimestamp(3);
                    Calendar calendar = Calendar.getInstance(Locale.getDefault());
                    calendar.setTimeInMillis(timestamp.getTime());
                    dateString = new SimpleDateFormat("dd-MM-yyyy").format(calendar.getTime());


                    commentsList.add(new Comments(description,username,dateString));
                    value++;
                }
                System.out.println(commentsList.size());
                System.out.println(value);
                Log.i("commentList_value","value recived");

                connection.close();

            } catch (SQLException e) {
                Log.i("comment_error",e.getMessage());
                error = e.getMessage();
                System.out.println(e.getMessage());
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            progressDialog.dismiss();
            swipeRefreshLayout.setRefreshing(false);

            Log.i("comment_recy","recyclerview");
            System.out.println(commentsList.size());
            CommentAdapter commentAdapter = new CommentAdapter(CommentActivity.this,commentsList);
            comment_recyclerview.setAdapter(commentAdapter);
        }

    }






    class Task1 extends AsyncTask<Void,Void,Void>
    {

        String error = "",success="";
        int userId;
        int all_issue_id = Constant.currentStoreImageModel.getAll_issue_id();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            userId = SharedPrefManager.getInstance(CommentActivity.this).getUserId();
            progressDialog.setMessage("please wait...");
            progressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... voids) {

            Connection connection = ConnectionJdbc.getConnection();
            try {

                String query = "insert into comments (description,all_issue_id,user_id) values(?,?,?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, userComment);
                preparedStatement.setInt(2, all_issue_id);
                preparedStatement.setInt(3,userId);
                preparedStatement.executeUpdate();
                success = "Comment Added";
            }catch (SQLException e) {
                Log.i("comment_error",e.getMessage());
                error = e.getMessage();
                System.out.println(e.getMessage());
            }


            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            if(success!=null){
                Toast.makeText(CommentActivity.this, success, Toast.LENGTH_SHORT).show();
            }
            edit_comment.setText("");
        }
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
//        saveData();
    }

    private void saveData() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(SharedPrefManager.getInstance(this).KEY_SAVE_POST,this.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(commentsList);
        editor.putString("store comment",json);
        editor.apply();
    }
}