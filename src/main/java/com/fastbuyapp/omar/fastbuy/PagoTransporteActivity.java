package com.fastbuyapp.omar.fastbuy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fastbuyapp.omar.fastbuy.Operaciones.Calcular_Minutos;
import com.fastbuyapp.omar.fastbuy.config.Globales;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Map;

import cn.refactor.lib.colordialog.PromptDialog;
import lib.visanet.com.pe.visanetlib.VisaNet;
import lib.visanet.com.pe.visanetlib.presentation.custom.VisaNetViewAuthorizationCustom;

public class PagoTransporteActivity extends AppCompatActivity {

    public ImageView imageMetodoPago;
    public Button btnPedirTransporte;

    String userId;
    double subTotal;
    double chargeAmount;
    double totalAmount;
    int paymentMethodId;

    String paymentMethodName;

    String originLat;
    String originLong;
    String destLat;
    String destLong;

    String destinationAddress;
    String originAddress;

    String codigo_pedido_transp;

    int codigoVisa = 0;

    Calcular_Minutos calcula = new Calcular_Minutos();


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //esto sucedera luego de ejecutar el activity de visa
        super.onActivityResult(requestCode, resultCode, data);
        Log.v("RESULT CODE", String.valueOf(resultCode));
        Log.v("DATA", String.valueOf(data));
        if (requestCode == VisaNet.VISANET_AUTHORIZATION) {
            if (data != null) {

                if (resultCode == RESULT_OK) {

                    String JSONString = data.getExtras().getString("keySuccess");
                    Toast toast1 = Toast.makeText(getApplicationContext(), JSONString, Toast.LENGTH_LONG);
                    toast1.show();


                    /*
                    Globales.mensajeVisa = JSONString;



                        ProgressDialog progDailog = null;
                        progDailog = new ProgressDialog(PagoTransporteActivity.this);
                        progDailog.setMessage("Generando pedido...");
                        progDailog.setIndeterminate(true);
                        progDailog.setCancelable(true);
                        progDailog.show();

                        //registrarPedido(Globales.nombreCliente, Globales.direccion2 + ", " + Globales.ciudadOrigen, Globales.numeroTelefono, String.valueOf(Globales.montoDelivery), String.valueOf(Globales.montoCargo), Globales.formaPago, "00:30:00", Globales.getInstance().getListaPedidos());
                        Toast toast1 = Toast.makeText(PagoTransporteActivity.this, "Success :D ", Toast.LENGTH_LONG);
                        //actualizaCorrelativoVisa(codigoVisa);
                     */
                    /*Intent intent = new Intent(this, PagoExitosoVisaActivity.class);
                    intent.putExtra("estado", "true");
                    startActivity(intent);*/

                } else {

                    String JSONString = data.getExtras().getString("keyError");
                    JSONString = JSONString != null ? JSONString : "";
                    Toast toast1 = Toast.makeText(getApplicationContext(), JSONString, Toast.LENGTH_LONG);
                    toast1.show();


                    /*
                    String JSONString = data.getExtras().getString("keyError");
                    JSONString = JSONString != null ? JSONString : "";
                    Globales.mensajeVisa = JSONString;
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

                     */
                    //actualizaCorrelativoVisa(codigoVisa);
                    //Intent intent = new Intent(this, PagoExitosoVisaActivity.class);
                    //intent.putExtra("estado", "false");
                    //startActivity(intent);
                }
            } else {
                btnPedirTransporte.setEnabled(true);
                Toast toast1 = Toast.makeText(PagoTransporteActivity.this, "Transacción Detenida...", Toast.LENGTH_LONG);
                View vistaToast = toast1.getView();
                vistaToast.setBackgroundResource(R.drawable.toast_yellow);
                toast1.show();
            }
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pago_transporte);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        originLat = intent.getStringExtra("originLat");
        originLong = intent.getStringExtra("originLong");
        destLat = intent.getStringExtra("destLat");
        destLong = intent.getStringExtra("destLong");

        destinationAddress = intent.getStringExtra("destinationAddress");
        originAddress = intent.getStringExtra("originAddress");


        paymentMethodId = Globales.tipoPago;
        paymentMethodName = Globales.formaPago;
        subTotal=Globales.montoCompra;

        TextView textViewSubtotal = findViewById(R.id.txtSubTotalGeneral);
        TextView textViewCharge = findViewById(R.id.txtCargoGeneral);
        TextView textViewTotal = findViewById(R.id.txtTotalGeneral);
        btnPedirTransporte = findViewById(R.id.btnPedirTransporte);


        chargeAmount = calculateVisaCharge(subTotal,paymentMethodId);
        totalAmount = chargeAmount + subTotal;

        textViewSubtotal.setText(String.valueOf(subTotal));
        textViewCharge.setText(String.valueOf(chargeAmount));
        textViewTotal.setText(String.valueOf(totalAmount));

        adaptToPaymentMethod(Globales.tipoPago);

        btnPedirTransporteOnClick();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_chevron_left_black_24dp));
    }

    private void btnPedirTransporteOnClick() {
        btnPedirTransporte.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(paymentMethodId==2) {
                    ProgressDialog progDailog = null;
                    progDailog = new ProgressDialog(PagoTransporteActivity.this);
                    progDailog.setMessage("Cargando datos...");
                    progDailog.setIndeterminate(true);
                    progDailog.setCancelable(false);
                    progDailog.show();
                    String consultita = "https://apifbdelivery.fastbuych.com/Delivery/CorrelativoVisa?auth="+Globales.tokencito;
                    CorrelativoVisa(consultita, totalAmount, progDailog);

                }
                else
                {
                    registerInDB(userId,                        originAddress,
                                destinationAddress,             originLat,
                                originLong,                     destLat,
                                destLong,                       String.valueOf(subTotal),
                                String.valueOf(chargeAmount),   String.valueOf(totalAmount),
                                paymentMethodName,              String.valueOf(Globales.ubicacion),
                                Globales.tokencito);

                }
            }
        });
    }


    private void registerInDB(String client_id,             String origin_address,
                              String destionation_address,  String origin_lat,
                              String origin_long,           String dest_lat,
                              String dest_long,             String  sub_total,
                              String charge,                String total,
                              String payment_method_id,     String location_id,
                              String token){



        String URL="https://apifbtransportes.fastbuych.com/Transporte/saveNewTaxiReservation?auth="+token+      "&clientId="+client_id+
                                                                                "&origAdrs="+origin_address+    "&destAdrs="+destionation_address+
                                                                                "&origLat="+origin_lat+         "&origLong="+origin_long+
                                                                                "&destLat="+dest_lat+           "&destLong="+dest_long+
                                                                                "&subTotal="+sub_total+          "&charge="+charge+
                                                                                "&total="+total+                "&paymentMtd="+payment_method_id+
                                                                                "&locationId="+location_id;
        Log.i("URL",URL);

        final ProgressDialog dialogSaveInDB;
        dialogSaveInDB = new ProgressDialog(PagoTransporteActivity.this);
        dialogSaveInDB.setMessage("Iniciando Delivery");
        dialogSaveInDB.setIndeterminate(true);
        dialogSaveInDB.setCancelable(false);
        dialogSaveInDB.show();

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.length() > 0) {
                    try {
                        JSONObject objResponse = new JSONObject(response);
                        String codeResponse = objResponse.getString("codigo_pedido");
                        String codeSuccess = objResponse.getString("code");
                        Log.i("URL",objResponse.toString());

                        if("200".equals(codeSuccess))
                        {

                            dialogSaveInDB.dismiss();
                            codigo_pedido_transp = codeResponse;
                            Toast.makeText(getApplicationContext(),codigo_pedido_transp, Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(PagoTransporteActivity.this, ResumenTransporteActivity.class);

                            intent.putExtra("state", "PENDIENTE");
                            intent.putExtra("code_transp", codigo_pedido_transp);
                            intent.putExtra("payment_method",paymentMethodName);
                            intent.putExtra("total",String.valueOf(totalAmount));
                            intent.putExtra("charge",String.valueOf(chargeAmount));
                            intent.putExtra("subtotal",String.valueOf(subTotal));

                            startActivity(intent);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        dialogSaveInDB.dismiss();
                    }
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "Error al generar pago ", Toast.LENGTH_LONG).show();
                dialogSaveInDB.dismiss();
            }
        });
        queue.add(stringRequest);

    }






    private void adaptToPaymentMethod(int payment_method) {
        imageMetodoPago = findViewById(R.id.imagenMetodoPago);

        Bitmap bmp ;
        LinearLayout cargoField=findViewById(R.id.linearCargo);
        cargoField.setVisibility(View.GONE);

        switch (payment_method){
            case 1:
                 bmp = BitmapFactory.decodeResource(getResources(),R.drawable.ic_efectivo);
                imageMetodoPago.setImageBitmap(bmp);
                break;
            case 2:
                bmp = BitmapFactory.decodeResource(getResources(),R.drawable.ic_efectivo_tarjeta);
                imageMetodoPago.setImageBitmap(bmp);
                cargoField.setVisibility(View.VISIBLE);
                break;
            case 3:
                bmp = BitmapFactory.decodeResource(getResources(),R.drawable.yape);
                imageMetodoPago.setImageBitmap(bmp);
                break;
            case 4:
                bmp = BitmapFactory.decodeResource(getResources(),R.drawable.plin);
                imageMetodoPago.setImageBitmap(bmp);
                break;
        }
    }


    public double calculateVisaCharge(double amount,int paymentMethod){

        double charge = 0.0;
        if(paymentMethod == 2)
        {
            if (amount < 50){
                charge = 0.0;
            }
            else{
                int cant = (int) amount/50;
                charge = (double) cant*2.5;
            }
            Log.v("CARGO", String.valueOf(charge));
        }

        return charge;
    }


    public void CorrelativoVisa(final String URL, final double total, final ProgressDialog progressDialog){
        RequestQueue queue = Volley.newRequestQueue(PagoTransporteActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                if (response.length()>0){
                    try {
                        JSONObject jo = new JSONObject(response);
                        codigoVisa = jo.getInt("codigo");
                        Log.i("VISA_SALIENTE", String.valueOf(codigoVisa));


                        Map<String, Object> data = new HashMap<>();
                        data.put(VisaNet.VISANET_CHANNEL,VisaNet.Channel.MOBILE);
                        data.put(VisaNet.VISANET_COUNTABLE, true);
                        data.put(VisaNet.VISANET_USERNAME, "diegodelucio01@gmail.com");   // for release : diegodelucio01@gmail.com
                        data.put(VisaNet.VISANET_PASSWORD, "@Am$168Z");                    // for release : @Am$168Z
                        data.put(VisaNet.VISANET_MERCHANT, "101039934");//604410301-test-101039934  //forRelease : 604410301
                        Log.i("VISA", String.valueOf(codigoVisa));
                        data.put(VisaNet.VISANET_PURCHASE_NUMBER, (Object) String.valueOf(codigoVisa));//codigo de compra visa
                        //data.put(VisaNet.VISANET_AMOUNT, (Object) String.valueOf(Globales.montoCompra).toString().replace(",","."));
                        //data.put(VisaNet.VISANET_AMOUNT, (Object) TotalGeneral); // este es para enviar en modo produccion
                        data.put(VisaNet.VISANET_AMOUNT, (Object) "1.50");//a modo de prueba
                        data.put(VisaNet.VISANET_END_POINT_URL, "https://apiprod.vnforapps.com/");
                        //Personalización (Ingreso opcional)

                        VisaNetViewAuthorizationCustom custom = new VisaNetViewAuthorizationCustom();
                        custom.setLogoTextMerchant(true);
                        custom.setLogoTextMerchantText("FASTBUY");
                        custom.setLogoTextMerchantTextColor(R.color.visanet_black);
                        custom.setLogoTextMerchantTextSize(20);
                        //Personalización 2: Configuración del color del botón pagar en el formulario de pago
                        custom.setButtonColorMerchant(R.color.verde_fosforescente);
                        custom.setInputCustom(true);
                        progressDialog.dismiss();
                        try {
                            VisaNet.authorization(PagoTransporteActivity.this, data, custom);
                            Log.i("TAG", "----------------------------- onClick: SUCCESS " );
                        }
                        catch (Exception e) {
                            Log.i("TAG", "----------------------------- onClick: " + e.getMessage());
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
}
