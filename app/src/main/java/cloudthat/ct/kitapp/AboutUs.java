package cloudthat.ct.kitapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.ctlogo)
                .setDescription("Incorporated in March 2012, CloudThat is the first company in India to provide Cloud Training and Consulting services for mid market and enterprise clients around the world. With expertise in major Cloud platforms including Amazon Web Services and Microsoft Azure, CloudThat is uniquely positioned to be the single technology source for organizations looking to utilize the flexibility and power Cloud Computing provides.")
                .addItem(new Element().setTitle("Version 1.0.0"))
                .addWebsite("https://cloudthat.in/")
                .addPlayStore("")
                .create();

        setContentView(aboutPage);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AboutUs.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
