package co.edu.udea.compumovil.gr01_20172.lab1.Helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class Session {
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Context ctx;
    private static User user = new User();

    public Session(Context ctx){
        this.ctx = ctx;
        prefs = ctx.getSharedPreferences("myapp", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void setLoggedin(boolean logggedin, User usuario){
        user = usuario;
        editor.putBoolean("loggedInmode",logggedin);
        editor.commit();
    }

    public User getUser(){
        return user;
    }

    public void logout(){
        editor.clear();
        editor.commit();
    }
    public boolean loggedin(){
        return prefs.getBoolean("loggedInmode", false);
    }
}
