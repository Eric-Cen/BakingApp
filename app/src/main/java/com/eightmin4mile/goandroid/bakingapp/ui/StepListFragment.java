package com.eightmin4mile.goandroid.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eightmin4mile.goandroid.bakingapp.DetailViewModel;
import com.eightmin4mile.goandroid.bakingapp.R;
import com.eightmin4mile.goandroid.bakingapp.Utility;
import com.eightmin4mile.goandroid.bakingapp.data.Step;

import java.util.ArrayList;
import java.util.List;

public class StepListFragment extends Fragment
    implements StepAdapter.StepClickListener {

    private static final String TAG = "StepListFragment";
    public static String STEP_LIST_ARG = "step_list";

    private DetailViewModel detailViewModel;

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    StepAdapter stepAdapter;

    public static StepListFragment newInstance() {
        StepListFragment f = new StepListFragment();
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_step_list, container, false);

        detailViewModel = new ViewModelProvider(requireActivity()).get(DetailViewModel.class);

        Button ingredientsButton = (Button) view.findViewById(R.id.bt_ingredients);
        ingredientsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                IngredientFragment ingredientFragment = IngredientFragment.newInstance();

                int layoutId;
                if (getActivity().findViewById(R.id.fragment_video) != null) {
                    layoutId = R.id.fragment_video;
                } else {
                    layoutId = R.id.fragment_container;
                }

                detailViewModel.setIngredientsDisplayed(true);
                fragmentManager.beginTransaction()
                    .replace(layoutId, ingredientFragment, "ingredients")
                    .addToBackStack(null)
                    .commit();
            }
        });

        List<Step> newList = detailViewModel.getRecipe().getValue().getSteps();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_steps);
        layoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        stepAdapter = new StepAdapter(this.getActivity(), StepListFragment.this);
        stepAdapter.setStepList(newList);

        mRecyclerView.setAdapter(stepAdapter);
        return view;
    }

    @Override
    public void onItemClickListener(int itemId) {
        ArrayList<Step> steps = Utility.fromListtoArrayList(
            detailViewModel.getRecipe().getValue().getSteps());

        if (detailViewModel.isTwoPane()) {//tablet

            detailViewModel.setCurrentStepId(itemId);

            String stepTag = "step" + itemId;

            StepFragment stepFragment = (StepFragment) getActivity().getSupportFragmentManager()
                .findFragmentByTag(stepTag);

            if (stepFragment == null) {
                stepFragment = StepFragment.newInstance(itemId, steps);
            }


            String videoTag = "video" + itemId;
            VideoFragment videoFragment =
                (VideoFragment) getActivity().getSupportFragmentManager()
                    .findFragmentByTag(videoTag);

            if (videoFragment == null) {
                String urlPath = steps.get(itemId).getVideoURL();
                videoFragment = VideoFragment.newInstance(urlPath);
            }


            FragmentTransaction transaction = getActivity().getSupportFragmentManager()
                .beginTransaction();
            transaction.replace(R.id.fragment_step_instruction, stepFragment, stepTag);
            transaction.replace(R.id.fragment_video, videoFragment, videoTag);
            transaction.commit();
        } else { // phone

            // start StepActivity
            Intent intent = new Intent(getActivity(),
                StepActivity.class);
            intent.putExtra(
                StepActivity.NAME_EXTRA, detailViewModel.getRecipe().getValue().getName());
            intent.putExtra(StepActivity.STEP_ID_EXTRA, itemId);
            intent.putParcelableArrayListExtra(StepActivity.STEP_LIST_EXTRA, steps);
            startActivity(intent);
        }
    }
}
