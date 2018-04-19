package a3d.led.hex.a3dledhex;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.UUID;
import android.app.*;
import android.view.View.*;
import android.view.*;
import android.view.InputDevice.*;
import android.os.*;
import android.util.*;


public class PatternControl extends Activity
{
    Button pat1,off,btDisconnect;
    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    boolean btnUp=false,btnDown=false;
    private Handler handler=new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent newint = getIntent();
        address = newint.getStringExtra(MainActivity.EXTRA_ADDRESS);

        setContentView(R.layout.pattern);

        pat1= (Button)findViewById(R.id.pat1);
        off= (Button)findViewById(R.id.off);

        btDisconnect=(Button)findViewById(R.id.disconnect);


        new ConnectBT().execute();

        pat1.setOnTouchListener(new OnTouchListener()
        {
            public boolean onTouch(View v,MotionEvent m)
            {
                switch(m.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        dataTransfer("A");
                        return true;
                    case MotionEvent.ACTION_UP:
                        return true;
                    default:
                        return false;
                }
            }
        });

        off.setOnTouchListener(new OnTouchListener()
        {
            public boolean onTouch(View v,MotionEvent m)
            {
                switch(m.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        dataTransfer("O");
                        return true;
                    case MotionEvent.ACTION_UP:
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private void dataTransfer(String s)
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write(s.toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }
    /*private void actionDown(final String s)
    {
        if(!btnDown)
        {
            Thread r=new Thread(){
                public void run()
                {
                    try{
                        btnDown=true;
                        while(!btnUp)
                        {
                            handler.post(new Runnable(){
                                public void run()
                                {
                                    dataTransfer(s);
                                }
                            });

                            try{
                                Thread.sleep(20);

                            }
                            catch(InterruptedException e){
                                throw new RuntimeException("Could not wait between char transfer",e);
                            }
                        }
                    }
                    finally
                    {
                        btnUp=false;
                        btnDown=false;
                    }
                }
            };

            r.start();
        }
    }

    private void actionUp()
    {
        actionDown("S");
        btnUp=true;
    }*/

    public void drive(View v)
    {
        int vid=v.getId();

        if(vid==R.id.disconnect)
        {
            if (btSocket!=null)
            {
                try
                {
                    btSocket.close();
                }
                catch (IOException e)
                {
                    msg("Error");
                }
            }
            finish();
        }
    }

    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void>
    {
        private boolean ConnectSuccess = true;

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(PatternControl.this, "Connecting...", "Please wait!!!");
        }

        @Override
        protected Void doInBackground(Void... devices)
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                msg("Connection Failed. Is the device available to connect? Try again.");
                finish();
            }
            else
            {
                msg("Connected.");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }
}

