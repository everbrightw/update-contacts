package com.everbright.wangyusen.contacs_app;


import android.app.Activity;
import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;
import android.util.Log;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.TextView;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.core.UQI;
import com.github.privacystreams.core.exceptions.PSException;
import com.github.privacystreams.core.purposes.Purpose;
import com.github.privacystreams.communication.Contact;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wangyusen on 6/17/17.
 */

public class Contacts {

    private String displayName;
    private String displayID;
    private String displayEmail;
    private String displayPhone;
    private String displayTime;

    private UQI uqi;
    private Purpose purpose;

    Contacts(Context context){
        this.purpose = Purpose.TEST("test");
        this.uqi = new UQI(context);
        displayName = "no contacts";
        displayID = "no contacts";
        displayEmail = "no contacts";
        displayPhone = "no contacts";
        displayTime = "no contacts";

    }
    public String getDisplay() {
        return displayName;
    }

    public String getDisplayEmail() {
        return displayEmail;
    }

    public String getDisplayID() {
        return displayID;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDisplayPhone() {
        return displayPhone;
    }

    public String getDisplayTime() {
        return displayTime;
    }


    /*
        get all contacts name and store them in the displayName

     */
    public void getContacts_name(){
        try {
            //convert the raw data to List<Item>
            List<Item> temp = uqi.getData(Contact.getAll(), purpose).asList();
            if(!temp.isEmpty()){
                //check if the list is empty, if it is empty, the default value is "no contacts"
                displayName = "name: \n";
            }
            for(int i = 0; i < temp.size();i++) {
                //aquire each elment's value in the list
                displayName += temp.get(i).getValueByField("name") + "  \n";
            }
        } catch (PSException e) {
            e.printStackTrace();
        }
    }
    /*
       get all contacts time and store them in the displayTime

    */
    public void getContacts_time(){
        try {
            //convert the raw data to List<Item>
            List<Item> temp = uqi.getData(Contact.getAll(), purpose).asList();

            if(!temp.isEmpty()){
                //check if the list is empty, if it is empty, the default value is "no contacts"
                displayTime = "time_created:  \n";// this line is for the convenience of testing
            }

            for(int i = 0; i < temp.size();i++) {
                //aquire each elment's value in the list
                displayTime += temp.get(i).getValueByField("time_created") + "  \n";
            }

        } catch (PSException e) {
            e.printStackTrace();
                                }
    }

    /*
     get all contacts ID  and store them in the displayID

    */
    public void getContacts_id(){
        try {
            //convert the raw data to List<Item>
            List<Item> temp = uqi.getData(Contact.getAll(), purpose).asList();

            if(!temp.isEmpty()){
                //check if the list is empty, if it is empty, the default value is "no contacts"
                displayID = "id:  \n";// this line is for the convenience of testing
            }

            for(int i = 0; i < temp.size();i++) {
                //acquire each element's value in the list
                displayID += temp.get(i).getValueByField("id") + "  \n";
            }

        } catch (PSException e) {
            e.printStackTrace();
        }
    }

    /*
     get all contacts phones  and store them in the displayPhone
    */

    public void getContacts_phones(){
        try {
            //convert the raw data to List<Item>
            List<Item> temp = uqi.getData(Contact.getAll(), purpose).asList();

            if(!temp.isEmpty()){
                //check if the list is empty, if it is empty, the default value is "no contacts"
                displayID = "phones:  \n";// this line is for the convenience of testing
            }

            for(int i = 0; i < temp.size();i++) {
                //acquire each element's value in the list
                displayID += temp.get(i).getValueByField("phones") + "  \n";
            }

        } catch (PSException e) {
            e.printStackTrace();
        }
    }


    // update contacts helper;
    private void updateContacts_helper(Context context,String oldname, String phone, String email) {
        Cursor cursor = context.getContentResolver().query(Data.CONTENT_URI,new String[] { Data.RAW_CONTACT_ID },
                ContactsContract.Contacts.DISPLAY_NAME + "=?",new String[] { oldname }, null);
        cursor.moveToFirst();
        String id = cursor.getString(cursor.getColumnIndex(Data.RAW_CONTACT_ID));
        cursor.close();
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

//更新电话
        ops.add(ContentProviderOperation.newUpdate(Data.CONTENT_URI)
                .withSelection(
                        Data.RAW_CONTACT_ID + "=?" + " AND "+ Data.MIMETYPE + " = ?" +
                                " AND " + Phone.TYPE + "=?",new String[] { String.valueOf(id), Phone.CONTENT_ITEM_TYPE,
                                String.valueOf(Phone.TYPE_HOME) }).withValue(Phone.NUMBER, phone).build());
// 更新email
       ops.add(ContentProviderOperation.newUpdate(Data.CONTENT_URI)
                .withSelection(Data.RAW_CONTACT_ID + "=?" + " AND "+ Data.MIMETYPE + " = ?" +" AND " + ContactsContract.CommonDataKinds.Email.TYPE + "=?",new String[] { String.valueOf(id), ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE,
                        String.valueOf(ContactsContract.CommonDataKinds.Email.TYPE_HOME) }).withValue(ContactsContract.CommonDataKinds.Email.DATA, email).build());
        try{
            context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (Exception e) {
        }
    }


   public void updateContactsEmail(Context context, String id, String email) throws PSException {

       String original_name =//get the original name
               uqi.getData(Contact.getAll(), purpose).filter("id", id).getItemAt(0).getField("name");

       List<String> original_phones =//get the original phone number
               uqi.getData(Contact.getAll(), purpose).filter("id", id).getItemAt(0).getField("phones");

       updateContacts_helper(context, original_name, original_phones.get(0), email);

    }
    public void updateContactsPhone(Context context, String id, String phone) throws PSException {
            String original_name =
                    uqi.getData(Contact.getAll(), purpose).filter("id", id).getItemAt(0).getField("name");

            List<String> original_email =
                    uqi.getData(Contact.getAll(), purpose).filter("id", id).getItemAt(0).getField("emails");

            updateContacts_helper(context, original_name, phone, original_email.get(0));
        
    }





    public void delContact(Context context, String name) {
        Cursor cursor = context.getContentResolver().query(Data.CONTENT_URI,new String[] { Data.RAW_CONTACT_ID },
                ContactsContract.Contacts.DISPLAY_NAME + "=?",new String[] { name }, null);
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        if (cursor.moveToFirst()) {
            do {
                long Id = cursor.getLong(cursor.getColumnIndex(Data.RAW_CONTACT_ID));
                ops.add(ContentProviderOperation.newDelete(
                        ContentUris.withAppendedId(ContactsContract.RawContacts.CONTENT_URI,Id)).build());
                try {
                    context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                }
                catch (Exception e){}
            } while (cursor.moveToNext());
            cursor.close();
        }
    }
    public void addContact(Context context, String name,
                           String organisation,String phone, String fax, String email, String address,String website){
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
//在名片表插入一个新名片
        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null).withValue(ContactsContract.RawContacts._ID, 0).withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).withValue(
                        ContactsContract.RawContacts.AGGREGATION_MODE,ContactsContract.RawContacts.AGGREGATION_MODE_DISABLED).build());
// add name
//添加一条新名字记录；对应RAW_CONTACT_ID为0的名片
        if (!name.equals("")) {
            ops.add(ContentProviderOperation.newInsert(Data.CONTENT_URI)
                    .withValueBackReference(Data.RAW_CONTACT_ID, 0).withValue(
                            Data.MIMETYPE,ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE).withValue(
                            ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,name).build());
        }
//添加昵称
        ops.add(ContentProviderOperation.newInsert(Data.CONTENT_URI)
                .withValueBackReference(Data.RAW_CONTACT_ID, 0).withValue(
                        Data.MIMETYPE,ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE).withValue(ContactsContract.CommonDataKinds.Nickname.NAME,"Anson昵称").build());
// add company
        if (!organisation.equals("")) {
            ops.add(ContentProviderOperation.newInsert(Data.CONTENT_URI).withValueBackReference(Data.RAW_CONTACT_ID, 0).withValue(Data.MIMETYPE,
                    Organization.CONTENT_ITEM_TYPE).withValue(
                    Organization.COMPANY,organisation).withValue(
                    Organization.TYPE, Organization.TYPE_WORK).build());
        }
// add phone
        if (!phone.equals("")) {
            ops.add(ContentProviderOperation.newInsert(Data.CONTENT_URI)
                    .withValueBackReference(Data.RAW_CONTACT_ID, 0)
                    .withValue(Data.MIMETYPE,
                            Phone.CONTENT_ITEM_TYPE).withValue(Phone.NUMBER,phone).withValue(Phone.TYPE,1).build());
        }
// add Fax
        if (!fax.equals("")) {
            ops.add(ContentProviderOperation.newInsert(Data.CONTENT_URI).withValueBackReference(
                    Data.RAW_CONTACT_ID, 0).withValue(
                    Data.MIMETYPE,
                    Phone.CONTENT_ITEM_TYPE).withValue(
                    Phone.NUMBER,fax)
                    .withValue(Phone.TYPE,
                            Phone.TYPE_FAX_WORK).build());
        }
// add email
        if (!email.equals("")) {
            ops.add(ContentProviderOperation.newInsert(Data.CONTENT_URI)
                    .withValueBackReference(
                            Data.RAW_CONTACT_ID, 0).withValue(
                            Data.MIMETYPE,ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE).withValue(ContactsContract.CommonDataKinds.Email.DATA,email).withValue(ContactsContract.CommonDataKinds.Email.TYPE,1).build());
        }
// add address
        if (!address.equals("")) {
            ops.add(ContentProviderOperation.newInsert(Data.CONTENT_URI).withValueBackReference(Data.RAW_CONTACT_ID, 0).withValue(
                    Data.MIMETYPE,ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE).withValue(
                    ContactsContract.CommonDataKinds.StructuredPostal.STREET,address)
                    .withValue(ContactsContract.CommonDataKinds.StructuredPostal.TYPE,
                            ContactsContract.CommonDataKinds.StructuredPostal.TYPE_WORK).build());
        }
// add website
        if (!website.equals("")) {
            ops.add(ContentProviderOperation.newInsert(Data.CONTENT_URI)
                    .withValueBackReference(Data.RAW_CONTACT_ID, 0)
                    .withValue(Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE).withValue(
                            ContactsContract.CommonDataKinds.Website.URL,website)
                    .withValue(
                            ContactsContract.CommonDataKinds.Website.TYPE,
                            ContactsContract.CommonDataKinds.Website.TYPE_WORK).build());
        }
// add IM
        String qq1="452824089";
        ops.add(ContentProviderOperation.newInsert(Data.CONTENT_URI).withValueBackReference(
                Data.RAW_CONTACT_ID, 0).withValue(
                Data.MIMETYPE,
                ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE).withValue(
                ContactsContract.CommonDataKinds.Im.DATA1,qq1)
                .withValue(
                        ContactsContract.CommonDataKinds.Im.PROTOCOL,
                        ContactsContract.CommonDataKinds.Im.PROTOCOL_QQ).build());
// add logo image
// Bitmap bm = logo;
// if (bm != null) {
// ByteArrayOutputStream baos = new ByteArrayOutputStream();
// bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
// byte[] photo = baos.toByteArray();
// if (photo != null) {
//
// ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
// .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
// .withValue(ContactsContract.Data.MIMETYPE,
// ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
// .withValue(ContactsContract.CommonDataKinds.Photo.PHOTO, photo)
// .build());
// }
// }
        try {
            context.getContentResolver().applyBatch(
                    ContactsContract.AUTHORITY, ops);
        } catch (Exception e){
        }
    }



}
