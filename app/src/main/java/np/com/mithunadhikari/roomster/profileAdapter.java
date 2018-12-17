package np.com.mithunadhikari.roomster;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

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
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class profileAdapter extends ArrayAdapter {
    static Context context;
    public List<Model_Class> itemList;
    SharedPreferences myPrefs;
    String emailAddress;
    String room_flat, district, city, price, contact, desc, Date;
    String Email;
    ProgressDialog progressDialog;
    RequestParams params = new RequestParams();
    boolean check = true;
    boolean only_once = true;
    String profileFirstUse;
    private int like_no, dislike_no;
    private int resource;
    private LayoutInflater inflater;

    public profileAdapter(Context context, int resource, List<Model_Class> objects) {
        super(context, resource, objects);
        itemList = objects;
        this.resource = resource;
        inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        myPrefs = getContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        profileFirstUse = myPrefs.getString("first_profile_use", null);

        Email = myPrefs.getString("USERNAME", null);
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.fragment_main, null);
            holder.txt_city = (TextView) convertView.findViewById(R.id.txt_city);
            holder.txt_district = (TextView) convertView.findViewById(R.id.txt_district);
            holder.txt_price = (TextView) convertView.findViewById(R.id.txt_price);
            holder.txt_id = (TextView) convertView.findViewById(R.id.ID);
            holder.txt_date = (TextView) convertView.findViewById(R.id.txt_date);
            holder.txt_description = (TextView) convertView.findViewById(R.id.txt_desc);
            holder.txt_email = (TextView) convertView.findViewById(R.id.emailAddress);
            holder.txt_image1 = (TextView) convertView.findViewById(R.id.image__1);
            holder.txt_image2 = (TextView) convertView.findViewById(R.id.image__2);
            holder.txt_image3 = (TextView) convertView.findViewById(R.id.image__3);
            holder.txt_image4 = (TextView) convertView.findViewById(R.id.image__4);
            holder.room_flat = (TextView) convertView.findViewById(R.id.txt_room_flat);
            holder.txt_contact = (TextView) convertView.findViewById(R.id.txt_contact);
            holder.count_like = (TextView) convertView.findViewById(R.id.like_count);
            holder.count_dislike = (TextView) convertView.findViewById(R.id.dislike_count);
            holder.like = (TextView) convertView.findViewById(R.id.like_text);
            holder.dislike = (TextView) convertView.findViewById(R.id.dislike_text);
            holder.likedBy = (TextView) convertView.findViewById(R.id.like_by);
            holder.dislikedBy = (TextView) convertView.findViewById(R.id.dislike_by);
            holder.no_ = (TextView) convertView.findViewById(R.id.no);
            holder.txt_no_ = (TextView) convertView.findViewById(R.id.txt_no);
            holder.hint = (ImageView) convertView.findViewById(R.id.list_view_image);
            holder.hint_text = (TextView) convertView.findViewById(R.id.hint_text);
           /* holder.showMore= (Button) convertView.findViewById(R.id.show_more);*/
            if (profileFirstUse==null) {
                if (only_once) {
                    holder.hint.setVisibility(View.VISIBLE);
                    holder.hint_text.setVisibility(View.VISIBLE);
                    only_once = false;
                } else {
                    holder.hint_text.setVisibility(View.INVISIBLE);
                    holder.hint.setVisibility(View.INVISIBLE);
                }
            } else {
                holder.hint_text.setVisibility(View.INVISIBLE);
                holder.hint.setVisibility(View.INVISIBLE);
            }
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Model_Class current = itemList.get(position);
        //data insert
        try {
            holder.txt_contact.setText(current.getContact());
            holder.txt_id.setText(current.getId());
            holder.room_flat.setText(current.getRoom_flat());
            holder.txt_district.setText(current.getDistrict());
            holder.txt_date.setText(current.getDate());
            holder.txt_description.setText(current.getDescription());
            holder.txt_city.setText(current.getCity());
            holder.txt_price.setText(current.getPrice());
            holder.txt_email.setText(current.getEmail());
            holder.txt_image1.setText(current.getImage1());
            holder.txt_image2.setText(current.getImage2());
            holder.txt_image3.setText(current.getImage3());
            holder.txt_image4.setText(current.getImage4());
            holder.count_like.setText(current.getLike_count());
            holder.count_dislike.setText(current.getDislike_count());
            holder.likedBy.setText(current.getLiked_by());
            holder.txt_no_.setText(current.getNo());
            holder.no_.setText("No of " + current.getRoom_flat() + "s");
            holder.dislikedBy.setText(current.getDisliked_by());
            String liked_list = current.getLiked_by();
            final String disliked_list = current.getDisliked_by();
            if (liked_list.contains(Email)) {
                holder.like.setTextColor(Color.BLUE);
            } else if (disliked_list.contains(Email)) {
                holder.dislike.setTextColor(Color.BLUE);
            }

        } catch (Exception e) {
            Log.e("The adapter like", e.getMessage());
        }

       /* Button show_more =new Button(getContext());
        show_more.setText("Show more");
        show_more.setTextSize(12);
        show_more.setVerticalScrollbarPosition();*/

        holder.hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check) {
                    holder.hint_text.setText("\t\t\t  Long click to \n \t\t\t update or \n \t\t\t   delete upload\n\n \t\t\t   tap to dismiss");
                    check = false;
                } else {
                    SharedPreferences prefs = getContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("first_profile_use", "Hello first time user tutorial");
                    editor.apply();
                    holder.hint_text.setVisibility(View.INVISIBLE);
                    holder.hint.setVisibility(View.INVISIBLE);
                }
            }

        });

        // when like is clicked
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    if (new Upload().isOnline(getContext())) {
                        // Toast.makeText(getContext(),String.valueOf(holder.like.getCurrentTextColor()),Toast.LENGTH_LONG).show();
                        if (holder.like.getCurrentTextColor() == Color.BLUE) {
                            holder.like.setTextColor(Color.BLACK);
                            like_no = Integer.parseInt(holder.count_like.getText().toString()) - 1;
                            holder.count_like.setText(String.valueOf(like_no));

                            like_dislike(current.getId(), Email, "remove_like");
                        } else
               /* if (holder.like.getCurrentTextColor() == Color.BLACK)*/ {
                            holder.like.setTextColor(Color.BLUE);
                            like_no = Integer.parseInt(holder.count_like.getText().toString()) + 1;
                            holder.count_like.setText(String.valueOf(like_no));
                            if (holder.dislike.getCurrentTextColor() == Color.BLUE) {
                                holder.dislike.setTextColor(Color.BLACK);
                                dislike_no = Integer.parseInt(holder.count_like.getText().toString()) - 1;
                                holder.count_dislike.setText(String.valueOf(dislike_no));

                                like_dislike(current.getId(), Email, "like_dislike");
                            } else {

                                like_dislike(current.getId(), Email, "like");
                            }
                        }
                    } else {
                        Toast.makeText(getContext(), "No active internet connection available", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Log.e("Gated exception", e.getMessage());
                }
            }
        });

        // when dislike is clicked
        holder.dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (new Upload().isOnline(getContext())) {
                        if (holder.dislike.getCurrentTextColor() == Color.BLUE) {
                            holder.dislike.setTextColor(Color.BLACK);
                            dislike_no = Integer.parseInt(holder.count_dislike.getText().toString()) - 1;
                            holder.count_dislike.setText(String.valueOf(dislike_no));
                            like_dislike(current.getId(), Email, "remove_dislike");

                        } else /*if (holder.dislike.getCurrentTextColor() == Color.BLACK)*/ {
                            holder.dislike.setTextColor(Color.BLUE);
                            dislike_no = Integer.parseInt(holder.count_dislike.getText().toString()) + 1;
                            holder.count_dislike.setText(String.valueOf(dislike_no));
                            if (holder.like.getCurrentTextColor() == Color.BLUE) {
                                holder.like.setTextColor(Color.BLACK);
                                like_no = Integer.parseInt(holder.count_like.getText().toString()) - 1;
                                holder.count_like.setText(String.valueOf(like_no));
                                like_dislike(current.getId(), Email, "dislike_like");
                            } else {

                                like_dislike(current.getId(), Email, "dislike");

                            }
                        }
                    } else {
                        Toast.makeText(getContext(), "No active internet connection available", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Log.e("Geted exeption", e.getMessage());
                }
            }
        });


        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (new Upload().isOnline(getContext())) {
                    try {
                        int new_db_year, new_db_month;
                        String identity = ((TextView) view.findViewById(R.id.ID)).getText().toString();
                        String Date = ((TextView) view.findViewById(R.id.txt_date)).getText().toString();
                        String db_year = Date.substring(0, 4);
                        new_db_year = Integer.parseInt(db_year);
                        int minus_index_first = Date.indexOf("-");
                        String month_day = Date.substring(minus_index_first + 1);
                        int minus_index_sec = month_day.indexOf("-");
                        String db_month = month_day.substring(0, minus_index_sec);
                        String db_day = month_day.substring(minus_index_sec + 1);
                        Toast.makeText(getContext(), db_day, Toast.LENGTH_LONG).show();

                        if (Integer.parseInt(db_month) == 12) {
                            new_db_year = Integer.parseInt(db_year) + 1;
                            new_db_month = 1;
                        } else {
                            new_db_month = Integer.parseInt(db_month) + 1;
                        }
                        String updated_date = new_db_year + "-" + new_db_month + "-" + db_day;
                        showAlertBox(identity, updated_date);

                    } catch (Exception e) {
                        Log.e("Long click", e.getMessage());
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();

                    }

                } else {
                    Toast.makeText(getContext(), "No active internet connection available", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (new Upload().isOnline(getContext())) {
                    myPrefs = getContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                    emailAddress = myPrefs.getString("USERNAME", null);
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
                    String no_of = ((TextView) view.findViewById(R.id.txt_no)).getText().toString();

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
                    bundle.putString("no_of", no_of);

                    fullListView.setArguments(bundle);
                    FragmentActivity activity = null;
                    FragmentTransaction ft = ((FragmentActivity) getContext()).getFragmentManager().beginTransaction();
                    // FragmentTransaction ft = getContext().getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_navigation_drawer, fullListView);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.addToBackStack(null);
                    ft.commit();

                } else {
                    Toast.makeText(getContext(), "No active internet connection available", Toast.LENGTH_LONG).show();
                }


            }
        });


        return convertView;
    }

    private void showAlertBox(final String id, final String date) {
        try {
            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
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
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("Finally here", e.getMessage());
        }
    }


    private void updateContent(final String id, final String date) {
        new AsyncTask<Void, Void, String>() {
            protected void onPreExecute() {
                try {
                    progressDialog = new ProgressDialog(getContext());
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                } catch (Exception e) {
                    // Log.e("The error is",e.getMessage());
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                return null;
            }

            @Override
            protected void onPostExecute(String msg) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
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

                            Toast.makeText(getContext(), response,
                                    Toast.LENGTH_LONG).show();
                        }

                        public void onFailure(int statusCode, Throwable error,
                                              String content) {

                            if (statusCode == 404) {
                                Toast.makeText(getContext(),
                                        "Requested resource not found",
                                        Toast.LENGTH_LONG).show();
                            }
                            // When Http response code is '500'
                            else if (statusCode == 500) {
                                Toast.makeText(getContext(),
                                        "Something went wrong at server end",
                                        Toast.LENGTH_LONG).show();
                            }
                            // When Http response code other than 404, 500
                            else {
                                Toast.makeText(
                                        getContext(),
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


    private void like_dislike(String id, String email, String type) {
        new backGroundWorker(getContext()).execute(id, email, type);

    }


    class ViewHolder {

        ImageView hint;
        TextView hint_text;
        private TextView txt_id, txt_district, txt_city, txt_price, txt_date, txt_description, txt_image1,
                txt_image2, txt_image3, txt_image4, txt_email, room_flat, txt_contact, count_like,
                count_dislike, like, dislike, likedBy, dislikedBy, no_, txt_no_;
       /* private Button showMore;*/

    }


    class backGroundWorker extends AsyncTask<String, Void, String> {
        AlertDialog alertDialog;
        ProgressDialog progressDialog;
        String result;

        backGroundWorker(Context ctx) {
            context = ctx;
        }

        @Override
        protected String doInBackground(String... params) {


            try {
                String loginUrl = "https://isochasmic-circuits.000webhostapp.com/like_flag.php";
                String upload_id = params[0];
                String email = params[1];
                String type = params[2];
                URL url = new URL(loginUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String postData = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(upload_id, "UTF-8") + "&"
                        + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&"
                        + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8");
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
        protected void onPreExecute() {
            try {
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Please wait");
                progressDialog.setCancelable(false);
                progressDialog.show();
                alertDialog = new AlertDialog.Builder(context).create();
            } catch (Exception e) {
                Log.e("like dislike", e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();

            try {
                String message;
                JSONObject jsonResult = new JSONObject(result);
                message = jsonResult.optString("message");
                if (message.contains("request has been completed")) {
                    Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Something went wrong, please try again ", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                Log.e("Second", e.getMessage());
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}
