package com.fastbuyapp.omar.fastbuy;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fastbuyapp.omar.fastbuy.HelperEsquema.UsuarioDbHelper;
import com.fastbuyapp.omar.fastbuy.adaptadores.CiudadMapaAdapter;
import com.fastbuyapp.omar.fastbuy.config.Globales;
import com.fastbuyapp.omar.fastbuy.config.Servidor;
import com.fastbuyapp.omar.fastbuy.entidades.Ubicacion;
import com.fastbuyapp.omar.fastbuy.entidades.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class CiudadActivity extends AppCompatActivity {

    GridView ciudades;
    CiudadMapaAdapter adapter;
    ProgressDialog progDailog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ciudad);

        Globales.typefaceNexa = Typeface.createFromAsset(getAssets(), Globales.Nexa);
        Globales.typefaceGothic = Typeface.createFromAsset(getAssets(), Globales.Gothic);

        TextView txtMensaje = (TextView) findViewById(R.id.txtMensaje);
        ciudades = findViewById(R.id.gvCiudadesMapa);

        String consulta2 = "https://apifbajustes.fastbuych.com/sucursal/ListarApp";
        EnviarRecibirDatosCiudades(consulta2);

        /*Button btnChepen = (Button) findViewById(R.id.btnChepen);
        Button btnGuadalupe = (Button) findViewById(R.id.btnGuadalupe);
        Button btnPacasmayo = (Button) findViewById(R.id.btnPacasmayo);*/

        /*btnChepen.setTypeface(Globales.typefaceNexa);
        btnGuadalupe.setTypeface(Globales.typefaceNexa);
        btnPacasmayo.setTypeface(Globales.typefaceNexa);
        txtMensaje.setTypeface(Globales.typefaceGothic);
        btnChepen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Globales.ciudadOrigen = "Chepén";
                Globales.ubicacion = 1;
                //GuardarCiudad(Globales.ciudadOrigen);
                Intent intent = new Intent(CiudadActivity.this, PrincipalActivity.class);
                startActivity(intent);
            }
        });
        btnGuadalupe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Globales.ciudadOrigen = "Guadalupe";
                Globales.ubicacion = 2;
                //GuardarCiudad(Globales.ciudadOrigen);
                Intent intent = new Intent(CiudadActivity.this, PrincipalActivity.class);
                startActivity(intent);
            }
        });
        btnPacasmayo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Globales.ciudadOrigen = "Pacasmayo";
                Globales.ubicacion = 3;
                //GuardarCiudad(Globales.ciudadOrigen);
                Intent intent = new Intent(CiudadActivity.this, PrincipalActivity.class);
                startActivity(intent);
            }
        });*/
    }

    public void GuardarCiudad(String ciudad){
        try {
            SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(CiudadActivity.this);

            SharedPreferences.Editor myEditor = myPreferences.edit();
            myEditor.putString("City_Cliente",ciudad);
            myEditor.commit();

            //para recuperar la información de la cache
            //String name = myPreferences.getString("Name_Cliente", "default")

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void EnviarRecibirDatosCiudades(String URL){
        progDailog = new ProgressDialog(CiudadActivity.this);
        progDailog.setMessage("Cargando Ciudades...");
        progDailog.setIndeterminate(true);
        progDailog.setCancelable(false);
        progDailog.show();
        RequestQueue queue = Volley.newRequestQueue(CiudadActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.length()>0){
                    try {
                        Globales.listCiudades.clear();
                        JSONArray ja = new JSONArray(response);
                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject objeto = ja.getJSONObject(i);
                            Ubicacion ubicacion = new Ubicacion();
                            ubicacion.setCodigo(objeto.getInt("UBI_Codigo"));
                            ubicacion.setNombre(objeto.getString("UBI_Nombre"));
                            ubicacion.setEstado(objeto.getInt("UBI_Estado"));
                            ubicacion.setLat(objeto.getDouble("UBI_Latitud"));
                            ubicacion.setLon(objeto.getDouble("UBI_Longitud"));
                            ubicacion.setRadio(objeto.getInt("UBI_Radio"));
                            ubicacion.setPreciobase(objeto.getDouble("UPB_Pedidos"));
                            ubicacion.setPrecioextra(objeto.getDouble("UPB_Extras"));

                            Globales.listCiudades.add(ubicacion);
                        }

                        try {
                            ciudades.setNumColumns(1);
                            adapter = new CiudadMapaAdapter(CiudadActivity.this,R.layout.list_item_ciudad_mapa,Globales.listCiudades);
                            ciudades.setAdapter(adapter);
                            progDailog.dismiss();
                        } catch (Exception ex) {
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.v("error_de_carga",error.getMessage());
                progDailog.dismiss();
                Intent intent = new Intent(CiudadActivity.this, ActivityDesconectado.class);
                startActivity(intent);
            }
        });

        queue.add(stringRequest);

    }

    @Override
    public void onBackPressed (){
        finish();
    }
}
