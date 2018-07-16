package com.eightmin4mile.goandroid.bakingapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by goandroid on 6/25/18.
 */

public class Recipe implements Parcelable{

    /*
    {
        "id":1,
        "name":"Nutella Pie",
        "ingredients":[],
        "steps":[],
        "servings":8,
        "image":""
    },
    */

    int id;
    String name;
    List<Ingredient> ingredients;
    List<Step> steps;
    int servings;
    String image;

    public Recipe(int id,
                  String name,
                  List<Ingredient> ingredientList,
                  List<Step> stepList,
                  int servings,
                  String image){
        this.id = id;
        this.name = name;
        this.ingredients = ingredientList;
        this.steps = stepList;
        this.servings = servings;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


//    int id;
//    String name;
//    List<Ingredient> ingredients;
//    List<Step> steps;
//    int servings;
//    String image;



    @Override
    public int describeContents() {
        return 0;
    }

    protected Recipe(Parcel in){
        this.id = in.readInt();
        this.name = in.readString();
        this.ingredients = in.readArrayList(Ingredient.class.getClassLoader());
        this.steps = in.readArrayList(Step.class.getClassLoader());

        // this.ingredients = in.createTypedArrayList(Ingredient.CREATOR);
        //this.steps  = in.createTypedArrayList(Step.CREATOR);

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeList(ingredients);
        dest.writeList(steps);
        dest.writeInt(servings);
        dest.writeString(image);

    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel parcel) {
            return new Recipe(parcel);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}
