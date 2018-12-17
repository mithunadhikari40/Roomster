package np.com.mithunadhikari.roomster;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.iid.FirebaseInstanceId;

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

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


public class Navigation_Drawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    static Context context;
    SharedPreferences myPrefs;
    String email_address;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_navigation__drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            setTitle("Roomster");
        } catch (Exception e) {

        }

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View view = navigationView.getHeaderView(0);
        Model_Class model_class = new Model_Class();
        model_class.setWhich_class("navigation_drawer");

        myPrefs = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        email_address = myPrefs.getString("USERNAME", null);
        imageView = (ImageView) view.findViewById(R.id.profile_circular_image);
        showAllData();
        /*setCircularImage();*/
        Glide.with(this)
                .load("https://isochasmic-circuits.000webhostapp.com/images/profile/profile_picture/" + email_address + ".png")
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
       /* FirebaseMessaging.getInstance().subscribeToTopic("message");
        FirebaseInstanceId.getInstance().getToken();*/

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toProfile();
                if (drawer.isDrawerOpen(Gravity.LEFT)) {
                    drawer.closeDrawer(Gravity.LEFT);
                }
            }
        });

    }


    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
            this.finishAffinity();
        } else {
            getFragmentManager().popBackStack();

        }
    }

    private void toProfile() {
        Profile profile = new Profile();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_navigation_drawer, profile);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation__drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (new signIn_signUp().isOnline(getApplicationContext())) {
            if (id == R.id.nav_home) {
                showAllData();
            } else if (id == R.id.nav_profile) {
                toProfile();
            } else if (id == R.id.nav_upload) {
                Upload upload = new Upload();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_navigation_drawer, upload);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
            } else if (id == R.id.nav_search) {
                Search search = new Search();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_navigation_drawer, search);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
            } else if (id == R.id.nav_sign_out) {
                SharedPreferences prefs = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("USERNAME", "");
                editor.putString("PASSWORD", "");
                editor.apply();
                clearToken();
                startActivity(new Intent(this, signIn_signUp.class));
            }
        } else {
            Toast.makeText(getApplicationContext(), "Sorry no active network Available\n Please connect to internet and try again!", Toast.LENGTH_LONG).show();

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void clearToken() {
        try {
            String token = FirebaseInstanceId.getInstance().getToken();
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add("token", token)
                    .build();
            Toast.makeText(getApplicationContext(), "Hell yeah", Toast.LENGTH_SHORT).show();


            Request request = new Request.Builder()
                    .url("https://isochasmic-circuits.000webhostapp.com/clear_token.php")
                    .post(body)
                    .build();
            try {
           /* Response response=*/
                client.newCall(request).execute();
                Toast.makeText(getApplicationContext(), String.valueOf(client.newCall(request).execute()), Toast.LENGTH_LONG).show();

            } catch (IOException e) {
                // Toast.makeText(getApplicationContext() ,"Hell yeah again"+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        } catch (Exception ex) {

        }
    }

    private void showAllData() {
        DisplayAllData displayAllData = new DisplayAllData();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_navigation_drawer, displayAllData);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();

    }

   /* public boolean isOnline() {
        ConnectivityManager cm;
        NetworkInfo netInfo = null;

        try {
            cm =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            netInfo = cm.getActiveNetworkInfo();
        } catch (Exception e) {

        }
        return netInfo != null && netInfo.isConnected();

    }*/


    class backGroundWorker extends AsyncTask<String, Void, String> {


        backGroundWorker(Context ctx) {
            context = ctx;
        }

        @Override
        protected String doInBackground(String... params) {


            try {
                String token = params[0];
                String pass_word = params[1];
                URL url = new URL("https://isochasmic-circuits.000webhostapp.com/clear_token.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String postData = URLEncoder.encode("token", "UTF-8") + "=" + URLEncoder.encode(token, "UTF-8");
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

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String result) {
            try {
                Log.e("Server report", result);
            } catch (Exception e) {


                String message = "";

                try {

                    JSONObject jsonResult = new JSONObject(result);

                    message = jsonResult.optString("message");
                    if (!(message.contains("not"))){

                    }

                } catch (JSONException ex) {
                    ex.printStackTrace();
                }


            }


        }
    }
}



