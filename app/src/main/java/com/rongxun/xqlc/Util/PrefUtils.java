package com.rongxun.xqlc.Util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * author: HouShengLi
 * description: SharedPreferences工具类
 *              存储位置：用DDMS工具找到文件系统下/data/data/你应用的包名/shared_prefs文件夹
 *              文件名字：config
 */

public class PrefUtils {

    /**
     * boolean类型存取
     *
     * @param ctx
     * @param key
     * @param defValue
     * @return
     */
    public static boolean getBoolean(Context ctx, String key, boolean defValue) {
        SharedPreferences sp = ctx.getSharedPreferences("config",
                Context.MODE_PRIVATE);
        return sp.getBoolean(key, defValue);
    }

    public static void putBoolean(Context ctx, String key, boolean value) {
        SharedPreferences sp = ctx.getSharedPreferences("config",
                Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }

    /**
     * String类型存取
     *
     * @param ctx
     * @param key
     * @param value
     */
    public static void putString(Context ctx, String key, String value) {
        SharedPreferences sp = ctx.getSharedPreferences("config",
                Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }

    public static String getString(Context ctx, String key, String defValue) {
        SharedPreferences sp = ctx.getSharedPreferences("config",
                Context.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }

    /**
     * int类型存取
     *
     * @param ctx
     * @param key
     * @param value
     */
    public static void putInt(Context ctx, String key, int value) {
        SharedPreferences sp = ctx.getSharedPreferences("config",
                Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).commit();
    }

    public static int getInt(Context ctx, String key, int defValue) {
        SharedPreferences sp = ctx.getSharedPreferences("config",
                Context.MODE_PRIVATE);
        return sp.getInt(key, defValue);
    }

    /**
     * float类型的存取
     *
     * @param ctx
     * @param key
     * @param value
     */
    public static void putFloat(Context ctx, String key, float value) {
        SharedPreferences sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        sp.edit().putFloat(key, value).commit();

    }

    public static float getFloat(Context ctx, String key, float defValue) {
        SharedPreferences sp = ctx.getSharedPreferences("config",
                Context.MODE_PRIVATE);
        return sp.getFloat(key, defValue);
    }
}
