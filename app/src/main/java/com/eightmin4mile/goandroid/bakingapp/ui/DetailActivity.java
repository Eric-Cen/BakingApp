package com.eightmin4mile.goandroid.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.eightmin4mile.goandroid.bakingapp.DetailViewModel;
import com.eightmin4mile.goandroid.bakingapp.R;
import com.eightmin4mile.goandroid.bakingapp.Utility;
import com.eightmin4mile.goandroid.bakingapp.data.Recipe;


public class DetailActivity extends AppCompatActivity {
    private static final String TAG = "DetailActivity";

    public static String RECIPE_ITEM = "recipe_item";

    private int currentStepId;

    private DetailViewModel detailViewModel;

    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Recipe recipe = null;
        Intent intent = getIntent();
        if (intent != null
            && intent.hasExtra(RECIPE_ITEM)) {
            recipe = intent.getParcelableExtra(RECIPE_ITEM);

        } else {
            finish();
        }

        setTitle(recipe.getName());
        setupDetailViewModel(recipe);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeView();
    }


    private void setupDetailViewModel(Recipe newRecipe) {
        detailViewModel = new ViewModelProvider(this).get(DetailViewModel.class);
        detailViewModel.setRecipeLiveData(newRecipe);
    }


    private void initializeView() {
        if (findViewById(R.id.guideline) != null) {
            detailViewModel.setTwoPane(true);
            setupTwoPane();
        } else {
            detailViewModel.setTwoPane(false);
            setupSinglePane();
        }
    }

    private void setupSinglePane() {

        if (!detailViewModel.isIngredientsDisplayed()) {

            String stepListTag = "step_list";

            fragmentManager = getSupportFragmentManager();

            StepListFragment stepListFragment = (StepListFragment) fragmentManager
                .findFragmentByTag(stepListTag);
            if (stepListFragment == null) {
                stepListFragment = StepListFragment.newInstance();
            }

            fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, stepListFragment, stepListTag)
                .commit();
        }
    }

    private void setupTwoPane() {
        currentStepId = detailViewModel.getCurrentStepId();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        String videoTag = "video" + currentStepId;
        VideoFragment videoFragment = (VideoFragment) getSupportFragmentManager()
            .findFragmentByTag(videoTag);

        if (videoFragment == null) {
            String urlPath = detailViewModel.getRecipe().getValue()
                .getSteps().get(currentStepId)
                .getVideoURL();
            videoFragment = VideoFragment.newInstance(urlPath);
        }
        transaction.replace(R.id.fragment_video, videoFragment, videoTag);

        String stepTag = "step" + currentStepId;
        StepFragment stepFragment = (StepFragment) getSupportFragmentManager()
            .findFragmentByTag(stepTag);

        if (stepFragment == null) {
            stepFragment = StepFragment.newInstance(currentStepId,
                Utility.fromListtoArrayList(detailViewModel.getRecipe().getValue().getSteps()));
        }

        transaction.replace(R.id.fragment_step_instruction, stepFragment, stepTag);
        transaction.addToBackStack(stepTag);

        String stepListTag = "step_list";
        StepListFragment stepListFragment = (StepListFragment) getSupportFragmentManager()
            .findFragmentByTag(stepListTag);

        if (stepListFragment == null) {
            stepListFragment = StepListFragment.newInstance();
        }
        transaction.replace(R.id.fragment_container, stepListFragment, stepListTag);

        transaction.commit();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!detailViewModel.isTwoPane()) {
            if (detailViewModel.isIngredientsDisplayed()) {
                detailViewModel.setIngredientsDisplayed(false);
            }
        }
    }
}
