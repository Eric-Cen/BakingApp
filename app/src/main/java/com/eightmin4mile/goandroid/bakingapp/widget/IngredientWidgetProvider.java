package com.eightmin4mile.goandroid.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.eightmin4mile.goandroid.bakingapp.R;
import com.eightmin4mile.goandroid.bakingapp.data.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class IngredientWidgetProvider extends AppWidgetProvider {

    private static final String TAG = "IngredientWidgetProvid";

    private static String recipeName;
    public static List<Ingredient> ingredientList;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, String name, List<Ingredient> ingredients) {

        recipeName = name;
        ingredientList = ingredients;

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        if (recipeName == null) {
            Log.d(TAG, "updateAppWidget: do nothing for recipeName = null");
        } else {

            remoteViews.setTextViewText(R.id.tv_widget_header, recipeName);

            Intent intent = new Intent(context, IngredientWidgetService.class);

            Bundle bundle = new Bundle();
            bundle.putString("name", recipeName);
            ArrayList<Ingredient> items = new ArrayList<>(ingredientList);
            bundle.putParcelableArrayList("ingredients", items);
            intent.putExtra("data", bundle);

            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.list_view);
            remoteViews.setRemoteAdapter(R.id.list_view, intent);
        }

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }


    public static void updateIngredientWidgets(Context context,
                                               AppWidgetManager appWidgetManager,
                                               int[] appWidgetIds,
                                               String name,
                                               List<Ingredient> ingredientArrayList) {

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, name, ingredientArrayList);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        if (recipeName != null && ingredientList != null) {
            OneTimeWorkRequest widgetWorkRequest = WidgetWorker.getOneTimeRequest(recipeName, ingredientList);
            WorkManager.getInstance(context)
                .beginUniqueWork(WidgetWorker.TAG, ExistingWorkPolicy.REPLACE, widgetWorkRequest)
                .enqueue();
        }
    }


    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // Perform any action when one or more AppWidget instances have been deleted
    }

    @Override
    public void onEnabled(Context context) {
        // Perform any action when an AppWidget for this provider is instantiated
        // Bug: WorkManager repeatedly triggers onUpdate on App widget
        // https://issuetracker.google.com/issues/180436098
        DummyWorker.schedule(context);
    }

    @Override
    public void onDisabled(Context context) {
        // Perform any action when the last AppWidget instance for this provider is deleted
        DummyWorker.remove(context);
    }
}
