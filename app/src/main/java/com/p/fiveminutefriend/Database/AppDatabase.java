package com.p.fiveminutefriend.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.p.fiveminutefriend.Constants;
import com.p.fiveminutefriend.Model.User;

/**
 * Created by charlie on 2018-06-02.
 */

@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static final String TAG = "AppDatabase";
    private static AppDatabase INSTANCE;

    public abstract UserDao userDao();

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "user-database").build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    public void syncUser() {
        FirebaseDatabase
                .getInstance()
                .getReferenceFromUrl(Constants.FIREBASE_USERS)
                .child(FirebaseAuth.getInstance()
                        .getCurrentUser()
                        .getUid())
                .addValueEventListener(new ValueEventListener() {
                                           @Override
                                           public void onDataChange(final DataSnapshot dataSnapshot) {
                                               if (dataSnapshot.getChildren() != null) {
                                                   Thread thread = new Thread(){
                                                       public void run(){
                                                           User user = dataSnapshot.getValue(User.class);
                                                           userDao().insert(user);
                                                       }
                                                   };
                                                   thread.start();
                                               }
                                           }

                                           @Override
                                           public void onCancelled(DatabaseError databaseError) {
                                               Log.e(TAG, databaseError.toString());
                                           }
                                       }
                );
    }
}
