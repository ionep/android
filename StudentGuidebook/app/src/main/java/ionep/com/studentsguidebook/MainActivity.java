package ionep.com.studentsguidebook;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    NotificationCompat.Builder notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notification=new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);

        Button btn=(Button)findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationHelper nothelper=new NotificationHelper(notification);
                nothelper.popUp(getApplicationContext(),"A god is speaking","in english","Feel the wrath",R.mipmap.ic_launcher);
            }
        });
    }

}
