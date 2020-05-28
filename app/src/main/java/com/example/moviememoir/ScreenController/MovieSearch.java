package com.example.moviememoir.ScreenController;

import android.app.ActivityManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Parcelable;
import android.renderscript.ScriptGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moviememoir.R;
import com.example.moviememoir.ServerConnection.Server;

import java.io.InputStream;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MovieSearch extends Fragment {
    TextView movieSearchScreenLabel;
    TextView movieSearchLabel;
    EditText movieSearchInput;
    Button movieSearchButton;
    TextView movieSearchOutput;
    ListView movieSearchListView;
    private List<String> APImovieList = new ArrayList<>();
    private List<HashMap<String,Object>> classifiedAPImovieList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_search, container, false);
        movieSearchScreenLabel = view.findViewById(R.id.movieSearchScreenLabel);
        movieSearchLabel = view.findViewById(R.id.movieSearchLabel);
        movieSearchInput = view.findViewById(R.id.movieSearchInput);
        movieSearchButton = view.findViewById(R.id.movieSearchButton);
        movieSearchOutput = view.findViewById(R.id.movieSearchOutput);
        movieSearchListView = view.findViewById(R.id.movieSearchListView);

        //set up search button
        movieSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (movieSearchInput.getText().toString().isEmpty()){
                    movieSearchInput.setError("Please enter a movie");
                    Toast.makeText(getContext(),"Please check your input",Toast.LENGTH_SHORT).show();
                }
                new searchMovieAsyncTask().execute(movieSearchInput.getText().toString());
            }
        });
        return view;
    }

    //search movie from The movieDB API
    private class searchMovieAsyncTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            return Server.searchMovie(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            new passResultToListViewAsyncTask().execute(s);
        }
    }

    //fetch data from search result and pass them to list view
    private class passResultToListViewAsyncTask extends AsyncTask<String,Void, SimpleAdapter>{

        @Override
        protected SimpleAdapter doInBackground(String... strings) {
            APImovieList = Server.getSnippet(strings[0]);
            for (int j = 0; j < APImovieList.size(); j+=4){//every 4 items is a group
                HashMap<String,Object> hashMap = new HashMap<>();
                Bitmap bitmap;
                hashMap.put("Movie Name", APImovieList.get(j));
                hashMap.put("Release Date",APImovieList.get(j+1));
                //get image url from the api
                String movieImageURL = "https://image.tmdb.org/t/p/w600_and_h900_bestv2/" + APImovieList.get(j+2);
                //transfer the image into bitmap
                try {
                    URL url = new URL(movieImageURL);
                    InputStream inputStream = url.openStream();
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();
                    hashMap.put("Image",bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                hashMap.put("MovieID",APImovieList.get(j+3));
                classifiedAPImovieList.add(hashMap);
            }//set up list view
            String[] columnHead = new String[]{"Image","Movie Name","Release Date","MovieID"};
            int[] dataCell = new int[]{R.id.searchMovieImage, R.id.searchMovieName,R.id.searchMovieReleaseDate,R.id.searchMovieID};
            SimpleAdapter simpleAdapter = new SimpleAdapter(MovieSearch.this.getContext(),classifiedAPImovieList,R.layout.list_view_search,columnHead,dataCell);
            //let list view displaying image by using bitmap
            simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Object data, String textRepresentation) {
                    if((view instanceof ImageView) & (data instanceof Bitmap) ) {
                        ImageView imageView = (ImageView) view;
                        Bitmap bitmap = (Bitmap) data;
                        imageView.setImageBitmap(bitmap);
                        return true;
                    }
                    return false;
                }
            });
            return simpleAdapter;

        }

        @Override
        protected void onPostExecute(SimpleAdapter simpleAdapter) {
            super.onPostExecute(simpleAdapter);
            movieSearchListView.setAdapter(simpleAdapter);
            movieSearchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //when the item is clicked pass the view to movie view screen
                    Fragment fragment = new MovieView();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("Image", (Parcelable) classifiedAPImovieList.get(position).get("Image"));
                    bundle.putString("Movie Name",classifiedAPImovieList.get(position).get("Movie Name").toString());
                    bundle.putString("Release Date",classifiedAPImovieList.get(position).get("Release Date").toString());
                    bundle.putString("MovieID",classifiedAPImovieList.get(position).get("MovieID").toString());

                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame,fragment).commit();

                }
            });
        }
    }
}
