package universe.sk.syndriveapp;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class EditContactsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contacts);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.drawable.contacts);
        actionBar.setTitle(" Emergency Contacts");
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

    } // end of onCreate
} // end of EditContactsActivityActivity
