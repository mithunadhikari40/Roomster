package np.com.mithunadhikari.roomster;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by mithun on 6/23/2017.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        /*super.onMessageReceived(remoteMessage);*/
        showNotification(remoteMessage.getData().get("message"));
    }

    private void showNotification(String message) {
        // here remove the {"message like type from the received message
        //message receiving url is "https://isochasmic-circuits.000webhostapp.com/push_notification.php"
        int first_index=message.indexOf(":")+2;
        int second_index=message.lastIndexOf("}");
       // Log.e("The reveived message",message);
        String org_msg=message.substring(first_index,second_index);
        //Log.e("The reveived message",org_msg);

        Intent i = new Intent(this, signIn_signUp.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("Roomster")
                .setContentText(org_msg)
                /*.setSmallIcon(R.drawable.ic_menu_camera)*/
                .setSmallIcon(R.mipmap.logo)
                .setContentIntent(pendingIntent);
        builder.setDefaults(Notification.DEFAULT_ALL);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());


    }
}
