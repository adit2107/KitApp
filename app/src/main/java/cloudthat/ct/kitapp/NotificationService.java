package cloudthat.ct.kitapp;

import android.app.Notification;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Adit on 11/24/2017.
 */

public class NotificationService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Notification notification = new Notification.Builder(this)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .build();
        NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
        manager.notify(123, notification);

        if (remoteMessage.getNotification() != null) {
            Log.d("NotifMsg", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

    }
}
