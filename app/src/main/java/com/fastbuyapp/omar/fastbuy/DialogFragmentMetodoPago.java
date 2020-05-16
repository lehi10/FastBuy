package com.fastbuyapp.omar.fastbuy;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.fastbuyapp.omar.fastbuy.config.Globales;

public class DialogFragmentMetodoPago extends DialogFragment {

    private int pago_id=1;
    private String pago_name;

    CheckBox    chbxEfectivo;
    CheckBox    chbxTarjeta;
    CheckBox    chbxYape;
    CheckBox    chbxPlin;

    Button      buttonAcept;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_fragment_metodo_pago, container, false);

        buttonAcept     = rootView.findViewById(R.id.button_enviar_metodoPago);

        chbxEfectivo    = rootView.findViewById(R.id.chbxEfectivo);
        chbxTarjeta     = rootView.findViewById(R.id.chbxTarjeta);
        chbxYape        = rootView.findViewById(R.id.chbxYape);
        chbxPlin        = rootView.findViewById(R.id.chbxPlin);


        optionSavedCheckBox(Globales.tipoPago);
        checkBoxOnSelectOneItem();
        buttonAceptOnClick();

        return rootView;
    }

    private void optionSavedCheckBox(int pago_id) {

        chbxEfectivo.setChecked(false);
        chbxTarjeta.setChecked(false);
        chbxYape.setChecked(false);
        chbxPlin.setChecked(false);

        switch (pago_id){
            case 1:
                chbxEfectivo.setChecked(true);
                pago_name="Efectivo";
                break;
            case 2:
                chbxTarjeta.setChecked(true);
                pago_name="Tarjeta";
                break;
            case 3:
                chbxYape.setChecked(true);
                pago_name="Yape";
                break;
            case 4:
                chbxPlin.setChecked(true);
                pago_name="Plin";
                break;

        }
    }

    private void buttonAceptOnClick() {
        buttonAcept.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (pago_id){
                    case 1:
                        pago_name="Efectivo";
                        break;
                    case 2:
                        pago_name="Tarjeta";
                        break;
                    case 3:
                        pago_name="Yape";
                        break;
                    case 4:
                        pago_name="Plin";
                        break;

                }
                ((TransporteActivity)getActivity()).metodo_pago_id = pago_id;
                ((TransporteActivity)getActivity()).metodo_pago_name = pago_name;

                ((TransporteActivity)getActivity()).textView_metodoPago.setText(pago_name);


                getDialog().dismiss();
            }
        });
    }

    private void checkBoxOnSelectOneItem() {

        chbxEfectivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chbxEfectivo.setChecked(true);
                chbxTarjeta.setChecked(false);
                chbxYape.setChecked(false);
                chbxPlin.setChecked(false);
                //chbxCodQR.setChecked(false);
                pago_id = 1;
                Globales.tipoPago = pago_id;
                Globales.formaPago = "Efectivo";

            }
        });

        chbxTarjeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chbxEfectivo.setChecked(false);
                chbxTarjeta.setChecked(true);
                chbxYape.setChecked(false);
                chbxPlin.setChecked(false);
                //chbxCodQR.setChecked(false);
                pago_id = 2;
                Globales.tipoPago = pago_id;
                Globales.formaPago = "Tarjeta";
                Globales.pagarcon = "0";
            }
        });
        chbxYape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chbxEfectivo.setChecked(false);
                chbxTarjeta.setChecked(false);
                chbxYape.setChecked(true);
                chbxPlin.setChecked(false);
                //chbxCodQR.setChecked(false);
                pago_id = 3;
                Globales.tipoPago = pago_id;
                Globales.formaPago = "Yape";
                Globales.pagarcon = "0";
            }
        });
        chbxPlin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chbxEfectivo.setChecked(false);
                chbxTarjeta.setChecked(false);
                chbxYape.setChecked(false);
                chbxPlin.setChecked(true);
                //chbxCodQR.setChecked(false);
                pago_id = 4;
                Globales.tipoPago = pago_id;
                Globales.formaPago = "Plin";
                Globales.pagarcon = "0";
            }
        });
    }
}
