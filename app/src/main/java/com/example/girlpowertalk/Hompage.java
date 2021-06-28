package com.example.girlpowertalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Hompage extends AppCompatActivity {
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String uid;
    private int n,p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hompage);
        Button button=(Button)findViewById(R.id.applynow);
        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        uid=fAuth.getCurrentUser().getUid();
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#cc0c0c"));
        actionBar.setBackgroundDrawable(colorDrawable);

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
                        t1.setText("This is the page for login of admins\n\n");

                    }
                    else {
                        p=(documentSnapshot.getLong("response no").intValue());
                        TextView t=findViewById(R.id.t);
                        t.setText("ABOUT US");
                        TextView t1=findViewById(R.id.textView10);
                        t1.setText("Girl Power Talk strives to inspire girls with persistence, empathy, and confidence. We empower young Women, Men & Non-Binary with merit-based opportunities to grow and achieve their full potential. Our mission, in collaboration with our sister company Blue Ocean Global Technology, is to develop girls in India to become global leaders. We are relentlessly committed to education, gender equality, and integrating the strengths of specially abled communities. We celebrate the diverse talents of each individual. \n Through our nurturing culture of learning and mentorship, we instill young people with exceptional soft-skills, technical knowledge, and purpose in life. We provide a platform to share the voices and stories of girls and women across India. #GirlPowerTalk \n “One girl empowers another. Let’s change lives together: one girl, one woman and one human being at a time.” \n \n -Rachita Sharma \n Co-Founder, Girl Power Talk \n Chief Marketing Officer, Blue Ocean Global Technology \n");

                        if (p == 0)
                            button.setText("APPLY NOW");
                        else
                            button.setText("SEE PROGRESS");
                    }

                }
                else
                {
                    Toast.makeText(Hompage.this,task.getException().getMessage(),Toast.LENGTH_LONG);
                    button.setText("APPLY NOW");
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
}