package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.login.UserState.SharedPrefManager;
import com.example.login.UserState.UserDetail;
import com.example.login.fregments.FragmentHome;
import com.example.login.fregments.FragmentIssueList;
import com.example.login.fregments.FragmentProfile;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AllIssueActivity extends AppCompatActivity {
    private TextView textView;
    private TextView textview_user, textView_email;
    Fragment selectedFragment = null;
    UserDetail userDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_issue);

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
//        AppBarConfiguration appBarConfiguration = AppBarConfiguration(setOf)


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemReselectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new FragmentHome()).commit();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.logout,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
//            case R.id.menu_issue_page:
//                startActivity(new Intent(AllIssueActivity.this,IssuePage.class));
//                break;

            case R.id.menu_logout:
                SharedPrefManager.getInstance(this).logout();
//                userDetail.final_username = "";
//                userDetail.final_email = "";
//                userDetail.user_id =0;
                finish();
                startActivity(new Intent(this,MainActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private BottomNavigationView.OnNavigationItemReselectedListener navListener =
            new BottomNavigationView.OnNavigationItemReselectedListener() {
                @Override
                public void onNavigationItemReselected(@NonNull MenuItem item) {

                    switch (item.getItemId()) {
                        case R.id.fragmentHome:
                            selectedFragment = new FragmentHome();
                            break;
                        case R.id.fragmentIssueList:
                            selectedFragment = new FragmentIssueList();
                            break;
                        case R.id.fragmentProfile:
                            selectedFragment = new FragmentProfile();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment, selectedFragment).commit();

                }
            };

}