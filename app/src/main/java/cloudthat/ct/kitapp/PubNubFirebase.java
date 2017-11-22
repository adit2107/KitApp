package cloudthat.ct.kitapp;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.gson.JsonObject;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ChiragS on 14-11-2017.
 */

public class PubNubFirebase extends FirebaseInstanceIdService {
    public static final String PREFS_NAME = "PubNubUserFile";
//    SharedPreferences sharedPref = this.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
//    String pubKey = sharedPref.getString("pubkey","default");
//    String subKey = sharedPref.getString("subkey","default");
    String pubKey = "pub-c-beeb4ece-98da-49b7-97ac-1134d7ed285e";
    String subKey = "sub-c-e83ac06c-c88a-11e7-9628-f616d8b03518";

    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.i("FireBaseID", "Refreshed token: " + refreshedToken);

        try {
            JsonObject jsonToken = new JsonObject();
            jsonToken.addProperty("tokenJson", refreshedToken);
            Log.i("jsonToken", String.valueOf(jsonToken));
            sendTokenToPubNub(jsonToken);
        }catch(Exception e){
            Log.i("jsonToken", "Exception: " + e);
        }


        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
    }

    private void sendTokenToPubNub(JsonObject refreshedToken) {
        //String msg = "{registrationToken : " + refreshedToken + "}";
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setPublishKey(pubKey);
        pnConfiguration.setSubscribeKey(subKey);
        //Log.i("firepub",pubKey);
        PubNub pubnub = new PubNub(pnConfiguration);
        pubnub.publish().channel("test_channel3").message(refreshedToken).async(new PNCallback<PNPublishResult>() {
            @Override
            public void onResponse(PNPublishResult result, PNStatus status) {
                // Check whether request successfully completed or not.
                if (!status.isError()) {
                    Log.i("FireToken", String.valueOf(status.getStatusCode()));
                    Log.i("FireToken", status.getCategory().toString());
                    // Message successfully published to specified channel.
                }
                // Request processing failed.
                else {
                    Log.i("FireToken", String.valueOf(status.getStatusCode()));
                    Log.i("FireToken", status.getCategory().toString());
                    Log.i("FireToken", status.getErrorData().toString());
                    // Handle message publish error. Check 'category' property to find out possible issue
                    // because of which request did fail.
                    //
                    // Request can be resent using: [status retry];
                }
            }
        });
        //Send this data to a specific channel that will store the data in PubNub vault using PubNub functions
        //When a msg from DIY kit hits PubNub, a PubNub function will trigger
        //The triggered PubNub function will pull the registration token from the vault and send to FireBase
        //Firebase will then send the push notification to the app
    }
    }

