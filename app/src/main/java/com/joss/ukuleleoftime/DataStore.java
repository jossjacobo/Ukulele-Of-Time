package com.joss.ukuleleoftime;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by: jossayjacobo
 * Date: 2/26/15
 * Time: 1:59 PM
 */
public class DataStore {

    private static String BROADCAST_BLE_SERVICE = "broadcast";
    private static String FIRST_LAUNCH = "first";

    private static SharedPreferences getDataStore(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    private static SharedPreferences.Editor getEditor(Context context) {
        return getDataStore(context).edit();
    }

    private static SharedPreferences getPrefs(Context context) {
        return getDataStore(context);
    }

    public static boolean getBroadcastBleService(Context context){
        return getPrefs(context).getBoolean(BROADCAST_BLE_SERVICE, false);
    }

    public static void persistBroadcastBleService(Context context, boolean broadcast){
        getEditor(context).putBoolean(BROADCAST_BLE_SERVICE, broadcast).commit();
    }

    public static boolean getFirstLaunch(Context context){
        return getPrefs(context).getBoolean(FIRST_LAUNCH, true);
    }

    public static void persistFirstLaunch(Context context, boolean broadcast){
        getEditor(context).putBoolean(FIRST_LAUNCH, broadcast).commit();
    }

}
