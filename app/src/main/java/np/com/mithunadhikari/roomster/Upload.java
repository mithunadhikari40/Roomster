package np.com.mithunadhikari.roomster;

import android.Manifest;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class Upload extends Fragment {
    public Spinner district;
    private AutoCompleteTextView city;
    public RadioButton room, flat;
    public EditText price, contact, description;
    public TextView image1, image2, image3, image4;
    public TextView emailAddress;
    public String selectedDistrict, room_flat;
    EditText no;
    View view;
    String email_Address;
    SharedPreferences myPrefs;
    String encodedString1, encodedString2, encodedString3, encodedString4;
    RequestParams params = new RequestParams();
    String imgPath1, imgPath2, imgPath3, imgPath4, fileName;
    Bitmap bitmap;
    int check = 0;
    int enquiry = 0;
    Button upload;
    private ProgressDialog progress;
    private int RESULT_LOAD_IMG = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myPrefs = getActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        email_Address = myPrefs.getString("USERNAME", "");
        view = inflater.inflate(R.layout.fragment_upload, container, false);
        // emailAddress=(TextView)view.findViewById(R.id.email_address);
        /*try {
            city=(AutoCompleteTextView) view.findViewById(R.id.address_city_upload) ;
            String set = myPrefs.getString("cities", null);
            System.out.println("upload"+set);
            String new_string = set.substring(set.indexOf("[") + 1, set.indexOf("]"));
            List<String> myList = new ArrayList<>(Arrays.asList(new_string.split(",")));
            int size = myList.size();
            String[] mArray = new String[size];
            for(int i=0;i<size;i++){
                mArray[i]=myList.get(i);
                mArray[i]=mArray[i].substring(mArray[i].indexOf("")+1,mArray[i].lastIndexOf("")-1);

            }
            ArrayAdapter adapter =
                    new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, mArray);
            city.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        init();
        addDistrict();
        onUpload(view);
        upload = (Button) view.findViewById(R.id.upload_button);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUpload(null);
            }
        });
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        try {

            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Upload");
        } catch (Exception e) {
            Log.e("Exception in title", e.getMessage());
        }
        return view;
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


                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity().getBaseContext(), "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    public void init() {

        room = (RadioButton) view.findViewById(R.id.radio_room);
        flat = (RadioButton) view.findViewById(R.id.radio_flat);
        upload = (Button) view.findViewById(R.id.upload_button);
        price = (EditText) view.findViewById(R.id.upload_rate);
        contact = (EditText) view.findViewById(R.id.upload_contact);
        emailAddress = (TextView) view.findViewById(R.id.email_address);
        emailAddress.setText(email_Address);
        description = (EditText) view.findViewById(R.id.upload_description);
        district = (Spinner) view.findViewById(R.id.address_dist_upload);
        city=(AutoCompleteTextView) view.findViewById(R.id.address_city_upload) ;
        no = (EditText) view.findViewById(R.id.no_of);
        image1 = (TextView) view.findViewById(R.id.image1);
        image2 = (TextView) view.findViewById(R.id.image2);
        image3 = (TextView) view.findViewById(R.id.image3);
        image4 = (TextView) view.findViewById(R.id.image4);
        image2.setVisibility(View.GONE);
        image3.setVisibility(View.GONE);
        image4.setVisibility(View.GONE);
        city.setHint("Area eg Baneshwor");
        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = 1;
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
                image2.setVisibility(View.VISIBLE);

            }
        });
        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = 2;
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
                image3.setVisibility(View.VISIBLE);

            }
        });
        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = 3;
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
                image4.setVisibility(View.VISIBLE);

            }
        });
        image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = 4;
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent

                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
                Toast.makeText(getActivity().getBaseContext(), "You can only upload maximum of four pictures", Toast.LENGTH_LONG).show();


            }
        });


        flat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                room.setChecked(false);
                room_flat = "flat";

            }
        });

        room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flat.setChecked(false);
                room_flat = "room";

            }
        });



        district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDistrict = parent.getSelectedItem().toString();
                //addCity(selectedDistrict);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                Toast.makeText(getActivity().getBaseContext(), "Please select the address", Toast.LENGTH_LONG).show();

            }
        });
        try {
            String set = myPrefs.getString("cities", null);
            System.out.println("upload"+set);
            String new_string = set.substring(set.indexOf("[") + 1, set.indexOf("]"));
            List<String> myList = new ArrayList<>(Arrays.asList(new_string.split(",")));
            int size = myList.size();
            String[] mArray = new String[size];
            for(int i=0;i<size;i++){
                mArray[i]=myList.get(i);
                mArray[i]=mArray[i].substring(mArray[i].indexOf("")+1,mArray[i].lastIndexOf("")-1);

            }
            ArrayAdapter adapter =
                    new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, mArray);
            city.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void addDistrict() {

        district = (Spinner) view.findViewById(R.id.address_dist_upload);
        //district.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
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

    /*public void addCity(String zzz) {
        city = (AutoCompleteTextView) view.findViewById(R.id.address_city_upload);
        // city.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
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
                Toast.makeText(getActivity().getBaseContext(), "Please select city", Toast.LENGTH_LONG).show();

            }
        });
    }
*/

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
                String fileNameSegments[];
                int columnIndex;
                if (check == 1) {
                    columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imgPath1 = cursor.getString(columnIndex);
                    fileNameSegments = imgPath1.split("/");
                    fileName = fileNameSegments[fileNameSegments.length - 1];
                    image1.setTextSize(12);
                    image1.setText(fileName);

                    // Put file name in Async Http Post Param which will used in Php web app
                    params.put("filename1", fileName);

                }
                if (check == 2) {
                    columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imgPath2 = cursor.getString(columnIndex);
                    fileNameSegments = imgPath2.split("/");
                    fileName = fileNameSegments[fileNameSegments.length - 1];
                    image2.setTextSize(12);
                    image2.setText(fileName);
                    // Put file name in Async Http Post Param which will used in Php web app
                    params.put("filename2", fileName);

                }
                if (check == 3) {
                    columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imgPath3 = cursor.getString(columnIndex);
                    fileNameSegments = imgPath3.split("/");
                    fileName = fileNameSegments[fileNameSegments.length - 1];
                    image3.setTextSize(12);
                    image3.setText(fileName);
                    // Put file name in Async Http Post Param which will used in Php web app
                    params.put("filename3", fileName);

                }
                if (check == 4) {
                    columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imgPath4 = cursor.getString(columnIndex);
                    fileNameSegments = imgPath4.split("/");
                    fileName = fileNameSegments[fileNameSegments.length - 1];
                    image4.setTextSize(12);
                    image4.setText(fileName);
                    // Put file name in Async Http Post Param which will used in Php web app
                    params.put("filename4", fileName);

                }
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


    // AsyncTask - To convert Image to String
    public void encodeImagetoString() {
        new AsyncTask<Void, Void, String>() {
            protected void onPreExecute() {
               /* try {
                    alertDialog = new AlertDialog.Builder(getActivity().getBaseContext()).create();
                    alertDialog.setTitle("Progress dialog");
                    alertDialog.setCancelable(false);
                    alertDialog.setMessage("Uploading please wait");

                } catch (Exception ex) {
                    Log.e("The error is\n\n\n", ex.getMessage());
                }*/
            }

            @Override
            protected String doInBackground(Void... params) {
                try {
                    BitmapFactory.Options options;
                    options = new BitmapFactory.Options();
                    options.inSampleSize = 7;
                    if (enquiry == 0) {
                        bitmap = BitmapFactory.decodeFile(imgPath1, options);

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        // Must compress the Image to reduce image size to make upload easy
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                        byte[] byte_arr = stream.toByteArray();
                        // Encode Image to String
                        encodedString1 = Base64.encodeToString(byte_arr, 0);
                        enquiry++;

                    }
                    if (enquiry == 1) {
                        bitmap = BitmapFactory.decodeFile(imgPath2, options);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        // Must compress the Image to reduce image size to make upload easy
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                        byte[] byte_arr = stream.toByteArray();
                        // Encode Image to String

                        encodedString2 = Base64.encodeToString(byte_arr, 0);
                        enquiry++;
                    }
                    if (enquiry == 2) {
                        bitmap = BitmapFactory.decodeFile(imgPath3, options);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        // Must compress the Image to reduce image size to make upload easy
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                        byte[] byte_arr = stream.toByteArray();
                        // Encode Image to String

                        encodedString3 = Base64.encodeToString(byte_arr, 0);
                        enquiry++;
                    }
                    if (enquiry == 3) {
                        bitmap = BitmapFactory.decodeFile(imgPath4, options);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        // Must compress the Image to reduce image size to make upload easy
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                        byte[] byte_arr = stream.toByteArray();
                        // Encode Image to String
                        encodedString4 = Base64.encodeToString(byte_arr, 0);
                        enquiry++;
                    }
                } catch (Exception e) {
                    Log.e("j7 upload", e.getMessage());
                }

                return "";
            }

            @Override
            protected void onPostExecute(String msg) {

                try {
                    params.put("image1", encodedString1);
                    params.put("image2", encodedString2);
                    params.put("image3", encodedString3);
                    params.put("image4", encodedString4);
                    params.put("room_flat", room_flat);
                    params.put("district", selectedDistrict);
                    params.put("city", city.getText().toString());
                    params.put("price", price.getText().toString().trim());
                    params.put("phone_number", contact.getText().toString().trim());
                    params.put("description", description.getText().toString().trim());
                    params.put("email_address", emailAddress.getText().toString().trim());
                    params.put("no", no.getText().toString());
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
            client.post("http://isochasmic-circuits.000webhostapp.com/upload_image.php",
                    params, new AsyncHttpResponseHandler() {


                        // When the response returned by REST has Http
                        // response code '200'
                        @Override
                        public void onSuccess(String response) {
                            // Hide Progress Dialog
                            progress.dismiss();

                            Toast.makeText(getActivity().getBaseContext(), "Successfully uploaded",
                                    Toast.LENGTH_LONG).show();
                        }

                        // When the response returned by REST has Http
                        // response code other than '200' such as '404',
                        // '500' or '403' etc
                        //@Override
                        public void onFailure(int statusCode, Throwable error,
                                              String content) {
                            // Hide Progress Dialog
                            progress.dismiss();

                            // When Http response code is '404'
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

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        // Dismiss the progress bar when application is closed

    }


    public void onUpload(View view) {
        if (isOnline(getActivity())) {

            emailAddress.setText(email_Address);
            final String Description = description.getText().toString().trim();
            String prc = price.getText().toString();
            final String Contact = contact.getText().toString().trim();
            String noF=no.getText().toString();
            if (prc.equals("")){
                prc="0";
            }
            if (noF.equals("")){
                noF="0";

            }
            /*final String email_address = emailAddress.getText().toString().trim();*/
          /*  String type = "upload";*/

            //  backGroundWorker backGroundWorker = new backGroundWorker(getActivity().getBaseContext());
            if (!(district.getSelectedItem().toString().equalsIgnoreCase("district"))) {
                if (!(city.getText().toString().length()<3)) {
                    if (room.isChecked() || flat.isChecked()) {

                        //// TODO: 1/28/2017 your code here

                        if (Contact.length() >= 9) {

                            if (Integer.parseInt(prc) > 0) {
                                if (Description.length() >= 30) {

                                    /*backGroundWorker.execute(type, room_flat, selectedDistrict, selectedCity,
                                            prc, Contact, Description, emailAddress.getText().toString().trim());*/

                                    if (Integer.parseInt(noF) > 0) {

                                        if (imgPath1 != null && !imgPath1.isEmpty()) {

                                            getImageSource(imgPath1, imgPath2, imgPath3, imgPath4);
                                       /* progress = new ProgressDialog(getActivity());
                                        progress.setMessage("Please wait");
                                        progress.setCancelable(false);
                                        progress.show();*/
                                       /* encodeImagetoString();*/
                                        /*imageInDialog inDialog=new imageInDialog();
                                        inDialog.showPopUp(imgPath1,imgPath2,image3,imgPath4);*/

                                      /*  }*/
                                            //backGroundWorker.execute(type, first_name, last_name, Email, Password, Phone_number, addrDistrict, addrCity, Gender);

                                            // When Image is not selected from Gallery
                                        } else {
                                            Toast.makeText(
                                                    getActivity().getApplicationContext(),
                                                    "You must select image from gallery before you try to upload",
                                                    Toast.LENGTH_LONG).show();

                                        }
                                    } else {
                                        Toast.makeText(getActivity().getBaseContext(), "Please select positive no of rooms or flats", Toast.LENGTH_LONG).show();

                                    }
                                } else {
                                    Toast.makeText(getActivity().getBaseContext(), "Please give description in at least 30 characters", Toast.LENGTH_LONG).show();

                                }
                            } else {
                                Toast.makeText(getActivity().getBaseContext(), "Please enter some positive amount", Toast.LENGTH_LONG).show();

                            }
                        } else {
                            Toast.makeText(getActivity().getBaseContext(), "Please enter valid phone number", Toast.LENGTH_LONG).show();

                        }

                    } else {
                        Toast.makeText(getActivity().getBaseContext(), "Please select room or flat", Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(getActivity().getBaseContext(), "Please select correct  address\n Too short name for Area", Toast.LENGTH_LONG).show();

                }

            } else {
                Toast.makeText(getActivity().getBaseContext(), "Please select district", Toast.LENGTH_LONG).show();

            }
        } else {
            Toast.makeText(getActivity(), "Sorry no active network Available\n Please connect to internet and try again!", Toast.LENGTH_LONG).show();

        }

    }


    public boolean isOnline(Context ctx) {
        ConnectivityManager cm;
        NetworkInfo netInfo = null;

        try {
            cm =
                    (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
            netInfo = cm.getActiveNetworkInfo();
        } catch (Exception e) {

        }
        return netInfo != null && netInfo.isConnected();

    }


    public void getImageSource(String image1, String image2, String image3, String image4) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.image_in_alert);
        dialog.setTitle("Images to upload");
        ImageView one = (ImageView) dialog.findViewById(R.id.one_image);
        ImageView two = (ImageView) dialog.findViewById(R.id.two_image);
        ImageView three = (ImageView) dialog.findViewById(R.id.three_image);
        ImageView four = (ImageView) dialog.findViewById(R.id.four_image);

        if (image1 != null) {
            Glide.with(getActivity())
                    .load(image1)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(one);
        }
        if (image2 != null) {
            Glide.with(getActivity())
                    .load(image2)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(two);
        }
        if (image3 != null) {
            Glide.with(getActivity())
                    .load(image3)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(three);
        }
        if (image4 != null) {
            Glide.with(getActivity())
                    .load(image4)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(four);
        }

        TextView ok = (TextView) dialog.findViewById(R.id.ok_image);
        TextView cancel = (TextView) dialog.findViewById(R.id.cancel_image);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   /* imgPath4=null;
                    imgPath3=null;
                    imgPath2=null;
                    imgPath1=null;*/
                dialog.dismiss();

            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do your code to reset all the images to nulll

                progress = new ProgressDialog(getActivity());
                progress.setMessage("Please wait");
                progress.setCancelable(false);
                progress.show();
                encodeImagetoString();
                dialog.dismiss();
            }
        });

        dialog.show();
    }


}
