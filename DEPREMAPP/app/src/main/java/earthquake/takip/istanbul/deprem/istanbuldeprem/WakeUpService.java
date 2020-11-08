package earthquake.takip.istanbul.deprem.istanbuldeprem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class WakeUpService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, EarthquakeNotify.class);
        context.startService(serviceIntent);
    }
}
