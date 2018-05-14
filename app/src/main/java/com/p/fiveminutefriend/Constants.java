package com.p.fiveminutefriend;

import com.google.firebase.auth.FirebaseAuth;

public class Constants {
    public static final String FIREBASE_USERS = "https://five-minute-friend.firebaseio.com/Users";
    public static final String FIREBASE_STORAGE_REFERENCE = "gs://five-minute-friend.appspot.com/" +
            FirebaseAuth.getInstance().getCurrentUser().getUid();
}
