package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.login.UserState.SharedPrefManager;
import com.example.login.Utils.ConnectionJdbc;
import com.google.android.material.textfield.TextInputLayout;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

import javax.xml.transform.Result;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputLayout email_text_layout,password_text_layout;
    private Button btn_login;
    private TextView login_txt;
    private final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final Pattern PASSWORD_PATTERN =Pattern.compile("[a-zA-Z0-9\\!\\@\\#\\$]{8,24}");
    String login_email,login_password;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this,AllIssueActivity.class));
            return;
        }

        email_text_layout = (TextInputLayout) findViewById(R.id.email_text_layout);
        password_text_layout = (TextInputLayout) findViewById(R.id.password_text_layout);
        btn_login = (Button)findViewById(R.id.btn_login);
        login_txt = (TextView)findViewById(R.id.login_txt);
//        progress = (ProgressBar)findViewById(R.id.progress);
        progress = new ProgressDialog(this);

        btn_login.setOnClickListener(this);
        login_txt.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                if(login())
                {
                    progress.setMessage("Please wait...");
                    progress.show();
                    new Task().execute();
                }
                break;
            case R.id.login_txt:
                startActivity(new Intent(MainActivity.this,RegisterActivity.class));
        }
    }

    private boolean login() {
            login_email = email_text_layout.getEditText().getText().toString().trim();
            login_password = password_text_layout.getEditText().getText().toString().trim();


        if(!login_email.isEmpty() && !login_password.isEmpty()){
            return true;
        }else{
            Toast.makeText(this, "fields are empty.", Toast.LENGTH_SHORT).show();
            return false;
        }

    }


    private class Task extends AsyncTask<Void, Void,Void>
    {
        String records = "",error1="";
        String records_user,records_email;
        int id;

        @Override
        protected Void doInBackground(Void... voids) {
            try{

                Connection connection = ConnectionJdbc.getConnection();
                if(connection == null){
                    System.out.println("Connection failed");
                }else{
//
                    String q = "select * from users where email = ? and password = ?";
                    PreparedStatement pstmt = connection.prepareStatement(q);
                    pstmt.setString(1,login_email);
                    pstmt.setString(2,login_password);
                    ResultSet resultSet = pstmt.executeQuery();

                    while (resultSet.next()){
                        id = resultSet.getInt(1);
                        records_user =resultSet.getString(2);
                        records_email = resultSet.getString(4);

                    }


                    System.out.println("Connection Done");

                    connection.close();

                }

            }
            catch (Exception e){
                Log.i("TAG",e.getMessage());//e.toString() ;

            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            progress.dismiss();
            if(records_user!=null){
                SharedPrefManager.getInstance(getApplicationContext()).userLogin(records_user,records_email,id);
                startActivity(new Intent(MainActivity.this,AllIssueActivity.class));
                finish();
                Toast.makeText(MainActivity.this, "User Login Successfully.", Toast.LENGTH_SHORT).show();
//                System.out.println("Data fetched Successfully.");
            }
            else{
//                records = "User not found";
                Toast.makeText(MainActivity.this, "User not found please try again", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(aVoid);
        }

    }
}
