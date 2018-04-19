package examprojectapp.ionep.com.examprojectapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class MainPage extends AppCompatActivity {


    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    DBhelper myDB;

    int hr=0,min=0,sec=0,app_start=0;

    Handler bluetoothIn;

    final int handlerState = 1;                        //used to identify handler message
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder recDataString = new StringBuilder();
    int len=0;

    private ConnectedThread mConnectedThread;

    // SPP UUID service - this should work for most devices
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // String for MAC address
    private static String address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDB= new DBhelper(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);



        btAdapter = BluetoothAdapter.getDefaultAdapter();
        checkBTState();
        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {                                     //if message is what we want
                    byte[] readBuf = (byte[]) msg.obj;
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    String roll=null,dbname;
                    int chk=0;
                    if(readMessage.equals("A"))
                    {
                        chk=1;
                        roll="070/BEX/01";
                        dbname="Ajay Raj Aryal";
                    }
                    if(readMessage.equals("B"))
                    {
                        chk=1;
                        roll="070/BEX/11";
                        dbname="Kamal Bhandari";
                    }
                    if(readMessage.equals("C"))
                    {
                        chk=1;
                        roll="070/BEX/29";
                        dbname="Suman Gautam";
                    }
                    if(readMessage.equals("D"))
                    {
                        chk=1;
                        roll="070/BEX/31";
                        dbname="Umesh Poudel";
                    }
                        /*len++;
                        Log.d("mykey", readMessage);
                        if (len>=12) {                                           // make sure there data before ~
                            String dataInPrint = recDataString.substring(0,recDataString.length()-1);    // extract string

                            Log.d("mykey", dataInPrint);
                            if (recDataString.charAt(0) == '0')                             //if it starts with # we know it is what we are looking for
                            {*/
                    if(chk==1) {
                        Log.d("mytag",readMessage);
                        try {
                            if (app_start == 1) {
                                int state=0;
                                Cursor crs = myDB.readData(roll);
                                if (crs.moveToFirst() && crs.getCount() > 0 && (sec>=45 || (min<=2 && min>0)))  {
                                    Cursor getInit=myDB.readData(roll,0);
                                    Cursor getInit2=myDB.readData(roll,1);
                                    Cursor getInit3=myDB.readData(roll,2);
                                    if((getInit.moveToFirst() || getInit2.moveToFirst() || getInit3.moveToFirst()) && !(getInit2.moveToFirst() && getInit3.moveToFirst())) {
                                        if (crs.getInt(5) == 1) {
                                            state = 2;
                                            myDB.insertData(roll, hr, min, sec, state);
                                        } else if (crs.getInt(5) == 0 || crs.getInt(5) == 2) {
                                            state = 1;
                                            myDB.insertData(roll, hr, min, sec, state);
                                        }
                                        Log.d("mykey", "Data Inserted");
                                    }
                                }
                                if (!crs.moveToFirst() && min==0 && sec< 30) {
                                    state = 0;
                                    myDB.insertData(roll, hr, min, sec, state);
                                    Log.d("mykey", "Data Inserted");
                                }

                            } else {
                                Log.d("mykey", "Timer not started");
                            }
                        }catch (Exception e){
                            Log.d("mykey",e.toString());
                        }
                    }
                }

            }

        };

    }


    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {

        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
    }

    public void onResume() {
        super.onResume();
        Intent intent = getIntent();

        address = intent.getStringExtra(BTConnect.EXTRA_ADDRESS);
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        try {
            btSocket = createBluetoothSocket(device);
            Log.d("mykey","Socket Created");
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_SHORT).show();
        }

        try
        {
            btSocket.connect();
            Log.d("mykey","Socket Connected");
        } catch (IOException e) {
            try
            {
                btSocket.close();
            } catch (IOException e2)
            {
                Toast.makeText(getApplicationContext(),"Resume:Close Socket",Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(getApplicationContext(),"Resume:Connect to Socket",Toast.LENGTH_SHORT).show();
        }
        mConnectedThread = new ConnectedThread(btSocket);

        mConnectedThread.start();

        //I send a character when resuming.beginning transmission to check device is connected
        //If it is not an exception will be thrown in the write method and finish() will be called
        mConnectedThread.write("x");

    }

    @Override
    public void onPause()
    {
        super.onPause();
        try
        {
            btSocket.close();
        } catch (IOException e2) {
            Toast.makeText(getApplicationContext(),"Pause:Close",Toast.LENGTH_SHORT).show();
        }
    }

    private void checkBTState() {

        if(btAdapter==null) {
            Toast.makeText(getBaseContext(), "Device does not support bluetooth", Toast.LENGTH_SHORT).show();
        } else {
            if (btAdapter.isEnabled()) {
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                //Toast.makeText(getApplicationContext(),"Unable to get streams",Toast.LENGTH_SHORT).show();
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[255];
            int bytes;
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, buffer).sendToTarget();
                } catch (IOException e) {
                    Log.d("mykey",e.toString());
                    break;
                }
            }
        }
        public void write(String input) {
            byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
            } catch (IOException e) {
                //if you cannot write, close the application
                //Toast.makeText(getBaseContext(), "Connection Failure", Toast.LENGTH_LONG).show();
                finish();

            }
        }
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position)
            {
                case 0:
                    return new Time();
                case 1:
                    return new Attendance();
                case 2:
                    return new InOut();
                default:
                    return new Time();
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Time";
                case 1:
                    return "Attendance";
                case 2:
                    return "In/Out";
            }
            return null;
        }
    }

    public DBhelper getDB()
    {
        return myDB;
    }

    public void setTime(int h,int m,int s)
    {
        hr=h;
        min=m;
        sec=s;
    }
    public void setAppState(int app)
    {
        app_start=app;
    }
    public void writeBT(String str)
    {
        mConnectedThread.write(str);
    }
}
