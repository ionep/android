package fnf.pro.sag.fnf;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class smsReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;
        String str = "";
        if (bundle != null)
        {
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                str += "SMS from " + msgs[i].getOriginatingAddress();
                str += " :";
                str += msgs[i].getMessageBody().toString();
                //str += "n";
            }
            if(str.equals("SMS from 1415 :Dear customer, you have not subscribed to the FNF service."))
            {
                Toast.makeText(context,"Not Subscribed", Toast.LENGTH_LONG  ).show();
            }
            else
            {
                Toast.makeText(context,"Subscribed", Toast.LENGTH_LONG  ).show();
            }
            Toast.makeText(context, str, Toast.LENGTH_LONG  ).show();
        }
    }
}
