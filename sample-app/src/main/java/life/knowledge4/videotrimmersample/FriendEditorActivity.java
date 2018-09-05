package life.knowledge4.videotrimmersample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FriendEditorActivity extends AppCompatActivity {
    Button button1,button2,button3;
    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_editor);
        button1 = findViewById(R.id.button);
        button3 = findViewById(R.id.button2);
        final Intent intent = getIntent();
        id  = intent.getStringExtra("clientid");

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),FriendActivity.class);
                intent.putExtra("clientid",id);
                startActivity(intent);
            }
        });



        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),FriendAddActivity.class);
                intent.putExtra("clientid",id);
                startActivity(intent);
            }
        });
    }
}
