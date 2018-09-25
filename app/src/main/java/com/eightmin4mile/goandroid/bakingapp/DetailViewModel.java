package com.eightmin4mile.goandroid.bakingapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.eightmin4mile.goandroid.bakingapp.data.Ingredient;
import com.eightmin4mile.goandroid.bakingapp.data.Recipe;
import com.eightmin4mile.goandroid.bakingapp.data.Step;

import java.util.List;

/**
 * Created by goandroid on 7/16/18.
 */

public class DetailViewModel extends ViewModel {

    private static final String TAG = "DetailViewModel";

    private MutableLiveData<Recipe> recipeLiveData;
    private boolean twoPane;
    private int currentStepId = 0;
    private boolean ingredientsDisplayed;

    public void setRecipeLiveData(Recipe recipe){
        if(this.recipeLiveData != null
                && this.recipeLiveData.getValue().getId() != recipe.getId()){
            return; // do nothing since it is the same recipe data
        }

        if(this.recipeLiveData == null){
            this.recipeLiveData = new MutableLiveData<>();
        }

        this.recipeLiveData.setValue(recipe);
    }

    public  LiveData<Recipe> getRecipe(){
        return recipeLiveData;
    }


    public boolean isTwoPane() {
        return twoPane;
    }

    public void setTwoPane(boolean twoPane) {
        this.twoPane = twoPane;
    }

    public int getCurrentStepId() {
        return currentStepId;
    }

    public void setCurrentStepId(int stepId) {
        this.currentStepId = stepId;
    }


    public boolean isIngredientsDisplayed() {
        return ingredientsDisplayed;
    }

    public void setIngredientsDisplayed(boolean ingredientsDisplayed) {
        this.ingredientsDisplayed = ingredientsDisplayed;
    }
}
