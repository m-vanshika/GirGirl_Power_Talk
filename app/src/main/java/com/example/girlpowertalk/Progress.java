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
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Progress extends AppCompatActivity {

    FirebaseAuth fAuth,firebaseAuth;
    static FirebaseFirestore fStore;
    String uid;TextView t;
    ProgressBar progressBar;
    private static  int n=0,p=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        progressBar=findViewById(R.id.progressBar4);
        progressBar.setVisibility(View.VISIBLE);
        t=(TextView)findViewById(R.id.textView15);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.logo1_round);
        fAuth= FirebaseAuth.getInstance();
        fStore= FirebaseFirestore.getInstance();
        uid=fAuth.getCurrentUser().getUid();
        final DocumentReference documentReference=fStore.collection("users").document(uid);
        final Handler h = new Handler();
        h.postDelayed(new Runnable(){

            @Override
            public void run() {
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot=task.getResult();
                            assert documentSnapshot != null;
                            n=(documentSnapshot.getLong("response no").intValue());
                            getstatus(n);
                            //Log.d("hey n","f"+n);
                            p=(documentSnapshot.getLong("status").intValue());
                            if(n!=0) {
                                if (p == 1) {
                                    t.setText("APPLICATION SUBMITTED");
                                    t.setBackgroundColor(Color.parseColor("#35a853"));
                                }
                                else if(p==2) {
                                    t.setText("APPLICATION UNDER EVALUATION");
                                    t.setBackgroundColor(Color.parseColor("#fbbc04"));
                                }
                                else if(p==3) {
                                    t.setText("SHORTLISTED FOR INTERVIEW");
                                    t.setBackgroundColor(Color.parseColor("#35a853"));
                                }
                                else if(p==4) {
                                    t.setText("REJECTED");
                                    t.setBackgroundColor(Color.RED);
                                }
                            }

                            progressBar.setVisibility(View.GONE);

                            // Log.d("jaa","c");
                        }
                        else
                        {
                            Toast.makeText(Progress.this,task.getException().getMessage(),Toast.LENGTH_LONG);
progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        },1000);

    }
    int getstatus(int n)
    {

        fStore= FirebaseFirestore.getInstance();
        DocumentReference documentReference3=fStore.collection("questions").document("Responses").collection("Answers").document("r"+n);
        documentReference3.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){

                    DocumentSnapshot documentSnapshot=task.getResult();
                    assert documentSnapshot != null;
                    p=(documentSnapshot.getLong("progress").intValue());
                    firebaseAuth=FirebaseAuth.getInstance();
                    uid= Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
                    DocumentReference documentReference2=fStore.collection("users").document(uid);
                    Map<String,Object> user=new HashMap<>();
                    user.put("status",p);
                    documentReference2.set(user, SetOptions.merge());


                }
                else
                {
                    Toast.makeText(Progress.this,task.getException().getMessage(),Toast.LENGTH_LONG);

                }
            }
        });
        return p;

    }

    @Override
    protected void onResume() {
        super.onResume();
       //
        // Log.d("jaa","d");
        final DocumentReference documentReference=fStore.collection("users").document(uid);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot=task.getResult();
                    assert documentSnapshot != null;
                    n=(documentSnapshot.getLong("response no").intValue());
                    p=getstatus(n);
                    //Log.d("hey p","f"+p);
                    p=(documentSnapshot.getLong("status").intValue());
                    if(n!=0) {
                        if (p == 1)
                            t.setText("APPLICATION SUBMITTED");
                        else if(p==2)
                            t.setText("APPLICATION UNDER EVALUATION");
                        else if(p==3)
                            t.setText("SHORTLISTED FOR INTERVIEW");
                        else if(p==4)
                            t.setText("REJECTED");
                    }
                }
                else
                {
                    Toast.makeText(Progress.this,task.getException().getMessage(),Toast.LENGTH_LONG);

                }
            }
        });
        if(MainActivity.flag==true)
            finish();
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

                AlertDialog.Builder l=new AlertDialog.Builder(Progress.this);
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

}
