package com.myapp.tremplist_update;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class PersonalUserInfoActivity extends AppCompatActivity {

    private EditText first_name,last_name,telephone,_email;
    Button update;

    // Global var to hold user data inside this activity
    String USER_FIRST_NAME,USER_LAST_NAME,TELEPHONE,EMAIL;

//    DatabaseReference reference;
    FirebaseAuth mAuth= FirebaseAuth.getInstance();
    String UID= Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

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

//        reference = FirebaseDatabase.getInstance().getReference("users");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                assert user != null;
                first_name.setText(user.getFirst_name());
                last_name.setText(user.getLast_name());
                telephone.setText(user.getPhone());
                _email.setText(user.getEmail());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PersonalUserInfoActivity.this,"error reading from database",Toast.LENGTH_LONG).show();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUser(v);
            }
        });





//        showAllUserData();
    }


    public void saveUser(View view) {
       User user = new User(first_name.getText().toString(),last_name.getText().toString(), telephone.getText().toString(),_email.getText().toString());

       myRef.setValue(user);
    }

//    private void showAllUserData(){
//
//        Intent intent = getIntent();
//        USER_FIRST_NAME = intent.getStringExtra("first_name");
//        USER_LAST_NAME = intent.getStringExtra("last_name");
//        TELEPHONE = intent.getStringExtra("phone");
//        EMAIL = intent.getStringExtra("email");
//
//        first_name.setText(USER_FIRST_NAME);
//        last_name.setText(USER_LAST_NAME);
//        telephone.setText(TELEPHONE);
//        email.setText(EMAIL);
//
//    }
//
//    public void update(View view){
//
//        if(isNameChanged() || isLastNameChanged() || isTeleChanged() || isEmailChanged() ){
//            Toast.makeText(this,"Data has been update",Toast.LENGTH_LONG).show();
//        }
//        else {
//            Toast.makeText(this,"Data has same and cannot be update",Toast.LENGTH_LONG).show();
//        }
//
//
//    }
//
//    private boolean isEmailChanged() {
//
//        if (!EMAIL.equals(email.getText().toString())){
//            reference.child(UID).child("email").setValue(email.getText().toString());
//            EMAIL = email.getText().toString();
//            return true;
//        }
//        else {
//            return false;
//        }
//    }
//
//    private boolean isTeleChanged() {
//
//        if (!TELEPHONE.equals(telephone.getText().toString())){
//            reference.child(UID).child("phone").setValue(telephone.getText().toString());
//            TELEPHONE = telephone.getText().toString();
//            return true;
//        }
//        else {
//            return false;
//        }
//    }
//
//    private boolean isLastNameChanged() {
//
//        if (!USER_LAST_NAME.equals(last_name.getText().toString())){
//            reference.child(UID).child("last_name").setValue(last_name.getText().toString());
//            USER_LAST_NAME = last_name.getText().toString();
//            return true;
//        }
//        else {
//            return false;
//        }
//
//    }
//
//    private boolean isNameChanged() {
//
//        if (!USER_FIRST_NAME.equals(first_name.getText().toString())){
//            reference.child(UID).child("first_name").setValue(first_name.getText().toString());
//            USER_FIRST_NAME = first_name.getText().toString();
//            return true;
//        }
//        else {
//            return false;
//        }
//
//    }
}