package lib.utils.SharedPreferencesUtil;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import lib.MainApplication;


/**
 * Created by n-240 on 2015/9/25.*/


public abstract class BaseSharedPreferences {

    protected abstract String getPreferencesName();

    protected Context mContext;

    public BaseSharedPreferences(Context context) {
        this.mContext = context;
    }

    protected SharedPreferences getPreferences() {
        if (getPreferencesName() == null)
            return PreferenceManager.getDefaultSharedPreferences(MainApplication.getInstance());
        else
            return mContext.getSharedPreferences(getPreferencesName(), Context.MODE_PRIVATE);
    }

    protected SharedPreferences.Editor getPreferencesEdit() {
        return getPreferences().edit();
    }


/*
*
     * write the preferences*/


    protected void writePreferences(String key, String value) {
        SharedPreferences.Editor editor = getPreferencesEdit();
        editor.putString(key, value);
        editor.commit();
    }

    protected void writePreferences(String key, int value) {
        SharedPreferences.Editor editor = getPreferencesEdit();
        editor.putInt(key, value);
        editor.commit();
    }

    protected void writePreferences(String key, boolean value) {
        SharedPreferences.Editor editor = getPreferencesEdit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    protected Integer readInt(String key, int defValue) {
        return getPreferences().getInt(key, defValue);
    }

    protected Boolean readBoolean(String key, boolean defValue) {
        return getPreferences().getBoolean(key, defValue);
    }

    protected String readString(String key, String defValue) {
        return getPreferences().getString(key, defValue);
    }

}

