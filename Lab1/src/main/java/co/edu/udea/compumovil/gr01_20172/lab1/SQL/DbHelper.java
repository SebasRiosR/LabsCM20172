package co.edu.udea.compumovil.gr01_20172.lab1.SQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import co.edu.udea.compumovil.gr01_20172.lab1.Helpers.User;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "myapp.db";
    private static final int DB_VERSION = 1;
    private static User user = new User();

    private static final String USER_TABLE = "users";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_FOTO = "foto";
    private static final String COLUMN_NOMBRES = "nombres";
    private static final String COLUMN_APELLIDOS = "apellidos";
    private static final String COLUMN_SEXO = "sexo";
    private static final String COLUMN_FECHA_DE_NACIMIENTO = "fecha";
    private static final String COLUMN_TELEFONO = "telefono";
    private static final String COLUMN_DIRECCION = "direccion";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASS = "password";
    private static final String COLUMN_CIUDAD = "ciudad";
    /*
    create table users(
        id integer primary key autoincrement,
        email text,
        password text);
     */
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + USER_TABLE + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_FOTO + " TEXT,"
            + COLUMN_EMAIL + " TEXT,"
            + COLUMN_PASS + " TEXT,"
            + COLUMN_NOMBRES + " TEXT,"
            + COLUMN_APELLIDOS + " TEXT,"
            + COLUMN_SEXO + " TEXT,"
            + COLUMN_FECHA_DE_NACIMIENTO + " TEXT,"
            + COLUMN_TELEFONO + " TEXT,"
            + COLUMN_DIRECCION + " TEXT,"
            + COLUMN_CIUDAD + " TEXT)";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(User user2) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_EMAIL, user2.getCorreo());
        values.put(COLUMN_PASS, user2.getContraseña());
        values.put(COLUMN_NOMBRES, user2.getNombres());
        values.put(COLUMN_APELLIDOS, user2.getApellidos());
        values.put(COLUMN_SEXO, user2.getSexo());
        values.put(COLUMN_TELEFONO, user2.getTelefono());
        values.put(COLUMN_DIRECCION, user2.getDireccion());
        values.put(COLUMN_CIUDAD, user2.getCiudad());
        values.put(COLUMN_FOTO, user2.getImagen());
        values.put(COLUMN_FECHA_DE_NACIMIENTO, user2.getFechaNacimiento());
        long id = db.insert(USER_TABLE, null, values);
        user.setId(String.valueOf(id));
        db.close();
    }

    public boolean getUser(String email, String pass){
        String selectQuery = "select * from " + USER_TABLE + " where " +
                COLUMN_EMAIL + " = " + "'"+email+"'" + " and " + COLUMN_PASS + " = " + "'"+pass+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            user.setImagen(cursor.getString(1));
            user.setCorreo(cursor.getString(2));
            user.setContraseña(cursor.getString(3));
            user.setNombres(cursor.getString(4));
            user.setApellidos(cursor.getString(5));
            user.setSexo(cursor.getString(6));
            user.setFechaNacimiento(cursor.getString(7));
            user.setTelefono(cursor.getString(8));
            user.setDireccion(cursor.getString(9));
            user.setCiudad(cursor.getString(10));
            return true;
        }
        cursor.close();
        db.close();
        return false;
    }

    public void updateUser(User user2){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_EMAIL, user2.getCorreo());
        values.put(COLUMN_PASS, user2.getContraseña());
        values.put(COLUMN_NOMBRES, user2.getNombres());
        values.put(COLUMN_APELLIDOS, user2.getApellidos());
        values.put(COLUMN_SEXO, user2.getSexo());
        values.put(COLUMN_TELEFONO, user2.getTelefono());
        values.put(COLUMN_DIRECCION, user2.getDireccion());
        values.put(COLUMN_CIUDAD, user2.getCiudad());
        values.put(COLUMN_FOTO, user2.getImagen());
        values.put(COLUMN_FECHA_DE_NACIMIENTO, user2.getFechaNacimiento());

        db.update(USER_TABLE, values, COLUMN_ID+"="+user2.getId(), null);
    }

    public User getUser2(){
        return user;
    }
}
