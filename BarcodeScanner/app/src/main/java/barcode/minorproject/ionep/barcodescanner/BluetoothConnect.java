package barcode.minorproject.ionep.barcodescanner;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class BluetoothConnect extends AppCompatActivity {

    Button connect;
    ListView btDevices;
    TextView connectInfo;

    private BluetoothAdapter adapter=null;
    private Set<BluetoothDevice> pairedDevices;
    public static String EXTRA_ADDRESS="device_address";
    boolean  listShown=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_connect);

        connect=(Button) findViewById(R.id.connect);
        btDevices=(ListView) findViewById(R.id.btDevices);
        connectInfo=(TextView) findViewById(R.id.connectInfo);

        adapter=BluetoothAdapter.getDefaultAdapter();

        if(adapter==null)
        {
            Toast.makeText(this,"No Bluetooth devcie found.",Toast.LENGTH_LONG).show();
            finish();
        }
        else if(!adapter.isEnabled())
        {
            Intent btOn=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(btOn,1);
        }

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!listShown)
                {
                    connectInfo.setText("Tap on the device to connect.\nIf you cant find the device, goto bluetooth settings and pair to it.");
                    listShown=true;
                }
                showPairedDevices();
            }
        });

    }

    private void showPairedDevices()
    {
        pairedDevices=adapter.getBondedDevices();
        ArrayList list=new ArrayList();

        if(pairedDevices.size()>0)
        {
            for(BluetoothDevice device: pairedDevices) {
                list.add(device.getName() + "\n" + device.getAddress());
            }
        }
        else
        {
            Toast.makeText(this,"No Paired Devices found.",Toast.LENGTH_SHORT).show();
            listShown=false;
        }
        final ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,list);
        btDevices.setAdapter(arrayAdapter);
        btDevices.setOnItemClickListener(connectToDevice);
    }

    private AdapterView.OnItemClickListener connectToDevice= new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String info=((TextView) view).getText().toString();
            String address=info.substring(info.length()-17);

            Intent intent=new Intent(BluetoothConnect.this,MainActivity.class);
            intent.putExtra(EXTRA_ADDRESS,address);

            startActivity(intent);
        }
    };
}
