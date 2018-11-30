package hu.bme.aut.whiskeyjudge.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import hu.bme.aut.whiskeyjudge.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText etEmailAddress = findViewById(R.id.etEmailAddress);                          //references for login page
        final EditText etPassword = findViewById(R.id.etPassword);
        final Button btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {                                    //login button
            @Override
            public void onClick(final View view) {
                if (etEmailAddress.getText().toString().isEmpty()) {
                    etEmailAddress.requestFocus();
                    etEmailAddress.setError("Please enter your email address");
                    return;
                }

                if (etPassword.getText().toString().isEmpty()) {
                    etPassword.requestFocus();
                    etPassword.setError("Please enter your password");
                    return;
                }
                Log.d("LoginActivity", etEmailAddress.getText().toString() + " has logged in successfully");

                Intent intent = new Intent(LoginActivity.this, ListActivity.class);   //Login button navigate to the mainActivity
                startActivity(intent);
            }

        });


    }
}
