package com.fastbuyapp.omar.fastbuy.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.fastbuyapp.omar.fastbuy.CiudadActivity;
import com.fastbuyapp.omar.fastbuy.PrincipalActivity;
import com.fastbuyapp.omar.fastbuy.R;
import com.fastbuyapp.omar.fastbuy.config.Globales;
import com.fastbuyapp.omar.fastbuy.entidades.Ubicacion;

import java.util.ArrayList;

public class CiudadMapaAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<Ubicacion> listCiudadesMapa;

    public CiudadMapaAdapter(Context context, int layout, ArrayList<Ubicacion> listCiudadesMapa) {
        this.context = context;
        this.layout = layout;
        this.listCiudadesMapa = listCiudadesMapa;
    }

    @Override
    public int getCount() {
        return listCiudadesMapa.size();
    }

    @Override
    public Object getItem(int position) {
        return listCiudadesMapa.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        Button btnCiudad;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;
        if (row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);
            holder = new ViewHolder();
            holder.btnCiudad = row.findViewById(R.id.btnCiudadMapa);
            row.setTag(holder);
        }
        else {
            holder = (ViewHolder) row.getTag();
        }

        holder.btnCiudad.setText("   "+listCiudadesMapa.get(position).getNombre());
        holder.btnCiudad.setTypeface(Globales.typefaceNexa);

        holder.btnCiudad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Globales.ciudadOrigen = listCiudadesMapa.get(position).getNombre();
                Globales.ubicacion = listCiudadesMapa.get(position).getCodigo();

                Globales.latitudCiudadMapa = listCiudadesMapa.get(position).getLat();
                Globales.longitudCiudadMapa = listCiudadesMapa.get(position).getLon();
                Globales.radioCiudadMapa = listCiudadesMapa.get(position).getRadio();
                Globales.precioBaseCiudadMapa = listCiudadesMapa.get(position).getPreciobase();
                Globales.precioExtraCiudadMapa = listCiudadesMapa.get(position).getPrecioextra();

                GuardarCiudad(Globales.ciudadOrigen);
                Intent intent = new Intent(context, PrincipalActivity.class);
                //intent.putExtra("origen","Mapa");
                context.startActivity(intent);
            }
        });
        return row;
    }

    public void GuardarCiudad(String ciudad){
        try {
            SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(context);

            SharedPreferences.Editor myEditor = myPreferences.edit();
            myEditor.putString("City_Cliente",ciudad);
            myEditor.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
