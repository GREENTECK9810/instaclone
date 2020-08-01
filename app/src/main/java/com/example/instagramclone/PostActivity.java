package com.example.instagramclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.hendraanggrian.appcompat.widget.SocialEditText;
import com.hendraanggrian.appcompat.widget.SocialTextView;
import com.theartofdev.edmodo.cropper.CropImage;

import java.net.URL;
import java.security.CryptoPrimitive;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PostActivity extends AppCompatActivity {

    private ImageView close, image;
    private String imageUrl;
    private TextView post;
    private SocialEditText description;
    private Uri imageUri;
    private List<String> mFollowers;
    private int followersCount = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        close = findViewById(R.id.close);
        image = findViewById(R.id.image);
        post = findViewById(R.id.post);
        description = findViewById(R.id.description);
        mFollowers = new ArrayList<>();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PostActivity.this, MainActivity.class));
                finish();
            }
        });

        getFollowers();

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                while (true){
                    if(followersCount == mFollowers.size() && followersCount != -1){
                        upload();
                        break;
                    }
                }

            }
        });



        CropImage.activity().start(PostActivity.this);

    }

    private void getFollowers() {

        // getting all followers list to update their timeline at one time
        FirebaseDatabase.getInstance().getReference().child("Follow").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Followers")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        followersCount = (int) dataSnapshot.getChildrenCount();

                        if(dataSnapshot.exists()){
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                mFollowers.add(snapshot.getKey());
                            }
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {



                    }
                });

    }

    private void upload() {

        final ProgressDialog progressDialog = new ProgressDialog(PostActivity.this);
        progressDialog.setMessage("Uploading");
        progressDialog.show();

        if(imageUri != null) {
            final StorageReference filepath = FirebaseStorage.getInstance().getReference("posts").child(System.currentTimeMillis() + getFileExtension(imageUri));

            StorageTask uploadTask = filepath.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return filepath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Uri downloadUri = task.getResult();
                    imageUrl = downloadUri.toString();


                    doPost(imageUrl, mFollowers);

                    progressDialog.dismiss();
                    Toast.makeText(PostActivity.this, "Post added", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PostActivity.this, MainActivity.class));
                    finish();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PostActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "No image was selected", Toast.LENGTH_SHORT).show();
        }

    }

    private void doPost(String imageUrl, List<String> followers) {
        HashMap<String, Object> fanout = new HashMap<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
        DatabaseReference timelineRef = FirebaseDatabase.getInstance().getReference("Timeline");
        String postId = ref.push().getKey();

        for (int i = 0; i < followers.size(); i++){
            fanout.put(followers.get(i) + "/" + postId, true);
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("commentscount", (int)0);
        map.put("likescount", (int)0);
        map.put("createdAt", ServerValue.TIMESTAMP);
        map.put("postid", postId);
        map.put("imageurl", imageUrl);
        map.put("description", description.getText().toString());
        map.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());

        ref.child(postId).setValue(map);
        if (!followers.isEmpty()){
            timelineRef.updateChildren(fanout);
        }



        DatabaseReference mHashTagRef = FirebaseDatabase.getInstance().getReference("Hashtags");

        List<String> hashTags = description.getHashtags();

        if (!hashTags.isEmpty()) {
            for (String tag : hashTags) {
                map.clear();

                map.put("tag", tag.toLowerCase());
                map.put("postid", postId);

                mHashTagRef.child(tag.toLowerCase()).child(postId).setValue(map);
            }
        }
    }

    private String getFileExtension(Uri uri) {

        return MimeTypeMap.getSingleton().getExtensionFromMimeType(this.getContentResolver().getType(uri));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            image.setImageURI(imageUri);
        }else {
            Toast.makeText(this, "Try again", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(PostActivity.this, MainActivity.class));
            finish();
        }

    }
}
