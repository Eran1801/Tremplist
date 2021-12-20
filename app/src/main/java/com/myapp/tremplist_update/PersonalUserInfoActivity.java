package com.myapp.tremplist_update;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class PersonalUserInfoActivity extends AppCompatActivity {

    private EditText first_name, last_name, telephone, _email;
    Button update;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String UID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

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
                Toast.makeText(PersonalUserInfoActivity.this, "error reading from database", Toast.LENGTH_LONG).show();
            }
        });

        update.setOnClickListener(v -> saveUser(v));

    }


    public void saveUser(View view) {
        User user = new User(first_name.getText().toString(), last_name.getText().toString(), telephone.getText().toString(), _email.getText().toString());

        myRef.setValue(user);
    }
}