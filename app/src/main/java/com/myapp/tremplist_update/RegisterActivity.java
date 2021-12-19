package com.myapp.tremplist_update;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    TextInputEditText etRegEmail;
    TextInputEditText etRegPassword;
    TextInputEditText etRegFirstName;
    TextInputEditText etRegLastName;
    TextInputEditText etRegTelephone;

    TextView tvLoginHere; // if you already register and want to login
    Button btnRegister;
    ImageButton AddProfileImage;
    ImageView ProfilePicture;

    FirebaseAuth mAuth;

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
        AddProfileImage = findViewById(R.id.add_image_register);
        ProfilePicture = findViewById(R.id.imageView4);
        mAuth = FirebaseAuth.getInstance();

        AddProfileImage.setOnClickListener(view -> {
            // request permission for camera
            if(ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{
                        Manifest.permission.CAMERA
                }, 100);
            }
            take_picture();
        });

        // Register button
        btnRegister.setOnClickListener(view -> {
            createUser();
        });

        tvLoginHere.setOnClickListener(view -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });
    }

    private void take_picture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 100);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ProfilePicture.setImageBitmap(bitmap);
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



}