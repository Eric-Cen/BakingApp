package com.eightmin4mile.goandroid.bakingapp;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.Nullable;
import android.util.Log;

import com.eightmin4mile.goandroid.bakingapp.data.AppDatabase;
import com.eightmin4mile.goandroid.bakingapp.data.Recipe;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by goandroid on 7/16/18.
 */

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = "MainViewModel";

    private MutableLiveData<List<Recipe>> recipeList;
    private AppDatabase database;

    public  MainViewModel (Application application){
        super(application);

        database = AppDatabase.getsInstance(this.getApplication());
        Log.d(TAG, "MainViewModel: Actively retrieving the tasks from the database");
        recipeList = new MutableLiveData<>();
    }


    public LiveData<List<Recipe>> getRecipeList(
            @Nullable final SimpleIdlingResource idlingResource) {

        loadRecipeData(idlingResource);

        return recipeList;
    }

    public void setRecipeList(List<Recipe> newRecipes){
        recipeList.setValue(newRecipes);
    }

    private void loadRecipeData(@Nullable final SimpleIdlingResource idlingResource) {

        // The IdlingResource is null in production.
        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }

        RetrofitRequest mRetrofitRequest =
                new Retrofit.Builder()
                        .baseUrl(RetrofitRequest.WEB_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(RetrofitRequest.class);

        final List<Recipe> newData = new ArrayList<>();
        Call<List<Recipe>> call = mRetrofitRequest.getRecipeResult();

        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {

                newData.addAll(response.body());
                //recipeAdapter.setRecipeList(newData);

                Log.d(TAG, "onResponse: recipe size = " + response.body().size());
                Log.d(TAG, "onResponse: newData size = " + newData.size());
                recipeList.postValue(newData);

                // insert recipe data into the ROOM database
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        for (Recipe newRecipe : newData) {
                            Recipe recipe = database.recipeDao().loadRecipeById(newRecipe.getId());
                            if(recipe == null) {
                                // add the database only if it has an unique id
                                database.recipeDao().insertRecipe(newRecipe);
                            }
                        }
                    }
                });

                if (idlingResource != null) {
                    idlingResource.setIdleState(true);
                }

            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + "Retrofit error");
                Log.d(TAG, "onFailure: can't retrieve recipe data");

            }
        });

        Log.d(TAG, "getInternetRecipeData: returning newData.size() = " + newData.size());
        if(newData!= null && newData.size()>0){
            Log.d(TAG, "getInternetRecipeData: returning newData");
        } else {
            Log.d(TAG, "getInternetRecipeData: returning null");

        }


    }

}
