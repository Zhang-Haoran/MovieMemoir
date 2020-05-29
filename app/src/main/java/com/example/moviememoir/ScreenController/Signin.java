package com.example.moviememoir.ScreenController;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moviememoir.MainActivity;
import com.example.moviememoir.Model.Usertable;
import com.example.moviememoir.R;
import com.example.moviememoir.ServerConnection.Server;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Signin extends AppCompatActivity {

    private String usernameValue;
    private String passwordValue;
    TextView signinScreenLabel;
    TextView usernameLabel;
    EditText usernameInput;
    TextView passwordLabel;
    EditText passwordInput;
    Button signinButton;
    Button signupButton;

    public static Usertable usertable = new Usertable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        //initialize every widgets of layout
        signinScreenLabel = findViewById(R.id.signinScreenLabel);
        usernameLabel = findViewById(R.id.usernameLabel);
        usernameInput = findViewById(R.id.usernameInput);
        passwordLabel = findViewById(R.id.passwordLabel);
        passwordInput = findViewById(R.id.passwordInput);
        signinButton = findViewById(R.id.signinButton);
        signupButton = findViewById(R.id.signupButton);

        //user click sign in button
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameValue = usernameInput.getText().toString();
                passwordValue = passwordInput.getText().toString();
                //sign in input cannot be empty
                if (usernameValue.isEmpty()){
                    usernameInput.setError("Username cannot be empty");
                    Toast.makeText(Signin.this,"Please check your username",Toast.LENGTH_SHORT).show();
                }
                else {
                    //find user's account in database
                    new signinAsyncTask().execute(usernameValue,passwordValue);


                }
            }
        });

        //user click sign up button
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signin.this,Signup.class);
                startActivity(intent);
            }
        });
    }

    private class signinAsyncTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {
            String usernameValue = strings[0];
            String passwordValue = md5(strings[1]);
            return Server.findByUsernameAndPassword(usernameValue,passwordValue);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //if the given username and password cannot find a pair in database return []. therefore, to check them here.
            if (s.equals("[]") || s.equals("")){
                usernameInput.setError("Incorrect login credential");
                passwordInput.setError("Incorrect login credential");
            }
            else {
                try{
                    //get username and userid pass it to home
                    JSONArray jsonArray = new JSONArray(s);
                    for(int j = 0; j < jsonArray.length(); j++){
                        JSONObject jsonObject = jsonArray.getJSONObject(j);
                        JSONObject useridJson = jsonObject.getJSONObject("userid");
                        Signin.usertable.setUserid(useridJson.getInt("userid"));
                        Signin.usertable.setName(useridJson.getString("name"));
                        Signin.usertable.setSurname(useridJson.getString("surname"));
                        Signin.usertable.setState(useridJson.getString("state"));
                        Signin.usertable.setPostcode(useridJson.getString("postcode"));
                        Signin.usertable.setGender(useridJson.getString("gender"));
                        Signin.usertable.setDob(useridJson.getString("dob"));
                        Signin.usertable.setAddress(useridJson.getString("address"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(Signin.this, MainActivity.class);
                startActivity(intent);
            }
        }
    }

    //use md5 to hash the password
    public static String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }



}
