package com.fastbuyapp.omar.fastbuy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class ResumenTransporteActivity extends AppCompatActivity {

    TextView txtEstadoPago,txtImporteVisa1,txtNumOperacionVisa, txtTitularVisa, txtNumTarjetaVisa, txtFechaVisa, txtDescripcionVisa, txtImporteVisa;
    TextView lblTitularVisa, lblNumOperacionVisa;
    Button btnCerrarResumen;
    LottieAnimationView animacion1, animacion2;

    String statePedido, empresaPedido, numPedido;
    int cantidadRespuestas;

    String code_transp, payment_method , state;
    double total, charge, subtotal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen_transporte);

        //inicializar componentes
        txtEstadoPago = findViewById(R.id.txtEstadoVisa);
        txtNumOperacionVisa = findViewById(R.id.txtNumOperacionVisa);
        txtTitularVisa = findViewById(R.id.txtTitularVisa);
        txtNumTarjetaVisa = findViewById(R.id.txtNumTarjetaVisa);
        txtFechaVisa = findViewById(R.id.txtFechaVisa);
        txtDescripcionVisa = findViewById(R.id.txtDescripcionVisa);
        txtImporteVisa = findViewById(R.id.txtImporteVisa);
        btnCerrarResumen = findViewById(R.id.btnCerrarDetallePago);
        txtImporteVisa1 = findViewById(R.id.txtImporteVisa1);

        lblNumOperacionVisa = findViewById(R.id.lblNumOperacionVisa);
        lblTitularVisa = findViewById(R.id.lblTitularVisa);

        animacion1 = findViewById(R.id.animationExitoso);
        animacion2 = findViewById(R.id.animationRechazado);


        Intent intent = getIntent();

        state = intent.getStringExtra("state");
        code_transp = intent.getStringExtra("code_transp");
        payment_method = intent.getStringExtra("payment_method");
        total = Double.valueOf(intent.getStringExtra("total"));
        charge = Double.valueOf(intent.getStringExtra("charge"));
        subtotal = Double.valueOf(intent.getStringExtra("subtotal"));




        Log.i("Resumen",code_transp + " "+state +" "+payment_method+" "+total+" ....................................................... ");

        btnCerrarResumen.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResumenTransporteActivity.this,SiguiendoTransporteActivity.class);

                intent.putExtra("state",state);
                intent.putExtra("code_transp",code_transp);

                startActivity(intent);


            }
        });
    }
}
