package com.fastbuyapp.omar.fastbuy.adaptadores;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.fastbuyapp.omar.fastbuy.R;
import com.fastbuyapp.omar.fastbuy.config.GlideApp;
import com.fastbuyapp.omar.fastbuy.config.Globales;
import com.fastbuyapp.omar.fastbuy.config.Servidor;
import com.fastbuyapp.omar.fastbuy.entidades.EmpresaSubcategoria;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by OMAR on 11/03/2019.
 */

public class EmpresaSubcategoriaListAdapter extends BaseAdapter {
    private Typeface fuente1;
    private Context context;
    private int layout;
    private ArrayList<EmpresaSubcategoria> rubroList;

    public EmpresaSubcategoriaListAdapter(Context context, int layout, ArrayList<EmpresaSubcategoria> rubroList) {
        this.context = context;
        this.layout = layout;
        this.rubroList = rubroList;
    }

    @Override
    public int getCount() {
        return rubroList.size();
    }

    @Override
    public Object getItem(int position) {
        return rubroList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        int codigo;
        TextView descripcion;
        ImageView imagen;
    }

    @Override
    public View getView(final int i, View row, ViewGroup viewGroup) {
        EmpresaSubcategoriaListAdapter.ViewHolder holder = null;
        if (row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout,viewGroup,false);
            holder = new EmpresaSubcategoriaListAdapter.ViewHolder();
            holder.descripcion = (TextView) row.findViewById(R.id.txtNombreSub);
            holder.imagen = (ImageView) row.findViewById(R.id.ivimagensub);
            row.setTag(holder);
        }
        else{
            holder = (EmpresaSubcategoriaListAdapter.ViewHolder) row.getTag();
        }

        String fuente = Globales.Nexa;
        Globales.typefaceNexa = Typeface.createFromAsset(context.getAssets(), fuente);
        EmpresaSubcategoria rubro = rubroList.get(i);
        holder.descripcion.setText(rubro.getDescripcion());
        holder.codigo = rubro.getCodigo();
        holder.descripcion.setTypeface(fuente1);
        String nombreImagen = rubro.getImagen();
        Servidor s = new Servidor();
        String url = "https://"+s.getServidor()+"/empresas/subcategorias/imagenes/" + nombreImagen;

        GlideApp.with(context)
                .load(url)
                .fitCenter()
                .transform(new RoundedCornersTransformation(20,0, RoundedCornersTransformation.CornerType.TOP))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        // log exception
                        Log.v("img_promo_carga", "Error loading image", e);
                        return false; // important to return false so the error placeholder can be placed
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(holder.imagen);
        return  row;
    }
}
