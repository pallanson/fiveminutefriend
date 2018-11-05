package com.p.fiveminutefriend.Model;

import java.util.ArrayList;
import java.util.List;

public class Match {
    public int ageMin = 0;
    public int ageMax = 100;
    public int gender = 7;
    public List<String> languages;
    public int myAge;
    public long canMatch = 0;
    public int myGender;
    public String myLanguage;
    public String myToken;

    public Match(){
        languages = new ArrayList<String>(){{ add("English"); }};
    }

    public Match(int ageMin, int ageMax, int gender, List<String> languages, int myAge, int myGender, String myLanguage, String myToken) {
        this.ageMin = ageMin;
        this.ageMax = ageMax;
        this.gender = gender;
        this.languages = languages;
        this.myAge = myAge;
        this.myGender = myGender;
        this.myLanguage = myLanguage;
        this.myToken = myToken;
    }

    public Match(int ageMin, int ageMax, int gender, List<String> languages, int myAge, int myGender, String myLanguage, String myToken, long canMatch) {
        this.ageMin = ageMin;
        this.ageMax = ageMax;
        this.gender = gender;
        this.languages = languages;
        this.myAge = myAge;
        this.myGender = myGender;
        this.myLanguage = myLanguage;
        this.myToken = myToken;
        this.canMatch = canMatch;
    }

    public int getAgeMin() {
        return ageMin;
    }

    public void setAgeMin(int ageMin) {
        this.ageMin = ageMin;
    }

    public int getAgeMax() {
        return ageMax;
    }

    public void setAgeMax(int ageMax) {
        this.ageMax = ageMax;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public int getMyAge() {
        return myAge;
    }

    public void setMyAge(int myAge) {
        this.myAge = myAge;
    }

    public int getMyGender() {
        return myGender;
    }

    public void setMyGender(int myGender) {
        this.myGender = myGender;
    }

    public String getMyLanguage() {
        return myLanguage;
    }

    public void setMyLanguage(String myLanguage) {
        this.myLanguage = myLanguage;
    }

    public String getMyToken() {
        return myToken;
    }

    public void setMyToken(String myToken) {
        this.myToken = myToken;
    }

    public long getCanMatch() { return canMatch; }

    public void setCanMatch(long canMatch) { this.canMatch = canMatch; }
}
