package com.example.courseproject.SharedPerferences;

import android.content.Context;

public class DataLocalManager {
    private static DataLocalManager instance;
    private MySharePreferences mySharePreferences;

    public  static void  init(Context context){
        instance = new DataLocalManager();
        instance.mySharePreferences = new MySharePreferences(context);
    };

    public static DataLocalManager getInstance(){
        if(instance == null){
            instance = new DataLocalManager();
        }
        return instance;
    }
    public  static void setToken(String token){
        DataLocalManager.getInstance().mySharePreferences.putStringValue("TOKEN",token);
        }
    public static String getToken(){
        return DataLocalManager.getInstance().mySharePreferences.getStringValue("TOKEN");
    }
    public  static void setEmail(String email){
        DataLocalManager.getInstance().mySharePreferences.putStringValue("Email",email);
    }
    public static String getEmail(){
        return DataLocalManager.getInstance().mySharePreferences.getStringValue("Email");
    }
    public  static void userId(int userId){
        DataLocalManager.getInstance().mySharePreferences.putIntValue("UserId",userId);
    }
    public static int getUserId(){
        return DataLocalManager.getInstance().mySharePreferences.getIntValue("UserId");
    }
    public  static void setImage(String image){
        DataLocalManager.getInstance().mySharePreferences.putStringValue("Image",image);
    }
    public static String getImage(){
        return DataLocalManager.getInstance().mySharePreferences.getStringValue("Image");
    }
    public  static void setUserName(String userName){
        DataLocalManager.getInstance().mySharePreferences.putStringValue("UserName",userName);
    }
    public static String getUserName(){
        return DataLocalManager.getInstance().mySharePreferences.getStringValue("UserName");
    }

    public  static void setFullName(String fullName){
        DataLocalManager.getInstance().mySharePreferences.putStringValue("FullName",fullName);
    }
    public static String getFullName(){
        return DataLocalManager.getInstance().mySharePreferences.getStringValue("FullName");
    }
    public  static void setUserLoggedIn(Boolean isLoggedIn){
        DataLocalManager.getInstance().mySharePreferences.putBooleanValue("IsLoggedIn",isLoggedIn);
    }
    public static boolean isUserLoggedIn(){
        return DataLocalManager.getInstance().mySharePreferences.getBooleanValue("IsLoggedIn");
    }
    public  static void setPhoneNumber(String phone){
        DataLocalManager.getInstance().mySharePreferences.putStringValue("PhoneNumber",phone);
    }
    public static String getPhoneNumber(){
        return DataLocalManager.getInstance().mySharePreferences.getStringValue("PhoneNumber");
    }
    public  static void setLocation(String location){
        DataLocalManager.getInstance().mySharePreferences.putStringValue("Location",location);
    }
    public static String getLocation(){
        return DataLocalManager.getInstance().mySharePreferences.getStringValue("Location");
    }




}
