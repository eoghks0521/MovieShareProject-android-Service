package com.example.administrator.loginapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;

public class Main4Activity extends AppCompatActivity {

    TextView textView;
    String id;
    Button button1,button2,button3,button4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        final Intent intent = getIntent();
        String idd = intent.getStringExtra("clientid");
        id = idd;
        textView = (TextView)findViewById(R.id.textView2);
        textView.setText(id+"님 환영합니다요");
        button1 = (Button)findViewById(R.id.btn1);
        button2 = (Button)findViewById(R.id.btn2);
        button3 = (Button)findViewById(R.id.btn3);
        button4 = (Button)findViewById(R.id.btn4);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(getApplicationContext(),Main3Activity.class);
                intent3.putExtra("clientid",id);
                startActivity(intent3);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getApplicationContext(),Main5Activity.class);
                intent2.putExtra("clientid",id);
                startActivity(intent2);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent3 = new Intent(getApplicationContext(),FriendActivity.class);
                        intent3.putExtra("clientid",id);
                        startActivity(intent3);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(getApplicationContext(),VideoExtract.class);
                intent4.putExtra("clientid",id);
                startActivity(intent4);
            }
        });

    }

}
