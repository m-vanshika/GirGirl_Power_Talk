package com.example.girlpowertalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Signin extends AppCompatActivity {
    EditText email, password,name,number;
    Button sign;
    TextView t;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    FirebaseFirestore firestore;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        name=(EditText)findViewById(R.id.n);
        number=(EditText)findViewById(R.id.pn);
        progressBar=findViewById(R.id.progressBar3);
        email = (EditText) findViewById(R.id.e);
        password = (EditText) findViewById(R.id.p);
        sign = (Button) findViewById(R.id.signup);
        t=(TextView) findViewById(R.id.already);
        firebaseAuth = FirebaseAuth.getInstance();
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#cc0c0c"));
        actionBar.setBackgroundDrawable(colorDrawable);

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String e,p,n,pn,h,r;
                e=email.getText().toString().trim();
                p=password.getText().toString().trim();
                n=name.getText().toString().trim();
                pn=number.getText().toString().trim();
                if (TextUtils.isEmpty(n)){
                    name.setError("NAME IS REQUIRED");
                    return;
                }
                if (TextUtils.isEmpty(pn)){
                    number.setError("PHONE NUMBER IS REQUIRED");
                    return;
                }

                if(TextUtils.isEmpty(e)) {
                    email.setError("E-MAIL IS REQUIRED");
                    return;

                }
                if(TextUtils.isEmpty(p)) {
                    password.setError("PASSWORD IS REQUIRED");
                    return;
                }
                if(p.length()<6)
                {
                    password.setError("MUST BE GREATER THAN 6");
                    return;
                }
                if (pn.length()!=10)
                {
                    number.setError("Enter a valid number");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                firestore=FirebaseFirestore.getInstance();
                firebaseAuth.createUserWithEmailAndPassword(e,p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            FirebaseUser fuser=firebaseAuth.getCurrentUser();
                          ///  Toast.makeText(Signin.this,"NOW LOGIN",Toast.LENGTH_SHORT).show();
                            userId=firebaseAuth.getCurrentUser().getUid();
                            DocumentReference documentReference=firestore.collection("users").document(userId);
                            Map<String,Object> user=new HashMap<>();
                            user.put("NAME",n);
                            user.put("EMAIL",e);
                            user.put("PHONE NUMBER",pn);
                            user.put("ADMIN",0);
                            user.put("response no",0);
                            user.put("status",0);

                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Signin.this,"DETAILS SAVED",Toast.LENGTH_LONG).show();

                                    FirebaseAuth.getInstance().signOut();
                                }
                            });
                            progressBar.setVisibility(View.GONE);
                            startActivity(new Intent(getApplicationContext(),Login.class));
                            finish();


                        }
                        else{
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(Signin.this,"ERROR:"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    }
                });





            }
        });

        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Signin.this,Login.class));
                finish();
            }
        });
    }
}