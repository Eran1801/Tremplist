package com.myapp.tremplist_update;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class PersonalUserInfoActivity extends AppCompatActivity {

    EditText first_name,last_name,telephone,email;

    // Global var to hold user data inside this activity
    String USER_FIRST_NAME,USER_LAST_NAME,TELEPHONE,EMAIL;

    DatabaseReference reference;
    FirebaseAuth mAuth= FirebaseAuth.getInstance();
    String UID= Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_user_info);

        reference = FirebaseDatabase.getInstance().getReference("users");

        first_name = findViewById(R.id.personal_firstName);
        last_name = findViewById(R.id.personal_lastName);
        telephone = findViewById(R.id.personal_telephone);
        email = findViewById(R.id.personal_email);

        showAllUserData();
    }

    private void showAllUserData(){

        Intent intent = getIntent();
        USER_FIRST_NAME = intent.getStringExtra("first_name");
        USER_LAST_NAME = intent.getStringExtra("last_name");
        TELEPHONE = intent.getStringExtra("phone");
        EMAIL = intent.getStringExtra("email");

        first_name.setText(USER_FIRST_NAME);
        last_name.setText(USER_LAST_NAME);
        telephone.setText(TELEPHONE);
        email.setText(EMAIL);

    }

    public void update(View view){

        if(isNameChanged() || isLastNameChanged() || isTeleChanged() || isEmailChanged() ){
            Toast.makeText(this,"Data has been update",Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this,"Data has same and cannot be update",Toast.LENGTH_LONG).show();
        }


    }

    private boolean isEmailChanged() {

        if (!EMAIL.equals(email.getText().toString())){
            reference.child(UID).child("email").setValue(email.getText().toString());
            EMAIL = email.getText().toString();
            return true;
        }
        else {
            return false;
        }
    }

    private boolean isTeleChanged() {

        if (!TELEPHONE.equals(telephone.getText().toString())){
            reference.child(UID).child("phone").setValue(telephone.getText().toString());
            TELEPHONE = telephone.getText().toString();
            return true;
        }
        else {
            return false;
        }
    }

    private boolean isLastNameChanged() {

        if (!USER_LAST_NAME.equals(last_name.getText().toString())){
            reference.child(UID).child("last_name").setValue(last_name.getText().toString());
            USER_LAST_NAME = last_name.getText().toString();
            return true;
        }
        else {
            return false;
        }

    }

    private boolean isNameChanged() {

        if (!USER_FIRST_NAME.equals(first_name.getText().toString())){
            reference.child(UID).child("first_name").setValue(first_name.getText().toString());
            USER_FIRST_NAME = first_name.getText().toString();
            return true;
        }
        else {
            return false;
        }

    }
}