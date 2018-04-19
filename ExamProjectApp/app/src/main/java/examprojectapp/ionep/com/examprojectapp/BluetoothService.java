package examprojectapp.ionep.com.examprojectapp;


import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.UUID;

public class BluetoothService {
    private static final String appName="MYAPP";
    private static final UUID my_UUID=UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private final BluetoothAdapter mBluetoothAdapter;
    Context mContext;

    private AcceptThread mAcceptThread;

    private ConnectThread mConnectThread;
    private BluetoothDevice mmDevice;
    private UUID deviceUUID;
    ProgressDialog mProgressDialog;

    private ConnectedThread mConnectedThread;

    public BluetoothService(Context context) {
        mContext=context;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        start();
    }

    private class AcceptThread extends Thread{
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread()
        {
            BluetoothServerSocket tmp=null;

            try {
                tmp=mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(appName,my_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mmServerSocket=tmp;
        }

        public void run()
        {
            BluetoothSocket socket=null;

            try {
                socket=mmServerSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(socket!=null)
            {
                connected(socket,mmDevice);
            }
        }
        public void cancel()
        {
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ConnectThread extends Thread{
        private BluetoothSocket mmSocket;

        public ConnectThread(BluetoothDevice device,UUID uuid)
        {
            mmDevice=device;
            deviceUUID=uuid;
        }

        public void run()
        {
            BluetoothSocket tap=null;

            try {
                tap=mmDevice.createRfcommSocketToServiceRecord(deviceUUID);
            } catch (IOException e) {
                e.printStackTrace();
            }

            mmSocket=tap;
            mBluetoothAdapter.cancelDiscovery();

            try {
                mmSocket.connect();
            } catch (IOException e) {
                try {
                    mmSocket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }

            connected(mmSocket,mmDevice);
        }

        public void cancel()
        {
            try {
                mmSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void start()
    {
        if(mConnectThread!=null)
        {
            mConnectThread.cancel();
            mConnectThread=null;
        }
        if(mAcceptThread ==null)
        {
            mAcceptThread=new AcceptThread();
            mAcceptThread.start();
        }
    }

    public void startClient(BluetoothDevice device,UUID uuid)
    {
        mProgressDialog=ProgressDialog.show(mContext,"Connecting","Please Wait...",true);
        mConnectThread=new ConnectThread(device,uuid);
        mConnectThread.start();
    }

    private class ConnectedThread extends Thread
    {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket)
        {
            mmSocket=socket;
            InputStream tmpIn=null;
            OutputStream tmpOut=null;
            try {
                mProgressDialog.dismiss();
            }catch(NullPointerException e)
            {
                e.printStackTrace();
            }

            try {
                tmpIn=mmSocket.getInputStream();
                tmpOut=mmSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mmInStream=tmpIn;
            mmOutStream=tmpOut;
        }

        public void run()
        {
            byte[] buffer=new byte[1024];
            int bytes;

            while(true)
            {
                try {
                    bytes=mmInStream.read(buffer);
                    String incomingMsg=new String(buffer,0,bytes);

                    //This message to store to database
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }

        public void write(byte[] bytes)
        {
            String text=new String(bytes, Charset.defaultCharset());
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void cancel()
        {
            try {
                mmSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void connected(BluetoothSocket mmSocket,BluetoothDevice mmDevice)
    {
        mConnectedThread=new ConnectedThread(mmSocket);
        mConnectedThread.start();
    }

    public void write(byte[] out)
    {
        ConnectedThread r;

        mConnectedThread.write(out);
    }
}
