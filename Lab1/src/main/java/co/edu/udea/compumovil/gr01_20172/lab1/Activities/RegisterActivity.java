package co.edu.udea.compumovil.gr01_20172.lab1.Activities;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;

import co.edu.udea.compumovil.gr01_20172.lab1.Helpers.User;
import co.edu.udea.compumovil.gr01_20172.lab1.SQL.DbHelper;
import co.edu.udea.compumovil.gr01_20172.lab1.R;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private static String APP_DIRECTORY = "MyPictureApp/";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "PictureApp";

    private final int MY_PERMISSIONS = 100;
    private final int PHOTO_CODE = 200;
    private final int SELECT_PICTURE = 300;

    private CoordinatorLayout clView;
    private String mPath;

    private ImageButton selectImage;
    private Button reg;
    private TextView tvLogin;
    private EditText etEmail, etPass, etPass2, etName, etLastName, etPhone, etAddress, etCity;
    private RadioGroup radioGroup;
    private RadioButton r1, r2, r3;
    private DbHelper db;
    private User user = new User();
    private String pathImage;
    private String[] ciudades={"Medellín", "Arauca", "Barranquilla", "Cartagena", "Tunja",
            "Manizales", "Florencia", "Yopal", "Popayán", "Valledupar", "Quibdó", "Montería",
            "Bogotá", "Puerto Inírida", "Guaviare", "Neiva", "Riohacha", "Santa Marta",
            "Villavicencio", "Pasto", "Cúcuta", "Mocoa", "Armenia", "Pereira", "San Andrés",
            "Bucaramanga", "Sincelejo", "Ibagué", "Cali", "Mitú", "Puerto Carreño"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DbHelper(this);
        clView = (CoordinatorLayout) findViewById(R.id.cl_view);
        selectImage = (ImageButton)findViewById(R.id.buttonImage);
        reg = (Button)findViewById(R.id.btnReg);
        tvLogin = (TextView)findViewById(R.id.tvLogin);
        etEmail = (EditText)findViewById(R.id.etEmail);
        etName = (EditText)findViewById(R.id.etName);
        etPass = (EditText)findViewById(R.id.etPass);
        etPass2 = (EditText)findViewById(R.id.etPass2);
        etLastName = (EditText)findViewById(R.id.etLastName);
        etPhone = (EditText)findViewById(R.id.etPhone);
        etAddress = (EditText)findViewById(R.id.etAddress);
        etCity = (EditText)findViewById(R.id.etCity);
        radioGroup = (RadioGroup)findViewById(R.id.groupB);
        reg.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
        r1 = (RadioButton)findViewById(R.id.radioB1);
        r2 = (RadioButton)findViewById(R.id.radioB2);
        r3 = (RadioButton)findViewById(R.id.radioB3);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ciudades);
        etCity.setAdapter(adapter);
        etCity.setThreshold(1);

        if(mayRequestStoragePermission())
            selectImage.setEnabled(true);
        else
            selectImage.setEnabled(false);


        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptions();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnReg:
                register();
                break;
            case R.id.tvLogin:
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                finish();
                break;
            default:

        }
    }

    private void register(){
        String imagen = pathImage;
        String correo = etEmail.getText().toString();
        String contraseña = etPass.getText().toString();
        String nombres = etName.getText().toString();
        String apellidos = etLastName.getText().toString();
        String telefono = etPhone.getText().toString();
        String direccion = etAddress.getText().toString();
        String ciudad = etCity.getText().toString();
        if(imagen.isEmpty() || correo.isEmpty() || contraseña.isEmpty() || nombres.isEmpty() || apellidos.isEmpty()
                || telefono.isEmpty() || direccion.isEmpty() || ciudad.isEmpty() || (!r1.isChecked()
                && !r2.isChecked() && !r3.isChecked())){
            displayToast(getString(R.string.error1));
        } else if (!(contraseña.equals(etPass2.getText().toString()))){
            displayToast(getString(R.string.error2));
            etPass.setText("");
            etPass2.setText("");
        } else {
            String sex = this.findViewById(radioGroup.getCheckedRadioButtonId()).toString();
            user.setImagen(imagen);
            user.setCorreo(correo);
            user.setContraseña(contraseña);
            user.setNombres(nombres);
            user.setApellidos(apellidos);
            user.setTelefono(telefono);
            user.setDireccion(direccion);
            user.setCiudad(ciudad);
            user.setSexo(sex);
            db.addUser(user);
            displayToast(getString(R.string.confirmacion));
            finish();
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
            Snackbar.make(clView, "Los permisos son necesarios para poder usar la aplicación",
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
        final CharSequence[] option = {"Tomar foto", "Elegir de galeria", "Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("Eleige una opción");
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(option[which] == "Tomar foto"){
                    openCamera();
                }else if(option[which] == "Elegir de galeria"){
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent, "Selecciona app de imagen"), SELECT_PICTURE);
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
                Toast.makeText(RegisterActivity.this, "Permisos aceptados", Toast.LENGTH_SHORT).show();
                selectImage.setEnabled(true);
            }
        }else{
            showExplanation();
        }
    }

    private void showExplanation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("Permisos denegados");
        builder.setMessage("Para usar las funciones de la app necesitas aceptar los permisos");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
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
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

}