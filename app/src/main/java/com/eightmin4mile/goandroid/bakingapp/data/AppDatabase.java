package com.eightmin4mile.goandroid.bakingapp.data;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Recipe.class},
    version = 1,
    exportSchema = false)
@TypeConverters(DataConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "recipelist";
    private static AppDatabase sInstance;

    public static AppDatabase getsInstance(Context context) {
        if (sInstance == null) {

            synchronized (LOCK) {
                if (sInstance == null) { // Double check synchronization
                    Log.d(LOG_TAG, "Creating new database instance");
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class,
                        AppDatabase.DATABASE_NAME).build();
                }
            }
        }

        return sInstance;
    }

    public abstract RecipeDao recipeDao();
}
