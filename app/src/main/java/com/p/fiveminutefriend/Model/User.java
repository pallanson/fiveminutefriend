package com.p.fiveminutefriend.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Objects;

@Entity
public class User {

    public static final int GENDER_MALE = 0;
    public static final int GENDER_FEMALE = 1;
    public static final int GENDER_OTHER = 2;

    @PrimaryKey
    @NonNull
    private String uid;
    @ColumnInfo(name = "first_name")
    private String firstName;
    @ColumnInfo(name = "last_name")
    private String lastName;
    @ColumnInfo(name = "username")
    private String username;
    @ColumnInfo(name = "email")
    private String email;
    @ColumnInfo(name = "language")
    private String language;
    @ColumnInfo(name = "age")
    private int age;
    @ColumnInfo(name = "gender")
    private int gender;
    @ColumnInfo(name = "status")
    private String status;
    @ColumnInfo(name = "fcm_token")
    private String FCMToken;

    public User(@NonNull String uid,
                String firstName,
                String lastName,
                String username,
                String email,
                String language,
                int age,
                int gender) {

        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.language = language;
        this.age = age;
        this.gender = gender;
    }

    public User() {

    }

    //Temporary Constructor for list
    public User(String uid, String firstName, String lastName) {
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;

    }

    public User(String uid, String username, String firstName, String lastName)
    {
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getFCMToken() {
        return FCMToken;
    }

    public void setFCMToken(String FCMToken) {
        this.FCMToken = FCMToken;
    }

    public int hashCode() {
        return Objects.hash(this.uid, this.username, this.firstName, this.lastName);
    }

    @Override
    public boolean equals(Object obj){
        if (obj != null && User.class.isAssignableFrom(obj.getClass())) {
            final User other = (User) obj;
            return other.uid.equals(this.uid);
        }
        return false;
    }
}
