package com.myapp.tremplist_update;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText forgot_password_email;
    private Button forgot_password_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        forgot_password_btn = findViewById(R.id.forgot_password_btn);
        forgot_password_email = findViewById(R.id.forgot_password_email);

        forgot_password_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String check = checkInputs(); // Check if there is error in the email

                if (check.isEmpty()) {
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    String emailAddress = forgot_password_email.getText().toString();
                    // using google function to reset a password
                    auth.sendPasswordResetEmail(emailAddress)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ForgotPasswordActivity.this, "Password sent to your email", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(ForgotPasswordActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }
            }
        });
    }

    private String checkInputs() {
        String errors = "";
        String email = forgot_password_email.getText().toString();

        if (TextUtils.isEmpty(email)) {
            forgot_password_email.setError("Please insert your email.");
            errors += "Insert your email \n";
        }

        if (!TextUtils.isEmpty(email) && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            forgot_password_email.setError("Please enter valid Email address!");
            errors += "Please enter valid Email address! \n";
        }

        return errors;
    }

}
