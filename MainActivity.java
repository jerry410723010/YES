package com.example.video;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
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
    // 系統視訊檔案
    File viodFile;
    MediaRecorder mRecorder;
    // 顯示視訊的SurfaceView
    SurfaceView sView;
    // 記錄是否正在進行錄製
    boolean isRecording = false;

    Camera camera;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        check() ;


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        record = (Button) findViewById(R.id.record);
        stop = (Button) findViewById(R.id.stop);
        sView = (SurfaceView) findViewById(R.id.dView);
        // stop按鈕不可用
        stop.setEnabled(false);

        // 設定Surface不需要維護自己的緩衝區
        sView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        // 設定解析度
        sView.getHolder().setFixedSize(320, 280);
        // 設定該元件不會讓螢幕自動關閉
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
                    // 建立MediaPlayer物件
                    mRecorder = new MediaRecorder();
                    mRecorder.reset();
   /* camera = Camera.open();
    camera.unlock();
    camera.setDisplayOrientation(0);
    mRecorder.setCamera(camera);*/
                    // 建立儲存錄制視訊的視訊檔案
                    viodFile = new File(Environment.getExternalStorageDirectory().getCanonicalFile(),"/myvideo.mp4");

                    if (!viodFile.exists())
                        viodFile.createNewFile();

                    // 設定從麥克風採集聲音
                    mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    // 設定從攝像頭採集影象
                    mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                    // 設定視訊、音訊的輸出格式
                    mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
                    // 設定音訊的編碼格式、
                    mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
                    // 設定影象編碼格式
                    mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
                    mRecorder.setOrientationHint(90);
                    //mRecorder.setVideoSize(320, 280);
                    // mRecorder.setVideoFrameRate(5);
                    mRecorder.setOutputFile(viodFile.getAbsolutePath());
                    // 指定SurfaceView來預覽視訊
                    mRecorder.setPreviewDisplay(sView.getHolder().getSurface());
                    mRecorder.prepare();
                    // 開始錄製
                    mRecorder.start();
                    // 讓record按鈕不可用
                    record.setEnabled(false);
                    // 讓stop按鈕可用
                    stop.setEnabled(true);
                    isRecording = true;

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.stop:
                // 如果正在錄製
                if (isRecording) {
                    // 停止錄製
                    mRecorder.stop();
                    // 釋放資源
                    mRecorder.release();
                    mRecorder = null;
                    // 讓record按鈕可用
                    record.setEnabled(true);
                    // 讓stop按鈕不可用
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
                        !=PackageManager.PERMISSION_GRANTED
        ||ContextCompat.checkSelfPermission(Objects.requireNonNull(this),Manifest.permission.WRITE_EXTERNAL_STORAGE)
                !=PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(Objects.requireNonNull(this),Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS)
                !=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(Objects.requireNonNull(this),new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS
            },200);
            return;
        }



    }
}

