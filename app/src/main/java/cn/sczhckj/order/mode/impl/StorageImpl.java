package cn.sczhckj.order.mode.impl;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * @ describe: 存储数据
 * @ author: Like on 2016/12/29.
 * @ email: 572919350@qq.com
 */

public class StorageImpl {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public StorageImpl(Context mContext, String name) {
        sharedPreferences = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    /**
     * 设置数据
     *
     * @param key
     * @param value
     */
    public void setData(String key, Object value) {
        if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else {
            editor.putStringSet(key, (Set<String>) value);
        }
        editor.commit();
    }

    /**
     * 获取数据
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public Object getData(String key, Object defaultValue) {
        if (defaultValue == null) {
            return sharedPreferences.getAll();
        }
        if (defaultValue instanceof Integer) {
            return sharedPreferences.getInt(key, (Integer) defaultValue);
        } else if (defaultValue instanceof String) {
            return sharedPreferences.getString(key, (String) defaultValue);
        } else if (defaultValue instanceof Boolean) {
            return sharedPreferences.getBoolean(key, (Boolean) defaultValue);
        } else if (defaultValue instanceof Float) {
            return sharedPreferences.getFloat(key, (Float) defaultValue);
        } else if (defaultValue instanceof Long) {
            return sharedPreferences.getLong(key, (Long) defaultValue);
        } else {
            return sharedPreferences.getStringSet(key, (Set<String>) defaultValue);
        }

    }

}
