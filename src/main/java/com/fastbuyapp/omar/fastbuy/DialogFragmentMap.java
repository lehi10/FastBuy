package com.fastbuyapp.omar.fastbuy;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Objects;

public class DialogFragmentMap extends DialogFragment implements OnMapReadyCallback{
    GoogleMap   mMap;
    double      latitude, longitude;

    Button      button_continue;
    Button      button_cancel;
    EditText    txt_direccion_exacta;
    TextView    title_location_fragment;
    String      orig_dest_flag;


    public DialogFragmentMap() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.dialog_fragment_map, container, false);

        button_cancel           = (Button) rootView.findViewById(R.id.button_cancel_map);
        button_continue         = (Button) rootView.findViewById(R.id.button_continue_map);
        txt_direccion_exacta    = (EditText) rootView.findViewById(R.id.txt_direccion_exacta);

        title_location_fragment          = (TextView) rootView.findViewById(R.id.title_location_fragment);

        orig_dest_flag = this.getArguments().getString("orig_dest_flag");

        SupportMapFragment mapFragment = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Listeners for events when an option is clicked
        on_Click_Cancel();
        on_Click_Continue();

        return rootView;
    }

    /**
     * Method for Click event on Button Cancel
     */
    private void on_Click_Cancel() {
        button_cancel.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });
    }

    /**
     * Method for Click event on Button Continue
     */
    private void on_Click_Continue() {

        button_continue.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                String direccion_exacta = txt_direccion_exacta.getText().toString();
                if(!direccion_exacta.equals("") ){
                    if(orig_dest_flag.equals("origin")){
                        ((TransporteActivity)getActivity()).textView_Ubicacion.setText(direccion_exacta);
                        ((TransporteActivity)getActivity()).setOriginCoordinates(latitude,longitude);

                        getDialog().dismiss();
                    }
                    if(orig_dest_flag.equals("dest")){
                        ((TransporteActivity)getActivity()).textView_Destino.setText(direccion_exacta);
                        ((TransporteActivity)getActivity()).setDestCoordinates(latitude,longitude);
                        getDialog().dismiss();
                    }
                }
                else{
                    Toast.makeText(getActivity(), "Ingrese la dirección",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     *  Method to Display Map on Dialog Fragment and Draw the marker in the correct position
     *  Also, this method allow the draggable marker.
     * @param googleMap as a Object to Display the Map
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Latitude and Longitude by Default
        latitude =-9.5298147;
        longitude =-77.5291624;

        // Get data from origin
        if(orig_dest_flag == "origin") {
            double temp_lat     = ((TransporteActivity)getActivity()).lat_origin;
            double temp_long    = ((TransporteActivity)getActivity()).long_origin;
            if( temp_lat != 0.0 &&  temp_long != 0.0) {
                latitude    = temp_lat;
                longitude   = temp_long;
            }
            String temp_direcction = ((TransporteActivity)getActivity()).textView_Ubicacion.getText().toString();
            if(temp_direcction != "" ){
                txt_direccion_exacta.setText(temp_direcction);
            }
            title_location_fragment.setText(" Dirección Exacta de Partida");
        }
        // Get data from Dest
        else if (orig_dest_flag =="dest") {
            double temp_lat     = ((TransporteActivity)getActivity()).lat_dest;
            double temp_long    = ((TransporteActivity)getActivity()).long_dest;

            if( temp_lat != 0.0 &&  temp_long != 0.0){
                latitude    = temp_lat;
                longitude   = temp_long;
            }
            String temp_direcction = ((TransporteActivity)getActivity()).textView_Destino.getText().toString();
            if(temp_direcction != "" ){
                txt_direccion_exacta.setText(temp_direcction);
            }
            title_location_fragment.setText(" Dirección Exacta de Destino");
        }


        LatLng latLng = new LatLng(latitude,longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,18));

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Posición");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mMap.addMarker(markerOptions);

        // Setting a click event handler for the map
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {
                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();
                // Setting the position for the marker
                markerOptions.position(latLng);
                // Clears the previously touched position
                mMap.clear();
                // Animating to the touched position
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                // Placing a marker on the touched position
                mMap.addMarker(markerOptions);
                // Saving Latitude and longitude
                latitude = latLng.latitude;
                longitude = latLng.longitude;
            }
        });
    }

    /**
     * Method for delete Dialog Fragment
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        assert getFragmentManager() != null;
        Fragment fragment = (getFragmentManager().findFragmentById(R.id.map));
        FragmentTransaction ft = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        ft.remove(fragment);
        ft.commit();
    }



}
