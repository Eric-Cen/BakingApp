package com.eightmin4mile.goandroid.bakingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.eightmin4mile.goandroid.bakingapp.data.Recipe;
import com.eightmin4mile.goandroid.bakingapp.data.SampleData;
import com.eightmin4mile.goandroid.bakingapp.data.Step;

import java.util.List;

public class MainActivity extends AppCompatActivity
    implements RecipeAdapter.ItemClickListener {

    // TODO 6-25-2018
    // 1) completed - create Recipe object and sample data
    // 2) completed - implement RecyclerView with sample data on Phone

    // TODO 7-02-2018
    // 1) add AppExecutor file
    // 2) implement json parser with retrofit
    // 3) create fragments, fragment_recipes, fragment_detail,
    //                      fragment_ingredients and fragment_steps
    // 4) implement RecyclerView with sample data on Tablet
    // 5) implement Test for RecyclerView

    // TODO 7-03-2018
    // 1) add ROOM database
    // 2) add ViewModel

    // TODO 7-04-2018
    // 1) tablet and customize UI
    // 2) implement exoplayer

    private static final String TAG = "MainActivity";

    private RecyclerView recipeRecyclerView;
    private RecipeAdapter recipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeView();

        recipeRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recipeAdapter = new RecipeAdapter(getApplicationContext(), this);
        recipeRecyclerView.setAdapter(recipeAdapter);

        recipeAdapter.setRecipeList(SampleData.getRecipeList(60));

        List<Recipe> recipeList = recipeAdapter.getRecipeList();
        Intent intent = new Intent();
        intent.putExtra("recipe", recipeList.get(0));

        Recipe recipe = intent.getParcelableExtra("recipe");
        Log.d(TAG, "onCreate: recipe.name = " + recipe.getName());

        Step originStep = recipeList.get(0).getSteps().get(0);
        Log.d(TAG, "onCreate: # step1.description = " + originStep.getDescription());
        Step step = recipe.getSteps().get(0);
        Log.d(TAG, "onCreate: step1.description = " + step.getDescription());
    }


    private void initializeView(){
        recipeRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
    }

    @Override
    public void onItemClickListener(int itemId) {
        Log.d(TAG, "onItemClickListener: " + itemId);
    }
}
