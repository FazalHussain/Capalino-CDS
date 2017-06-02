package com.MWBE.Connects.NY.AppConstants;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by asif on 2/24/2016.
 */
public class Utils {
    static Context context;
    public static final String TAG = "Utils";
    public static final String UserName = "UserName";
    private static HttpURLConnection urlc;
    public SharedPreferences preferences;
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";

    SharedPreferences.Editor editor;

    public Utils(Context context) {

        Utils.context = context;
        preferences = context.getSharedPreferences("gcmdemo", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void savedata(String key, String val) {
        editor.putString(key, val).commit();
    }

    public boolean saveArray(Set<String> sKey) {

        editor.putStringSet("key", sKey);
        return editor.commit();
    }

    public ArrayList<String> getArray() {
        ArrayList<String> getDeletedList = new ArrayList<>();
        Set<String> set = preferences.getStringSet("key", null);
        if(set!=null)
        getDeletedList.addAll(set);

        return getDeletedList;
    }


    public ArrayList<Integer> loadArray(Context mContext) {
        ArrayList<Integer> list = new ArrayList<>();
        list.clear();
        int size = preferences.getInt("Status_size", 0);

        for (int i = 0; i < size; i++) {
            list.add(Integer.valueOf(preferences.getString("Status_" + i, null)));
        }

        return list;
    }


    public String getdata(String key) {
        String value = preferences.getString(key, "");
        if (value.isEmpty()) {
            Log.i(TAG, key + " not found.");
            return "";
        }
        return value;
    }

    public static boolean isConnected(Context context) {


        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable()){
            return true;
        }
        return false;

    }

    private static String LIST_SEPARATOR = ",";

    public static String convertListToString(Integer[] intList) {
        StringBuffer stringBuffer = new StringBuffer();
        for (Integer str : intList) {
            stringBuffer.append(String.valueOf(str)).append(LIST_SEPARATOR);
        }

        // Remove last separator
        int lastIndex = stringBuffer.lastIndexOf(LIST_SEPARATOR);
        stringBuffer.delete(lastIndex, lastIndex + LIST_SEPARATOR.length() + 1);

        return stringBuffer.toString();
    }

    public static List<String> convertStringToList(String str) {
        return Arrays.asList(String.valueOf(str).split(LIST_SEPARATOR));
    }

    public static int hasActiveInternetConnection(final Context context) throws IOException {

        Thread myconnectionthread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (isConnected(context)) {
                    try {
                        urlc = (HttpURLConnection) (new URL("https://google.com").openConnection());
                        urlc.setRequestProperty("User-Agent", "Test");
                        urlc.setRequestProperty("Connection", "close");
                        urlc.setConnectTimeout(1500);
                        urlc.connect();
                    } catch (IOException e) {
                        Log.e("Connected", "Error checking internet connection", e);
                    }
                } else {
                    Log.d("Disconnect", "No network available!");
                }
            }
        });

        myconnectionthread.start();

        if(urlc!=null) {
            if(urlc.getResponseCode() == 200){
                return 1;
            }
        }

        return 0;
    }

    public static void AlertInternetConnection(Context context){
        new AlertDialog.Builder(context)
                .setTitle("Alert!")
                .setMessage("There is a problem when geting response from server. Check your internet connection, Please try again.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


}
