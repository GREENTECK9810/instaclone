package com.example.instagramclone.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.instagramclone.Adapters.PostAdapter;
import com.example.instagramclone.Model.Post;
import com.example.instagramclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerViewPost;
    private PostAdapter postAdapter;
    private List<Post> mPosts;
    private List<String> mpostIdList;
    private LinearLayoutManager layoutManager;
    private String lastPostId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerViewPost = view.findViewById(R.id.recycler_view_post);
        mPosts = new ArrayList<>();
        mpostIdList = new ArrayList<>();
        postAdapter = new PostAdapter(mPosts, getContext());

        recyclerViewPost.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerViewPost.setLayoutManager(layoutManager);

        recyclerViewPost.setAdapter(postAdapter);

        getPostId();

        recyclerViewPost.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE){
                    getMorePosts();
                }

            }
        });

        return view;
    }

    private void getMorePosts() {

        if(lastPostId == null){
            return;
        }

        Query reference = FirebaseDatabase.getInstance().getReference().child("Timeline")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).orderByKey().endAt(lastPostId).limitToLast(6);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        if(!mpostIdList.contains(snapshot.getKey())){
                            mpostIdList.add(snapshot.getKey());
                        }
                    }
                    if(lastPostId != mpostIdList.get(0)){
                        lastPostId = mpostIdList.get(0);
                    }
                    if(mpostIdList.size() == 6){
                        mpostIdList.remove(0);
                    }else{
                        lastPostId = null;
                    }
                    getPosts(mpostIdList);
                    mpostIdList.clear();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getPostId() {

        Query reference = FirebaseDatabase.getInstance().getReference().child("Timeline")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).limitToLast(6);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        if(!mpostIdList.contains(snapshot.getKey())){
                            mpostIdList.add(snapshot.getKey());
                        }
                    }
                   lastPostId = mpostIdList.get(0);
                    if(mpostIdList.size() == 6){
                        mpostIdList.remove(0);
                    }
                    getPosts(mpostIdList);
                    mpostIdList.clear();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getPosts(List<String> postList) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Posts");

        for (String postId : postList){

            reference.orderByKey().equalTo(postId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            if (!mPosts.contains(snapshot.getValue(Post.class))){
                                mPosts.add(snapshot.getValue(Post.class));
                                postAdapter.notifyDataSetChanged();
                            }

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

    }


}
