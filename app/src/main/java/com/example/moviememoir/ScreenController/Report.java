package com.example.moviememoir.ScreenController;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.moviememoir.R;
import com.example.moviememoir.ServerConnection.Server;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class Report extends Fragment {

    TextView reportScreenLabel;
    TextView startDateLabel;
    TextView startDateOutput;
    Button startDateButton;
    TextView endDateLabel;
    TextView endDateOutput;
    Button endDateButton;
    Spinner yearSpinner;
    static PieChart pieChart;
    static BarChart barChart;
    Button pieChartButton;
    Button barChartButton;
    TextView yearLabel;
    String spinnerState;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report, container, false);

       reportScreenLabel = view.findViewById(R.id.reportScreenLabel);
       startDateLabel = view.findViewById(R.id.startDateLabel);
       startDateOutput = view.findViewById(R.id.startDateOutput);
       startDateButton = view.findViewById(R.id.startDateButton);
       endDateLabel = view.findViewById(R.id.endDateLabel);
       endDateOutput = view.findViewById(R.id.endDateOutput);
       endDateButton = view.findViewById(R.id.endDateButton);
       yearSpinner = view.findViewById(R.id.yearSpinner);
       pieChart = view.findViewById(R.id.pie_chart);
       barChart = view.findViewById(R.id.bar_chart);
       pieChartButton = view.findViewById(R.id.pieChartButton);
       barChartButton = view.findViewById(R.id.barChartButton);
       yearLabel = view.findViewById(R.id.yearLabel);

        Calendar calendar = Calendar.getInstance();// get a calendar using the current time zone and locale of the system. For example today is 10/05/2020, it will get 10,4, 2020
        final int calendarYear = calendar.get(Calendar.YEAR);
        final int calendarMonth = calendar.get(Calendar.MONTH);
        final int caldendarDay = calendar.get(Calendar.DAY_OF_MONTH);
       startDateButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                   @Override
                   public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                       startDateOutput.setText(dateFormatting(year,month,dayOfMonth));
                   }
               },calendarYear,calendarMonth,caldendarDay); //set current day, month, year into datepicker
               DatePicker datePicker = datePickerDialog.getDatePicker();//initialize datepicker to set max date, get the datetime that user picked
               datePicker.setMaxDate(System.currentTimeMillis());//dob is no more than current date
               datePickerDialog.show();
           }
       });

       endDateButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                   @Override
                   public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                       endDateOutput.setText(dateFormatting(year,month,dayOfMonth));
                   }
               },calendarYear,calendarMonth,caldendarDay); //set current day, month, year into datepicker
               DatePicker datePicker = datePickerDialog.getDatePicker();//initialize datepicker to set max date, get the datetime that user picked
               datePicker.setMaxDate(System.currentTimeMillis());//dob is no more than current date
               datePickerDialog.show();
           }
       });

       pieChartButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               new findByUseridANDStartingdateANDEnddateAsyncTask().execute(startDateOutput.getText().toString(),endDateOutput.getText().toString());
           }
       });

        //set up year by using spinner
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList("2015","2016","2017","2018","2019","2020"));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        yearSpinner.setAdapter(arrayAdapter);
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerState =(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinnerState = "2019";
            }
        });

        barChartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }

    private static class findByUseridANDStartingdateANDEnddateAsyncTask extends AsyncTask<String,Void, List<PieEntry>>{
        @Override
        protected List<PieEntry> doInBackground(String... strings) {
            return Server.findByUseridANDStartingdateANDEnddate(strings[0],strings[1]);
        }

        @Override
        protected void onPostExecute(List<PieEntry> pieEntries) {
            super.onPostExecute(pieEntries);
            pieChart.setUsePercentValues(false);
            pieChart.getDescription().setEnabled(false);
            pieChart.setExtraOffsets(5, 10, 5, 5);
            ArrayList<Integer> colors = new ArrayList<Integer>();
            for (int c : ColorTemplate.MATERIAL_COLORS)
                colors.add(c);
            for (int c : ColorTemplate.JOYFUL_COLORS)
                colors.add(c);
            for (int c : ColorTemplate.COLORFUL_COLORS)
                colors.add(c);
            colors.add(ColorTemplate.getHoloBlue());
            PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
            pieDataSet.setColors(colors);
            PieData pie = new PieData();
            pie.setDataSet(pieDataSet);
            pieChart.setData(pie);
            pieChart.invalidate();
        }
    }

    private static class findByUseridANDYearAsyncTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            return Server.findByUseridANDYear(strings[0]);
        }
    }



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
        result = year + "-" + monthFormatting + '-' + dayFormatting;
        return result;
    }
}
