package com.example.girlpowertalk;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    static int n,rn;ProgressBar p;
    static String uid,email,ph;
    public static String name="";
    List<EditText> allEds;
    Button s;
    List<String> ques;
    public static boolean flag=false;
    List<String> det;
    DocumentReference documentReference2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_userpage);
        p=findViewById(R.id.progressBar5);
        s=findViewById(R.id.button3);
        allEds= new ArrayList<EditText>();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.logo1_round);
        ques=new ArrayList<String>();
        det=new ArrayList<String>();
        fStore=FirebaseFirestore.getInstance();
        p.setVisibility(View.VISIBLE);
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
        p.setVisibility(View.VISIBLE);
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

// Finally, apply the gradient drawable to the edit text background
            e.setBackground(gd);
            // e.setHeight();
            e.setGravity(0);
            allEds.add(e);
            e.setId(i);
            e.setHint("Write your answer here");

            ll.addView(e);

        }
        s.setVisibility(View.VISIBLE);
        p.setVisibility(View.GONE);
    }
   /* public int getWifiLevel()
    {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        int linkSpeed = wifiManager.getConnectionInfo().getRssi();
        int level = WifiManager.calculateSignalLevel(linkSpeed, 5);
        return level;
    }*/
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    public void submit(View view)
    {

        AlertDialog.Builder passres=new AlertDialog.Builder(view.getContext());
        passres.setTitle("Are you sure?");
        passres.setMessage("Once submitted you cannot edit it.");
        passres.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                boolean t=isOnline();
                //  Toast.makeText(userpage.this,""+t,Toast.LENGTH_LONG).show();
                if(false==t)
                {
                    Toast.makeText(userpage.this,"NETWORK FAILURE",Toast.LENGTH_LONG).show();
                    return;
                }
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
                                    DocumentReference documentReference3=fStore.collection("questions").document("Responses").collection("Answers").document("r"+rn);
                                    Map<String,Object> user=new HashMap<>();

                                    for(int i=0; i < allEds.size(); i++){
                                        user.put("a"+(i+1),strings[i]);
                                        user.put("q"+(i+1),ques.get(i));
                                    }

                                    user.put("progress",1);
                                    user.put("total",allEds.size());

                                    documentReference3.set(user,SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                            flag=true;
                            startActivity(new Intent(getApplicationContext(),Progress.class));
                            finish();
                        }
                        else
                        {
                            Toast.makeText(userpage.this,task.getException().getMessage(),Toast.LENGTH_LONG);

                        }

                    }
                });



            }
        });
        passres.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //close dialog
            }
        });
        passres.create().show();
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


}

