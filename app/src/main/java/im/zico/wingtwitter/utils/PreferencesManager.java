package im.zico.wingtwitter.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by tinyao on 11/29/14.
 */
public class PreferencesManager {

    private static final String PREF_NAME = "com.example.app.PREF_NAME";

    private static PreferencesManager sInstance;
    private final SharedPreferences mPref;

    private PreferencesManager(Context context) {
        mPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized PreferencesManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PreferencesManager(context);
        }
        return sInstance;
    }

    public void setValue(String key, String value) {
        mPref.edit()
                .putString(key, value)
                .commit();
    }

    public void setValue(String key, long value) {
        mPref.edit()
                .putLong(key, value)
                .commit();
    }

    public boolean hasKey(String key) {
        return mPref.contains(key);
    }

    public String getValue(String key) {
        return mPref.getString(key, null);
    }

    public long getLongValue(String key) {
        return mPref.getLong(key, 0);
    }

    public void remove(String key) {
        mPref.edit()
                .remove(key)
                .commit();
    }

    public boolean clear() {
        return mPref.edit()
                .clear()
                .commit();
    }

}
