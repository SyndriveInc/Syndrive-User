package universe.sk.syndriveapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SendSMSActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private GPSTracker mGPSTracker;

    String etName, etName1, etNum1, etName2, etNum2, etName3, etNum3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms);

        mGPSTracker = new GPSTracker(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        storageReference = firebaseStorage.getReference();

        final DatabaseReference databaseReference = firebaseDatabase.getReference("users").child(firebaseAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Userinfo userinfo;
                userinfo = dataSnapshot.getValue(Userinfo.class);

                etName = userinfo.getUsername();
                etName1 = userinfo.getCname1();
                etName2 = userinfo.getCname2();
                etName3 = userinfo.getCname3();
                etNum1 = userinfo.getCnum1();
                etNum2 = userinfo.getCnum2();
                etNum3 = userinfo.getCnum3();
                sendSMSMessage();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SendSMSActivity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void sendSMSMessage(){
        String message = constructMessage();
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(etNum1,null,message,null,null);
        smsManager.sendTextMessage(etNum2,null,message,null,null);
        smsManager.sendTextMessage(etNum3,null,message,null,null);
    }

    private String constructMessage(){
        String message = "Alert! It appears that " + etName + " may have been in an accident. " + etName + " has chosen you as their emergency contact. ";
        return message;
    }
}
