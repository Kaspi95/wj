package hu.bme.aut.whiskeyjudge;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {                                                       //delay
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        setTheme(R.style.AppTheme);                                 //set back to main theme

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}
