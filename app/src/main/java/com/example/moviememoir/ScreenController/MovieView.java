package com.example.moviememoir.ScreenController;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moviememoir.R;
import com.example.moviememoir.ServerConnection.Server;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MovieView extends Fragment {
    TextView movieViewScreenLabel;
    TextView movieViewMovieNameLabel;
    TextView movieViewMovieNameOutput;
    TextView movieViewReleaseDateLabel;
    TextView movieViewReleaseDateOutput;
    TextView movieViewGenreLabel;
    TextView movieViewGenreOutput;
    TextView movieViewCastLabel;
    TextView movieViewCastOutput;
    TextView movieViewCountryLabel;
    TextView movieViewCountryOutput;
    TextView movieViewDirectorLabel;
    TextView movieViewDirectorOutput;
    TextView movieViewSummaryLabel;
    TextView movieViewSummaryOutput;
    TextView movieViewRatingLabel;
    TextView movieViewRatingScore;
    RatingBar movieViewRatingOutput;
    Button addToWatchlistButton;
    Button addToMemoirButton;
    private String movieID;
    private String movieName;
    private String releaseDate;
    private Bitmap bitmap;
    List<String> resultList;
    List<String> resultList2;
    List<String> resultList3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_view, container, false);
        movieViewScreenLabel = view.findViewById(R.id.movieViewScreenLabel);
        movieViewMovieNameLabel = view.findViewById(R.id.movieViewMovieNameLabel);
        movieViewMovieNameOutput = view.findViewById(R.id.movieViewMovieNameOutput);
        movieViewReleaseDateLabel  = view.findViewById(R.id.movieViewReleaseDateLabel);
        movieViewReleaseDateOutput = view.findViewById(R.id.movieViewReleaseDateOutput);
        movieViewGenreLabel = view.findViewById(R.id.movieViewGenreLabel);
        movieViewGenreOutput = view.findViewById(R.id.movieViewGenreOutput);
        movieViewCastLabel = view.findViewById(R.id.movieViewCastLabel);
        movieViewCastOutput = view.findViewById(R.id.movieViewCastOutput);
        movieViewCountryLabel = view.findViewById(R.id.movieViewCountryLabel);
        movieViewCountryOutput = view.findViewById(R.id.movieViewCountryOutput);
        movieViewDirectorLabel = view.findViewById(R.id.movieViewDirectorLabel);
        movieViewDirectorOutput = view.findViewById(R.id.movieViewDirectorOutput);
        movieViewSummaryLabel = view.findViewById(R.id.movieViewSummaryLabel);
        movieViewSummaryOutput = view.findViewById(R.id.movieViewSummaryOutput);
        movieViewRatingLabel = view.findViewById(R.id.movieViewRatingLabel);
        movieViewRatingScore = view.findViewById(R.id.movieViewRatingScore);
        movieViewRatingOutput = view.findViewById(R.id.movieViewRatingOutput);
        addToWatchlistButton = view.findViewById(R.id.addToWatchlistButton);
        addToMemoirButton = view.findViewById(R.id.addToMemoirButton);
        //get passed value from movie search
        Bundle bundle = getArguments();
        movieID = bundle.getString("MovieID");
        releaseDate = bundle.getString("Release Date");
        movieName = bundle.getString("Movie Name");
        bitmap = bundle.getParcelable("Image");

        movieViewMovieNameOutput.setText(movieName);
        new movieDetailAsyncTask().execute(movieID);

        addToWatchlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Date currentTime = Calendar.getInstance().getTime();
                bundle.putString("movieID",movieID);
                bundle.putString("movieName",movieName);
                bundle.putString("releaseDate",releaseDate);
                bundle.putString("currentTime",currentTime.toString());
                Fragment fragment = new Watchlist();
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame,fragment).commit();
            }
        });

        addToMemoirButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("movieID",movieID);
                bundle.putString("movieName",movieName);
                bundle.putString("releaseDate",releaseDate);
                bundle.putParcelable("Image",bitmap);

                Fragment fragment = new AddToMemoir();
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame,fragment).commit();
            }
        });
        return view;
    }

    //get movie detail from the movieDB api which is movie detail api
    private class movieDetailAsyncTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            return Server.getMovieDetail(strings[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new getDetailAsyncTask().execute(result);
        }
    }
    //get the target data and pass them into view
    private class getDetailAsyncTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            resultList = Server.getDetails(strings[0]);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            movieViewCountryOutput.setText(resultList.get(0));
            movieViewGenreOutput.setText(resultList.get(1));
            movieViewReleaseDateOutput.setText(resultList.get(2));
            movieViewSummaryOutput.setText(resultList.get(3));
            movieViewRatingScore.setText(resultList.get(4));
            float ratingFloat = Float.parseFloat(resultList.get(4))/10*5;
            movieViewRatingOutput.setRating(ratingFloat);
            new getCreditAsyncTask().execute(movieID);
        }
    }
    //cast and director are in the different api section which is credit api
    private class getCreditAsyncTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            return Server.getCredit(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            new getCastAndDirectorAsyncTask().execute(s);
        }
    }
    //get the target data and pass them into view
    private class getCastAndDirectorAsyncTask extends AsyncTask<String,Void,Void> {

        @Override
        protected Void doInBackground(String... strings) {
            resultList2 = Server.getCast(strings[0]);
            resultList3 = Server.getDirector(strings[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ArrayList<String> allCast = new ArrayList<>();
            for (String s: resultList2){
                allCast.add(s);
            }
            movieViewCastOutput.setText(allCast.toString().replace("[","").replace("]",""));
            ArrayList<String> allDirectors = new ArrayList<>();
            for (String s: resultList2){
                allDirectors.add(s);
            }
            movieViewDirectorOutput.setText(allDirectors.toString().replace("[","").replace("]",""));

        }
    }
}

