package com.example.moviememoir.ScreenController;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moviememoir.Model.Credentialstable;
import com.example.moviememoir.Model.Usertable;
import com.example.moviememoir.R;
import com.example.moviememoir.ServerConnection.Server;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Scanner;

public class Signup extends AppCompatActivity {

    TextView signupScreenLabel;
    TextView firstnameLabel;
    EditText firstnameInput;
    TextView surnameLabel;
    EditText surnameInput;
    RadioGroup genderRadioGroup;
    TextView dayOfBirthLabel;
    TextView dayOfBirthOutput;
    Button datepickerButton;
    TextView addressLabel;
    EditText addressInput;
    TextView stateLabel;
    Spinner stateSpinner;
    TextView postcodeLabel;
    EditText postcodeInput;
    TextView signupUsernameLabel;
    EditText signupUsernameInput;
    TextView signupPasswordLabel;
    EditText signupPasswordInput;
    Button confirmSignupButton;
    private Usertable usertable = new Usertable();
    private Credentialstable credentialstable = new Credentialstable();
    private int userCount;
    private ArrayList<String> existingUsername = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        signupScreenLabel = findViewById(R.id.signupScreenLabel);
        firstnameLabel = findViewById(R.id.firstnameLabel);
        firstnameInput = findViewById(R.id.firstnameInput);
        surnameLabel = findViewById(R.id.surnameLabel);
        surnameInput = findViewById(R.id.surnameInput);
        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        dayOfBirthLabel = findViewById(R.id.dayOfBirthLabel);
        dayOfBirthOutput = findViewById(R.id.dayOfBirthOutput);
        datepickerButton = findViewById(R.id.datepickerButton);
        addressLabel = findViewById(R.id.addressLabel);
        addressInput = findViewById(R.id.addressInput);
        stateLabel = findViewById(R.id.stateLabel);
        stateSpinner = findViewById(R.id.stateSpinner);
        postcodeLabel = findViewById(R.id.postcodeLabel);
        postcodeInput = findViewById(R.id.postcodeInput);
        signupUsernameLabel = findViewById(R.id.signupUsernameLabel);
        signupUsernameInput = findViewById(R.id.signupUsernameInput);
        signupPasswordLabel = findViewById(R.id.signupPasswordLabel);
        signupPasswordInput = findViewById(R.id.signupPasswordInput);
        confirmSignupButton = findViewById(R.id.confirmSignupButton);

        //get gender by using radio group
        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                switch (radioButton.getText().toString()){
                    case "Male":
                        usertable.setGender("Male");
                        break;
                    case "Female":
                        usertable.setGender("Female");
                        break;

                }

            }
        });

        //set up sign up date
        Calendar calendar = Calendar.getInstance();// get a calendar using the current time zone and locale of the system. For example today is 10/05/2020, it will get 10,4, 2020
        final int calendarYear = calendar.get(Calendar.YEAR);
        final int calendarMonth = calendar.get(Calendar.MONTH);
        final int caldendarDay = calendar.get(Calendar.DAY_OF_MONTH);
        credentialstable.setSignupdate(dateFormatting(calendarYear,calendarMonth,caldendarDay));

        //set up day of birth by using datepicker
        datepickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Signup.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dayOfBirthOutput.setText(dateFormatting(year,month,dayOfMonth));
                        usertable.setDob(dateFormatting(year,month,dayOfMonth));
                    }
                },calendarYear,calendarMonth,caldendarDay); //set current day, month, year into datepicker
                DatePicker datePicker = datePickerDialog.getDatePicker();//initialize datepicker to set max date, get the datetime that user picked
                datePicker.setMaxDate(System.currentTimeMillis());//dob is no more than current date
                datePickerDialog.show();
            }
        });

        //set up state by using spinner
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList("VIC","SA","TAS","WA","NT","QLD","NSW","ACT"));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        stateSpinner.setAdapter(arrayAdapter);
        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                usertable.setState(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        new existingUsernameAsyncTask().execute();
        //set up sign up button to submit form
        confirmSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //validation check
                boolean inputValidation = true;
                if (firstnameInput.getText().toString().isEmpty()){
                    inputValidation = false;
                    firstnameInput.setError("The firstname is empty");
                }
                if (surnameInput.getText().toString().isEmpty()){
                    inputValidation = false;
                    surnameInput.setError("The surname is empty");
                }
                if (addressInput.getText().toString().isEmpty()){
                    inputValidation = false;
                    addressInput.setError("The address is empty");
                }
                if (postcodeInput.getText().toString().isEmpty()){
                    inputValidation = false;
                    postcodeInput.setError("The postcode is empty");
                }
                if (signupUsernameInput.getText().toString().isEmpty()){
                    inputValidation = false;
                    signupUsernameInput.setError("The username is empty");
                }

                //check if there is any username in the list is same as user's input
                for (int j = 0; j < existingUsername.size(); j++){
                    //check redundant usernam
                    if (signupUsernameInput.getText().toString().equals(existingUsername.get(j))){
                        signupUsernameInput.setError("The username exists");
                        inputValidation = false;
                    }
                }

                if (inputValidation){
                    usertable.setUserid(userCount+1);//the new userid should be current total number plus 1
                    usertable.setSurname(surnameInput.getText().toString());
                    usertable.setName(firstnameInput.getText().toString());
                    usertable.setAddress(addressInput.getText().toString());
                    usertable.setPostcode(postcodeInput.getText().toString());
                    //post the new usertable data into database
                    new usertableAsyncTask().execute(usertable);
                    Intent intent = new Intent(Signup.this,Signin.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(Signup.this,"Please check your input", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    //formatting date
    public String dateFormatting(int year, int month, int day){
        String result ="";
        int currentmonth = month + 1; //month from datepicker is current month -1, therefore, we need to add 1 back
        String monthFormatting = String.valueOf(currentmonth);
        String dayFormatting = String.valueOf(day);
        if (monthFormatting.length() == 1){
            monthFormatting = "0" + monthFormatting;//append the month at the end of stringBuffer which is after 0
        }
        if (dayFormatting.length() == 1){
            dayFormatting = 0 + dayFormatting;
        }
        result = year + "-" + monthFormatting + '-' + dayFormatting + "T00:00:00+00:00";
        return result;
    }

    //get current user id by counting the number of existing id
    //use AsyncTask to avoid time consuming when user is waiting to have multiple thread
    private class currentUseridAsyncTask extends AsyncTask<Void,Void,String> {
        @Override
        protected String doInBackground(Void...voids)  {
           return Server.findCurrentUseridCount();
        }
        @Override
        protected void onPostExecute(String s){
            userCount = Integer.parseInt(s);//get current counting number of userid
        }
    }
    //get all the username to check if user enter existing username. record all username into list
    private class existingUsernameAsyncTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            String s = Server.findAllUsername();
            try {
                JSONArray jsonArray = new JSONArray(s);
                for (int j = 0; j < jsonArray.length(); j++){
                    JSONObject jsonObject = jsonArray.getJSONObject(j);
                    existingUsername.add(jsonObject.getString("username"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        //to set asynctask order, put next asynctask in the post execute
        @Override
        protected void onPostExecute(Void nothing){
             new currentUseridAsyncTask().execute();
        }
    }

    //post new user data into database
    private class usertableAsyncTask extends AsyncTask<Usertable,Void,Usertable>{

        @Override
        protected Usertable doInBackground(Usertable... usertables) {
            Server.postUsertable(usertables[0]);
            return usertables[0];
        }

        @Override
        protected void onPostExecute(Usertable usertable) {
            super.onPostExecute(usertable);
            credentialstable.setCredentialsid(usertable.getUserid());
            credentialstable.setUserid(usertable);
            credentialstable.setUsername(signupUsernameInput.getText().toString());
            credentialstable.setPasswordhash(Signin.md5(signupPasswordInput.getText().toString()));
            new credentialstableAsyncTask().execute(credentialstable);

        }
    }
    //post new credentials data into database
    private class credentialstableAsyncTask extends AsyncTask<Credentialstable,Void,Void>{

        @Override
        protected Void doInBackground(Credentialstable... credentialstables) {
            Server.postcredentialstable(credentialstables[0]);
            return null;
        }
    }
}
