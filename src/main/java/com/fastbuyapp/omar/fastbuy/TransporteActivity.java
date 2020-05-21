package com.fastbuyapp.omar.fastbuy;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fastbuyapp.omar.fastbuy.config.Globales;

import org.json.JSONException;
import org.json.JSONObject;


public class TransporteActivity extends AppCompatActivity  {

    boolean             state = true;
    LottieAnimationView btnBoxTransporte; // Button to open a hidden Layout
    LinearLayout        animacionTransporte, generarTransporte;

    TextView            text_transporte_header;
    TextView            textView_Ubicacion;
    TextView            textView_Destino;
    TextView            textView_tarifa;
    TextView            textView_metodoPago;
    TextView            textView_comments;

    Button              confirmarTransporte;

    DialogFragmentMap           myDialogFragmentMap;
    DialogFragmentTarifa        myDialogFragmentTarifa;
    DialogFragmentComentarios   myDialogFragmentComentarios;
    DialogFragmentMetodoPago    myDialogFragmentMetodoPago;

    // -- Origin --
    public double lat_origin , long_origin;
    // -- Dest --
    public double lat_dest , long_dest;
    // Metodo de pago
    public int metodo_pago_id;
    public String metodo_pago_name;

    public String userId;

    public String idSpecificationSelected;
    public String textSpecificationSelected;
    public double tariffAmount;
    public String textTariffAmount;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transporte);


        // Initialization of coordinates
        lat_origin  =0.0;
        long_origin =0.0;

        lat_dest    =0.0;
        long_dest   =0.0;

        // Get user Id from server from telephone number

        getIdUserfromServer(Globales.numeroTelefono,Globales.tokencito);

        // Initialization of Layout Interface objects
        btnBoxTransporte        = (LottieAnimationView) findViewById(R.id.btnCajaAnimationTransporte);
        animacionTransporte     = (LinearLayout)        findViewById(R.id.linerAnimacionTransporte);
        generarTransporte       = (LinearLayout)        findViewById(R.id.linearGenerarTransporte);
        text_transporte_header  = (TextView)            findViewById(R.id.text_transporte_header);

        textView_Ubicacion      = (TextView)            findViewById(R.id.txtUbicacion);
        textView_Destino        = (TextView)            findViewById(R.id.txtDestino);
        textView_tarifa         = (TextView)            findViewById(R.id.txtTarifa);
        textView_metodoPago     = (TextView)            findViewById(R.id.txtMetodo_Pago);
        textView_comments       = (TextView)            findViewById(R.id.txtComentarios_y_deseos);

        confirmarTransporte     = (Button)              findViewById(R.id.button_confirmar_transporte);
        

        // Primer Layout y Header Text  Inicializado en VISIBLE
        animacionTransporte.setVisibility(View.VISIBLE);
        text_transporte_header.setVisibility(View.VISIBLE);

        // Segundo Layout Inicializado en GONE  (desabiliado)
        generarTransporte.setVisibility(View.GONE);

        //Start boton flotante
        final LinearLayout flotante             = (LinearLayout) findViewById(R.id.linearFlotanteTransporte);
        final LinearLayout.LayoutParams param   = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                                                                LinearLayout.LayoutParams.WRAP_CONTENT);
        setToolbarConfig();                     // Display Toolbar Configurations
        push_float_button(flotante , param);    // Display Float Button Action OnClick

        // Listeners for events when an option is clicked
        on_Click_Ubicacion();
        on_Click_Destino();
        on_Click_Tarifa();
        on_Click_Metodo_Pago();
        on_Click_Comments();
        
        on_Click_confirmar_transporte();

    }

    private AlertDialog AskOption()
    {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                // set message, title, and icon
                .setTitle("Confirmación")
                .setMessage("Confirma el perdido del Taxi")

                .setPositiveButton("Realizar el Pago", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        Globales.montoDescuento = 0;
                        Globales.montoDelivery = 0;

                        Globales.montoCargo = 0;
                        Globales.montoCompra =tariffAmount;
                        Globales.montoTotal = 0;


                        Globales.tipoPago = metodo_pago_id;
                        Globales.formaPago = metodo_pago_name;
                        Globales.pagarcon = "0";

                        Intent intent = new Intent(TransporteActivity.this, PagoTransporteActivity.class);
                        intent.putExtra("userId", String.valueOf(userId) );
                        intent.putExtra("originLat", String.valueOf(lat_origin) );
                        intent.putExtra("originLong",String.valueOf(long_origin) );
                        intent.putExtra("destLat",   String.valueOf(lat_dest ));
                        intent.putExtra("destLong",  String.valueOf(long_dest));

                        intent.putExtra("originAddress",  String.valueOf(textView_Ubicacion.getText()));
                        intent.putExtra("destinationAddress",  String.valueOf(textView_Destino.getText()));


                        startActivity(intent);
                        dialog.dismiss();
                    }

                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();

        return myQuittingDialogBox;
    }



    private void on_Click_confirmar_transporte() {
        confirmarTransporte.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( !textView_Ubicacion.getText().equals("") &&
                    !textView_Destino.getText().equals("") &&
                    !textView_tarifa.getText().equals("") &&
                    !textView_metodoPago.getText().equals("") &&
                    !textView_comments.getText().equals(""))
                {
                    AlertDialog diaBox = AskOption();
                    diaBox.show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Ingrese todos los campos requeridos", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private void on_Click_Metodo_Pago() {
        textView_metodoPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myDialogFragmentMetodoPago= new DialogFragmentMetodoPago();
                myDialogFragmentMetodoPago.show(getSupportFragmentManager(),null);
            }
        });
    }

    /**
     *  Method for set new coordinates of origin location
     * @param latitude_map  new latitude  value
     * @param longitude_map new longitude value
     */

    void setOriginCoordinates(double latitude_map, double longitude_map){
        lat_origin= latitude_map;
        long_origin= longitude_map;
    }

    /**
     * Method for set new coordinates of Destination location
     * @param latitude_map  new latitude value
     * @param longitude_map new longitude value
     */

    void setDestCoordinates(double latitude_map, double longitude_map){
        lat_dest= latitude_map;
        long_dest= longitude_map;
    }

    /**
     *  Method for Click event on textView Comments
     */

    private void on_Click_Comments() {
        textView_comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myDialogFragmentComentarios= new DialogFragmentComentarios();
                myDialogFragmentComentarios.show(getSupportFragmentManager(),null);
            }
        });
    }

    /**
     * Method for Click event on textView Tarifa
     */

    private void on_Click_Tarifa() {
        textView_tarifa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialogFragmentTarifa = new DialogFragmentTarifa();
                myDialogFragmentTarifa.show(getSupportFragmentManager(),null);
            }
        });
    }

    /**
     * Method for Click event on textView Destination
     */

    private void on_Click_Destino() {
        // On Click in Destino  (Dest)
        textView_Destino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sending Dest Position Flag
                Bundle bundle = new Bundle();
                bundle.putString("orig_dest_flag","dest");
                myDialogFragmentMap = new DialogFragmentMap();
                myDialogFragmentMap.setArguments(bundle);
                myDialogFragmentMap.show(getSupportFragmentManager(),null);
            }
        });
    }

    /**
     * Method for Click event on textView Origin
     */

    private void on_Click_Ubicacion() {
        // On Click in Ubicación (Origin)
        textView_Ubicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sending Origin Position Flag
                Bundle bundle = new Bundle();
                bundle.putString("orig_dest_flag","origin");
                myDialogFragmentMap = new DialogFragmentMap();
                myDialogFragmentMap.setArguments(bundle);
                myDialogFragmentMap.show(getSupportFragmentManager(),null);
            }
        });
    }

    /**
     *  Method to allow Back Arrow
     * @return false
     */

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    /**
     * Method to Display Menu Options
     * @param menu
     * @return true
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_transporte, menu);
        return true;
    }

    /**
     * Toolbar Configurations
     */
    private void setToolbarConfig()
    {
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_navigate_before));
    }

    /**
     * Faster Pushing Float Button, layout 1 hides, and layout 2 is shown
     */
    private void push_float_button(final LinearLayout flotante , final LinearLayout.LayoutParams param){
        //inicializando variables
        final ScrollView myScrollGeneral    = (ScrollView)  findViewById(R.id.scrollGeneralTransportes);

        btnBoxTransporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ingresa al 2do Layout
                if (state){
                    param.setMargins(0,0,0,-50);
                    animacionTransporte.setVisibility(View.GONE);
                    generarTransporte.setVisibility(View.VISIBLE);
                    text_transporte_header.setVisibility(View.GONE);
                    myScrollGeneral.setBackgroundResource(R.drawable.background_map);
                    state = false;
                    flotante.setPadding(0,0,0,0);

                }
                // Retorna al 1er Layout
                else {
                    param.setMargins(0,0,0,0);
                    animacionTransporte.setVisibility(View.VISIBLE);
                    generarTransporte.setVisibility(View.GONE);
                    text_transporte_header.setVisibility(View.VISIBLE);
                    myScrollGeneral.setBackgroundResource(R.color.blanco);
                    state = true;
                    flotante.setPadding(0,0,0,30);
                }
                flotante.setLayoutParams(param);
            }
        });
    }

    public void getIdUserfromServer(String telfNumber, String token){

        String URL="https://apifbtransportes.fastbuych.com/Transporte/getUserIdByTelf?auth="+token+"&telfNumber="+telfNumber;

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.length() > 0) {
                    try {
                        JSONObject json_obj = new JSONObject(response).getJSONObject("response");
                        userId   = json_obj.getString("CLI_Codigo");
                        String userName = json_obj.getString("CLI_Nombre");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "Error Usuario id" + Globales.nombreCliente, Toast.LENGTH_LONG).show();
            }
        });

        queue.add(stringRequest);
    }


    public void setSpecificationsData(String idSpecification, String textStecification){
        idSpecificationSelected = idSpecification;
        textSpecificationSelected = textStecification;

    }

    public void setTariff(double tariff){
        tariffAmount = tariff;
        textTariffAmount = String.valueOf(tariff);

    }
}
