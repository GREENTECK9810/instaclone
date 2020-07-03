package com.example.instagramclone.Fragments;

import android.app.SearchableInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.instagramclone.Adapters.UserAdapter;
import com.example.instagramclone.Model.HashTag;
import com.example.instagramclone.Model.User;
import com.example.instagramclone.R;
import com.example.instagramclone.Adapters.SectionPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment {
    fragmentSearchListener listener;
    fragmentSearchListenerTag listenerTag;

    public interface fragmentSearchListener{
        void userDataSent(List<User> users, String s);
    }

    public interface fragmentSearchListenerTag{
        void tagDataSent(List<HashTag> tags);
    }

    View myFragment;
    ViewPager viewPager;
    TabLayout tabLayout;
    SearchView searchView;
    List<User> mUsers;
    List<HashTag> mTags;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragment = inflater.inflate(R.layout.fragment_search, container, false);

        viewPager = myFragment.findViewById(R.id.viewpager);
        tabLayout = myFragment.findViewById(R.id.tablayout);
        searchView = myFragment.findViewById(R.id.search);

        mUsers = new ArrayList<>();
        mTags = new ArrayList<>();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchUser(query);
                searchTag(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchUser(newText);
                searchTag(newText);

                return true;
            }
        });

        return myFragment;
    }

    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
        super.onAttachFragment(childFragment);
        if(childFragment instanceof fragmentSearchListener){
            listener = (fragmentSearchListener) childFragment;
        }
        if(childFragment instanceof fragmentSearchListenerTag){
            listenerTag = (fragmentSearchListenerTag) childFragment;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
        listenerTag = null;
    }

    private void searchTag(final String s) {
        if(TextUtils.isEmpty(s)){
            mTags.clear();
            listenerTag.tagDataSent(mTags);
            return;
        }

        FirebaseDatabase.getInstance().getReference().child("Hashtags").startAt(s).endAt(s + "\uf8ff").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mTags.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                    HashTag tag = new HashTag();
                    tag.setTag(snapshot.getKey());
                    tag.setPostCount((int) snapshot.getChildrenCount());

                    mTags.add(tag);

                }
                listenerTag.tagDataSent(mTags);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void searchUser(final String s) {
        if(TextUtils.isEmpty(s)){
            mUsers.clear();
            listener.userDataSent(mUsers, s);
            return;
        }

        Query query = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("username").startAt(s).endAt(s + "\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    mUsers.add(snapshot.getValue(User.class));
                }

                listener.userDataSent(mUsers, s);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setUpViewPager(ViewPager viewPager) {

        SectionPagerAdapter adapter = new SectionPagerAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        adapter.addFragment(new SearchAccountFragment(), "ACCOUNT");
        adapter.addFragment(new SearchHashtagFragment(), "TAGS");

        viewPager.setAdapter(adapter);
    }
}
