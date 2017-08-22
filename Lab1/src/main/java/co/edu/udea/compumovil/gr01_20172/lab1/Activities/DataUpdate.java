package co.edu.udea.compumovil.gr01_20172.lab1.Activities;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;
import co.edu.udea.compumovil.gr01_20172.lab1.Helpers.Session;
import co.edu.udea.compumovil.gr01_20172.lab1.Helpers.User;
import co.edu.udea.compumovil.gr01_20172.lab1.R;
import co.edu.udea.compumovil.gr01_20172.lab1.SQL.DbHelper;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class DataUpdate extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static String APP_DIRECTORY = "MyPictureApp/";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "PictureApp";

    private final int MY_PERMISSIONS = 100;
    private final int PHOTO_CODE = 200;
    private final int SELECT_PICTURE = 300;

    private NestedScrollView clView;
    private DatePickerDialog datePickerDialog;
    private String mPath;

    private ImageButton selectImage;
    private Button reg;
    private Button selectBirthDate;
    private TextView fechaNacimiento;
    private TextInputEditText etEmail, etPass, etPass2, etName, etLastName, etPhone, etAddress;
    private AutoCompleteTextView etCity;
    private RadioGroup radioGroup;
    private RadioButton r1, r2, r3;
    private RadioButton radioButton;
    private String pathImage = "";
    private String[] ciudades={"Medellín", "Arauca", "Barranquilla", "Cartagena", "Tunja",
            "Manizales", "Florencia", "Yopal", "Popayán", "Valledupar", "Quibdó", "Montería",
            "Bogotá", "Puerto Inírida", "Guaviare", "Neiva", "Riohacha", "Santa Marta",
            "Villavicencio", "Pasto", "Cúcuta", "Mocoa", "Armenia", "Pereira", "San Andrés",
            "Bucaramanga", "Sincelejo", "Ibagué", "Cali", "Mitú", "Puerto Carreño"};
    private Session session;
    private static User usuario = new User();
    DbHelper db = new DbHelper(this);
    String foto, nombres, apellidos, sexo, fecha_nacimiento, telefono, direccion, email, ciudad, contraseña1, contraseña2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_update);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_update);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //
        db = new DbHelper(this);
        clView= (NestedScrollView) findViewById(R.id.nested_scroll_view_update);
        fechaNacimiento = (TextView)findViewById(R.id.fechaStringUpdate);
        selectBirthDate = (Button)findViewById(R.id.botonCalendarioUpdate);
        selectImage = (ImageButton)findViewById(R.id.buttonImageUpdate);
        reg = (Button)findViewById(R.id.btnRegUpdate);
        etEmail = (TextInputEditText) findViewById(R.id.etEmailUpdate);
        etName = (TextInputEditText)findViewById(R.id.etNameUpdate);
        etPass = (TextInputEditText)findViewById(R.id.etPassUpdate);
        etPass2 = (TextInputEditText)findViewById(R.id.etPass2Update);
        etLastName = (TextInputEditText)findViewById(R.id.etLastNameUpdate);
        etPhone = (TextInputEditText)findViewById(R.id.etPhoneUpdate);
        etAddress = (TextInputEditText)findViewById(R.id.etAddressUpdate);
        etCity = (AutoCompleteTextView)findViewById(R.id.etCityUpdate);
        radioGroup = (RadioGroup)findViewById(R.id.groupBUpdate);
        r1 = (RadioButton)findViewById(R.id.radioB1Update);
        r2 = (RadioButton)findViewById(R.id.radioB2Update);
        r3 = (RadioButton)findViewById(R.id.radioB3Update);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ciudades);
        etCity.setAdapter(adapter);
        etCity.setThreshold(1);

        if(mayRequestStoragePermission()) {
            selectImage.setEnabled(true);
        }else {
            showExplanation();
        }

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizar();
            }
        });
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptions();
            }
        });
        selectBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                //String[] separada = fecha_nacimiento.split("/");
                c.setTimeInMillis(System.currentTimeMillis());
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(DataUpdate.this, R.style.DialogTheme,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                fechaNacimiento.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                c.add(c.YEAR,-10);//una persona menor de 10 años no usa la aplicación
                datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        session = new Session(this);
        usuario = session.getUser();

        pathImage = usuario.getImagen();
        foto = usuario.getImagen();
        nombres = usuario.getNombres();
        apellidos = usuario.getApellidos();
        sexo = usuario.getSexo();
        fecha_nacimiento = usuario.getFechaNacimiento();
        telefono = usuario.getTelefono();
        direccion = usuario.getDireccion();
        email = usuario.getCorreo();
        ciudad = usuario.getCiudad();
        contraseña1 = usuario.getContraseña();
        contraseña2 = usuario.getContraseña();

        mostrarDatos();
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
            startActivity(new Intent(DataUpdate.this, MainActivity.class));
        } else if (id == R.id.nav_config) {
            //Nada
        } else if (id == R.id.nav_logout) {
            Session session = new Session(this);
            session.logout();
            startActivity(new Intent(DataUpdate.this, LoginActivity.class));
            finish();
        } else if (id == R.id.nav_info) {
            startActivity(new Intent(DataUpdate.this, Activity_informacion.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_update);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void actualizar(){
        if(mayRequestStoragePermission()) {//Si hay permisos se puede registrar
        String fechaNac = fechaNacimiento.getText().toString();
        String imagen = pathImage;
        String correo = etEmail.getText().toString();
        String contraseña = etPass.getText().toString();
        String nombres = etName.getText().toString();
        String apellidos = etLastName.getText().toString();
        String telefono = etPhone.getText().toString();
        String direccion = etAddress.getText().toString();
        String ciudad = etCity.getText().toString();

        if(fechaNac.isEmpty() || imagen.isEmpty() || correo.isEmpty() || contraseña.isEmpty() || nombres.isEmpty() || apellidos.isEmpty()
                || telefono.isEmpty() || direccion.isEmpty() || ciudad.isEmpty() || (!r1.isChecked()
                && !r2.isChecked() && !r3.isChecked())){
            displayToast(getString(R.string.error1));
        } else if (!(contraseña.equals(etPass2.getText().toString()))){
            displayToast(getString(R.string.error2));
            etPass.setText("");
            etPass2.setText("");
        } else {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            radioButton = (RadioButton) findViewById(selectedId);

            String sex = radioButton.getText().toString();
            usuario.setFechaNacimiento(fechaNac);
            usuario.setImagen(imagen);
            usuario.setCorreo(correo);
            usuario.setContraseña(contraseña);
            usuario.setNombres(nombres);
            usuario.setApellidos(apellidos);
            usuario.setTelefono(telefono);
            usuario.setDireccion(direccion);
            usuario.setCiudad(ciudad);
            usuario.setSexo(sex);
            db.updateUser(usuario);
            displayToast(getString(R.string.confirmacionUpdate));
            startActivity(new Intent(DataUpdate.this, MainActivity.class));
        }
        }else {
            showExplanation();
        }
    }

    private void displayToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private boolean mayRequestStoragePermission() {

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;

        if((checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED))
            return true;

        if((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) || (shouldShowRequestPermissionRationale(CAMERA))){
            Snackbar.make(clView, getString(R.string.permisosNecesarios),
                    Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok, new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
                }
            });
        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
        }

        return false;
    }

    private void showOptions() {
        final CharSequence[] option = {getString(R.string.takePicture), getString(R.string.selectGallery), getString(R.string.cancel)};
        final AlertDialog.Builder builder = new AlertDialog.Builder(DataUpdate.this);
        builder.setTitle(getString(R.string.eligeOpcion));
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(option[which] == getString(R.string.takePicture)){
                    openCamera();
                }else if(option[which] == getString(R.string.selectGallery)){
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent, getString(R.string.selectAppDeImagen)), SELECT_PICTURE);
                }else {
                    dialog.dismiss();
                }
            }
        });

        builder.show();
    }

    private void openCamera() {
        File file = new File(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if(!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if(isDirectoryCreated){
            Long timestamp = System.currentTimeMillis() / 1000;
            String imageName = timestamp.toString() + ".jpg";

            mPath = Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORY
                    + File.separator + imageName;

            File newFile = new File(mPath);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newFile));
            startActivityForResult(intent, PHOTO_CODE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("file_path", mPath);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mPath = savedInstanceState.getString("file_path");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            switch (requestCode){
                case PHOTO_CODE:
                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });


                    Bitmap bitmap = BitmapFactory.decodeFile(mPath);
                    pathImage = BitMapToString(bitmap);
                    selectImage.setImageBitmap(bitmap);
                    //mSetImage.setImageBitmap(bitmap);
                    break;
                case SELECT_PICTURE:
                    Uri path = data.getData();
                    pathImage = path.toString();
                    selectImage.setImageURI(path);
                    //mSetImage.setImageURI(path);
                    break;

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == MY_PERMISSIONS){
            if(grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(DataUpdate.this, getString(R.string.permisosAceptados), Toast.LENGTH_SHORT).show();
                selectImage.setEnabled(true);
            }
        }else{
            showExplanation();
        }
    }

    private void showExplanation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DataUpdate.this);
        builder.setTitle(getString(R.string.permisosDenegados));
        builder.setMessage(getString(R.string.permisosNecesarios));
        builder.setPositiveButton(getString(R.string.modify), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        builder.show();
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public void mostrarDatos(){
        Uri uri;
        uri = Uri.parse(foto);
        selectImage.setImageURI(uri);
        etName.setText(nombres);
        etLastName.setText(apellidos);

        //Setear sexo
        if(sexo.equals("Male") || sexo.equals("Masculino")){
            r1.setChecked(true);
        }else if (sexo.equals("Female") || sexo.equals("Femenino")){
            r2.setChecked(true);
        }else{
            r3.setChecked(true);
        }
        fechaNacimiento.setText(fecha_nacimiento);
        etPhone.setText(telefono);
        etAddress.setText(direccion);
        etEmail.setText(email);
        etCity.setText(ciudad);
        etPass.setText(contraseña1);
        etPass2.setText(contraseña1);
    }
}