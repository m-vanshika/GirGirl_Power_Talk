package com.example.girlpowertalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    EditText email,password;
    TextView t,forg;
    Button b;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        forg=(TextView)findViewById(R.id.textView12);
        email=(EditText)findViewById(R.id.editText);
        password=(EditText)findViewById(R.id.editText2);
        t=(TextView)findViewById(R.id.textView3);
        b=(Button)findViewById(R.id.button);
        firebaseAuth=FirebaseAuth.getInstance();
        /*ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#cc0c0c"));
        actionBar.setBackgroundDrawable(colorDrawable);*/
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String e,p;
                e=email.getText().toString().trim();
                p=password.getText().toString().trim();
                if (TextUtils.isEmpty(e)&&TextUtils.isEmpty(p)){
                    password.setError("PASSWORD IS REQUIRED");
                    email.setError("E-MAIL IS REQUIRED");
                    Toast.makeText(Login.this,"FIELDS ARE REQUIRED",Toast.LENGTH_LONG).show();
                    return;
                }
                else  if(TextUtils.isEmpty(e)) {
                    email.setError("E-MAIL IS REQUIRED");
                    return;

                }
                else if(TextUtils.isEmpty(p)) {
                    password.setError("PASSWORD IS REQUIRED");
                    return;
                }
                else if(p.length()<6)
                {
                    password.setError("MUST BE GREATER THAN 6");
                    return;
                }
                firebaseAuth.signInWithEmailAndPassword(e,p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            MainActivity.flag=false;
                            Toast.makeText(Login.this,"WELCOME TO Girl Power Talk",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Login.this,Hompage.class));
                            finish();


                        }
                        else{
                            Toast.makeText(Login.this,"ERROR:"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                        }

                    }
                });
            }
        });
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Signin.class));
            }
        });
        forg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetMail=new EditText(v.getContext());
                AlertDialog.Builder passres=new AlertDialog.Builder(v.getContext());
                passres.setTitle("Reset Password?");
                passres.setMessage("Enter your E-mail to reset password.");
                passres.setView(resetMail);
                passres.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mail=resetMail.getText().toString();
                        firebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Login.this,"Reset link sent to your E-mail",Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this,"Error! Reset Link not sent "+e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                passres.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //close dialog
                    }
                });
                passres.create().show();
            }
        });
    }
}