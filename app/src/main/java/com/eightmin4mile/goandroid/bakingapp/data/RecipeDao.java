package com.eightmin4mile.goandroid.bakingapp.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecipeDao {

    @Query("SELECT * FROM recipe ORDER BY id")
    List<Recipe> loadAllRecipes();

    @Insert
    void insertRecipe(Recipe recipe);

    @Delete
    void deleteRecipe(Recipe recipe);


    @Query("SELECT * FROM recipe WHERE id = :id")
    Recipe loadRecipeById(int id);

    @Query("SELECT * FROM recipe WHERE name = :name")
    Recipe loadRecipeByName(String name);
}
