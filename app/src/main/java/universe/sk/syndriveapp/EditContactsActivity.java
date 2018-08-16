package universe.sk.syndriveapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class EditContactsActivity extends AppCompatActivity {

    ListView lvContactList;
    ArrayList<Contact> contacts;
    private static ContactAdapter adapter;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    private final int REQUEST_CONTACTS = 1;

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

        lvContactList = findViewById(R.id.lvContacts);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        contacts = new ArrayList<>();

        contacts.add(new Contact("Srividya", "+917736497532"));
        contacts.add(new Contact("Megha", "+918078906366"));
        contacts.add(new Contact("Suvarna", "+919074976560"));

        adapter = new ContactAdapter(contacts, getApplicationContext());
        lvContactList.setAdapter(adapter);

    } // end of onCreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.addcontacts, menu);
        //return true;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.add_contact) {
            // start contact picker intent
            Intent intent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
            intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
            startActivityForResult(intent, REQUEST_CONTACTS);
            //Toast.makeText(this, "Add Contact", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CONTACTS && resultCode == RESULT_OK) {
            //SharedPreferences.Editor edit = contactDetails.edit();
            Uri uri = data.getData();
            String names[] = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
            Cursor cursor = getContentResolver().query(uri, names, null, null, null);
            cursor.moveToFirst();
            int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            String name = cursor.getString(column);
            cursor.close();
            String numbers[] = {ContactsContract.CommonDataKinds.Phone.NUMBER};
            Cursor cursor1 = getContentResolver().query(uri, numbers, null, null, null);
            cursor1.moveToFirst();
            column = cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            String number = cursor1.getString(column);
            cursor1.close();

            contacts.add(new Contact(name, number));

//            edit.putString("Name: ", _name);
//            edit.apply();
//            edit.putString("Number: ", _number);
//            edit.apply();
        }
    } // end of onActivityResult

} // end of EditContactsActivity
