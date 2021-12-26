package com.myapp.tremplist_update.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

//import com.bumptech.glide.Glide;
//import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.myapp.tremplist_update.fireBase.FireBaseDBActivity;
import com.myapp.tremplist_update.R;
import com.myapp.tremplist_update.model.User;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Objects;

public class PersonalUserInfoActivity extends AppCompatActivity {

    static final int PERMISSION_REQUEST_CODE = 200;
    EditText first_name, last_name, telephone;
    Button update;
    Button AddProfileImage;
    ImageView ProfilePicture;

    FireBaseDBActivity fb;

    User user; //for profile picture
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String UID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
    private Bitmap my_image;

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

        user = null;

        AddProfileImage = findViewById(R.id.take_picture);
        ProfilePicture = findViewById(R.id.profile_picture);
        StorageReference storageReference = storage.child("profilePictures");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                assert user != null;
                first_name.setText(user.getFirst_name());
                last_name.setText(user.getLast_name());
                telephone.setText(user.getPhone());

                try {
                    final File localFile = File.createTempFile("img", "jpg");
                    storageReference.child(Objects.requireNonNull(mAuth.getUid())).getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            my_image = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            ProfilePicture.setImageBitmap(my_image);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(PersonalUserInfoActivity.this, "שגיאה בעת הורדת תמונה", Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PersonalUserInfoActivity.this, "error reading from database", Toast.LENGTH_LONG).show();
            }
        });

        update.setOnClickListener(v -> saveUser(v));

        AddProfileImage.setOnClickListener(view -> {
            if (user != null) {
                if (checkPermission()) {
                    //main logic or main code

                    // . write your main code to execute, It will execute if the permission is already given.

                } else {
                    requestPermission();
                }
                uploadImage(view);
            } else {
                Toast.makeText(PersonalUserInfoActivity.this, "register user first then add a photo", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void saveUser(View view) {
        User user = User.create_user_for_personal_info(UID, first_name.getText().toString(), last_name.getText().toString(), telephone.getText().toString());
        try {
            myRef.setValue(user);
            fb = new FireBaseDBActivity();
            fb.update_relevant_driver_details(user, UID);
            Toast.makeText(PersonalUserInfoActivity.this, "הידד הפרטים מעודכנים", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void uploadImage(View v) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 101);
    }

    protected void onActivityResult(int rcode, int rscode, Intent data) {
        super.onActivityResult(rcode, rscode, data);

        if (rscode == Activity.RESULT_OK) {
            if (rcode == 101) {
                onCaptureImageResult(data);
            }
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap bit = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        byte [] bb = bytes.toByteArray();
        ProfilePicture.setImageBitmap(bit);

        uploadPhototofirebase(bb);
    }

    private void uploadPhototofirebase(byte[] bb) {
        String first_name = user.getFirst_name();
        StorageReference sr = storage.child("profilePictures/" + mAuth.getUid());
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

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();

                    // main logic
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel("You need to allow access permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermission();
                                            }
                                        }
                                    });
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(PersonalUserInfoActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}