package np.com.mithunadhikari.roomster;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressLint("NewApi")

public class Signup extends AppCompatActivity {

    private static int RESULT_LOAD_IMG = 1;
    public EditText fName;
    public EditText lName;
    public EditText email;
    public EditText password;
    public EditText confirm_pass;
    public EditText phone_number;
    public Button sign_up;
    public RadioButton radioMale;
    public RadioButton radioFemale;
    public RadioButton radioNotSpecified;
    public Spinner district;
    public AutoCompleteTextView city;
    public String selectedDistrict, gender;
    SharedPreferences myPrefs;
    String encodedString;
    RequestParams params = new RequestParams();
    String imgPath, fileName;
    Bitmap bitmap;
    AlertDialog alertDialog;
    private ProgressDialog progress;

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //requestWindowFeature(WindowCompat.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);

        ActivityCompat.requestPermissions(Signup.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1);
        try {

            setTitle("Sign up");
        } catch (Exception e) {
            Log.e("Exception in title", e.getMessage());
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    init();
                    addDistrict();
                    onSignUp(null);


                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(Signup.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }


    }

    public void init() {
        myPrefs = getApplicationContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        fName = (EditText) findViewById(R.id.first_nameEdit);
        lName = (EditText) findViewById(R.id.last_nameEdit);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        confirm_pass = (EditText) findViewById(R.id.confirm_password);
        phone_number = (EditText) findViewById(R.id.mobile_no);
        sign_up = (Button) findViewById(R.id.signup_signup);
        radioMale = (RadioButton) findViewById(R.id.radio_male);
        radioFemale = (RadioButton) findViewById(R.id.radio_female);
        radioNotSpecified = (RadioButton) findViewById(R.id.radio_none);
        district = (Spinner) findViewById(R.id.address_dist);
        city = (AutoCompleteTextView) findViewById(R.id.address_city);
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
                    new ArrayAdapter<>(Signup.this, android.R.layout.simple_dropdown_item_1line, mArray);
            city.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        radioMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioFemale.setChecked(false);
                radioNotSpecified.setChecked(false);
                gender = "male";
            }
        });

        radioFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioMale.setChecked(false);
                radioNotSpecified.setChecked(false);
                gender = "female";
            }
        });


        radioNotSpecified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioMale.setChecked(false);
                radioFemale.setChecked(false);
                gender = "unknown";
            }
        });


        district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

                                           {
                                               @Override
                                               public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                   selectedDistrict = parent.getSelectedItem().toString();

                                               }

                                               @Override
                                               public void onNothingSelected(AdapterView<?> parent) {
                                                   Toast.makeText(np.com.mithunadhikari.roomster.Signup.this, "Please select district", Toast.LENGTH_LONG).show();
                                               }
                                           }

        );

    }

    public void addDistrict() {

        district = (Spinner) findViewById(R.id.address_dist);
        //district.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        List<String> districtList = new ArrayList<>();
        getDistrict getDist = new getDistrict();
        List<String> districtGet = getDist.returnDistrict();
        for (int i = 0; i < districtGet.size(); i++) {
            districtList.add(districtGet.get(i));
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, districtList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        district.setAdapter(dataAdapter);


    }

    /*public void addCity(String zzz) {
        city = (AutoCompleteTextView) findViewById(R.id.address_city);
        // city.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        List<String> cityList = new ArrayList<>();
        cityList.add("Area");
        getCity CITY = new getCity();
        List <String> cityget = CITY.returnCity(zzz);
        int size=cityget.size();
        for(int i=0;i<size;i++) {
            cityList.add(cityget.get(i));

        }
       *//* if (zzz.equalsIgnoreCase("kathmandu")) {
            cityList.add("Baneshor");
            cityList.add("Koteshor");
            cityList.add("Buspark");
            cityList.add("Kalanki");
            cityList.add("Chandragiri");
        } else if (zzz.equalsIgnoreCase("Bhaktapur")) {
            cityList.add("Thimi");
            cityList.add("Chagu");
            cityList.add("Pepsikola");
            cityList.add("Lokanthali");
        } else if (zzz.equalsIgnoreCase("Lalitpur")) {
            cityList.add("Emadol");
            cityList.add("Kupandol");
            cityList.add("Pharphing");
            cityList.add("Sukana");
            cityList.add("Lagankhel");

        }*//*


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cityList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        city.setAdapter(dataAdapter);
        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCity = parent.getSelectedItem().toString();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(Signup.this, "Please select city", Toast.LENGTH_LONG).show();

            }
        });
    }*/


    public void onSignUp(View view) {

        /*if (new signIn_signUp().isNetAvailable(this)) {*/

        if (new signIn_signUp().isOnline(getApplicationContext())) {
            fName = (EditText) findViewById(R.id.first_nameEdit);
            lName = (EditText) findViewById(R.id.last_nameEdit);
            email = (EditText) findViewById(R.id.email);
            password = (EditText) findViewById(R.id.password);
            confirm_pass = (EditText) findViewById(R.id.confirm_password);
            phone_number = (EditText) findViewById(R.id.mobile_no);
            sign_up = (Button) findViewById(R.id.signup_signup);
            radioMale = (RadioButton) findViewById(R.id.radio_male);
            radioFemale = (RadioButton) findViewById(R.id.radio_female);
            radioNotSpecified = (RadioButton) findViewById(R.id.radio_none);
            district = (Spinner) findViewById(R.id.address_dist);
            city = (AutoCompleteTextView) findViewById(R.id.address_city);

            String first_name = fName.getText().toString().trim();
            String last_name = lName.getText().toString().trim();
            String Email = email.getText().toString().trim();
            String Password = password.getText().toString().trim();
            String Confirm_pass = confirm_pass.getText().toString().trim();
            String Phone_number = phone_number.getText().toString().trim();

            // backGroundWorker backGroundWorker = new backGroundWorker(this);


            if ((first_name.length() > 2) && (last_name.length() > 2) && (Email.length() > 7) && (Password.length() > 2)
                    && (Confirm_pass.length() > 2) && (Phone_number.length() > 8) && (Password.length() < 40)) {
                if (Password.equals(Confirm_pass)) {
                    if (radioMale.isChecked() || radioFemale.isChecked() || radioNotSpecified.isChecked()) {

                        if (!(district.getSelectedItem().toString().equalsIgnoreCase("district"))) {


                            if (!(city.getText().toString().length() < 3)) {

                                if (imgPath != null && !imgPath.isEmpty()) {

                                    progress = new ProgressDialog(this);
                                    progress.setMessage("Please wait");
                                    progress.setCancelable(false);
                                    progress.show();
                                    // Convert image to String using Base64

                                    encodeImagetoString();


                                    // When Image is not selected from Gallery
                                } else {
                                    Toast.makeText(
                                            getApplicationContext(),
                                            "You must select image from gallery before you try to upload",
                                            Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(Signup.this, "Please select full  address \n Area name too short", Toast.LENGTH_LONG).show();

                            }


                        } else{
                            Toast.makeText(Signup.this, "Please select district", Toast.LENGTH_LONG).show();


                        }


                    } else {
                        Toast.makeText(Signup.this, "Please select gender", Toast.LENGTH_LONG).show();
                    }


                } else {
                    Toast.makeText(Signup.this, "Passwords do not match", Toast.LENGTH_LONG).show();
                    password.setText("");
                    confirm_pass.setText("");

                }
            } else {
                Toast.makeText(np.com.mithunadhikari.roomster.Signup.this, "Input data too short or" +
                        " too long \n Must be between 3 to 40 characters ", Toast.LENGTH_LONG).show();

            }
        } else {
            Toast.makeText(getApplicationContext(), "Sorry no active network Available\n Please connect to internet and try again!", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void onBackPressed() {
        Intent mainPage = new Intent(Signup.this, signIn_signUp.class);
        startActivity(mainPage);
    }

    public void choosePhoto(View view) {


        // If request is cancelled, the result arrays are empty.

        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgPath = cursor.getString(columnIndex);

                cursor.close();
                Button button = (Button) findViewById(R.id.photo_upload);

                // Set the Image in ImageView

                // Get the Image's file name
                String fileNameSegments[] = imgPath.split("/");
                fileName = fileNameSegments[fileNameSegments.length - 1];
                // Put file name in Async Http Post Param which will used in Php web app
                button.setText(fileName);
                params.put("filename", fileName);

            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
            Log.e("Sorry", "Something went wrong");
        }


    }


    // AsyncTask - To convert Image to String
    public void encodeImagetoString() {
        new AsyncTask<Void, Void, String>() {

            protected void onPreExecute() {

            }

            @Override
            protected String doInBackground(Void... params) {
                BitmapFactory.Options options;
                try {
                    options = new BitmapFactory.Options();
                    options.inSampleSize = 3;


                    bitmap = BitmapFactory.decodeFile(imgPath, options);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();

                    // Must compress the Image to reduce image size to make upload easy

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                    byte[] byte_arr = stream.toByteArray();
                    // Encode Image to String Base64.DEFAULT

                    encodedString = Base64.encodeToString(byte_arr, 0);

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);

                } catch (Exception e) {
                    Log.e("Exception  j7", e.getMessage());
                }

                return "";


            }

            @Override
            protected void onPostExecute(String msg) {
                try {
                    // Put converted Image string into Async Http Post param
                    String first_name = fName.getText().toString().trim();
                    String last_name = lName.getText().toString().trim();
                    String Email = email.getText().toString().trim();
                    String Password = password.getText().toString().trim();
                    String Phone_number = phone_number.getText().toString().trim();
                    String addrDistrict = district.getSelectedItem().toString();
                    String addrCity = city.getText().toString();

                    params.put("image", encodedString);
                    params.put("email", Email);
                    params.put("first_name", first_name);
                    params.put("last_name", last_name);
                    params.put("district", addrDistrict);
                    params.put("city", addrCity);
                    params.put("phone_number", Phone_number);
                    params.put("gender", gender);
                    params.put("password", Password);

                    makeHTTPCall();


                } catch (Exception e) {

                }


            }
        }.execute(null, null, null);
    }

    // Make Http call to upload Image to Php server

    public void makeHTTPCall() {

        AsyncHttpClient client = new AsyncHttpClient();
        try {
            // isochasmic-circuits.000webhostapp.com/images/profile/upload_profile.php
            // Don't forget to change the IP address to your LAN address. Port no as well.

            client.post("http://isochasmic-circuits.000webhostapp.com/new_signup.php",
                    params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(String response) {
                            progress.dismiss();
                            if (response.contains("already")) {
                                Toast.makeText(getApplicationContext(), "This email already exists", Toast.LENGTH_LONG).show();

                                displayEmailExists();

                            } else {
                                Toast.makeText(getApplicationContext(), "Please login with newly created account credential", Toast.LENGTH_LONG).show();
                                String Email = email.getText().toString().trim();
                                String Password = password.getText().toString().trim();
                                Intent intent = new Intent(getApplicationContext(), signIn_signUp.class);
                                intent.putExtra("new_user_name", Email);
                                intent.putExtra("new_user_pass", Password);
                                startActivity(intent);
                            }

                        }

                        private void displayEmailExists() {
                            try {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getApplicationContext());
                                alertDialogBuilder.setTitle("Failed");
                                alertDialogBuilder.setMessage("This email already exists!\n Please choose another email");
                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                            } catch (Exception e) {
                                Log.e("Hahah", e.getMessage());
                            }

                        }

                        @Override
                        public void onFailure(int statusCode, Throwable error,
                                              String content) {

                            // When Http response code is '404'
                            if (statusCode == 404) {
                                Toast.makeText(getApplicationContext(),
                                        "Requested resource not found",
                                        Toast.LENGTH_LONG).show();
                            }
                            // When Http response code is '500'
                            else if (statusCode == 500) {
                                Toast.makeText(getApplicationContext(),
                                        "Something went wrong at server end",
                                        Toast.LENGTH_LONG).show();
                            }
                            // When Http response code other than 404, 500
                            else {
                                Toast.makeText(
                                        getApplicationContext(),
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


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        // Dismiss the progress bar when application is closed

    }

    @Override
    public void onStop() {
        super.onStop();
    }
    /*public boolean isOnline() {
        ConnectivityManager cm;
        NetworkInfo netInfo = null;

        try {
            cm =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            netInfo = cm.getActiveNetworkInfo();
        }catch (Exception e){

        }            return netInfo != null && netInfo.isConnected();

    }*/
}




