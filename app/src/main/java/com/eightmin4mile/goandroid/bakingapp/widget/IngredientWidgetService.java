package com.eightmin4mile.goandroid.bakingapp.widget;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.eightmin4mile.goandroid.bakingapp.data.Ingredient;

import java.util.List;

/**
 * Created by goandroid on 9/6/18.
 */

public class IngredientWidgetService extends RemoteViewsService {


    private static final String TAG = "IngredientWidgetService";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientRemoteViewsFactory(this, intent);
    }

}

class IngredientRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private static final String TAG = "RemoteViewsFactory";
    public static final String INGREDIENTS_EXTRA = "ingredient_list";


    Context mContext;
    List<Ingredient> ingredients;
    String recipeName;


    public IngredientRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;

        //recipeName = intent.getStringExtra("name");

        Bundle extras = intent.getBundleExtra("data");
        if(extras == null){

            Log.d(TAG, "IngredientRemoteViewsFactory: extras is null");
        } else {
            recipeName = extras.getString("name");
            ingredients = extras.getParcelableArrayList("ingredients");
        }

    }

    @Override
    public void onCreate() {

    }

    // called on start and when notifyAppWidgetViewDataChanged is called
    @Override
    public void onDataSetChanged() {

        //how to initialize ingredients list
        ingredients = IngredientWidgetProvider.ingredientList;

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return ingredients == null ? 0 : ingredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (ingredients == null || ingredients.isEmpty()) {
            Log.d(TAG, "getViewAt: ingredients = null");
            return null;
        } else {

           // Log.d(TAG, "getViewAt: ingredients.size() = " + ingredients.size());
            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), android.R.layout.simple_list_item_1);
            Ingredient ingredient = ingredients.get(position);
            remoteViews.setTextViewText(android.R.id.text1,
                    ingredient.getQuantity()
                            + " "
                            + ingredient.getMeasure()
                            + ", "
                            + ingredient.getIngredient()
            );
            return remoteViews;

        }


    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;  // Treat all items in the ListView the same
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

