package com.example.moviememoir.ScreenController;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moviememoir.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;

public class Maps extends Fragment implements OnMapReadyCallback  {

    View view;
    private MapView mapView;
    private GoogleMap googleMap;
    public static String cinemaLat;
    public static String cinemaLng;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_maps, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstance) {
        super.onViewCreated(view, savedInstance);
        mapView = (MapView) this.view.findViewById(R.id.mapview);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        LatLng loc = new LatLng(Double.parseDouble(Home.lat), Double.parseDouble(Home.lng));
        this.googleMap.addMarker(new MarkerOptions().position(loc).title("Home"));
            LatLng cinema = new LatLng(Double.parseDouble(cinemaLat), Double.parseDouble(cinemaLng));
            googleMap.addMarker(new MarkerOptions().position(cinema).title(Home.cinemaNameList.get(0)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).alpha(0.7f));

        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 12.0f));
    }

}
