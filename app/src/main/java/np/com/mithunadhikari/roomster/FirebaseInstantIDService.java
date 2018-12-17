package np.com.mithunadhikari.roomster;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by mithun on 6/23/2017.
 */

public class FirebaseInstantIDService extends FirebaseInstanceIdService {

   /* FirebaseInstantIDService(){

    }*/

    @Override
    public void onTokenRefresh() {
        String token= FirebaseInstanceId.getInstance().getToken();
        registerToken(token);

    }


    @Override
    public void onStart(Intent intent, int startId) {
        String token= FirebaseInstanceId.getInstance().getToken();
        registerToken(token);
    }

    public void registerToken(String token) {
        OkHttpClient client=new OkHttpClient();
        RequestBody body=new FormBody.Builder()
                .add("token",token)
                .build();
        Request request=new Request.Builder()
                .url("https://isochasmic-circuits.000webhostapp.com/send_notification.php")
                .post(body)
                .build();
        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            Log.e("Sending token",e.getMessage());
        }
    }

    /*public void clearToken() {
        Toast.makeText(getApplicationContext(),"Executed",Toast.LENGTH_LONG).show();
        String token= FirebaseInstanceId.getInstance().getToken();
        OkHttpClient client=new OkHttpClient();
        RequestBody body=new FormBody.Builder()
                .add("token",token)
                .build();
        Request request=new Request.Builder()
                .url("https://isochasmic-circuits.000webhostapp.com/clear_token.php")
                .post(body)
                .build();
        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            Log.e("Sending token",e.getMessage());
        }
    }*/


   /* private void getNotification() {
        OkHttpClient client=new OkHttpClient();
        RequestBody body=new FormBody.Builder()
                .build();
        Request request=new Request.Builder()
                .url("https://isochasmic-circuits.000webhostapp.com/push_notification.php")
                .post(body)
                .build();
        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            Log.e("Sending token",e.getMessage());
        }
    }*/
}
