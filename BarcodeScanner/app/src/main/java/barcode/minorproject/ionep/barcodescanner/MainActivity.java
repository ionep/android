package barcode.minorproject.ionep.barcodescanner;

import static android.Manifest.permission.CAMERA;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.Result;


import java.io.IOException;
import java.util.UUID;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import me.dm7.barcodescanner.zxing.ZXingScannerView.ResultHandler;

public class MainActivity extends AppCompatActivity implements ResultHandler {

    private static final int REQUEST_CAMERA= 1;
    private ZXingScannerView scannerView;

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

        //get the passed address of the device
        Intent intent=getIntent();
        address=intent.getStringExtra(BluetoothConnect.EXTRA_ADDRESS);

        new ConnectBt().execute();

        scannerView=new ZXingScannerView(this);
        setContentView(scannerView);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN)
        {
            if(checkPermission())
            {
                Toast.makeText(this,"Permission is granted",Toast.LENGTH_SHORT).show();
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            }
            else{
                requestPermission();
            }
        }
    }

    private boolean checkPermission()
    {
        return (ContextCompat.checkSelfPermission(this, CAMERA)== PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this,new String[]{CAMERA},REQUEST_CAMERA);
    }

    public void onRequestPermissionsResult(int requestCode, String permission[],int grantResults[])
    {
        switch (requestCode)
        {
            case REQUEST_CAMERA:
                if(grantResults.length>0)
                {
                    boolean cameraAccepted=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted)
                    {
                        Toast.makeText(this,"Permission granted",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(this,"Permission denied",Toast.LENGTH_SHORT).show();
                        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.M)
                        {
                            if(shouldShowRequestPermissionRationale(CAMERA))
                            {
                                displayAlertMessage("You need to allow access for both permissions",
                                        new DialogInterface.OnClickListener(){

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                                    requestPermissions(new String[]{CAMERA}, REQUEST_CAMERA);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onResume(){
        super.onResume();

        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.JELLY_BEAN)
        {
            if(checkPermission())
            {
                if(scannerView==null)
                {
                    scannerView= new ZXingScannerView(this);
                    setContentView(scannerView);
                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            }
            else{
                requestPermission();
            }
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        scannerView.stopCamera();
        if(btSocket!=null)
        {
            try {
                btSocket.close();
            } catch (IOException e) {
                msg("Unable to close socket");
            }
        }
    }

    public void displayAlertMessage(String message, DialogInterface.OnClickListener listener)
    {
        new AlertDialog.Builder(this).setMessage(message)
                .setPositiveButton("Ok", listener)
                .setNegativeButton("Cancel",null)
                .create()
                .show();

    }

    @Override
    public void handleResult(Result result) {
        final String myResult=result.getText();
        /*AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Scan Result");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                scannerView.resumeCameraPreview(MainActivity.this);
            }
        });
        builder.setNeutralButton("Visit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(myResult));
                startActivity(intent);
            }
        });
        builder.setMessage(myResult);
        AlertDialog alertDialog=builder.create();
        alertDialog.show();*/
        msg(myResult);
        dataTransfer(myResult);
        scannerView.resumeCameraPreview(MainActivity.this);
    }

    //an asynctask class to connect to the bluetooth device
    private class ConnectBt extends AsyncTask<Void,Void,Void>
    {

        private boolean connectSuccess=true;

        @Override
        protected void onPreExecute() {
            progress= ProgressDialog.show(MainActivity.this,"Connecting...","Please Wait!!!");
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
                //finish();
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
                btSocket.getOutputStream().write("~".getBytes());
                btSocket.getOutputStream().write(s.getBytes());
                btSocket.getOutputStream().write("`".getBytes());
            } catch (IOException e) {
                msg("Cant write to device. Check the device or connection");
            }
        }
    }
}
