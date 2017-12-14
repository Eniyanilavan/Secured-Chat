package com.example.eniyanilavan.securedchat;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.*;

public class chat extends AppCompatActivity {

    TextView sent;
    EditText edit;
    ImageButton button;
    DatabaseReference reference;
    String receiver,sender;
    ScrollView scrollView;
    RelativeLayout relativeLayout;
    int j=850;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        edit = (EditText)findViewById(R.id.editText);
        scrollView = (ScrollView)findViewById(R.id.scroll);
        relativeLayout = (RelativeLayout)findViewById(R.id.id);
        button = (ImageButton) findViewById(R.id.imageButton);
        final ArrayList<TextView> viewr = new ArrayList<>();
        Bundle trans = new Bundle();
        reference = FirebaseDatabase.getInstance().getReference();
        trans = getIntent().getExtras();
        final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                }
            });
        Toast.makeText(getApplicationContext(),"Chat",Toast.LENGTH_LONG).show();
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
                edit.setActivated(true);
                edit.setEnabled(true);
            }
        });
        if (trans!=null)
        {
            receiver = trans.getString("receiver");
            sender = trans.getString("sender");
        }
        reference.child(sender).child(receiver).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ArrayList<TextView> views = new ArrayList<>();
                manager.cancelAll();
                String message[] = dataSnapshot.getValue().toString().split("=");
                sent = new TextView(getApplicationContext());
                views.add(sent);
                views.get(views.size()-1).setText(message[1].replace("}",""));
                views.get(views.size()-1).setTextSize(20);
                views.get(views.size()-1).setTextColor(getResources().getColor(R.color.colorAccent));
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                if(message[0].equals("{sent"))
                {
                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    params.setMargins(0,j,10,0);
                }
                else
                {
                    params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    params.setMargins(10,j,0,0);
                }

                relativeLayout.addView(views.get(views.size()-1),params);
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
                j+=100;
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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = edit.getText().toString();
                if (!message.isEmpty())
                {
                    reference.child(receiver).child(sender).push().child("received").setValue(message);
                    reference.child(sender).child(receiver).push().child("sent").setValue(message);
                    reference.child(receiver).child("flag").push().setValue(sender);
                    edit.setText("");
                }
            }
        });

    }
}
