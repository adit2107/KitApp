package cloudthat.ct.kitapp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.content.Context;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.gson.JsonObject;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;


public class PubNubFirebase extends FirebaseInstanceIdService {
    public static final String PREFS_NAME = "PubNubUserFile";
    SharedPreferences sharedPref;
//    SharedPreferences sharedPref = getApplication().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
//    String pubKey = sharedPref.getString("pubkey","default");
//    String subKey = sharedPref.getString("subkey","default");
        String pubKey = "pub-c-beeb4ece-98da-49b7-97ac-1134d7ed285e";
        String subKey = "sub-c-e83ac06c-c88a-11e7-9628-f616d8b03518";

    public void onTokenRefresh() {
            // Get updated InstanceID token.
            //String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            //Log.i("FireBaseID", "Refreshed token: " + refreshedToken);
            sharedPref = getBaseContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            //String pubKey = sharedPref.getString("pubkey","default");
            //String subKey = sharedPref.getString("subkey","default");
            //String fcmtoken = sharedPref.getString("fcmtoken","notoken");
            Log.i("PreferencesFire", "subkey: " + subKey);
            Log.i("PreferencesFire", "pubkey: " + pubKey);
           //Log.i("PreferencesFire", "fcmtoken: " + fcmtoken);
            try {
                JsonObject jsonToken = new JsonObject();
                jsonToken.addProperty("tokenJson", FirebaseInstanceId.getInstance().getToken());
                Log.i("jsonToken", String.valueOf(jsonToken));
                sendTokenToPubNub(jsonToken, subKey, pubKey);
            } catch (Exception e) {
                Log.i("jsonToken", "Exception: " + e);
            }
    }
    private void sendTokenToPubNub(JsonObject token, String subKey, String pubKey) {
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setPublishKey(pubKey);
        pnConfiguration.setSubscribeKey(subKey);
        PubNub pubnub = new PubNub(pnConfiguration);
        Log.i("PreferencesFireSend", "subkey: " + subKey);
        Log.i("PreferencesFireSend", "pubkey: " + pubKey);
        //Log.i("jsonTokenSend", String.valueOf(FirebaseInstanceId.getInstance().getToken()));

            pubnub.publish().channel("test_channel3").message(token).async(new PNCallback<PNPublishResult>() {
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

