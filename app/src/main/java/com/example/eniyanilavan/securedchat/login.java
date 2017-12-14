package com.example.eniyanilavan.securedchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class login extends AppCompatActivity {

    Button bt,s;
    EditText m,p;
    FirebaseAuth mAuth;
    CheckBox check;
    String email = null;
    String pass  = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bt = (Button) findViewById(R.id.sign);
        s = (Button) findViewById(R.id.signup);
        m = (EditText) findViewById(R.id.email);
        p = (EditText) findViewById(R.id.password);
        check = (CheckBox) findViewById(R.id.checkBox);
        mAuth = FirebaseAuth.getInstance();
        int kay=0;

        Bundle bundle;
        bundle = getIntent().getExtras();
        if (bundle!=null)
        {
            kay = bundle.getInt("key");
        }
        if(kay!=2)
        {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                Intent c = new Intent(getApplicationContext(), MainActivity.class);
                Bundle b = new Bundle();
                b.putInt("re", 1);
                c.putExtras(b);
                startActivity(c);
        }
        }
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = m.getText().toString();
                pass = p.getText().toString();
                if (!(email.isEmpty() || pass.isEmpty()))
                {
                    mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent c = new Intent(getApplicationContext(), MainActivity.class);
                                Bundle b = new Bundle();
                                if (check.isChecked()) {
                                    b.putInt("re", 1);
                                }
                                c.putExtras(b);
                                startActivity(c);
                            } else {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Enter the details",Toast.LENGTH_LONG).show();
                }

            }
        });
        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),signup.class));
            }
        });
        startService(new Intent(getApplicationContext(),MyService.class));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finishAffinity();
        System.exit(1);
    }
}
