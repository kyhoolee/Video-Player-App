package com.example.videoplayer;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.codec.media.FrameGrab;

import java.io.File;

public class DecodeActivity extends AppCompatActivity {
    private int REQUEST_PERMISSION = 1;
    // Video Files
    private static String videoFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/videos/";
//    private static String hdLowVideo = "cats_with_timecode-1920x1080-30fps-baseline-4mbps.mp4";
//    private static String hdHighVideo = "cats_with_timecode-1920x1080-30fps-main-14mbps.mp4";
//    private static String hdHighBase = "cats_with_timecode-1920x1080-30fps-baseline-14mbps.mp4";
//    private static String lowVideo = "cats_with_timecode-640x320-30fps-baseline-4mbps.mp4";
//    private static String hdHigh24 = "cats_with_timecode-1920x1080-24fps-baseline-14mpbs.mp4";

//    private static String SAMPLE = videoFolder + lowVideo;

    FrameGrab grab = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decode);

        if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)){


            if((ActivityCompat.shouldShowRequestPermissionRationale(DecodeActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE))){
            }
            else{
                ActivityCompat.requestPermissions(DecodeActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSION);
            }

        } else {
            showVideoChooserDialog();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_PERMISSION){

            if(grantResults.length>0 && grantResults[0]  == PackageManager.PERMISSION_GRANTED){

                showVideoChooserDialog();

            }
            else{
                Toast.makeText(this, "Please Allow the Permission", Toast.LENGTH_SHORT).show();
            }



        }
    }

    private void showVideo(String videoPath) {

    }


    private void decodeVideo(String videoPath) {
        Log.i("DecodeVideo", "Path:: " + videoPath);
        int frame = 1750;
        int frameEnd = 1900;
        int frameMiddle= frameEnd/2;

        // Create Decoder for File
        // Re-run if using new source
        grab = new FrameGrab();
        grab.setSource(videoPath);
        grab.init();

        long frameRate = grab.getFrameRate();
        long duration = grab.getDuration();
        Log.i("DecodeVideo", "Path:: " + videoPath + " frameRate :: " + frameRate + " duration :: " + duration);
        // Get Single Frame
//        grab.seekToTime(10);
//        grab.getFrameAtTime(10);
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


    // function to show a dialog to select video file
    private void showVideoChooserDialog() {

        final CharSequence[] options = { "From Camera", "From Gallery",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Upload!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("From Camera")) {
                    Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                    File f = new File(Environment
                            .getExternalStorageDirectory(), "temp.mp4");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                } else if (options[item].equals("From Gallery")) {
                    // Intent intent = new
                    // Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    // intent.setType("image/*");
                    // startActivityForResult(Intent.createChooser(intent,
                    // "Select File"),2);

                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("video/*");
                    startActivityForResult(intent, 2);
                    //
                    // Intent photoPickerIntent = new
                    // Intent(Intent.ACTION_PICK);
                    // photoPickerIntent.setType("image/*");
                    // startActivityForResult(photoPickerIntent, 2);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }

    // on activity result to get file from intent data
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory()
                        .toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.mp4")) {
                        f = temp;
                        break;
                    }
                }
                String videoPath = f.getAbsolutePath();
                Log.d("SelectedVideoPathCamera", videoPath);
                decodeVideo(videoPath);

            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Video.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage, filePath,
                        null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String videoPath = c.getString(columnIndex);
                c.close();
                //videoPath = videoPath.replace("/storage/emulated/0", "/mnt/sdcard");
                Log.d("SelectedVideoPath", videoPath);
                decodeVideo(videoPath);
            }
        }
    }

}
