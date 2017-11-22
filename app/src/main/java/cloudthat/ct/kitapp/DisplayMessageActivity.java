package cloudthat.ct.kitapp;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class DisplayMessageActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "PubNubUserFile"; //add this to strings.xml
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("Android", "Inside second intent");
        SharedPreferences sharedPref = this.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Log.i("Preferences", "Bagged SharedPreferences");
//        String storedKey = sharedPref.getAll().toString();
        String storedKey = sharedPref.getString("pubkey","default");
        Log.i("Preferences", storedKey);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);
        TextView textView = findViewById(R.id.textView2);
        textView.setText(storedKey);
    }
}
