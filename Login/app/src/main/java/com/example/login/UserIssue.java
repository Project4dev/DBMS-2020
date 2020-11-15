package com.example.login;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.login.Model.StoreImageModel;
import com.example.login.UserState.SharedPrefManager;
import com.example.login.UserState.UserDetail;
import com.example.login.Utils.ConnectionJdbc;
import com.example.login.Utils.Constant;
import com.example.login.fregments.FragmentHome;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class UserIssue extends AppCompatActivity implements View.OnClickListener {

    Button add_issue;
    ImageView img,camera_img;
    EditText issue_title,issue_description;
    ProgressDialog progressDialog;
    String title,description;
    private static final int CAMERA_REQUEST = 200;
    String image;
    Bitmap images;
    StoreImageModel storeImageModel;
    public static String userNameFromUserTable="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_issue);


        add_issue = (Button) findViewById(R.id.btn_add_issue);

        img = (ImageView)findViewById(R.id.img);
        camera_img = findViewById(R.id.camera_img);
        issue_description = (EditText) findViewById(R.id.issue_description);
        issue_title = (EditText) findViewById(R.id.issue_title);


        progressDialog = new ProgressDialog(this);
        add_issue.setOnClickListener(this);

        camera_img.setOnClickListener(this);



         showImagesAndText();
    }

    private void showImagesAndText() {

        if(Constant.currentItem != null){
            img.setImageResource(Constant.currentItem.getItemImages());

            Drawable drawable = this.getResources().getDrawable(Constant.currentItem.getItemImages());
            Bitmap bit = ((BitmapDrawable) drawable).getBitmap();

            ByteArrayOutputStream baoo = new ByteArrayOutputStream();
            bit.compress(Bitmap.CompressFormat.JPEG,100,baoo);
            byte[] bytes = baoo.toByteArray();
            image = Base64.encodeToString(bytes,Base64.DEFAULT);

            Log.i("Images_string",image);
            issue_title.setText(Constant.currentItem.getItemName());
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add_issue:
                if(title()&&description()){
                    new Task().execute();
                }
                break;

            case R.id.camera_img:
                openCamera();
                break;
        }

    }


    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager())!=null){
            startActivityForResult(intent,CAMERA_REQUEST);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAMERA_REQUEST && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            images = (Bitmap)extras.get("data");
            img.setImageBitmap(images);

            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            images.compress(Bitmap.CompressFormat.JPEG,100,bao);
            byte[] bytes = bao.toByteArray();
            image = Base64.encodeToString(bytes,Base64.DEFAULT);

        }
    }



    private boolean description() {
        description = issue_description.getText().toString().trim();
        if(description.isEmpty()){
            Toast.makeText(this, "Description can't be empty.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean title() {
        title = issue_title.getText().toString().trim();
        if(title.isEmpty()){
            Toast.makeText(this, "Title can't be empty.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    class Task extends AsyncTask<Void, Void,Void>
    {
        String records = "",error1="";
        int user_id;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            user_id = SharedPrefManager.getInstance(getApplicationContext()).getUserId();
            progressDialog.setMessage("Please wait...");
            progressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                Connection connection = ConnectionJdbc.getConnection();
                System.out.println("connection successfully");

                if(images!=null || Constant.currentItem != null){
                    String sql = "insert into issue(img,title,description,user_id) values(?,?,?,?)";
                    PreparedStatement pStmt= connection.prepareStatement(sql);
                    pStmt.setInt(4,user_id);
                    pStmt.setString(2,title);
                    pStmt.setString(3,description);
                    pStmt.setString(1,image);
                    pStmt.executeUpdate();

//                    String getUsernameSql = "select * from users where id= ?";
//                    PreparedStatement preparedStatement= connection.prepareStatement(getUsernameSql);
//                    preparedStatement.setInt(1,user_id);
//                    ResultSet rs = preparedStatement.executeQuery();
//                    while(rs.next()){
//                        userNameFromUserTable = rs.getString(2);
//                    }
                    records = "Post Added Successfully";

                }else {
                    error1 = "Image filed can't be empty";
                }

                connection.close();

            }
            catch (Exception e){
                Log.i("TAG",e.getMessage());
                error1 = e.getMessage();

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();

            if(error1!=null) {
                Toast.makeText(UserIssue.this, error1, Toast.LENGTH_SHORT).show();
            }
            showToast();

        }

        private void showToast() {
            if(records!=null) {
                Toast.makeText(UserIssue.this, records, Toast.LENGTH_SHORT).show();
                issue_description.setText("");
                issue_title.setText("");
            }
        }


    }

}