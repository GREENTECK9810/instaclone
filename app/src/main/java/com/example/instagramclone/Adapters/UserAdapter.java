package com.example.instagramclone.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagramclone.Fragments.SearchUserProfileFragment;
import com.example.instagramclone.MainActivity;
import com.example.instagramclone.Model.User;
import com.example.instagramclone.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context mContext;
    private List<User> users;
    private boolean isFragment;

    public UserAdapter(Context mContext, List<User> users, boolean isFragment) {
        this.mContext = mContext;
        this.users = users;
        this.isFragment = isFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);

        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final User user = users.get(position);

        holder.name.setText(user.getName());
        holder.username.setText(user.getUsername());
        Picasso.get().load(user.getImageurl()).placeholder(R.drawable.ic_person_black_24dp).into(holder.imageProfile);

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("userid", user.getId());

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                SearchUserProfileFragment nextFrag= new SearchUserProfileFragment();
                Fragment fragment = new SearchUserProfileFragment();
                fragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public CircleImageView imageProfile;
        public TextView name;
        public TextView username;
        public RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageProfile = itemView.findViewById(R.id.userimage);
            name = itemView.findViewById(R.id.name);
            username = itemView.findViewById(R.id.username);
            parentLayout = itemView.findViewById(R.id.useritem);

        }
    }

}
