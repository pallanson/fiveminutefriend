package com.p.fiveminutefriend.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.p.fiveminutefriend.Model.User;

/**
 * Created by charlie on 2018-06-02.
 */

@Dao
public interface UserDao {

    @Query("SELECT * FROM user WHERE uid LIKE :uid LIMIT 1")
    User findByUid(String uid);

    @Query("SELECT fcm_token FROM user WHERE uid LIKE :uid LIMIT 1")
    String getFCMToken(String uid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Query("DELETE FROM user")
    void clearUsers();

}
