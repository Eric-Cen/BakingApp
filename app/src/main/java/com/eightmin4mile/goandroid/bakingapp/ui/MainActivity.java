package com.eightmin4mile.goandroid.bakingapp.ui;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.IdlingResource;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.eightmin4mile.goandroid.bakingapp.MainViewModel;
import com.eightmin4mile.goandroid.bakingapp.R;
import com.eightmin4mile.goandroid.bakingapp.SimpleIdlingResource;
import com.eightmin4mile.goandroid.bakingapp.Utility;
import com.eightmin4mile.goandroid.bakingapp.data.AppDatabase;
import com.eightmin4mile.goandroid.bakingapp.data.Ingredient;
import com.eightmin4mile.goandroid.bakingapp.data.Recipe;
import com.eightmin4mile.goandroid.bakingapp.widget.WidgetWorker;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
    implements RecipeAdapter.ItemClickListener {

    private static final String TAG = "MainActivity";

    // The Idling Resource which will be null in production.
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    private AppDatabase mDb;
    private MainViewModel viewModel;

    private RecyclerView recipeRecyclerView;
    private RecipeAdapter recipeAdapter;

    TextView emptyView;

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeView();

        recipeAdapter = new RecipeAdapter(getApplicationContext(), this);
        recipeRecyclerView.setAdapter(recipeAdapter);

        mDb = AppDatabase.getsInstance(getApplicationContext());
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (Utility.isNetworkAvailable(getApplicationContext())) {
            setupViewModel();
        } else {
            emptyView.setVisibility(View.VISIBLE);
            String errorMsg = getResources().getString(R.string.empty_view_text)
                + " "
                + getResources().getString(R.string.network_issue);
            emptyView.setText(errorMsg);
        }
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.getRecipeList(mIdlingResource).observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                Log.d(TAG, "onChanged: Updating list of recipes from LiveData in ViewModel");
                if (recipes != null && !recipes.isEmpty()) {
                    // show recipes in recyclerview
                    emptyView.setVisibility(View.INVISIBLE);
                    recipeAdapter.setRecipeList(recipes);

                } else {
                    Log.d(TAG, "onChanged: recipes list is null or empty");
                    emptyView.setVisibility(View.VISIBLE);
                    emptyView.setText(getResources().getString(R.string.empty_view_text));
                }
            }
        });
    }


    private void initializeView() {
        emptyView = (TextView) findViewById(R.id.tv_main_empty_view);
        recipeRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        if (getResources().getBoolean(R.bool.isTablet)) {
            mTwoPane = true;
            recipeRecyclerView.setLayoutManager(
                new GridLayoutManager(getApplicationContext(), 3));
        } else {
            mTwoPane = false;
            recipeRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        }
    }

    @Override
    public void onItemClickListener(int itemId) {

        Recipe recipe = recipeAdapter.getItem(itemId);
        ArrayList<Ingredient> ingredients = Utility.fromListtoArrayList(
            recipe.getIngredients()
        );

        // update app widget UI after user pick a recipe
        OneTimeWorkRequest widgetWorkRequest = WidgetWorker.getOneTimeRequest(recipe.getName(), ingredients);
        WorkManager.getInstance(MainActivity.this)
            .beginUniqueWork(WidgetWorker.TAG, ExistingWorkPolicy.REPLACE, widgetWorkRequest)
            .enqueue();


        Intent intent = new Intent(getApplicationContext(),
            DetailActivity.class);
        intent.putExtra(DetailActivity.RECIPE_ITEM,
            recipe);
        startActivity(intent);

    }

    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }
}
