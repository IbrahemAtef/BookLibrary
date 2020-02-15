package com.example.booklibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {

    EditText et_email, et_userName, et_pass, et_ph;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    Button btn_signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        inflate();
        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = et_email.getText().toString();
                final String pass = et_pass.getText().toString();
                final String ph = et_ph.getText().toString();
                final String user = et_userName.getText().toString();
                if(email.isEmpty()|| pass.isEmpty() || ph.isEmpty() || user.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "please fill all blanks", Toast.LENGTH_SHORT).show();
                } else {
                    addUser(email, pass, user, ph);
                }
            }
        });
    }

    private void addUser(final String email, final String pass, final String user, final String ph) {
        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "User signed successfully", Toast.LENGTH_SHORT).show();
                    User u = new User(email,user,pass, ph,null);
                    db.collection("Users").document(u.getEmail()).set(u);
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "User signed failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void inflate(){
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        et_email = findViewById(R.id.et_reg_email);
        et_userName = findViewById(R.id.et_reg_userName);
        et_pass = findViewById(R.id.et_reg_password);
        et_ph = findViewById(R.id.et_reg_ph);
        btn_signUp = findViewById(R.id.btn_reg_sign_up);
    }

    public void btn_return(View view) {
        finish();
    }

    public void loginActivity(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
