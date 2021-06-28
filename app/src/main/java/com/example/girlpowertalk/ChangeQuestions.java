package com.example.girlpowertalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ChangeQuestions extends AppCompatActivity {
    FirebaseFirestore fStore;
    int n;
    TextView b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_questions);
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#cc0c0c"));
        actionBar.setBackgroundDrawable(colorDrawable);

    }
    public void display(int n,DocumentSnapshot documentSnapshot)
    {
        LinearLayout ll=findViewById(R.id.ll);
        ll.removeAllViews();
        if(n==0)
        {
            TextView t=new TextView(ChangeQuestions.this);
            t.setText("No questions found");
            t.setId(0);
            ll.addView(t);
            return;
        }
        for(int i=1;i<=n;i++)
        {
            String s="q"+i;
            String q=(documentSnapshot.getString(s));
            TextView t=new TextView(ChangeQuestions.this);
            t.setText(i+".  "+q);
            t.setTextColor(Color.BLACK);
            t.setId(i);
            //  t.setTextScaleX(2);
            t.setTextSize(1,20);
            int finalI = i;
            t.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editq(v, v.getId());
                    //Toast.makeText(ChangeQuestions.this,"no"+v.getId(),Toast.LENGTH_LONG).show();
                }
            });
            ll.addView(t);

        }
    }

    public void newq(View v)
    {
        final EditText nq=new EditText(v.getContext());
        AlertDialog.Builder passres=new AlertDialog.Builder(v.getContext());
        passres.setTitle("ENTER NEW QUESTION");
        passres.setView(nq);
        passres.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String qus=nq.getText().toString();
                fStore=FirebaseFirestore.getInstance();
                final DocumentReference documentReference=fStore.collection("questions").document("Details");
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot=task.getResult();
                            assert documentSnapshot != null;
                            n=(documentSnapshot.getLong("number").intValue());
                            n++;
                            Map<String,Object> ques=new HashMap<>();
                            for(int i=1;i<=n;i++)
                            {
                                String s="q"+i;
                                String q=(documentSnapshot.getString(s));
                                ques.put(s,q);

                            }
                            ques.put("q"+n,qus);
                            ques.put("number",n);
                            documentReference.set(ques).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(ChangeQuestions.this,"QUESTION ADDED",Toast.LENGTH_LONG).show();
                                }
                            });
                            onResume();

                        }
                        else
                        {
                            Toast.makeText(ChangeQuestions.this,task.getException().getMessage(),Toast.LENGTH_LONG);

                        }
                    }
                });
            }
        });
        passres.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //close dialog
            }
        });
        passres.create().show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        fStore=FirebaseFirestore.getInstance();
        final DocumentReference documentReference=fStore.collection("questions").document("Details");
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot=task.getResult();
                    assert documentSnapshot != null;
                    n=(documentSnapshot.getLong("number").intValue());
                    display(n,documentSnapshot);


                }
                else
                {
                    Toast.makeText(ChangeQuestions.this,task.getException().getMessage(),Toast.LENGTH_LONG);

                }
            }
        });
    }

    public void getq(EditText w,int j)
    {
        fStore=FirebaseFirestore.getInstance();
        final DocumentReference documentReference=fStore.collection("questions").document("Details");
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot=task.getResult();
                    assert documentSnapshot != null;
                    String t=(documentSnapshot.getString("q"+j));
                    w.setText(t);


                }
                else
                {

                }
            }
        });
    }
    public void editq(View v,int k)
    {

        final EditText nn=new EditText(v.getContext());
        if(k>0&&k<=n)
        {
            getq(nn,k);
        }
        AlertDialog.Builder passres=new AlertDialog.Builder(v.getContext());
        passres.setTitle("EDIT QUESTION");
        passres.setMessage("QUESTION"+k);
        passres.setView(nn);
        passres.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int p=k;
                String qus=nn.getText().toString();
                fStore=FirebaseFirestore.getInstance();
                final DocumentReference documentReference=fStore.collection("questions").document("Details");
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot=task.getResult();
                            assert documentSnapshot != null;
                            n=(documentSnapshot.getLong("number").intValue());
                            Map<String,Object> ques=new HashMap<>();
                            for(int i=1;i<=n;i++)
                            {
                                String s="q"+i;
                                if(p==i)
                                {
                                    ques.put(s,qus);
                                    continue;
                                }
                                String q=(documentSnapshot.getString(s));
                                ques.put(s,q);

                            }
                            ques.put("number",n);
                            documentReference.set(ques).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(ChangeQuestions.this,"QUESTION CHANGED",Toast.LENGTH_LONG).show();
                                }
                            });
                            onResume();

                        }
                        else
                        {
                            Toast.makeText(ChangeQuestions.this,task.getException().getMessage(),Toast.LENGTH_LONG);

                        }
                    }
                });
            }
        });
        passres.setNeutralButton("DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int p=k;
                String qus=nn.getText().toString();
                fStore=FirebaseFirestore.getInstance();
                final DocumentReference documentReference=fStore.collection("questions").document("Details");
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot=task.getResult();
                            assert documentSnapshot != null;
                            n=(documentSnapshot.getLong("number").intValue());
                            Map<String,Object> ques=new HashMap<>();
                            int c=1;
                            for(int i=1;i<=n;i++)
                            {
                                if(p==i)
                                {
                                    continue;
                                }
                                String s="q"+c++;
                                String t="q"+i;
                                String q=(documentSnapshot.getString(t));
                                ques.put(s,q);

                            }
                            n--;
                            ques.put("number",n);
                            documentReference.set(ques).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(ChangeQuestions.this,"QUESTION DELETED",Toast.LENGTH_LONG).show();
                                }
                            });
                            onResume();

                        }
                        else
                        {
                            Toast.makeText(ChangeQuestions.this,task.getException().getMessage(),Toast.LENGTH_LONG);

                        }
                    }
                });
            }
        });
        passres.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //close dialog
            }
        });
        passres.create().show();

    }
}