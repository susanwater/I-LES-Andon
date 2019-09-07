package com.yangda.andon.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.yangda.andon.MyApplication;


/**
 * Created by Admin on 17/6/2.
 */
public class PreferencesHelper {

    private static final String DATA_NAME = "LEDAI.SHARED_PREFERENCES";



    private static PreferencesHelper instance;

    /**sp注册listener的时候弱引用，为了防止销毁，所以加了这个引用关系*/
    private SharedPreferences.OnSharedPreferenceChangeListener prefChangeListener;

    private SharedPreferences pref;



    public static synchronized PreferencesHelper getInstance() {
        if (instance == null) {
            instance = new PreferencesHelper();
            instance.pref = MyApplication.getApplication().getSharedPreferences(DATA_NAME, Context.MODE_PRIVATE);

        }
        return instance;
    }

    public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        prefChangeListener = listener;
        pref.registerOnSharedPreferenceChangeListener(listener);
    }



    public boolean getBoolean(String name, boolean bDefault) {
        return pref.getBoolean(name, bDefault);
    }

    public boolean putBoolean(String name, boolean value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(name, value);
        return editor.commit();
    }

    public boolean putString(String key, String value) {
//        return  putEncryptString(key,value);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public String getString(String key) {
//        return  getDecryptString(key);
        return pref.getString(key, "");
    }


    public boolean putInt(String key, int value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    public int getInt(String key, int iDefault) {
        return pref.getInt(key, iDefault);
    }

    public boolean putLong(String key, Long value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(key, value);
        return editor.commit();
    }

    public Long getLong(String key, Long iDefault) {
        return pref.getLong(key, iDefault);
    }

    /**
     * 删除一条数据
     *
     * @param key
     * @return
     */
    public boolean remove(String key) {
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(key);
        return editor.commit();
    }

}
