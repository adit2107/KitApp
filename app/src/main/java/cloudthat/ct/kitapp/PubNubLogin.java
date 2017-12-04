package cloudthat.ct.kitapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;

public class PubNubLogin extends AppCompatActivity {
    public static final String PREFS_NAME = "PubNubUserFile";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub_nub_login);

        sharedpreferences = getBaseContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        Button submitButton = findViewById(R.id.submitButton);
        final EditText pubKeyField = findViewById(R.id.pubField);
        final EditText subKeyField = findViewById(R.id.subField);
        final EditText usernameField = findViewById(R.id.nameField);

        final TextInputLayout usernameWrapper = (TextInputLayout) findViewById(R.id.usernameWrapper);
        final TextInputLayout subkeyWrapper = (TextInputLayout) findViewById(R.id.subKeyWrapper);
        final TextInputLayout pubkeyWrapper = (TextInputLayout) findViewById(R.id.pubKeyWrapper);

        usernameWrapper.setHint("Enter username");
        subkeyWrapper.setHint("Enter subscribe key");
        pubkeyWrapper.setHint("Enter publish key");

        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                Intent intent = new Intent(PubNubLogin.this, MainActivity.class);
                editor.putString("pubkey", String.valueOf(pubKeyField.getText()));
                editor.putString("subkey", String.valueOf(subKeyField.getText()));
                editor.putString("username", String.valueOf(usernameField.getText()));
                //editor.putString("fcmtoken", refreshedToken);
                Log.i("PreferencesLogin", "pubkey is " + String.valueOf(pubKeyField.getText()));
                Log.i("PreferencesLogin", "subkey is " + String.valueOf(subKeyField.getText()));
                Log.i("PreferencesLogin", "username is " + String.valueOf(usernameField.getText()));
                //Log.i("PreferencesLogin", "fcmtoken is " + refreshedToken);
                editor.apply();
                sendTokenToPubNub();
                Toast.makeText(PubNubLogin.this, "Logged in.",Toast.LENGTH_LONG).show();
                startActivity(intent);
                finish();
            }
        });
    }

    private void sendTokenToPubNub() {
        sharedpreferences = getBaseContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String pubKey = sharedpreferences.getString("pubkey","default");
        String subKey = sharedpreferences.getString("subkey","default");
        String fcmtoken = sharedpreferences.getString("fcmtoken","default");
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setPublishKey(pubKey);
        pnConfiguration.setSubscribeKey(subKey);
        PubNub pubnub = new PubNub(pnConfiguration);

        Log.i("PreferencesLogin", fcmtoken);

        pubnub.publish().channel("test_channel3").message(fcmtoken).async(new PNCallback<PNPublishResult>() {
            @Override
            public void onResponse(PNPublishResult result, PNStatus status) {
                // Check whether request successfully completed or not.
                if (!status.isError()) {
                    Log.i("FireToken", String.valueOf(status.getStatusCode()));
                    Log.i("FireToken", status.getCategory().toString());
                    // Message successfully published to specified channel.
                } else {
                    Log.i("FireToken", String.valueOf(status.getStatusCode()));
                    Log.i("FireToken", status.getCategory().toString());
                    Log.i("FireToken", status.getErrorData().toString());
                }
            }
        });
    }
}
