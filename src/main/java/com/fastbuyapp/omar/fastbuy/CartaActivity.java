package com.fastbuyapp.omar.fastbuy;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fastbuyapp.omar.fastbuy.config.Globales;
import com.fastbuyapp.omar.fastbuy.config.Servidor;
import com.fastbuyapp.omar.fastbuy.entidades.Categoria;
import com.fastbuyapp.omar.fastbuy.entidades.Empresa;
import com.fastbuyapp.omar.fastbuy.entidades.Producto;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.snackbar.Snackbar;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.core.view.MenuCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;

import android.view.Menu;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class CartaActivity extends AppCompatActivity {
    GridView gridViewCatego;
    public String codigo;
    //public String nombreComercial;
    public int codCatego;
    public String categoria;
    ArrayList<Producto> list;
    ArrayList<Categoria> list1;
    ProductosListAdapter adapter = null;
    CategoriasCartaListAdapter adapter1 = null;
    DrawerLayout drawer;

    //private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carta);
        displayMenu();

        //Start Diseño de popup
        DisplayMetrics medidasVentana = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(medidasVentana);

        int ancho = medidasVentana.widthPixels;
        final int alto = medidasVentana.heightPixels;

        getWindow().setLayout((int)(ancho*0.80), (int)(alto));
        getWindow().setGravity(Gravity.LEFT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //End Diseño de popup

        TextView tituloCarta = (TextView) findViewById(R.id.txtTituloCarta);

        final TextView nomEmpresa = (TextView) findViewById(R.id.txtNomEmpresa);
        nomEmpresa.setText(Globales.nombreEmpresaSeleccionada+" te ofrece...");

        if (Globales.categoria == 1){
            tituloCarta.setText("LA CARTA");
        }else if (Globales.categoria == 2){
            tituloCarta.setText("PRODUCTOS");
        }

        Button btnAll = (Button) findViewById(R.id.btnVerTodo);

        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Globales.catProductoSeleccionado=0;
                onBackPressed();
            }
        });
    }

    public void displayMenu(){
        try {
            listarCategorias(String.valueOf(Globales.empresaSeleccionada),String.valueOf(Globales.ubicacion));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void listarCategorias(String empresa, String ubicacion) throws UnsupportedEncodingException {
        String consulta = "https://apifbdelivery.fastbuych.com/Delivery/ListaCategoriasProdXEmpresXUbicacion?auth="+Globales.tokencito+"&empresa="+empresa+"&ubica="+ubicacion;
        EnviarRecibirDatosCategorias(consulta);
    }

    public void EnviarRecibirDatosCategorias(String URL){
        RequestQueue queue = Volley.newRequestQueue(CartaActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.length()>0){
                    try {
                        JSONArray ja = new JSONArray(response);
                        CargarListaCategorias(ja);
                    } catch (JSONException e) {
                        onBackPressed();
                        Toast toast = Toast.makeText(CartaActivity.this, "No Existen Categorias por seleccionar...", Toast.LENGTH_SHORT);
                        View vistaToast = toast.getView();
                        vistaToast.setBackgroundResource(R.drawable.toast_yellow);
                        toast.show();
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(stringRequest);

    }

    public void CargarListaCategorias(JSONArray ja) throws JSONException {
        list1 = new ArrayList<>();
        for(int i = 0; i < ja.length(); i++) {
            JSONObject objeto = ja.getJSONObject(i);
            Categoria categoria = new Categoria();
            categoria.setCodigo(objeto.getInt("CAT_Codigo"));
            categoria.setDescripcion(objeto.getString("CAT_Descripcion"));
            categoria.setEstado(objeto.getInt("CAT_Estado"));
            list1.add(categoria);
        }
        try {
            gridViewCatego = (GridView) findViewById(R.id.tablaListaCatego);
            adapter1 = new CategoriasCartaListAdapter(CartaActivity.this, R.layout.list_item_carta, list1);
            gridViewCatego.setAdapter(adapter1);

            gridViewCatego.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int codCatego =(list1.get(position).getCodigo());
                    Globales.catProductoSeleccionado = codCatego;
                    //Toast.makeText(CartaActivity.this, "Presionaste "+String.valueOf(codCatego), Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            });
        }
        catch (Exception ex){

        }
    }
}
