package com.example.girlpowertalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

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
    private static  int n,p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        t=(TextView)findViewById(R.id.textView15);
        fAuth= FirebaseAuth.getInstance();
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#cc0c0c"));
        actionBar.setBackgroundDrawable(colorDrawable);

        fStore= FirebaseFirestore.getInstance();
        uid=fAuth.getCurrentUser().getUid();
        final DocumentReference documentReference=fStore.collection("users").document(uid);
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
    }

}
