package com.example.administrator.loginapp;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public final String TAG = "KDH";
    private  NetworkService networkService;
    @BindView(R.id.editText)
    EditText editText;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.editText2)
    EditText editText2;
    Client client = new Client();
    PassId passId = new PassId();





    @OnClick(R.id.btn)
    public void btn_Click() {
        //GET

        final String id = editText.getText().toString();
        final String pw = editText2.getText().toString();

        Call<List<Client>> clientCall = networkService.get_client();
        clientCall.enqueue(new Callback<List<Client>>() {
            @Override
            public void onResponse(Call<List<Client>> call, Response<List<Client>> response) {
                if (response.isSuccessful()) {
                    List<Client> client_List = response.body();

                    String client_txt = "";
                    for (Client client : client_List) {
                        Log.i("kwon",client.getClientid()+"====="+id+id.compareTo(client.getClientid()));
                        Log.i("kwon",client.getPassword()+"====="+pw+pw.compareTo(client.getPassword()));

                        if(id.equals(client.getClientid().toString()) && pw.equals(client.getPassword().toString())) {
                            textView.setText("성공");

                            passId.setPassid(id);
                            Call<PassId> postCall = networkService.post_passid(passId);

                            postCall.enqueue(new Callback<PassId>() {
                                @Override
                                public void onResponse(Call<PassId> call, Response<PassId> response) {
                                    if (response.isSuccessful()) {
                                    } else {
                                        int StatusCode = response.code();
                                        Log.i(ApplicationController.TAG, "Status Code : " + StatusCode);
                                    }

                                }

                                @Override
                                public void onFailure(Call<PassId> call, Throwable t) {
                                    Log.i(ApplicationController.TAG, "Fail Message : " + t.getMessage());
                                }
                            });
                            Log.i("kwon","여기가 안돼요");
                            Intent intent = new Intent(getApplicationContext(),Main4Activity.class);
                            intent.putExtra("clientid",id);
                            startActivity(intent);

                            /*String url = "http://ec2-13-125-237-24.ap-northeast-2.compute.amazonaws.com:8000/";

                            // AsyncTask를 통해 HttpURLConnection 수행.

                            ContentValues values = new ContentValues();
                            values.put("clientid",id);
                            NetworkTask networkTask = new NetworkTask(url, values);
                            networkTask.execute();
                            */
                            break;
                        }
                            textView.setText("실패");
                    }
                } else {
                    int StatusCode = response.code();
                    Log.i(ApplicationController.TAG, "Status Code : " + StatusCode);
                }
            }

            @Override
            public void onFailure(Call<List<Client>> call, Throwable t) {
                Log.i(ApplicationController.TAG, "Fail Message : " + t.getMessage());
            }
        });
    }

    @OnClick(R.id.btn2)
    public void btn2_Click(){
        Intent intent = new Intent(this,Main2Activity.class);
        startActivity(intent);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService("23b95962.ngrok.io");
        networkService = ApplicationController.getInstance().getNetworkService();


    }




    public class NetworkTask extends AsyncTask<Void, Void, String> {

        private String url;
        private ContentValues values;

        public NetworkTask(String url, ContentValues values) {

            this.url = url;
            this.values = values;
        }

        @Override
        protected String doInBackground(Void... params) {

            String result="";
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result = requestHttpURLConnection.request(url, values);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }
}

