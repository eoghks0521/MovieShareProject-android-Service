package life.knowledge4.videotrimmersample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistActivity extends AppCompatActivity {

    public final String TAG = "KDH";
    private  NetworkService networkService;
    @BindView(R.id.tv1)
    EditText tv1;
    @BindView(R.id.tv2)
    EditText tv2;
    @BindView(R.id.tv3)
    EditText tv3;
    Client client = new Client();



    @OnClick(R.id.button)
    public void bt1_Click() {
        //POST
        String id = tv1.getText().toString();
        String pw = tv3.getText().toString();
        String pw2 = tv2.getText().toString();

        if(!pw.equals(pw2)){
            Toast.makeText(getApplicationContext(),"비밀 번호가 다릅니다.",Toast.LENGTH_LONG).show();
        }
        else {
            client.setClientid(id);
            client.setPassword(pw);
            Call<Client> postCall = networkService.post_client(client);
            postCall.enqueue(new Callback<Client>() {
                @Override
                public void onResponse(Call<Client> call, Response<Client> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "가입 완료", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "가입 실패", Toast.LENGTH_LONG).show();
                        int StatusCode = response.code();
                        Log.i(ApplicationController.TAG, "Status Code : " + StatusCode);
                    }

                }

                @Override
                public void onFailure(Call<Client> call, Throwable t) {
                    Log.i(ApplicationController.TAG, "Fail Message : " + t.getMessage());
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        ButterKnife.bind(this);

        ApplicationController application = ApplicationController.getInstance();
        //application.buildNetworkService("a9eab5e9.ngrok.io");
        application.buildNetworkService("52.194.102.63", 8000);
        networkService = ApplicationController.getInstance().getNetworkService();
    }
}
