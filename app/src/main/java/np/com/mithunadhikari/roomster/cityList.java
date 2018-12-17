package np.com.mithunadhikari.roomster;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class cityList extends Service {
    static Context context;
    static SharedPreferences myPrefs;
    List<String> city_list = new ArrayList<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*@Override
    public void onStart(Intent intent, int startId) {
        System.out.println("Here we come");
    }*/

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        myPrefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        System.out.println("Service started");
        new getCityFromServer(getApplicationContext()).execute();
       // super.onStartCommand(intent, flags, startId);
        /*new getCityFromServer();*/
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        //Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show();
    }


    public <T> void setList(String key, List<T> list) {
        try {
            SharedPreferences.Editor editor = myPrefs.edit();
            Gson gson = new Gson();
            String json = gson.toJson(list);
            editor.putString(key, json);
            editor.apply();
        }catch (Exception e){
            System.out.println("latest exception\t"+e.getMessage());
        }
    }




       /* Set<String> set = new HashSet<>();
        set.addAll(city_list);
        editor.putStringSet("cities", set);
        editor.apply();
        System.out.println("baad me"+set);*/
 /*       try {
*//*
            storeCityList cityList = new storeCityList(getApplication(), null, null, 2);
*//*
            Looper.prepare();
            tempStoreCity cityList = new tempStoreCity(cityList.this);
            cityList.setString(city_list);
          Intent intent = new Intent(cityList.this, storeCityList.class);
          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
           intent.putStringArrayListExtra("data", (ArrayList<String>) city_list);
            startActivity(new Intent(cityList.this, storeCityList.class));
        } catch (Exception e) {
            Log.e("City list print ", e.getMessage());
        }*/




private class getCityFromServer extends AsyncTask<String, Void, String> {
    getCityFromServer(Context ctx) {
        context = ctx;
    }

    @Override
    protected String doInBackground(String... params) {
        HttpHandler handler = new HttpHandler();
        String response = handler.makeServiceCall("https://isochasmic-circuits.000webhostapp.com/get_city_list.php");

        if (response != null) {
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    city_list.add(object.optString("city"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        setList("cities",city_list);

        return null;
    }
}
}
