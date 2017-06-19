package lilun.com.pensionlife.module.utils;


import android.content.Context;
import android.content.SharedPreferences;

import lilun.com.pensionlife.app.App;

/**
 * SharedPreferences存储的工具类
 */
public class PreUtils {

    private static SharedPreferences getSharedPreferences() {
        return android.preference.PreferenceManager.getDefaultSharedPreferences(App.context);
    }

    public static boolean isFirstTime(Context context, String key) {
        if (getBoolean(key, false)) {
            return false;
        } else {
            putBoolean(key, true);
            return true;
        }
    }

    public static boolean contains(String key) {
        return PreUtils.getSharedPreferences().contains(key);
    }

    public static int getInt(final String key, final int defaultValue) {
        return PreUtils.getSharedPreferences().getInt(key, defaultValue);
    }

    public static boolean putInt(final String key, final int pValue) {
        final SharedPreferences.Editor editor = PreUtils.getSharedPreferences().edit();

        editor.putInt(key, pValue);

        return editor.commit();
    }

    public static long getLong(final String key, final long defaultValue) {
        return PreUtils.getSharedPreferences().getLong(key, defaultValue);
    }

    public static Long getLong(final String key, final Long defaultValue) {
        if (PreUtils.getSharedPreferences().contains(key)) {
            return PreUtils.getSharedPreferences().getLong(key, 0);
        } else {
            return null;
        }
    }


    public static boolean putLong(final String key, final long pValue) {
        final SharedPreferences.Editor editor = PreUtils.getSharedPreferences().edit();

        editor.putLong(key, pValue);

        return editor.commit();
    }

    public static boolean getBoolean(final String key, final boolean defaultValue) {
        return PreUtils.getSharedPreferences().getBoolean(key, defaultValue);
    }

    public static boolean putBoolean(final String key, final boolean pValue) {
        final SharedPreferences.Editor editor = PreUtils.getSharedPreferences().edit();

        editor.putBoolean(key, pValue);

        return editor.commit();
    }

    public static String getString(final String key, final String defaultValue) {
        return PreUtils.getSharedPreferences().getString(key, defaultValue);
    }

    public static boolean putString(final String key, final String pValue) {
        final SharedPreferences.Editor editor = PreUtils.getSharedPreferences().edit();

        editor.putString(key, pValue);

        return editor.commit();
    }


    public static boolean remove(final String key) {
        final SharedPreferences.Editor editor = PreUtils.getSharedPreferences().edit();

        editor.remove(key);

        return editor.commit();
    }

    public static boolean clear() {
        final SharedPreferences.Editor editor = PreUtils.getSharedPreferences().edit();

        editor.clear();
        return editor.commit();
    }

//    public static Theme getCurrentTheme(Context context) {
//        back Theme.valueOf(PreUtils.getString(context, "app_theme", Theme.Red.name()));
//    }
//
//    public static void setCurrentTheme(Context context, Theme currentTheme) {
//        PreUtils.putString(context, "app_theme", currentTheme.name());
//    }
}