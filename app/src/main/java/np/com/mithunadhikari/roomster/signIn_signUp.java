package np.com.mithunadhikari.roomster;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

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

public class signIn_signUp extends AppCompatActivity {
    public static final String USERNAME = "USERNAME";
    public static final String PASSWORD = "PASSWORD";
    static Context context;
    public EditText username;
    public EditText password;
    public Button signIn, signUp;
    public TextView forgotPassword;
    SharedPreferences myPrefs;
    String user_name;
    String pass_word;
    private String loginUrl = "https://isochasmic-circuits.000webhostapp.com/login.php";

    //  MODE_WORLD_READABLE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //(signIn_signUp.this).getSupportActionBar().hide();
        startService(new Intent(this,cityList.class));
        myPrefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        String userName = myPrefs.getString(USERNAME, null);
        String passWord = myPrefs.getString(PASSWORD, null);
        try {

            if ((userName.length() > 0) && (passWord.length() > 0)) {
                startActivity(new Intent(this, Navigation_Drawer.class));

            }
        } catch (Exception e) {
            Log.e("The exception", e.getMessage());
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_sign_up);
        try {

            setTitle("Sign in");
        } catch (Exception e) {
            Log.e("Exception in title", e.getMessage());
        }
        FirebaseMessaging.getInstance().subscribeToTopic("message");
        FirebaseInstanceId.getInstance().getToken();

        init();


    }


    public void init() {
        username =  findViewById(R.id.usernameEdit);
        password = (EditText) findViewById(R.id.editPassword);
        signIn = (Button) findViewById(R.id.signin_btn);
        signUp = (Button) findViewById(R.id.signup_btn);
        forgotPassword = (TextView) findViewById(R.id.forget_password);
        Intent intent = getIntent();
        String getNewUserName = intent.getStringExtra("new_user_name");
        String getNewUserPass = intent.getStringExtra("new_user_pass");
        username.setText(getNewUserName);
        password.setText(getNewUserPass);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline(getApplicationContext())) {

                    Intent mainPage = new Intent(signIn_signUp.this, Signup.class);
                    Toast.makeText(signIn_signUp.this, "Loading Please wait", Toast.LENGTH_LONG).show();
                    startActivity(mainPage);
                } else {
                    Toast.makeText(getApplicationContext(), "Sorry no active network Available\n Please connect to internet and try again!", Toast.LENGTH_LONG).show();
                }

            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline(getApplicationContext())) {

                    showPopUp();
                } else {
                    Toast.makeText(getApplicationContext(), "Sorry no active network Available\n Please connect to internet and try again!", Toast.LENGTH_LONG).show();
                }

            }
        });


    }


    private void showPopUp() {

        try {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Forgot password");
            alertDialog.setCancelable(false);
            final EditText first_input = new EditText(this);
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            try {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                lp.setMargins(20, 15, 20, 0);
                first_input.setLayoutParams(lp);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            first_input.setHint("Enter your email address");

            layout.addView(first_input);
            alertDialog.setView(layout);
            first_input.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS);
            first_input.setSelection(first_input.getText().length());

            alertDialog.setNeutralButton("Change",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String email_add = first_input.getText().toString();

                            if (email_add.length() > 8) {
                                if (email_add.contains("@") && email_add.contains(".")) {
                                    loginUrl = "https://isochasmic-circuits.000webhostapp.com/send_random_number.php";
                                    new backGroundWorker(getApplicationContext()).execute(email_add, "hehehe");
                                } else {
                                    Toast.makeText(getApplicationContext(), "Wrong email entered", Toast.LENGTH_LONG).show();

                                }

                            } else {
                                Toast.makeText(getApplicationContext(), "Email too short", Toast.LENGTH_LONG).show();

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
        } catch (Exception e) {
            Log.e("The alert exception", e.getMessage());
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    public void onLogin(View view) {
        if (isOnline(getApplicationContext())) {
            signIn = (Button) findViewById(R.id.signin_btn);
            signUp = (Button) findViewById(R.id.signup_btn);
            username = (EditText) findViewById(R.id.usernameEdit);
            password = (EditText) findViewById(R.id.editPassword);
            final String Username = username.getText().toString().trim();
            final String Password = password.getText().toString().trim();
            if (Username.length() < 2) {
                username.setError("Required");
            }

            new backGroundWorker(this).execute(Username, Password);


        } else {
            Toast.makeText(getApplicationContext(), "Sorry no active network Available\n Please connect to internet and try again!", Toast.LENGTH_LONG).show();

        }
    }


    @Override
    public void onBackPressed() {
        this.finishAffinity();
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

    public void confirm_text(final String emailAdd, final String message) {
        try {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Forgot password");
            alertDialog.setCancelable(false);
            final EditText rand_num = new EditText(this);
            final EditText new_pass = new EditText(this);
            final EditText confirm_pass = new EditText(this);
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            lp.setMargins(20, 15, 20, 0);
            rand_num.setLayoutParams(lp);
            new_pass.setLayoutParams(lp);
            confirm_pass.setLayoutParams(lp);
           /* } catch (Exception e) {
                Log.e("FIrst exception",e.getMessage());
            }*/

            rand_num.setHint("Enter the received number");
            new_pass.setHint("Enter your new password");
            confirm_pass.setHint("Enter password again");
            rand_num.setInputType(InputType.TYPE_CLASS_NUMBER);
            rand_num.setSelection(rand_num.getText().length());
            new_pass.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            new_pass.setSelection(rand_num.getText().length());
            confirm_pass.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            confirm_pass.setSelection(rand_num.getText().length());

            layout.addView(rand_num);
            layout.addView(new_pass);
            layout.addView(confirm_pass);
            alertDialog.setView(layout);
            alertDialog.setNeutralButton("Change",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String number = rand_num.getText().toString();
                            if (number.equals(message)) {
                                String newPass = new_pass.getText().toString();
                                String confirmPass = confirm_pass.getText().toString();
                                if (newPass.equals(confirmPass)) {
                                    if ((newPass.length() > 5) && (confirmPass.length() > 5)) {
                                        loginUrl = "https://isochasmic-circuits.000webhostapp.com/reset_forgot_password.php";

                                        new backGroundWorker(signIn_signUp.this).execute(emailAdd, newPass);

                                    } else {
                                        Toast.makeText(context, "Passwords is too short", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(context, "Passwords do not match", Toast.LENGTH_LONG).show();

                                }

                            } else {
                                Toast.makeText(context, "Numbers do not match", Toast.LENGTH_LONG).show();

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
        } catch (Exception e) {
            Log.e("The alert exception", e.getMessage());
        }
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
                user_name = params[0];
                pass_word = params[1];
                URL url = new URL(loginUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String postData = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(user_name, "UTF-8") + "&"
                        + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(pass_word, "UTF-8");
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
                Log.e("Pre execute", e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {
            try {
                Log.e("Server report", result);
            } catch (Exception e) {
            }
            try {

                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            } catch (Exception e) {
                Log.e("Post execute", e.getMessage());
            }
            String status = "";
            String message = "";
            String tag = "";
            String mail = "";
            try {

                JSONObject jsonResult = new JSONObject(result);
                status = jsonResult.optString("status");
                message = jsonResult.optString("message");
                tag = jsonResult.optString("tag");
                mail = jsonResult.optString("mail");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            switch (tag) {
                case "login":
                    if (message.contains("logged")) {
                        SharedPreferences.Editor editor = myPrefs.edit();
                        editor.putString("USERNAME", user_name);
                        editor.putString(PASSWORD, pass_word);
                        editor.apply();
                        Intent intent = new Intent(signIn_signUp.context, Navigation_Drawer.class);
                        context.startActivity(intent);
                    } else {
                        SharedPreferences.Editor editor = myPrefs.edit();
                        editor.putString("USERNAME", "");
                        editor.putString(PASSWORD, "");
                        editor.apply();
                        alertDialog.setTitle(status);
                        alertDialog.setMessage(message);
                        alertDialog.show();
                    }
                    break;
                case "random_number":
                    if (message.contains("success")) {
                        Toast.makeText(getApplicationContext(), "Please wait\n Processing", Toast.LENGTH_SHORT).show();
                        confirm_text(mail, status);
                    } else {

                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                    break;
                case "reset_pass":
                    if (message.equals("success")) {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }

                    break;
                default:
                    break;

            }
        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }


    }


}




