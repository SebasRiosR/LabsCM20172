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
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;

import co.edu.udea.compumovil.gr01_20172.lab1.Helpers.InputValidation;
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
    private DatePickerDialog datePickerDialog;
    private String mPath = "";

    private Uri path;
    private TextView fechaNacimiento;
    private ImageButton selectImage;
    private Button reg;
    private Button selectBirthDate;
    private TextView tvLogin;
    private TextInputEditText etEmail, etPass, etPass2, etName, etLastName, etPhone, etAddress;
    private AutoCompleteTextView etCity;
    private RadioGroup radioGroup;
    private RadioButton r1, r2, r3;
    private RadioButton radioButton;
    private DbHelper db;
    private User user = new User();
    private InputValidation inputValidation;
    private TextInputLayout textInputLayoutEmail, textInputLayoutName, textInputLayoutLastName, textInputLayoutPhone,
                            textInputLayoutAddress, textInputLayoutCity, textInputLayoutPass, textInputLayoutPass2,
                            textInputLayoutSex, textInputLayoutDate, textInputLayoutImage;

    int permisos = 0;
    private String pathImage = "android.resource://co.edu.udea.compumovil.gr01_20172.lab1/drawable/ic_photo";
            //"drawable://" + R.drawable.ic_photo;
    private String[] ciudades={"Medellín", "Arauca", "Barranquilla", "Cartagena", "Tunja",
            "Manizales", "Florencia", "Yopal", "Popayán", "Valledupar", "Quibdó", "Montería",
            "Bogotá", "Puerto Inírida", "Guaviare", "Neiva", "Riohacha", "Santa Marta",
            "Villavicencio", "Pasto", "Cúcuta", "Mocoa", "Armenia", "Pereira", "San Andrés",
            "Bucaramanga", "Sincelejo", "Ibagué", "Cali", "Mitú", "Puerto Carreño"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        clView = (CoordinatorLayout) findViewById(R.id.cl_view);
        fechaNacimiento = (TextView)findViewById(R.id.fechaString);
        selectBirthDate = (Button)findViewById(R.id.botonCalendario);
        selectImage = (ImageButton)findViewById(R.id.buttonImage);
        reg = (Button)findViewById(R.id.btnReg);
        tvLogin = (TextView)findViewById(R.id.tvLogin);
        etEmail = (TextInputEditText) findViewById(R.id.etEmail);
        etName = (TextInputEditText)findViewById(R.id.etName);
        etPass = (TextInputEditText)findViewById(R.id.etPass);
        etPass2 = (TextInputEditText)findViewById(R.id.etPass2);
        etLastName = (TextInputEditText)findViewById(R.id.etLastName);
        etPhone = (TextInputEditText)findViewById(R.id.etPhone);
        etAddress = (TextInputEditText)findViewById(R.id.etAddress);
        etCity = (AutoCompleteTextView)findViewById(R.id.etCity);
        radioGroup = (RadioGroup)findViewById(R.id.groupB);
        reg.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
        r1 = (RadioButton)findViewById(R.id.radioB1);
        r2 = (RadioButton)findViewById(R.id.radioB2);
        r3 = (RadioButton)findViewById(R.id.radioB3);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutName = (TextInputLayout) findViewById(R.id.textInputLayoutName);
        textInputLayoutLastName = (TextInputLayout) findViewById(R.id.textInputLayoutLastName);
        textInputLayoutPhone = (TextInputLayout) findViewById(R.id.textInputLayoutPhone);
        textInputLayoutAddress = (TextInputLayout) findViewById(R.id.textInputLayoutAddress);
        textInputLayoutCity = (TextInputLayout) findViewById(R.id.textInputLayoutCity);
        textInputLayoutSex = (TextInputLayout) findViewById(R.id.textInputLayoutSex);
        textInputLayoutDate = (TextInputLayout) findViewById(R.id.textInputLayoutDate);
        textInputLayoutPass =  (TextInputLayout) findViewById(R.id.textInputLayoutPass);
        textInputLayoutPass2 =  (TextInputLayout) findViewById(R.id.textInputLayoutPass2);
        textInputLayoutImage =  (TextInputLayout) findViewById(R.id.textInputLayoutImage);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ciudades);
        etCity.setAdapter(adapter);
        etCity.setThreshold(1);

        if (!mayRequestStoragePermission()){
            permisos=0;
        }

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mayRequestStoragePermission()){ //Si no hay permisos muesta la explicación
                    showExplanation();
                }else {
                    showOptions();
                }
            }
        });

        // perform click event on edit text
        selectBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                c.setTimeInMillis(System.currentTimeMillis());
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(RegisterActivity.this, R.style.DialogTheme,
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
        initObjects();
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

    private void initObjects() {
        db = new DbHelper(this);
        inputValidation = new InputValidation(this);

    }

    private void register(){
        if(mayRequestStoragePermission()) {
            String fechaNac = fechaNacimiento.getText().toString();
            String imagen = pathImage;
            String correo = etEmail.getText().toString();
            String contraseña = etPass.getText().toString();
            String contraseña2 = etPass2.getText().toString();
            String nombres = etName.getText().toString();
            String apellidos = etLastName.getText().toString();
            String telefono = etPhone.getText().toString();
            String direccion = etAddress.getText().toString();
            String ciudad = etCity.getText().toString();
            int errores = 0;
            if(fechaNac.isEmpty()){
                textInputLayoutDate.setError(getString(R.string.error1));
                errores++;
            }
            if (imagen.equals("android.resource://co.edu.udea.compumovil.gr01_20172.lab1/drawable/ic_photo")){
                textInputLayoutImage.setError(getString(R.string.error1));
                errores++;
            }
            if (correo.isEmpty()) {
                textInputLayoutEmail.setError(getString(R.string.error1));
                inputValidation.hideKeyboardFrom(etEmail);
                errores++;
            } else if (!inputValidation.isInputEditTextEmail(etEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
                errores++;
            }
            if (contraseña.isEmpty()){
                Log.d("TAG", "Entré");
                textInputLayoutPass.setError(getString(R.string.error1));
                inputValidation.hideKeyboardFrom(etPass);
                errores++;
            }
            if (contraseña2.isEmpty()) {
                Log.d("TAG", "Entré2");
                textInputLayoutPass2.setError(getString(R.string.error1));
                inputValidation.hideKeyboardFrom(etPass2);
                errores++;
            }
            if (nombres.isEmpty()) {
                Log.d("TAG", "Entré3");
                textInputLayoutName.setError(getString(R.string.error1));
                inputValidation.hideKeyboardFrom(etName);
                errores++;
            }
            if (apellidos.isEmpty()) {
                textInputLayoutLastName.setError(getString(R.string.error1));
                inputValidation.hideKeyboardFrom(etLastName);
                errores++;
            }
            if (telefono.isEmpty()) {
                textInputLayoutPhone.setError(getString(R.string.error1));
                inputValidation.hideKeyboardFrom(etPhone);
                errores++;
            }
            if (direccion.isEmpty()) {
                textInputLayoutAddress.setError(getString(R.string.error1));
                inputValidation.hideKeyboardFrom(etAddress);
                errores++;
            }
            if (ciudad.isEmpty()) {
                textInputLayoutCity.setError(getString(R.string.error1));
                inputValidation.hideKeyboardFrom(etCity);
                errores++;
            }
            if (!r1.isChecked() && !r2.isChecked() && !r3.isChecked()){
                textInputLayoutSex.setError(getString(R.string.error1));
                errores++;
            }
            if (!contraseña.equals(contraseña2)){
                displayToast(getString(R.string.error2));
                etPass.setText("");
                etPass2.setText("");
                errores++;
            }
            if(db.getUserEmail(correo)){
                displayToast(getString(R.string.error4));
                errores++;
            }
            if (errores > 0){
                return;
            }else {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selectedId);
                String sex = radioButton.getText().toString();
                user.setFechaNacimiento(fechaNac);
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
        final AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
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
        outState.putString("file_path", pathImage);
        outState.putString("fecha_nacimiento", fechaNacimiento.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        String uriString = savedInstanceState.getString("file_path");
        //displayToast(uriString);
        Uri uri = Uri.parse(uriString);
        selectImage.setImageURI(uri);
        fechaNacimiento.setText(savedInstanceState.getString("fecha_nacimiento"));
        pathImage = savedInstanceState.getString("file_path");
        super.onRestoreInstanceState(savedInstanceState);

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
                    path = data.getData();
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
                Toast.makeText(RegisterActivity.this, getString(R.string.permisosAceptados), Toast.LENGTH_SHORT).show();
                selectImage.setEnabled(true);
            }
        }else{
            showExplanation();
        }
    }

    private void showExplanation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this,R.style.MyDialogStyle);
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
}