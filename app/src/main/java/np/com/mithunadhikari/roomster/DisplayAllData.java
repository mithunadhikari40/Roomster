package np.com.mithunadhikari.roomster;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DisplayAllData extends Fragment {
    ProgressDialog progressDialog;
    String room_flat, district, city, price, contact, desc, Date;
    SharedPreferences myPrefs;
    String emailAddress;
    Button show_more;
    int current_page = 1;
    private ListView list_view;
    private List<Model_Class> list_display;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        list_display = new ArrayList<>();
        View view = inflater.inflate(R.layout.list_view, container, false);
        list_view = (ListView) view.findViewById((R.id.list_view));
        show_more = new Button(getActivity());
        show_more.setText("Load more");
        show_more.setTag("footer");
        list_view.addFooterView(show_more);
        show_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current_page++;
                new JsonTask().execute();
            }
        });

        new JsonTask().execute();
        return view;
    }



    private void showAction() {

    }

    private class JsonTask extends AsyncTask<String, String, List<Model_Class>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected List<Model_Class> doInBackground(String... params) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response

            String jsonStr = sh.makeServiceCall("https://isochasmic-circuits.000webhostapp.com/getAllData.php?page=" + current_page);
            List<Model_Class> list_display = new ArrayList<>();

            if (jsonStr != null) {
                try {

                    JSONArray jsonArray = new JSONArray(jsonStr);
                    // looping through All information
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
                        model_class.setNo(c.optString("no"));
                        model_class.setLike_count(c.optString("like_count"));
                        model_class.setDislike_count(c.optString("dislike_count"));
                        model_class.setLiked_by(c.optString("liked_by"));
                        model_class.setDisliked_by(c.optString("disliked_by"));



                        list_display.add(model_class);
                    }
                } catch (final JSONException e) {

                }
            }
            return list_display;
        }

        @Override
        protected void onPostExecute(List<Model_Class> result) {
            super.onPostExecute(result);
            try {

                // Dismiss the progress dialog
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                /**
                 * Updating parsed JSON data into ListView
                 */
                ItemAdapter adapter = new ItemAdapter(getActivity(), R.layout.fragment_main, result);
                list_view.setAdapter(adapter);
/*
                show_more.setVisibility(View.INVISIBLE);
*/
                int size = result.size();
                /*show_more.setTag("footer");
                show_more.setVisibility(View.INVISIBLE);*/
                if (size >= 3) {

                } else {

                    for (int i = 0; i < list_view.getFooterViewsCount(); i++) {
                        View v = list_view.findViewWithTag("footer");
                        list_view.removeFooterView(v);
                    }
                    show_more.setVisibility(View.INVISIBLE);

                }
            } catch (Exception e) {
                Log.e("Display ", e.getMessage());
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Roomster");
        if(getView() == null){
            return;
        }

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    // handle back button's click listener
                    DisplayAllData displayAllData = new DisplayAllData();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_navigation_drawer, displayAllData);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.addToBackStack(null);
                    ft.commit();
                    return true;
                }
                return false;
            }
        });
    }
}




