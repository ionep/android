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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class KickOff extends AppCompatActivity {

    //all the buttons
    ImageButton up,down,left,right,close,armLeft,armRight;
    Button armUp,armDown,smallUp,smallDown;
    SeekBar seekBar;
    int prevProgress=0;
    TextView hold;

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
        setContentView(R.layout.minor);

        //get the passed address of the device
        Intent intent=getIntent();
        address=intent.getStringExtra(BluetoothConnect.EXTRA_ADDRESS);

        //instantiate all the buttons
        up=(ImageButton) findViewById(R.id.up);
        down=(ImageButton) findViewById(R.id.down);
        left=(ImageButton) findViewById(R.id.left);
        right=(ImageButton) findViewById(R.id.right);
        close=(ImageButton) findViewById(R.id.close);
        armLeft=(ImageButton) findViewById(R.id.armLeft);
        armRight=(ImageButton) findViewById(R.id.armRight);
        armUp=(Button) findViewById(R.id.armUp);
        armDown=(Button) findViewById(R.id.armDown);
        smallUp=(Button) findViewById(R.id.smallUp);
        smallDown=(Button) findViewById(R.id.smallDown);
        seekBar=(SeekBar) findViewById(R.id.seekBar);
        hold=(TextView) findViewById(R.id.hold);

        seekBar.setMax(100);
        seekBar.setProgress(0);

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

        armUp.setOnTouchListener(new View.OnTouchListener() {
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

        armDown.setOnTouchListener(new View.OnTouchListener() {
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

        armLeft.setOnTouchListener(new View.OnTouchListener() {
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

        armRight.setOnTouchListener(new View.OnTouchListener() {
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

        smallUp.setOnTouchListener(new View.OnTouchListener() {
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

        smallDown.setOnTouchListener(new View.OnTouchListener() {
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

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                //if increased
                if(progress>prevProgress)
                {
                    dataTransfer("X");
                    hold.setText("Open");
                }
                //if decreased
                else if(progress<prevProgress)
                {
                    dataTransfer("Y");
                    hold.setText("Close");
                }
                //set for next iteration
                prevProgress=progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

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
