package co.edu.udea.compumovil.gr01_20172.lab1.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import co.edu.udea.compumovil.gr01_20172.lab1.Helpers.User;
import co.edu.udea.compumovil.gr01_20172.lab1.R;
import co.edu.udea.compumovil.gr01_20172.lab1.Helpers.Session;
import co.edu.udea.compumovil.gr01_20172.lab1.SQL.DbHelper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ImageView userImage;
    private TextView nameUser, lastNameUser, sexUser, birthDayUser, telephoneUser, addressUser,
            emailUser, cityUser;
    private Session session;
    private static User usuario = new User();
    DbHelper db = new DbHelper(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.setTitle("Pantalla principal");
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, getResources().getString(R.string.proximamente), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        //ColorStateList csl = new ColorStateList(new int[][]{new int[0]}, new int[]{0xff00ff00});
        //fab.setBackgroundTintList(csl);
        session = new Session(this);
        usuario = session.getUser();

        userImage = (ImageView) findViewById(R.id.imageUser);
        nameUser = (TextView)findViewById(R.id.userName);
        lastNameUser = (TextView)findViewById(R.id.userLastName);
        sexUser = (TextView)findViewById(R.id.userSex);
        birthDayUser = (TextView)findViewById(R.id.userBirthDate);
        telephoneUser = (TextView)findViewById(R.id.userTelephone);
        addressUser = (TextView)findViewById(R.id.userAddress);
        emailUser = (TextView)findViewById(R.id.userEmail);
        cityUser = (TextView)findViewById(R.id.userCity);

        mostrarDatos();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home){
            //Nada
        } else if (id == R.id.nav_config) {
            startActivity(new Intent(MainActivity.this, DataUpdate.class));
        } else if (id == R.id.nav_logout) {
            Session session = new Session(this);
            session.logout();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        } else if (id == R.id.nav_info) {
            startActivity(new Intent(MainActivity.this, Activity_informacion.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //public void actualizar(){
        //usuario.setApellidos(prueba.getText().toString());
        //db.updateUser(usuario);
        //mostrarDatos();
    //}

    public void mostrarDatos(){
        Uri uri;
        uri = Uri.parse(usuario.getImagen());
        userImage.setImageURI(uri);
        nameUser.setText(usuario.getNombres());
        lastNameUser.setText(usuario.getApellidos());
        sexUser.setText(usuario.getSexo());
        birthDayUser.setText(usuario.getFechaNacimiento());
        telephoneUser.setText(usuario.getTelefono());
        addressUser.setText(usuario.getDireccion());
        emailUser.setText(usuario.getCorreo());
        cityUser.setText(usuario.getCiudad());
    }
}
