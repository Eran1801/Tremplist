package com.myapp.tremplist_update.UI;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.myapp.tremplist_update.fireBase.FireBaseDBActivity;
import com.myapp.tremplist_update.R;
import com.myapp.tremplist_update.model.User;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

public class RegisterActivity<privete> extends AppCompatActivity {

    FirebaseDatabase rootNode;
    DatabaseReference reference;
    StorageReference storage = FirebaseStorage.getInstance().getReference();

    TextInputEditText etRegEmail;
    TextInputEditText etRegPassword;
    TextInputEditText etRegFirstName;
    TextInputEditText etRegLastName;
    TextInputEditText etRegTelephone;

    byte [] bb;

    TextView tvLoginHere; // if you already register and want to login
    Button btnRegister;
    Button profile_picture;

    FirebaseAuth mAuth;

    static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Getting the information from the user
        etRegFirstName = findViewById(R.id.first_name_register);
        etRegLastName = findViewById(R.id.last_name_register);
        etRegTelephone = findViewById(R.id.telephone_number_register);
        etRegEmail = findViewById(R.id.email_register);
        etRegPassword = findViewById(R.id.etRegPass);

        tvLoginHere = findViewById(R.id.tvLoginHere);
        btnRegister = findViewById(R.id.btnRegister);
        profile_picture = findViewById(R.id.camera);


        mAuth = FirebaseAuth.getInstance();



        // Register button
        btnRegister.setOnClickListener(view -> {
            createUser();
        });

        profile_picture.setOnClickListener(view ->{
            uploadImage(view);
        });


    }
    public void uploadImage(View v) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 101);
    }

    protected void onActivityResult(int rcode, int rscode, Intent data) {
        super.onActivityResult(rcode, rscode, data);
        checkPermission();
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
        bb = bytes.toByteArray();
        //TODO: put image on screen
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


    private void createUser() {

        // Take all the data that the User insert
        String firstName = Objects.requireNonNull(etRegFirstName.getText()).toString();
        String lastName = Objects.requireNonNull(etRegLastName.getText()).toString();
        String telephone = Objects.requireNonNull(etRegTelephone.getText()).toString();
        String email = Objects.requireNonNull(etRegEmail.getText()).toString();
        String password = Objects.requireNonNull(etRegPassword.getText()).toString();

        // Checking if some of the information is null
        if (TextUtils.isEmpty(email)) {
            etRegEmail.setError("Email cannot be empty");
            etRegEmail.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            etRegPassword.setError("Password cannot be empty");
            etRegPassword.requestFocus();
        } else if (TextUtils.isEmpty(firstName)) {
            etRegFirstName.setError("First Name cannot be empty");
            etRegFirstName.requestFocus();
        } else if (TextUtils.isEmpty(lastName)) {
            etRegLastName.setError("Last Name cannot be empty");
            etRegLastName.requestFocus();
        }
        else if (TextUtils.isEmpty(telephone)){
            etRegTelephone.setError("Telephone cannot be empty");
            etRegTelephone.requestFocus();
        }
        else {
            // If its all good we now can add this user to the database
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("users");

            // Using the function from firebase to create a User in the Authentication with his email and password
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                        register_user_to_Database(Objects.requireNonNull(mAuth.getCurrentUser()).getUid(), firstName,lastName,telephone,email);

                        if (bb!=null) {
                            StorageReference sr = storage.child("profilePictures/" + mAuth.getUid());
                            sr.putBytes(bb).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Toast.makeText(RegisterActivity.this, "photo update to DB", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RegisterActivity.this, "Failed upload", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    } else {
                        Toast.makeText(RegisterActivity.this, "Registration Error: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    // Insert the User to my RealTime DataBase
    private void register_user_to_Database(String id, String txt_first_name, String txt_last_name, String txt_telephone, String txt_email) {
        User user = new User(id, txt_first_name, txt_last_name, txt_telephone, txt_email);
        FireBaseDBActivity fb = new FireBaseDBActivity();
        fb.addUserToDB(user);
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(RegisterActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }



}