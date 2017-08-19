package co.edu.udea.compumovil.gr01_20172.lab1.Activities;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
    private EditText etEmail, etPass, etPass2, etName, etLastName, etPhone, etAddress, etCity;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private DbHelper db;
    private User user = new User();

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
        etCity = (EditText)findViewById(R.id.etCity);
        radioGroup = (RadioGroup)findViewById(R.id.groupB);
        reg.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
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

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    private void register(){
        String sex = this.findViewById(radioGroup.getCheckedRadioButtonId()).toString();
        if(etEmail.getText().toString().isEmpty() && etPass.getText().toString().isEmpty()){
            displayToast("Username/password field empty");
        } else if (!(etPass.getText().toString().equals(etPass2.getText().toString()))){
            displayToast("Username/las contraseñas no coinciden");
            etPass.setText("");
            etPass2.setText("");
        } else {
            user.setCorreo(etEmail.getText().toString());
            user.setContraseña(etPass.getText().toString());
            user.setNombres(etName.getText().toString());
            user.setApellidos(etLastName.getText().toString());
            user.setTelefono(etPhone.getText().toString());
            user.setDireccion(etAddress.getText().toString());
            user.setCiudad(etCity.getText().toString());
            if (sex.isEmpty()){
                user.setSexo(sex);
            } else {
                user.setSexo("");
            }
            db.addUser(user);
            displayToast("User registered");
            finish();
        }
    }

    private void displayToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}