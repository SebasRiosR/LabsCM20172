package co.edu.udea.compumovil.gr01_20172.lab1.SQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import co.edu.udea.compumovil.gr01_20172.lab1.Helpers.User;

/**
 * Created by Administrator on 5/5/2016.
 */
public class DbHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "myapp.db";
    public static final int DB_VERSION = 1;

    public static final String USER_TABLE = "users";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_FOTO = "foto";
    public static final String COLUMN_NOMBRES = "nombres";
    public static final String COLUMN_APELLIDOS = "apellidos";
    public static final String COLUMN_SEXO = "sexo";
    public static final String COLUMN_FECHA_DE_NACIMIENTO = "fecha";
    public static final String COLUMN_TELEFONO = "telefono";
    public static final String COLUMN_DIRECCION = "direccion";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASS = "password";
    public static final String COLUMN_CIUDAD = "ciudad";
    /*
    create table users(
        id integer primary key autoincrement,
        email text,
        password text);
     */
    public static final String CREATE_TABLE_USERS = "CREATE TABLE " + USER_TABLE + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_EMAIL + " TEXT,"
            + COLUMN_PASS + " TEXT,"
            + COLUMN_NOMBRES + " TEXT,"
            + COLUMN_APELLIDOS + " TEXT,"
            + COLUMN_SEXO + " TEXT,"
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
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, user.getCorreo());
        values.put(COLUMN_PASS, user.getContraseña());
        values.put(COLUMN_NOMBRES, user.getNombres());
        values.put(COLUMN_APELLIDOS, user.getApellidos());
        values.put(COLUMN_SEXO, user.getSexo());
        values.put(COLUMN_TELEFONO, user.getTelefono());
        values.put(COLUMN_DIRECCION, user.getDireccion());
        values.put(COLUMN_CIUDAD, user.getCiudad());

        long id = db.insert(USER_TABLE, null, values);
        db.close();
        long otro = 10;
        Log.d("TAG", "user inserted" + id + "Este es otro " + otro);
    }

    public boolean getUser(String email, String pass){
        Log.d("TAG", "Estoy buscando");
        String selectQuery = "select * from  " + USER_TABLE + " where " +
                COLUMN_EMAIL + " = " + "'"+email+"'" + " and " + COLUMN_PASS + " = " + "'"+pass+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        //cursor.moveToFirst();
        if (cursor.getCount() != 0) {

            return true;
        }
        cursor.close();
        db.close();

        return false;
    }
}
