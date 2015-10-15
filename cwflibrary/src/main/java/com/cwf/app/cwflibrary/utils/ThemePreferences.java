package com.cwf.app.cwflibrary.utils;

import android.content.Context;
import android.graphics.Color;

import com.cwf.app.cwflibrary.SharedPreferencesUtil.BaseSharedPreferences;


/**
 * Created by n-240 on 2015/9/25.*/


public class ThemePreferences extends BaseSharedPreferences {


    private static final String StatusTintColor= "StatusTintColor";

    public ThemePreferences(Context context) {
        super(context);
    }

    @Override
    protected String getPreferencesName() {
        return "this";
    }

    public  void setStatusTintColor(int color){
        writePreferences(StatusTintColor,color);
    }

    public int getStatusTineColor(){
        return readInt(StatusTintColor, Color.BLACK);
    }
}
