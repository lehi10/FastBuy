package com.fastbuyapp.omar.fastbuy;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fastbuyapp.omar.fastbuy.Validaciones.ValidacionDatos;
import com.fastbuyapp.omar.fastbuy.config.Servidor;
import com.fastbuyapp.omar.fastbuy.config.Globales;
import com.fastbuyapp.omar.fastbuy.entidades.Operaciones;
import com.fastbuyapp.omar.fastbuy.entidades.PedidoDetalle;
import com.fastbuyapp.omar.fastbuy.entidades.Ubicacion;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import cn.refactor.lib.colordialog.PromptDialog;
import lib.visanet.com.pe.visanetlib.VisaNet;

public class Principal2Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ArrayList<Ubicacion> list;
    ArrayAdapter<CharSequence> adapter;
    FragmentDesconectado panelDesconectado;// = new FragmentDesconectado();
    FragmentCategoriaEmpresas panelCategoriaEmpresas;// = new FragmentCategoriaEmpresas();
    FragmentCarrito panelCarritoCompras;// = new FragmentCarrito();
    FragmentPromociones panelPromociones;// = new FragmentPromociones();
    FragmentSoporte panelSoporte;// = new FragmentSoporte();
    FragmentManager manager;
    Spinner cboCiudad;
    ImageButton btnCategoriasEmpresas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Operaciones operaciones = new Operaciones();
        int cant = operaciones.cargarDatosPersonales(getBaseContext());
        Principal2Activity.AsyncTask_load ast = new Principal2Activity.AsyncTask_load();
        ast.execute();
        if(cant == 0) {
            Intent intent = new Intent(Principal2Activity.this, LoginActivity.class);
            startActivity(intent);
            //Toast.makeText(getBaseContext(), "DATOS: " + String.valueOf(cant), Toast.LENGTH_SHORT).show();
            //this.finish();
        }

        btnCategoriasEmpresas = (ImageButton) findViewById(R.id.btnCategoriaEmpresas);
        final ImageButton btnCarritoCompras = (ImageButton) findViewById(R.id.btnCarritoCompras);
        final ImageButton btnPromociones = (ImageButton) findViewById(R.id.btnPromociones);
        final ImageButton btnSoporte = (ImageButton) findViewById(R.id.btnSoporte);


        btnCategoriasEmpresas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("ubicacion2: ", String.valueOf(Globales.ubicacion));
                TextView txtBarra = (TextView) findViewById(R.id.txtBarra);
                txtBarra.setText("FASTBUY");
                cboCiudad.setVisibility(View.VISIBLE);
                if(Globales.ubicacion != -1)
                    cboCiudad.setSelection(Globales.ubicacion - 1);
                ValidacionDatos validacionDatos = new ValidacionDatos();
                if(validacionDatos.hayConexiónRed(getBaseContext())) {

                    manager.beginTransaction()
                            .remove(panelCategoriaEmpresas)
                            .replace(R.id.contenedorPrincipal, panelCategoriaEmpresas, "panelCategorias")
                            .addToBackStack(null)
                            .commit();
                }
                else {
                    txtBarra = (TextView) findViewById(R.id.txtBarra);
                    txtBarra.setText("Desconectado");
                    manager.beginTransaction()
                            .replace(R.id.contenedorPrincipal, panelDesconectado, panelDesconectado.getTag())
                            .commit();
                }
            }
        });
        btnCategoriasEmpresas.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnCategoriasEmpresas.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.fastbuy));
                    Bitmap bmpCategoria = BitmapFactory.decodeResource(getResources(), R.drawable.ic_tienda2);
                    btnCategoriasEmpresas.setImageBitmap(bmpCategoria);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnCategoriasEmpresas.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.blanco));
                    Bitmap bmpCategoria = BitmapFactory.decodeResource(getResources(), R.drawable.ic_tienda1);
                    btnCategoriasEmpresas.setImageBitmap(bmpCategoria);
                }
                return false;
            }
        });

        btnCarritoCompras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txtBarra = (TextView) findViewById(R.id.txtBarra);
                txtBarra.setText("FASTBUY");
                cboCiudad.setVisibility(View.INVISIBLE);
                ValidacionDatos validacionDatos = new ValidacionDatos();
                if(validacionDatos.hayConexiónRed(getBaseContext())) {

                    manager.beginTransaction()
                            .replace(R.id.contenedorPrincipal, panelCarritoCompras, panelCarritoCompras.getTag())
                            .addToBackStack(null)
                            .commit();
                }
                else {
                    txtBarra = (TextView) findViewById(R.id.txtBarra);
                    txtBarra.setText("Desconectado");
                    manager.beginTransaction()
                            .replace(R.id.contenedorPrincipal, panelDesconectado, panelDesconectado.getTag())
                            .commit();
                }
            }
        });

        btnCarritoCompras.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Globales g = Globales.getInstance();
                if(g.getListaPedidos().size() == 0){
                    if(event.getAction() == MotionEvent.ACTION_DOWN) {
                        btnCarritoCompras.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.fastbuy));
                        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_carrito2);
                        btnCarritoCompras.setImageBitmap(bmp);
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        btnCarritoCompras.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.blanco));
                        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_carrito1);
                        btnCarritoCompras.setImageBitmap(bmp);
                    }
                }
                else{
                    if(event.getAction() == MotionEvent.ACTION_DOWN) {
                        btnCarritoCompras.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.fastbuy));
                        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_carrito_notif2);
                        btnCarritoCompras.setImageBitmap(bmp);
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        btnCarritoCompras.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.blanco));
                        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_carrito_notif);
                        btnCarritoCompras.setImageBitmap(bmp);
                    }
                }
                return false;
            }
        });

        btnPromociones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txtBarra = (TextView) findViewById(R.id.txtBarra);
                txtBarra.setText("PROMOCIONES");
                cboCiudad.setVisibility(View.INVISIBLE);
                ValidacionDatos validacionDatos = new ValidacionDatos();
                if(validacionDatos.hayConexiónRed(getBaseContext())) {
                    manager.beginTransaction()
                            .replace(R.id.contenedorPrincipal, panelPromociones, panelPromociones.getTag())
                            .addToBackStack(null)
                            .commit();
                }
                else {
                    txtBarra = (TextView) findViewById(R.id.txtBarra);
                    txtBarra.setText("Desconectado");
                    manager.beginTransaction()
                            .replace(R.id.contenedorPrincipal, panelDesconectado, panelDesconectado.getTag())
                            .commit();
                }
            }
        });

        btnPromociones.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnPromociones.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.fastbuy));
                    Bitmap bmpPromociones  = BitmapFactory.decodeResource(getResources(), R.drawable.ic_notificacion2);
                    btnPromociones.setImageBitmap(bmpPromociones);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnPromociones.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.blanco));
                    Bitmap bmpPromociones  = BitmapFactory.decodeResource(getResources(), R.drawable.ic_notificacion1);
                    btnPromociones.setImageBitmap(bmpPromociones);
                }
                return false;
            }
        });

        btnSoporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txtBarra = (TextView) findViewById(R.id.txtBarra);
                txtBarra.setText("¡Pide lo que quieras!");
                cboCiudad.setVisibility(View.INVISIBLE);
                ValidacionDatos validacionDatos = new ValidacionDatos();
                if(validacionDatos.hayConexiónRed(getBaseContext())) {
                    manager.beginTransaction()
                            .replace(R.id.contenedorPrincipal, panelSoporte, panelSoporte.getTag())
                            .addToBackStack(null)
                            .commit();
                }
                else {
                    txtBarra = (TextView) findViewById(R.id.txtBarra);
                    txtBarra.setText("Desconectado");

                    manager.beginTransaction()
                            .replace(R.id.contenedorPrincipal, panelDesconectado, panelDesconectado.getTag())
                            .commit();
                }
            }
        });
        btnSoporte.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnSoporte.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.fastbuy));
                    Bitmap bmpSoporte = BitmapFactory.decodeResource(getResources(), R.drawable.ic_otros2);
                    btnSoporte.setImageBitmap(bmpSoporte);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnSoporte.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.blanco));
                    Bitmap bmpSoporte = BitmapFactory.decodeResource(getResources(), R.drawable.ic_otros1);
                    btnSoporte.setImageBitmap(bmpSoporte);
                }
                return false;
            }
        });

    }

    ProgressDialog progDailog = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        /*Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentResumen);
        fragment.onActivityResult(requestCode, resultCode, data);*/
        Log.v("RESULT CODE", String.valueOf(resultCode));
        Log.v("DATA", String.valueOf(data));
        if (requestCode == VisaNet.VISANET_AUTHORIZATION) {
            if (data != null) {
                if (resultCode == RESULT_OK) {
                    String JSONString = data.getExtras().getString("keySuccess");
                    //Toast toast1 = Toast.makeText(getApplicationContext(), JSONString, Toast.LENGTH_LONG);
                    //toast1.show();
                    Globales.mensaje = JSONString;
                    try {
                        progDailog = new ProgressDialog(Principal2Activity.this);
                        progDailog.setMessage("Generando pedido...");
                        progDailog.setIndeterminate(true);
                        progDailog.setCancelable(false);
                        progDailog.show();
                        registrarPedidoTarjeta(Globales.nombreCliente, Globales.direccion2 + ", " + Globales.ciudadOrigen, Globales.numeroTelefono, String.valueOf(Globales.montoDelivery), String.valueOf(Globales.montoCargo), Globales.formaPago, "00:30:00", Globales.getInstance().getListaPedidos(), progDailog);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    //Intent intent = new Intent(this, MensajeActivity.class);
                    //startActivity(intent);

                } else {
                    String JSONString = data.getExtras().getString("keyError");
                    JSONString = JSONString != null ? JSONString : "";
                    //Toast toast1 = Toast.makeText(getApplicationContext(), JSONString, Toast.LENGTH_LONG);
                    //toast1.show();
                    Globales.mensaje = JSONString;
                    new PromptDialog(this)
                            .setDialogType(PromptDialog.DIALOG_TYPE_WRONG)
                            .setAnimationEnable(true)
                            .setTitleText("ERROR")
                            .setContentText("Por favor inténtalo nuevamente o con otra tarjeta.")
                            .setPositiveListener("OK", new PromptDialog.OnPositiveListener() {
                                @Override
                                public void onClick(PromptDialog dialog) {
                                    dialog.dismiss();
                                }
                            }).show();
                    //Intent intent = new Intent(this, MensajeActivity.class);
                    //startActivity(intent);
                }
            }
            else {
                Toast toast1 = Toast.makeText(getApplicationContext(), "Cancel...", Toast.LENGTH_LONG);
                toast1.show();
            }
        }
    }

    public void registrarPedidoTarjeta(String nombre, String direccion, String telefono, String delivery, String cargo, String forma, String tiempo, ArrayList<PedidoDetalle> lista, final ProgressDialog pd) throws UnsupportedEncodingException {
        Servidor s = new Servidor();
        String a = URLEncoder.encode(nombre, "UTF-8");
        String b = URLEncoder.encode(direccion, "UTF-8");
        String c = URLEncoder.encode(telefono, "UTF-8");
        String d = URLEncoder.encode(delivery, "UTF-8");
        String e = URLEncoder.encode(cargo, "UTF-8");
        String f = URLEncoder.encode(forma, "UTF-8");
        String g = URLEncoder.encode(tiempo, "UTF-8");
        String h = URLEncoder.encode(Globales.ciudadOrigen, "UTF-8");
        String i = URLEncoder.encode(String.valueOf(Globales.latitudOrigen), "UTF-8");
        String j = URLEncoder.encode(String.valueOf(Globales.longitudOrigen), "UTF-8");
        String consulta = "http://"+s.getServidor()+"/app/consultasapp/app_pedido_guardar.php?nombre="+a+"&direccion="+b+"&telefono=" + c +"&delivery=" + d +"&cargo=" + e +"&forma=" + f +"&tiempo=" + g+"&origen=" + h + "&latitud=" + i + "&longitud=" + j ;
        RegistrarPedidoTarjetaBD(consulta,lista, pd);
    }
    int codigoRegistro = 0;
    public ArrayList<Double> listaDistancias = new ArrayList<Double>();
    private final int DURACION_SPLASH = 500;
    public void RegistrarPedidoTarjetaBD(String URL, final ArrayList<PedidoDetalle> lista, final ProgressDialog pd){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.length()>0){
                    try {

                        //TextView txtCodigoPedido = (TextView) findViewById(R.id.txtCodigoPedido);
                        JSONObject jo = new JSONObject(response);
                        Log.v("pedido", jo.getString("codigo"));
                        codigoRegistro = jo.getInt("codigo");
                        int i = 0;
                        for (PedidoDetalle pd: lista) {
                            i++;
                            String item = String.valueOf(i);
                            int promo = 0;
                            String codigoProducto = "0";
                            String cantidad = String.valueOf(pd.getCantidad());
                            String precio = String.valueOf(pd.getPreciounit());
                            String total = String.valueOf(pd.getTotal());
                            String ubicacion = String.valueOf(pd.getUbicacion());
                            String personalizado = URLEncoder.encode(pd.getPersonalizacion(), "UTF-8");
                            if(pd.isEsPromocion()){
                                promo = 1;
                                codigoProducto = String.valueOf(pd.getPromocion().getCodigo());
                                Log.v("promo", codigoProducto);
                            }
                            else{
                                promo = 0;
                                codigoProducto = String.valueOf(pd.getProducto().getCodigo());
                                Log.v("normal", codigoProducto);
                            }
                            Servidor s = new Servidor();
                            Log.v("codigo", codigoProducto);
                            Log.v("cantidad", cantidad);
                            Log.v("ubicacion", ubicacion);
                            Log.v("personalizado", personalizado);
                            Log.v("espromo", String.valueOf(pd.isEsPromocion()));
                            String consulta = "http://"+s.getServidor()+"/app/consultasapp/app_detallepedido_guardar.php?pedido=" + String.valueOf(codigoRegistro) +"&item="+item+"&producto="+codigoProducto+"&cantidad="+cantidad+"&precio="+precio+"&total=" + total+"&ubicacion=" + ubicacion +"&personalizado=" + personalizado + "&promo=" + String.valueOf(promo);
                            //txtCodigoPedido.setText(consulta);
                            RegistrarDetallePedido(consulta);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    pd.dismiss();
                    new PromptDialog(Principal2Activity.this)
                            .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                            .setAnimationEnable(true)
                            .setTitleText("PEDIDO ENVIADO")
                            .setContentText("En breve recibirá una llamada para su confirmación.")
                            .setPositiveListener("OK", new PromptDialog.OnPositiveListener() {
                                @Override
                                public void onClick(PromptDialog dialog) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(Principal2Activity.this, Principal2Activity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            }).show();
                    lista.clear();
                    listaDistancias.clear();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);
    }

    public void RegistrarDetallePedido(String URL) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);
    }
    @Override
    public void onBackPressed() {
        try {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else if (getFragmentManager().getBackStackEntryCount() > 0) {
                getFragmentManager().popBackStack();
            }
            else if (getSupportFragmentManager().findFragmentByTag("panelCategorias") instanceof FragmentCategoriaEmpresas && getSupportFragmentManager().findFragmentByTag("panelCategorias").isVisible()){
                finish();
            }
            /*else if((getSupportFragmentManager().findFragmentByTag("panelCategorias") instanceof FragmentCategoriaEmpresas)) {
                finish();
            }*/
            else
                super.onBackPressed();
        }
        catch (Exception ex){

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        /*if (id == R.id.misdireccion) {
            TextView txtBarra = (TextView) findViewById(R.id.txtBarra);
            txtBarra.setText("Desconectado");
            FragmentDirecciones panelDirecciones = new FragmentDirecciones();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.contenedorPrincipal, panelDirecciones, panelDirecciones.getTag())
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_share) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class AsyncTask_load extends AsyncTask<Void, Integer, Boolean> {

        ProgressDialog progDailog = new ProgressDialog(Principal2Activity.this);;
        @Override
        protected void onPreExecute() {
            panelDesconectado = new FragmentDesconectado();
            panelCategoriaEmpresas = new FragmentCategoriaEmpresas();
            panelCarritoCompras = new FragmentCarrito();
            panelPromociones = new FragmentPromociones();
            panelSoporte = new FragmentSoporte();
            cboCiudad = (Spinner) findViewById(R.id.cboCiudad1);
            manager = getSupportFragmentManager();
            ValidacionDatos validacion = new ValidacionDatos();
            if(validacion.hayConexiónRed(getBaseContext())) {
                final Servidor servidor = new Servidor();
                String consulta1 = "http://" + servidor.getServidor() + "/app/consultasapp/app_cliente_xtelefono.php?telefono=" + Globales.numeroTelefono;
                RequestQueue queue1 = Volley.newRequestQueue(Principal2Activity.this);
                StringRequest stringRequest1 = new StringRequest(Request.Method.GET, consulta1, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.length() > 0) {
                            try {
                                JSONObject cd = new JSONObject(response);
                                Globales.latitudOrigen = cd.getDouble("latitud");
                                Globales.longitudOrigen = cd.getDouble("longitud");
                                Globales.direccion = cd.getString("direccion");
                                Globales.ciudadOrigen = cd.getString("ciudad");

                                JSONObject cl = cd.getJSONObject("cliente");
                                TextView txt1 = (TextView) findViewById(R.id.txtCliente);
                                txt1.setText(cl.getString("nombre"));
                                TextView txt2 = (TextView) findViewById(R.id.txtTelefono);
                                txt2.setText(cl.getString("telefono"));
                                Log.v("nombre: ", cl.getString("nombre"));
                                Log.v("telefono: ", cl.getString("telefono"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            String consulta2 = "http://" + servidor.getServidor() + "/app/consultasapp/app_ubicacion_listar.php";
                            RequestQueue queue2 = Volley.newRequestQueue(Principal2Activity.this);
                            StringRequest stringRequest2 = new StringRequest(Request.Method.GET, consulta2, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (response.length() > 0) {
                                        try {
                                            JSONArray ja = new JSONArray(response);

                                            list = new ArrayList<>();
                                            for (int i = 0; i < ja.length(); i++) {
                                                JSONObject objeto = ja.getJSONObject(i);
                                                Ubicacion ubicacion = new Ubicacion();
                                                ubicacion.setCodigo(objeto.getInt("codigo"));
                                                ubicacion.setNombre(objeto.getString("nombre"));
                                                ubicacion.setEstado(objeto.getInt("estado"));
                                                list.add(ubicacion);
                                            }
                                            for (int i = 0; i < list.size(); i++) {
                                                Log.v("ciudad10: ", "'" + list.get(i).getNombre() + "'");
                                                Log.v("ciudadorigen: ", "'" + Globales.ciudadOrigen + "'");
                                                if ((list.get(i).getNombre().toString()).equals(Globales.ciudadOrigen)) {
                                                    Globales.ubicacion = list.get(i).getCodigo();
                                                }
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        adapter = ArrayAdapter.createFromResource(Principal2Activity.this, R.array.ciudades, R.layout.support_simple_spinner_dropdown_item);
                                        cboCiudad.setAdapter(adapter);
                                        Log.v("ciudad2: ", Globales.ciudadOrigen);
                                        //int spinnerPosition = adapter.getPosition(Globales.ciudadOrigen);
                                        cboCiudad.setSelection(Globales.ubicacion - 1);
                                        cboCiudad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                //FragmentManager manager1 = getSupportFragmentManager();
                                                Log.v("ubicacion3: ", String.valueOf(Globales.ubicacion));
                                                Globales.ubicacion = cboCiudad.getSelectedItemPosition()+ 1;
                                                btnCategoriasEmpresas.performClick();
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> parent) {

                                            }
                                        });
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            });
                            queue2.add(stringRequest2);

                            //
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                queue1.add(stringRequest1);



                progDailog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        Principal2Activity.AsyncTask_load.this.cancel(true);
                    }
                });
                progDailog.setMessage("Cargando...");
                progDailog.setIndeterminate(true);
                progDailog.setCancelable(false);
                progDailog.show();
            }
            else{
                TextView txtBarra = (TextView) findViewById(R.id.txtBarra);
                txtBarra.setText("Desconectado");
                FragmentDesconectado panelDesconectado = new FragmentDesconectado();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.contenedorPrincipal, panelDesconectado, panelDesconectado.getTag())
                        .commit();
            }

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO Auto-generated method stub
            ValidacionDatos validacion = new ValidacionDatos();
            while (Globales.ubicacion == 0 || !validacion.hayConexiónRed(getBaseContext())) {

            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean bool) {
            progDailog.dismiss();


            isCancelled();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}
