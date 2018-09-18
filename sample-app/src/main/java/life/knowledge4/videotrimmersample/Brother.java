package life.knowledge4.videotrimmersample;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Brother extends AppCompatActivity
        implements CameraBridgeViewBase.CvCameraViewListener2 {

    public final String TAG = "KJH";
    private NetworkService networkService;

    String clientid;

    public class Version {
        String version;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
    }

    public class Thumbnail{
        byte[] bmp;
        String user_id;
        String success;
        int max_id;
        int duration;
        int total_sec;

        public int getTotal_sec() {
            return total_sec;
        }

        public void setTotal_sec(int total_sec) {
            this.total_sec = total_sec;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {

            this.user_id = user_id;
        }

        public int getMax_id() {
            return max_id;

        }

        public void setMax_id(int max_id) {
            this.max_id = max_id;
        }

        public String getSuccess() {
            return success;
        }

        public void setSuccess(String success) {
            this.success = success;
        }

        public byte[] getBmp() {
            return bmp;
        }

        public void setBmp(byte[] bmp) {
            this.bmp = bmp;
        }
    }

    public class Video_frame{
        /*
        byte[][] frame;

        public byte[][] getFrame() {
            return frame;
        }

        public void setFrame(byte[][] frame) {
            this.frame = frame;
        }
        */
        String[] frame;

        public String[] getFrame() {
            return frame;
        }

        public void setFrame(String[] frame) {
            this.frame = frame;
        }
    }

    public class User {
        String user_id;
        String user_psw;

        public String getUserId() {
            return user_id;
        }

        public String getUserPsw() {
            return user_psw;
        }

        public void setUser(String user_id, String user_psw) {
            this.user_id = user_id;
            this.user_psw = user_psw;
        }
    }

    public class ResponseGet {
        public final String success;
        public ResponseGet(String success) {
            this.success = success;
        }

        public String getSuccess() {
            return success;
        }
    }

    public class Index {
        public int idx;
        int thumbnail_id;

        public Index(int idx,int thumbnail_id) {
            this.thumbnail_id = thumbnail_id;
            this.idx = idx;
        }

        public int getThumbnail_id() {
            return thumbnail_id;
        }

        public void setThumbnail_id(int thumbnail_id) {
            this.thumbnail_id = thumbnail_id;
        }

        public void setIdx(int idx) {
            this.idx = idx;
        }

        public int getIdx() {
            return idx;
        }
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2){
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }


    public class MyRunnable implements Runnable {

        int idx;
        int thumbnail_id;

        public int getIdx() {
            return idx;
        }

        public void setIdx(int idx) {
            this.idx = idx;
        }

        public int getThumbnail_id() {
            return thumbnail_id;
        }

        public void setThumbnail_id(int thumbnail_id) {
            this.thumbnail_id = thumbnail_id;
        }

        public MyRunnable(int idx) {
            this.idx = idx;
        }

        public void run() {
            //Call<Video_frame> frameCall = networkService.get_frame();
            Index idx_tmp = new Index(idx,thumbnail_id);
            Call<Video_frame> frameCall = networkService.post_frame(idx_tmp);
            frameCall.enqueue(new Callback<Video_frame>() {
                @Override
                public void onResponse(Call<Video_frame> call, Response<Video_frame> response) {
                    if (response.isSuccessful()) {
                        String result = new Gson().toJson(response.body()).toString();
                        Gson gson = new Gson();
                        Video_frame resp = gson.fromJson(result, Video_frame.class);
                        //byte[][] resp_result = resp.getFrame();
                        String[] resp_result = resp.getFrame();
                        //Log.i(ApplicationController.TAG, "HIHI Message : idx = " + resp_result[0]);
                        //byte[] byteArr = resp_result[0].getBytes();
                        //Log.i(ApplicationController.TAG, "HIHI Message : idx = " + Integer.toString(byteArr.length));

                        Log.i(ApplicationController.TAG, "HIHI Message : idx = " + Integer.toString(idx));
                        Log.i(ApplicationController.TAG, "HIHI Message : resp_result.length = " + Integer.toString(resp_result.length));
                        for (int i = 0; i < resp_result.length; i++) {
                            //byte[] byteArr = resp_result[i].getBytes();
                            //String resp_result_sub = resp_result[i].substring(resp_result[i].indexOf("\\"),resp_result[i].length()-1);
                            /*
                            Log.i(ApplicationController.TAG, "HIHI Message : resp_result = " + resp_result[i]);
                            Log.i(ApplicationController.TAG, "HIHI Message : resp_result = " + Integer.toString(resp_result[i].length()));
                            */
                            byte[] bytes = hexStringToByteArray(resp_result[i]);
                            //Mat mat = Imgcodecs.imdecode(new MatOfByte(bytes), Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);

                            //video_mat_bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            /*
                            Mat video_mat_tmp = new Mat(video_size, video_size, CvType.CV_8UC4);
                            video_mat_tmp.put(0, 0, bytes);
                            */
                            Mat video_mat_tmp = new Mat(video_size, video_size, CvType.CV_8UC4);

                            Mat video_mat_decode = Imgcodecs.imdecode(new MatOfByte(bytes), Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
                            //Log.i(ApplicationController.TAG, "HIHI Message : channels = " + Integer.toString(video_mat_decode.channels()));
                            byte[] return_buff_tmp = new byte[(int) (video_mat_decode.total() * video_mat_decode.channels())];
                            byte[] return_buff = new byte[(int) (video_mat_decode.total() * 4)];
                            //Log.i(ApplicationController.TAG, "HIHI Message : channels = " + Integer.toString(return_buff.length));
                            video_mat_decode.get(0, 0, return_buff_tmp);
                            int tmp_idx = 0;
                            for(int n=0;n<return_buff.length;n++){
                                if(n%4 == 3){
                                    return_buff[n] = -1;
                                }
                                else {
                                    return_buff[n] = return_buff_tmp[tmp_idx++];
                                }
                            }
                            //Log.i(ApplicationController.TAG, "HIHI Message : channels = " + Arrays.toString(return_buff));
                            video_mat_tmp.put(0, 0, return_buff);

                            /*
                            if(idx <= 54) {
                                video_mat_arr[i + idx] = video_mat_tmp;
                            }
                            else if(idx <= 114){
                                //video_mat_arr2[i + (idx-60)] = video_mat_tmp;
                                video_mat_arr[i + idx] = video_mat_tmp;
                            }

                            else if(idx <= 174) {
                                //video_mat_arr3[i + (idx-120)] = video_mat_tmp;
                                video_mat_arr2[i + (idx - 120)] = video_mat_tmp;
                            }
                            else if(idx <= 234){
                                //video_mat_arr4[i + (idx-180)] = video_mat_tmp;
                                video_mat_arr2[i + (idx-120)] = video_mat_tmp;
                            }
                            */
                            /*
                            if(idx <= 110) {
                                video_mat_arr[i + idx] = video_mat_tmp;
                            }
                            //0 10 20 30 40 50 60 70 80 90 100 110
                            //120 130 140 150 160 170
                            else if(idx <= 230) {
                                //video_mat_arr3[i + (idx-120)] = video_mat_tmp;
                                video_mat_arr2[i + (idx - 120)] = video_mat_tmp;
                            }
                            */
                            /*
                            if(idx <= 165) {
                                video_mat_arr[i + idx] = video_mat_tmp;
                            }

                            else if(idx <= 345) {
                                //video_mat_arr3[i + (idx-120)] = video_mat_tmp;
                                video_mat_arr2[i + (idx - 180)] = video_mat_tmp;
                            }
                            */
                            /*
                            if(idx <= 170) {
                                video_mat_arr[i + idx] = video_mat_tmp;
                            }

                            else if(idx <= 350) {
                                //video_mat_arr3[i + (idx-120)] = video_mat_tmp;
                                video_mat_arr2[i + (idx - 180)] = video_mat_tmp;
                            }
                            */
                            if(idx <= 360) {
                                video_mat_arr[i + idx] = video_mat_tmp;
                            }
                            else if(idx <= 810) {
                                //video_mat_arr3[i + (idx-120)] = video_mat_tmp;
                                video_mat_arr2[i + (idx - 450)] = video_mat_tmp;
                            }
                            frame_check[i+idx] = 1;

                        }
                        Log.i(ApplicationController.TAG, "HIHI Message : idx = " + Integer.toString(idx) + "finish");

                        int tif_idx = (int)idx/90;
                        thread_is_finish[tif_idx] = 1;
                        check++;

                    } else {
                        int StatusCode = response.code();
                        Log.i(ApplicationController.TAG, "HIHI Code : " + StatusCode);
                    }
                }

                @Override
                public void onFailure(Call<Video_frame> call, Throwable t) {
                    Log.i(ApplicationController.TAG, "Fail Message : " + t.getMessage());
                }
            });
        }
    }


    //private static final String TAG = "opencv";
    private CameraBridgeViewBase mOpenCvCameraView;
    private Mat matInput;
    private Mat matResult;
    private Mat thumbnail;
    private int thumbnail_f;
    private Mat mFrame;
    private MediaMetadataRetriever mmr;
    private Bitmap[] bmp_buffer;
    private int buffer_idx = 0;
    private int frame_start = 0;
    private Thumbnail thumbnail_post;
    private Bitmap bmp;
    private Button reset_btn;
    private Button thumb_btn;
    private Button play_toggle_btn;
    //private Mat video_mat;
    private int reset;
    private boolean get_thumbnail;
    private int thumb;
    private Mat video_mat;
    private Bitmap video_mat_bmp;

    private String post_resp_result_success;
    private int post_resp_result_max_id;
    private int post_resp_result_duration;
    private int post_resp_result_total_sec;

    private Mat[] video_mat_arr;
    private Mat[] video_mat_arr2;
    private int cnt;
    private Bitmap[] loading_img_arr;
    private int k;
    private int video_size = 400;
    private int[] thread_is_finish;
    private Thread[] get_frame_thread;
    private Mat[] loading_mat_arr;
    private int check;
    private int start_20 = 0;
    private int arr1_done = 0;
    private int[] frame_check;

    private MyRunnable[] video_thread_arr;

    private int[] idx = {0,6,12,18,24,30,36,42,48,54};
    //private static ExecutorService executor = Executors.newFixedThreadPool(10);
    private static ExecutorService executor = Executors.newSingleThreadExecutor();
    private int index;


    private SeekBar sb;
    private boolean sb_check = true;
    private int sb_cnt = 0;

    private boolean is_playing = true;

    private boolean is_touching = false;

    TextView progress_text;
    TextView sec_text;


    public native void ConvertRGBtoGray(long matAddrInput, long matAddrResult, long video_mat, long thumbnail, int reset_btn, int thumb);
    //public native void ConvertRGBtoGray(long matAddrInput, long matAddrResult);
    public native void getJNIString();

    static {
        System.loadLibrary("opencv_java3");
        System.loadLibrary("native-lib");
    }



    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_brother);

        Intent intent  = getIntent();
        clientid = intent.getStringExtra("clientid");


        reset_btn = (Button)findViewById(R.id.reset_btn);
        thumb_btn = (Button)findViewById(R.id.thumb_btn);
        play_toggle_btn = (Button)findViewById(R.id.play_toggle_btn) ;
        reset = 0;
        get_thumbnail = false;
        thumb = 0;
        cnt = 0;
        loading_img_arr = new Bitmap[5];
        video_thread_arr = new MyRunnable[45];
        thread_is_finish = new int[45];
        get_frame_thread = new Thread[45];
        loading_mat_arr = new Mat[5];
        check = 0;
        frame_check = new int[900];
        progress_text = (TextView)findViewById(R.id.progress_text);
        sec_text = (TextView)findViewById(R.id.sec_text);

        for(int i=0;i<45;i++){
            thread_is_finish[i] = 0;
        }
        for(int i=0;i<900;i++){
            frame_check[i] = 0;
        }

        for(int i=0;i<1;i++) {
            loading_img_arr[i*5 + 0] = BitmapFactory.decodeResource(getResources(), R.drawable.loading_img1);
            loading_img_arr[i*5 + 1] = BitmapFactory.decodeResource(getResources(), R.drawable.loading_img2);
            loading_img_arr[i*5 + 2] = BitmapFactory.decodeResource(getResources(), R.drawable.loading_img3);
            loading_img_arr[i*5 + 3] = BitmapFactory.decodeResource(getResources(), R.drawable.loading_img4);
            loading_img_arr[i*5 + 4] = BitmapFactory.decodeResource(getResources(), R.drawable.loading_img5);
        }
        video_mat = new Mat(video_size, video_size, CvType.CV_8UC4);
        for(int i=0;i<5;i++){
            Utils.bitmapToMat(loading_img_arr[i], video_mat);
            loading_mat_arr[i] = video_mat;
            if (i != loading_img_arr.length - 1) {
                video_mat = new Mat(video_size, video_size, CvType.CV_8UC4);
            }
        }

        int k=0;

        for(int i=0;i<=90*10;i=i+90){
            video_thread_arr[k++] = new MyRunnable(i);
        }


        sb  = (SeekBar) findViewById(R.id.seekBar);

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(seekBar.getProgress() < 15){
                    arr1_done = 0;
                }
                else {
                    arr1_done = 1;
                }
                is_touching = false;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                is_touching = true;
            }

            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                if(check >= 3 && is_touching) {
                    if (arr1_done == 0) {
                        cnt = (progress * Math.round(((float) post_resp_result_duration / post_resp_result_total_sec))) % 450;
                    } else if (arr1_done == 1) {
                        cnt = (progress * Math.round(((float) post_resp_result_duration / post_resp_result_total_sec))) % 450;
                    }
                }

                if(progress/10 == 0) {
                    progress_text.setText("0:0" + Integer.toString(progress));
                }
                else {
                    progress_text.setText("0:" + Integer.toString(progress));
                }
            }
        });

        sb.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return sb_check;
            }
        });




        Button.OnClickListener reset_ClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                for(int rs=0;rs<2;rs++) {
                    //Toast.makeText(MainActivity.this, "Hey I'm a toast messsage",Toast.LENGTH_LONG).show();
                    reset = 1;
                    ConvertRGBtoGray(matInput.getNativeObjAddr(), matResult.getNativeObjAddr(), video_mat.getNativeObjAddr(), thumbnail.getNativeObjAddr(), reset, thumb);
                    thumbnail_f = 0;
                    thumb = 0;
                    thumbnail = new Mat(500, 500, CvType.CV_8UC4);
                    video_mat = new Mat(video_size, video_size, CvType.CV_8UC4);
                    video_mat_bmp = Bitmap.createBitmap(video_size, video_size, Bitmap.Config.ARGB_8888);
                    cnt = 0;
                    /*
                    for(int j=0;j<(int)(video_mat_arr.length / loading_img_arr.length);j++) {
                        for (int i = 0; i < loading_img_arr.length; i++) {
                            Utils.bitmapToMat(loading_img_arr[i], video_mat);
                            video_mat_arr[(5*j) + i] = video_mat;
                            if (i != loading_img_arr.length - 1) {
                                video_mat = new Mat(video_size, video_size, CvType.CV_8UC4);
                            }
                        }
                    }
                    */

                    get_frame_thread = new Thread[45];
                    check = 0;
                    sb.setProgress(0);
                    int k = 0;
                    for (int i = 0; i <= 90 * 10; i = i + 90) {
                        video_thread_arr[k++] = new MyRunnable(i);
                    }
                    for (int i = 0; i < 900; i++) {
                        frame_check[i] = 0;
                    }
                    video_mat_arr = new Mat[450];
                    video_mat_arr2 = new Mat[450];
                    start_20 = 0;
                    sb_check = true;
                    sb_cnt = 0;
                    progress_text.setText("0:00");
                    sec_text.setText("wait");
                    /*
                    for(int i=0;i<45;i++){
                        thread_is_finish[i] = 0;
                    }
                    */
                    executor = Executors.newSingleThreadExecutor();
                }
            }
        };
        reset_btn.setOnClickListener(reset_ClickListener);

        Button.OnClickListener thumb_ClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                thumb = 1;
                ConvertRGBtoGray(matInput.getNativeObjAddr(), matResult.getNativeObjAddr(), video_mat.getNativeObjAddr(), thumbnail.getNativeObjAddr(), reset, thumb);
            }
        };
        thumb_btn.setOnClickListener(thumb_ClickListener);



        //play_toggle_btn.setVisibility(View.INVISIBLE);
        Button.OnClickListener play_toggle_ClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                if(check >= Math.round(post_resp_result_duration/90)) {
                    if (play_toggle_btn.getText().equals("STOP")) {
                        is_playing = false;
                        play_toggle_btn.setText("PLAY");
                    } else if (play_toggle_btn.getText().equals("PLAY")) {
                        is_playing = true;
                        play_toggle_btn.setText("STOP");
                    }
                }
            }
        };
        play_toggle_btn.setOnClickListener(play_toggle_ClickListener);



        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService("52.194.102.63", 8000);
        //application.buildNetworkService("44ea0d45.ngrok.io");
        //application.buildNetworkService("자신의 ip", 8000);
        networkService = ApplicationController.getInstance().getNetworkService();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //퍼미션 상태 확인
            if (!hasPermissions(PERMISSIONS)) {

                //퍼미션 허가 안되어있다면 사용자에게 요청
                requestPermissions(PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }
        }
        mOpenCvCameraView = (CameraBridgeViewBase)findViewById(R.id.activity_surface_view);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
        mOpenCvCameraView.setCameraIndex(0); // front-camera(1),  back-camera(0)
        //mOpenCvCameraView.setMaxFrameSize(720,480);
        mOpenCvCameraView.setMaxFrameSize(400,400);
        //mOpenCvCameraView.setMaxFrameSize(500,300);
        mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);

    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "onResume :: Internal OpenCV library not found.");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "onResum :: OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        executor.shutdown();

        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        bmp_buffer = new Bitmap[10];
        thumbnail = new Mat(500, 500, CvType.CV_8UC4);
        thumbnail_f = 0;
        thumbnail_post = new Thumbnail();
        bmp = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
        video_mat = new Mat(video_size, video_size, CvType.CV_8UC4);
        video_mat_bmp = Bitmap.createBitmap(video_size, video_size, Bitmap.Config.ARGB_8888);
        Utils.bitmapToMat(video_mat_bmp, video_mat);
        video_mat_arr = new Mat[450];
        video_mat_arr2 = new Mat[450];

        for(int i=0;i<5;i++){
            Utils.bitmapToMat(loading_img_arr[i], video_mat);
            loading_mat_arr[i] = video_mat;
            if (i != loading_img_arr.length - 1) {
                video_mat = new Mat(video_size, video_size, CvType.CV_8UC4);
            }
        }

        /*
        for(int i=0;i<loading_img_arr.length;i++) {
            Utils.bitmapToMat(loading_img_arr[i], video_mat);
            video_mat_arr[i] = video_mat;
            if(i!=loading_img_arr.length-1) {
                video_mat = new Mat(video_size, video_size, CvType.CV_8UC4);
            }
        }
        */
        //bmp = mmr.getFrameAtTime(100000 * cnt, MediaMetadataRetriever.OPTION_CLOSEST);
    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {

        reset = 0;
        matInput = inputFrame.rgba();

        if ( matResult != null ) matResult.release();
        matResult = new Mat(matInput.rows(), matInput.cols(), matInput.type());


        if(video_mat_arr[cnt] == null && check >= Math.ceil((float)post_resp_result_duration/90)){
            cnt = 0;
        }


        Log.i(ApplicationController.TAG, "HIHI Message : check = " + Integer.toString(check));

        if(check<3) {
            video_mat = loading_mat_arr[cnt];
            cnt = (cnt + 1) % 5;
        }

        else if(3 <= check && check <= Math.ceil((float)post_resp_result_duration/90) && frame_check[0] == 1 && frame_check[1] == 1){
            if(start_20 == 0){
                cnt = 0;
                start_20 = 1;
                sb_check = false;
                sb_cnt = 0;
                //play_toggle_btn.setVisibility(View.VISIBLE);
            }

            int progress = Math.round((float) (arr1_done * 450 + cnt) / Math.round(((float) post_resp_result_duration / post_resp_result_total_sec)));
            //sb.setProgress(Math.round(((float)arr1_done*450 + cnt) / 30 * Math.round(((float)30/((float)post_resp_result_duration/post_resp_result_total_sec)))));
            sb.setProgress(progress);
            //progress_text.setText("0:" + Integer.toString(progress));
            Log.i(ApplicationController.TAG, "HIHI Message : setProgress = " + Integer.toString(progress));
            //Log.i(ApplicationController.TAG, "HIHI Message : sp = " + Integer.toString(sb.getProgress()));
            if(arr1_done == 0) {
                Log.i(ApplicationController.TAG, "HIHI Message : cnt = " + Integer.toString(cnt));
                video_mat = video_mat_arr[cnt];
                if(cnt == 449){
                    arr1_done = 1;
                    cnt = 0;
                }
                else if(cnt < 449 && frame_check[cnt+1] == 1){
                    if(is_playing) {
                        cnt = (cnt + 1) % 450;
                        sb_cnt = (sb_cnt + 1) % post_resp_result_duration;
                    }
                }
                if(cnt == post_resp_result_duration-2){
                    cnt = 0;
                }
            }

            else if(arr1_done == 1){
                Log.i(ApplicationController.TAG, "HIHI Message : cnt = " + Integer.toString(cnt));
                video_mat = video_mat_arr2[cnt];
                if(cnt == (post_resp_result_duration-450)-1){
                    arr1_done = 0;
                    cnt = 0;
                }
                else if(cnt < (post_resp_result_duration-450)-1 && frame_check[(cnt+450)+1] == 1){
                    if(is_playing) {
                        cnt = (cnt + 1) % 450;
                        sb_cnt = (sb_cnt + 1) % post_resp_result_duration;
                    }
                }
                else {
                    arr1_done = 0;
                    cnt = 0;
                }
            }

        }


        ConvertRGBtoGray(matInput.getNativeObjAddr(), matResult.getNativeObjAddr(), video_mat.getNativeObjAddr(), thumbnail.getNativeObjAddr(), reset, thumb);



        if(thumbnail.cols() != 500 && thumbnail_f == 0){


            Size t_sz = new Size(200,200);
            Imgproc.resize( thumbnail, thumbnail, t_sz );

            byte[] return_buff = new byte[(int) (thumbnail.total() * thumbnail.channels())];
            thumbnail.get(0, 0, return_buff);

            /*
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
            bmp = BitmapFactory.decodeByteArray(return_buff, 0, return_buff.length, options);
            */

            thumbnail_post.setBmp(return_buff);
            thumbnail_post.setUser_id(clientid);

            /*
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
            Bitmap bmp_tmp;
            bmp_tmp = Bitmap.createScaledBitmap( bmp, 200, 200, true );
            thumbnail_post.setBmp(bmp_tmp);
            */
            Log.i(ApplicationController.TAG, "HIHI Message : " + "!!!!!!");


            Call<Thumbnail> postCall = networkService.post_thumbnail(thumbnail_post);
            postCall.enqueue(new Callback<Thumbnail>() {
                @Override
                public void onResponse(Call<Thumbnail> call, Response<Thumbnail> response) {
                    if( response.isSuccessful()) {
                        //Log.i(ApplicationController.TAG, "response : " + new Gson().toJson(response.body()));
                        String result = new Gson().toJson(response.body()).toString();
                        //Log.i(ApplicationController.TAG, "HIHI Message : " + result);
                        Gson gson = new Gson();
                        Thumbnail post_resp = gson.fromJson(result, Thumbnail.class);
                        post_resp_result_success = post_resp.getSuccess();
                        post_resp_result_max_id = post_resp.getMax_id();
                        post_resp_result_duration = post_resp.getDuration();
                        post_resp_result_total_sec = post_resp.getTotal_sec();
                        Log.i(ApplicationController.TAG, "HIHI Message : post_resp_result_total_sec = " + Integer.toString(post_resp_result_duration));
                        Log.i(ApplicationController.TAG, "HIHI Message : post_resp_result = " + post_resp_result_success);

                        if(post_resp_result_success.equals("1")) {
                            /*
                            for(int idx=0;idx<20;idx++){
                                video_thread_arr[idx].setThumbnail_id(post_resp_result_max_id);
                                get_frame_thread[idx % 10] = new Thread(video_thread_arr[idx]);
                                get_frame_thread[idx % 10].start();
    }
                            */

                            if(post_resp_result_duration >= 900){
                                post_resp_result_duration = 900;
                            }
                            if(post_resp_result_total_sec >= 30){
                                post_resp_result_total_sec = 30;
                            }
                            sb.setMax(post_resp_result_total_sec);
                            sec_text.setText("0:" + Integer.toString(post_resp_result_total_sec));
                            //Log.i(ApplicationController.TAG, "HIHI Message : Math.round(post_resp_result_duration/60) = " + Integer.toString(Math.round((float)post_resp_result_duration/60)));
                            for(int x=0;x<Math.ceil((float)post_resp_result_duration/90);x++){
                                video_thread_arr[x].setThumbnail_id(post_resp_result_max_id);
                                executor.execute(video_thread_arr[x]);
                                System.out.println(Integer.toString(x) + "번쓰래드 submit");
                            }
                            executor.shutdown();
                        }

                        else {
                            for(int rs=0;rs<2;rs++) {
                                //Toast.makeText(MainActivity.this, "Hey I'm a toast messsage",Toast.LENGTH_LONG).show();
                                reset = 1;
                                ConvertRGBtoGray(matInput.getNativeObjAddr(), matResult.getNativeObjAddr(), video_mat.getNativeObjAddr(), thumbnail.getNativeObjAddr(), reset, thumb);
                                thumbnail_f = 0;
                                thumb = 0;
                                thumbnail = new Mat(500, 500, CvType.CV_8UC4);
                                video_mat = new Mat(video_size, video_size, CvType.CV_8UC4);
                                video_mat_bmp = Bitmap.createBitmap(video_size, video_size, Bitmap.Config.ARGB_8888);
                                cnt = 0;
                                get_frame_thread = new Thread[45];
                                check = 0;
                                sb.setProgress(0);
                                int k = 0;
                                for (int i = 0; i <= 90 * 10; i = i + 90) {
                                    video_thread_arr[k++] = new MyRunnable(i);
                                }
                                for (int i = 0; i < 900; i++) {
                                    frame_check[i] = 0;
                                }
                                video_mat_arr = new Mat[450];
                                video_mat_arr2 = new Mat[450];
                                start_20 = 0;
                                sb_check = true;
                                sb_cnt = 0;
                                progress_text.setText("0:00");
                                sec_text.setText("wait");
                                executor = Executors.newSingleThreadExecutor();
                            }
                            Toast.makeText(Brother.this, "해당 썸네일이 없습니다.", Toast.LENGTH_LONG).show();
                        }




                    } else {
                        int StatusCode = response.code();
                        Log.i(ApplicationController.TAG, "HIHI Code : " + StatusCode);
                    }

                }

                @Override
                public void onFailure(Call<Thumbnail> call, Throwable t) {
                    Log.i(ApplicationController.TAG, "HIHI Message : " + t.getMessage());
                }
            });



            thumbnail_f = 1;
        }


        //ConvertRGBtoGray(matInput.getNativeObjAddr(), matResult.getNativeObjAddr());

        return matResult;

    }



    //여기서부턴 퍼미션 관련 메소드
    static final int PERMISSIONS_REQUEST_CODE = 1000;
    String[] PERMISSIONS  = {"android.permission.CAMERA"};


    private boolean hasPermissions(String[] permissions) {
        int result;

        //스트링 배열에 있는 퍼미션들의 허가 상태 여부 확인
        for (String perms : permissions){

            result = ContextCompat.checkSelfPermission(this, perms);

            if (result == PackageManager.PERMISSION_DENIED){
                //허가 안된 퍼미션 발견
                return false;
            }
        }

        //모든 퍼미션이 허가되었음
        return true;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode){

            case PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean cameraPermissionAccepted = grantResults[0]
                            == PackageManager.PERMISSION_GRANTED;

                    if (!cameraPermissionAccepted)
                        showDialogForPermission("앱을 실행하려면 퍼미션을 허가하셔야합니다.");
                }
                break;
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void showDialogForPermission(String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder( Brother.this);
        builder.setTitle("알림");
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id){
                requestPermissions(PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                finish();
            }
        });
        builder.create().show();
    }


}