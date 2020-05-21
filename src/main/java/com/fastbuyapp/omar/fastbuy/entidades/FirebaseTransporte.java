package com.fastbuyapp.omar.fastbuy.entidades;

import java.io.Serializable;

public class FirebaseTransporte implements Serializable {
    private String codTransporte;
    private String pedido;


    public String getCodRepartidor() {
        return codTransporte;
    }

    public void setCodRepartidor(String codRepartidor) {
        this.codTransporte = codRepartidor;
    }


}
