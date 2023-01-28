package com.eightmin4mile.goandroid.bakingapp.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.eightmin4mile.goandroid.bakingapp.DetailViewModel;
import com.eightmin4mile.goandroid.bakingapp.R;
import com.eightmin4mile.goandroid.bakingapp.data.Step;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class StepFragment extends Fragment {
    public static final String STEP_ID_ARG = "step";
    public static final String STEP_LIST_ARG = "step_list";
    private static final String TAG = "StepFragment";

    private int stepId;
    private ArrayList<Step> steps;

    public static StepFragment newInstance(int stepId, ArrayList<Step> stepArrayList) {
        StepFragment f = new StepFragment();

        Bundle args = new Bundle();
        args.putInt(STEP_ID_ARG, stepId);
        args.putParcelableArrayList(STEP_LIST_ARG, stepArrayList);
        f.setArguments(args);

        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_step, container, false);

        if (getArguments().containsKey(STEP_ID_ARG)
            && getArguments().containsKey(STEP_LIST_ARG)) {

            stepId = getArguments().getInt(STEP_ID_ARG);
            steps = getArguments().getParcelableArrayList(STEP_LIST_ARG);

            if (stepId >= 0 && stepId < steps.size()) {
                final Step stepInfo = steps.get(stepId);


                TextView textViewStepId = (TextView) view.findViewById(R.id.tv_step_id);
                String strId = "Step #" + stepInfo.getId();
                textViewStepId.setText(strId);

                TextView textViewStepShortDesc = (TextView) view.findViewById(R.id.tv_step_short_desc);
                textViewStepShortDesc.setText(stepInfo.getShortDescription());

                TextView textViewStepDesc = (TextView) view.findViewById(R.id.tv_step_desc);
                textViewStepDesc.setText(stepInfo.getDescription());

                Button buttonVideo = (Button) view.findViewById(R.id.bt_play_video);

                String urlPath = steps.get(stepId).getVideoURL();

                if (urlPath == null || urlPath.isEmpty()) {
                    buttonVideo.setEnabled(false);

                } else {
                    buttonVideo.setEnabled(true);
                }


                buttonVideo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DetailViewModel detailViewModel = new ViewModelProvider(requireActivity())
                            .get(DetailViewModel.class);


                        String videoTag = "video" + stepId;
                        VideoFragment videoFragment = (VideoFragment) getActivity()
                            .getSupportFragmentManager()
                            .findFragmentByTag(videoTag);

                        if (videoFragment == null) {
                            String videoUrlString = steps.get(stepId).getVideoURL();
                            videoFragment = VideoFragment.newInstance(videoUrlString);
                        }

                        FragmentTransaction transaction = getActivity().getSupportFragmentManager()
                            .beginTransaction();

                        int resId;
                        if (detailViewModel.isTwoPane()) {
                            // do nothing
                            Log.d(TAG, "onClick: start video in fragment_video");
                            resId = R.id.fragment_video;

                        } else {
                            // start the video fragment on phone in full screen
                            resId = R.id.fragment_step_video;
                        }

                        transaction.replace(resId, videoFragment, videoTag);
                        transaction.commit();
                    }
                });


                ImageView imageView = (ImageView) view.findViewById(R.id.iv_image);

                //String thumbnailUrl = "https://www.drawingtutorials101.com/drawing-tutorials/Anime-and-Manga/Dragon-Ball-Z/krillin/how-to-draw-Krillin-from-Dragon-Ball-Z-step-0.png";
                String thumbnailUrl = steps.get(stepId).getThumbnailURL();
                if (thumbnailUrl == null || thumbnailUrl.isEmpty()) {
                    Log.d(TAG, "onCreateView: thumbnailUrl string is null or empty");
                } else {

                    Picasso.get()
                        .load(thumbnailUrl)
                        .into(imageView);
                }

            } else {
                Log.d(TAG, "onCreateView: stepId is out of range");
            }

        }

        return view;
    }
}
