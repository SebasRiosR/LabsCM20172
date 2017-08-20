package co.edu.udea.compumovil.gr01_20172.lab1.Activities;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import co.edu.udea.compumovil.gr01_20172.lab1.Helpers.User;
import co.edu.udea.compumovil.gr01_20172.lab1.SQL.DbHelper;
import co.edu.udea.compumovil.gr01_20172.lab1.R;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private Button reg;
    private TextView tvLogin;
    private EditText etEmail, etPass, etPass2, etName, etLastName, etPhone, etAddress;
    private AutoCompleteTextView etCity;
    private RadioGroup radioGroup;
    private RadioButton r1, r2, r3;
    private DbHelper db;
    private User user = new User();
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
        reg = (Button)findViewById(R.id.btnReg);
        tvLogin = (TextView)findViewById(R.id.tvLogin);
        etEmail = (EditText)findViewById(R.id.etEmail);
        etName = (EditText)findViewById(R.id.etName);
        etPass = (EditText)findViewById(R.id.etPass);
        etPass2 = (EditText)findViewById(R.id.etPass2);
        etLastName = (EditText)findViewById(R.id.etLastName);
        etPhone = (EditText)findViewById(R.id.etPhone);
        etAddress = (EditText)findViewById(R.id.etAddress);
        etCity = (AutoCompleteTextView) findViewById(R.id.etCity);
        radioGroup = (RadioGroup)findViewById(R.id.groupB);
        reg.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
        r1 = (RadioButton)findViewById(R.id.radioB1);
        r2 = (RadioButton)findViewById(R.id.radioB2);
        r3 = (RadioButton)findViewById(R.id.radioB3);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ciudades);
        etCity.setAdapter(adapter);
        etCity.setThreshold(1);
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
        String correo = etEmail.getText().toString();
        String contraseña = etPass.getText().toString();
        String nombres = etName.getText().toString();
        String apellidos = etLastName.getText().toString();
        String telefono = etPhone.getText().toString();
        String direccion = etAddress.getText().toString();
        String ciudad = etCity.getText().toString();
        if(correo.isEmpty() || contraseña.isEmpty() || nombres.isEmpty() || apellidos.isEmpty()
                || telefono.isEmpty() || direccion.isEmpty() || ciudad.isEmpty() || (!r1.isChecked()
                && !r2.isChecked() && !r3.isChecked())){
            displayToast(getString(R.string.error1));
        } else if (!(contraseña.equals(etPass2.getText().toString()))){
            displayToast(getString(R.string.error2));
            etPass.setText("");
            etPass2.setText("");
        } else {
            String sex = this.findViewById(radioGroup.getCheckedRadioButtonId()).toString();
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
}