package com.fastbuyapp.omar.fastbuy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.fastbuyapp.omar.fastbuy.Validaciones.ValidacionDatos;
import com.fastbuyapp.omar.fastbuy.config.Globales;
import com.fastbuyapp.omar.fastbuy.entidades.Operaciones;
import com.fastbuyapp.omar.fastbuy.entidades.Tutorial_Item;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SplashActivity extends AppCompatActivity {

    private final int DURACION_SPLASH = 2000; // 2 segundos
    int cant = 0;
    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.fastbuyapp.omar.fastbuy",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA-1");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                Log.d("SHA1:", convertToHex(md.digest()));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_splash);

        //Asignación de TypeFace para las letras de la ventana inicial
        Globales.typefaceRiffic = Typeface.createFromAsset(getAssets(), Globales.Riffic);
        TextView lblFast = (TextView) findViewById(R.id.lblFastBuy);
        TextView lblBuy = (TextView) findViewById(R.id.lbleslogan);
        lblFast.setTypeface(Globales.typefaceRiffic);
        lblBuy.setTypeface(Globales.typefaceRiffic);

         cant = 0; //Cantidad de usuarios encontrados en sqlite 1:0
        Operaciones operaciones = new Operaciones();

        //antiguo
        //cant = operaciones.cargarDatosPersonales(getBaseContext()); //De haber usuario registrado en el celular deberá estar en 1
        //new
        cant = operaciones.cargarDatosPersonalesCache(getBaseContext()); //De haber usuario registrado en el celular deberá estar en 1
        //Handler para mostrar el sphash
        new Handler().postDelayed(new Runnable(){
            public void run(){
                //Si hay conexión continua con la aplicación, de lo contrario aparece la ventana de desconectado
                ValidacionDatos validacion = new ValidacionDatos();
                    if(validacion.hayConexiónRed(SplashActivity.this)){
                    //De haber conexión a internet verifica la existencia de datos en sqlite
                    //De no haber datos (finalCant = 0) entonces mandar a la opcion de inicio de sesión
                    //De haber datos (finalCant = 1) entonces mandar a la selección de ciudad
                            SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);
                            String usuario = myPreferences.getString("Name_Cliente", "unknown");
                            if(usuario.equals("unknown")) {
                                Intent intent = new Intent(SplashActivity.this, TerminosYCondicionesActivity.class);
                                startActivity(intent);
                                /*Intent intent = new Intent(SplashActivity.this, OpcionLoginActivity.class);
                                startActivity(intent);*/
                                Globales.hayUsuario = false;
                            }
                        else{
                            Intent intent = new Intent(SplashActivity.this,CiudadActivity.class);
                            //Intent intent = new Intent(SplashActivity.this, TutorialActivity.class);
                            String codigo = myPreferences.getString("Id_Cliente", "0");
                            String name = myPreferences.getString("Name_Cliente", "");
                            String number = myPreferences.getString("Number_Cliente", "");
                            String e_mail = myPreferences.getString("Email_Cliente", "");
                            String city = myPreferences.getString("City_Cliente", "");
                            String photo = myPreferences.getString("Photo_Cliente", "");
                            Globales.codigoCliente = codigo;
                            Globales.nombreCliente = name;
                            Globales.numeroTelefono = number;
                            Globales.email = e_mail;
                            Globales.ciudadOrigen = city;
                            Globales.fotoCliente = photo;
                            startActivity(intent);
                            Globales.hayUsuario = true;
                        }
                }
                else{
                    Intent intent = new Intent(SplashActivity.this, ActivityDesconectado.class);
                    startActivity(intent);
                }

                finish();
            };
        }, DURACION_SPLASH);
    }



}
