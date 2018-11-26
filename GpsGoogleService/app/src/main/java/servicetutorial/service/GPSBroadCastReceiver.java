package servicetutorial.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by koteswara on 17/06/17.
 */

public class GPSBroadCastReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {

        String value=intent.getStringExtra("restartservice");
        Log.i("GPSBroadCastReceiver","BroadCastReceived");
        Intent i = new Intent(context, GoogleService.class);
        context.startService(i);


    }
}
