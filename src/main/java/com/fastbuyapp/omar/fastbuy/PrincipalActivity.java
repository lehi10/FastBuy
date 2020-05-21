package com.fastbuyapp.omar.fastbuy;

import android.app.ProgressDialog;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.fastbuyapp.omar.fastbuy.Operaciones.Calcular_Minutos;
import com.fastbuyapp.omar.fastbuy.adaptadores.RecyclerAdapterPromociones;
import com.fastbuyapp.omar.fastbuy.config.GlideApp;
import com.fastbuyapp.omar.fastbuy.config.Globales;
import com.fastbuyapp.omar.fastbuy.config.Servidor;
import com.fastbuyapp.omar.fastbuy.entidades.Categoria;
import com.fastbuyapp.omar.fastbuy.entidades.Empresa;
import com.fastbuyapp.omar.fastbuy.entidades.Promocion;
import com.fastbuyapp.omar.fastbuy.entidades.Ubicacion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class PrincipalActivity extends AppCompatActivity {

    RecyclerView rvPromociones;
    Spinner ciudades;
    public String codigo;
    public String categoria;
    ArrayList<Promocion> list;
    PromocionListAdapter adapter = null;
    ProgressDialog progDailog = null;
    TextView txtPromociones;
    String elOrigen;

    //Intent myService;

    ImageButton btnCarrito;

    @Override
    protected void onResume() {
        super.onResume();
        Globales.valida.validarCarritoVacio(btnCarrito);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        //elOrigen = getIntent().getStringExtra("origen");//para evitar que el servicio se inicie

        //inicio 2º plano
        if (Globales.myService == null){
            Globales.myService = new Intent(getApplicationContext(), BackgroundChat.class);
            startService(Globales.myService);
        }

        /*if (!elOrigen.equals("Notificacion")){
            if (myService == null){
                myService = new Intent(getApplicationContext(), BackgroundChat.class);
                startService(myService);
            }
        }*/
        //fin 2º plano

        txtPromociones = (TextView) findViewById(R.id.txtPromociones);

        //estableciendo en true el inicio de sesion
        Globales.mySession = true;

        rvPromociones = (RecyclerView) findViewById(R.id.rvPromociones);
        listarPromociones(Globales.ubicacion);

        //Asignando valores a las variables globales ciudadEncargo y codigo
        Globales.CiudadEncargoSeleccionada = Globales.ciudadOrigen;
        Globales.CodigoCiudadEncargoSeleccionada = Globales.ubicacion;

        //Asignando valores a las variables globales ciudadPidelo y codigo
        Globales.CiudadPideloSeleccionada = Globales.ciudadOrigen;
        Globales.CodigoCiudadPideloSeleccionada = Globales.ubicacion;
        String urlGif = "https://fastbuych.com/empresas/categorias/imagenes/marketgif.gif";
        //Agregar implementacion Glide dentro de archivo build.gradle.
        ImageView imageView = (ImageView)findViewById(R.id.imageView8);
        Uri uri = Uri.parse(urlGif);
        GlideApp.with(getApplicationContext())
                .load(uri)
                .transform(new RoundedCornersTransformation(20,0, RoundedCornersTransformation.CornerType.TOP))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        // log exception
                        Log.v("img_promo_carga", "Error loading image", e);
                        return false; // important to return false so the error placeholder can be placed
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(imageView);

        String urlRest = "https://fastbuych.com/empresas/categorias/imagenes/restaurants.jpg";
        //Agregar implementacion Glide dentro de archivo build.gradle.
        ImageView imageView2 = (ImageView)findViewById(R.id.imageView5);
        Uri uri2 = Uri.parse(urlRest);
        GlideApp.with(getApplicationContext())
                .load(uri2)
                .transform(new RoundedCornersTransformation(20,0, RoundedCornersTransformation.CornerType.TOP))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        // log exception
                        Log.v("img_promo_carga", "Error loading image", e);
                        return false; // important to return false so the error placeholder can be placed
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(imageView2);
        //Start Spinner de ciudades
        ciudades = (Spinner) findViewById(R.id.spinnerCiudad);
        //esto consultamos desde la base de datos_ esta en prueba la otra opcion de cargar las ciudades en ciudadActivity
        //String consulta2 = "https://apifbajustes.fastbuych.com/sucursal/ListarApp";
        //EnviarRecibirDatosCiudades(consulta2);
        listaCiudades();
        ciudades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Globales.ciudadOrigen = ciudades.getSelectedItem().toString();
                Globales.CiudadEncargoSeleccionada = Globales.ciudadOrigen;
                Globales.CiudadPideloSeleccionada = Globales.ciudadOrigen;

                Globales.ubicacion = obtenerPosicionCiudadSeleccionada(ciudades.getSelectedItem().toString());

                Globales.CodigoCiudadEncargoSeleccionada = Globales.ubicacion;
                Globales.CodigoCiudadPideloSeleccionada = Globales.ubicacion;

                listarPromociones(Globales.ubicacion);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //End Spinner de ciudades

        LinearLayout llRestaurantes = (LinearLayout) findViewById(R.id.llRestaurantes);
        llRestaurantes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Globales.categoria = 1;
                Intent intent = new Intent(PrincipalActivity.this, DeliveryActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout llMarket = (LinearLayout) findViewById(R.id.llMarket);
        llMarket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Globales.categoria = 2;
                Intent intent = new Intent(PrincipalActivity.this, DeliveryActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout llEncargo = (LinearLayout) findViewById(R.id.llEncargos);
        llEncargo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrincipalActivity.this, EncargoActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout llTransporte = (LinearLayout) findViewById(R.id.llTransporte);
        llTransporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrincipalActivity.this, TransporteActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout llHoteleria = (LinearLayout) findViewById(R.id.llHoteleria);
        llHoteleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrincipalActivity.this, HoteleriaActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout llPidelo = (LinearLayout) findViewById(R.id.llPidelo);
        llPidelo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrincipalActivity.this, PideloActivity.class);
                startActivity(intent);
            }
        });

        ImageButton btnHome = (ImageButton) findViewById(R.id.btnHome);
        ImageButton btnFavoritos = (ImageButton) findViewById(R.id.btnFavoritos);
        btnCarrito = (ImageButton) findViewById(R.id.btnCarrito);
        ImageButton btnUsuario = (ImageButton) findViewById(R.id.btnUsuario);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnFavoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrincipalActivity.this, FavoritosActivity.class);
                startActivity(intent);
            }
        });

        btnCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrincipalActivity.this, CarritoActivity.class);
                startActivity(intent);
            }
        });

        btnUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrincipalActivity.this, UserActivity.class);
                startActivity(intent);
            }
        });
    }

    public int obtenerPosicionItem(Spinner spinner, String texto) {
        int posicion = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(texto)) {
                posicion = i;
            }
        }
        return posicion;
    }

    public int obtenerPosicionCiudadSeleccionada(String texto) {
        int posicion = 0;
        for (int i = 0; i < Globales.listCiudades.size(); i++) {
            if (texto.equals(Globales.listCiudades.get(i).getNombre())){
                posicion = Globales.listCiudades.get(i).getCodigo();

                Globales.latitudCiudadMapa = Globales.listCiudades.get(i).getLat();
                Globales.longitudCiudadMapa = Globales.listCiudades.get(i).getLon();
                Globales.radioCiudadMapa = Globales.listCiudades.get(i).getRadio();
                Globales.precioBaseCiudadMapa = Globales.listCiudades.get(i).getPreciobase();
                Globales.precioExtraCiudadMapa = Globales.listCiudades.get(i).getPrecioextra();
            }
        }
        //Devuelve un valor entero (si encontro una coincidencia devuelve la
        // posición 0 o N, de lo contrario devuelve 0 = posición inicial)
        return posicion;
    }

    public void listarPromociones(int codUbi){
        String consulta = "https://apifbdelivery.fastbuych.com/Delivery/PromocionesXUbicacion?auth="+Globales.tokencito+"&ubica="+String.valueOf(codUbi);
        EnviarRecibirDatosPromociones(consulta);
    }

    public void EnviarRecibirDatosCiudades(String URL){
        RequestQueue queue = Volley.newRequestQueue(PrincipalActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.length()>0){
                    try {
                        Globales.listCiudades.clear();
                        JSONArray ja = new JSONArray(response);
                        ArrayList<String> nameCiudades = new ArrayList<>();
                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject objeto = ja.getJSONObject(i);
                            Ubicacion ubicacion = new Ubicacion();
                            ubicacion.setCodigo(objeto.getInt("UBI_Codigo"));
                            ubicacion.setNombre(objeto.getString("UBI_Nombre"));
                            ubicacion.setEstado(objeto.getInt("UBI_Estado"));
                            Globales.listCiudades.add(ubicacion);

                            nameCiudades.add(objeto.getString("UBI_Nombre").toString());
                        }
                        try {
                            ArrayAdapter<String> adap = new ArrayAdapter<String>(PrincipalActivity.this,R.layout.spinner_vera, nameCiudades);
                            //ArrayAdapter<String> adap = new ArrayAdapter<String>(PrincipalActivity.this,R.layout.support_simple_spinner_dropdown_item, nameCiudades);
                            ciudades.setAdapter(adap);
                            //mostrar la ciudad seleccionada en el Mapa
                            ciudades.setSelection(obtenerPosicionItem(ciudades,Globales.ciudadOrigen.toString()));
                            progDailog.dismiss();
                        } catch (Exception ex) {
                            progDailog.dismiss();
                        }
                    } catch (JSONException e) {
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

    public void listaCiudades(){
        ArrayList<String> nameCiudades = new ArrayList<>();
        for (int i=0; i<Globales.listCiudades.size();i++){
            nameCiudades.add(Globales.listCiudades.get(i).getNombre());
        }
        ArrayAdapter<String> adap = new ArrayAdapter<String>(PrincipalActivity.this,R.layout.spinner_vera, nameCiudades);
        adap.setDropDownViewResource(R.layout.spinner_vera);
        ciudades.setAdapter(adap);
        //mostrar la ciudad seleccionada en el Mapa
        ciudades.setSelection(obtenerPosicionItem(ciudades,Globales.ciudadOrigen.toString()));
        progDailog.dismiss();
    }

    public void EnviarRecibirDatosPromociones(String URL){
        progDailog = new ProgressDialog(PrincipalActivity.this);
        progDailog.setMessage("Cargando Promociones...");
        progDailog.setIndeterminate(true);
        progDailog.setCancelable(false);
        progDailog.show();

        RequestQueue queue = Volley.newRequestQueue(PrincipalActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.length()>0){
                    txtPromociones.setVisibility(View.VISIBLE);
                    rvPromociones.setVisibility(View.VISIBLE);
                    try {
                        JSONArray ja = new JSONArray(response);
                        list = new ArrayList<>();
                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject objeto = ja.getJSONObject(i);
                            Promocion promocion = new Promocion();
                            promocion.setCodigo(objeto.getInt("PRM_Codigo"));
                            promocion.setDescripcion(objeto.getString("PRM_Descripcion"));
                            promocion.setPrecio(objeto.getString("PRM_Precio"));
                            promocion.setImagen(objeto.getString("PRM_Imagen"));
                            promocion.setEstado(objeto.getInt("PRM_Estado"));
                            Categoria categoria = new Categoria();
                            categoria.setDescripcion(objeto.getString("CAT_Codigo"));
                            promocion.setCategoria(categoria);
                            Empresa empresa = new Empresa();
                            empresa.setCodigo(objeto.getInt("EMP_Codigo"));
                            empresa.setNombreComercial(objeto.getString("EMP_NombreComercial"));
                            empresa.setLongitud(objeto.getDouble("EU_Longitud"));
                            empresa.setLatitud(objeto.getDouble("EU_Latitud"));
                            promocion.setEmpresa(empresa);
                            Calcular_Minutos calcula = new Calcular_Minutos();
                            promocion.setTiempo(calcula.ObtenMinutos(objeto.getString("PRM_Preparacion")));
                            list.add(promocion);
                        }
                        try {
                            rvPromociones.setLayoutManager(new LinearLayoutManager(PrincipalActivity.this, LinearLayoutManager.HORIZONTAL, false));
                            rvPromociones.setAdapter(new RecyclerAdapterPromociones(PrincipalActivity.this, list));
                            final GestureDetector mGestureDetector = new GestureDetector(PrincipalActivity.this, new GestureDetector.SimpleOnGestureListener(){
                                @Override
                                public boolean onSingleTapUp(MotionEvent e){
                                    return true;
                                }
                            });
                            rvPromociones.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                                @Override
                                public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                                    try {
                                        View child = rvPromociones.findChildViewUnder(e.getX(),e.getY());
                                        if(child != null && mGestureDetector.onTouchEvent(e)){
                                            int position = rvPromociones.getChildAdapterPosition(child);
                                            Globales.PromocionPersonalizar = list.get(position);
                                            Intent intent = new Intent(PrincipalActivity.this,PersonalizaPromoActivity.class);
                                            startActivity(intent);
                                            return true;
                                        }
                                    }catch (Exception ex){
                                        ex.printStackTrace();
                                    }
                                    return false;
                                }

                                @Override
                                public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

                                }

                                @Override
                                public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                                }
                            });
                            progDailog.dismiss();
                        } catch (Exception ex) {

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progDailog.dismiss();
                    }
                }
                else {
                    progDailog.dismiss();
                    txtPromociones.setVisibility(View.INVISIBLE);
                    rvPromociones.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                progDailog.dismiss();
            }
        });

        queue.add(stringRequest);

    }

    @Override
    public void onBackPressed (){
        /*Intent intent = new Intent(PrincipalActivity.this,CiudadActivity.class);
        startActivity(intent);*/
        PrincipalActivity.this.finishAffinity();
        startActivity(new Intent(getBaseContext(), CiudadActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
        finish();
    }
}
