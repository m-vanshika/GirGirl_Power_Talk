package com.example.girlpowertalk;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class userpage extends AppCompatActivity {

    FirebaseFirestore fStore;
    FirebaseAuth firebaseAuth,fAuth;
    static int n,rn;
    static String uid,email,ph;
    public static String name="";
    List<EditText> allEds;
    List<String> ques;
    List<String> det;
    DocumentReference documentReference2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userpage);
        allEds= new ArrayList<EditText>();
        ques=new ArrayList<String>();
        det=new ArrayList<String>();
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
                    Toast.makeText(userpage.this,task.getException().getMessage(),Toast.LENGTH_LONG);

                }

            }
        });


    }

    public void display(int n,DocumentSnapshot documentSnapshot)
    {
        LinearLayout ll=findViewById(R.id.ll);
        if(n==0)
        {
            TextView t=new TextView(userpage.this);
            t.setText("No questions found");
            t.setId(0);
            ll.addView(t);
            return;
        }
        for(int i=1;i<=n;i++)
        {
            String s="q"+i;
            String q=(documentSnapshot.getString(s));
            ques.add(q);
            TextView t=new TextView(userpage.this);
            t.setText(i+".  "+q);
            t.setTextColor(Color.BLACK);
            //  t.setTextScaleX(2);
            t.setTextSize(1,20);
            ll.addView(t);
            EditText e=new EditText((userpage.this));
            GradientDrawable gd = new GradientDrawable();
// Set the gradient drawable background to transparent
            gd.setColor(Color.parseColor("#f8f9fa"));
            gd.setStroke(2,Color.BLACK);

// Finally, apply the gradient drawable to the edit text background
            e.setBackground(gd);
            // e.setHeight();
            e.setGravity(0);
            allEds.add(e);
            e.setId(i);

            ll.addView(e);

        }
    }
    public void submit(View view)
    {
        String strings[] = new String[allEds.size()];

        for(int i=0; i < allEds.size(); i++){
            strings[i] = allEds.get(i).getText().toString();
        }
        fStore=FirebaseFirestore.getInstance();
        //got response no
        final DocumentReference documentReference=fStore.collection("questions").document("Responses");
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot=task.getResult();
                    assert documentSnapshot != null;
                    rn =(documentSnapshot.getLong("number").intValue());
                    // Log.v("number", String.valueOf(rn));
                }
                else
                {
                    Toast.makeText(userpage.this,task.getException().getMessage(),Toast.LENGTH_LONG);

                }

            }
        });
        rn++;
        Map<String, Object> data = new HashMap<>();
        data.put("number", rn);
        documentReference.set(data, SetOptions.merge());
        //added response number to user
        firebaseAuth=FirebaseAuth.getInstance();
        uid= Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        documentReference2=fStore.collection("users").document(uid);
        readData(new FirestoreCallback() {
            @Override
            public void onCallback(List<String> list) {
                name=list.get(0);
                email=list.get(1);
                ph=list.get(2);
                DocumentReference documentReference3=fStore.collection("questions").document("Responses").collection("Answers").document("r"+rn);
                Map<String,Object> user=new HashMap<>();
                user.put("NAME",name);
                user.put("EMAIL",email);
                user.put("PHONE NUMBER",ph);
                for(int i=0; i < allEds.size(); i++){
                    user.put("a"+(i+1),strings[i]);
                    user.put("q"+(i+1),ques.get(i));
                }

                user.put("progress",1);
                user.put("total",allEds.size());

                documentReference3.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(userpage.this,"SUBMISSION DONE",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        Map<String, Object> data2 = new HashMap<>();
        data2.put("response no", rn);
        documentReference2.set(data2, SetOptions.merge());
        //put data in new collection response

        startActivity(new Intent(getApplicationContext(),Progress.class));
        finish();



    }
    private void readData(FirestoreCallback firestoreCallback)
    {
        documentReference2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot=task.getResult();
                assert documentSnapshot != null;
                det.add(documentSnapshot.getString("NAME"));
                det.add(documentSnapshot.getString("PHONE NUMBER"));
                det.add(documentSnapshot.getString("EMAIL"));
                firestoreCallback.onCallback(det);
            }
        });
    }

    private interface FirestoreCallback{
        void onCallback(List<String> list);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
       // Log.d("hey0","hey");
        return true;
    }
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle presses on the action bar items

        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        switch (item.getItemId()) {

            case R.id.logout:

                AlertDialog.Builder l=new AlertDialog.Builder(userpage.this);
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

