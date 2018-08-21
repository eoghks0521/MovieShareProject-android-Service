package com.example.administrator.loginapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main2Activity extends AppCompatActivity {

    public final String TAG = "KDH";
    private  NetworkService networkService;
    @BindView(R.id.tv1)
    EditText tv1;
    @BindView(R.id.tv3)
    EditText tv3;
    @BindView(R.id.tv4)
    TextView tv4;
    Client client = new Client();



    @OnClick(R.id.button)
    public void bt1_Click() {
        //POST
        String id = tv1.getText().toString();
        String pw = tv3.getText().toString();
        client.setClientid(id);
        client.setPassword(pw);
        Call<Client> postCall = networkService.post_client(client);
        postCall.enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, Response<Client> response) {
                if (response.isSuccessful()) {
                    tv4.setText("등록완료");
                } else {
                    tv4.setText("등록실패");
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ButterKnife.bind(this);

        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService("23b95962.ngrok.io");
        networkService = ApplicationController.getInstance().getNetworkService();
    }
}
