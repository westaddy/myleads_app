package com.myleads.android.custom;

import android.content.Context;
import android.content.SharedPreferences;


public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "android";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String TOKEN_KEY = "token";
    private static final String USER_ID = "userId";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setToken(String token) {
        editor.putString(TOKEN_KEY,token);
        editor.commit();
        pref.edit().remove(TOKEN_KEY);
    }

    public void deleteToken() {
        pref.edit().remove(TOKEN_KEY).commit();
    }

    public String getToken() {
        return pref.getString(TOKEN_KEY,null);

    }

    public void setUserId(String userId) {
        editor.putString(USER_ID,userId);
        editor.commit();
    }

    public String getUserId() {
        return pref.getString(USER_ID,null);

    }

    public void deleteUserId() {
        pref.edit().remove(USER_ID).commit();
    }

}