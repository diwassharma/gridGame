package curofy.staging.messenger.phonepegrid;

import android.content.Context;
import android.content.SharedPreferences;

public class Sessions {
    private static final String KEY = "IDvalue";
    private static final String LEVEL = "level";


    public static boolean setLevel(Context context, Integer version) {
        if (context == null) {
            return false;
        }
        SharedPreferences.Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
        editor.putInt(LEVEL, version);
        return editor.commit();
    }

    public static Integer getLevel(Context context) {
        if (context == null) {
            return 0;
        }
        SharedPreferences preferences = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        return preferences.getInt(LEVEL, 1);
    }


}
