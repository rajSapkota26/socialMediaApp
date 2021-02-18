package com.technoabinash.mig33;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.hendraanggrian.appcompat.socialview.Hashtag;
import com.hendraanggrian.appcompat.widget.HashtagArrayAdapter;
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;
import com.theartofdev.edmodo.cropper.CropImage;

import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class PostActivity extends AppCompatActivity {
    private ImageView Image;
    private TextView postBtn;
    private SocialAutoCompleteTextView postDesc;
    private Uri imageUri;
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

       Image = findViewById(R.id.postImage);
        postBtn = findViewById(R.id.titlePost);
        postDesc = findViewById(R.id.postDescription);
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload();
            }


        });
        CropImage.activity().start(PostActivity.this);
    }

    private void upload() {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("uploading...");
        dialog.show();
        if (imageUri != null) {
            StorageReference filepath = FirebaseStorage.getInstance().getReference("Posts").child(System.currentTimeMillis()
                    + getFilesExtension(imageUri));
            StorageTask uploadTask = filepath.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful())
                        throw task.getException();
                    return filepath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {

                @Override
                public void onComplete(@NonNull Task<Uri> task) {

                    Uri downloadUri = task.getResult();
                    imageUrl = downloadUri.toString();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
                    String postId = reference.push().getKey();
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("postId", postId);
                    map.put("postImage", imageUrl);
                    map.put("postDescription", postDesc.getText().toString());
                    map.put("postPublisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    reference.child(postId).setValue(map);
                    DatabaseReference hashTagReference = FirebaseDatabase.getInstance().getReference("HashTag");
                    List<String> hashTag=postDesc.getHashtags();
                    if (! hashTag.isEmpty()){
                        for (String tags:hashTag){
                            map.clear();
                            map.put("tag", tags.toLowerCase());
                            map.put("postId", postId);
                            hashTagReference.child(tags.toLowerCase()).child(postId).setValue(map);

                        }
                    }
                    dialog.dismiss();
                    startActivity(new Intent(PostActivity.this, MainActivity.class));
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PostActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Toast.makeText(PostActivity.this,"Image is Not selected...", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFilesExtension(Uri uri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(this.getContentResolver().getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            Image.setImageURI(imageUri);
        } else {
            Toast.makeText(this, "try again", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(PostActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        ArrayAdapter<Hashtag> hashtagAdapter=new HashtagArrayAdapter<>(getApplicationContext());
        FirebaseDatabase.getInstance().getReference().child("HashTag").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                   hashtagAdapter.add(new Hashtag(dataSnapshot.getKey(),(int)dataSnapshot.getChildrenCount()));
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        postDesc.setHashtagAdapter(hashtagAdapter);
    }

    public void backToHome(View view) {
        super.onBackPressed();
    }
}