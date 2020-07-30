package com.example.instagramclone.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.instagramclone.Model.User;
import com.example.instagramclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchUserProfileFragment extends Fragment {

    private TextView userName, postCount, followerCount, followingCount, fullName;
    private CircleImageView userImage;
    private Button followButton, messageButton, followingButton, editProfile;
    private User user;
    private String userid;
    private RelativeLayout progressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_user_profile, container, false);

        userName = view.findViewById(R.id.username);
        postCount = view.findViewById(R.id.postcount);
        followerCount = view.findViewById(R.id.followercount);
        followingCount = view.findViewById(R.id.followingcount);
        fullName = view.findViewById(R.id.fullname);
        userImage = view.findViewById(R.id.userimage);
        followButton = view.findViewById(R.id.follow);
        messageButton = view.findViewById(R.id.message);
        followingButton = view.findViewById(R.id.following);
        editProfile = view.findViewById(R.id.editprofile);
        progressBar = view.findViewById(R.id.progressbar);


        userid = getArguments().getString("userid");

        inflateProfile();

        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                follow();
            }
        });

        return view;
    }

    private void follow() {

        FirebaseDatabase.getInstance().getReference().child("Follow").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Following")
                .child(userid).setValue(true);

        FirebaseDatabase.getInstance().getReference().child("Follow").child(userid).child("Followers")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);

    }

    private void isFollowed() {
        FirebaseDatabase.getInstance().getReference().child("Follow").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Following")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(userid).exists()){
                    followButton.setVisibility(View.GONE);
                    followingButton.setVisibility(View.VISIBLE);
                }else{
                    followingButton.setVisibility(View.GONE);
                    followButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void inflateProfile() {

        progressBar.setVisibility(View.VISIBLE);

        FirebaseDatabase.getInstance().getReference().child("Users").orderByKey().equalTo(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    user = snapshot.getValue(User.class);
                }

                userName.setText(user.getUsername());
                fullName.setText(user.getName());
                Picasso.get().load(user.getImageurl()).into(userImage);

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
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        isFollowed();

        if(userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            editProfile.setVisibility(View.VISIBLE);
            followButton.setVisibility(View.GONE);
            followingButton.setVisibility(View.GONE);
            messageButton.setVisibility(View.GONE);
        }



    }
}