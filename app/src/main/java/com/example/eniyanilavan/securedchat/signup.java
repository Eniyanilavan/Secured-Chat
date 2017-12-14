package com.example.eniyanilavan.securedchat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class signup extends AppCompatActivity {

    Button b1;
    EditText e1,e2,e3;
    DatabaseReference ref,users;
    FirebaseAuth mAuth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();
        b1 = (Button) findViewById(R.id.creat);
        e1 = (EditText) findViewById(R.id.mail);
        e2 = (EditText) findViewById(R.id.pass);
        e3 = (EditText) findViewById(R.id.disp);
        final ArrayList<String>userlist=new ArrayList<>();
        ref = FirebaseDatabase.getInstance().getReference().child("users");
        users = FirebaseDatabase.getInstance().getReference();
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                userlist.add(dataSnapshot.getValue().toString().toLowerCase());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String mail = e1.getText().toString();
                final String pass = e2.getText().toString();
                final String name = e3.getText().toString();
                if (!(mail.isEmpty()||pass.isEmpty()||name.isEmpty()))
                {
                    if (userlist.contains(name.toLowerCase()))
                    {
                        Toast.makeText(getApplicationContext(),"Username Already Exists",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        mAuth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Successfuly Created The Account", Toast.LENGTH_LONG).show();
                                    user = mAuth.getCurrentUser();
                                    UserProfileChangeRequest update = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                                    user.updateProfile(update).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                ref.push().setValue(mAuth.getCurrentUser().getDisplayName());
                                                users.child(mAuth.getCurrentUser().getDisplayName()).child("flag").setValue(0);
                                                mAuth.signOut();
                                            }
                                        }
                                    });
                                    mAuth.signOut();
                                    Intent c = new Intent(getApplicationContext(), login.class);
                                    Bundle d = new Bundle();
                                    d.putInt("key", 2);
                                    c.putExtras(d);
                                    startActivity(c);
                                } else {
                                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Entert the details",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
