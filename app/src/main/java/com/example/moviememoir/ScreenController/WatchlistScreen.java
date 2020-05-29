package com.example.moviememoir.ScreenController;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moviememoir.Model.Watchlist;
import com.example.moviememoir.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class WatchlistScreen extends Fragment {

    TextView watchlistScreenLabel;
    TextView watchlistLabel;
    ListView watchlistListView;
    Button watchlistViewButton;
    Button watchlistDeleteButton;
    private String movieID;
    private String watchID;
    private String movieName;
    Button shareButton;
    CallbackManager callbackManager;
    ShareDialog shareDialog;

    private List<HashMap<String,String>> allWachlistsWithAllInformation = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_watchlist, container, false);

        watchlistScreenLabel = view.findViewById(R.id.watchlistScreenLabel);
        watchlistLabel = view.findViewById(R.id.watchlistLabel);
        watchlistListView = view.findViewById(R.id.watchlistListView);
        watchlistViewButton = view.findViewById(R.id.watchlistViewButton);
        watchlistDeleteButton = view.findViewById(R.id.watchlistDeleteButton);
        shareButton = view.findViewById(R.id.shareButton);

        FacebookSdk.sdkInitialize(this.getContext().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareLinkContent linkContent = new ShareLinkContent.Builder().setQuote("This is the movie name in my watchlist")
                        .setContentUrl(Uri.parse("https://www.themoviedb.org/"))
                        .build();
                if (ShareDialog.canShow(ShareLinkContent.class)){
                    shareDialog.show(linkContent);
                }
            }
        });

        Home.watchlistViewModel.getAllWatchlist(String.valueOf(Signin.usertable.getUserid())).observe(getViewLifecycleOwner(), new Observer<List<Watchlist>>() {
            @Override
            public void onChanged(List<Watchlist> watchlists) {
                allWachlistsWithAllInformation.clear();
                for (Watchlist watchlist: watchlists){
                    HashMap<String,String> hashMap = new HashMap<>();
                    hashMap.put("movie name",watchlist.getMovieName());
                    hashMap.put("movie id",watchlist.getMovieid());
                    hashMap.put("watch id",String.valueOf(watchlist.getWatchid()));
                    hashMap.put("release date",watchlist.getReleaseDate());
                    hashMap.put("add date",watchlist.getAddDate());
                    hashMap.put("add time",watchlist.getAddTime());
                    allWachlistsWithAllInformation.add(hashMap);
                }
                //apply listview
                String[] columnHeader = new String[]{"movie name","release date","add date","add time","movie id", "watch id"};
                int[] dataCell = new int[]{R.id.watchlistViewMovieName,R.id.watchListViewReleaseDate,R.id.watchlistViewAddDate,R.id.watchlistViewAddTime,R.id.watchlistViewMovieID,R.id.watchlistViewWatchID};
                SimpleAdapter simpleAdapter = new SimpleAdapter(WatchlistScreen.this.getActivity(), allWachlistsWithAllInformation,R.layout.list_view_watchlist,columnHeader,dataCell);
                watchlistListView.setAdapter(simpleAdapter);
            }
        });

        watchlistListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                movieID = allWachlistsWithAllInformation.get(position).get("movie id");
                watchID = allWachlistsWithAllInformation.get(position).get("watch id");
                movieName = allWachlistsWithAllInformation.get(position).get("movie name");
            }
        });

        watchlistViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new MovieView();
                Bundle bundle = new Bundle();
                bundle.putString("MovieID",movieID);
                bundle.putString("Movie Name",movieName);
                bundle.putString("movieExist","exist");
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame,fragment).commit();
            }
        });

        watchlistDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(WatchlistScreen.this.getContext());
                alert.setTitle("Confirm deleting?");
                alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Home.watchlistViewModel.deleteByID(Integer.parseInt(watchID));
                        Toast.makeText(getContext(),"The movie has been deleted",Toast.LENGTH_SHORT).show();
                    }
                });
                 alert.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
            alert.show();
            }

        });

        return view;
    }
}
