package com.everbright.wangyusen.contacs_app;


import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.TextView;

import com.github.privacystreams.core.exceptions.PSException;


public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

        /** Called when the user taps the Send button */
        public void sendMessage(View view) throws PSException, RemoteException, OperationApplicationException {
            TextView textView = (TextView) findViewById(R.id.textView);
            Contacts s = new Contacts(getApplicationContext());

            s.getContacts_name();
            s.getContacts_id();

            s.updateContactsEmail(getApplicationContext(), "6", "success@");
            s.updateContactsPhone(getApplicationContext(),"6", "13613215058");
            textView.setText(s.getDisplayName()+s.getDisplayID()  );



        }

}



