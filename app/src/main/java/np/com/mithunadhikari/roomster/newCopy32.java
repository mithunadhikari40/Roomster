package np.com.mithunadhikari.roomster;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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

public class newCopy32 extends Fragment {
    private static String search_url = "https://isochasmic-circuits.000webhostapp.com/for_search_results.php";
    ArrayList<HashMap<String, String>> room_list1;
    ProgressDialog progressDialog;
    static Context context;
    String Room_flat, District, City, MinPrice, MaxPrice;
    String room_flat, district, city, price, contact;
    private ListView list_view;
    private List<Model_Class> list_display;

    private class JsonTask extends AsyncTask<String, List<Model_Class>, String>  {
        AlertDialog alertDialog;
        JsonTask(Context ctx) {
            context = ctx;
        }
        @Override
        protected void onPreExecute () {

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
        protected String doInBackground (String...params){
            List<Model_Class> list_display = new ArrayList<>();
            String result;
            try {

                URL url = new URL(search_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String postData = URLEncoder.encode("room_flat", "UTF-8") + "=" + URLEncoder.encode(Room_flat, "UTF-8") + "&"
                        + URLEncoder.encode("district", "UTF-8") + "=" + URLEncoder.encode(District, "UTF-8") + "&"
                        + URLEncoder.encode("area", "UTF-8") + "=" + URLEncoder.encode(City, "UTF-8") + "&"
                        + URLEncoder.encode("minPrice", "UTF-8") + "=" + URLEncoder.encode(MinPrice, "UTF-8") + "&"
                        + URLEncoder.encode("maxPrice", "UTF-8") + "=" + URLEncoder.encode(MaxPrice, "UTF-8");
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
        protected void onPostExecute (String result) {
            //Log.e("Gotten response", String.valueOf(result));
            System.out.println("Gotten response"+result);
           // super.onPostExecute(result);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }


           // System.out.println("List data"+myList);
            try {
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject  c = jsonArray.getJSONObject(i);
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
                ItemAdapter adapter = new ItemAdapter(getActivity(), R.layout.fragment_main, list_display);
                list_view.setAdapter(adapter);
            } catch (Exception e) {
                Log.e("Display all exc", e.getMessage());
            }
        }


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_view, container, false);
        list_display = new ArrayList<>();

        list_view = (ListView) view.findViewById((R.id.list_view));
        room_list1 = new ArrayList<>();
        Room_flat = getArguments().getString("room_flat");
        District = getArguments().getString("district");
        City = getArguments().getString("city");
        MinPrice = getArguments().getString("minPrice");
        MaxPrice = getArguments().getString("maxPrice");
        System.out.println("Gotten Data"+Room_flat+District+City+MinPrice+MaxPrice);
        new JsonTask(getActivity()).execute();
        return view;
    }




}

