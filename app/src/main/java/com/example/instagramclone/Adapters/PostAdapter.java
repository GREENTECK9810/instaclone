package com.example.instagramclone.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagramclone.Model.Post;
import com.example.instagramclone.Model.User;
import com.example.instagramclone.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.type.PostalAddress;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    List<Post> mPosts;
    Context mContext;
    FirebaseUser firebaseUser;

    public PostAdapter(List<Post> mPosts, Context mContext) {
        this.mPosts = mPosts;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_item, parent, false);

        return new PostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        Post post = mPosts.get(position);

        FirebaseDatabase.getInstance().getReference().child("Users").child(post.getPublisher()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                Picasso.get().load(user.getImageurl()).into(holder.authorImage);
                holder.authorName.setText(user.getUsername());
                holder.authorNameBelow.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView authorImage;
        public ImageView more;
        public ImageView like;
        public ImageView comment;
        public ImageView save;
        public ImageView postImage;

        public TextView authorName;
        public TextView description;
        public TextView noOfLikes;
        public TextView noOfComments;
        public TextView authorNameBelow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            authorImage = itemView.findViewById(R.id.authorimage);
            more = itemView.findViewById(R.id.more);
            like = itemView.findViewById(R.id.like);
            comment = itemView.findViewById(R.id.comment);
            save = itemView.findViewById(R.id.save);
            postImage = itemView.findViewById(R.id.postimage);

            authorName = itemView.findViewById(R.id.postauthorname);
            description = itemView.findViewById(R.id.description);
            noOfLikes = itemView.findViewById(R.id.like_count);
            noOfComments = itemView.findViewById(R.id.no_of_comments);
            authorNameBelow = itemView.findViewById(R.id.authornamebelow);


        }
    }

}
