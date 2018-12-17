package np.com.mithunadhikari.roomster;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class testClass extends AppCompatActivity {
    Context context;
    SharedPreferences myPrefs;
    String emailAddress;
    Context check;
    public testClass(Context ctx){

    }
    public Context account_active() {
        myPrefs = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        emailAddress = myPrefs.getString("USERNAME", null);
        HttpHandler sh = new HttpHandler();
        String url = "https://isochasmic-circuits.000webhostapp.com/getEmail.php";
        String jsonStr = sh.makeServiceCall(url);
        if (jsonStr != null) {
            try {
                JSONArray jsonArray = new JSONArray(jsonStr);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String email_address = jsonObject.optString("email");
                    if (email_address.equals(emailAddress)) {
                       context=getApplicationContext();
                    }
                }
            } catch (final JSONException e) {

            }
        }
       return check;
    }


}
