package com.example.courseproject.SharedPerferences;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

public class MySharePreferences {

    public static final String MY_SHARED_PREFERENCES = "MY_SHARED_PREFERENCES";
    private Context context;
    public MySharePreferences(Context context) {
        this.context = context;
    }
    public void putBooleanValue( String key, boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                MY_SHARED_PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
    public boolean getBooleanValue(String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                MY_SHARED_PREFERENCES,Context.MODE_PRIVATE);

        return sharedPreferences.getBoolean(key,false);
    }
    public void putStringValue(String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                MY_SHARED_PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
    public String getStringValue( String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                MY_SHARED_PREFERENCES,Context.MODE_PRIVATE);

        return sharedPreferences.getString(key,null);
    }
    public void putIntValue(String key, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                MY_SHARED_PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }
    public int getIntValue( String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                MY_SHARED_PREFERENCES,Context.MODE_PRIVATE);

        return sharedPreferences.getInt(key,1);
    }

}
