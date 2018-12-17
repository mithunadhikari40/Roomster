package np.com.mithunadhikari.roomster;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mithun on 3/5/2017.
 */

public class displayUploadHistory  extends Fragment {
    private static String getUpload = "http://isochasmic-circuits.000webhostapp.com/getAllData.php";
    SharedPreferences myPrefs;
    boolean onlyOnce = true;
    String name1;
    String district_city;
    ArrayList<HashMap<String, String>> profile_list;

    ProgressDialog progressDialog;
   // TextView NAME, ADDRESS, EMAIL, CONTACT;
    String Email;
    private ListView list_view1;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_view, container, false);
        myPrefs = this.getActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);

        Email = myPrefs.getString("USERNAME", null);

         list_view1 = (ListView) view.findViewById((R.id.list_view));
/*
        NAME = (TextView) view.findViewById(R.id.name);
        ADDRESS = (TextView) view.findViewById(R.id.address);
        EMAIL = (TextView) view.findViewById(R.id.email);
        CONTACT = (TextView) view.findViewById(R.id.contact);*/
        profile_list = new ArrayList<>();
        new DisplayOwnUploads().execute();
        // new DisplayOwnUploads().execute();
        // CreateView(null,null,null);
        return view;

    }


    private class DisplayOwnUploads extends AsyncTask<String, Object, String> {

        @Override
        protected void onPreExecute() {
            // super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(getUpload);
            if (jsonStr != null) {
                try {
                    JSONArray jsonArray = new JSONArray(jsonStr);

                    // looping through All Contacts
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String ID = jsonObject.optString("ID");
                        String room_flat = jsonObject.optString("room_flat");
                        String district = jsonObject.optString("district");
                        String getCity = jsonObject.optString("city");
                        String price = jsonObject.optString("price");
                        String contact = jsonObject.optString("contact");
                        String description = jsonObject.optString("description");
                        String date = jsonObject.optString("date");
                        String email_address = jsonObject.optString("email");

                        // tmp hash map for single student_info
                        HashMap<String, String> room_list = new HashMap<>();

                        // adding each child node to HashMap key => value
                        if (email_address.equalsIgnoreCase(Email)) {
                            room_list.put("Room/flat:\t", room_flat);
                            room_list.put("District:\t", district);
                            room_list.put("getCity:\t", getCity);
                            room_list.put("price:\t", price);
                            room_list.put("contact:\t", contact);
                            room_list.put("description:\t", description);
                            room_list.put("date:\t", date);
                            profile_list.add(room_list);
                        }
                    }
                } catch (final JSONException e) {


                }
            }

            return jsonStr;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
            ListAdapter adapter = new SimpleAdapter(
                    getActivity(), profile_list,
                    R.layout.fragment_profile, new String[]{"room_flat", "district",
                    "getCity", "price", "contact", "description", "date"}, new int[]{
                    R.id.txt_room_flat, R.id.txt_district, R.id.txt_city, R.id.txt_price,
                    R.id.txt_contact, R.id.txt_desc, R.id.txt_date});

                list_view1.setAdapter(adapter);
            } catch (Exception e) {
                Log.e("The  ", e.getMessage());
            }

        }


    }



}