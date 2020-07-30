package com.example.instagramclone.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.instagramclone.EditProfile;
import com.example.instagramclone.Model.User;
import com.example.instagramclone.R;
import com.example.instagramclone.SignUp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    private Button logout, editProfile;
    private CircleImageView userimage;
    private TextView followerCount, followingCount, postCount, name, username;
    private User user;
    private String userid;
    private FrameLayout progress;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        logout = view.findViewById(R.id.logout);
        editProfile = view.findViewById(R.id.editprofile);
        userimage = view.findViewById(R.id.userimage);
        followerCount = view.findViewById(R.id.followercount);
        followingCount = view.findViewById(R.id.followingcount);
        postCount = view.findViewById(R.id.postcount);
        name = view.findViewById(R.id.fullname);
        username = view.findViewById(R.id.username);
        progress = view.findViewById(R.id.progress);
        userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        inflaterViews();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), SignUp.class));
                getActivity().finish();
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), EditProfile.class));

            }
        });


        return view;
    }

    private void inflaterViews() {

        progress.setVisibility(View.VISIBLE);

        FirebaseDatabase.getInstance().getReference().child("Users").orderByKey().equalTo(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    user = snapshot.getValue(User.class);
                }

                username.setText(user.getUsername());
                name.setText(user.getName());
                Picasso.get().load(user.getImageurl()).into(userimage);

                FirebaseDatabase.getInstance().getReference().child("Follow").child(userid).child("Followers")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                followerCount.setText((int) dataSnapshot.getChildrenCount() + "");
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                FirebaseDatabase.getInstance().getReference().child("Follow").child(userid).child("Following")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                followingCount.setText((int) dataSnapshot.getChildrenCount() + "");

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                FirebaseDatabase.getInstance().getReference().child("Posts").orderByChild("publisher").equalTo(userid)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                postCount.setText((int) dataSnapshot.getChildrenCount() + "");
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                progress.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
