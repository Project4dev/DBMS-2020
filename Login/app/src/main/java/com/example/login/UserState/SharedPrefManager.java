package com.example.login.UserState;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.login.CommentActivity;
import com.example.login.Model.Comments;
import com.example.login.Model.StoreImageModel;
import com.example.login.Model.UserImageModel;
import com.example.login.fregments.FragmentProfile;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.example.login.fregments.FragmentHome.stIModel;

public class SharedPrefManager {

    private static SharedPrefManager instance;
    private Context mCtx;

    private static final String SHARED_PREF_NAME = "mysharedpref123";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_USER_EMAIL = "useremail";
    private static final String KEY_USER_ID = "";
    public static final String KEY_SAVE = "shared preference";
    public static final String KEY_SAVE_POST = "shared preference post";
//    private static final String KEY_USER_ID = "userid";

    private SharedPrefManager(Context context){
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context){
        if(instance == null){
            instance = new SharedPrefManager(context);
        }
        return instance;
    }

    public boolean userLogin(String username,String email,int id){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERNAME,username);
        editor.putString(KEY_USER_EMAIL,email);
        editor.putInt(KEY_USER_ID,id);

        editor.apply();

        return true;
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if (sharedPreferences.getString(KEY_USERNAME, null) != null) {
            return true;
        }
        return false;
    }

    public boolean logout(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }

    public String getUsername(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME,null);
    }

    public String getEmail(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_EMAIL,null);
    }

    public int getUserId(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_USER_ID,0);
    }



    public void loadData() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(KEY_SAVE,mCtx.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("store post",null);
        Type type = new TypeToken<ArrayList<StoreImageModel>>() {}.getType();
        stIModel = gson.fromJson(json,type);
        if(stIModel == null){
            stIModel = new ArrayList<>();
        }
    }

    public void loadUserPost(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(KEY_SAVE_POST,mCtx.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("store user_post",null);
        Type type = new TypeToken<ArrayList<UserImageModel>>() {}.getType();
        FragmentProfile.uiModel = gson.fromJson(json,type);
        if(FragmentProfile.uiModel == null){
            FragmentProfile.uiModel = new ArrayList<>();
        }
    }

}
