package com.example.login.fregments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.login.Adapter.ImageAdapter;
import com.example.login.Model.StoreImageModel;
import com.example.login.R;
import com.example.login.UserIssue;
import com.example.login.UserState.SharedPrefManager;
import com.example.login.Utils.ConnectionJdbc;
import com.example.login.Utils.Constant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class FragmentHome extends Fragment {


    RecyclerView img_recyclerview;
    Button btn_get_images;
    ImageAdapter imageAdapter;
//    SwipeRefreshLayout swipeRef;

    public static List<StoreImageModel> stIModel;
    ProgressDialog progressDialog;

    public FragmentHome() {
        // Required empty public constructor
        SharedPrefManager.getInstance(getContext()).loadData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        loadData();
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        img_recyclerview = view.findViewById(R.id.img_recyclerview);
        btn_get_images = view.findViewById(R.id.btn_get_images);
//        swipeRef = view.findViewById(R.id.swipeRef);


        btn_get_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Task().execute();

            }
        });
//        swipeRef.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                new Task().execute();
//            }
//        });

        img_recyclerview.setHasFixedSize(true);
        img_recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        img_recyclerview.setItemViewCacheSize(20);
        img_recyclerview.setDrawingCacheEnabled(true);
        img_recyclerview.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);


        progressDialog = new ProgressDialog(getContext());

        imageAdapter = new ImageAdapter(stIModel,getContext());
        img_recyclerview.setAdapter(imageAdapter);

        return view;
    }




    class Task extends AsyncTask<Void, Void,Void>
    {
        Context context;
        String records = "",error1="";
        String dateString;
//        List<StoreImageModel> storeImageModels = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            progressDialog.setMessage("Please wait...");
            progressDialog.show();
//            Collections.copy(storeImageModels,stIModel);
//            stIModel.clear();
//            imageAdapter = new ImageAdapter(stIModel,getContext());
//            img_recyclerview.setAdapter(imageAdapter);
            stIModel.clear();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                Connection connection = ConnectionJdbc.getConnection();
                System.out.println("connection successfully");
                Statement statement = connection.createStatement();


//                ResultSet resultSet = statement.executeQuery("select img,title,description,username,id from issue i,users u where i.user_id = u.id order by i.id desc");
                ResultSet resultSet = statement.executeQuery("select *,username from issue i,users u where i.user_id = u.id order by i.id desc limit 2");

                while (resultSet.next()){

                    String img = resultSet.getString(2);
                    String title = resultSet.getString(3);
                    String description = resultSet.getString(5);
                    String username = resultSet.getString("username");
                    int all_issue_id = resultSet.getInt(1);
                    Timestamp timestamp = resultSet.getTimestamp("date_time");
                    Calendar calendar = Calendar.getInstance(Locale.getDefault());
                    calendar.setTimeInMillis(timestamp.getTime());
                    dateString = new SimpleDateFormat("dd-MM-yyyy").format(calendar.getTime());


                    stIModel.add(new StoreImageModel(img,title,description,username,all_issue_id,dateString));


                }

                Log.i("dateString",dateString);
                System.out.println(dateString);
                Log.i("image","image retrive");
                connection.close();
            }
            catch (Exception e){
//                error1 ="Error during connection";//e.toString() ;
                Log.i("TAG",e.getMessage());
                error1 = e.getMessage();

            }


            return null;

        }


        @Override
        protected void onPostExecute(Void aVoid) {


            progressDialog.dismiss();
//            swipeRef.setRefreshing(false);
//            storeImageModels.clear();
//            saveData();
            ImageAdapter imageAdapter = new ImageAdapter(stIModel,getContext());
            img_recyclerview.setAdapter(imageAdapter);
//            imageAdapter.notifyDataSetChanged();
//            Collections.copy(stIModel, storeImageModels);

        }

    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        saveData();
    }

    public void saveData(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(SharedPrefManager.getInstance(getContext()).KEY_SAVE,getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(stIModel);
        editor.putString("store post",json);
        editor.apply();
    }

}