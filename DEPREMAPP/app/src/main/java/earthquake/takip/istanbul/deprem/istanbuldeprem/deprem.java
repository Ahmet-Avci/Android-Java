package earthquake.takip.istanbul.deprem.istanbuldeprem;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.util.ArrayList;

public class deprem extends AppCompatActivity {

    private TextView mTextMessage;

    private String[] depremOncesi = {
            "Bu uygulama beklenen İstanbul depreminin olası zararlarını ez aza indirmek için yapılması gerekenleri size gösterebilmeyi amaç edinmiştir. Deprem hakkında yapılması gerekenler şu şekildedir",
            "Binadan acilen çıkmak için kullanılacak yollardaki tehlikeler ortadan kaldırılmalı, bu yollar işaretlemeli, çıkışı engelleyebilecek eşyalar çıkış yolu üzerinden kaldırılmalıdır.",
            "Geniş çıkış yolları oluşturulmalıdır. Dışa doğru açılan kapılar kullanılmalı, acil çıkış kapıları kilitli olmamalıdır. Acil çıkışlar aydınlatılmalıdır.",
            "Bir afet ve acil durumda eve ulaşılamayacak durumlar için aile bireyleri ile iletişimin nasıl sağlayacağı, alternatif buluşma yerleri ve bireylerin ulaşabileceği bölge dışı bağlantı kişisi (ev, işyeri, okul içinde, dışında ve ya mahalle dışında) belirlenmelidir.",
            "Dolaplar ve devrilebilecek benzeri eşyaları birbirine ve duvara sabitlenmelidir. Eğer sabitlenen eşya ve duvar arasında boşluk kalıyorsa, çarpma etkisini düşürmek için araya bir dolgu malzemesi konulmalıdır.",
            "Zehirli, patlayıcı, yanıcı maddeler düşmeyecek bir konumda sabitlenmeli ve kırılmayacak bir şekilde depolanmalıdır. Bu maddelerin üzerlerine fosforlu, belirleyici etiketler konulmalıdır.",
            "Kesinlikle panik yapılmamalıdır.",
            "Sabitlenmemiş dolap, raf, pencere vb. eşyalardan uzak durulmalıdır.",
            "Varsa sağlam sandalyelerle desteklenmiş masa altına veya dolgun ve hacimli koltuk, kanepe, içi dolu sandık gibi koruma sağlayabilecek eşya yanına çömelerek hayat üçgeni oluşturulmalıdır.",
            "Baş iki el arasına alınarak veya bir koruyucu (yastık, kitap vb) malzeme ile korunmalıdır. Sarsıntı geçene kadar bu pozisyonda beklenmelidir.",
            "Güvenli bir yer bulup, diz üstü ÇÖK",
            "Başını ve enseni koruyacak şekilde KAPAN",
            "Düşmemek için sabit bir yere TUTUN",
            "Merdivenlere ya da çıkışlara doğru koşulmamalıdır.",
            "Balkonlardan ya da pencerelerden aşağıya atlanmamalıdır.",
            "Kesinlikle asansör kullanılmamalıdır.",
            "Telefonlar acil durum ve yangınları bildirmek dışında kullanılmamalıdır.",
            "Sarsıntı geçtikten sonra elektrik, gaz ve su vanalarını kapatılmalı, soba ve ısıtıcılar söndürülmelidir.",
            "Okulda sınıfta ya da büroda ise sağlam sıra, masa altlarında ve ya yanında; koridorsa ise duvarın yanına hayat üçgeni oluşturacak şekilde ÇÖK-KAPAN-TUTUN hareketi ile baş ve boyun korunmalıdır.",
            "Metro gibi toplu taşıma alanlarıda iseniz, gerekmedikçe, kesinlikle metro ve trenden inilmemelidir. Elektriğe kapılabilinir veya diğer hattan gelen başka bir metro yada tren size çarpabilir.",
            "Sarsıntı bitinceye kadar metro ya da trenin içinde, sıkıca tutturulmuş askı, korkuluk veya herhangi bir yere tutunmalı, metro veya tren personeli tarafından verilen talimatlara uyulmalıdır.",
            "Paniklemeden durumunuzu kontrol edin.",
            "Enkaz altında iseniz ve hareket kabiliyetiniz kısıtlanmışsa çıkış için hayatınızı riske atacak hareketlere kalkışmayın. Biliniz ki kurtarma ekipleri en kısa zamanda size ulaşmak için çaba gösterecektir. ",
            "Enerjinizi en tasarruflu şekilde kullanmak için hareketlerinizi kontrol altında tutun.",
            "El ve ayaklarınızı kullanabiliyorsanız su, kalorifer, gaz tesisatlarına, zemine vurmak suretiyle varlığınızı duyurmaya çalışın.",
            "Sesinizi kullanabiliyorsanız kurtarma ekiplerinin seslerini duymaya ve onlara seslenmeye çalışınız. Ancak enerjinizi kontrollü kullanın."

    };


    private ArrayList<String> sonDepremler = new ArrayList<>();


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            ListView depremler = (ListView)findViewById(R.id.listView2);
            ScrollView resimler = (ScrollView)findViewById(R.id.resimler);
            ImageView logo = findViewById(R.id.logo);
            TextView switchText = findViewById(R.id.switchText);
            TextView infoText = findViewById(R.id.infoText);
            AdView reklam1 = findViewById(R.id.adView1);
            AdView reklam2 = findViewById(R.id.adView2);
            AdView reklam3 = findViewById(R.id.adView3);

            switch (item.getItemId()) {

                case R.id.navigation_home:
                    reklam2.setVisibility(View.GONE);
                    reklam3.setVisibility(View.GONE);
                    depremler.setVisibility(View.GONE);
                    resimler.setVisibility(View.GONE);
                    infoText.setVisibility(View.VISIBLE);
                    switchText.setVisibility(View.VISIBLE);
                    logo.setVisibility(View.VISIBLE);
                    reklam1.setVisibility(View.VISIBLE);
                    return true;

                case R.id.navigation_dashboard:
                    reklam1.setVisibility(View.GONE);
                    reklam3.setVisibility(View.GONE);
                    infoText.setVisibility(View.GONE);
                    switchText.setVisibility(View.GONE);
                    logo.setVisibility(View.GONE);
                    resimler.setVisibility(View.GONE);
                    ListView depremList = (ListView)findViewById(R.id.listView2);
                    if(CheckInternet())
                    {
                        sonDepremler.clear();
                        depremList.setAdapter(null);
                        new GetLastEarthQuake().execute();
                    }
                    else
                        {

                            ArrayAdapter<String> veri = new ArrayAdapter<String>
                                    (getApplicationContext(), android.R.layout.simple_list_item_2, android.R.id.text2,new String[]{"İnternet bağlantınız olmadığı için son depremler listelenemedi :("}  );
                            depremList.setAdapter(veri);
                    }
                    reklam2.setVisibility(View.VISIBLE);
                    depremler.setVisibility(View.VISIBLE);
                    return true;

                case R.id.navigation_notifications:
                    reklam1.setVisibility(View.GONE);
                    reklam2.setVisibility(View.GONE);
                    infoText.setVisibility(View.GONE);
                    switchText.setVisibility(View.GONE);
                    logo.setVisibility(View.GONE);
                    depremler.setVisibility(View.GONE);
                    resimler.setVisibility(View.VISIBLE);
                    reklam3.setVisibility(View.VISIBLE);
                    return true;

            }
            return false;
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deprem);

        //Reklam1
        AdView mAdView1 = (AdView) findViewById(R.id.adView1);
        AdRequest adRequest1 = new AdRequest.Builder().build();
        mAdView1.loadAd(adRequest1);

        //Reklam2
        AdView mAdView2 = (AdView) findViewById(R.id.adView2);
        AdRequest adRequest2 = new AdRequest.Builder().build();
        mAdView2.loadAd(adRequest2);

        //Reklam3
        AdView mAdView3 = (AdView) findViewById(R.id.adView3);
        AdRequest adRequest3 = new AdRequest.Builder().build();
        mAdView3.loadAd(adRequest3);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Intent intent = new Intent();
        String packageName = getPackageName();
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (pm.isIgnoringBatteryOptimizations(packageName)) {
            intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
        }
        else {
            intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + packageName));
            startActivity(intent);
        }


        Intent earthquaker = new Intent(getApplicationContext(),EarthquakeNotify.class);
        startService(earthquaker);

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



    public class GetLastEarthQuake extends AsyncTask<Void, Void, Void> {

        String desc;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Toast.makeText(getApplicationContext(),"Depremler getirililiyor...",Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... params) {

            try{
                Document doc  = Jsoup.connect("http://www.koeri.boun.edu.tr/scripts/lst4.asp").get();
                Elements elements = doc.select("pre");  // ilgili sayfanın açıklamasını almak için

                desc = elements.html();


            }catch (Exception e){
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            desc = desc.substring(582);
            sonDepremler.add("Burada Türkiye'de yakın zamanda oluşan depremlerin listesi verilmektedir. KAYNAK: Boğaziçi Üniversitesi Kandilli Rasathanesi ve Deprem Araştırma Enstitüsü Bölgesel Deprem-Tsunami İzleme Ve Değerlendirme Merkezi");

            for (int i = 0; i <= 50; i++) {

                String quake = desc.substring(0,153);
                String sonDeger = "";

                if (quake.contains("İlksel"))
                {
                    String deprem = quake.substring(0,127);
                    sonDeger = deprem.substring(0,18);
                    sonDeger += deprem.substring(60,65);
                    sonDeger += deprem.substring(71,123);

                    sonDepremler.add(sonDeger);

                    desc = desc.substring(129);
                } else{
                    String deprem = quake.substring(0,153);
                    sonDeger = deprem.substring(0,18);
                    sonDeger += deprem.substring(60,65);
                    sonDeger += deprem.substring(71,123);

                    sonDepremler.add(sonDeger);

                    desc = desc.substring(155);
                }


            }

            ListView depremList = (ListView)findViewById(R.id.listView2);
            ArrayAdapter<String> veri = new ArrayAdapter<String>
                    (getApplicationContext(), android.R.layout.simple_list_item_2, android.R.id.text2,sonDepremler );
            depremList.setAdapter(veri);

        }
    }

}
