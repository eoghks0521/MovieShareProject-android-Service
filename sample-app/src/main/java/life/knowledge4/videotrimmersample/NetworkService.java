package life.knowledge4.videotrimmersample;

import java.util.List;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;



/**

 * Created by KJH on 2017-06-06.

 */


public interface NetworkService {
    @POST("/clients/")
    Call<Client> post_client(@Body Client client);
    @POST("/post/")
    Call<PassId> post_passid(@Body PassId passid);
    @GET("/clients/")
    Call<List<Client>> get_client();
}

