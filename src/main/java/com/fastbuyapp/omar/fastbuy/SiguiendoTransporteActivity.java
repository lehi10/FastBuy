package com.fastbuyapp.omar.fastbuy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.fastbuyapp.omar.fastbuy.entidades.FireBaseAnulacion;
import com.fastbuyapp.omar.fastbuy.entidades.FireBaseCancelacion;
import com.fastbuyapp.omar.fastbuy.entidades.FireBaseConfirmacion;
import com.fastbuyapp.omar.fastbuy.entidades.FireBasePreparacion;
import com.fastbuyapp.omar.fastbuy.entidades.FireBaseRepartidor;
import com.fastbuyapp.omar.fastbuy.entidades.FireBaseTransporteGeneric;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

public class SiguiendoTransporteActivity extends AppCompatActivity {
    String stateTransp,codeTransp;

    TextView txtState, txtDescripState;

    LottieAnimationView animacionPendiente, animacionAceptado, animacionEnCamino, animacionEntregado, animacionCancelado, animacionEsperando;

    TextView btnConfirmarCambio;


    LinearLayout LinearEsperando;

    Button btnConfirmaEntrega;

    TextView ind1,ind2,ind3,ind4;
    TextView line2, line3, line4;

    DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference(); //hacemos referencia a la base de datos

    DatabaseReference cambioAsignado = mDatabaseReference.child("t_asignado/"); //hacemos referencia a la tabla
    DatabaseReference cambioAceptado = mDatabaseReference.child("t_aceptado/"); //hacemos referencia a la tabla
    DatabaseReference cambioEnCamino = mDatabaseReference.child("t_en_camino/"); //hacemos referencia a la tabla
    DatabaseReference cambioEnServicio = mDatabaseReference.child("t_servicio/"); //hacemos referencia a la tabla
    DatabaseReference cambioTemporal = mDatabaseReference.child("t_temporal/"); //hacemos referencia a la tabla
    DatabaseReference cambioFinalizado = mDatabaseReference.child("t_finalizado/"); //hacemos referencia a la tabla
    DatabaseReference cambioAnulado = mDatabaseReference.child("t_anulado/"); //hacemos referencia a la tabla
    DatabaseReference cambioEliminado = mDatabaseReference.child("t_eliminado/"); //hacemos referencia a la tabla


    @Override
    protected void onStart() {
        super.onStart();

        final String estado = stateTransp;

        if (!estado.equals("ANULADO") && !estado.equals("ELIMINADO") && !estado.equals("FINALIZADO") ) {
            if (!estado.equals("ASIGNADO")) {

                cambioAsignado.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String confirmacion = dataSnapshot.getValue().toString();
                        Log.i("CONFIRM",confirmacion);

                        FireBaseTransporteGeneric data = dataSnapshot.getValue(FireBaseTransporteGeneric.class);
                        String estadoPedido = data.getEstado();

                        String codPedido = data.getId();
                        if (codPedido.equals(codeTransp)) {
                            validaState(estadoPedido);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            cambioAceptado.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String confirmacion = dataSnapshot.getValue().toString();
                    Log.i("CONFIRM",confirmacion);

                    FireBaseTransporteGeneric data = dataSnapshot.getValue(FireBaseTransporteGeneric.class);
                    String estadoPedido = data.getEstado();

                    String codPedido = data.getId();
                    if (codPedido.equals(codeTransp)) {
                        validaState(estadoPedido);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            cambioEnCamino.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String confirmacion = dataSnapshot.getValue().toString();
                    Log.i("CONFIRM",confirmacion);

                    FireBaseTransporteGeneric data = dataSnapshot.getValue(FireBaseTransporteGeneric.class);
                    String estadoPedido = data.getEstado();

                    String codPedido = data.getId();
                    if (codPedido.equals(codeTransp)) {
                        validaState(estadoPedido);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            cambioEnServicio.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String confirmacion = dataSnapshot.getValue().toString();
                    Log.i("CONFIRM",confirmacion);

                    FireBaseTransporteGeneric data = dataSnapshot.getValue(FireBaseTransporteGeneric.class);
                    String estadoPedido = data.getEstado();

                    String codPedido = data.getId();
                    if (codPedido.equals(codeTransp)) {
                        validaState(estadoPedido);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            cambioTemporal.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String confirmacion = dataSnapshot.getValue().toString();
                    Log.i("CONFIRM",confirmacion);

                    FireBaseTransporteGeneric data = dataSnapshot.getValue(FireBaseTransporteGeneric.class);
                    String estadoPedido = data.getEstado();

                    String codPedido = data.getId();
                    if (codPedido.equals(codeTransp)) {
                        validaState(estadoPedido);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            cambioFinalizado.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String confirmacion = dataSnapshot.getValue().toString();
                    Log.i("CONFIRM",confirmacion);

                    FireBaseTransporteGeneric data = dataSnapshot.getValue(FireBaseTransporteGeneric.class);
                    String estadoPedido = data.getEstado();

                    String codPedido = data.getId();
                    if (codPedido.equals(codeTransp)) {
                        validaState(estadoPedido);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            cambioAnulado.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String confirmacion = dataSnapshot.getValue().toString();
                    Log.i("CONFIRM",confirmacion);

                    FireBaseTransporteGeneric data = dataSnapshot.getValue(FireBaseTransporteGeneric.class);
                    String estadoPedido = data.getEstado();

                    String codPedido = data.getId();
                    if (codPedido.equals(codeTransp)) {
                        validaState(estadoPedido);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            cambioEliminado.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String confirmacion = dataSnapshot.getValue().toString();
                    Log.i("CONFIRM",confirmacion);

                    FireBaseTransporteGeneric data = dataSnapshot.getValue(FireBaseTransporteGeneric.class);
                    String estadoPedido = data.getEstado();

                    String codPedido = data.getId();
                    if (codPedido.equals(codeTransp)) {
                        validaState(estadoPedido);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siguiendo_transporte);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_chevron_left_black_24dp));


        //Inicializando Componentes
        animacionPendiente = (LottieAnimationView) findViewById(R.id.animationPendiente);
        animacionAceptado = (LottieAnimationView) findViewById(R.id.animationAceptado);
        animacionEnCamino = (LottieAnimationView) findViewById(R.id.animationEnCamino);
        animacionEntregado = (LottieAnimationView) findViewById(R.id.animationEntregado);
        animacionCancelado = (LottieAnimationView) findViewById(R.id.animationCancelado);
        animacionEsperando = (LottieAnimationView) findViewById(R.id.animationEsperando);

        txtState = (TextView) findViewById(R.id.txtStatePedido);
        txtDescripState = (TextView) findViewById(R.id.txtDescripcionStatePedido);
        ind1 = (TextView) findViewById(R.id.indicador1);
        ind2 = (TextView) findViewById(R.id.indicador2);
        ind3 = (TextView) findViewById(R.id.indicador3);
        ind4 = (TextView) findViewById(R.id.indicador4);
        line2 = (TextView) findViewById(R.id.lineIndicador2);
        line3 = (TextView) findViewById(R.id.lineIndicador3);
        line4 = (TextView) findViewById(R.id.lineIndicador4);
        LinearEsperando = (LinearLayout) findViewById(R.id.LinearEsperando);

        btnConfirmarCambio = findViewById(R.id.btnConfirmarCambio);
        btnConfirmaEntrega = findViewById(R.id.btnConfirmaEntrega);

        // Estado por defecto, será actualizado en loadeFronDB
        stateTransp ="PENDIENTE";
        //recibiendo datos de activity anterior
        codeTransp = getIntent().getStringExtra("code_transp");
        //este me ayuda a saber si es la primera vez que pide o no

        loadStateFromDB(codeTransp);


        validaState(stateTransp);





        btnConfirmarCambio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(SiguiendoTransporteActivity.this, DetallePedidoConfirmar.class);
                //intent.putExtra("codigo",numPedido);
                //intent.putExtra("empresa",empresaPedido);
                //String consulta = "https://apifbdelivery.fastbuych.com/Delivery/DatosPedido_Confirmar_XCodigo?auth="+ Globales.tokencito+"&codigo="+numPedido;
                //EnviarRecibirDatos2(consulta,intent);
            }
        });

        btnConfirmaEntrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String consulta = "https://apifbdelivery.fastbuych.com/Delivery/ConfirmarEntrega_XPedido_XCodigo?auth="+ Globales.tokencito+"&codigo="+numPedido;
                //EnviarRecibirDatos3(consulta);
            }
        });



    }

    private void loadStateFromDB(String code) {

        String URL="https://apifbtransportes.fastbuych.com/Transporte/EstadoPedido_XCodigo?auth="+Globales.tokencito+"&codigo="+code;
        Log.i("URL",URL);

        final ProgressDialog dialogSaveInDB;
        dialogSaveInDB = new ProgressDialog(SiguiendoTransporteActivity.this);
        dialogSaveInDB.setMessage("Cargando");
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
                        String codeSuccess = objResponse.getString("code");
                        String TRA_Estado = objResponse.getString("TRA_Estado");

                        Log.i("STATE",codeSuccess);

                        if("200".equals(codeSuccess))
                        {
                            dialogSaveInDB.dismiss();
                            stateTransp = TRA_Estado;
                            Log.i("STATE",stateTransp);



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


    private final Runnable m_Runnable = new Runnable() {
        @Override
        public void run() {
            Log.i("Runable"," RUNNABLE !!!!!!!!! ");
            String consulta = "http://apifbtransportes.fastbuych.com/Transporte/EstadoPedido_XCodigo?auth="+ Globales.tokencito+"&codigo="+codeTransp;

            EnviarRecibirDatos(consulta);
            validaState(stateTransp);
        }
    };

    public void EnviarRecibirDatos(String URL){
        RequestQueue queue = Volley.newRequestQueue(SiguiendoTransporteActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.length() > 0) {
                    try {
                        JSONObject jo = new JSONObject(response);
                        stateTransp = jo.getString("PED_Estado");
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


    @SuppressLint("SetTextI18n")
    public void validaState(String estado){
        switch (estado)
        {
            case "PENDIENTE":
                clearAnimations();
                animacionPendiente.playAnimation();
                animacionPendiente.setVisibility(View.VISIBLE);

                txtState.setText("Pendiente");
                txtDescripState.setText("Pedido pendiente de Taxi");

                activaCirculo(ind1);
                desactivaLinea(line2);
                desactivaCirculo(ind2);
                desactivaLinea(line3);
                desactivaCirculo(ind3);
                desactivaLinea(line4);
                desactivaCirculo(ind4);

                muestraEspera(LinearEsperando,animacionEsperando);
                desactivaBoton(btnConfirmarCambio);
                desactivaBoton(btnConfirmaEntrega);
                break;
            case "ASIGNADO":

                clearAnimations();
                animacionAceptado.playAnimation();
                animacionAceptado.setVisibility(View.VISIBLE);


                txtState.setText("Asignado");
                txtDescripState.setText("Tu Taxi fue signado");

                activaCirculo(ind1);
                activaLinea(line2);
                activaCirculo(ind2);
                desactivaLinea(line3);
                desactivaCirculo(ind3);
                desactivaLinea(line4);
                desactivaCirculo(ind4);

                ocultaEspera(LinearEsperando,animacionEsperando);
                desactivaBoton(btnConfirmarCambio);
                desactivaBoton(btnConfirmaEntrega);
                break;
            case "ACEPTADO":
                clearAnimations();
                animacionAceptado.playAnimation();
                animacionAceptado.setVisibility(View.VISIBLE);

                activaCirculo(ind1);
                activaLinea(line2);
                activaCirculo(ind2);
                desactivaLinea(line3);
                desactivaCirculo(ind3);
                desactivaLinea(line4);
                desactivaCirculo(ind4);

                txtState.setText("Taxista confirmado");
                txtDescripState.setText("El taxista confirmo el servicio");

                break;
            case "EN_CAMINO":
                clearAnimations();
                animacionEnCamino.playAnimation();
                animacionEnCamino.setVisibility(View.VISIBLE);

                txtState.setText("En Camino");
                txtDescripState.setText("El taxi está en camino");

                activaCirculo(ind1);
                activaLinea(line2);
                activaCirculo(ind2);
                activaLinea(line3);
                activaCirculo(ind3);
                desactivaLinea(line4);
                desactivaCirculo(ind4);

                ocultaEspera(LinearEsperando,animacionEsperando);
                desactivaBoton(btnConfirmarCambio);
                activaBoton(btnConfirmaEntrega);

                break;
            case "SERVICIO":
                clearAnimations();
                animacionEnCamino.playAnimation();
                animacionEnCamino.setVisibility(View.VISIBLE);

                txtState.setText("En servicio");
                txtDescripState.setText("En servicio");

                activaCirculo(ind1);
                activaLinea(line2);
                activaCirculo(ind2);
                activaLinea(line3);
                activaCirculo(ind3);
                desactivaLinea(line4);
                desactivaCirculo(ind4);

                ocultaEspera(LinearEsperando,animacionEsperando);
                desactivaBoton(btnConfirmarCambio);
                desactivaBoton(btnConfirmaEntrega);
                break;
            case "TEMPORAL":
                clearAnimations();
                animacionPendiente.playAnimation();
                animacionPendiente.setVisibility(View.VISIBLE);

                txtState.setText("Esperando Confirmación");
                txtDescripState.setText("Esperando confirmación de finalización");

                activaCirculo(ind1);
                activaLinea(line2);
                activaCirculo(ind2);
                activaLinea(line3);
                activaCirculo(ind3);
                desactivaLinea(line4);
                desactivaCirculo(ind4);

                ocultaEspera(LinearEsperando,animacionEsperando);
                desactivaBoton(btnConfirmarCambio);
                desactivaBoton(btnConfirmaEntrega);
                break;
            case "FINALIZADO":
                clearAnimations();
                animacionAceptado.playAnimation();
                animacionAceptado.setVisibility(View.VISIBLE);

                txtState.setText("Finalizado");
                txtDescripState.setText("El servicio fue Finalizado");

                activaCirculo(ind1);
                activaLinea(line2);
                activaCirculo(ind2);
                activaLinea(line3);
                activaCirculo(ind3);
                activaLinea(line4);
                activaCirculo(ind4);

                ocultaEspera(LinearEsperando,animacionEsperando);
                desactivaBoton(btnConfirmarCambio);
                desactivaBoton(btnConfirmaEntrega);
                break;
            case "ANULADO":
                clearAnimations();
                animacionCancelado.playAnimation();
                animacionCancelado.setVisibility(View.VISIBLE);

                txtState.setText("Anulado");
                txtDescripState.setText("Tu pedido fue anulado");

                activaCirculo(ind1);
                activaLinea(line2);
                activaCirculo(ind2);
                activaLinea(line3);
                activaCirculo(ind3);
                activaLinea(line4);
                activaCirculo(ind4);

                ocultaEspera(LinearEsperando,animacionEsperando);
                desactivaBoton(btnConfirmarCambio);
                desactivaBoton(btnConfirmaEntrega);
                break;
            case "ELIMINADO":
                clearAnimations();
                animacionCancelado.playAnimation();
                animacionCancelado.setVisibility(View.VISIBLE);

                txtState.setText("Eliminado");
                txtDescripState.setText("Tu pedido fue Eliminado");

                activaCirculo(ind1);
                activaLinea(line2);
                activaCirculo(ind2);
                activaLinea(line3);
                activaCirculo(ind3);
                activaLinea(line4);
                activaCirculo(ind4);

                ocultaEspera(LinearEsperando,animacionEsperando);
                desactivaBoton(btnConfirmarCambio);
                desactivaBoton(btnConfirmaEntrega);
                break;
        }
    }

    public void clearAnimations(){
        animacionPendiente.pauseAnimation();
        animacionAceptado.pauseAnimation();
        animacionEnCamino.pauseAnimation();
        animacionEntregado.pauseAnimation();
        animacionCancelado.pauseAnimation();


        animacionPendiente.setVisibility(View.GONE);
        animacionAceptado.setVisibility(View.GONE);
        animacionEnCamino.setVisibility(View.GONE);
        animacionEntregado.setVisibility(View.GONE);
        animacionCancelado.setVisibility(View.GONE);

    }

    public void controlaAnimacion(LottieAnimationView lot1, LottieAnimationView lot2,
                                  LottieAnimationView lot3, LottieAnimationView lot4,
                                  LottieAnimationView lot5){
        lot1.playAnimation();
        lot1.setVisibility(View.VISIBLE);
        lot2.pauseAnimation();
        lot2.setVisibility(View.GONE);
        lot3.pauseAnimation();
        lot3.setVisibility(View.GONE);
        lot4.pauseAnimation();
        lot4.setVisibility(View.GONE);
        lot5.pauseAnimation();
        lot5.setVisibility(View.GONE);
    }


    public void muestraEspera(LinearLayout espera, LottieAnimationView espAnimation){
        espAnimation.playAnimation();
        espera.setVisibility(View.VISIBLE);
    }

    public void ocultaEspera(LinearLayout espera, LottieAnimationView espAnimation){
        espAnimation.pauseAnimation();
        espera.setVisibility(View.GONE);
    }

    public void desactivaBoton(TextView btn){
        btn.setVisibility(View.GONE);
        btn.setEnabled(false);
    }

    public void activaBoton(TextView btn){
        btn.setVisibility(View.VISIBLE);
        btn.setEnabled(true);
    }

    public void activaCirculo(TextView txt){
        txt.setBackgroundResource(R.drawable.boton_blanco_verde_circulo);
        txt.setTextColor(txt.getContext().getResources().getColor(R.color.fastbuy));
    }

    public void desactivaCirculo(TextView txt){
        txt.setBackgroundResource(R.drawable.boton_blanco_amarillo_circulo);
        txt.setTextColor(txt.getContext().getResources().getColor(R.color.amarillo));
    }

    public void activaLinea(TextView txt){
        txt.setBackgroundResource(R.drawable.linea_verde_fastbuy);
    }

    public void desactivaLinea(TextView txt){
        txt.setBackgroundResource(R.drawable.linea_amarilla);
    }

}


