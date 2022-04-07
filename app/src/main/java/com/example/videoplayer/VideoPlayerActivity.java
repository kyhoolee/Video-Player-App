package com.example.videoplayer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import org.codec.media.FrameGrab;

public class VideoPlayerActivity extends AppCompatActivity {

    VideoView videoView;
    int position = -1;
    String videoPath = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        videoView = (VideoView)findViewById(R.id.myPlayer);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        position = getIntent().getIntExtra("position",-1);
        videoPath = getIntent().getStringExtra("videoPath");
        videoPath = videoPath.replace("/mnt/sdcard", "/storage/emulated/0" );
        Log.i("VideoPlayer", "position : " + position + " videoPath : " + videoPath);
        getSupportActionBar().hide();

        decodeVideo(videoPath);

        playerVideo();

    }

    private void decodeVideo(String videoPath) {
        Log.i("DecodeVideo", "Path:: " + videoPath);
        long frame = 1750;
        long frameEnd = 1900;
        long frameMiddle= frameEnd/2;

        // Create Decoder for File
        // Re-run if using new source
        FrameGrab grab = null;
        grab = new FrameGrab();
        grab.setSource(videoPath);
        grab.init();

        long frameRate = grab.getFrameRate();
        long duration = grab.getDuration();
        frame = 0;
        frameEnd = duration / frameRate;
        Log.i("DecodeVideo", "Path:: " + videoPath + " frameRate :: " + frameRate + " duration :: " + duration);
        // Get Single Frame
//        grab.seekToTime(10);
//        grab.getFrameAtTime(10);
        // /mnt/sdcard/Download/275469753_129965459554769_8240898377705244545_n.mp4
        // /mnt/sdcard/Download/275469753_129965459554769_8240898377705244545_n.mp4
        // /mnt/sdcard/Download/275469753_129965459554769_8240898377705244545_n.mp4
        // /storage/emulated/0/Download/275469753_129965459554769_8240898377705244545_n.mp4
//        grab.saveBitmap(videoFolder + "time.jpg");

        // Get Frame Sequence
//        grab.seekToFrame(frame);
//
//        for(int i = frame; i <= frameEnd; i++) {
//            if(grab.isEOS()){ break; }
//            grab.getFrameAt(frame);
//            grab.saveBitmap(videoFolder + "test/frame"+i+".jpg");
//        }

        // Get Frame from the back, need to resetDecoder
//        grab.flushDecoder();
//        grab.seekToFrame(frameMiddle);
//        grab.getFrameAt(frameMiddle);
//        grab.saveBitmap(videoFolder + "test03.jpg");
//
//        // Release if framegrab is not needed anymore or creating new FrameGrab
//        grab.release();
//
//        // Create new FrameGrab with new Source
//        grab = new FrameGrab();
//        grab.setSource(videoFolder + hdHighVideo);
//        grab.setTargetSize(640, 360);
//        grab.init();
//
//        // Get Single Frame with another Image Size
//        grab.seekToFrame(frame);
//        grab.getFrameAt(frame);
//        Bitmap img = grab.getBitmap();
    }

    private void playerVideo() {


        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);

        videoView.setMediaController(mediaController);
        videoView.setVideoPath(videoPath);
        videoView.requestFocus();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                            @Override
                                            public void onPrepared(MediaPlayer mp) {

                                                videoView.start();

                                            }
                                        }
        );

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoView.setVideoPath(String.valueOf(MainActivity.fileArrayList.get(position = position+1)));
                videoView.start();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        videoView.stopPlayback();
    }
}
