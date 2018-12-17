package np.com.mithunadhikari.roomster;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class updateProfile extends Fragment {

    /*
        private static final String TAG = updateProfile.class.getSimpleName();
    */
    static Context context;
    private static int RESULT_LOAD_IMG = 1;
    SharedPreferences myPrefs;
    String email_address;
    RequestParams params = new RequestParams();
    ImageView imageView;
    String getFirstName, getLastName, getEmail, getPassword, getGender, getDistrict, getCity, getContact;
    EditText first_name, last_name, contact;
    Spinner district,  gender;
    AutoCompleteTextView city;
    TextView change_email, change_pass, change_photo;
    View view;
    String selectedDistrict, selectedGender;
    String imgPath, fileName;
    ImageView done, cancel;
    TextView show_off;
    String encodedString;
    Bitmap bitmap;
    private ProgressDialog progress;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        view = inflater.inflate(R.layout.update_profile, container, false);

        myPrefs = getActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        email_address = myPrefs.getString("USERNAME", null);
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);


        getFirstName = getArguments().getString("first_name");
        getLastName = getArguments().getString("last_name");
        getGender = getArguments().getString("gender");
        getDistrict = getArguments().getString("district");
        getCity = getArguments().getString("city");
        getContact = getArguments().getString("contact");
        getPassword = getArguments().getString("password");
        getEmail = getArguments().getString("email_address");
        show_off = (TextView) view.findViewById(R.id.toolbar_button);

        done = (ImageView) view.findViewById(R.id.edit_done);
        cancel = (ImageView) view.findViewById(R.id.edit_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reload_page();
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new Upload().isOnline(getActivity())) {
                    update_profile();
                } else {
                    Toast.makeText(getActivity(), "No internet connection available", Toast.LENGTH_LONG).show();
                }
            }
        });
        imageView = (ImageView) view.findViewById(R.id.image_view);

        first_name = (EditText) view.findViewById(R.id.first_name_edit);
        last_name = (EditText) view.findViewById(R.id.last_name_edit);
        contact = (EditText) view.findViewById(R.id.contact_edit);
        district = (Spinner) view.findViewById(R.id.edit_district);
        city = (AutoCompleteTextView) view.findViewById(R.id.edit_city);
        gender = (Spinner) view.findViewById(R.id.edit_gender);
        change_email = (TextView) view.findViewById(R.id.email_edit);
        change_pass = (TextView) view.findViewById(R.id.password_edit);
        city.setHint("Area eg Baneshwor");
        try {
            String set = myPrefs.getString("cities", null);
            //System.out.println("Hello"+set);
            String new_string = set.substring(set.indexOf("[") + 1, set.indexOf("]"));
            List<String> myList = new ArrayList<>(Arrays.asList(new_string.split(",")));
            int size = myList.size();
            String[] mArray = new String[size];
            for(int i=0;i<size;i++){
                mArray[i]=myList.get(i);
                mArray[i]=mArray[i].substring(mArray[i].indexOf("")+1,mArray[i].lastIndexOf("")-1);
                //System.out.println("Finally"+mArray[i].toString());
            }
            ArrayAdapter adapter =
                    new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, mArray);
            city.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        change_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUp("email", "New email address", "Current password");
            }
        });
        change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUp("pass", "New password", "Current password");
            }
        });

        first_name.setText(getFirstName);
        last_name.setText(getLastName);
        contact.setText(getContact);
        Glide.with(getActivity())
                .load("https://isochasmic-circuits.000webhostapp.com/images/profile/profile_picture/" + email_address + ".png")
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .override(50, 50)
                .into(imageView);
        init();


        district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDistrict = parent.getSelectedItem().toString();
               // addCity(selectedDistrict);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getActivity().getBaseContext(), "Please select district", Toast.LENGTH_LONG).show();

            }
        });

        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedGender = parent.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getActivity().getBaseContext(), "Please select genrde", Toast.LENGTH_LONG).show();

            }
        });


        addDistrict();
        addGender();

        return view;
    }


    /*public boolean isOnline() {
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


    private void update_profile() {
        String firstName = first_name.getText().toString();
        String lastName = last_name.getText().toString();
        String Contact = contact.getText().toString();
        if (!((firstName.equals(getFirstName)) && (lastName.equals(getLastName)) && (Contact.equals(getContact))
                && (imgPath == null)  && (selectedDistrict.equals("District"))
                && (selectedGender.equals("Gender")))) {
            if (firstName.length() >= 3) {
                if (lastName.length() >= 3) {
                    if (Contact.length() >= 9) {
                        if (city.getText().length()>2) {
                            progress = new ProgressDialog(getActivity());
                            progress.setMessage("Please wait");
                            progress.setCancelable(false);
                            progress.show();
                            encodeImagetoString();
                        }else{
                            Toast.makeText(getActivity(), "Enter correct area", Toast.LENGTH_LONG).show();

                        }
                    } else {
                        Toast.makeText(getActivity(), "Contact too short", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Last name too short", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getActivity(), "First name too short", Toast.LENGTH_LONG).show();
            }

        }
    }

    private void encodeImagetoString() {


        new AsyncTask<Void, Void, String>() {


            protected void onPreExecute() {

            }


            @Override
            protected String doInBackground(Void... params) {
                BitmapFactory.Options options;
                try {
                    options = new BitmapFactory.Options();
                    options.inSampleSize = 3;
                    if (imgPath != null) {

                        bitmap = BitmapFactory.decodeFile(imgPath, options);

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();

                        // Must compress the Image to reduce image size to make upload easy

                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                        byte[] byte_arr = stream.toByteArray();
                        // Encode Image to String Base64.DEFAULT

                        encodedString = Base64.encodeToString(byte_arr, 0);

                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                    } else {
                        encodedString = null;
                    }
                } catch (Exception e) {
                    Log.e("Exception  j7", e.getMessage());
                }

                return "";


            }

            @Override
            protected void onPostExecute(String msg) {
                try {
                    // Put converted Image string into Async Http Post param
                    String firstName = first_name.getText().toString().trim();
                    String lastName = last_name.getText().toString().trim();
                    String Phone_number = contact.getText().toString().trim();
                    String addrDistrict = district.getSelectedItem().toString();
                    String addrCity = city.getText().toString();
                    String Gender = gender.getSelectedItem().toString();
                    params.put("image", encodedString);
                    params.put("first_name", firstName);
                    params.put("last_name", lastName);
                    params.put("district", addrDistrict);
                    params.put("city", addrCity);
                    params.put("phone_number", Phone_number);
                    params.put("gender", Gender);
                    params.put("email_address", email_address);


                    makeHTTPCall();


                } catch (Exception e) {
                    Log.e("Exception", e.getMessage());
                }


            }


        }.execute(null, null, null);
    }


    // Make Http call to upload Image to Php server

    public void makeHTTPCall() {

        AsyncHttpClient client = new AsyncHttpClient();
        try {

            client.post("https://isochasmic-circuits.000webhostapp.com/update_basic_info.php",
                    params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(String response) {
                            progress.dismiss();
                            if (response.equalsIgnoreCase("Account updated successfully")) {
                                getFirstName = first_name.getText().toString();
                                getLastName = last_name.getText().toString();
                                getContact = contact.getText().toString();
                                Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                                reload_page();
                            } else {
                                Log.e("Response gotten", response);

                                Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                                reload_page();
                            }

                        }


                        @Override
                        public void onFailure(int statusCode, Throwable error,
                                              String content) {

                            // When Http response code is '404'
                            if (statusCode == 404) {
                                Toast.makeText(getActivity(),
                                        "Requested resource not found",
                                        Toast.LENGTH_LONG).show();
                            }
                            // When Http response code is '500'
                            else if (statusCode == 500) {
                                Toast.makeText(getActivity(),
                                        "Something went wrong at server end",
                                        Toast.LENGTH_LONG).show();
                            }
                            // When Http response code other than 404, 500
                            else {
                                Toast.makeText(
                                        getActivity(),
                                        "Error occurred n Most Common Error: n1. Device not connected to Internetn  2. Web App is not deployed in App servern  3. App server is not runningn HTTP Status code : "
                                                + statusCode, Toast.LENGTH_LONG)
                                        .show();
                            }
                        }
                    }

            );
        } catch (Exception e) {
            Log.e("Caught  Exception", e.getMessage());
        }
    }

    /*private void displayEmailExists() {
        try {
            android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(getActivity());
            alertDialogBuilder.setTitle("Failed");
            alertDialogBuilder.setMessage("This email already exists!\n Please choose another email");
            android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } catch (Exception e) {
            Log.e("Hahah", e.getMessage());
        }

    }*/

    private void addGender() {

        gender = (Spinner) view.findViewById(R.id.edit_gender);
        List<String> genderList = new ArrayList<>();
        genderList.add("Gender");
        genderList.add("Male");
        genderList.add("Female");
        genderList.add("N/A");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_spinner_item, genderList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(dataAdapter);
    }

    private void addDistrict() {

        district = (Spinner) view.findViewById(R.id.edit_district);
        List<String> districtList = new ArrayList<>();
        getDistrict getDist = new getDistrict();
        List<String> districtGet = getDist.returnDistrict();
        for (int i = 0; i < districtGet.size(); i++) {
            districtList.add(districtGet.get(i));
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_spinner_item, districtList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        district.setAdapter(dataAdapter);

    }

   /* private void addCity(String zzz) {
        city = (Spinner) view.findViewById(R.id.edit_city);
        List<String> cityList = new ArrayList<>();
        cityList.add("Area");
        getCity CITY = new getCity();
        List<String> cityget = CITY.returnCity(zzz);
        int size = cityget.size();
        for (int i = 0; i < size; i++) {
            cityList.add(cityget.get(i));

        }


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_spinner_item, cityList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        city.setAdapter(dataAdapter);
        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCity = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getActivity().getBaseContext(), "Please select Area", Toast.LENGTH_LONG).show();

            }
        });


    }
*/
    public void populate_new_data() {
        FragmentTransaction t = getActivity().getFragmentManager().beginTransaction();
        t.detach(this).attach(this).commitAllowingStateLoss();
    }

    private void reload_page() {


        first_name.setText(getFirstName);
        last_name.setText(getLastName);
        contact.setText(getContact);
        gender.setSelection(0);
        district.setSelection(0);
        city.setSelection(0);
        Glide.with(getActivity())
                .load("https://isochasmic-circuits.000webhostapp.com/images/profile/profile_picture/" + email_address + ".png")
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .override(50, 50)
                .into(imageView);
    }


    public void init() {


        imageView = (ImageView) view.findViewById(R.id.image_view);
        change_photo = (TextView) view.findViewById(R.id.change_photo);
        change_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data) {
                // Get the Image from data
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                // Get the cursor
                Cursor cursor = getActivity().getBaseContext().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();


                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgPath = cursor.getString(columnIndex);

                String fileNameSegments[] = imgPath.split("/");
                fileName = fileNameSegments[fileNameSegments.length - 1];


                Glide.with(getActivity().getBaseContext())
                        .load(imgPath)
                        .override(50, 50)
                        .into(imageView);


                params.put("filename", fileName);

                cursor.close();
                // Get the Image's file name
            } else {
                Toast.makeText(getActivity().getBaseContext(), "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity().getBaseContext(), "Something went wrong", Toast.LENGTH_LONG)
                    .show();
            Log.e("The raised exception", e.getMessage());
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    init();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity().getBaseContext(), "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    private void showPopUp(final String type, String msg1, String msg2) {


        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        alertDialog.setTitle("Change Security Info");
        alertDialog.setCancelable(false);
        final EditText first_input = new EditText(getActivity());
        final EditText second_input = new EditText(getActivity());
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        try {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            lp.setMargins(20, 15, 20, 0);
            first_input.setLayoutParams(lp);
            second_input.setLayoutParams(lp);
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        first_input.setHint(msg1);
        second_input.setHint(msg2);
        second_input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        second_input.setSelection(second_input.getText().length());
        layout.addView(first_input);
        layout.addView(second_input);
        alertDialog.setView(layout);
        alertDialog.setNeutralButton("Update",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String password = second_input.getText().toString();

                        String email_pass = first_input.getText().toString();
                        if (type.equals("email")) {
                            if (!(email_pass.equals(getEmail))) {
                                if (password.equals(getPassword)) {
                                    if (email_pass.length() > 8) {
                                        if (email_pass.contains("@") && email_pass.contains(".")) {
                                            change_security("change_email", getEmail, email_pass);
                                            dialog.dismiss();
                                        } else {
                                            Toast.makeText(getActivity(), "Wrong email entered", Toast.LENGTH_LONG).show();

                                        }

                                    } else {
                                        Toast.makeText(getActivity(), "Email too short", Toast.LENGTH_LONG).show();

                                    }

                                } else {
                                    Toast.makeText(getActivity(), "Incorrect password entered", Toast.LENGTH_LONG).show();
                                }

                            } else {
                                Toast.makeText(getActivity(), "Same email entered", Toast.LENGTH_LONG).show();

                            }
                        } else if (type.equals("pass")) {
                            if (password.equals(getPassword)) {

                                if (!(email_pass.equals(getPassword))) {

                                    if (email_pass.length() > 5) {
                                        change_security("change_pass", getEmail, email_pass);
                                        dialog.dismiss();

                                    } else {
                                        Toast.makeText(getActivity(), "New password too short", Toast.LENGTH_LONG).show();

                                    }

                                } else {
                                    Toast.makeText(getActivity(), "Same password entered", Toast.LENGTH_LONG).show();

                                }

                            } else {
                                Toast.makeText(getActivity(), "Wrong password entered", Toast.LENGTH_LONG).show();

                            }
                        }

                    }
                });

        alertDialog.setPositiveButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });

        alertDialog.show();
    }

    private void change_security(String info, final String getEmail, final String email_pass) {

        new backGroundWorker(getActivity()).execute(info, getEmail, email_pass);
    }


    class backGroundWorker extends AsyncTask<String, Void, String> {

        private ProgressDialog progressDialog;
        private AlertDialog alertDialog;

        backGroundWorker(Context ctx) {
            context = ctx;
        }

        @Override
        protected String doInBackground(String... params) {


            try {
                String type = params[0];
                String user_name = params[1];
                String pass_word = params[2];
                URL url = new URL("https://isochasmic-circuits.000webhostapp.com/update_security_info.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String postData = URLEncoder.encode("first_data", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8") + "&"
                        + URLEncoder.encode("second_data", "UTF-8") + "=" + URLEncoder.encode(user_name, "UTF-8") + "&"
                        + URLEncoder.encode("third_data", "UTF-8") + "=" + URLEncoder.encode(pass_word, "UTF-8");
                writer.write(postData);
                writer.flush();
                writer.close();
                outputStream.close();
                InputStream stream = httpURLConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "iso-8859-1"));
                String result = "";
                String line;
                while ((line = reader.readLine()) != null) {
                    result += line;
                }
                reader.close();
                stream.close();
                httpURLConnection.disconnect();

                return result;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
            alertDialog = new AlertDialog.Builder(context).create();


        }

        @Override
        protected void onPostExecute(String result) {
            try {
                Log.e("Update response", result);
                populate_new_data();
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            } catch (Exception e) {

            }

            String message="";String status="";


            try {
                JSONObject jsonResult = new JSONObject(result);
                message = jsonResult.optString("message");
                status = jsonResult.optString("status");
                if (message.equalsIgnoreCase("Request granted")) {
                    SharedPreferences prefs = getActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("USERNAME", "");
                    editor.putString("PASSWORD", "");
                    editor.commit();
                    Intent intent = new Intent(getActivity(), signIn_signUp.class);
                    getActivity().startActivity(intent);
                    Toast.makeText(getActivity(), "Request granted\n Please login again", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Request not granted", Toast.LENGTH_LONG).show();

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }


    }
}






/*

        layout = (RelativeLayout) view.findViewById(R.id.edit_profile_layout);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        layoutParams2.setMargins(10, 0, 50, 0);


        RelativeLayout.LayoutParams layoutParams4 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        layout.setLayoutParams(layoutParams);
        ImageView imageView = new ImageView(getActivity());
        TextView change_photo_txt = new TextView(getActivity());
        EditText editFirstName = new EditText(getActivity());
        EditText editLastName = new EditText(getActivity());
        Spinner district = new Spinner(getActivity());
        Spinner city=new Spinner(getActivity());

        String image_id = "1";
        String change_photo_id = "2";
        String first_name_id = "3";
        String last_name_id = "4";
        imageView.setId(Integer.parseInt(image_id));
        change_photo_txt.setId(Integer.parseInt(change_photo_id));
        imageView.setX(220);
        imageView.setY(20);
        imageView.setLayoutParams(layoutParams1);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        layoutParams3.addRule(RelativeLayout.BELOW, imageView.getId());

        change_photo_txt.setX(180);
        change_photo_txt.setY(40);
        change_photo_txt.setLayoutParams(layoutParams3);
        change_photo_txt.setText("Change photo");
        change_photo_txt.setTextColor(Color.argb(110, 0, 0, 255));
        change_photo_txt.setTypeface(null, Typeface.BOLD);
        change_photo_txt.setTextSize(16);
        change_photo_txt.setClickable(true);


        editFirstName.setTypeface(null, Typeface.BOLD);
        editFirstName.setTextSize(16);
        editFirstName.setX(20);
        editFirstName.setY(150);
        editFirstName.setSingleLine(true);
        editFirstName.setInputType(InputType.TYPE_CLASS_TEXT);
        editFirstName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        layoutParams2.addRule(RelativeLayout.BELOW, change_photo_txt.getId());
        editFirstName.setLayoutParams(layoutParams2);
        editFirstName.setId(Integer.parseInt(first_name_id));
        editFirstName.setHint("Your first name");


        editLastName.setTypeface(null, Typeface.BOLD);
        editLastName.setTextSize(16);
        editLastName.setX(20);
        editLastName.setY(220);
        editLastName.setSingleLine(true);
        editLastName.setInputType(InputType.TYPE_CLASS_TEXT);
        editLastName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        layoutParams2.addRule(RelativeLayout.BELOW, editFirstName.getId());
        editLastName.setLayoutParams(layoutParams2);
        editLastName.setId(Integer.parseInt(last_name_id));
        editLastName.setHint("Your last name");

*/


       /* layout.addView(imageView);
        layout.addView(change_photo_txt);
        layout.addView(editFirstName);
        layout.addView(editLastName);*/