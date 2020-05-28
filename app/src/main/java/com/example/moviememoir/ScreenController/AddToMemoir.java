package com.example.moviememoir.ScreenController;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.moviememoir.Model.Cinematable;
import com.example.moviememoir.Model.Memoirtable;
import com.example.moviememoir.R;
import com.example.moviememoir.ServerConnection.Server;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddToMemoir extends Fragment {
    TextView addToMemoirScreenLabel;
    ImageView addToMemoirImageView;
    TextView addToMemoirMovieNameLabel;
    TextView addToMemoirMovieNameOutput;
    TextView addToMemoirReleaseDateLabel;
    TextView addToMemoirReleaseDateOutput;
    TextView addToMemoirWatchDateLabel;
    TextView addToMemoirWatchDateOutput;
    Button watchDatepickerButton;
    TextView addToMemoirWatchTImeLabel;
    TextView addToMemoirWatchTimeOutput;
    Button watchTimepickerButton;
    TextView addToMeoirCinemaLabel;
    Spinner cinemaSpinner;
    Button addCinemaButton;
    TextView addToMemoirCommentLabel;
    EditText addToMemoirCommentInput;
    TextView addToMemoirRatingLabel;
    RatingBar addToMemoirRatingOutput;
    Button addToMemoirConfirmButton;
    private String movieID;
    private String movieName;
    private String releaseDate;
    private Bitmap bitmap;
    private String watchedDate;
    private String watchedTime;
    private Cinematable cinematable = new Cinematable();
    Memoirtable memoirtable;
    private List<Cinematable> cinematableStringList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_to_memoir, container, false);

        addToMemoirScreenLabel = view.findViewById(R.id.addToMemoirScreenLabel);
        addToMemoirImageView = view.findViewById(R.id.addToMemoirImageView);
        addToMemoirMovieNameLabel = view.findViewById(R.id.addToMemoirMovieNameLabel);
        addToMemoirMovieNameOutput = view.findViewById(R.id.addToMemoirMovieNameOutput);
        addToMemoirReleaseDateLabel = view.findViewById(R.id.addToMemoirReleaseDateLabel);
        addToMemoirReleaseDateOutput = view.findViewById(R.id.addToMemoirReleaseDateOutput);
        addToMemoirWatchDateLabel = view.findViewById(R.id.addToMemoirWatchDateLabel);
        addToMemoirWatchDateOutput = view.findViewById(R.id.addToMemoirWatchDateOutput);
        watchDatepickerButton = view.findViewById(R.id.watchDatepickerButton);
        addToMemoirWatchTImeLabel = view.findViewById(R.id.addToMemoirWatchTImeLabel);
        addToMemoirWatchTimeOutput = view.findViewById(R.id.addToMemoirWatchTimeOutput);
        watchTimepickerButton = view.findViewById(R.id.watchTimepickerButton);
        addToMeoirCinemaLabel = view.findViewById(R.id.addToMeoirCinemaLabel);
        cinemaSpinner = view.findViewById(R.id.cinemaSpinner);
        addCinemaButton = view.findViewById(R.id.addCinemaButton);
        addToMemoirCommentLabel = view.findViewById(R.id.addToMemoirCommentLabel);
        addToMemoirCommentInput = view.findViewById(R.id.addToMemoirCommentInput);
        addToMemoirRatingLabel = view.findViewById(R.id.addToMemoirRatingLabel);
        addToMemoirRatingOutput = view.findViewById(R.id.addToMemoirRatingOutput);
        addToMemoirConfirmButton = view.findViewById(R.id.addToMemoirConfirmButton);
        //get passed value from movie view
        Bundle bundle = getArguments();
        movieID = bundle.getString("movieID");
        movieName = bundle.getString("movieName");
        releaseDate = bundle.getString("releaseDate");
        bitmap = bundle.getParcelable("Image");

        addToMemoirMovieNameOutput.setText(movieName);
        addToMemoirReleaseDateOutput.setText(releaseDate);
        addToMemoirImageView.setImageBitmap(bitmap);

        new findAllCinemaAsyncTask().execute();

        //pick the watch date
        Calendar calendar = Calendar.getInstance();// get a calendar using the current time zone and locale of the system. For example today is 10/05/2020, it will get 10,4, 2020
        final int calendarYear = calendar.get(Calendar.YEAR);
        final int calendarMonth = calendar.get(Calendar.MONTH);// month from calendar starts from 0 end in 11. it needs to + 1 to represent current month
        final int calendarDay = calendar.get(Calendar.DAY_OF_MONTH);

        //date time picker
        watchDatepickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Day of birth
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        watchedDate = dateFormatting(year,month,day);
                        addToMemoirWatchDateOutput.setText(watchedDate);
                    }
                },calendarYear,calendarMonth,calendarDay); //set current day, month, year into datepicker

                DatePicker dp = datePickerDialog.getDatePicker();//initialize datepicker to set max date, get the datetime that user picked
                dp.setMaxDate(System.currentTimeMillis()); //dob is no more than current date
                datePickerDialog.show();
            }

        });

        //pick the watch time
        Calendar calendar1 = Calendar.getInstance();
        final int hour= calendar1.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar1.get(Calendar.MINUTE);
        watchTimepickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        StringBuffer time = new StringBuffer();
                        String hour = "";
                        if (hourOfDay < 10){
                            StringBuffer str = new StringBuffer("0");
                            hour = str.append(hourOfDay).toString();
                        }else {
                            hour = String.valueOf(hourOfDay);
                        }
                        String min = "";
                        if (minute < 10){
                            StringBuffer str = new StringBuffer("0");
                            min = str.append(hourOfDay).toString();
                        }else {
                            min = String.valueOf(hourOfDay);
                        }
                        time.append(hour+":"+min+":00");
                        watchedTime = "2020-01-01T"+time+"+10:00";
                        addToMemoirWatchTimeOutput.setText(watchedTime);
                    }
                },hour,minute,true);
                timePickerDialog.show();
            }
        });


//add a new cinema
        addCinemaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(getActivity());
                final View promptsView = li.inflate(R.layout.add_cinema,null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        getActivity());
                alertDialogBuilder.setView(promptsView);
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        EditText cinemaEditText = promptsView.findViewById(R.id.addCinemaName);
                                        EditText suburbEditText = promptsView.findViewById(R.id.addCinemaSuburb);
                                        String cinemaName = cinemaEditText.getText().toString().trim();
                                        String cinemaSuburb = suburbEditText.getText().toString().trim();
                                        Cinematable cinematable = new Cinematable(Integer.parseInt(movieID),cinemaName,cinemaSuburb,"");

                                        addCinemaAsyncTask addCinemaAsyncTask = new addCinemaAsyncTask();
                                        addCinemaAsyncTask.execute(cinematable);
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();

            }
        });



        addToMemoirConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rDate = releaseDate+ "T00:00:00+10:00";
                memoirtable = new Memoirtable(Integer.parseInt(movieID),movieName,rDate,watchedDate,watchedTime,addToMemoirCommentInput.getText().toString(), (int) addToMemoirRatingOutput.getRating(),cinematable,Signin.usertable);
                new postMemoirAsyncTask().execute(memoirtable);
            }
        });

        return view;
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

    private class findAllCinemaAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {

            String allCinema = Server.findAllCinema();

            return allCinema;
        }

        @Override
        protected void onPostExecute(String allCinema) {
            super.onPostExecute(allCinema);
            ArrayList<String> cinemaList = new ArrayList<>();
            JsonElement jsonElement = new JsonParser().parse(allCinema);
            JsonArray jsonArray =jsonElement.getAsJsonArray();
            for (JsonElement j: jsonArray){
                JsonObject jsonObject = j.getAsJsonObject();
                String cinema = jsonObject.getAsJsonPrimitive("cinemaid").getAsString() + ". " + jsonObject.getAsJsonPrimitive("cinemaname").getAsString() + " " + jsonObject.getAsJsonPrimitive("suburb").getAsString();
                cinemaList.add(cinema);
            }

            try {
                JSONArray jsonArray1 = new JSONArray(allCinema);
                for (int i =0; i<jsonArray1.length();i++){
                    JSONObject jsonObject = jsonArray1.getJSONObject(i);
                    Cinematable c = new Cinematable(jsonObject.getInt("cinemaid"),jsonObject.getString("cinemaname"),jsonObject.getString("suburb"),"");
                    cinematableStringList.add(c);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, cinemaList);
            Spinner spinner = getView().findViewById(R.id.cinemaSpinner);
            spinner.setAdapter(arrayAdapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    cinematable= cinematableStringList.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }



    private class addCinemaAsyncTask extends AsyncTask<Cinematable, Void,Void> {
        @Override
        protected Void doInBackground(Cinematable... cinematables) {
            Server.addCinema(cinematables[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            new findAllCinemaAsyncTask().execute();
        }
    }
    private class postMemoirAsyncTask extends AsyncTask<Memoirtable,Void,Void>{

        @Override
        protected Void doInBackground(Memoirtable... memoirtables) {
            Server.postMemoirtable(memoirtables[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getActivity(),"Post memoir successfully",Toast.LENGTH_SHORT).show();
        }
    }
}
