package np.com.mithunadhikari.roomster;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Search extends Fragment {
    public Spinner district;
    public RadioButton room, flat;
    public Spinner maxPrice, minPrice;
    public Button search;
    public String selectedDistrict, selectedMinPrice, selectedMaxPrice, room_flat;
    AutoCompleteTextView selectedCity;
    SharedPreferences myPrefs;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            view = inflater.inflate(R.layout.fragment_search, container, false);
            init();
            addDistrict();
            addMaxPrice();
            addMinPrice();
            search = (Button) view.findViewById(R.id.search_button);
            search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSearch(null);
                }
            });
            // ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Search");
            try {

                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Search");
            } catch (Exception e) {
                Log.e("Exception in title", e.getMessage());
            }
        } catch (Exception e) {
            Log.e("The graet ezception", e.getMessage());
        }
        return view;

    }

    public void init() {
        myPrefs = getActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        selectedCity = (AutoCompleteTextView) view.findViewById(R.id.address_city_search);
        district = (Spinner) view.findViewById(R.id.address_dist_search);
        minPrice = (Spinner) view.findViewById(R.id.search_min_price);
        maxPrice = (Spinner) view.findViewById(R.id.search_max_price);
        room = (RadioButton) view.findViewById(R.id.radio_room_search);
        flat = (RadioButton) view.findViewById(R.id.radio_flat_search);
        search = (Button) view.findViewById(R.id.search_button);
        selectedCity.setHint("Area eg Baneshwor");
        //selectedCity.setHint(Html.fromHtml("<font size=\"10\">" + "Area like: Baneshwor,Kalanki"));


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
                Toast.makeText(getActivity().getBaseContext(), "Please select district", Toast.LENGTH_LONG).show();

            }
        });


        minPrice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMinPrice = parent.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getActivity().getBaseContext(), "Please select minimum price", Toast.LENGTH_LONG).show();

            }
        });

        maxPrice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMaxPrice = parent.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getActivity().getBaseContext(), "Please select maximum price", Toast.LENGTH_LONG).show();

            }
        });

/*
         List cities = new storeCityList(getActivity(), null, null, 2).getString();
*/
        /*set=*/
        try {
            String set = myPrefs.getString("cities", null);
            System.out.println("search"+set);
            String new_string = set.substring(set.indexOf("[") + 1, set.indexOf("]"));
            List<String> myList = new ArrayList<>(Arrays.asList(new_string.split(",")));
            int size = myList.size();
            String[] mArray = new String[size];
            for (int i = 0; i < size; i++) {
                mArray[i] = myList.get(i);
                mArray[i] = mArray[i].substring(mArray[i].indexOf("") + 1, mArray[i].lastIndexOf("") - 1);
                //System.out.println("Finally"+mArray[i].toString());
            }
            ArrayAdapter adapter =
                    new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, mArray);
            selectedCity.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
            /*for(int i=0;i<size;i++){
                mArray[i]=myList.get(i);
                mArray[i]=mArray[i].substring(mArray[i].indexOf("")+1,mArray[i].lastIndexOf("")-1);
                System.out.println("Finally"+mArray[i].toString());
            }

            hi
*/
        //System.out.println("arraylist"+myList);

        //String[] mArray=new String[]{"Baneshor","Chagu","Kalanki"};
        //System.out.println("new string is"+new_string);
        //JSONArray temp = new JSONArray(set);
        // final String[] mArray = temp.join(" ").split(" ");

            /* final String[] mArray=new String[] {new_string};
            for (int i=0;i<mArray.length;i++) {
                System.out.println("newly created" + mArray[i]);
            }*/
        // String[] mArray=new String[]{};


//
       /* String [] cityList = set.toArray(new String[set.size()]);
        System.out.println("The fuck of cities"+cityList);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, cityList);
        selectedCity.setAdapter(adapter);*/


   /* public void getErrorMessage(String title, String messages) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity().getBaseContext());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(messages);
        builder.show();

    }*/

  /*  public void getSearchData(String messages) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity().getBaseContext());
        builder.setMessage(messages);
        builder.show();

    }*/


    public void addDistrict() {
        district = (Spinner) view.findViewById(R.id.address_dist_search);
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
        city = (Spinner) view.findViewById(R.id.address_city_search);

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

    }*/

    public void addMinPrice() {

        minPrice = (Spinner) view.findViewById(R.id.search_min_price);
        //minPrice.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        List<String> minPriceList = new ArrayList<>();
        minPriceList.add("Min price");
        minPriceList.add("1000-");
        minPriceList.add("1000");
        minPriceList.add("2000");
        minPriceList.add("4000");
        minPriceList.add("5000");
        minPriceList.add("10000");
        minPriceList.add("20000");
        minPriceList.add("20000+");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_spinner_item, minPriceList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        minPrice.setAdapter(dataAdapter);


    }

    public void addMaxPrice() {

        maxPrice = (Spinner) view.findViewById(R.id.search_max_price);
        // maxPrice.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        List<String> maxPriceList = new ArrayList<>();
        maxPriceList.add("Max price");
        maxPriceList.add("2000");
        maxPriceList.add("4000");
        maxPriceList.add("5000");
        maxPriceList.add("10000");
        maxPriceList.add("20000");
        maxPriceList.add("30000");
        maxPriceList.add("30000+");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_spinner_item, maxPriceList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        maxPrice.setAdapter(dataAdapter);


    }

    public void onSearch(View view) {
        //String type = "search";
        //backGroundWorker backGroundWorker = new backGroundWorker(getActivity().getBaseContext());
        //selectedCity=(EditText)view.findViewById(R.id.address_city_search);
        if (new Upload().isOnline(getActivity())) {

            if ((selectedMinPrice == "1000-") || selectedMinPrice.equals("Min price")) {
                selectedMinPrice = "0";
            }
            if ((selectedMaxPrice == "30000+") || selectedMaxPrice.equals("Max price")) {
                selectedMaxPrice = "99999999";
            }
            if (room.isChecked() || flat.isChecked()) {
                if (!(district.getSelectedItem().toString().equalsIgnoreCase("district"))) {

                    if (Integer.parseInt(selectedMaxPrice) >= Integer.parseInt(selectedMinPrice)) {

                        newCopy32 displaySearchResult = new newCopy32();
                        Bundle bundle = new Bundle();
                        bundle.putString("room_flat", room_flat);
                        bundle.putString("district", district.getSelectedItem().toString());
                        bundle.putString("city", selectedCity.getText().toString());
                        bundle.putString("minPrice", selectedMinPrice);
                        bundle.putString("maxPrice", selectedMaxPrice);
                        displaySearchResult.setArguments(bundle);
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.content_navigation_drawer, displaySearchResult);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        ft.addToBackStack(null);
                        ft.commit();

                        //backGroundWorker.execute(type, room_flat, district.getSelectedItem().toString(), city.getSelectedItem().toString(), selectedMinPrice, selectedMaxPrice);


                    } else {
                        Toast.makeText(getActivity().getBaseContext(), "Maximum price must be grater than minimum price", Toast.LENGTH_SHORT).show();

                    }

                } else {
                    Toast.makeText(getActivity().getBaseContext(), "Select a district", Toast.LENGTH_SHORT).show();

                }
            } else {
                Toast.makeText(getActivity().getBaseContext(), "Select room or flat", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "Sorry no active network Available\n Please connect to internet and try again!", Toast.LENGTH_LONG).show();

        }

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
}
