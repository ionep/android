package hex.floodproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.app.LauncherActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NotificationManagerCompat notificationManager;

    DBhelper db;


    Button btn,btn2;
    TextView tv;
    String json_url="http://192.168.85.1/hexflood/appinterface.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn=findViewById(R.id.button);
        btn2=findViewById(R.id.button2);
        tv=findViewById(R.id.textview);

        //Database initialization
        db=new DBhelper(this);

        notificationManager= NotificationManagerCompat.from(this);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                new BackgroundTask().execute();

                //notification
                Notification notification=new NotificationCompat.Builder(getApplicationContext(),NotificationHelper.channelID)
                        .setSmallIcon(R.drawable.ic_warning_black_24dp)
                        .setContentTitle("Warning")
                        .setContentText("Flee now")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_ALARM)
                        .build();
                notificationManager.notify(1,notification);

                //watch data
                Cursor cursor=db.readData();
                if(cursor.moveToFirst() && cursor.getCount()>0)
                {
                    String str=cursor.getFloat(1)+" "+cursor.getInt(2)+" "+
                            cursor.getInt(3)+" "+cursor.getInt(4)+" "+
                            cursor.getString(5);
                    tv.setText(str);
                }
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add data on database
                if(db.insertData("10","10","10","2010","KTM"))
                {
                    Toast.makeText(getApplicationContext(),"Data inserted",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Error in insertion",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    class BackgroundTask extends AsyncTask<Void,Void,String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String data;
                URL url=new URL(json_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
                InputStream inputStream=httpURLConnection.getInputStream();
//                return "here";

                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder stringBuilder=new StringBuilder();

                while((data= bufferedReader.readLine())!=null)
                {
                    stringBuilder.append(data+"\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            catch (Exception e)
            {
                Log.d("flooderror",e.getMessage());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
//            Log.d("flooderror",result.toString());
            tv.setText(result);
        }
    }

}
