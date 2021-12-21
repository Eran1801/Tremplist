package com.myapp.tremplist_update;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

public class PersonalUserInfoActivity extends AppCompatActivity {

    private EditText first_name, last_name, telephone, _email;
    Button update;
    Button AddProfileImage;
    ImageView ProfilePicture;

    User user; //for profile picture

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String UID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

    StorageReference storage = FirebaseStorage.getInstance().getReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://tremplistupdtaefirebase-default-rtdb.firebaseio.com/");
    DatabaseReference myRef = database.getReference("users/" + UID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_user_info);

        update = findViewById(R.id.updateBtn);

        first_name = findViewById(R.id.personal_firstName);
        last_name = findViewById(R.id.personal_lastName);
        telephone = findViewById(R.id.personal_telephone);
        _email = findViewById(R.id.personal_email);

        user=null;

        AddProfileImage = findViewById(R.id.take_picture);
        ProfilePicture = findViewById(R.id.profile_picture);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                assert user != null;
                first_name.setText(user.getFirst_name());
                last_name.setText(user.getLast_name());
                telephone.setText(user.getPhone());
                _email.setText(user.getEmail());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PersonalUserInfoActivity.this, "error reading from database", Toast.LENGTH_LONG).show();
            }
        });

        update.setOnClickListener(v -> saveUser(v));

        AddProfileImage.setOnClickListener(view -> {
            if(user != null) {
                uploadImage(view);
            }
            else {
                Toast.makeText(PersonalUserInfoActivity.this, "register user first then add a photo", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void saveUser(View view) {
        User user = new User(first_name.getText().toString(), last_name.getText().toString(), telephone.getText().toString(), _email.getText().toString());

        myRef.setValue(user);
    }

    public void uploadImage(View v){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 101);
    }
    protected void onActivityResult(int rcode, int rscode, Intent data){
        super.onActivityResult(rcode, rscode, data);

        if(rscode == Activity.RESULT_OK){
            if(rcode == 101){
                onCaptureImageResult(data);
            }
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap bit = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        byte bb[] = bytes.toByteArray();
        ProfilePicture.setImageBitmap(bit);

        uploadPhototofirebase(bb);
    }

    private void uploadPhototofirebase(byte[] bb) {
        String email = user.getEmail();
        StorageReference sr =  storage.child("profilePictures/"+email);
        sr.putBytes(bb).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(PersonalUserInfoActivity.this, "photo update to DB", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PersonalUserInfoActivity.this, "Failed upload", Toast.LENGTH_SHORT).show();
            }
        });
    }
}