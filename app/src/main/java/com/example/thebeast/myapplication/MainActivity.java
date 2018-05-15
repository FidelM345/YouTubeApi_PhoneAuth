package com.example.thebeast.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends YouTubeBaseActivity {
    FirebaseAuth mAuth;
    private Button signout;
    YouTubePlayerView youTubePlayerView;
    YouTubePlayer.OnInitializedListener onInitializedListener;
    Button player,pop_video;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance();

        signout=findViewById(R.id.main_signout);
        youTubePlayerView=findViewById(R.id.player_view);
        player=findViewById(R.id.player_btn);

        pop_video=findViewById(R.id.pop_video);




        onInitializedListener=new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

                youTubePlayer.loadVideo("W4hTJybfU7s");
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };


        player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               youTubePlayerView.initialize(YoutubeApiKey_Holder.getApiKey(),onInitializedListener);

            }
        });


        pop_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = YouTubeStandalonePlayer.createVideoIntent(MainActivity.this, YoutubeApiKey_Holder.getApiKey(),"W4hTJybfU7s",100,true,true);
                startActivity(intent);
            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent=new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
            }
        });





            }



    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser()==null){
            Intent intent=new Intent(this,Login.class);
            startActivity(intent);
        }



    }
}
