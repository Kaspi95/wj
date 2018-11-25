package hu.bme.aut.whiskeyjudge;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*
        try {                                                                                       //delay, TODO: remove this when u ready
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Theme(R.style.AppTheme);                                                                  //set back to main theme, but need only after the login page
        */
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

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);   //Login button navigate to the mainActivity
                startActivity(intent);
            }

        });


    }
}
