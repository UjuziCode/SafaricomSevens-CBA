package com.cbagroup.safaricom.sevens.util;

import android.content.Context;
import android.content.SharedPreferences;

public class Pref {
	
	private static SharedPreferences settings;

    private static SharedPreferences.Editor editor;
    
    public static String user="";
    
    public static int _ID = 0;
    
    public static final String PREFS_NAME = "CBA Group";
    
    
    public static void loadSettings(Context context) {
        final SharedPreferences settings = (SharedPreferences) context.getSharedPreferences(PREFS_NAME, 0);     
        user=settings.getString("User", user); 
        _ID =settings.getInt("ID", _ID);
    }
    
    public static void saveSettings(Context context) {
        settings = (SharedPreferences) context.getSharedPreferences(PREFS_NAME, 0);
        editor = settings.edit();       
        editor.putString("User", user);
        editor.putInt("ID",_ID);
        editor.commit();
    }

}