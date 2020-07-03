package com.example.instagramclone.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagramclone.Model.HashTag;
import com.example.instagramclone.R;

import java.util.List;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder>{

    private List<HashTag> mTags;
    private Context mContext;

    public TagAdapter(List<HashTag> mTags, Context mContext) {
        this.mTags = mTags;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.tag_item, parent, false);

        return new TagAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tagName.setText(mTags.get(position).getTag());
        holder.postCount.setText(mTags.get(position).getPostCount());
    }

    @Override
    public int getItemCount() {
        return mTags.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tagName, postCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tagName = itemView.findViewById(R.id.tagname);
            postCount = itemView.findViewById(R.id.postcount);
        }
    }
}
