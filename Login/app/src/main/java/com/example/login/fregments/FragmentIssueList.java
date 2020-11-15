package com.example.login.fregments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.login.Adapter.IssueAdapter;
import com.example.login.Model.IssueItems;
import com.example.login.R;
import com.example.login.UserIssue;
import com.example.login.Utils.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentIssueList extends Fragment {

    RecyclerView issue_recyclerview;
    Button btn_isssue;

    public FragmentIssueList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_issue_list, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        issue_recyclerview = view.findViewById(R.id.issue_recyclerview);
        btn_isssue = view.findViewById(R.id.btn_issue);

        List<IssueItems> issueItems = new ArrayList<>();
        issueItems.add(new IssueItems(R.drawable.water,"Water Problem"));
        issueItems.add(new IssueItems(R.drawable.fans,"Fan Problem"));
        issueItems.add(new IssueItems(R.drawable.bench,"Bench Problem"));
        issueItems.add(new IssueItems(R.drawable.projector,"Projector Problem"));
        issueItems.add(new IssueItems(R.drawable.ceiling,"Roof Ceiling Problem"));
        issueItems.add(new IssueItems(R.drawable.light,"Light Problem"));

        issue_recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));

        IssueAdapter issueAdapter = new IssueAdapter(issueItems,getContext());
        issue_recyclerview.setAdapter(issueAdapter);

        btn_isssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.currentItem = null;
                startActivity(new Intent(getContext(), UserIssue.class));
            }
        });

    }
}