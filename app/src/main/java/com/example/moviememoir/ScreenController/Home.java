package com.example.moviememoir.ScreenController;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.moviememoir.Model.WatchlistViewModel;
import com.example.moviememoir.R;
import com.example.moviememoir.ServerConnection.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class Home extends Fragment {

    TextView welcomeHomeLabel;
    TextView currentDateLabel;
    TextView currentDateOutput;
    TextView topMovieLabel;
    ListView topMovieListView;
    ImageView homeImageView;
    public static String lat;
    public static String lng;
    private List<HashMap<String,String>> topMovieList = new ArrayList<>();
    public static WatchlistViewModel watchlistViewModel;

    public Home() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        welcomeHomeLabel = view.findViewById(R.id.welcomeHomeLabel);
        currentDateLabel = view.findViewById(R.id.currentDateLabel);
        currentDateOutput = view.findViewById(R.id.currentDateOutput);
        topMovieLabel = view.findViewById(R.id.topMovieLabel);
        topMovieListView = view.findViewById(R.id.topMovieListView);
        homeImageView = view.findViewById(R.id.homeImageView);
        //database initialize
        watchlistViewModel = new ViewModelProvider(this).get(WatchlistViewModel.class);
        watchlistViewModel.initializeVars(getActivity().getApplication());

        //set welcome user message
        welcomeHomeLabel.setText("Welcome "+ Signin.usertable.getName());

        //set current date
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date(System.currentTimeMillis());
        currentDateOutput.setText(simpleDateFormat.format(date));

        //get top movie
        new topMovieAsyncTask().execute();
        return view;
    }

    private class topMovieAsyncTask extends AsyncTask<String, Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            return Server.findHighRatingMovieNameByUserid(Signin.usertable.getUserid());
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONArray jsonArray = new JSONArray(s);
                for (int j = 0; j < jsonArray.length(); j++){
                    JSONObject jsonObject = jsonArray.getJSONObject(j);
                    //put target data into hash map for display in list view in the future
                    HashMap<String,String> hashMap = new HashMap<>();
                    hashMap.put("Movie Name",jsonObject.getString("Movie Name"));
                    hashMap.put("Release Date",jsonObject.getString("Release Date"));
                    hashMap.put("Rating Score",jsonObject.getString("Rating Score"));
                    topMovieList.add(hashMap);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //apply listview
            String[] columnHeader = new String[]{"Movie Name", "Release Date", "Rating Score"};
            int[] dataCell = new int[]{R.id.listViewMovieNameOutput,R.id.listViewReleaseDateOutput,R.id.listViewRatingOutput};
            SimpleAdapter simpleAdapter = new SimpleAdapter(Home.this.getContext(),topMovieList,R.layout.list_view_home,columnHeader,dataCell);
            topMovieListView.setAdapter(simpleAdapter);
            new findUserLocationAsyncTask().execute(Signin.usertable.getAddress());
        }
    }

    private static class findUserLocationAsyncTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            return Server.findLocation(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray candidates = new JSONArray(jsonObject.getString("candidates"));
                JSONObject object = (JSONObject) candidates.get(0);
                JSONObject geometry = new JSONObject(object.getString("geometry"));
                JSONObject location = new JSONObject(geometry.getString("location"));
                lat = location.getString("lat");
                lng = location.getString("lng");


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
