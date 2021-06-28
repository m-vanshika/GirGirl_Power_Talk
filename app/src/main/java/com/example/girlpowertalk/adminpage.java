package com.example.girlpowertalk;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class adminpage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminpage);
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#cc0c0c"));
        actionBar.setBackgroundDrawable(colorDrawable);


    }
    public void changeq(View view)
    {
        Intent i=new Intent(adminpage.this,ChangeQuestions.class);
        startActivity(i);
    }
    public void subform(View v)
    {
        Intent i=new Intent(adminpage.this,SubQues.class);
        startActivity(i);
    }
}