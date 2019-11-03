package iride.app.com.iride;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ViewDatabase extends AppCompatActivity {

    private List<SatisInfo> list,prevlist;
    DatabaseConnection dc;
    ListView listView;
    SatisInfo satisInfo;
    int fisNo;
    private Button goruntule;
    public static int code = 0;
    static int temp;
    private Button back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_database);

        back = (Button)findViewById(R.id.backdb);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        listView = (ListView) findViewById(R.id.listView);

        prevlist = new ArrayList<>();

        dc = new DatabaseConnection(this);
        dc.read();

        list = dc.gunlukKayıtlar();
        //list.add(new SatisInfo(2,"t","m",3,3,"s","s",123));
        Collections.sort(list);
        for(SatisInfo s : list)
            System.out.println("Sıralı gelen fiş no"+s.getFisNo());
        dc.close();

        for(int i=1; i<=list.size(); i++){
            prevlist.add(list.get(list.size()-i));
        }


        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/
        if(list.size()==0){
            Toast.makeText(this,"Liste boş!",Toast.LENGTH_SHORT).show();
            finish();
        }else{
            final ListAdapter adapter = new ListAdapter(this,R.layout.list_adapter,prevlist);

            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    satisInfo = prevlist.get(i);
                    //Bundle bundle = Home.bundle;
                    //Home.satisInfo = satisInfo;
                    //Object o = satisInfo;
                    //Log.e("af",Home.satisInfo.baslangıcSüre);
                   // Intent intent = new Intent(ViewDatabase.this,Home.class);

                    Home.info = satisInfo;
                    //Home.timer.cancel();
                    onBackPressed();

                    /*intent.putExtra("obje",satisInfo);
                    Log.e("info",""+satisInfo.totalÜcret);
                    startActivity(intent);*/
                }
            });

            /*listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(ViewDatabase.this);
                    builder.setTitle("iRide");
                    builder.setMessage("Seçilen kayıt silinecek.Onaylıyor musunuz?");
                    Log.e("index",""+i);
                    Log.e("fişno",""+list.get(i).fisNo);
                    Log.e("tarih",""+list.get(i).kayitTarihi);
                    temp=i;

                    builder.setPositiveButton("SİL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int t) {

                            List<SatisInfo> tm,tn;
                            DatabaseConnection data = new DatabaseConnection(getApplicationContext());

                            tn=new ArrayList<>();
                            data.open();
                            data.kayitSil(prevlist.get(temp).fisNo,prevlist.get(temp).kayitTarihi);
                            tm = data.gunlukKayıtlar();
                            data.close();

                            adapter.remove(adapter.getItem(i));


                            for(int i=1; i<=tm.size(); i++){
                                tn.add(tm.get(tm.size()-i));
                            }
                            ListAdapter adapter = new ListAdapter(getApplicationContext(),R.layout.list_adapter,tn);

                            listView.setAdapter(adapter);
                        }
                    });

                    builder.setNegativeButton("VAZGEÇ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.show();
                    return true;
                }
            });*/
        }
        goruntule = (Button) findViewById(R.id.goruntule);
        goruntule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),GunlukPassword.class);
                //i.putExtra("val","viewdb");
                startActivity(i);
            }
        });
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Home.timer.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (code == 256){
            code = 0;
            Log.e("code",""+code);
            DatabaseConnection dc = new DatabaseConnection(getApplicationContext());
            dc.read();
            int total = dc.gunlukKazanc();
            AlertDialog builder = new AlertDialog.Builder(this).setPositiveButton("Tamam",null).create();
            builder.setMessage("Günlük Toplam Tutar: "+total+" TL");
            builder.show();
            TextView textView = (TextView) builder.findViewById(android.R.id.message);
            textView.setTextSize(32);//to change font size
        }
    }
}
