package com.omenacle.bamzu;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by omegareloaded on 7/13/16.
 */
public class PrefManager {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Context _context;

    int PRIVATE_MODE = 0;

    //Shared preferences file name
    private static final String PREF_NAME = "bamzu-welcome";
    private static final String FIRST_TIME_LAUNCH = "isFirstTimeLaunch";

    public PrefManager(Context context) {
        this._context = context;
        this.preferences = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        this.editor = preferences.edit();
    }

    public void setFirstTimeLaunch(Boolean isFirstTime) {
        editor.putBoolean(FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public Boolean isFirstTimeLaunch() {
        return preferences.getBoolean(FIRST_TIME_LAUNCH, true);
    }


}
