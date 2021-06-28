package com.example.girlpowertalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class JudgeQues extends AppCompatActivity {
    FirebaseFirestore fStore;
    DocumentReference documentReference3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_judge_ques);
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#cc0c0c"));
        actionBar.setBackgroundDrawable(colorDrawable);
        String t= getIntent().getStringExtra("CANDIDATE NUMBER");
        fStore=FirebaseFirestore.getInstance();
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
                    for(int i=1;i<=total;i++)
                    {
                        String q,a;
                        TextView t1=new TextView(JudgeQues.this);
                        TextView t2=new TextView(JudgeQues.this);
                        t1.setTextColor(Color.BLACK);
                        t2.setTextColor(Color.GRAY);
                        t1.setTextSize(20);
                        t2.setTextSize(25);
                        q=i+". "+documentSnapshot.getString("q"+i);
                        a="\t\t"+documentSnapshot.getString("a"+i)+"\n";
                        t1.setText(q);
                        t2.setText(a);
                        ll.addView(t1);
                        ll.addView(t2);
                    }
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
}