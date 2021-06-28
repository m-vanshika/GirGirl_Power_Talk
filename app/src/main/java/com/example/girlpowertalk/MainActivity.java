package com.example.girlpowertalk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
public static boolean flag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void next(View v)
    {
        FirebaseAuth firebaseAuth;
        firebaseAuth=FirebaseAuth.getInstance();
      FirebaseUser userId=firebaseAuth.getCurrentUser();
if(userId==null){
        Intent i=new Intent(this,Login.class);
        startActivity(i);
        finish();
}
else
{
    Intent i=new Intent(this,Hompage.class);
    startActivity(i);
    finish();
}
    }
}