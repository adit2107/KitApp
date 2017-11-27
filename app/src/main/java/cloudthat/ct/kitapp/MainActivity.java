package cloudthat.ct.kitapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import android.support.v7.widget.Toolbar;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.pubnub.api.*;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNPushType;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import com.pubnub.api.models.consumer.push.PNPushAddChannelResult;

import java.util.Arrays;

public class MainActivity extends Activity {
    public static final String PREFS_NAME = "PubNubUserFile";

    String refreshedToken = FirebaseInstanceId.getInstance().getToken();

    // importing Toolbar for nav drawer
    android.support.v7.widget.Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView nameDisp = findViewById(R.id.name);

        SharedPreferences sharedPref = this.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        String nameDisplay = sharedPref.getString("username","default");

        nameDisp.setText(nameDisplay);

        toolbar = findViewById(R.id.toolbarMain);

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

        PrimaryDrawerItem settings = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.drawer_item_settings).withIcon(FontAwesome.Icon.faw_pencil_square_o);
        PrimaryDrawerItem about  = new PrimaryDrawerItem().withIdentifier(2).withName(R.string.drawer_item_about).withIcon(FontAwesome.Icon.faw_info);

//create the drawer and remember the `Drawer` result object
        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult)
                .withToolbar(toolbar)
                .addDrawerItems(
                        settings,
                        about
                )
                .withSelectedItem(-1)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                      if(drawerItem != null){

                          Intent intent = null;

                          switch (position){
                              case 1:
                                  intent = new Intent(getApplicationContext(), EditLogin.class);
                                  break;
                              case 2:
                                  intent = new Intent(getApplicationContext(), AboutUs.class);
                          }
                          if(intent != null){
                              startActivity(intent);
                              finish();
                          }
                      }

                      return true;
                    }
                })
                .build();

        final ToggleButton toggle = (ToggleButton)findViewById(R.id.toggleButton3);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    Toast.makeText(MainActivity.this, "Desk activated.", Toast.LENGTH_SHORT).show();
                    // The toggle is enabled
                    Log.i("PubNub", "Checked!");
                    primeAlarm();

                } else {
                    // The toggle is disabled
                    Toast.makeText(MainActivity.this, "Desk deactivated.", Toast.LENGTH_SHORT).show();

                    Log.i("PubNub", "Unchecked!");
                    unPrimeAlarm();
                }
            }
        });

    }


    private void primeAlarm() {
        try {
            JsonObject primeObj = new JsonObject();
            primeObj.addProperty("state", "O");
            Log.i("primeObject", String.valueOf(primeObj));
            sendMessage(primeObj);
        }catch(Exception e){
            Log.i("primeObject", "Exception: " + e);
        }

    }

    private void unPrimeAlarm() {
        try {
            JsonObject unprimeObj = new JsonObject();
            unprimeObj.addProperty("state", "F");
            Log.i("primeObject", String.valueOf(unprimeObj));
            sendMessage(unprimeObj);
        }catch(Exception e){
            Log.i("primeObject", "Exception: " + e);
        }
    }

    private void sendMessage(JsonObject msgObj) { //not using configVar; will do that later
        PNConfiguration pnConfiguration = new PNConfiguration();
        SharedPreferences sharedPref = this.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Log.i("Preferences", "Bagged SharedPreferences File");
        String pubKey = sharedPref.getString("pubkey","default");
        String subKey = sharedPref.getString("subkey","default");
        Log.i("Preferences", "Pub Key from preferences: " + pubKey);
        Log.i("Preferences", "Sub Key from preferences: " + subKey);
        Log.i("Preferences", String.valueOf(sharedPref.getAll()));
        pnConfiguration.setPublishKey(pubKey);
        pnConfiguration.setSubscribeKey(subKey);
        PubNub pubnub = new PubNub(pnConfiguration);
        Log.i("PubNub", "PubNub Set up!");
        pubnub.publish().channel("test_channel2").message(msgObj).async(new PNCallback<PNPublishResult>() {
            @Override
            public void onResponse(PNPublishResult result, PNStatus status) {
                // Check whether request successfully completed or not.
                if (!status.isError()) {
                    Log.i("PubNub", String.valueOf(status.getStatusCode()));
                    Log.i("PubNub", status.getCategory().toString());
                    // Message successfully published to specified channel.
                }
                // Request processing failed.
                else {
                    Log.i("PubNub", String.valueOf(status.getStatusCode()));
                    Log.i("PubNub", status.getCategory().toString());
                    Log.i("PubNub", status.getErrorData().toString());
                    // Handle message publish error. Check 'category' property to find out possible issue
                    // because of which request did fail.
                    //
                    // Request can be resent using: [status retry];
                }
            }
        });

        pubnub.addListener(new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {
                if (status.getOperation() != null) {
                    switch (status.getOperation()) {
                        // let's combine unsubscribe and subscribe handling for ease of use
                        case PNSubscribeOperation:
                        case PNUnsubscribeOperation:
                            // note: subscribe statuses never have traditional
                            // errors, they just have categories to represent the
                            // different issues or successes that occur as part of subscribe
                            switch (status.getCategory()) {
                                case PNConnectedCategory:
                                    // this is expected for a subscribe, this means there is no error or issue whatsoever
                                case PNReconnectedCategory:
                                    // this usually occurs if subscribe temporarily fails but reconnects. This means
                                    // there was an error but there is no longer any issue
                                case PNDisconnectedCategory:
                                    // this is the expected category for an unsubscribe. This means there
                                    // was no error in unsubscribing from everything
                                case PNUnexpectedDisconnectCategory:
                                    // this is usually an issue with the internet connection, this is an error, handle appropriately
                                case PNAccessDeniedCategory:
                                    // this means that PAM does allow this client to subscribe to this
                                    // channel and channel group configuration. This is another explicit error
                                default:
                                    // More errors can be directly specified by creating explicit cases for other
                                    // error categories of `PNStatusCategory` such as `PNTimeoutCategory` or `PNMalformedFilterExpressionCategory` or `PNDecryptionErrorCategory`
                            }

                        case PNHeartbeatOperation:
                            // heartbeat operations can in fact have errors, so it is important to check first for an error.
                            // For more information on how to configure heartbeat notifications through the status
                            // PNObjectEventListener callback, consult <link to the PNCONFIGURATION heartbeart config>
                            if (status.isError()) {
                                // There was an error with the heartbeat operation, handle here
                            } else {
                                // heartbeat operation was successful
                            }
                        default: {
                            // Encountered unknown status type
                        }
                    }
                } else {
                    // After a reconnection see status.getCategory()
                }
            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
                if(message.getChannel() != null){
                    Log.i("pnmsg","Msg from pubnub:" + message.getMessage());
                }else{
                    Log.i("pnmsg", "pnmsg not recieved");
                }
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {

            }
        });

        pubnub.addPushNotificationsOnChannels()
                .pushType(PNPushType.GCM)
                .channels(Arrays.asList("test_channel"))
                .deviceId(refreshedToken)
                .async(new PNCallback<PNPushAddChannelResult>() {
                    @Override
                    public void onResponse(PNPushAddChannelResult result, PNStatus status) {
                        Log.i("PushResult","Status code:" + status.getStatusCode());
                    }
                });
        pubnub.subscribe().channels(Arrays.asList("test_channel")).execute();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}