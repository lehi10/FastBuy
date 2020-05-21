package com.fastbuyapp.omar.fastbuy.config;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;

import com.fastbuyapp.omar.fastbuy.Validaciones.ValidacionDatos;
import com.fastbuyapp.omar.fastbuy.entidades.DistanciaPedidoDetalle;
import com.fastbuyapp.omar.fastbuy.entidades.Empresa;
import com.fastbuyapp.omar.fastbuy.entidades.Establecimiento;
import com.fastbuyapp.omar.fastbuy.entidades.PedidoDetalle;
import com.fastbuyapp.omar.fastbuy.entidades.Producto;
import com.fastbuyapp.omar.fastbuy.entidades.Promocion;
import com.fastbuyapp.omar.fastbuy.entidades.Ubicacion;

import java.util.ArrayList;

/**
 * Created by OMAR on 05/09/2018.
 */

public class Globales {
    private static Globales instance;
    //private ArrayList<PedidoDetalle> listaPedidos = new ArrayList<PedidoDetalle>(); //lista global de los pedidos
    public static ArrayList<PedidoDetalle> listaPedidos = new ArrayList<PedidoDetalle>(); //lista global de los pedidos
    public static ArrayList<Ubicacion> listCiudades = new ArrayList<Ubicacion>(); //lista global de las ciudades
    private ArrayList<DistanciaPedidoDetalle> listaDistancias = new ArrayList<DistanciaPedidoDetalle>(); //lista de distancias
    private ArrayList<Empresa> listaEmpresasPedido= new ArrayList<Empresa>();//lista para empresas de donde se hagan pedidos
    public Globales(){

    }

    public ArrayList<PedidoDetalle> getListaPedidos() {
        return listaPedidos;
    }
    public ArrayList<Empresa> getListaEmpresasPedido() {
        return listaEmpresasPedido;
    }
    public ArrayList<DistanciaPedidoDetalle> getListaDistancias() {
        return listaDistancias;
    }

    public void agregarPedido(PedidoDetalle detalle) {
        this.listaPedidos.add(detalle);
    }
    public void agregarEmpresa(Empresa empresa) {
        this.listaEmpresasPedido.add(empresa);
    }

    public void agregarDistancia(DistanciaPedidoDetalle distancia) {
        this.listaDistancias.add(distancia);
    }

    public void setListaPedidos(ArrayList<PedidoDetalle> listaPedidos){
        this.listaPedidos = listaPedidos;
    }

    public void setListaDistancias(ArrayList<DistanciaPedidoDetalle> listaDistancias){
        this.listaDistancias = listaDistancias;
    }

    public static synchronized Globales getInstance(){
        if (instance == null)
            instance = new Globales();
        return instance;
    }

    //Mi Token de ACCESO
    public static String tokencito = "Xid20200110e34CorpFastBuySAC2020comfastbuyusuario";

    //para controlar el ingreso al activity favoritos
    public static boolean isFavoritos = false;

    //para poder controlar la vista de direcciones
    public static boolean addNewDirec = false;

    //para eliminar del carrito de compras
    public static int codiProdCar = -1;

    public static ValidacionDatos valida = new ValidacionDatos();

    public static Intent myService;

    //para validar si se cerro session
    public static boolean mySession = false;
    public static int Subcategoria = 0;
    public static int categoria = 0;
    public static int ubicacion = -1;

    //para controlar el acceso a google y Facebook
    public static boolean preSession = false;

    //para controlar que el pedido no sea de más de 2 establecimientos
    public static int numEstablecimientos = 0;
    public static String establecimiento1 = "";
    public static int codEstablecimiento1;
    public static int ubicaEstablecimiento1;

    //para delimitar el area
    public static Double longitudCiudadMapa = 0.0;
    public static Double latitudCiudadMapa = 0.0;
    public static int radioCiudadMapa = 0;
    public static Double precioBaseCiudadMapa = 0.0;
    public static Double precioExtraCiudadMapa = 0.0;

    public static int catProductoSeleccionado;
    public static String apiKey = "AIzaSyCOsnfR9NuuaynSCgVAMM2d9NwMF3rv-PE";
    public static String apiKeyMiMaps = "AIzaSyCIO8-QPK8kuDjMn0fcQqnSkj5sV4r8ItA";
    public static String codigoCliente="";
    public static String nombreCliente = "";
    public static String fotoCliente = "";
    public static String direccion = "";
    public static String direccion2 = "";

    public static String referencia = "";
    public static String numeroTelefono = "";
    public static String email = "";
    public static String ciudadOrigen = "";
    public static Double longitudOrigen = 0.0;
    public static Double latitudOrigen = 0.0;
    public static double montoTotal;
    public static double montoDelivery;
    public static double montoCompra;
    public static String formaPago = "Efectivo";
    public static double montoCargo = 0;
    public static double montoDescuento = 0;

    // variables para direcciones - Delivery Normal
    public static String EtiquetaSeleccionada = "";
    public static String DireccionSeleccionada = "";
    public static String LatitudSeleccionada = "";
    public static String LongitudSeleccionada = "";
    public static int CodigoDireccionSeleccionada;
    public static String CiudadDireccionSeleccionada = "";

    // variables para direcciones - Encargo
    public static String EtiquetaSeleccionada_Encargo = "";
    public static String DireccionSeleccionada_Encargo = "";
    public static String LatitudSeleccionada_Encargo = "";
    public static String LongitudSeleccionada_Encargo = "";
    public static int CodigoDireccionSeleccionada_Encargo;
    public static String CiudadDireccionSeleccionada_Encargo = "";

    // variables para direcciones - Extra
    public static String EtiquetaSeleccionada_Extra = "";
    public static String DireccionSeleccionada_Extra = "";
    public static String LatitudSeleccionada_Extra = "";
    public static String LongitudSeleccionada_Extra = "";
    public static int CodigoDireccionSeleccionada_Extra;
    public static String CiudadDireccionSeleccionada_Extra = "";

    // variables para Encargos
    public static boolean isEncargo = false;
    public static String CiudadEncargoSeleccionada = "";
    public static int CodigoCiudadEncargoSeleccionada;
    public static double montoDeliveryEncargo = 0.0;
    public static String LugarRecogerEncargo = "";
    public static String DetalleEncargo = "";
    public static String NumeroContactoEncargo = "";

    // variables para Extras - pide lo que quieras
    public static boolean isExtra = false;
    public static String CiudadPideloSeleccionada = "";
    public static int CodigoCiudadPideloSeleccionada;
    public static double montoDeliveryPidelo = 0.0;
    public static String LugarComprarPidelo = "";
    public static String DetallePidelo = "";
    public static String NumeroContactoPidelo = "";

    //variable para determinar el tipo de pago
    public static int tipoPago;

    //variable para determinar si es promocion
    public static boolean promo = false;

    public static int empresaSeleccionada;
    public static String nombreEmpresaSeleccionada="";
    public static Double  LongitudEmpresaSeleccionada;
    public static Double  LatitudEmpresaSeleccionada;
    public static int ubicacionEmpresaSeleccionada;
    public static String taperEmpresaSel = "";
    public static double costoTaperEmpresaSel = 0.0;

    public static int tiendasAbiertas = 0;
    //Promocion seleccionada
    public static Promocion PromocionPersonalizar;
    //Producto seleccionada
    public static Producto productoPersonalizar;
    //variables para personalización de pedido
    public static String personalizarNombre;
    public static String personalizarDescripcion  = "";
    public static String personalizarCategoria ;
    public static String personalizarPrecio;
    public static String personalizarCantidad;
    public static String personalizarImagen;

    //variables para distancia y duracion
    public static int Distancia;
    public static int DuracionSegundos;
    public static String DuracionTexto;
    public static String mensaje;
    public static String ok = "";

    public static String mensajeVisa;

    //para validar si la tienda seleccionada está abierta
    public static boolean tiendaCerrada = false;

    public static String imagenSubcategoria;
    public static String imagenEmpresa;
    public static String imagenFondoEmpresa;
    public static String Riffic = "fonts/Riffic.ttf";
    public static String Nexa = "fonts/NEXABOLD.otf";
    public static String Gothic = "fonts/GOTHIC.ttf";
    public static String FontAwesome = "fonts/FontAwesome.ttf";
    public static Typeface typefaceRiffic;
    public static Typeface typefaceNexa;
    public static Typeface typefaceGothic;
    public static Typeface typefaceFontAwesome;
    public static String mVerificationId;

    public static boolean hayUsuario = false;
    public static String OpcionInicio = "";

    public static String pagarcon = "";
    public static boolean recoger_en_tienda = false;
    public static double deliveryTemporal = 0; //solo para cuando se hace el rojo en tienda
}
