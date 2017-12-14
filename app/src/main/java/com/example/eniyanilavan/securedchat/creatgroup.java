package com.example.eniyanilavan.securedchat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class creatgroup extends AppCompatActivity {

    DatabaseReference musers,mgroup;
    ProgressBar progressBar;
    RelativeLayout relativeLayout;
    ImageButton doneb;
    TextView textView;
    EditText gname;
    FirebaseAuth mAuth;
    int i=0,j=10,id=1,id1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creatgroup);
        relativeLayout = (RelativeLayout) findViewById(R.id.relative);
        musers = FirebaseDatabase.getInstance().getReference().child("users");
        mgroup = FirebaseDatabase.getInstance().getReference();
        progressBar = (ProgressBar) findViewById(R.id.pro);
        textView = (TextView) findViewById(R.id.textView);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        gname = (EditText)findViewById(R.id.group);
        doneb = (ImageButton) findViewById(R.id.button);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(150,150);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.setMargins(0,0,16,30);
        doneb.setLayoutParams(params);
        final ArrayList<CheckBox> users = new ArrayList<>();
        final ArrayList<Integer> index = new ArrayList<Integer>();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        musers.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(!(dataSnapshot.getValue().toString().equals(user.getDisplayName()))) {
                    String value = dataSnapshot.getValue().toString();
                    CheckBox username=new CheckBox(getApplicationContext());
                    username.setText(value);
                    users.add(username);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(10,j,0,0);
                    params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    users.get(users.size()-1).setTextSize(14);
                    users.get(users.size()-1).setId(id);
                    users.get(users.size()-1).setTextColor( -16777216);
                    users.get(users.size()-1).setHighlightColor(-16777216);
                    relativeLayout.addView(users.get(users.size()-1),params);
                    progressBar.setVisibility(View.INVISIBLE);
                    j+=120;
                    id++;
                }
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
        doneb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (id1=0;id1<users.size();id1++)
                {
                    CheckBox temp = (CheckBox) relativeLayout.findViewById((users.get(id1).getId()));
                    if(temp.isChecked())
                    {
                        if (gname.getText().toString().replaceAll(" ","").isEmpty())
                        {
                            Toast.makeText(getApplicationContext(),"Enter the Group name",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            String nam = temp.getText().toString();
                            mgroup.child(gname.getText().toString()).child("users").push().setValue(nam);
                            mgroup.child(nam).child("flag").child("group").child(gname.getText().toString()).setValue(0);
                            mgroup.child(user.getDisplayName()).child("flag").child("group").child(gname.getText().toString()).setValue(0);
                            mgroup.child(nam).child(gname.getText().toString()).setValue(1);
                            mgroup.child(user.getDisplayName()).child(gname.getText().toString()).setValue(1);
                            finish();
                        }
                    }
                }
                mgroup.child(gname.getText().toString()).child("users").push().setValue(user.getDisplayName());
            }
        });
    }
}
