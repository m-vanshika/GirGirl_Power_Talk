package com.example.girlpowertalk;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class adminpage extends AppCompatActivity {
FirebaseAuth fAuth;
FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminpage);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.logo1_round);

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

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        Log.d("hey0","hey");
        return true;
    }
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle presses on the action bar items

        fAuth= FirebaseAuth.getInstance();
        fStore= FirebaseFirestore.getInstance();
        switch (item.getItemId()) {

            case R.id.logout:

                AlertDialog.Builder l=new AlertDialog.Builder(adminpage.this);
                l.setTitle("Logout!");
                l.setMessage("Are you sure?");
                l.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        MainActivity.flag=true;
                        startActivity(new Intent(getApplicationContext(),Login.class));
                        finish();

                    }
                });
                l.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //exits
                    }
                });
                l.create().show();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(MainActivity.flag==true)
            finish();
    }
}