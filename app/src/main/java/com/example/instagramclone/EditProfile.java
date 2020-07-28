package com.example.instagramclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.instagramclone.Model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;

import org.w3c.dom.Text;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends AppCompatActivity {

    private ImageView close, check;
    private CircleImageView userimage;
    private EditText name, username, bio;
    private TextView changeProfilePhoto;
    private Uri imageUri;
    private String imageUrl;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        changeProfilePhoto = findViewById(R.id.changeprofile);
        close = findViewById(R.id.close);
        check = findViewById(R.id.check);
        userimage = findViewById(R.id.userprofile);
        name = findViewById(R.id.name);
        username = findViewById(R.id.username);
        bio = findViewById(R.id.bio);

        changeProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().start(EditProfile.this);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });


    }

    private void upload() {

        final ProgressDialog progressDialog = new ProgressDialog(EditProfile.this);
        progressDialog.setMessage("Uploading");
        progressDialog.show();

        if(imageUri != null){
            final StorageReference filepath = FirebaseStorage.getInstance().getReference("Profile Photo")
                    .child(System.currentTimeMillis() + getFileExtension(imageUri));

            //Delete previous user profile photo
//            FirebaseDatabase.getInstance().getReference().child("Users").orderByKey()
//                    .equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
//                                user = snapshot.getValue(User.class);
//
//                            }
//                            String url = "string";
//                            url = user.getImageurl();
//
//                            FirebaseStorage.getInstance().getReference("Profile Photo").child(url).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
//
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//
//                                }
//                            });
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });

            StorageTask uploadTask = filepath.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }

                    return filepath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {

                    Uri downloadUri = task.getResult();
                    imageUrl = downloadUri.toString();

                    HashMap<String, Object> map = new HashMap<>();
                    map.put("imageurl", imageUrl);

                    FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(map);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        if(!TextUtils.isEmpty(name.getText()) || !TextUtils.isEmpty(username.getText())){

            if(!TextUtils.isEmpty(name.getText())){
                HashMap<String, Object> map = new HashMap<>();
                map.put("name", name.getText().toString());
                FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(map);
            }

            if(!TextUtils.isEmpty(username.getText())){
                final String usernameText = username.getText().toString();
                FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("username").equalTo(usernameText).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getChildrenCount() != 0){
                            Toast.makeText(EditProfile.this, "Username already taken, try another username", Toast.LENGTH_SHORT).show();
                        }else{
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("username", usernameText);
                            FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(map);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("bio", bio.getText().toString());
        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(map);

        progressDialog.dismiss();
        Toast.makeText(EditProfile.this, "Profile updated", Toast.LENGTH_SHORT).show();
        finish();

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

            userimage.setImageURI(imageUri);
        }else {
            Toast.makeText(this, "Try again", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(EditProfile.this, MainActivity.class));
            finish();
        }

    }
}