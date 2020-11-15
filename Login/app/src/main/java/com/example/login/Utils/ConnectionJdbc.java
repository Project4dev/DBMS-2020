package com.example.login.Utils;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionJdbc {

    public static Connection getConnection(){
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection connection = DriverManager.getConnection("jdbc:mysql://remotemysql.com:3306/Goa8Lt451B","Goa8Lt451B","50FPBnw52y");
            return connection;
        }catch (Exception e){
            Log.i("TAG","Error in Connection");
            Log.i("TAG",e.getMessage());
            return null;
        }

    }
}
