package com.example.jonat.historyclasscontentprovider;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jonat on 1/16/2017.
 */

public class ContactRetriever {
    private final Context mContext;

    public ContactRetriever(Context context){
        this.mContext = context;
    }

    public ArrayList<ProfileInfo> fetchAll(){
        String[] projectsFields = new String[]{
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
        };
        ArrayList<ProfileInfo> listProfile = new ArrayList<>();
        CursorLoader cursorLoader = new CursorLoader(mContext,
                ContactsContract.Contacts.CONTENT_URI,
                projectsFields,
                null,
                null,
                null);

        Cursor cursor = cursorLoader.loadInBackground();
        final Map<String, ProfileInfo> contactsMap = new HashMap<>(cursor.getCount());
        if(cursor.moveToFirst()){
            int idIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            do{
                String contactId = cursor.getString(idIndex);
                String contactName = cursor.getString(nameIndex);
                ProfileInfo profileInfo = new ProfileInfo(contactId, contactName);
                contactsMap.put(contactId, profileInfo);
                listProfile.add(profileInfo);
            }while (cursor.moveToNext());
        }
        cursor.close();

        getPhoneNumbers(contactsMap);
        getEmailAddress(contactsMap);

        return listProfile;
    }


    public void getPhoneNumbers(Map<String, ProfileInfo> contactsMap) {
        //Get numbers

        final String[] numbersProjections = new String[]{
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.TYPE,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
        };

        Cursor phone = new CursorLoader(mContext,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                numbersProjections,
                null,
                null,
                null).loadInBackground();

        if(phone.moveToFirst()){
            final int contactNumberColumnIndex = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            final int contactTypeColumnIndex = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
            final int contactIdColumnIndex = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);

            while(!phone.isAfterLast()){
                final String number = phone.getString(contactNumberColumnIndex);
                final String id = phone.getString(contactIdColumnIndex);
                ProfileInfo profileinfo = contactsMap.get(id);
                if(profileinfo == null){
                    continue;
                }

                final int type = phone.getInt(contactTypeColumnIndex);
                String customLabel = "Custom";
                CharSequence phoneType = ContactsContract.CommonDataKinds.Phone.getTypeLabel(mContext.getResources(), type,
                      customLabel);
                profileinfo.addNumber(number, phoneType.toString());
                phone.moveToLast();
            }

        }
        phone.close();
    }

    public void getEmailAddress(Map<String, ProfileInfo> contactsMap) {
        //get Email

        final String[] emailProjections = new String[]{
                ContactsContract.CommonDataKinds.Phone.DATA,
                ContactsContract.CommonDataKinds.Phone.TYPE,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID
        };

        Cursor emails = new CursorLoader(mContext,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                emailProjections,
                null,
                null,
                null).loadInBackground();

        if(emails.moveToFirst()){
            final int emailColumnIndex = emails.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA);
            final int TypeColumnIndex = emails.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
            final int IdColumnIndex = emails.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);

            while (!emails.isAfterLast()){
                final String address = emails.getString(emailColumnIndex);
                final String id = emails.getString(IdColumnIndex);
                final  int type = emails.getInt(TypeColumnIndex);
                String customLabel = "Custom";
                ProfileInfo profileInfo = contactsMap.get(id);
                if(profileInfo == null){
                    continue;
                }

                CharSequence emailType = ContactsContract.CommonDataKinds.Phone.getTypeLabel(mContext.getResources(), type,
                        customLabel);
                profileInfo.addEmail(address, emailType.toString());
                emails.moveToLast();
            }
        }

        emails.close();

    }
}
