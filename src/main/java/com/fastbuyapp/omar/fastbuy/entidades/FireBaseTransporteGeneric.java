package com.fastbuyapp.omar.fastbuy.entidades;

import java.io.Serializable;

public class FireBaseTransporteGeneric implements Serializable {
    private String estado;
    private String id;

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getId() {
        return id;
    }

    public void setId(String pedido) {
        this.id = pedido;
    }
}
