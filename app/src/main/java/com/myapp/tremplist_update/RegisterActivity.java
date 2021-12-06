package com.myapp.tremplist_update;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

    TextView tvLoginHere;
    Button btnRegister;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Edit Texts
        etRegFirstName = findViewById(R.id.first_name_register);
        etRegLastName = findViewById(R.id.last_name_register);
        etRegTelephone = findViewById(R.id.telephone_number_register);
        etRegEmail = findViewById(R.id.email_register);
        etRegPassword = findViewById(R.id.etRegPass);

        tvLoginHere = findViewById(R.id.tvLoginHere);
        btnRegister = findViewById(R.id.btnRegister);

        mAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(view -> {
            createUser();
        });

        tvLoginHere.setOnClickListener(view -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });
    }

    private void createUser() {

        String firstName = Objects.requireNonNull(etRegFirstName.getText()).toString();
        String lastName = Objects.requireNonNull(etRegLastName.getText()).toString();
        String telephone = Objects.requireNonNull(etRegTelephone.getText()).toString();
        String email = Objects.requireNonNull(etRegEmail.getText()).toString();
        String password = Objects.requireNonNull(etRegPassword.getText()).toString();

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
            // IF its all good we now can add this user to the database
            rootNode = FirebaseDatabase.getInstance();
            reference = rootNode.getReference("users");

            String txt_first_name, txt_last_name, txt_telephone,txt_password;
            txt_first_name = etRegFirstName.getText().toString();
            txt_last_name = etRegLastName.getText().toString();
            txt_telephone = etRegTelephone.getText().toString();
            txt_password = etRegPassword.getText().toString();

            register_user_to_Database(txt_first_name,txt_last_name,txt_telephone,txt_password);

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    } else {
                        Toast.makeText(RegisterActivity.this, "Registration Error: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void register_user_to_Database(String txt_first_name, String txt_last_name, String txt_telephone, String txt_password) {
        User user = new User(txt_first_name, txt_last_name, txt_telephone, txt_password);
//        reference.child(txt_first_name).setValue(user);
        FireBaseDBUser fb = new FireBaseDBUser();
        fb.addUserToDB(user.getFirst_name(), user.getLast_name() , user.getPhone(), user.getPassword());
    }



}