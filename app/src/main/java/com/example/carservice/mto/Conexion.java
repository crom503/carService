package com.example.carservice.mto;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ExpandableListAdapter;
import android.widget.Toast;

import java.util.Date;

public class Conexion {
    private static final String[] cargoEmpleado = new String[]{"id_cargo","nombre_cargo"};
    private static final String[] usuario = new String[]{"usuario","contra"};
    private static final String[] empleado = new String[]{"id_empleado","id_cargo","nombre","apellidos","usuario"};
    private static final String[] cliente = new String[]{"id_cliente","nombre","apellidos","direccion","telefono","dui","usuario","fecha_registro"};
    private static final String[] categoriaFalla = new String[]{"id_categoria_falla","nombre_categoria_falla","descripcion"};
    private static final String[] tipoMto = new String[]{"id_tipo_mto","nombre_tipo_mto"};
    private static final String[] tipoAuto = new String[]{"id_tipo_auto","tipo_auto"};
    private static final String[] sucursal = new String[]{"id_sucursal","nombre_sucursal","direccion"};
    private static final String[] marcaAuto = new String[]{"id_marca","nombre_marca","id_modelo"};
    private static final String[] falla = new String[]{"id_falla","descripcion","id_categoria_falla"};
    private static final String[] auto = new String[]{"id_auto","id_tipo_auto","año","placa","id_modelo"};
    private static final String[] diagnosticoFalla = new String[]{"id_falla","id_diagnostico"};
    private static final String[] mantenimiento = new String[]{"id_mto","id_diagnostico","id_tipo_mto","id_sucursal","id_empleado","estado_mto","descripcion","fecha_mto","proximo_mto"};
    private static final String[] detalleMto = new String[]{"id_mto","id_falla"};
    private static final String[] diagnosticoMto = new String[]{"id_diagnostico_mto","descripcion"};
    private static final String[] facturacion = new String[]{"id_facturacion","id_mto","monto","efectivo","cambio","fecha_factura"};


    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public Conexion(Context context) {
        this.context = context;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        private static final String BASE_DATOS = "carservice.s3db";
        private static final int VERSION = 1;

        public DatabaseHelper(Context context) {
            super(context, BASE_DATOS, null , VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try{
                //CREANDO TABLA CARGO EMPLEADO
                db.execSQL("CREATE TABLE cargoEmpleado(id_cargo INTEGER NOT NULL PRIMARY KEY,nombre_cargo VARCHAR(50) NOT NULL);");

                //CREANDO TABLA USUARIO
                db.execSQL("CREATE TABLE usuario(usuario CHAR(50) NOT NULL PRIMARY KEY,contra CHAR(50) NOT NULL);");

                //CREANDO TABLA EMPLEADO
                db.execSQL("CREATE TABLE empleado(id_empleado INTEGER NOT NULL PRIMARY KEY,nombre VARCHAR(50) NOT NULL,apellidos VARCHAR(50) NOT NULL,id_cargo int, usuario CHAR(50) NOT NULL UNIQUE,FOREIGN KEY(id_cargo) REFERENCES cargoEmpleado(id_cargo),FOREIGN KEY(usuario) REFERENCES usuario(usuario));");

                //CREANDO TABLA CLIENTE
                db.execSQL("CREATE TABLE cliente(id_cliente INTEGER NOT NULL PRIMARY KEY,nombre VARCHAR(50) NOT NULL,apellidos VARCHAR(50),direccion VARCHAR(150),telefono CHAR(9),dui CHAR(9) NOT NULL UNIQUE,fecha_ingreso VARCHAR(10) NOT NULL,usuario CHAR(50) NOT NULL UNIQUE,FOREIGN KEY(usuario) REFERENCES usuario(usuario));");

                //CREANDO TABLA TIPO AUTO
                db.execSQL("CREATE TABLE tipoAuto(id_tipo_auto INTEGER NOT NULL PRIMARY KEY,nombre_tipo_auto VARCHAR(50) NOT NULL UNIQUE);");

                //CREATE TABLA SUCURSAL
                db.execSQL("CREATE TABLE sucursal(id_sucursal INTEGER NOT NULL PRIMARY KEY,nombre_sucursal VARCHAR(50) NOT NULL UNIQUE,direccion_sucursal VARCHAR(150))");
                /*
                //CREANDO TABLA CATEGORIA FALLA
                db.execSQL("CREATE TABLE categoriaFalla(" +
                        "id_categoria_falla INT NOT NULL PRIMARY KEY," +
                        "nombre_categoria_falla VARCHAR(50) NOT NULL UNIQUE," +
                        "descripcion VARCHAR(150))");

                //CREANDO TABLA TIPO MANTENIMIENTO
                db.execSQL("CREATE TABLE tipoMantenimiento(" +
                        "id_tipo_mto INT NOT NULL PRIMARY KEY," +
                        "nombre_tipo_mto VARCHAR(50) NOT NULL UNIQUE)");

                //CREANDO TABLA MODELO AUTO
                db.execSQL("CREATE TABLE modeloAuto(" +
                        "id_modelo_auto INT NOT NULL PRIMARY KEY," +
                        "nombre_modelo VARCHAR(50) NOT NULL UNIQUE)");

                //CREANDO TABLA MARCA AUTO
                db.execSQL("CREATE TABLE marcaAuto(" +
                        "id_marca_auto INT NOT NULL PRIMARY KEY," +
                        "nombre_marca VARCHAR(50) NOT NULL UNIQUE," +
                        "id_modelo_auto INT NOT NULL," +
                        "FOREIGN KEY(id_modelo_auto) REFERENCES modeloAuto(id_modelo_auto))");

                //CREANDO TABLA AUTO
                db.execSQL("CREATE TABLE auto(" +
                        "id_auto INT NOT NULL PRIMARY KEY," +
                        "anio INT NOT NULL," +
                        "placa CHAR(8) NOT NULL UNIQUE," +
                        "id_marca INT NOT NULL," +
                        "id_tipo_auto INT NOT NULL," +
                        "FOREIGN KEY(id_marca) REFERENCES marcaAuto(id_marca)," +
                        "FOREIGN KEY(id_tipo_auto) REFERENCES tipoAuto(id_tipo_auto))");

                //CREANDO TABLA FALLA
                db.execSQL("CREATE TABLE falla(" +
                        "id_falla INT NOT NULL PRIMARY KEY," +
                        "descripcion_falla VARCHAR(150) NOT NULL," +
                        "id_categoria_falla INT NOT NULL," +
                        "FOREIGN KEY(id_categoria_falla) REFERENCES categoriaFalla(id_categoria_falla))");

                //CREANDO TABLA DIAGNOSTICO MANTENIMIENTO
                db.execSQL("CREATE TABLE diagnostico_mto(" +
                        "id_diagnostico INT NOT NULL PRIMARY KEY," +
                        "descripcion VARHCAR(150) NOT NULL)");

                //CREANDO TABLA DIAGNOSTICO-FALLA
                db.execSQL("CREATE TABLE diagnostico_falla(" +
                        "id_diagnostico INT NOT NULL," +
                        "id_falla INT NOT NULL," +
                        "FOREIGN KEY(id_diagnostico) REFERENCES diagnostico_mto(id_diagnostico)," +
                        "FOREIGN KEY(id_falla) REFERENCES falla(id_falla))");

                //CREANDO TABLA MANTENIMIENTO
                db.execSQL("CREATE TABLE mantenimiento(" +
                        "id_mantenimiento INT NOT NULL PRIMARY KEY," +
                        "descripcion_mto VARCHAR(200) NOT NULL," +
                        "fecha_mto DATETIME NOT NULL," +
                        "proximo_mto DATETIME," +
                        "estado_mto CHAR(1) NOT NULL," +
                        "id_diagnostico INT NOT NULL," +
                        "id_tipo_mto INT NOT NULL," +
                        "id_sucursal INT NOT NULL," +
                        "id_empleado INT NOT NULL," +
                        "FOREIGN KEY(id_diagnostico) REFERENCES diagnostico(id_diagnostico)," +
                        "FOREIGN KEY(id_tipo_mto) REFERENCES tipoMantenimiento(id_tipo_mto)," +
                        "FOREIGN KEY(id_sucursal) REFERENCES sucursal(id_sucursal)," +
                        "FOREIGN KEY(id_empleado) REFERENCES empleado(id_empleado))");

                //CREANDO TABLA DETALLE MANTENIMIENTO
                db.execSQL("CREATE TABLE detalle_mto(" +
                        "id_mto INT NOT NULL," +
                        "id_falla INT NOT NULL," +
                        "FOREIGN KEY(id_mto) REFERENCES mantenimiento(id_mantenimiento)," +
                        "FOREIGN KEY(id_falla) REFERENCES falla(id_falla))");

                //CREANDO TABLA FACTURA
                db.execSQL("CREATE TABLE facturacion(" +
                        "id_factura INT NOT NULL PRIMARY KEY," +
                        "monto REAL NOT NULL," +
                        "efectivo REAL NOT NULL," +
                        "cambio REAL NOT NULL," +
                        "fecha_factura DATETIME NOT NULL," +
                        "id_mto INT NOT NULL," +
                        "FOREIGN KEY(id_mto) REFERENCES mantenimiento(id_mantenimiento))");*/
            }catch(SQLException e){
                e.printStackTrace();}
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
    }

    public void abrir() throws SQLException{
        db = DBHelper.getWritableDatabase();
        return;
    }
    public void cerrar(){
        DBHelper.close();
    }

    public String insertar(mtoCargoEmpleado cargo){
        String regInsertados="ERROR!";
        long contador=0;
        try{
            ContentValues dato = new ContentValues();
            dato.put("id_cargo", cargo.getId_cargo());
            dato.put("nombre_cargo", cargo.getNombre_cargo());
            contador=db.insert("cargoEmpleado", null, dato);
            if(contador==-1 || contador==0) regInsertados= "Error al guardar los datos";
            else regInsertados = "¡Datos guardados con éxito!";
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return regInsertados;
    }

    public boolean login(String usuario, String contra){
        boolean resp = false;
        Cursor cursor = db.rawQuery("SELECT * FROM usuario WHERE usuario = '"+usuario+"' AND contra = '"+contra+"'",null);
        if(cursor.moveToFirst())
            resp = true;
        return resp;
    }

    public String insertar(mtoUsuario usuario){
        String resp="ERROR!";
        long contador=0;
        try{
            ContentValues dato = new ContentValues();
            dato.put("usuario",usuario.getUsuario());
            dato.put("contra",usuario.getContra());
            contador=db.insert("usuario", null, dato);
            if(contador==-1 || contador==0) resp= "Error al guardar los datos";
            else resp = "¡Datos guardados con éxito!";
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return resp;
    }

    public String actualizar(mtoUsuario usuario){
        try{
            String [] id = {usuario.getUsuario()};
            ContentValues cv = new ContentValues();
            cv.put("contra", usuario.getContra());
            db.update("usuario",cv,"usuario = ?", id);
            return "REGISTRO ACTUALIZADO";
        }catch (Exception ex){
            ex.printStackTrace();
            return "ERROR AL ACTUALIZAR";
        }
    }

    public String insertar(mtoEmpleado empleado){
        String resp = "ERROR!";
        long contador=0;
        try{
            ContentValues dato = new ContentValues();
            dato.put("id_empleado",empleado.getId_empleado());
            dato.put("nombre",empleado.getNombre());
            dato.put("apellidos",empleado.getApellidos());
            dato.put("id_cargo",empleado.getId_cargo());
            dato.put("usuario",empleado.getUsuario());
            contador=db.insert("empleado", null, dato);
            if(contador==-1 || contador==0) resp= "Error al guardar los datos";
            else resp = "¡Datos guardados con éxito!";
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return resp;
    }

    public String insertar(mtoCliente cliente){
        String resp = "ERROR!";
        long contador=0;
        try{
            ContentValues dato = new ContentValues();
            dato.put("id_cliente",cliente.getId_cliente());
            dato.put("nombre",cliente.getNombre());
            dato.put("apellidos",cliente.getApellidos());
            dato.put("direccion",cliente.getDireccion());
            dato.put("telefono",cliente.getTelefono());
            dato.put("dui",cliente.getDui());
            dato.put("fecha_ingreso",cliente.getFecha_registro());
            dato.put("usuario",cliente.getUsuario());
            contador=db.insert("cliente", null, dato);
            if(contador==-1 || contador==0) resp= "Error al guardar los datos";
            else resp = "¡Datos guardados con éxito!";
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return resp;
    }

    public String insertar(mtoTipoAuto tipoAuto){
        String resp = "ERROR!";
        long contador=0;
        try{
            ContentValues dato = new ContentValues();
            dato.put("id_tipo_auto",tipoAuto.getId_tipo_auto());
            dato.put("nombre_tipo_auto",tipoAuto.getTipo_auto());
            contador=db.insert("tipoAuto", null, dato);
            if(contador==-1 || contador==0) resp= "Error al guardar los datos";
            else resp = "¡Datos guardados con éxito!";
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return resp;
    }

    public String insertar(mtoSucursal sucursal){
        String regInsertados="";
        long contador=0;
        try {
            ContentValues dato = new ContentValues();
            dato.put("id_sucursal",sucursal.getId_sucursal());
            dato.put("nombre_sucursal",sucursal.getNombre_sucursal());
            dato.put("direccion_sucursal",sucursal.getDireccion());
            contador=db.insert("sucursal",null,dato);
            if(contador==-1 || contador==0) regInsertados= "Error al guardar los datos";
            else regInsertados = "¡Datos guardados con éxito!";
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return regInsertados;
    }

    public void llenarBDCarnet(){
        try {
            final int[] Codcargo = {1,2,3};
            final String[] Nomcargo = {"Administrador","Gerente","Mecanico"};
            final String[] user = {"admin","gerente1","mecanico1","cliente1","cliente2","cliente3"};
            final String[] pwd = {"admin","gerente1","mecanico1","cliente1","cliente2","cliente3"};
            final int[] Codempl = {1,2,3};
            final String[] NomEmp = {"Carlos","Jose","Jorge"};
            final String[] ApeEmp = {"Orellana","Duran","Lara"};
            final int[] Codcli = {1,2,3};
            final String[] Nomcli = {"Mario","Maria","Pablo"};
            final String[] Apecli = {"Zepeda","Lara","Ramirez"};
            final String[] Direcli = {
                    "Colonia Rio Chiquito #2, San Miguel",
                    "Residencial La Esquina #489, psj 1 pol 34 #490, Santa Ana",
                    "Barrio el Clavario #5043, San Salvador"
            };
            final String[] Duicli = {"0012314-9","0023156-9","0201266-7"};
            final String[] FechaCli = {"12/05/2022","12/05/2022","12/05/2022"};
            final String[] Telcli = {"2222-2222","2121-2121","2312-2312"};
            final int[] Tipoau = {1,2,3};
            final String[] Nomtp = {"Sedán","Hatchback","SUV"};
            final int[] Codsuc = {1,2,3};
            final String[] Nomsuc = {"Casa Matriz","Centro","San Miguel"};
            final String[] Dirsuc = {"San Salvador","Santa Ana","San Miguel"};
            abrir();

            db.execSQL("DELETE FROM empleado");
            db.execSQL("DELETE FROM cliente");
            db.execSQL("DELETE FROM cargoEmpleado");
            db.execSQL("DELETE FROM usuario");
            db.execSQL("DELETE FROM tipoAuto");
            db.execSQL("DELETE FROM sucursal");

            mtoCargoEmpleado cargo = new mtoCargoEmpleado();
            mtoUsuario usuario = new mtoUsuario();
            mtoEmpleado empleado = new mtoEmpleado();
            mtoCliente cliente = new mtoCliente();
            mtoTipoAuto tipoA = new mtoTipoAuto();
            mtoSucursal sucursal = new mtoSucursal();

            for (int i=0;i<6;i++){
                usuario.setUsuario(user[i]);
                usuario.setContra(pwd[i]);
                insertar(usuario);
            }
            for (int i=0;i<3;i++){
                cargo.setId_cargo(Codcargo[i]);
                cargo.setNombre_cargo(Nomcargo[i]);
                insertar(cargo);
                tipoA.setId_tipo_auto(Tipoau[i]);
                tipoA.setTipo_auto(Nomtp[i]);
                insertar(tipoA);
                empleado.setId_empleado(Codempl[i]);
                empleado.setNombre(NomEmp[i]);
                empleado.setApellidos(ApeEmp[i]);
                empleado.setId_cargo(Codcargo[i]);
                empleado.setUsuario(user[i]);
                insertar(empleado);
                cliente.setId_cliente(Codcli[i]);
                cliente.setNombre(Nomcli[i]);
                cliente.setApellidos(Apecli[i]);
                cliente.setDireccion(Direcli[i]);
                cliente.setTelefono(Telcli[i]);
                cliente.setDui(Duicli[i]);
                cliente.setFecha_registro(FechaCli[i]);
                cliente.setUsuario(user[i+3]);
                insertar(cliente);
                sucursal.setId_sucursal(Codsuc[i]);
                sucursal.setNombre_sucursal(Nomsuc[i]);
                sucursal.setDireccion(Dirsuc[i]);
                insertar(sucursal);
            }
            cerrar();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }
}
