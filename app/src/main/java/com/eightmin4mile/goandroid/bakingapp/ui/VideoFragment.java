package com.eightmin4mile.goandroid.bakingapp.ui;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import com.eightmin4mile.goandroid.bakingapp.R;

public class VideoFragment extends Fragment {
    public static String URL_ARG = "url_path";

    private static final String TAG = "VideoFragment";

    private final String VIDEO_POSITION = "position";
    private final String VIDEO_URL = "video_url";
    private final String VIDEO_IS_PLAYING = "is_playing";

    private ExoPlayer exoPlayer;
    private PlayerView playerView;

    private boolean playWhenReady = true;
    private int currentItem = 0;
    private long playbackPosition = 0L;

    private long position;
    private boolean isPlaying;


    private String stringUrl;

    public static VideoFragment newInstance(String urlString) {
        VideoFragment f = new VideoFragment();

        Bundle args = new Bundle();
        args.putString(URL_ARG, urlString);
        f.setArguments(args);

        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            position = 0;
            isPlaying = true;
        } else {
            position = savedInstanceState.getLong(VIDEO_POSITION);
            stringUrl = savedInstanceState.getString(VIDEO_URL);
            isPlaying = savedInstanceState.getBoolean(VIDEO_IS_PLAYING);
        }

        View view = inflater.inflate(R.layout.fragment_video, container, false);

        playerView = (PlayerView) view.findViewById(R.id.video_view);


        if (getArguments().containsKey(URL_ARG)) {
            stringUrl = getArguments().getString(URL_ARG);
        }

        if (stringUrl == null || stringUrl.isEmpty()) {
            Log.d(TAG, "onCreateView: stringUrl = null or \"\"");
        } else {
            initializePlayer();
        }
        return view;
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if (exoPlayer != null) {
            playbackPosition = exoPlayer.getCurrentPosition();
            currentItem = exoPlayer.getCurrentMediaItemIndex();
            playWhenReady = exoPlayer.getPlayWhenReady();
            exoPlayer.release();
        }
        exoPlayer = null;
    }

    @Override
    public void onPause() {
        super.onPause();

        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if (Util.SDK_INT <= 23 || exoPlayer == null) {
            initializePlayer();
        }
    }

    private void initializePlayer() {
        exoPlayer = new ExoPlayer.Builder(requireContext()).build();
        playerView.setPlayer(exoPlayer);
        MediaItem mediaItem = MediaItem.fromUri(Uri.parse(stringUrl));
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.setPlayWhenReady(playWhenReady);
        exoPlayer.seekTo(currentItem, playbackPosition);
        exoPlayer.prepare();
    }

    private void hideSystemUi() {
        WindowCompat.setDecorFitsSystemWindows(requireActivity().getWindow(), false);
        WindowInsetsControllerCompat insetsControllerCompat = new WindowInsetsControllerCompat(requireActivity().getWindow(), playerView);
        insetsControllerCompat.hide(WindowInsetsCompat.Type.systemBars());
        insetsControllerCompat.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
    }
}
