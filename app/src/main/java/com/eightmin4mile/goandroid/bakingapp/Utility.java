package com.eightmin4mile.goandroid.bakingapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;



public class Utility {

    //prevent Utility class from being instantiated.
    private Utility() {
    }

    public static void showToastMessage(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT)
            .show();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
            = (ConnectivityManager) context
            .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
            .getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public static <Type> ArrayList<Type> fromListtoArrayList(List<Type> newList) {
        int size = newList.size();

        ArrayList<Type> arrayList = new ArrayList<>(size);

        arrayList.addAll(newList);

        return arrayList;
    }
}
