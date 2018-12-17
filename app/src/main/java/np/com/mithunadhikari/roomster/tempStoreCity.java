package np.com.mithunadhikari.roomster;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class tempStoreCity extends Activity {
    static Context context;
    SharedPreferences myPrefs = getApplicationContext().getSharedPreferences("myPrefs",Context.MODE_PRIVATE);
    tempStoreCity(Context ctx){
        context=ctx;

    }
    List add_data = new ArrayList<>();
    public void setString(List str) {
        add_data = str;
        getString();
        //System.out.println("Received list are"+add_data);
        //onCreate(db);
        //fill_data();

    }
    public List getString() {
        try {
            SharedPreferences.Editor editor = myPrefs.edit();
            Set<String> set = new HashSet<>();
            set.addAll(add_data);
            editor.putStringSet("cities", set);
            editor.apply();
        }catch (Exception e){
            Log.d("Exception adding city",e.getMessage());
        }
        return add_data;

    }
}
