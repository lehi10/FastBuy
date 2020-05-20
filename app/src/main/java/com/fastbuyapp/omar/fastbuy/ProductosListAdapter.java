package com.fastbuyapp.omar.fastbuy;

import android.content.Context;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.fastbuyapp.omar.fastbuy.config.GlideApp;
import com.fastbuyapp.omar.fastbuy.config.Servidor;
import com.fastbuyapp.omar.fastbuy.entidades.PedidoDetalle;
import com.fastbuyapp.omar.fastbuy.entidades.Producto;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by OMAR on 23/08/2018.
 */

public class ProductosListAdapter extends ArrayAdapter<Producto> {
    private Context context;
    private int layout;
    private ArrayList<Producto> productosList;
    //private Typeface fuente1;
    private ConstraintLayout capa;
    private Button bt_main;
    private ImageView imagen;

    public ProductosListAdapter(Context context, int layout, ArrayList<Producto> _productosList) {
        super(context, layout, _productosList);
        this.context = context;
        this.layout = layout;
        this.productosList = _productosList;
    }

    @Override
    public int getCount() {
        return productosList.size();
    }

    @Override
    public Producto getItem(int pos) {
        return productosList.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    private class ViewHolder{
        ImageView imageView;
        TextView txtDescripcion;
        TextView txtCategoria;
        TextView txtPrecio;
        TextView txtNombreImagen;
        TextView txtTiempo;
    }

    public void cambiarIcono(ImageButton btn){
        Log.v("Compara", "OK");


    }

    @Override
    public View getView(final int pos, View row, final ViewGroup viewGroup) {
        //View row = view;
        ViewHolder holder = null ;
        if (row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout,viewGroup,false);
            holder = new ViewHolder();
            holder.txtDescripcion = (TextView) row.findViewById(R.id.txtDescripcion);
            holder.txtPrecio = (TextView) row.findViewById(R.id.txtPrecio);
            holder.txtCategoria = (TextView) row.findViewById(R.id.txtCategoria);
            holder.imageView = (ImageView) row.findViewById(R.id.imgProductos);
            holder.txtTiempo = (TextView) row.findViewById(R.id.txtTimePreparacion);
            holder.txtNombreImagen = (TextView) row.findViewById(R.id.txtNombreImagen);

            /*holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });*/
            row.setTag(holder);
        }
        else{
            holder = (ViewHolder) row.getTag();
        }

        final Producto producto = getItem(pos);
        //Toast.makeText(context, String.valueOf(pos),Toast.LENGTH_SHORT).show();
        String fuente = "fonts/Riffic.ttf";
        //this.fuente1 = Typeface.createFromAsset(context.getAssets(), fuente);
        holder.txtDescripcion.setText(producto.getDescripcion());
        //holder.txtDescripcion.setTypeface(fuente1);
        holder.txtCategoria.setText(producto.getCategoria().getDescripcion());
        holder.txtPrecio.setText("S/ "+ producto.getPrecio());
        //holder.txtCantidad.setTag(R.id.txtCantidad);
        //holder.btnMas.setTag(Integer.valueOf(pos));
        //holder.btnMenos.setTag(Integer.valueOf(pos));
        //holder.btnAgregar.setTag(Integer.valueOf(pos));
        //holder.btnPersonalizar.setTag(Integer.valueOf(pos));
        String nombreImagen = producto.getImagen();
        holder.txtNombreImagen.setText(nombreImagen);
        holder.txtTiempo.setText(" "+String.valueOf(producto.getTiempo())+" min.");
        Servidor s = new Servidor();
        String url = "https://"+s.getServidor()+"/productos/fotos/" + nombreImagen;
        GlideApp.with(context)
                .load(url)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .transform(new RoundedCornersTransformation(20,0))
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
                .into(holder.imageView);

        return  row;


    }
}
