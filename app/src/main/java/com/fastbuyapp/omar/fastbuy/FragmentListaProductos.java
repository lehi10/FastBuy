package com.fastbuyapp.omar.fastbuy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fastbuyapp.omar.fastbuy.config.Servidor;
import com.fastbuyapp.omar.fastbuy.entidades.Categoria;
import com.fastbuyapp.omar.fastbuy.entidades.Empresa;
import com.fastbuyapp.omar.fastbuy.config.Globales;
import com.fastbuyapp.omar.fastbuy.entidades.Producto;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class FragmentListaProductos extends Fragment {
    GridView gridView;
    public String codigo;
    public String nombreComercial;
    public String categoria;
    ArrayList<Producto> list;
    ProductosListAdapter adapter = null;

    public FragmentListaProductos() {
        // Required empty public constructor
    }
    public void listarProductos(String empresa, String filtro, String categoria, View v) throws UnsupportedEncodingException {

        Servidor s = new Servidor();
        String c = URLEncoder.encode(filtro, "UTF-8");
        //Toast.makeText(v.getContext(), String.valueOf(Globales.ubicacion), Toast.LENGTH_SHORT).show();
        String consulta = "http://"+s.getServidor()+"/app/consultasapp/app_productos_xempresa_xubicacion_xcategoria_xfiltro.php?empresa="+empresa+"&ubicacion="+Globales.ubicacion+"&categoria="+categoria+"&filtro="+filtro;
        EnviarRecibirDatos(consulta, v);
    }
    /*private final DropDownView.DropDownListener dropDownListener = new DropDownView.DropDownListener() {
        @Override
        public void onExpandDropDown() {
            adapter.notifyDataSetChanged();
            //ObjectAnimator.ofFloat() (headerChevronIV, View.ROTATION.getName(), 180).start();
        }

        @Override
        public void onCollapseDropDown() {
            //ObjectAnimator.ofFloat(headerChevronIV, View.ROTATION.getName(), -180, 0).start();
        }
    };*/
    SpinnerDialog spinnerDialog;

    private Typeface fuente1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // create ContextThemeWrapper from the original Activity Context with the custom theme
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.AppTheme_NoActionBar);

        // clone the inflater using the ContextThemeWrapper
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
        final View view = inflater.inflate(R.layout.fragment_fragment_lista_productos, container, false);
        String fuente = "fonts/Riffic.ttf";
        this.fuente1 = Typeface.createFromAsset(getActivity().getAssets(), fuente);
        //((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        //((AppCompatActivity) getActivity()).requestWindowFeature(Window.FEATURE_NO_TITLE);
        ImageView imgPortada = (ImageView) view.findViewById(R.id.imageViewPortada);
        final ImageView imgLogo = (ImageView) view.findViewById(R.id.imgPerfil);
        Servidor s = new Servidor();
        String url = "http://"+s.getServidor()+"/empresas/subcategorias/imagenes/" + Globales.imagenSubcategoria;
        String url2 = "http://"+s.getServidor()+"/empresas/logos/" + Globales.imagenEmpresa;

        Picasso.with(getContext())
                .load(url)
                .error(R.mipmap.ic_launcher)
                .fit()
                .centerInside()
                .into(imgPortada);

        Picasso.with(getContext())
                .load(url2)
                .error(R.mipmap.ic_launcher)
                .fit()
                .centerInside()
                .into(imgLogo, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap imageBitmap = ((BitmapDrawable) imgLogo.getDrawable()).getBitmap();
                        RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
                        imageDrawable.setCircular(true);
                        imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                        imgLogo.setImageDrawable(imageDrawable);
                    }
                    @Override
                    public void onError() {
                        imgLogo.setImageResource(R.mipmap.ic_launcher);
                    }
                });


        //extraemos el drawable en un bitmap

        codigo = String.valueOf(Globales.empresaSeleccionada);
        //nombreComercial = getIntent().getStringExtra("nombreComercial");
        categoria = String.valueOf(Globales.catProductoSeleccionado);
        try {
            listarProductos(codigo,"",categoria, view);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //EditText txtDescripcion = (EditText) view.findViewById(R.id.txtFiltroProductos);
        final String[] descrip = {""};
        /*txtDescripcion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    descrip[0] = URLEncoder.encode(s.toString().toUpperCase(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {
                    listarProductos(codigo,descrip[0],categoria, view);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/



        return view;
    }

    /*@IntDef(flag = true, value = {TYPE_VERTICLE, TYPE_HORIZONTAL, TYPE_SLIDE})
    public @interface AnimType {
    }*/

    public void EnviarRecibirDatos(String URL, final View view){

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.length()>0){
                    try {
                        JSONArray ja = new JSONArray(response);
                        CargarLista(ja, view);
                        //Toast.makeText(getApplicationContext(),"a: ", Toast.LENGTH_LONG);
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

    public void CargarLista(JSONArray ja, View view) throws JSONException {
        list = new ArrayList<>();
        for(int i = 0; i < ja.length(); i++) {
            JSONObject objeto = ja.getJSONObject(i);
            Producto producto = new Producto();
            producto.setCodigo(objeto.getInt("codigo"));
            producto.setDescripcion(objeto.getString("descripcion"));
            producto.setPrecio(objeto.getString("precio"));
            producto.setImagen(objeto.getString("imagen"));
            producto.setEstado(objeto.getInt("estado"));

            JSONObject objeto2 = objeto.getJSONObject("categoria");
            Categoria categoria = new Categoria();
            categoria.setDescripcion(objeto2.getString("descripcion"));
            producto.setCategoria(categoria);

            JSONObject objeto3 = objeto.getJSONObject("empresa");
            Empresa empresa = new Empresa();
            empresa.setCodigo(objeto3.getInt("codigo"));
            empresa.setNombreComercial(objeto3.getString("nombreComercial"));
            empresa.setLongitud(objeto3.getDouble("longitud"));
            empresa.setLatitud(objeto3.getDouble("latitud"));
            producto.setEmpresa(empresa);
            list.add(producto);
        }
        try {
            gridView = (GridView) view.findViewById(R.id.tablaListaProductos);
            adapter = new ProductosListAdapter(getActivity(), R.layout.list_productos_item, list);
            gridView.setAdapter(adapter);
        }
        catch (Exception ex){

        }
    }

}
