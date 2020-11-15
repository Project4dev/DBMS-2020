package com.example.login.fregments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.example.login.Adapter.UserImageAdapter;
import com.example.login.Model.UserImageModel;
import com.example.login.R;
import com.example.login.UserState.SharedPrefManager;
import com.example.login.UserState.UserDetail;
import com.example.login.Utils.ConnectionJdbc;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentProfile extends Fragment {

    TextView textview_user, textView_email;
    GridView gridView;
    List<UserImageModel> userImages;
    Task task = null;
    TextView txt_see_post;
    ProgressDialog progressDialog;
    public static List<UserImageModel> uiModel;

    public FragmentProfile() {
        // Required empty public constructor
        SharedPrefManager.getInstance(getContext()).loadUserPost();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        textview_user = view.findViewById(R.id.textview_user);
        textView_email = view.findViewById(R.id.textview_email);
        txt_see_post = view.findViewById(R.id.txt_see_post);
        gridView = view.findViewById(R.id.gridview);

        textview_user.setText(SharedPrefManager.getInstance(getContext()).getUsername());
        textView_email.setText(SharedPrefManager.getInstance(getContext()).getEmail());

        progressDialog = new ProgressDialog(getContext());
//        userImages = new ArrayList<>();

//        new Task().execute();

        txt_see_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Task().execute();
            }
        });

        UserImageAdapter userImageAdapter = new UserImageAdapter(getContext(),uiModel);
        gridView.setAdapter(userImageAdapter);

        return view;
    }


    class Task extends AsyncTask<Void,Void,Void>
    {

        int id = SharedPrefManager.getInstance(getContext()).getUserId();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("please wait...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            Connection conn = ConnectionJdbc.getConnection();
            String sql = "select img from issue where user_id = '"+id+"' ";
            System.out.println(sql);
            try {
                Statement statement = conn.createStatement();
                ResultSet rs = statement.executeQuery(sql);
                while (rs.next()){
                    String images = rs.getString("img");
//                    userImages.add(new UserImageModel(images));
                    uiModel.add(new UserImageModel(images));
                }
            } catch (SQLException e) {
                Log.i("post_error",e.getMessage());
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            UserImageAdapter userImageAdapter = new UserImageAdapter(getContext(),uiModel);
            gridView.setAdapter(userImageAdapter);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        saveData();
    }

    public void saveData(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(SharedPrefManager.getInstance(getContext()).KEY_SAVE_POST,getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(uiModel);
        editor.putString("store user_post",json);
        editor.apply();
    }

}