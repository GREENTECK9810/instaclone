package com.example.instagramclone.Fragments;

import android.icu.text.SearchIterator;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.instagramclone.Adapters.TagAdapter;
import com.example.instagramclone.Model.HashTag;
import com.example.instagramclone.Model.User;
import com.example.instagramclone.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hendraanggrian.appcompat.socialview.Hashtag;

import java.util.ArrayList;
import java.util.List;


public class SearchHashtagFragment extends Fragment implements SearchFragment.fragmentSearchListenerTag{

    RecyclerView tagList;
    View view;
    List<HashTag> mTags;
    TagAdapter tagAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search_hashtag, container, false);

        tagList = view.findViewById(R.id.taglist);
        mTags = new ArrayList<>();
        tagAdapter = new TagAdapter(mTags, getContext());
        tagList.setHasFixedSize(true);
        tagList.setLayoutManager(new LinearLayoutManager(getContext()));
        tagList.setAdapter(tagAdapter);



        return view;
    }

    @Override
    public void tagDataSent(List<HashTag> tags) {
        if(tags != null){
            mTags.clear();
            for (HashTag tag : tags){
                mTags.add(tag);
            }
            tagAdapter.notifyDataSetChanged();
            return;
        }

        mTags.clear();
        tagAdapter.notifyDataSetChanged();

    }



}