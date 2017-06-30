package com.everbright.wangyusen.contacs_app;


import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.TextView;

import com.github.privacystreams.core.exceptions.PSException;

import java.util.Calendar;

import static android.support.v4.content.ContextCompat.startActivity;


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
        public void testCalender(View view) throws PSException {
            TextView textView = (TextView) findViewById(R.id.textView);


            startActivity(Calender.addCalendar_event(2017,7,1,730,830,"wow"));
        }



}



