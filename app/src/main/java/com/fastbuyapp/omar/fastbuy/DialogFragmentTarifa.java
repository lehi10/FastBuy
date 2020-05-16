package com.fastbuyapp.omar.fastbuy;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fastbuyapp.omar.fastbuy.entidades.Cupon;
import com.fastbuyapp.omar.fastbuy.entidades.Ubicaciones;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fastbuyapp.omar.fastbuy.config.Globales;

import java.util.ArrayList;


public class DialogFragmentTarifa extends DialogFragment {

    Button button_enviar_tarifa, button_Add, button_Reduce;
    private RequestQueue dataFromGoogleMapsQueue;

    TextView textView_tarifa;

    // -- Origin --
    double lat_origin , long_origin;
    // -- Dest --
    double lat_dest , long_dest;

    double distance_value;
    String distance_text;

    double priceCalculated;
    double minPrice;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_fragment_tarifa, container, false);

        // Initialization

        button_enviar_tarifa    = (Button)rootView.findViewById(R.id.button_enviar_tarifa);
        textView_tarifa          = (TextView)rootView.findViewById(R.id.txt_tarifa);


        button_Add    = (Button)rootView.findViewById(R.id.button_add);
        button_Reduce    = (Button)rootView.findViewById(R.id.button_reduce);


        lat_origin  = ((TransporteActivity)getActivity()).lat_origin;
        long_origin = ((TransporteActivity)getActivity()).long_origin;
        lat_dest    = ((TransporteActivity)getActivity()).lat_dest;
        long_dest   = ((TransporteActivity)getActivity()).long_dest;

        priceCalculated = 0.0;
        minPrice = 0.0;

        double distance_in_m = get_distance_in_meters();
        // Listeners for events when an option is clicked
        on_Click_enviar_tafifa();

        priceCalculator();

        displayButtons();

        return rootView;
    }

    private void displayButtons() {
        button_Add.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        button_Reduce.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    public void priceCalculator(){



        String URL="https://apifbtransportes.fastbuych.com/Transporte/ubicacion_precios_taxi?auth="+ Globales.tokencito+"&codigo_ciudad="+String.valueOf(Globales.ubicacion);
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.length() > 0) {

                    try {

                        JSONObject json_obj = new JSONObject(response).getJSONObject("data_location");
                        Double min_pricei = Double.valueOf(json_obj.getString("UPB_PrecioTaxiKm"));
                        Double distance_taxi = Double.valueOf(json_obj.getString("UPB_TaxiKm"));
                        Double price_adicional = Double.valueOf(json_obj.getString("UPB_TaxiKmAdicional"));



                        Toast.makeText(getContext(), String.valueOf(distance_value/1000)+" "+String.valueOf(Globales.ubicacion), Toast.LENGTH_LONG).show();

                        minPrice = min_pricei;
                        priceCalculated=((distance_value/1000) * price_adicional)/distance_taxi;

                        if(priceCalculated <= minPrice) {
                            priceCalculated = min_pricei;
                        }

                        textView_tarifa.setText(String.valueOf(priceCalculated));



                    } catch (JSONException e) {

                        e.printStackTrace();
                    }

                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                dismiss();

                //Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        queue.add(stringRequest);
    }


    /**
     * Send Fragment Results with final amount of money to charge
     */
    private void on_Click_enviar_tafifa() {
        button_enviar_tarifa.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });
    }

    /**
     * Obtain the distance betwen two points from Google Maps API
     */
    private double get_distance_in_meters(){
        //Toast.makeText(getContext(), "("+str_lat_origin+","+str_long_origin+") ("+str_lat_dest+","+str_long_dest+")", Toast.LENGTH_LONG).show();

        dataFromGoogleMapsQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        String str_lat_origin=Double.toString(lat_origin);
        String str_long_origin=Double.toString(long_origin);

        String str_lat_dest=Double.toString(lat_dest);
        String str_long_dest=Double.toString(long_dest);

        final ProgressDialog dialogLoad = ProgressDialog.show(getActivity(), "Loading...", "Please wait...", true);



        String url ="https://maps.googleapis.com/maps/api/directions/json?origin="+str_lat_origin+","+str_long_origin+"&destination="+str_lat_dest+","+str_long_dest+"&key="+Globales.apiKeyMiMaps;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialogLoad.dismiss();

                    JSONObject json_obj = new JSONObject(response);
                    JSONObject distanceObject= json_obj.getJSONArray("routes").getJSONObject(0).
                            getJSONArray("legs").getJSONObject(0).
                            getJSONObject("distance");

                    distance_text    = distanceObject.getString("text");
                    // that regardless of what unit system is displayed as text, the distance.value field always contains a value expressed in meters. (Source: Official Google Maps Documentation )
                    distance_value   = distanceObject.getDouble("value");

                } catch (JSONException e) {
                    //Toast.makeText(getContext(), "Ruta invalida ", Toast.LENGTH_LONG).show();
                    dialogLoad.dismiss();
                    e.printStackTrace();
                    //dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialogLoad.dismiss();
            }
        });

        dataFromGoogleMapsQueue.add(stringRequest);

        return distance_value;
    }



}
