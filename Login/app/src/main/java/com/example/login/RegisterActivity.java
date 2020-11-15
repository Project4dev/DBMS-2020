package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.login.UserState.SharedPrefManager;
import com.example.login.UserState.UserDetail;
import com.example.login.Utils.ConnectionJdbc;
import com.google.android.material.textfield.TextInputLayout;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputLayout reg_username_layout,reg_password_layout,reg_email_layout;
    private Button btn_register;
    String  username,password,email;
    ProgressDialog progress;
    String DOMAIN = "@aiktc.ac.in";


    private final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final Pattern PASSWORD_PATTERN =Pattern.compile("[a-zA-Z0-9\\!\\@\\#\\$]{8,24}");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this,AllIssueActivity.class));
            return;
        }

        reg_username_layout = (TextInputLayout)findViewById(R.id.reg_username_layout);
        reg_password_layout = (TextInputLayout)findViewById(R.id.reg_password_layout);
        reg_email_layout = (TextInputLayout)findViewById(R.id.reg_email_layout);
        btn_register = (Button)findViewById(R.id.btn_register);

        progress = new ProgressDialog(this);
        btn_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_register:
                if(registerUser()){
                    progress.setMessage("Please wait...");
                    progress.show();
                    new Task().execute();
                }
        }
    }

    private boolean registerUser() {
        if (getUsername() && getPassword() && getEmail()) {
            return true;
        } else {
            return false;
        }

    }

    private boolean getUsername(){
        username = reg_username_layout.getEditText().getText().toString().trim();
        if(username.isEmpty()){
            reg_username_layout.setError("Field can't be empty");
            return false;
        }else if(username.length()>15){
            reg_username_layout.setError("please reduce the length of username");
            return false;
        }else{
            reg_username_layout.setError(null);
            return true;
        }
    }

    private boolean getPassword(){
    password = reg_password_layout.getEditText().getText().toString().trim();
    if(password.isEmpty()){
        reg_password_layout.setError("Field can't be empty");
        return false;
    }else if(!PASSWORD_PATTERN.matcher(password).matches()){
        reg_password_layout.setError("password to weak");
        return false;
    }else{
        reg_password_layout.setError(null);
        return true;
    }
    }

    private boolean getEmail(){
        email = reg_email_layout.getEditText().getText().toString().trim();
        if(email.isEmpty()){
            reg_email_layout.setError("Field can't be empty");
            return false;
        }else if(!Pattern.compile(EMAIL_PATTERN).matcher(email).matches() || !email.contains(DOMAIN)){
            reg_email_layout.setError("Enter an aiktc domain email");
            return false;
        }else{
            reg_email_layout.setError(null);
            return true;
        }
    }



    private class Task extends AsyncTask<Void, Void,Void>
    {
        String records = "",error1="";
        int user_exist = 0;
        ResultSet resultSet;
        int user_id;

        @Override
        protected Void doInBackground(Void... voids) {
            try{

                Connection connection = ConnectionJdbc.getConnection();
                if(connection == null){
                    System.out.println("Connection failed");
                }else{

                    String query = "select * from users where username=? and email=? ";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1,username);
                    preparedStatement.setString(2,email);
                    resultSet = preparedStatement.executeQuery();
                    while(resultSet.next()){
                        user_exist= resultSet.getInt(1);
                    }
                    if(user_exist == 0){

                        String q = "insert into users(username,password,email) values(?,?,?)";
                        PreparedStatement pstmt = connection.prepareStatement(q);
                        System.out.println("Connection Done");
                        pstmt.setString(1,username);
                        pstmt.setString(2,password);
                        pstmt.setString(3,email);
                        int res = pstmt.executeUpdate();
                        if(res>0){
                            //success
                            System.out.println("Success");
                        }else{
                            //failed
                            System.out.println("failed");
                        }

                    }

                    connection.close();

                }

            }
            catch (Exception e){
                error1 ="Error during connection";
                Log.i("TAG",e.getMessage());//e.toString() ;

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progress.dismiss();
//            user_exist = SharedPrefManager.getInstance(RegisterActivity.this).getUserId();
//            user_id = user_exist;
            if (error1 != "") {
                Toast.makeText(RegisterActivity.this, "Something wrong", Toast.LENGTH_SHORT).show();
                Log.i("TAG","Error during connection");
            }else if(user_exist>0){
                Toast.makeText(RegisterActivity.this, "User Already Exist.", Toast.LENGTH_SHORT).show();
            }else{
                startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                finish();
                Toast.makeText(RegisterActivity.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
            }

            super.onPostExecute(aVoid);
        }
    }
}
