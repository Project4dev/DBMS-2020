package com.example.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.login.Adapter.IssueAdapter;
import com.example.login.Model.IssueItems;

import java.util.ArrayList;
import java.util.List;

public class IssuePage extends AppCompatActivity implements View.OnClickListener {
    RecyclerView issueRecyclerview;
    Button btn_issue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_page);

        issueRecyclerview = (RecyclerView)findViewById(R.id.issue_recyclerview);
        btn_issue = (Button)findViewById(R.id.btn_issue);

        List<IssueItems> issueItems = new ArrayList<>();
        issueItems.add(new IssueItems(R.drawable.water,"Water Problem"));
        issueItems.add(new IssueItems(R.drawable.fans,"Fan Problem"));
        issueItems.add(new IssueItems(R.drawable.bench,"Bench Problem"));
        issueItems.add(new IssueItems(R.drawable.projector,"Projector Problem"));
        issueItems.add(new IssueItems(R.drawable.ceiling,"Roof Ceiling Problem"));
        issueItems.add(new IssueItems(R.drawable.light,"Light Problem"));

        issueRecyclerview.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        issueRecyclerview.setLayoutManager(mLayoutManager);
        issueRecyclerview.setItemViewCacheSize(20);
        issueRecyclerview.setDrawingCacheEnabled(true);
        issueRecyclerview.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        //Adapter
        IssueAdapter issueAdapter = new IssueAdapter(issueItems,this);
        issueRecyclerview.setAdapter(issueAdapter);

        btn_issue.setOnClickListener(this);

//        issueRecyclerview.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onClick(View v) {
//        startActivity(new Intent(this,User));
    }
}