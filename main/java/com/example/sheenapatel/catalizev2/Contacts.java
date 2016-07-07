package com.example.sheenapatel.catalizev2;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.content.ContentResolver;
import android.net.Uri;
import android.provider.ContactsContract;
import android.widget.ListView;
import android.database.Cursor;
import android.util.Log;

public class Contacts extends Activity {

    public ListView outputList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contacts);
//call the main layout from xml
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.contacts_layout_id);

        View view = getLayoutInflater().inflate(R.layout.contacts_list, mainLayout, false);

        mainLayout.addView(view);

        Toast.makeText(getApplicationContext(), "Choose two people and make an introduction", Toast.LENGTH_LONG).show();

        Bundle bundle = getIntent().getExtras();
        String personName = bundle.getString("name_value"); //gets name and email of the person from Login Screen
        String personEmail = bundle.getString("email_value");

        findViewById(R.id.listView);
        fetchContacts();

    }

    public void fetchContacts(){
        String phoneNumber = null;
        String email = null;

        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

        Uri EmailCONTENT_URI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        String EMAILCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
        String DATA = ContactsContract.CommonDataKinds.Email.DATA;

        StringBuffer output = new StringBuffer();

        ContentResolver contentResolver = getContentResolver();

        Cursor cursor = contentResolver.query(CONTENT_URI, null, null, null, null);

        //loop through contacts
        if (cursor.getCount() > 0){
            while (cursor.moveToNext()) {
                String contact_id = cursor.getString(cursor.getColumnIndex(ID));
                String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));

                if (hasPhoneNumber > 0) {
                    output.append("\n Name" + name);

                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[]{contact_id}, null);
                    while (phoneCursor.moveToNext()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        output.append("\nPhone" + phoneNumber);
                    }

                    phoneCursor.close();

                    Cursor emailCursor = contentResolver.query(EmailCONTENT_URI, null, EMAILCONTACT_ID + " = ?", new String[]{contact_id}, null);

                    while (emailCursor.moveToNext()) {
                        email = emailCursor.getString(emailCursor.getColumnIndex(DATA));
                        output.append("\nEmail" + email);
                    }
                    emailCursor.close();
                }
                output.append("\n");
            }



        }

         Log.d("This is the output", output.toString());
    }
}

