package com.fastbuyapp.omar.fastbuy.Operaciones;

import com.fastbuyapp.omar.fastbuy.config.Globales;

public class Pago_Delivery {

    double tarifaBase = Globales.precioBaseCiudadMapa;
    double distanciaBase = 2.2;
    double CostoDelivery = 0;
    double costoExtra = 0.9; //por kilometro

    public double calcularCostoEnvio(double distancia){
        if(distancia > distanciaBase){
            CostoDelivery = tarifaBase + (costoExtra * (distancia - distanciaBase));
        }
        else{
            CostoDelivery = tarifaBase;
        }
        return CostoDelivery;
    }
}
