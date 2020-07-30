package com.example.instagramclone.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.instagramclone.Adapters.PostAdapter;
import com.example.instagramclone.Model.Post;
import com.example.instagramclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerViewPost;
    private PostAdapter postAdapter;
    private List<Post> mPosts;
    private List<String> mFollowing;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerViewPost = view.findViewById(R.id.recycler_view_post);
        mPosts = new ArrayList<>();
        mFollowing = new ArrayList<>();
        postAdapter = new PostAdapter(mPosts, getContext());

        recyclerViewPost.setHasFixedSize(true);
        recyclerViewPost.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerViewPost.setAdapter(postAdapter);

        getFollowing();

        return view;
    }

    private void getPosts() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Posts");

        for (int i = 0; i < mFollowing.size(); i++){
            reference.orderByChild("publisher").equalTo(mFollowing.get(i)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            Post post = snapshot.getValue(Post.class);
                            mPosts.add(post);
                        }
                        postAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    private void getFollowing() {

        FirebaseDatabase.getInstance().getReference().child("Follow").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Following")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                mFollowing.add(snapshot.getKey());
                            }
                            getPosts();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }
}
