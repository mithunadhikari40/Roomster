package np.com.mithunadhikari.roomster;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Profile extends Fragment {
    static Context context;
    SharedPreferences myPrefs;
    ProgressDialog progressDialog;
    String name1;
    RequestParams params = new RequestParams();
    String district_city;
    ArrayList<HashMap<String, String>> profile_list;
    ArrayList<HashMap<String, String>> upload_list;
    TextView NAME, ADDRESS, EMAIL, CONTACT;
    String Email;
    ImageView imageView;
    String room_flat, district, city, price, contact, desc, Date;
    Button edit_profile;
    private List<Model_Class> list_display;
    private ListView list_view;
    String sendFirstName,sendLastName,sendEmail,sendPassword,sendGender,sendDistrict,sendCity,sendContact;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        list_display = new ArrayList<>();
        myPrefs = getActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        //relativeLayout = (RelativeLayout) view.findViewById(R.id.getUploadHistory);
        Email = myPrefs.getString("USERNAME", null);
        //  list_view1 = (ListView) view.findViewById((R.id.list_view));
        list_view = (ListView) view.findViewById(R.id.profile_list_view);
        edit_profile = (Button) view.findViewById(R.id.edit_profile);
        NAME = (TextView) view.findViewById(R.id.name);
        ADDRESS = (TextView) view.findViewById(R.id.address);
        EMAIL = (TextView) view.findViewById(R.id.email);
        CONTACT = (TextView) view.findViewById(R.id.contact);
        imageView = (ImageView) view.findViewById(R.id.image_profile);

        edit_profile.setEnabled(false);

        profile_list = new ArrayList<>();
        upload_list = new ArrayList<>();
        //  load_image();
        Glide.with(getActivity())
                .load("https://isochasmic-circuits.000webhostapp.com/images/profile/profile_picture/" + Email + ".png")
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
        new JsonTask().execute();
        //new displayUploadHistory();

        new DisplayOwnUploads(getActivity()).execute();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageUrl("https://isochasmic-circuits.000webhostapp.com/images/profile/profile_picture/" + Email + ".png");

            }
        });
        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeProfile();
            }
        });

     /*   list_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                if (new Upload().isOnline(getActivity())) {

                    try {
                        int new_db_year, new_db_month;

                        String identity = ((TextView) arg1.findViewById(R.id.ID)).getText().toString();

                        Date = ((TextView) arg1.findViewById(R.id.txt_date)).getText().toString();
                        String db_year = Date.substring(0, 4);
                        new_db_year = Integer.parseInt(db_year);

                        int minus_index_first = Date.indexOf("-");
                        String month_day = Date.substring(minus_index_first + 1);
                        int minus_index_sec = month_day.indexOf("-");


                        String db_month = month_day.substring(0, minus_index_sec);
                        String db_day = month_day.substring(minus_index_sec + 1);


                        if (Integer.parseInt(db_month) == 12) {
                            new_db_year = Integer.parseInt(db_year) + 1;
                            new_db_month = 1;
                        } else {
                            new_db_month = Integer.parseInt(db_month) + 1;
                        }
                        String updated_date = new_db_year + "-" + new_db_month + "-" + db_day;

                        showAlertBox(identity, updated_date);
                    } catch (Exception e) {
                        Log.e("THe exception dialog", e.getLocalizedMessage());
                    }
                } else {
                    Toast.makeText(getActivity(), "Sorry no active network Available\n Please connect to internet and try again!", Toast.LENGTH_LONG).show();


                }
                return true;
            }
        });*/


        /*list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (new Upload().isOnline(getActivity())) {
                    room_flat = ((TextView) view.findViewById(R.id.txt_room_flat)).getText().toString();
                    district = ((TextView) view.findViewById(R.id.txt_district)).getText().toString();
                    city = ((TextView) view.findViewById(R.id.txt_city)).getText().toString();
                    price = ((TextView) view.findViewById(R.id.txt_price)).getText().toString();
                    contact = ((TextView) view.findViewById(R.id.txt_contact)).getText().toString();
                    desc = ((TextView) view.findViewById(R.id.txt_desc)).getText().toString();
                    Date = ((TextView) view.findViewById(R.id.txt_date)).getText().toString();
                    String EmailAddress = ((TextView) view.findViewById(R.id.emailAddress)).getText().toString();
                    String image1 = ((TextView) view.findViewById(R.id.image__1)).getText().toString();
                    String image2 = ((TextView) view.findViewById(R.id.image__2)).getText().toString();
                    String image3 = ((TextView) view.findViewById(R.id.image__3)).getText().toString();
                    String image4 = ((TextView) view.findViewById(R.id.image__4)).getText().toString();

                    fullListView fullListView = new fullListView();
                    Bundle bundle = new Bundle();
                    bundle.putString("room_flat", room_flat);
                    bundle.putString("district", district);
                    bundle.putString("city", city);
                    bundle.putString("price", price);
                    bundle.putString("contact", contact);
                    bundle.putString("desc", desc);
                    bundle.putString("date", Date);
                    bundle.putString("email_address", EmailAddress);
                    bundle.putString("image1", image1);
                    bundle.putString("image2", image2);
                    bundle.putString("image3", image3);
                    bundle.putString("image4", image4);
                    fullListView.setArguments(bundle);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_navigation_drawer, fullListView);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.addToBackStack(null);
                    ft.commit();
                    // getDataFromListView();
                } else {
                    Toast.makeText(getActivity(), "Sorry no active network Available\n Please connect to internet and try again!", Toast.LENGTH_LONG).show();

                }

            }
        });*/
        try {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Profile");
        } catch (Exception e) {
            Log.e("Exception in title", e.getMessage());
        }
        return view;

    }

    private void changeProfile() {
       /* SharedPreferences prefs = getActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("USERNAME", "");
        editor.putString("PASSWORD", "");
        editor.commit();
        Intent intent=new Intent(getActivity(),signIn_signUp.class);
        getActivity().startActivity(intent);*/
       /* EMAIL.setText(jsonObject.optString("email"));
        NAME.setText(jsonObject.optString("first_name") + "\t" + jsonObject.optString("last_name"));
        ADDRESS.setText(jsonObject.optString("district") + "\t" + jsonObject.optString("city"));
        CONTACT.setText(jsonObject.optString("contact"));*/
        updateProfile updateProfile = new updateProfile();

        Bundle bundle = new Bundle();
        bundle.putString("first_name",sendFirstName );
        bundle.putString("last_name",sendLastName);
        bundle.putString("gender",sendGender );
        bundle.putString("district", sendDistrict);
        bundle.putString("city",sendCity );
        bundle.putString("contact", sendContact);
        bundle.putString("email_address",sendEmail );
        bundle.putString("password",sendPassword);
        updateProfile.setArguments(bundle);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_navigation_drawer, updateProfile);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
    }

    /*private void updateContent(final String id, final String date) {

        new AsyncTask<Void, Void, String>() {


            protected void onPreExecute() {

            }

            @Override
            protected String doInBackground(Void... params) {


                return null;
            }


            @Override
            protected void onPostExecute(String msg) {
                try {
                    params.put("id", id);
                    params.put("date_send", date);
                    params.put("email_address", Email);
                    makeHTTPCall();
                } catch (Exception e) {

                }
            }


        }.execute(null, null, null);
    }

    public void makeHTTPCall() {
        AsyncHttpClient client = new AsyncHttpClient();
        try {
            client.post("https://isochasmic-circuits.000webhostapp.com/update_delete_uploaded.php",
                    params, new AsyncHttpResponseHandler() {

                        @Override
                        public void onSuccess(String response) {
                            // Hide Progress Dialog

                            Toast.makeText(getActivity().getBaseContext(), response,
                                    Toast.LENGTH_LONG).show();
                        }

                        public void onFailure(int statusCode, Throwable error,
                                              String content) {

                            if (statusCode == 404) {
                                Toast.makeText(getActivity().getBaseContext(),
                                        "Requested resource not found",
                                        Toast.LENGTH_LONG).show();
                            }
                            // When Http response code is '500'
                            else if (statusCode == 500) {
                                Toast.makeText(getActivity().getBaseContext(),
                                        "Something went wrong at server end",
                                        Toast.LENGTH_LONG).show();
                            }
                            // When Http response code other than 404, 500
                            else {
                                Toast.makeText(
                                        getActivity().getBaseContext(),
                                        "Error Occured n Most Common Error: n1. Device not connected to Internetn2." +
                                                " Web App is not deployed in App servern3. App server is not running" +
                                                "n HTTP Status code : "
                                                + statusCode, Toast.LENGTH_LONG)
                                        .show();

                            }
                        }
                    });
        } catch (Exception e) {
            Log.e("Error posting image", e.getMessage());

        }


    }
*/
    /*private void showAlertBox(final String id, final String date) {
        try {

            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setTitle("Modify History");
            alertDialog.setMessage("Select your action");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "update",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            updateContent(id, date);
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "delete",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            updateContent(id, "");
                        }
                    });
            alertDialog.show();
        } catch (Exception e) {
            Toast.makeText(getActivity().getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("Finally here", e.getMessage());
        }
    }*/


    protected void getImageUrl(String imgUrl) {
        imagePreview preview = new imagePreview();
        Bundle bundle = new Bundle();
        bundle.putString("image", imgUrl);
        preview.setArguments(bundle);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_navigation_drawer, preview);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
    }

   /* public boolean isOnline() {
        ConnectivityManager cm;
        NetworkInfo netInfo = null;

        try {
            cm =
                    (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            netInfo = cm.getActiveNetworkInfo();
        } catch (Exception e) {

        }
        return netInfo != null && netInfo.isConnected();

    }*/

    private class JsonTask extends AsyncTask<String, Object, String> {

        @Override
        protected void onPreExecute() {
            //  super.onPreExecute();

           /* try {
                progressDialog = new ProgressDialog(getActivity().getBaseContext());
                progressDialog.setMessage("Please wait");
                progressDialog.setCancelable(false);
                progressDialog.show();
            } catch (Exception e) {
                Log.e("The is", e.getLocalizedMessage());
            }*/

        }

        @Override
        protected String doInBackground(String... arg0) {

            String result;
            try {

                URL url = new URL("https://isochasmic-circuits.000webhostapp.com/getProfile.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String postData = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(Email, "UTF-8");
                writer.write(postData);
                writer.flush();
                writer.close();
                outputStream.close();
                InputStream stream = httpURLConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "iso-8859-1"));
                result = "";
                String line;
                while ((line = reader.readLine()) != null) {
                    result += line;
                }
                reader.close();
                stream.close();
                httpURLConnection.disconnect();
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            System.out.println("User profile"+result);

            try {
                JSONArray jsonArray = new JSONArray(result);
                // looping through All Contacts
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    /*String ID = jsonObject.optString("ID");
                    String first_name = jsonObject.optString("first_name");
                    String last_name = jsonObject.optString("last_name");
                    String email_address = jsonObject.optString("email");
                    String password = jsonObject.optString("password");
                    String contact = jsonObject.optString("contact");
                    String district = jsonObject.optString("district");
                    String city = jsonObject.optString("city");
                    String gender = jsonObject.optString("gender");*/
                    // tmp hash map for single student_info


                     sendFirstName=jsonObject.optString("first_name");
                    sendLastName=jsonObject.optString("last_name");
                    sendEmail=jsonObject.optString("email");
                    sendPassword=jsonObject.optString("password");
                    sendGender=jsonObject.optString("gender");
                    sendDistrict=jsonObject.optString("district");
                    sendCity=jsonObject.optString("city");
                    sendContact=jsonObject.optString("contact");


                    EMAIL.setText(jsonObject.optString("email"));
                    NAME.setText(jsonObject.optString("first_name") + "\t" + jsonObject.optString("last_name"));
                    ADDRESS.setText(jsonObject.optString("district") + "\t" + jsonObject.optString("city"));
                    CONTACT.setText(jsonObject.optString("contact"));
                    edit_profile.setEnabled(true);


                }
            } catch (final JSONException e) {


            }
        }


    }



    /*private class DisplayOwnUploads extends AsyncTask<String, String, List<Model_Class>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {

                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Please wait...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            } catch (Exception e) {
                // Log.e("The error is",e.getMessage());
            }
        }

        @Override
        protected List<Model_Class> doInBackground(String... params) {
            List<Model_Class> list_display = new ArrayList<>();
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String getUpload = "https://isochasmic-circuits.000webhostapp.com/for_search_results.php";
            String jsonStr = sh.makeServiceCall(getUpload);
            if (jsonStr != null) {
                try {
                    JSONArray jsonArray = new JSONArray(jsonStr);

                    // looping through All Contacts
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);
                        Model_Class model_class = new Model_Class();

                        *//*String ID = jsonObject.optString("ID");
                        String room_flat = jsonObject.optString("room_flat");
                        String district = jsonObject.optString("district");
                        String getCity = jsonObject.optString("city");
                        String price = jsonObject.optString("price");
                        String contact = jsonObject.optString("contact");
                        String description = jsonObject.optString("description");
                        String date = jsonObject.optString("date");
                        String email = jsonObject.optString("email");
                        String image1 = jsonObject.optString("image1");
                        String image2 = jsonObject.optString("image2");
                        String image3 = jsonObject.optString("image3");
                        String image4 = jsonObject.optString("image4");

                        // tmp hash map for single user
                        HashMap<String, String> ownUploadList = new HashMap<>();*//*

                        // adding each child node to HashMap key => value

                        if (c.optString("email").equalsIgnoreCase(Email)) {
                           *//* ownUploadList.put("id", ID);
                            ownUploadList.put("room_flat", room_flat);
                            ownUploadList.put("district", district);
                            ownUploadList.put("city", getCity);
                            ownUploadList.put("price", price);
                            ownUploadList.put("contact", contact);
                            ownUploadList.put("description", description);
                            ownUploadList.put("date", date);
                            ownUploadList.put("email", email);
                            ownUploadList.put("image1", image1);
                            ownUploadList.put("image2", image2);
                            ownUploadList.put("image3", image3);
                            ownUploadList.put("image4", image4);

                            upload_list.add(ownUploadList);*//*
                            model_class.setId(c.optString("ID"));
                            model_class.setCity(c.optString("city"));
                            model_class.setContact(c.optString("contact"));
                            model_class.setDescription(c.optString("description"));
                            model_class.setDistrict(c.optString("district"));
                            model_class.setRoom_flat(c.optString("room_flat"));
                            model_class.setDate(c.optString("date"));
                            model_class.setEmail(c.optString("email"));
                            model_class.setImage1(c.optString("image1"));
                            model_class.setImage2(c.optString("image2"));
                            model_class.setImage3(c.optString("image3"));
                            model_class.setImage4(c.optString("image4"));
                            model_class.setPrice(c.optString("price"));
                            model_class.setNo(c.optString("no"));
                            model_class.setLike_count(c.optString("like_count"));
                            model_class.setDislike_count(c.optString("dislike_count"));
                            model_class.setLiked_by(c.optString("liked_by"));
                            model_class.setDisliked_by(c.optString("disliked_by"));
                            list_display.add(model_class);
                        }
                    }
                } catch (final JSONException e) {

                }
            }

            return list_display;
        }

        @Override
        protected void onPostExecute(List<Model_Class> result) {
            super.onPostExecute(result);

            if (progressDialog.isShowing())
                progressDialog.dismiss();
            try {
               *//* ListAdapter adapter = new SimpleAdapter(
                        getActivity(), upload_list,
                        R.layout.fragment_main, new String[]{"id", "room_flat", "district", "city",
                        "price", "contact", "description", "date", "email", "image1", "image2", "image3", "image4"}, new int[]{
                        R.id.ID, R.id.txt_room_flat, R.id.txt_district, R.id.txt_city, R.id.txt_price,
                        R.id.txt_contact, R.id.txt_desc, R.id.txt_date, R.id.emailAddress, R.id.image__1, R.id.image__2, R.id.image__3, R.id.image__4});
                list_view.setAdapter(adapter);*//*
                profileAdapter adapter = new profileAdapter(getActivity(), R.layout.fragment_main, result);
                list_view.setAdapter(adapter);
            } catch (Exception e) {
                Log.e("The adapter error ", e.getMessage());
            }

        }


    }*/


    private class DisplayOwnUploads extends AsyncTask<String, List<Model_Class>, String> {
        AlertDialog alertDialog;

        DisplayOwnUploads(Context ctx) {
            context = ctx;
        }

        @Override
        protected void onPreExecute() {

            try {
                // Showing progress dialog
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Please wait...");
           /* progressDialog.setCancelable(false);*/
                progressDialog.setCancelable(false);
                progressDialog.show();
                alertDialog = new AlertDialog.Builder(getActivity()).create();

            } catch (Exception e) {
                // Log.e("The error is",e.getMessage());
            }
        }

        @Override
        protected String doInBackground(String... params) {
            List<Model_Class> list_display = new ArrayList<>();
            String result;
            try {

                URL url = new URL("https://isochasmic-circuits.000webhostapp.com/get_own_upload.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String postData = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(Email, "UTF-8");
                writer.write(postData);
                writer.flush();
                writer.close();
                outputStream.close();
                InputStream stream = httpURLConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "iso-8859-1"));
                result = "";
                String line;
                while ((line = reader.readLine()) != null) {
                    result += line;
                }
                reader.close();
                stream.close();
                httpURLConnection.disconnect();
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            //Log.e("Gotten response", String.valueOf(result));
            System.out.println("Gotten response" + result);
            // super.onPostExecute(result);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }


            // System.out.println("List data"+myList);
            try {
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject c = jsonArray.getJSONObject(i);
                    Model_Class model_class = new Model_Class();
                    model_class.setId(c.optString("ID"));
                    model_class.setCity(c.optString("city"));
                    model_class.setContact(c.optString("contact"));
                    model_class.setDescription(c.optString("description"));
                    model_class.setDistrict(c.optString("district"));
                    model_class.setRoom_flat(c.optString("room_flat"));
                    model_class.setDate(c.optString("date"));
                    model_class.setEmail(c.optString("email"));
                    model_class.setImage1(c.optString("image1"));
                    model_class.setImage2(c.optString("image2"));
                    model_class.setImage3(c.optString("image3"));
                    model_class.setImage4(c.optString("image4"));
                    model_class.setPrice(c.optString("price"));
                    model_class.setLike_count(c.optString("like_count"));
                    model_class.setDislike_count(c.optString("dislike_count"));
                    model_class.setLiked_by(c.optString("liked_by"));
                    model_class.setDisliked_by(c.optString("disliked_by"));
                    model_class.setNo(c.optString("no"));
                    list_display.add(model_class);

                    //myList.add(model_class);
                    // System.out.println(c);


                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //System.out.println("Gotten response"+list_display);


       /*     try {
                if (result.size() == 0) {
                    alertDialog.setTitle("Sorry");
                    alertDialog.setMessage("Sorry no result found for your query \n Please broad your search");
                    alertDialog.show();
                    Search search = new Search();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_navigation_drawer, search);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.addToBackStack(null);
                    ft.commit();
                } *//*else if (result.size() > 1) {
                    final Button show_more = new Button(getActivity());
                    show_more.setText("Load more");
                    list_view.addFooterView(show_more);
                    show_more.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showAction();
                        }
                    });
                }*//*
            } catch (Exception e) {
                Log.e("Finally", e.getMessage());
            }*/
            try {
             /*   ListAdapter adapter = new SimpleAdapter(
                        getActivity(), room_list1,
                        R.layout.fragment_main, new String[]{"room_flat", "district", "city",
                        "price", "contact", "description", "date", "email", "image1", "image2", "image3", "image4"}, new int[]{
                        R.id.txt_room_flat, R.id.txt_district, R.id.txt_city, R.id.txt_price,
                        R.id.txt_contact, R.id.txt_desc, R.id.txt_date, R.id.emailAddress, R.id.image__1, R.id.image__2, R.id.image__3, R.id.image__4});

                list_view.setAdapter(adapter);
                */
                profileAdapter adapter = new profileAdapter(getActivity(), R.layout.fragment_main, list_display);
                list_view.setAdapter(adapter);
            } catch (Exception e) {
                Log.e("Display all exc", e.getMessage());
            }
        }


    }
}






