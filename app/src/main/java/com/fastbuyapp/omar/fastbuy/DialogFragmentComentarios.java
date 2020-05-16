package com.fastbuyapp.omar.fastbuy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fastbuyapp.omar.fastbuy.R;
import com.fastbuyapp.omar.fastbuy.config.Globales;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class DialogFragmentComentarios extends DialogFragment {
    Button button_enviar_comentarios;
    LinearLayout linearLayoutChebox;

    CheckBox chkArray[];
    int chkArraySize;

    JSONArray json_ArrayCheckBox;

    String idCheckBoxSelected;
    String textCheckBoxSelected;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_fragment_comentarios, container, false);

        button_enviar_comentarios = (Button)rootView.findViewById(R.id.button_enviar_comentarios);

        linearLayoutChebox = (LinearLayout) rootView.findViewById(R.id.layout_checkboxs);

        idCheckBoxSelected="";


        button_enviar_comentarios.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!idCheckBoxSelected.equals(""))
                {
                    ((TransporteActivity)getActivity()).setSpecificationsData(idCheckBoxSelected,textCheckBoxSelected);
                    ((TransporteActivity)getActivity()).textView_comments.setText(textCheckBoxSelected);
                }


                getDialog().dismiss();
            }
        });

        loadCheckBoxData();


        return rootView;
    }

    private void loadCheckBoxData() {

        String URL="https://apifbtransportes.fastbuych.com/Transporte/getEspecifications?auth="+ Globales.tokencito;


        Log.i("URL",URL);

        final ProgressDialog dialogGetFromDB;
        dialogGetFromDB = new ProgressDialog(getActivity());
        dialogGetFromDB.setMessage("Iniciando Delivery");
        dialogGetFromDB.setIndeterminate(true);
        dialogGetFromDB.setCancelable(false);
        dialogGetFromDB.show();

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.length() > 0) {

                    try {

                        json_ArrayCheckBox = new JSONObject(response).getJSONArray("response");
                        chkArraySize =json_ArrayCheckBox.length();
                        Log.i("RESPONSE SIZE",String.valueOf(chkArraySize));

                        chkArray=new CheckBox[chkArraySize];

                        for(int i = 0; i < chkArraySize; i++) {
                            CheckBox cbox = new CheckBox(getActivity().getApplicationContext());
                            cbox.setText(json_ArrayCheckBox.getJSONObject(i).getString("ESP_Descripcion"));
                            chkArray[i] = cbox;
                            linearLayoutChebox.addView(chkArray[i]);
                        }
                        checkBoxOnSelectOneItem();
                        dialogGetFromDB.dismiss();


                    } catch (JSONException e) {
                        dialogGetFromDB.dismiss();
                        e.printStackTrace();
                    }

                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                dialogGetFromDB.dismiss();
                dismiss();
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

        queue.add(stringRequest);


    }

    private void checkBoxOnSelectOneItem() {

        Log.i("RESP",String.valueOf(chkArraySize));
        for(int i = 0; i < chkArraySize; i++) {

            final int finalI = i;
            chkArray[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for(int j = 0; j < chkArraySize; j++) {
                        if(j!= finalI){
                            chkArray[j].setChecked(false);
                        }
                        else{
                            chkArray[j].setChecked(true);
                        }
                    }
                    try {
                        idCheckBoxSelected = json_ArrayCheckBox.getJSONObject(finalI).getString("ESP_Codigo");
                        textCheckBoxSelected = json_ArrayCheckBox.getJSONObject(finalI).getString("ESP_Descripcion");

                        Log.i("Selected",idCheckBoxSelected);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }

}
