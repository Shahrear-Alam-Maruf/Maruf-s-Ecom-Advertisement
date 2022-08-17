package com.example.videoupload;

import android.app.Application;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;


public class myviewholder extends RecyclerView.ViewHolder {

    SimpleExoPlayer simpleExoPlayer;
    VideoView simpleExoPlayerView;
    TextView vtitleview;
    ImageView like_btn;
    TextView like_text;
    ImageView comment_btn;
    DatabaseReference likereference;
    Context context;

    public myviewholder(@NonNull View itemView) {
        super(itemView);
        vtitleview = itemView.findViewById(R.id.s_vtitle);
        like_text = itemView.findViewById(R.id.s_like_text);
        like_btn = itemView.findViewById(R.id.s_like_btn);
        simpleExoPlayerView=itemView.findViewById(R.id.s_exoplayerview);
        comment_btn = itemView.findViewById(R.id.s_comment_btn);
    }
    void prepareexoplayer(Application application, String videotitle, String videourl)
    {
        try
        {
            vtitleview.setText(videotitle);

            /*BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter.Builder();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            simpleExoPlayer =(SimpleExoPlayer) ExoPlayerFactory.newSimpleInstance(application,trackSelector);

            Uri videoURI = Uri.parse(videourl);

            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(videoURI, dataSourceFactory, extractorsFactory, null, null);

            simpleExoPlayerView.setPlayer(simpleExoPlayer);
            simpleExoPlayer.prepare(mediaSource);
            simpleExoPlayer.setPlayWhenReady(false);*/
           /* Uri videoURI = Uri.parse(videourl);
            SimpleExoPlayer simpleExoPlayer = new SimpleExoPlayer.Builder(context.getApplicationContext()).build();
            simpleExoPlayerView.setPlayer(simpleExoPlayer);
            MediaItem mediaItem = MediaItem.fromUri(videoURI);
            simpleExoPlayer.addMediaItems(Collections.singletonList(mediaItem));
            simpleExoPlayer.prepare();
            simpleExoPlayer.setPlayWhenReady(false);*/

            Uri uri = Uri.parse(videourl);
            // initiate a video view
            simpleExoPlayerView.setVideoURI(uri);
            simpleExoPlayerView.start();
            simpleExoPlayerView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                }
            });
        }catch (Exception ex)
        {
            Log.d("Explayer Creshed", ex.getMessage().toString());
        }
    }

    public void getlikebuttonstatus(final String postkey, final String userid)
    {

        likereference = FirebaseDatabase.getInstance().getReference("likes");
        likereference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(postkey).hasChild(userid))
                {
                    int likecount=(int)snapshot.child(postkey).getChildrenCount();
                    like_text.setText(likecount+" likes");
                    like_btn.setImageResource(R.drawable.love_);
                }
                else
                {
                    int likecount=(int)snapshot.child(postkey).getChildrenCount();
                    like_text.setText(likecount+" likes");
                    like_btn.setImageResource(R.drawable.love_);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
