package com.example.idcard_demo;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesHelper {
    private static String name = "config";

    /**
     * SharedPreferences
     * 
     * @param context
     * @return
     */
    public static SharedPreferences getSharedPreference(Context context) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    /**
     * ����һ��Boolean���͵�ֵ��
     * 
     * @param context
     * @param key
     * @param value
     * @return
     */
    public static boolean putBoolean(Context context, String key, Boolean value) {
        SharedPreferences sharedPreference = getSharedPreference(context);
        Editor editor = sharedPreference.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    /**
     * ����һ��int���͵�ֵ��
     * 
     * @param context
     * @param key
     * @param value
     * @return
     */
    public static boolean putInt(Context context, String key, int value) {
        SharedPreferences sharedPreference = getSharedPreference(context);
        Editor editor = sharedPreference.edit();
        editor.putInt(key, value);
        return editor.commit();
    }


    /**
     * ����һ��float���͵�ֵ��
     * 
     * @param context
     * @param key
     * @param value
     * @return
     */
    public static boolean putFloat(Context context, String key, float value) {
        SharedPreferences sharedPreference = getSharedPreference(context);
        Editor editor = sharedPreference.edit();
        editor.putFloat(key, value);
        return editor.commit();
    }

    /**
     * ����һ��long���͵�ֵ��
     * 
     * @param context
     * @param key
     * @param value
     * @return
     */
    public static boolean putLong(Context context, String key, long value) {
        SharedPreferences sharedPreference = getSharedPreference(context);
        Editor editor = sharedPreference.edit();
        editor.putLong(key, value);
        return editor.commit();
    }

    /**
     * ����һ��String���͵�ֵ��
     * 
     * @param context
     * @param key
     * @param value
     * @return
     */
    public static boolean putString(Context context, String key, String value) {
        SharedPreferences sharedPreference = getSharedPreference(context);
        Editor editor = sharedPreference.edit();
        editor.putString(key, value);
        return editor.commit();
    }

   

    /**
     * ��ȡString��value
     * 
     * @param context
     * @param key
     *            ����
     * @param defValue
     *            Ĭ��ֵ
     * @return
     */
    public static String getString(Context context, String key, String defValue) {
        SharedPreferences sharedPreference = getSharedPreference(context);
        return sharedPreference.getString(key, defValue);
    }

    /**
     * ��ȡint��value
     * 
     * @param context
     * @param key
     *            ����
     * @param defValue
     *            Ĭ��ֵ
     * @return
     */
    public static int getInt(Context context, String key, int defValue) {
        SharedPreferences sharedPreference = getSharedPreference(context);
        return sharedPreference.getInt(key, defValue);
    }

    /**
     * ��ȡfloat��value
     * 
     * @param context
     * @param key
     *            ����
     * @param defValue
     *            Ĭ��ֵ
     * @return
     */
    public static float getFloat(Context context, String key, Float defValue) {
        SharedPreferences sharedPreference = getSharedPreference(context);
        return sharedPreference.getFloat(key, defValue);
    }

    /**
     * ��ȡboolean��value
     * 
     * @param context
     * @param key
     *            ����
     * @param defValue
     *            Ĭ��ֵ
     * @return
     */
    public static boolean getBoolean(Context context, String key,
            Boolean defValue) {
        SharedPreferences sharedPreference = getSharedPreference(context);
        return sharedPreference.getBoolean(key, defValue);
    }

    /**
     * ��ȡlong��value
     * 
     * @param context
     * @param key
     *            ����
     * @param defValue
     *            Ĭ��ֵ
     * @return
     */
    public static long getLong(Context context, String key, long defValue) {
        SharedPreferences sharedPreference = getSharedPreference(context);
        return sharedPreference.getLong(key, defValue);
    }

    /**
     * ɾ��Key��Ӧ������
     * 
     * @param context
     * @param key
     * @return
     */
    public static boolean remove(Context context, String key) {
        SharedPreferences sharedPreference = getSharedPreference(context);
        Editor editor = sharedPreference.edit();
        editor.remove(key);
        return editor.commit();
    }

    /**
     * ���SharedPreferences�е�����ֵ��
     * 
     * @param context
     * @return
     */
    public static boolean clear(Context context) {
        SharedPreferences sharedPreference = getSharedPreference(context);
        Editor editor = sharedPreference.edit();
        editor.clear();
        return editor.commit();
    }
}
