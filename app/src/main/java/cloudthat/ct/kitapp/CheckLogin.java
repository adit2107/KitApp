package cloudthat.ct.kitapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class CheckLogin extends Activity {

    public static final String PREFS_NAME = "PubNubUserFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPref = getBaseContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        final String username = sharedPref.getString("username","default");
        super.onCreate(savedInstanceState);
        Log.i("LoginAct","Check login activity");

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(2000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    if(username != "default"){
                        Intent intent = new Intent(CheckLogin.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        finish();
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(CheckLogin.this, PubNubLogin.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        finish();
                        startActivity(intent);
                    }
                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }


    }

