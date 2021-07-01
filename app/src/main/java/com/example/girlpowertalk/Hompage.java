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
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Hompage extends AppCompatActivity {
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String uid;
    private int n,p;
    Button button;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hompage);
        button=(Button)findViewById(R.id.applynow);
        fAuth=FirebaseAuth.getInstance();
        progress=findViewById(R.id.progressBar2);
        progress.setVisibility(View.VISIBLE);
        fStore=FirebaseFirestore.getInstance();
        uid=fAuth.getCurrentUser().getUid();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.logo1_round);
       /* ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#cc0c0c"));
        actionBar.setBackgroundDrawable(colorDrawable);*/

        final DocumentReference documentReference=fStore.collection("users").document(uid);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot=task.getResult();
                    assert documentSnapshot != null;
                    n=(documentSnapshot.getLong("ADMIN").intValue());

                    if(n==1) {
                        button.setText("ADMIN LOGIN");
                        TextView t=findViewById(R.id.t);
                        t.setText("ADMIN PAGE");
                        TextView t1=findViewById(R.id.textView10);
                        t1.setText("Hello Admin,\nWelcome to the admin page of Girl Power Talk.Press the login button to see various questions and submissions recieved.");

                        button.setVisibility(View.VISIBLE);
                    }
                    else {
                        p=(documentSnapshot.getLong("response no").intValue());
                        TextView t=findViewById(R.id.t);
                        t.setText("ABOUT US");
                        TextView r=findViewById(R.id.textView5);
                        TextView t1=findViewById(R.id.textView10);
                        TextView t2=findViewById(R.id.textView8);
                        TextView t3=findViewById(R.id.textView7);
                        t2.setText("#GirlPowerTalk \n");
                        t1.setText("Girl Power Talk strives to inspire girls with persistence, empathy, and confidence. We empower young Women, Men & Non-Binary with merit-based opportunities to grow and achieve their full potential. Our mission, in collaboration with our sister company Blue Ocean Global Technology, is to develop girls in India to become global leaders. We are relentlessly committed to education, gender equality, and integrating the strengths of specially abled communities. We celebrate the diverse talents of each individual. \n Through our nurturing culture of learning and mentorship, we instill young people with exceptional soft-skills, technical knowledge, and purpose in life. We provide a platform to share the voices and stories of girls and women across India. ");
                        t3.setText(" -Rachita Sharma \n Co-Founder, Girl Power Talk \n Chief Marketing Officer,\n Blue Ocean Global Technology \n");
                        r.setText("“One girl empowers another. Let’s change lives together: one girl, one woman and one human being at a time.”\n");
                        if (p == 0)
                            button.setText("APPLY NOW");
                        else
                            button.setText("SEE PROGRESS");
                    }
                    button.setVisibility(View.VISIBLE);
                    progress.setVisibility(View.INVISIBLE);
                }
                else
                {
                    Toast.makeText(Hompage.this,task.getException().getMessage(),Toast.LENGTH_LONG);
                    button.setText("APPLY NOW");
                    progress.setVisibility(View.GONE);
                }
            }
        });
    }
    public void next_page(View view)
    {
        if(n==1)
        {
            Intent i=new Intent(Hompage.this,adminpage.class);
            startActivity(i);
        }
        else
        {
            if(p==0) {
                Intent i = new Intent(Hompage.this, userpage.class);
                startActivity(i);
            }
            else
            {
                Intent i=new Intent(Hompage.this,Progress.class);
                startActivity(i);
            }
        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        //Log.d("hey0","hey");
        return true;
    }
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle presses on the action bar items

        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        switch (item.getItemId()) {

            case R.id.logout:

                AlertDialog.Builder l=new AlertDialog.Builder(Hompage.this);
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
        final DocumentReference documentReference=fStore.collection("users").document(uid);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void
            onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot=task.getResult();
                    assert documentSnapshot != null;
                    n=(documentSnapshot.getLong("ADMIN").intValue());

                    if(n==1) {
                        button.setText("ADMIN LOGIN");
                        TextView t=findViewById(R.id.t);
                        t.setText("ADMIN PAGE");
                        TextView t1=findViewById(R.id.textView10);
                        t1.setText("Hello Admin,\nWelcome to the admin page of Girl Power Talk.Press the login button to see various questions and submissions recieved.");

                    }
                    else {
                        p=(documentSnapshot.getLong("response no").intValue());
                        TextView t=findViewById(R.id.t);
                        t.setText("ABOUT US");
                        TextView r=findViewById(R.id.textView5);
                        TextView t1=findViewById(R.id.textView10);
                        TextView t2=findViewById(R.id.textView8);
                        TextView t3=findViewById(R.id.textView7);
                        t2.setText("\n#GirlPowerTalk ");
                        t1.setText("Girl Power Talk strives to inspire girls with persistence, empathy, and confidence. We empower young Women, Men & Non-Binary with merit-based opportunities to grow and achieve their full potential. Our mission, in collaboration with our sister company Blue Ocean Global Technology, is to develop girls in India to become global leaders. We are relentlessly committed to education, gender equality, and integrating the strengths of specially abled communities. We celebrate the diverse talents of each individual. \n Through our nurturing culture of learning and mentorship, we instill young people with exceptional soft-skills, technical knowledge, and purpose in life. We provide a platform to share the voices and stories of girls and women across India. ");
                        t3.setText(" -Rachita Sharma \n Co-Founder, Girl Power Talk \n Chief Marketing Officer,\n Blue Ocean Global Technology \n");
                        r.setText("“One girl empowers another. Let’s change lives together: one girl, one woman and one human being at a time.”");

                        if (p == 0)
                            button.setText("APPLY NOW");
                        else
                            button.setText("SEE PROGRESS");
                    }
                    progress.setVisibility(View.INVISIBLE);
                }
                else
                {
                    Toast.makeText(Hompage.this,task.getException().getMessage(),Toast.LENGTH_LONG);
                    button.setText("APPLY NOW");
                    progress.setVisibility(View.GONE);
                }
            }
        });
    }
}