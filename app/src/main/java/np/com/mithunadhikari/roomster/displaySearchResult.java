package np.com.mithunadhikari.roomster;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class displaySearchResult extends Fragment {
    private static String url = "https://isochasmic-circuits.000webhostapp.com/for_search_results.php";
    ArrayList<HashMap<String, String>> room_list1;
    ProgressDialog progressDialog;
    String Room_flat, District, City, MinPrice, MaxPrice;
    String room_flat, district, city, price, contact, desc, Date;
    int current_page = 1;
    private ListView list_view;
    private List<Model_Class> list_display;

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
        new JsonTask().execute();

/*
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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


                if (image4.length() < 1) {
                    image4 = "f";
                }
                if (image3.length() < 1) {
                    image3 = "u";
                }
                if (image2.length() < 1) {
                    image2 = "c";
                }
                if (image1.length() < 1) {
                    image1 = "k";
                }

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

            }
        });*/


        return view;
    }

    /*private void showAction() {

        Toast.makeText(getActivity(), "here we go finally got this", Toast.LENGTH_LONG).show();
        current_page++;
        new JsonTask().execute();


    }*/

    private class JsonTask extends AsyncTask<String, String, List<Model_Class>> {
        AlertDialog alertDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
        protected List<Model_Class> doInBackground(String... params) {
            List<Model_Class> list_display = new ArrayList<>();

            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);
            JSONObject c;

            if (jsonStr != null) {
                try {
                    JSONArray jsonArray = new JSONArray(jsonStr);
                    // looping through All Contacts
                    for (int i = 0; i < jsonArray.length(); i++) {
                         c = jsonArray.getJSONObject(i);
                        Model_Class model_class = new Model_Class();
                        /*String room_flat = c.optString("room_flat");
                        String district = c.optString("district");
                        String city = c.optString("city");
                        String price = c.optString("price");


                        HashMap<String, String> room_list = new HashMap<>();

*/
                        if (!(City.equalsIgnoreCase("area"))) {
                            if ((Room_flat.equalsIgnoreCase(c.optString("room_flat"))) && (District.equalsIgnoreCase(c.optString("district")))
                                    && (City.equalsIgnoreCase(c.optString("city"))) &&
                                    (Double.parseDouble(c.optString("price")) >= Double.parseDouble(MinPrice))
                                    && (Double.parseDouble(c.optString("price")) <= Double.parseDouble(MaxPrice))) {
                            /*room_list.put("room_flat", room_flat);
                            room_list.put("district", district);
                            room_list.put("city", city);
                            room_list.put("price", price);
                            room_list.put("contact", contact);
                            room_list.put("description", description);
                            room_list.put("date", date);
                            room_list.put("email", email);
                            room_list.put("image1", image1);
                            room_list.put("image2", image2);
                            room_list.put("image3", image3);
                            room_list.put("image4", image4);

                            room_list1.add(room_list);*/


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
                            }
                        } else {

                            if ((Room_flat.equalsIgnoreCase(c.optString("room_flat"))) && (District.equalsIgnoreCase(c.optString("district")))
                                    && (Double.parseDouble(c.optString("price")) >= Double.parseDouble(MinPrice))
                                    && (Double.parseDouble(c.optString("price")) <= Double.parseDouble(MaxPrice))) {
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


                            }
                        }

                        // adding room_info to romm list
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
                } /*else if (result.size() > 1) {
                    final Button show_more = new Button(getActivity());
                    show_more.setText("Load more");
                    list_view.addFooterView(show_more);
                    show_more.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showAction();
                        }
                    });
                }*/
            } catch (Exception e) {
                Log.e("Finally", e.getMessage());
            }
            try {
             /*   ListAdapter adapter = new SimpleAdapter(
                        getActivity(), room_list1,
                        R.layout.fragment_main, new String[]{"room_flat", "district", "city",
                        "price", "contact", "description", "date", "email", "image1", "image2", "image3", "image4"}, new int[]{
                        R.id.txt_room_flat, R.id.txt_district, R.id.txt_city, R.id.txt_price,
                        R.id.txt_contact, R.id.txt_desc, R.id.txt_date, R.id.emailAddress, R.id.image__1, R.id.image__2, R.id.image__3, R.id.image__4});

                list_view.setAdapter(adapter);*/
                ItemAdapter adapter = new ItemAdapter(getActivity(), R.layout.fragment_main, result);
                list_view.setAdapter(adapter);
            } catch (Exception e) {
                Log.e("Display all exc", e.getMessage());
            }
        }


    }


}
