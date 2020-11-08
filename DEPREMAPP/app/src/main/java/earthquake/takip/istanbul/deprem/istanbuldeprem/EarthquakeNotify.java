package earthquake.takip.istanbul.deprem.istanbuldeprem;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class EarthquakeNotify extends Service {

    public EarthquakeNotify() {
    }

    @Override
    public void onCreate() {

        new Thread(new Runnable() {  // Yeni bir Thread (iş parcacığı) oluşturuyorum.



            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() { // Thread'ım başladığında bitmemesi için while
                // ile sonsuz döngüye soktum. senaryo gereği
                while (1 == 1) {
                    try {
                        Thread.sleep(150000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if(CheckInternet()){
                        new GetLastEarthQuake().execute();
                    }

                }
            }
        }).start();
    }

    public boolean CheckInternet() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager.getActiveNetworkInfo() != null){
            manager.getActiveNetworkInfo().isAvailable();
            manager.getActiveNetworkInfo().isConnected();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void StopSelf()
    {
        StopSelf();
    }

    public class GetLastEarthQuake extends AsyncTask<Void, Void, Void> {

        String desc;
        String  newBigEartquake;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try{
                Document doc  = Jsoup.connect("http://www.koeri.boun.edu.tr/scripts/lst4.asp").get();
                Elements elements = doc.select("pre");  // ilgili sayfanın açıklamasını almak için

                desc = elements.html();

                desc = desc.substring(582,709);
                newBigEartquake = desc;

            }catch (Exception e){
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(Double.parseDouble(newBigEartquake.substring(62,65)) > 4)
            {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        CharSequence name = getString(R.string.channel_name);
                        String description = getString(R.string.channel_description);
                        int importance = NotificationManager.IMPORTANCE_DEFAULT;
                        NotificationChannel channel = new NotificationChannel("istanbuldeprem", name, importance);
                        channel.setDescription(description);
                        NotificationManager notificationManager = getSystemService(NotificationManager.class);
                        notificationManager.createNotificationChannel(channel);

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channel.getId())
                                .setSmallIcon(R.drawable.ic_launcher_foreground)
                                .setContentTitle(newBigEartquake.substring(13,18) + " saatlerinde " + newBigEartquake.substring(62,65) + " büyüklüğünde deprem!")
                                .setContentText(newBigEartquake.substring(71,110))
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                        playNotificationSound();
                        notificationManager.notify(3123, builder.build());
                    }
                }
            }

        public void playNotificationSound()
        {
            try
            {
                Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/raw/notification");
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), alarmSound);
                r.play();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}