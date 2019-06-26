package robosoccer.ionep.robosoccer;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class Baale extends AppCompatActivity {

    //all the buttons
    EditText interval,number,hour,min;
    TextView error;
    Button pill1,pill2,send;
    int selected=1;

    //variables for bluetooth
    private String address=null;
    private ProgressDialog progress;
    BluetoothAdapter adapter=null;
    BluetoothSocket btSocket=null;
    private boolean isBtConnected=false;

    static final UUID myUUID=UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baale);

        //get the passed address of the device
        Intent intent=getIntent();
        address=intent.getStringExtra(BluetoothConnect.EXTRA_ADDRESS);

        //instantiate all the buttons
        interval=(EditText) findViewById(R.id.interval);
        number=(EditText) findViewById(R.id.number);
        hour=(EditText) findViewById(R.id.hour);
        min=(EditText) findViewById(R.id.min);
        error=(TextView) findViewById(R.id.error);
        pill1=(Button) findViewById(R.id.pill1);
        pill2=(Button) findViewById(R.id.pill2);
        send=(Button) findViewById(R.id.send);


        new ConnectBt().execute();


        //event listeners for all buttons
        pill1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected=1;
                error.setText("Pill 1 selected.");
            }
        });

        pill2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected=2;
                error.setText("Pill 2 selected.");
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data;
                int in=Integer.parseInt(interval.getText().toString());
                int no=Integer.parseInt(number.getText().toString());
                int hr=Integer.parseInt(hour.getText().toString());
                int mn=Integer.parseInt(min.getText().toString());
                if(in>12 || in<=0)
                {
                    error.setText("Invalid time interval");
                }
                else if(no>6 || no<=0)
                {
                    error.setText("Invalid number of pills");
                }
                else if(hr>=24 || hr<0)
                {
                    error.setText("Invalid hour");
                }
                else if(mn>=60 || mn<0)
                {
                    error.setText("Invalid minutes");
                }
                else
                {
                    String txt1,txt2,txt3,txt4;
                    if(in<10)
                    {
                        txt1="0"+interval.getText();
                    }
                    else
                    {
                        txt1=interval.getText().toString();
                    }
                    if(no<10)
                    {
                        txt2="0"+number.getText();
                    }
                    else
                    {
                        txt2=number.getText().toString();
                    }
                    if(hr<10)
                    {
                        txt3="0"+hour.getText();
                    }
                    else
                    {
                        txt3=hour.getText().toString();
                    }
                    if(mn<10)
                    {
                        txt4="0"+min.getText();
                    }
                    else
                    {
                        txt4=min.getText().toString();
                    }
                    data="~"+String.valueOf(selected)+txt1+txt2+txt3+txt4+"@";
                    dataTransfer(data);
                    error.setText("Data sent "+data);
                }
            }
        });
    }


    //an asynctask class to connect to the bluetooth device
    private class ConnectBt extends AsyncTask<Void,Void,Void>
    {

        private boolean connectSuccess=true;

        @Override
        protected void onPreExecute() {
            progress= ProgressDialog.show(Baale.this,"Connecting...","Please Wait!!!");
        }

        @Override
        protected Void doInBackground(Void... params) {
            try{
                if(btSocket==null || !isBtConnected)
                {
                    adapter=BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice bluetoothDevice=adapter.getRemoteDevice(address);
                    btSocket=bluetoothDevice.createInsecureRfcommSocketToServiceRecord(myUUID);
                    adapter.cancelDiscovery();
                    btSocket.connect();
                }
            }
            catch (Exception e)
            {
                connectSuccess=false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(!connectSuccess)
            {
                msg("Connection Failed. Is the device available to connect?");
                finish();
            }
            else
            {
                msg("Connected");
                isBtConnected=true;
            }
            progress.dismiss();
        }
    }

    //toast all the errors or info
    private void msg(String str)
    {
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
    }

    //transfer data to the device
    private void dataTransfer(String s)
    {
        if(btSocket!=null)
        {
            try {
                Log.d("Bluetooth",s);
                btSocket.getOutputStream().write(s.getBytes());
            } catch (IOException e) {
                msg("Cant write to device. Check the device or connection");
            }
        }
    }

}
