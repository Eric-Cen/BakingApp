package com.eightmin4mile.goandroid.bakingapp;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eightmin4mile.goandroid.bakingapp.data.Recipe;

import java.util.List;

/**
 * Created by goandroid on 6/25/18.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>{

    private static final String TAG = "RecipeAdapter";
    private Context mContext;
    private List<Recipe> recipeList;

    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }

    final private ItemClickListener mItemClickListener;

    public RecipeAdapter(Context context, ItemClickListener listener){
        mContext = context;
        mItemClickListener = listener;
    }


    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_recipe_row, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        Recipe recipeItem = recipeList.get(position);

        holder.recipeTextView.setText(recipeItem.getName());

        String ingrident = "Ingredients #" + recipeItem.getIngredients().size();
        holder.ingredientTextView.setText(ingrident);

        String step = "Steps #" + recipeItem.getSteps().size();
        holder.stepTextView.setText(step);

    }

    @Override
    public int getItemCount() {
        return recipeList == null ? 0 : recipeList.size();
    }

    @Nullable
    public Recipe getItem(int position){
        return recipeList.get(position);
    }

    public List<Recipe> getRecipeList() {
        return recipeList;
    }

    public void setRecipeList(List<Recipe> recipeList) {
        this.recipeList = recipeList;

        notifyDataSetChanged();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

        TextView recipeTextView;
        TextView ingredientTextView;
        TextView stepTextView;

        public RecipeViewHolder(View itemView){

            super(itemView);
            recipeTextView = (TextView)itemView.findViewById(R.id.tv_main_recipe);
            ingredientTextView = (TextView)itemView.findViewById(R.id.tv_main_ingredient);
            stepTextView = (TextView)itemView.findViewById(R.id.tv_main_step);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
