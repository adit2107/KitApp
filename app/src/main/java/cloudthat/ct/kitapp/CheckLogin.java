package cloudthat.ct.kitapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class CheckLogin extends AppCompatActivity {

    public static final String PREFS_NAME = "PubNubUserFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPref = this.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String username = sharedPref.getString("username","default");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_login);

        Log.i("LoginAct","Check login activity");

        if(username != "default"){
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(intent);
        }else{
            Intent intent = new Intent(this, PubNubLogin.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(intent);
        }
    }
}
