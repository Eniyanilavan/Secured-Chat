   package com.example.eniyanilavan.securedchat;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.eniyanilavan.securedchat.chatorgroup;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

   public class MainActivity extends AppCompatActivity {
    Button b6,b7,message,group,logp;
    DatabaseReference m,flag;
    FirebaseAuth mAuth;
    ListView mess;
    int re;
    chatorgroup object = new chatorgroup();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        message = (Button)findViewById(R.id.mess);
        logp = (Button)findViewById(R.id.button);
        group = (Button)findViewById(R.id.group);
        b6 = (Button) findViewById(R.id.add);
        b7 = (Button) findViewById(R.id.log);
        mess = (ListView) findViewById(R.id.listc);
        final ArrayList<String>me = new ArrayList<String>();
        final ArrayAdapter<String> mad = new ArrayAdapter<String>(getApplicationContext(), R.layout.list,me);
        mess.setAdapter(mad);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        m = FirebaseDatabase.getInstance().getReference().child(user.getDisplayName());
        Toast.makeText(getApplicationContext(), "Welcome "+user.getDisplayName(), Toast.LENGTH_SHORT).show();
        Bundle b = getIntent().getExtras();
        flag = FirebaseDatabase.getInstance().getReference().child(user.getDisplayName()).child("flag");
        if(b!=null)
        {
            re = b.getInt("re");
        }
        final NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.drawable.checkmark).setContentTitle("There is a new message").setAutoCancel(true);
        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(contentIntent);
        final NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
        final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notification.setStyle(style);
        logp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(b7.getVisibility()==view.VISIBLE) {
                    b7.setVisibility(message.INVISIBLE);
                    logp.setRotation(0);
                }
                else
                {
                    b7.setVisibility(message.VISIBLE);
                    logp.setRotation(90);
                }
            }
        });
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(message.getVisibility()==view.VISIBLE) {
                    message.setVisibility(message.INVISIBLE);
                    group.setVisibility(message.INVISIBLE);
                    b6.setRotation(90);
                }
                else
                {
                    message.setVisibility(message.VISIBLE);
                    group.setVisibility(message.VISIBLE);
                    b6.setRotation(45);
                }
            }
        });
        group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b6.setRotation(90);
                message.setVisibility(message.INVISIBLE);
                group.setVisibility(message.INVISIBLE);
                startActivity(new Intent(getApplicationContext(),creatgroup.class));
            }
        });
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b6.setRotation(90);
                message.setVisibility(message.INVISIBLE);
                group.setVisibility(message.INVISIBLE);
                startActivity(new Intent(getApplicationContext(),message.class));

            }
        });
        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(),login.class));
            }
        });
        flag.setValue(0);
        DatabaseReference reference;
        final ArrayList<String> users = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference().child("users");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                users.add(dataSnapshot.getValue().toString());
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
        m.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String chat = dataSnapshot.getKey();
                if (!chat.equals("flag"))
                {
                    me.add(chat);
                    mad.notifyDataSetChanged();
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
        mess.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String receiver = me.get(i);
                boolean a = object.chat(users,receiver);
                if(a)
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("receiver",receiver);
                    bundle.putString("sender",user.getDisplayName());
                    Intent c = new Intent(getApplicationContext(),chat.class);
                    c.putExtras(bundle);
                    startActivity(c);
                }
                else
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("receiver",receiver);
                    bundle.putString("sender",user.getDisplayName());
                    Intent c = new Intent(getApplicationContext(),groupchat.class);
                    c.putExtras(bundle);
                    startActivity(c);
                }
            }
        });
    }
       @Override
       public void onBackPressed() {
           super.onBackPressed();
           if (re!=1)
           {
               mAuth.signOut();
               Toast.makeText(getApplicationContext(), "Signed Out", Toast.LENGTH_LONG).show();
           }
           else
           {
               this.finishAffinity();
               System.exit(0);
           }
       }
   }
