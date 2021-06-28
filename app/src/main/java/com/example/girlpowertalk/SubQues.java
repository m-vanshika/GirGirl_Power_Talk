package com.example.girlpowertalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SubQues extends AppCompatActivity {

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    public static int n;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_ques);
        // Log.d("hey","itni bar");
        fStore=FirebaseFirestore.getInstance();

        //number of responses
        final DocumentReference documentReference=fStore.collection("questions").document("Responses");

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot=task.getResult();
                    assert documentSnapshot != null;
                    n =(documentSnapshot.getLong("number").intValue());
                    LinearLayout ll=findViewById(R.id.ll);
                    ll.removeAllViews();
                    for (int i=1;i<=n;i++)
                    {
                        // Log.d("hey","kya"+i);
                        DocumentReference documentReference3=fStore.collection("questions").document("Responses").collection("Answers").document("r"+i);
                        int k= i;
                        documentReference3.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    DocumentSnapshot documentSnapshot=task.getResult();
                                    assert documentSnapshot != null;
                                    String name =(documentSnapshot.getString("NAME"));
                                    //    Log.d("hey ", "hum first"+name);
                                    String email=(documentSnapshot.getString("EMAIL"));
                                    String ph=(documentSnapshot.getString("PHONE NUMBER"));
                                    int prog=(documentSnapshot.getLong("progress")).intValue();
                                    if(prog==4) {
                                        return;
                                    }
                                    TextView textView=new TextView(SubQues.this);
                                    textView.setTextColor(Color.BLACK);
                                    textView.setText("\tNAME:"+name+"\n\tE-Mail"+email+"\n\tPhone number:"+ph);
                                    GradientDrawable gd = new GradientDrawable();
// gradient drawable background to transparent
                                    gd.setColor(Color.parseColor("#f8f9fa"));
                                    gd.setStroke(2,Color.BLACK);

//  apply the gradient drawable to the edit text background
                                    textView.setBackground(gd);
                                    // e.setHeight();
                                    textView.setGravity(0);
                                    ll.addView(textView);
                                    textView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            //Log.d("hey","here"+k);
                                            Intent intent=new Intent(SubQues.this,JudgeQues.class);
                                            intent.putExtra("CANDIDATE NUMBER", k+"");
                                            startActivity(intent);
                                        }
                                    });
                                    // Log.v("number", String.valueOf(rn));
                                }
                                else
                                {
                                    Toast.makeText(SubQues.this,task.getException().getMessage(),Toast.LENGTH_LONG);

                                }

                            }
                        });

                    }
                }
                else
                {
                    Toast.makeText(SubQues.this,task.getException().getMessage(),Toast.LENGTH_LONG);

                }

            }
        });


    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        Log.d("hey0","hey");
        return true;
    }
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle presses on the action bar items

        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        switch (item.getItemId()) {

            case R.id.logout:

                AlertDialog.Builder l=new AlertDialog.Builder(SubQues.this);
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