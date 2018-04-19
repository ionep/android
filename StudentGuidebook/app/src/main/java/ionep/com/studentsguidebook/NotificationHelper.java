package ionep.com.studentsguidebook;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

public class NotificationHelper {

    NotificationCompat.Builder notification;
    private static final int uniqueId=23452;

    NotificationHelper(NotificationCompat.Builder notification)
    {
        this.notification=notification;
    }
    public void popUp(Context context,String title, String text,String nottext,int img)
    {
        notification.setSmallIcon(img);
        notification.setTicker(nottext);
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle(title);
        notification.setContentText(text);

        Intent intent=new Intent(context,MainActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);

        NotificationManager nm=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(uniqueId,notification.build());
    }
    public void popUp(Context context,String title, String text)
    {
        notification.setSmallIcon(R.mipmap.ic_launcher);
        notification.setTicker("Hey There");
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle(title);
        notification.setContentText(text);

        Intent intent=new Intent(context,MainActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);

        NotificationManager nm=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(uniqueId,notification.build());
    }
}
