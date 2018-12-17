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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class searchResultsCopy extends Fragment {
    ArrayList<HashMap<String, String>> room_list1;
    ProgressDialog progressDialog;
    String Room_flat, District, City, MinPrice, MaxPrice;
    String room_flat, district, city, price, contact;
    private ListView list_view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_view, container, false);
        List<Model_Class> list_display = new ArrayList<>();

        list_view = (ListView) view.findViewById((R.id.list_view));
        room_list1 = new ArrayList<>();
        Room_flat = getArguments().getString("room_flat");
        District = getArguments().getString("district");
        City = getArguments().getString("city");
        MinPrice = getArguments().getString("minPrice");
        MaxPrice = getArguments().getString("maxPrice");
        new JsonTask().execute();


        return view;
    }


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

            HashMap<String, String> room_list = new HashMap<>();
            ArrayList<String> arrayList1 = new ArrayList<>();
            HttpHandler sh = new HttpHandler();
            String url = "https://isochasmic-circuits.000webhostapp.com/for_search.php";
            String jsonStr = sh.makeServiceCall(url);
            Model_Class model_class = new Model_Class();
            if (jsonStr != null) {
                try {
                    JSONArray jsonArray = new JSONArray(jsonStr);
                    // looping through All Contacts
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);

                        String room_flat = c.optString("room_flat");
                        String district = c.optString("district");
                        String city = c.optString("city");
                        String price = c.optString("price");

                        if ((Room_flat.equalsIgnoreCase(c.optString("room_flat"))) && (District.equalsIgnoreCase(c.optString("district"))) &&
                                (Double.parseDouble(c.optString("price")) >= Double.parseDouble(MinPrice))
                                && (Double.parseDouble(c.optString("price")) <= Double.parseDouble(MaxPrice))) {
                            arrayList1.add(c.optString("city"));
                        }

                    }

                } catch (final JSONException e) {

                }
            }
            ArrayList<Double> returned_percent = sort_array(arrayList1, City);
            //System.out.println("Percentage"+returned_percent);
            HashMap<String, Double> map = new HashMap<>();


            for (int iii = 0; iii < arrayList1.size(); iii++) {
                map.put(arrayList1.get(iii), returned_percent.get(iii));
                System.out.println("Hello"+arrayList1.get(iii)+"\t"+ returned_percent.get(iii));
            }
            ValueComparator bvc = new ValueComparator(map);
            TreeMap<String, Double> sorted_map = new TreeMap<>(bvc);
            sorted_map.putAll(map);
            System.out.println("Sorted array" + sorted_map);
            for (Map.Entry<String, Double> entry : sorted_map.entrySet()) {
                //System.out.println("No harm\t"+entry.getKey() + "/" + entry.getValue());
                System.out.println("Keys are:\t" + entry.getKey());
                model_class.setCity(entry.getKey());
                list_display.add(model_class);
            }

            //System.out.println("Current key"+sorted_map);


                      /*  model_class.setId(c.optString("ID"));
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
                        list_display.add(model_class);*/
                           /* }*/


            // adding room_info to romm list


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

        private ArrayList sort_array(ArrayList arrayList1, String str1) {
            int count;
            int length1 = str1.length();
            String string1;
            ArrayList<Double> percentRecord = new ArrayList<>();
            for (int ii = 0; ii < arrayList1.size(); ii++) {
                string1 = str1;
                count = 0;
                String str2 = (String) arrayList1.get(ii);
                int length2 = str2.length();
                if (length1 < length2) {
                    String temp = str2;
                    str2 = string1;
                    string1 = temp;
                }

                try {
                    for (int j = 0; j < str2.length(); j++) {
                        String match2 = str2.substring(j, j + 1);
                        if (string1.contains(match2)) {
                            count++;
                        }
                    }
                } catch (Exception e) {
                    //display_percentage.setText(e.getMessage());
                }
                double percent;
                if (length1 >= length2) {
                    percent = (count * 100) / length1;
                } else {
                    percent = (count * 100) / length2;
                }
                percentRecord.add(percent);
            }
            Comparator mycomparator = Collections.reverseOrder();
            Collections.sort(percentRecord, mycomparator);

            return percentRecord;
        }


    }


    class ValueComparator implements Comparator<String> {

        Map<String, Double> base;

        ValueComparator(Map<String, Double> base) {
            this.base = base;
        }

        // Note: this comparator imposes orderings that are inconsistent with
        // equals.
        @Override
        public int compare(String a, String b) {
            if (base.get(a) <= base.get(b)) {
                return 1;
            } else {
                return -1;
            } // returning 0 would merge keys
        }


    }
}
