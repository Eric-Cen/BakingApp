package com.eightmin4mile.goandroid.bakingapp.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eightmin4mile.goandroid.bakingapp.DetailViewModel;
import com.eightmin4mile.goandroid.bakingapp.R;
import com.eightmin4mile.goandroid.bakingapp.data.Ingredient;

import java.util.List;


public class IngredientFragment extends Fragment {


    private static final String TAG = "IngredientFragment";
    private DetailViewModel detailViewModel;

    RecyclerView mRecylerView;
    RecyclerView.LayoutManager layoutManager;
    IngredientAdapter ingredientAdapter;


    public static IngredientFragment newInstance() {
        IngredientFragment f = new IngredientFragment();

        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        detailViewModel = new ViewModelProvider(requireActivity()).get(DetailViewModel.class);

        View view = null;

        List<Ingredient> newIngredients = detailViewModel.getRecipe().getValue()
            .getIngredients();

        if (newIngredients != null && newIngredients.size() > 0) {
            view = inflater.inflate(R.layout.fragment_ingredients,
                container,
                false);

            mRecylerView = (RecyclerView) view.findViewById(R.id.recyclerView_ingredients);
            layoutManager = new LinearLayoutManager(this.getActivity());
            mRecylerView.setLayoutManager(layoutManager);

            ingredientAdapter = new IngredientAdapter(this.getActivity());
            ingredientAdapter.setIngredients(newIngredients);

            mRecylerView.setAdapter(ingredientAdapter);


        } else {
            // do nothing with an empty ingredient list
            Log.d(TAG, "onCreateView: empty ingredient list");
        }

        return view;
    }
}
