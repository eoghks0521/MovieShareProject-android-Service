package life.knowledge4.videotrimmersample;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FunctionListActivity extends AppCompatActivity {

    TextView textView;
    String id;
    Button button1,button2,button3,button4;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_list);
        final Intent intent = getIntent();
        String idd = intent.getStringExtra("clientid");
        id = idd;
        textView = (TextView)findViewById(R.id.textView2);
        textView.setText(id+"님 반갑습니다.");
        button1 = (Button)findViewById(R.id.btn1);
        button2 = (Button)findViewById(R.id.btn2);
        button3 = (Button)findViewById(R.id.btn3);
        button4 = (Button)findViewById(R.id.btn4);




        button1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){

                    case MotionEvent.ACTION_DOWN:

                        button1.setBackgroundColor(Color.RED);
                        button1.setTextColor(Color.BLACK);
                        break;

                    case MotionEvent.ACTION_UP:
                        button1.setBackgroundColor(Color.parseColor("#FFDB53F9"));
                        button1.setTextColor(Color.WHITE);
                        Intent intent3 = new Intent(getApplicationContext(),UploadActivity.class);
                        intent3.putExtra("clientid",id);
                        startActivity(intent3);
                        break;
                }
                return true;
            }
        });



        button2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        button2.setBackgroundColor(Color.RED);
                        button2.setTextColor(Color.BLACK);
                        break;

                    case MotionEvent.ACTION_UP:
                        button2.setBackgroundColor(Color.parseColor("#FFDB53F9"));
                        button2.setTextColor(Color.WHITE);
                        Intent intent2 = new Intent(getApplicationContext(),UploadListActivity.class);
                        intent2.putExtra("clientid",id);
                        startActivity(intent2);
                        break;
                }
                return true;
            }
        });


        button3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        button3.setBackgroundColor(Color.RED);
                        button3.setTextColor(Color.BLACK);
                        break;

                    case MotionEvent.ACTION_UP:
                        button3.setBackgroundColor(Color.parseColor("#FFDB53F9"));
                        button3.setTextColor(Color.WHITE);
                        Intent intent3 = new Intent(getApplicationContext(),FriendEditorActivity.class);
                        intent3.putExtra("clientid",id);
                        startActivity(intent3);
                        break;
                }
                return true;
            }
        });


        button4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        button4.setBackgroundColor(Color.RED);
                        button4.setTextColor(Color.BLACK);
                        break;

                    case MotionEvent.ACTION_UP:
                        button4.setBackgroundColor(Color.parseColor("#FFDB53F9"));
                        button4.setTextColor(Color.WHITE);
                        Intent intent4 = new Intent(getApplicationContext(),VideoEditorActivity.class);
                        intent4.putExtra("clientid",id);
                        startActivity(intent4);
                        break;
                }
                return true;
            }
        });


    }

}
