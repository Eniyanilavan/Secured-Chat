package com.example.eniyanilavan.securedchat;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by Eniyanilavan on 08-12-2017.
 */

public class chatorgroup {
    public static boolean chat(ArrayList<String>users,String getname)
    {
        if (users.contains(getname))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
