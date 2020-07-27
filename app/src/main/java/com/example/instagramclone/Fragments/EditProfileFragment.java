package com.example.instagramclone.Fragments;

import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.instagramclone.R;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileFragment extends Fragment {

    private CircleImageView imageProfile;
    private EditText name, bio, username;
    private TextView changeProfile;
    private ImageView close, check;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        imageProfile = view.findViewById(R.id.userprofile);
        name = view.findViewById(R.id.name);
        username = view.findViewById(R.id.username);
        bio = view.findViewById(R.id.bio);
        close = view.findViewById(R.id.close);
        check = view.findViewById(R.id.check);

        return view;
    }
}