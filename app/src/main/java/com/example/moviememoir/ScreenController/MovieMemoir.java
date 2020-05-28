package com.example.moviememoir.ScreenController;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.moviememoir.R;
import com.example.moviememoir.ServerConnection.Server;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MovieMemoir extends Fragment {

    TextView movieMemoirScreenLabel;
    TextView movieMemoirLabel;
    ListView movieMemoirListView;
    Button sortByDateButton;
    Button sortByRatingButton;
    Button sortByPublicRatingButton;
    Spinner genreSpinner;
    private List<HashMap<String,String>> movieMemoirList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_memoir, container, false);
        movieMemoirScreenLabel = view.findViewById(R.id.movieMemoirScreenLabel);
        movieMemoirLabel = view.findViewById(R.id.movieMemoirLabel);
        movieMemoirListView = view.findViewById(R.id.movieMemoirListView);
        sortByDateButton = view.findViewById(R.id.sortByDateButton);
        sortByRatingButton = view.findViewById(R.id.sortByRatingButton);
        sortByPublicRatingButton = view.findViewById(R.id.sortByPublicRatingButton);
        genreSpinner = view.findViewById(R.id.genreSpinner);

        //get all the movie memoir data and display in the list view
        new memoirAsyncTask().execute();

        //set up state by using spinner
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList("Drama","Horror","Action","Comedy"));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        genreSpinner.setAdapter(arrayAdapter);
        genreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sortByDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        sortByPublicRatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        sortByRatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }

    private class memoirAsyncTask extends AsyncTask<Void,Void,String> {

        @Override
        protected String doInBackground(Void... voids) {
            return Server.findAllMovieMemoir();
        }
        protected  void onPostExecute(String s){
            try {
                JSONArray jsonArray = new JSONArray(s);
                for (int j =0; j < jsonArray.length();j++){
                    JSONObject jsonObject = jsonArray.getJSONObject(j);
                    HashMap<String,String> hashMap = new HashMap<>();
                    hashMap.put("moviename",jsonObject.getString("moviename"));
                    hashMap.put("moviereleasedate",jsonObject.getString("moviereleasedate"));
                    hashMap.put("ratingscore",jsonObject.getString("ratingscore"));
                    hashMap.put("comment",jsonObject.getString("comment"));
                    hashMap.put("watchdate",jsonObject.getString("watchdate"));
                    hashMap.put("suburb",jsonObject.getJSONObject("cinemaid").getString("suburb"));
                    movieMemoirList.add(hashMap);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //apply listview
            String[] columnHeader = new String[]{"moviename", "moviereleasedate","watchdate","suburb","comment","ratingscore"};
            int[] dataCell = new int[]{R.id.movieMemoirMovieName,R.id.movieMemoirReleaseDate,R.id.MovieMemoirWatchDate,R.id.movieMemoirSuburb,R.id.movieMemoirComment,R.id.movieMemoirRating};
            SimpleAdapter simpleAdapter = new SimpleAdapter(MovieMemoir.this.getContext(),movieMemoirList,R.layout.list_view_movie_memoir,columnHeader,dataCell);
            movieMemoirListView.setAdapter(simpleAdapter);
        }
    }
}
