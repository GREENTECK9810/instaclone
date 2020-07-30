package com.example.instagramclone.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextClock;
import android.widget.TextView;

import com.example.instagramclone.Adapters.UserAdapter;
import com.example.instagramclone.MainActivity;
import com.example.instagramclone.Model.HashTag;
import com.example.instagramclone.Model.User;
import com.example.instagramclone.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class SearchAccountFragment extends Fragment implements SearchFragment.fragmentSearchListener{

    View view;
    RecyclerView usersList;
    List<User> mUsers;
    UserAdapter adapter;
    TextView textView;
    FrameLayout frameLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search_account, container, false);
        usersList = view.findViewById(R.id.userlist);
        frameLayout = view.findViewById(R.id.progress);


        usersList.setHasFixedSize(true);
        usersList.setLayoutManager(new LinearLayoutManager(getContext()));
        mUsers = new ArrayList<>();
        adapter = new UserAdapter(getContext(), mUsers, true);
        textView = view.findViewById(R.id.searchtext);

        usersList.setAdapter(adapter);

        return view;
    }


    @Override
    public void userDataSent(List<User> users, String s) {
        if(users != null){
            textView.setText(s);
            mUsers.clear();
            for (User user : users){
                mUsers.add(user);
            }
            adapter.notifyDataSetChanged();
            return;
        }

        mUsers.clear();
        adapter.notifyDataSetChanged();

    }


}