package com.example.administrator.loginapp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoExtract extends AppCompatActivity {


    private final int SELECT_IMAGE = 1;
    private final int SELECT_MOVIE = 2;
    // 이미지 선택
    private void doSelectImage()
    {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        try
        {
            startActivityForResult(i, SELECT_IMAGE);
        } catch (android.content.ActivityNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    // 동영상선택
    private void doSelectMovie()
    {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("video/*");
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        try
        {
            startActivityForResult(i, SELECT_MOVIE);
        } catch (android.content.ActivityNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == RESULT_OK)
        {
            if (requestCode == SELECT_IMAGE)
            {
                Uri uri = intent.getData();
                String path = getPath(uri);
                String name = getName(uri);
                String uriId = getUriId(uri);
                Log.e("###", "실제경로 : " + path + "\n파일명 : " + name + "\nuri : " + uri.toString() + "\nuri id : " + uriId);
            }
            else if (requestCode == SELECT_MOVIE)
            {
                Uri uri = intent.getData();
                String path = getPath(uri);
                String name = getName(uri);
                String uriId = getUriId(uri);
                Log.e("###", "실제경로 : " + path + "\n파일명 : " + name + "\nuri : " + uri.toString() + "\nuri id : " + uriId);
            }
        }
    }

    // 실제 경로 찾기
    private String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    // 파일명 찾기
    private String getName(Uri uri)
    {
        String[] projection = { MediaStore.Images.ImageColumns.DISPLAY_NAME };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DISPLAY_NAME);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    // uri 아이디 찾기
    private String getUriId(Uri uri)
    {
        String[] projection = { MediaStore.Images.ImageColumns._ID };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_extract);
        // VideoView : 동영상을 재생하는 뷰
        VideoView vv = (VideoView) findViewById(R.id.videoView);

        // MediaController : 특정 View 위에서 작동하는 미디어 컨트롤러 객체
        MediaController mc = new MediaController(this);
        vv.setMediaController(mc); // Video View 에 사용할 컨트롤러 지정

        String path = Environment.getExternalStorageDirectory()
                .getAbsolutePath(); // 기본적인 절대경로 얻어오기

        // 절대 경로 = SDCard 폴더 = "stroage/emulated/0"
        //          ** 이 경로는 폰마다 다를수 있습니다.**
        // 외부메모리의 파일에 접근하기 위한 권한이 필요 AndroidManifest.xml에 등록
        Log.d("test", "절대 경로 : " + path);

        vv.setVideoPath(path+"/kakaotalk_1458998519582.3gp");
        // VideoView 로 재생할 영상
        // 아까 동영상 [상세정보] 에서 확인한 경로
        vv.requestFocus(); // 포커스 얻어오기
        vv.start(); // 동영상 재생
    } // end of onCreate
} // end of class
