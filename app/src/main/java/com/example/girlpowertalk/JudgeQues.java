package com.example.girlpowertalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class JudgeQues extends AppCompatActivity {
    FirebaseFirestore fStore;
    DocumentReference documentReference3;
    FirebaseAuth fAuth;
    DatabaseReference database;

    String message;
    private FirebaseStorage storage;
ProgressBar pr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_judge_ques);
pr=findViewById(R.id.progressBar6);
        storage=FirebaseStorage.getInstance();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.logo1_round);
        String t= getIntent().getStringExtra("CANDIDATE NUMBER");
        fStore=FirebaseFirestore.getInstance();
        pr.setVisibility(View.VISIBLE);
        documentReference3=fStore.collection("questions").document("Responses").collection("Answers").document("r"+t);
        documentReference3.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    LinearLayout ll=findViewById(R.id.ll);
                    DocumentSnapshot documentSnapshot=task.getResult();
                    assert documentSnapshot != null;
                    String p="";
                    String name =(documentSnapshot.getString("NAME"));
                    int prog=(documentSnapshot.getLong("progress")).intValue();
                    int total=(documentSnapshot.getLong("total")).intValue();
                    if(prog==1) {
                        p="Submitted";
                    }
                    else if(prog==2)
                    {
                        p="Under Evaluation";
                    }
                    else if(prog==3)
                    {
                        p="Shortlisted for interview";
                    }
                    else if(prog==4)
                    {
                        p="Rejected";
                    }
                    TextView textView=findViewById(R.id.name);
                    textView.setText("\t"+name+" - "+p);
                    String q,a;
                    TextView t11=new TextView(JudgeQues.this);
                    TextView t22=new TextView(JudgeQues.this);
                    t11.setTextColor(Color.BLACK);
                    t22.setTextColor(Color.GRAY);
                    t11.setTextSize(20);
                    t22.setTextSize(25);
                    q="NAME";
                    GradientDrawable gd = new GradientDrawable();
// gradient drawable background to transparent
                    gd.setColor(Color.parseColor("#fafafa"));
                    gd.setStroke(5,Color.parseColor("#f8f9fa"));
                    FrameLayout.LayoutParams layoutParams =
                            new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,150);
                    layoutParams.setMargins(20, 20, 20, 20);
                    t22.setLayoutParams(layoutParams);

                    a=documentSnapshot.getString("NAME");
                    t11.setText(q);
                    t22.setBackground(gd);
                    t22.setText(a);
                    ll.addView(t11);
                    ll.addView(t22);
                    TextView t12=new TextView(JudgeQues.this);
                    TextView t21=new TextView(JudgeQues.this);
                    t12.setTextColor(Color.BLACK);
                    t21.setTextColor(Color.GRAY);
                    t12.setTextSize(20);
                    t21.setTextSize(25);
                    q="AGE";
                    t21.setLayoutParams(layoutParams);

                    a=documentSnapshot.getString("AGE");
                    t12.setText(q);
                    t21.setBackground(gd);
                    t21.setText(a);
                    ll.addView(t12);
                    ll.addView(t21);
                    TextView t13=new TextView(JudgeQues.this);
                    TextView t23=new TextView(JudgeQues.this);
                    t13.setTextColor(Color.BLACK);
                    t23.setTextColor(Color.GRAY);
                    t13.setTextSize(20);
                    t23.setTextSize(25);
                    q="GENDER";
                    t23.setLayoutParams(layoutParams);

                    a=documentSnapshot.getString("GENDER");
                    t13.setText(q);
                    t23.setBackground(gd);
                    t23.setText(a);
                    ll.addView(t13);
                    ll.addView(t23);

                    TextView t14=new TextView(JudgeQues.this);
                    TextView t24=new TextView(JudgeQues.this);
                    t14.setTextColor(Color.BLACK);
                    t24.setTextColor(Color.GRAY);
                    t14.setTextSize(20);
                    t24.setTextSize(25);
                    q="EMAIL";
                    t24.setLayoutParams(layoutParams);

                    a=documentSnapshot.getString("EMAIL");
                    t14.setText(q);
                    t24.setBackground(gd);
                    t24.setText(a);
                    ll.addView(t14);
                    ll.addView(t24);
                    TextView t15=new TextView(JudgeQues.this);
                    TextView t25=new TextView(JudgeQues.this);
                    t15.setTextColor(Color.BLACK);
                    t25.setTextColor(Color.GRAY);
                    t15.setTextSize(20);
                    t25.setTextSize(25);
                    q="PHONE NUMBER";
                    t25.setLayoutParams(layoutParams);

                    a=documentSnapshot.getString("PHONE NUMBER");
                    t15.setText(q);
                    t25.setBackground(gd);
                    t25.setText(a);
                    ll.addView(t15);
                    ll.addView(t25);
                    String uid=documentSnapshot.getString("uid");
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                    final StorageReference filepath = storageReference.child(uid + "." + "pdf");

                    TextView t=new TextView(JudgeQues.this);
                    SpannableString content = new SpannableString("Click here to open submitted file.");
                    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                    t.setText(content);
                    t.setTextColor(Color.BLUE);
                    t.setTextSize(25);
                    t.setLayoutParams(layoutParams);
                    ll.addView(t);
                    t.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View v) {

                            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(uid+".pdf");
                            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Got the download URL for 'users/me/profile.png'

                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                    startActivity(intent);
                                }
                            });



                        }
                    });
                    for(int i=1;i<=total;i++)
                    {
                        TextView t1=new TextView(JudgeQues.this);
                        TextView t2=new TextView(JudgeQues.this);
                        t1.setTextColor(Color.BLACK);
                        t2.setTextColor(Color.GRAY);
                        t1.setTextSize(20);
                        t2.setTextSize(25);
                        q=i+". "+documentSnapshot.getString("q"+i);

                        t2.setLayoutParams(layoutParams);

                        a=documentSnapshot.getString("a"+i);
                        t1.setText(q);
                        t2.setBackground(gd);
                        t2.setText(a);
                        ll.addView(t1);
                        ll.addView(t2);
                    }
                    pr.setVisibility(View.GONE);
                }
                else
                {
                    Toast.makeText(JudgeQues.this,task.getException().getMessage(),Toast.LENGTH_LONG);

                }

            }
        });

    }
    public void reject(View v)
    {
        Map<String,Object> user=new HashMap<>();
        user.put("progress",4);
        documentReference3.set(user, SetOptions.merge());
        Toast.makeText(JudgeQues.this,"CANDIDATE REJECTED",Toast.LENGTH_LONG).show();
        Intent i=new Intent(JudgeQues.this,SubQues.class);
        finish();

    }public void interview(View v)
    {
        Map<String,Object> user=new HashMap<>();
        user.put("progress",3);
        documentReference3.set(user, SetOptions.merge());
        Toast.makeText(JudgeQues.this,"CANDIDATE SELECTED FOR INTERVIEW",Toast.LENGTH_LONG).show();
        Intent i=new Intent(JudgeQues.this,SubQues.class);
        finish();

    }public void undereval(View v)
    {
        Map<String,Object> user=new HashMap<>();
        user.put("progress",2);
        documentReference3.set(user, SetOptions.merge());
        Toast.makeText(JudgeQues.this,"CANDIDATE SET UNDER EVALUATION",Toast.LENGTH_LONG).show();
        Intent i=new Intent(JudgeQues.this,SubQues.class);
        finish();

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        Log.d("hey0","hey");
        return true;
    }
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle presses on the action bar items

        fAuth= FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        switch (item.getItemId()) {

            case R.id.logout:

                AlertDialog.Builder l=new AlertDialog.Builder(JudgeQues.this);
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