package fnf.pro.sag.fnf;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;


public class CallInfo {
    public String[][] readContacts(ContentResolver c) {
        //data to be returned
        int maxContacts=500;

        //single array to hold all data
        //0=maxcontact  1=names   2=numbers
        String[][] contact=new String[3][maxContacts];

        Cursor cursor = c.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        int i=0;
        if(cursor.getCount()==0)
        {
            contact[0][0]="1";
            contact[1][0]="No contacts Found";
            contact[2][0]="No contacts Found";
            return contact;
        }
        while (cursor.moveToNext() && i<maxContacts) {
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))+"\n";
            String number=new String();
            if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)))>0)
            {
                Cursor phones = c.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID ,null, null);
                while (phones.moveToNext()) {
                    number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))+"\n";
                }
                phones.close();
            }
            contact[1][i]=name;
            contact[2][i]=number;
            i++;
        }
        contact[0][0]=Integer.toString(i);
        contact=sortData(contact);
        return contact;
    }

    private String[][] sortData(String[][] contact)
    {
        for(int i=0;i<contact[1].length-1;i++)
        {
            if(contact[1][i]==null)
            {
                break;
            }
            for (int j=1;j<contact[1].length;j++)
            {
                if(contact[1][j]==null)
                {
                    break;
                }
                //difference =str[i]-str[j]
                Log.d("key",Integer.toString(i)+Integer.toString(j));
                int difference=compareStrings(contact[1][i],contact[1][j],false);
                if(difference>0)
                {
                    String tmp[][]=new String[3][1];
                    tmp[1][0]=contact[1][i];
                    tmp[2][0]=contact[2][i];
                    contact[1][i]=contact[1][j];
                    contact[2][i]=contact[2][j];
                    contact[1][j]=tmp[1][0];
                    contact[2][j]=tmp[2][0];
                }
            }
        }
        return contact;
    }
    public int compareStrings(String s1,String s2,boolean sensitive)
    {
        /*
            sensitive=true --> case sensitive
            compares each characters by position
            if s1 is alphabetically first, returns negative
            for s2, positive
            and 0 if equal
        */
        int result=0;
        if(!sensitive) {
            s1.toLowerCase();
            s2.toLowerCase();
        }
        int end=(s1.length()<s2.length())?s1.length():s2.length();
        for(int i=0;i<end;i++)
        {
            result =s1.charAt(i)-s2.charAt(i);
            if(result!=0)
            {
                break;
            }
        }
        if(result==0 && s1.length()>end)
        {
            return 1;
        }
        return result;
    }

}
