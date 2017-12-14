package com.example.eniyanilavan.securedchat;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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

public class groupchat extends AppCompatActivity {

    TextView sent;
    EditText edit;
    ImageButton button;
    DatabaseReference reference;
    String groupname,sender;
    ScrollView scrollView;
    RelativeLayout relativeLayout;
    int i;
    final ArrayList<String> users = new ArrayList<>();
    int j=850;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupchat);
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
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
                edit.setEnabled(true);
                edit.setActivated(true);
            }
        });
        Toast.makeText(getApplicationContext(),"group",Toast.LENGTH_LONG).show();
        if (trans!=null)
        {
            groupname = trans.getString("receiver");
            sender = trans.getString("sender");
        }
        reference.child(groupname).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final String key = dataSnapshot.getKey();
                manager.cancelAll();
                    reference.child(groupname).child(key).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s)
                        {
                            final String temp = dataSnapshot.getKey();
                            reference.child(groupname).child(key).child(temp).addChildEventListener(new ChildEventListener()
                            {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    ArrayList<TextView> views = new ArrayList<>();
                                    String message = dataSnapshot.getValue().toString();
                                    sent = new TextView(getApplicationContext());
                                    views.add(sent);
                                    views.get(views.size() - 1).setTextSize(20);
                                    views.get(views.size() - 1).setTextColor(getResources().getColor(R.color.colorAccent));
                                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    if (temp.equals(sender))
                                    {
                                        views.get(views.size() - 1).setText(message);
                                        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                                        params.setMargins(0, j, 10, 0);
                                    }
                                    else
                                    {
                                        views.get(views.size() - 1).setText(temp+":\n"+message);
                                        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                        params.setMargins(10, j, 0, 0);
                                    }
                                    relativeLayout.addView(views.get(views.size() - 1), params);
                                    scrollView.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                                        }
                                    });
                                    j += 120;
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
                if (!message.isEmpty()) {
                    reference.child(groupname).push().child(sender).child("sent").setValue(message);
                    edit.setText("");
                    edit.setEnabled(true);
                    edit.setActivated(true);
                    reference.child(groupname).child("users").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            reference.child(dataSnapshot.getValue().toString()).child("flag").child("group").child(groupname).push().setValue(sender);
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
                }
            }
        });
    }
}
