package examprojectapp.ionep.com.examprojectapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    EditText username,password;
    Button login;

    String usernameTxt,passwordTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username=(EditText)findViewById(R.id.username);
        password=(EditText)findViewById(R.id.password);
        login=(Button)findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameTxt=username.getText().toString();
                passwordTxt=password.getText().toString();


                if(usernameTxt.equals("admin") && passwordTxt.equals("admin"))
                {
                    startActivity(new Intent(Login.this,BTConnect.class));
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Wrong Username or Password",Toast.LENGTH_SHORT).show();
                }
                username.setText("");
                password.setText("");
            }
        });
    }
}
