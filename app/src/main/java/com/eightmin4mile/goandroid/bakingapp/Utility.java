package com.eightmin4mile.goandroid.bakingapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.eightmin4mile.goandroid.bakingapp.data.Recipe;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by goandroid on 6/25/18.
 */

public class Utility {

    private static final String TAG = "Utility";
    public static void showToastMessage(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT)
                .show();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public static <Type> ArrayList<Type> fromListtoArrayList (List<Type> newList) {
        int size = newList.size();

        ArrayList<Type> arrayList = new ArrayList<>(size);

        arrayList.addAll(newList);

        return arrayList;

    }


}
