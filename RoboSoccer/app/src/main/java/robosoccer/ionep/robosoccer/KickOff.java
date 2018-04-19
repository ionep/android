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
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class KickOff extends AppCompatActivity {

    //all the buttons
    ImageButton up,down,left,right,close;
    Button sliderLeft,sliderRight,lockOn,lockOff,blUp,blDown,slUp,slDown,hamUp,hammer,hamDown,suspensionUp,suspensionDown;

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
        setContentView(R.layout.activity_kick_off);

        //get the passed address of the device
        Intent intent=getIntent();
        address=intent.getStringExtra(BluetoothConnect.EXTRA_ADDRESS);

        //instantiate all the buttons
        up=(ImageButton) findViewById(R.id.up);
        down=(ImageButton) findViewById(R.id.down);
        left=(ImageButton) findViewById(R.id.left);
        right=(ImageButton) findViewById(R.id.right);
        close=(ImageButton) findViewById(R.id.close);
        sliderLeft=(Button) findViewById(R.id.sliderLeft);
        sliderRight=(Button) findViewById(R.id.sliderRight);
        lockOn=(Button) findViewById(R.id.lockOn);
        lockOff=(Button) findViewById(R.id.lockOff);
        blUp=(Button) findViewById(R.id.blUp);
        blDown=(Button) findViewById(R.id.blDown);
        slUp=(Button) findViewById(R.id.slUp);
        slDown=(Button) findViewById(R.id.slDown);
        hamUp=(Button) findViewById(R.id.hamUp);
        hamDown=(Button) findViewById(R.id.hamDown);
        hammer=(Button) findViewById(R.id.hammer);
        suspensionUp=(Button) findViewById(R.id.suspensionUp);
        suspensionDown=(Button) findViewById(R.id.suspensionDown);

        new ConnectBt().execute();


        //event listeners for all buttons
        up.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        dataTransfer("A");
                        v.setBackgroundColor(getResources().getColor(R.color.clicked));
                        return true;
                    case MotionEvent.ACTION_UP:
                        dataTransfer("S");
                        v.setBackgroundColor(getResources().getColor(R.color.notclicked));
                        return true;
                    default:
                        return false;
                }
            }
        });

        down.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        dataTransfer("B");
                        v.setBackgroundColor(getResources().getColor(R.color.clicked));
                        return true;
                    case MotionEvent.ACTION_UP:
                        dataTransfer("S");
                        v.setBackgroundColor(getResources().getColor(R.color.notclicked));
                        return true;
                    default:
                        return false;
                }
            }
        });

        left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        dataTransfer("C");
                        v.setBackgroundColor(getResources().getColor(R.color.clicked));
                        return true;
                    case MotionEvent.ACTION_UP:
                        dataTransfer("S");
                        v.setBackgroundColor(getResources().getColor(R.color.notclicked));
                        return true;
                    default:
                        return false;
                }
            }
        });

        right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        dataTransfer("D");
                        v.setBackgroundColor(getResources().getColor(R.color.clicked));
                        return true;
                    case MotionEvent.ACTION_UP:
                        dataTransfer("S");
                        v.setBackgroundColor(getResources().getColor(R.color.notclicked));
                        return true;
                    default:
                        return false;
                }
            }
        });

        blUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        dataTransfer("E");
                        v.setBackgroundColor(getResources().getColor(R.color.clicked));
                        return true;
                    case MotionEvent.ACTION_UP:
                        dataTransfer("S");
                        v.setBackgroundColor(getResources().getColor(R.color.notclicked));
                        return true;
                    default:
                        return false;
                }
            }
        });

        blDown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        dataTransfer("F");
                        v.setBackgroundColor(getResources().getColor(R.color.clicked));
                        return true;
                    case MotionEvent.ACTION_UP:
                        dataTransfer("S");
                        v.setBackgroundColor(getResources().getColor(R.color.notclicked));
                        return true;
                    default:
                        return false;
                }
            }
        });

        slUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        dataTransfer("G");
                        v.setBackgroundColor(getResources().getColor(R.color.clicked));
                        return true;
                    case MotionEvent.ACTION_UP:
                        dataTransfer("S");
                        v.setBackgroundColor(getResources().getColor(R.color.notclicked));
                        return true;
                    default:
                        return false;
                }
            }
        });

        slDown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        dataTransfer("H");
                        v.setBackgroundColor(getResources().getColor(R.color.clicked));
                        return true;
                    case MotionEvent.ACTION_UP:
                        dataTransfer("S");
                        v.setBackgroundColor(getResources().getColor(R.color.notclicked));
                        return true;
                    default:
                        return false;
                }
            }
        });
        sliderLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        dataTransfer("I");
                        v.setBackgroundColor(getResources().getColor(R.color.clicked));
                        return true;
                    case MotionEvent.ACTION_UP:
                        dataTransfer("S");
                        v.setBackgroundColor(getResources().getColor(R.color.notclicked));
                        return true;
                    default:
                        return false;
                }
            }
        });
        sliderRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        dataTransfer("J");
                        v.setBackgroundColor(getResources().getColor(R.color.clicked));
                        return true;
                    case MotionEvent.ACTION_UP:
                        dataTransfer("S");
                        v.setBackgroundColor(getResources().getColor(R.color.notclicked));
                        return true;
                    default:
                        return false;
                }
            }
        });

        suspensionUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        dataTransfer("K");
                        v.setBackgroundColor(getResources().getColor(R.color.clicked));
                        return true;
                    case MotionEvent.ACTION_UP:
                        dataTransfer("S");
                        v.setBackgroundColor(getResources().getColor(R.color.notclicked));
                        return true;
                    default:
                        return false;
                }
            }
        });

        suspensionDown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        dataTransfer("L");
                        v.setBackgroundColor(getResources().getColor(R.color.clicked));
                        return true;
                    case MotionEvent.ACTION_UP:
                        dataTransfer("S");
                        v.setBackgroundColor(getResources().getColor(R.color.notclicked));
                        return true;
                    default:
                        return false;
                }
            }
        });

        hamUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        dataTransfer("M");
                        v.setBackgroundColor(getResources().getColor(R.color.clicked));
                        return true;
                    case MotionEvent.ACTION_UP:
                        dataTransfer("S");
                        v.setBackgroundColor(getResources().getColor(R.color.notclicked));
                        return true;
                    default:
                        return false;
                }
            }
        });

        hammer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        dataTransfer("N");
                        v.setBackgroundColor(getResources().getColor(R.color.clicked));
                        return true;
                    case MotionEvent.ACTION_UP:
                        dataTransfer("S");
                        v.setBackgroundColor(getResources().getColor(R.color.notclicked));
                        return true;
                    default:
                        return false;
                }
            }
        });

        hamDown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        dataTransfer("O");
                        v.setBackgroundColor(getResources().getColor(R.color.clicked));
                        return true;
                    case MotionEvent.ACTION_UP:
                        dataTransfer("S");
                        v.setBackgroundColor(getResources().getColor(R.color.notclicked));
                        return true;
                    default:
                        return false;
                }
            }
        });

        lockOn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        dataTransfer("P");
                        v.setBackgroundColor(getResources().getColor(R.color.clicked));
                        return true;
                    case MotionEvent.ACTION_UP:
                        dataTransfer("S");
                        v.setBackgroundColor(getResources().getColor(R.color.notclicked));
                        return true;
                    default:
                        return false;
                }
            }
        });

        lockOff.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        dataTransfer("Q");
                        v.setBackgroundColor(getResources().getColor(R.color.clicked));
                        return true;
                    case MotionEvent.ACTION_UP:
                        dataTransfer("S");
                        v.setBackgroundColor(getResources().getColor(R.color.notclicked));
                        return true;
                    default:
                        return false;
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btSocket!=null)
                {
                    try {
                        btSocket.close();
                    } catch (IOException e) {
                        msg("Unable to close socket");
                    }
                }
                finish();
            }
        });
    }


    //an asynctask class to connect to the bluetooth device
    private class ConnectBt extends AsyncTask<Void,Void,Void>
    {

        private boolean connectSuccess=true;

        @Override
        protected void onPreExecute() {
            progress= ProgressDialog.show(KickOff.this,"Connecting...","Please Wait!!!");
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
