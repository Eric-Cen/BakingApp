package com.eightmin4mile.goandroid.bakingapp.widget

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkerParameters
import com.eightmin4mile.goandroid.bakingapp.data.Ingredient
import com.eightmin4mile.goandroid.bakingapp.utils.getParcelableList
import com.eightmin4mile.goandroid.bakingapp.utils.putParcelableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WidgetWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {

    companion object {
        const val TAG = "TAG_WidgetWorker"
        const val KEY_INGREDIENTS = "KEY_INGREDIENTS"
        const val KEY_RECIPE_NAME = "KEY_RECIPE_NAME"

        @JvmStatic
        fun getOneTimeRequest(recipeName: String, ingredients: List<Ingredient>): OneTimeWorkRequest {
            return OneTimeWorkRequest.Builder(WidgetWorker::class.java)
                .setInputData(buildInputData(recipeName, ingredients))
                .build()
        }

        @JvmStatic
        fun buildInputData(recipeName: String, ingredients: List<Ingredient>): Data {
            val builder = Data.Builder()
            builder.putString(KEY_RECIPE_NAME, recipeName)
            builder.putParcelableList(KEY_INGREDIENTS, ingredients)
            return builder.build()
        }
    }

    override suspend fun doWork(): Result =
        withContext(Dispatchers.IO) {
            // get teh ingredients

            val ingredients = inputData.getParcelableList<Ingredient>(KEY_INGREDIENTS)

            val name = inputData.getString(KEY_RECIPE_NAME) ?: ""
            updateWidget(applicationContext, name, ingredients)

            Result.success()
        }

    private fun updateWidget(ctx: Context, name: String, ingredientList: List<Ingredient>) {
        val appWidgetManager = AppWidgetManager.getInstance(ctx)
        val appWidgetsIds = appWidgetManager
            .getAppWidgetIds(ComponentName(ctx, IngredientWidgetProvider::class.java))
        IngredientWidgetProvider.updateIngredientWidgets(
            ctx,
            appWidgetManager,
            appWidgetsIds,
            name,
            ingredientList
        )
    }
}
