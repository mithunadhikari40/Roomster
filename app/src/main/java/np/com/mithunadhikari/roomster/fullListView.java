package np.com.mithunadhikari.roomster;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import static np.com.mithunadhikari.roomster.R.id.txt_date;


public class fullListView extends Fragment {
    TextView roomFlat, District, City, Price, Description, Date, Contact;
    TextView room, room_no;
    ImageView firstImage, secondImage, thirdImage, fourthImage;
    ImageView hint;
    TextView hint_text;
    String fullFirstUse;
    RelativeLayout layout;
    SharedPreferences myPrefs;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        View view = inflater.inflate(R.layout.full_list_view, container, false);
        layout = (RelativeLayout) view.findViewById(R.id.full_list_layout);
        final float scale = getActivity().getResources().getDisplayMetrics().density;
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 1);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 20, 10, 0);
        layoutParams.height = (int) (180 * scale + 0.5f);
        layoutParams.width = (int) (160 * scale + 0.5f);
        try {
            String room_flat = getArguments().getString("room_flat");
            String district = getArguments().getString("district");
            String city = getArguments().getString("city");
            String contact = getArguments().getString("contact");
            String price = getArguments().getString("price");
            String description = getArguments().getString("desc");
            String date = getArguments().getString("date");
            final String image_1 = getArguments().getString("image1");
            final String image_2 = getArguments().getString("image2");
            final String image_3 = getArguments().getString("image3");
            final String image_4 = getArguments().getString("image4");
            final String email_address = getArguments().getString("email_address");
            String no_of = getArguments().getString("no_of");
            myPrefs = getActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
            fullFirstUse = myPrefs.getString("full_item_use", null);

            if (image_1.length() > 2) {
                firstImage = new ImageView(getActivity());
                firstImage.setScaleType(ImageView.ScaleType.FIT_XY);
                layoutParams.addRule(RelativeLayout.BELOW, txt_date);
                firstImage.setLayoutParams(layoutParams);
                Log.e("First Image", "Placed");

                Glide.with(getActivity())
                        .load("http://isochasmic-circuits.000webhostapp.com/images/uploaded_images/" + email_address + "/" + image_1)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(firstImage);

            }

            if (image_2.length() > 2) {
                secondImage = new ImageView(getActivity());
                secondImage.setScaleType(ImageView.ScaleType.FIT_XY);
                layoutParams.addRule(RelativeLayout.BELOW, txt_date);
                secondImage.setX((int) (170 * scale + 0.5f));
                secondImage.setLayoutParams(layoutParams);
                Log.e("Second Image", "Placed");

                Glide.with(getActivity())
                        .load("http://isochasmic-circuits.000webhostapp.com/images/uploaded_images/" + email_address + "/" + image_2)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(secondImage);

            }
            if (image_3.length() > 2) {
                thirdImage = new ImageView(getActivity());
                thirdImage.setScaleType(ImageView.ScaleType.FIT_XY);
                thirdImage.setY((int) (195 * scale + 0.5f));
                thirdImage.setLayoutParams(layoutParams);
                Log.e("Third Image", "Placed");

                Glide.with(getActivity())
                        .load("http://isochasmic-circuits.000webhostapp.com/images/uploaded_images/" + email_address + "/" + image_3)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(thirdImage);
            }
            if (image_4.length() > 2) {
                fourthImage = new ImageView(getActivity());
                fourthImage.setScaleType(ImageView.ScaleType.FIT_XY);
                fourthImage.setX((int) (170 * scale + 0.5f));
                fourthImage.setY((int) (195 * scale + 0.5f));
                fourthImage.setLayoutParams(layoutParams);
                Log.e("Fourth Image", "Placed");

                Glide.with(getActivity())
                        .load("http://isochasmic-circuits.000webhostapp.com/images/uploaded_images/" + email_address + "/" + image_4)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(fourthImage);
            }
            //View view = inflater.inflate(R.layout.list_view, container, false);
            roomFlat = (TextView) view.findViewById(R.id.txt_room_flat);
            District = (TextView) view.findViewById(R.id.txt_district);
            City = (TextView) view.findViewById(R.id.txt_city);
            Price = (TextView) view.findViewById(R.id.txt_price);
            Description = (TextView) view.findViewById(R.id.txt_desc);
            Date = (TextView) view.findViewById(txt_date);
            Contact = (TextView) view.findViewById(R.id.txt_contact);
            room = (TextView) view.findViewById(R.id.no);
            room_no = (TextView) view.findViewById(R.id.txt_no);
            hint = (ImageView) view.findViewById(R.id.full_list_view_image);
            hint_text = (TextView) view.findViewById(R.id.hint_text);
            roomFlat.setText(room_flat);
            District.setText(district);
            City.setText(city);
            Price.setText(price);
            Description.setText(description);
            Contact.setText(contact);
            Date.setText(date);
            room.setText("No of " + room_flat + "s");
            room_no.setText(no_of);
            Contact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    call_number();
                }
            });

            if (fullFirstUse==null){
                hint_text.setVisibility(View.VISIBLE);
                hint.setVisibility(View.VISIBLE);
            }else{
                hint_text.setVisibility(View.INVISIBLE);
                hint.setVisibility(View.INVISIBLE);
            }
            hint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hint_text.setVisibility(View.INVISIBLE);
                    hint.setVisibility(View.INVISIBLE);
                    SharedPreferences prefs = getActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("full_item_use", "Hello first time user tutorial");
                    editor.apply();
                }
            });

            try {
                firstImage.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        getImageUrl("http://isochasmic-circuits.000webhostapp.com/images/uploaded_images/" + email_address + "/" + image_1);
                    }
                });
                secondImage.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        getImageUrl("http://isochasmic-circuits.000webhostapp.com/images/uploaded_images/" + email_address + "/" + image_2);

                    }
                });
                thirdImage.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        getImageUrl("http://isochasmic-circuits.000webhostapp.com/images/uploaded_images/" + email_address + "/" + image_3);
                    }
                });
                fourthImage.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        getImageUrl("http://isochasmic-circuits.000webhostapp.com/images/uploaded_images/" + email_address + "/" + image_4);
                    }
                });
            } catch (Exception e) {
                Log.e("No image exception", e.getMessage());
            }
            layout.addView(firstImage);
            layout.addView(secondImage);
            layout.addView(thirdImage);
            layout.addView(fourthImage);
        } catch (Exception e) {
            Log.e("The exception", e.getMessage());
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

                    call_number();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity().getBaseContext(), "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void call_number() {


        try {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + Contact.getText().toString()));
            startActivity(callIntent);
        } catch (ActivityNotFoundException activityException) {
            Log.e("Calling a Phone Number", "Call failed", activityException);
        }


    }

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


}
