package life.knowledge4.videotrimmersample;

import android.content.Intent;
import android.opengl.Matrix;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        ImageView img = (ImageView)findViewById(R.id.img2);
        img.setImageResource(Pictures.pictures[1]);

        startLoading();

    }

    private void startLoading(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getBaseContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        },2000);
    }
}
