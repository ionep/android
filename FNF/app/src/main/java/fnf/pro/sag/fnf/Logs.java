package fnf.pro.sag.fnf;


import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Logs extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_logs, container, false);
        Button call = (Button) v.findViewById(R.id.call);
        Button chkBal = (Button) v.findViewById(R.id.balance);
        Button sms = (Button) v.findViewById(R.id.sms);
        final EditText target = (EditText) v.findViewById(R.id.text);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = target.getText().toString();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + Uri.encode(s)));
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(getActivity().getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });

        chkBal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = target.getText().toString();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + Uri.encode("*400#")));
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(getActivity().getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });

        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PendingIntent pi = PendingIntent.getActivity(getActivity().getApplicationContext(), 0,
                        new Intent(getActivity().getApplicationContext(), MainActivity.class), 0);
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage("1415", null, "FNFINQ", pi, null);
            }
        });
        return v;
    }

}
