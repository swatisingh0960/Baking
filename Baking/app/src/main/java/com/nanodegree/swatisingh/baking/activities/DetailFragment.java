package com.nanodegree.swatisingh.baking.activities;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.nanodegree.swatisingh.baking.R;
import com.nanodegree.swatisingh.baking.model.Recipe;

public class DetailFragment extends Fragment implements ExoPlayer.EventListener {
    int position;
    private static final String TAG = DetailFragment.class.getSimpleName();
    private SimpleExoPlayer simpleExoPlayer;
    private SimpleExoPlayerView simpleExoPlayerView;
    private static MediaSessionCompat mediaSessionCompat;
    private PlaybackStateCompat.Builder stateBuilder;
    private long playerPosition;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    Recipe recipe;
    public DetailFragment(){ }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState != null)
        playerPosition = savedInstanceState.getLong("playerPosition");

        final View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        simpleExoPlayerView = rootView.findViewById(R.id.mediaViewer);
        final TextView textView = rootView.findViewById(R.id.desc_textView);
        Button prevBtn = rootView.findViewById(R.id.prevButton);
        Button nextBtn = rootView.findViewById(R.id.nextButton);
        recipe = getActivity().getIntent().getParcelableExtra("parcel_data");

        position = 0;
        if (getArguments() != null)
            position = getArguments().getInt("position", 0);
        if (savedInstanceState!=null && savedInstanceState.containsKey("STEP_POS"))
            position = savedInstanceState.getInt("STEP_POS");
        Log.e("POSITION",String.valueOf(position));
        textView.setText(recipe.instructions.get(position));

        simpleExoPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher_foreground));
        initializeMediaSession();

        if (!recipe.media.get(position).equals(""))
            initializePlayer(Uri.parse(recipe.media.get(position)));
        else
            simpleExoPlayerView.setVisibility(View.GONE);



        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position != 0) {
                    position -= 1;
                    textView.setText(recipe.instructions.get(position));

                    if (simpleExoPlayerView == null){
                        if (!recipe.media.get(position).equals("")) {
                            simpleExoPlayerView.setVisibility(View.VISIBLE);
                            initializePlayer(Uri.parse(recipe.media.get(position)));
                        } else
                            simpleExoPlayerView.setVisibility(View.GONE);
                    } else {
                        if (!recipe.media.get(position).equals("")){
                            releasePlayer();
                            simpleExoPlayerView.setVisibility(View.VISIBLE);
                            initializePlayer(Uri.parse(recipe.media.get(position)));
                        } else {
                            simpleExoPlayerView.setVisibility(View.GONE);
                            releasePlayer();
                        }
                    }
                } else {
                    position = recipe.instructions.size() - 1;
                    textView.setText(recipe.instructions.get(recipe.instructions.size() - 1));

                    if (simpleExoPlayer == null){
                        if (!recipe.media.get(position).equals(""))
                            initializePlayer(Uri.parse(recipe.media.get(position)));
                        else
                            simpleExoPlayerView.setVisibility(View.GONE);
                    } else {
                        if (!recipe.media.get(position).equals("")){
                            releasePlayer();
                            initializePlayer(Uri.parse(recipe.media.get(position)));
                        } else {
                            simpleExoPlayerView.setVisibility(View.GONE);
                            releasePlayer();
                        }
                    }
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position != recipe.instructions.size() - 1){
                    position += 1;
                    textView.setText(recipe.instructions.get(position));

                    if (simpleExoPlayer == null){
                        if (!recipe.media.get(position).equals("")){
                            simpleExoPlayerView.setVisibility(View.VISIBLE);
                            initializePlayer(Uri.parse(recipe.media.get(position)));
                        } else simpleExoPlayerView.setVisibility(View.GONE);
                    } else {
                        if (!recipe.media.get(position).equals("")){
                            releasePlayer();
                            simpleExoPlayerView.setVisibility(View.VISIBLE);
                            initializePlayer(Uri.parse(recipe.media.get(position)));
                        } else {
                            simpleExoPlayerView.setVisibility(View.GONE);
                            releasePlayer();
                        }
                    }
                }
            }
        });
        return rootView;
    }

    private void initializeMediaSession() {
        mediaSessionCompat = new MediaSessionCompat(getContext(), TAG);
        mediaSessionCompat.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mediaSessionCompat.setMediaButtonReceiver(null);
        stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);
        mediaSessionCompat.setPlaybackState(stateBuilder.build());
        mediaSessionCompat.setCallback(new MySessionCallback());
        mediaSessionCompat.setActive(true);
    }


    private void initializePlayer(Uri parse) {
        if (simpleExoPlayer == null){
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            simpleExoPlayerView.setPlayer(simpleExoPlayer);
            simpleExoPlayer.addListener(this);

            String userAgent = Util.getUserAgent(getContext(), "baking");
            MediaSource mediaSource = new ExtractorMediaSource(parse, new DefaultDataSourceFactory(
                    getContext(), userAgent),new DefaultExtractorsFactory(), null, null);
            simpleExoPlayer.prepare(mediaSource);
            simpleExoPlayer.setPlayWhenReady(true);
        }
        simpleExoPlayer.seekTo(playerPosition);
    }


    private void releasePlayer(){
        if(simpleExoPlayer != null) {
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
        }
        simpleExoPlayer = null;

//        simpleExoPlayer.stop();
//        simpleExoPlayer.release();
//        simpleExoPlayer.setPlayWhenReady(false);
//        if(simpleExoPlayer!=null){
//            simpleExoPlayer.stop();
//            simpleExoPlayer.release();
//        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (simpleExoPlayer!= null){
            releasePlayer();
            mediaSessionCompat.setActive(false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <=25 && simpleExoPlayer!=null){
            simpleExoPlayer.setPlayWhenReady(true);
            Bundle bundle = new Bundle();
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 25 &&simpleExoPlayer!=null){
            simpleExoPlayer.setPlayWhenReady(true);
            Bundle bundle = new Bundle();
            onSaveInstanceState(bundle);
            releasePlayer();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        final Recipe recipe = getActivity().getIntent().getParcelableExtra("parcel_data");
        if (Util.SDK_INT > 25 &&simpleExoPlayer!=null){
            simpleExoPlayerView.setVisibility(View.VISIBLE);
            initializePlayer(Uri.parse(recipe.media.get(position)));
            simpleExoPlayer.getPlayWhenReady();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
      /*  final Recipe recipe = getActivity().getIntent().getParcelableExtra("parcel_data");
        if (Util.SDK_INT <= 25 || simpleExoPlayer == null && !recipe.media.get(position).equals("")){
            simpleExoPlayerView.setVisibility(View.VISIBLE);
            initializePlayer(Uri.parse(recipe.media.get(position)));
            simpleExoPlayer.getPlayWhenReady();
        }*/
    }




    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
         /*  if (simpleExoPlayer!=null){
                outState.putLong("playerPosition", simpleExoPlayer.getContentPosition());
                if (this.recipe !=null){
                    int pos = Integer.valueOf(recipe.instructions.get(position).split(" ")[0]);
//                    Log.e("onSaveInstanceState:POS",String.valueOf(pos));
                    outState.putInt("STEP_POS",pos );}
                super.onSaveInstanceState(outState);
        } else
                 {
               if (simpleExoPlayer == null){
//                   outState.putLong("playerPosition", simpleExoPlayer.getContentPosition());
//                   if (this.recipe == null){
                       int pos = Integer.valueOf(recipe.instructions.get(position).split(" ")[0]);
                       outState.putInt("STEP_POS",pos );
//                   }
                   super.onSaveInstanceState(outState);
                   }
               } */
           outState.putInt("STEP_POS", position);
            if (simpleExoPlayer != null) {
            outState.putLong("playerPosition", simpleExoPlayer.getContentPosition());
             }
            super.onSaveInstanceState(outState);
           }


    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady){
            stateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    simpleExoPlayer.getCurrentPosition(),1f);
        } else if ((playbackState == ExoPlayer.STATE_READY)) {
            stateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    simpleExoPlayer.getCurrentPosition(),1f);
        }
        mediaSessionCompat.setPlaybackState(stateBuilder.build());
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }

    private class MySessionCallback extends MediaSessionCompat.Callback {

        @Override
        public void onPlay() {
            simpleExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            simpleExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            simpleExoPlayer.seekTo(0);
        }
    }
}
