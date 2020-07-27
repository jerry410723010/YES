package com.example.video;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;


        import java.io.File;
import java.util.Objects;

import android.app.Activity;
import android.hardware.Camera;
import android.media.MediaRecorder;

        import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class MainActivity extends Activity implements OnClickListener {

    Button record, stop;
    File voidFile;
    MediaRecorder mRecorder;
    SurfaceView sView;
    boolean isRecording = false;

    Camera camera;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        record = (Button) findViewById(R.id.record);
        stop = (Button) findViewById(R.id.stop);
        sView = (SurfaceView) findViewById(R.id.dView);
        check();
        stop.setEnabled(false);

        sView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        sView.getHolder().setFixedSize(320, 280);
        sView.getHolder().setKeepScreenOn(true);
        record.setOnClickListener(this);
        stop.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.record:
                if (!Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    Toast.makeText(this, "SD卡不存在，請插卡！", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    mRecorder = new MediaRecorder();
                    mRecorder.reset();
   /* camera = Camera.open();
    camera.unlock();
    camera.setDisplayOrientation(0);
    mRecorder.setCamera(camera);*/
                    // 建立儲存錄制視訊的視訊檔案
                    File newFile = new File(getExternalFilesDir(null).getAbsolutePath(),"video");
                    if(!newFile.exists()) {
                        boolean check = newFile.mkdir();
                        Log.i("file",check+"");
                    }
                    Log.i("file",newFile.toString());
                    String fileName = System.currentTimeMillis() +".mp4";
                    voidFile = new File(newFile,fileName);
                    if(!voidFile.exists()){
                        voidFile.createNewFile();
                    }

                    Log.i("file",voidFile.toString());
                    if (!voidFile.exists()){
                        Log.i("file",newFile.exists()+"");
                        voidFile.createNewFile();
                    }

                    mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                    mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
                    mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
                    mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
                    mRecorder.setOrientationHint(90);
                    mRecorder.setOutputFile(voidFile.getAbsolutePath());
                    mRecorder.setPreviewDisplay(sView.getHolder().getSurface());
                    mRecorder.prepare();
                    mRecorder.start();
                    record.setEnabled(false);
                    stop.setEnabled(true);
                    isRecording = true;

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.stop:
                if (isRecording) {
                    mRecorder.stop();
                    mRecorder.release();
                    mRecorder = null;
                    record.setEnabled(true);
                    stop.setEnabled(false);
                }
                break;
            default:
                break;
        }
    }
    private void check() {
        if(ContextCompat.checkSelfPermission(Objects.requireNonNull(this), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(Objects.requireNonNull(this),Manifest.permission.READ_EXTERNAL_STORAGE)
                        !=PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(Objects.requireNonNull(this),Manifest.permission.WRITE_EXTERNAL_STORAGE)
                !=PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(Objects.requireNonNull(this),Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS)
                !=PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(Objects.requireNonNull(this),Manifest.permission.RECORD_AUDIO)
                !=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(Objects.requireNonNull(this),new String[]{
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS
            },200);
            return;
        }
    }
}

