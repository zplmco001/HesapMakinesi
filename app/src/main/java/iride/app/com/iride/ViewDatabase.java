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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ViewDatabase extends AppCompatActivity {

    private List<SatisInfo> list;
    DatabaseConnection dc;
    ListView listView;
    SatisInfo satisInfo;
    int fisNo;
    private Button goruntule;
    public static int code = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_database);


        dc = new DatabaseConnection(this);
        dc.read();
        list = dc.gunlukKayıtlar();
        dc.close();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        if(list.size()==0){
            Toast.makeText(this,"Liste boş!",Toast.LENGTH_SHORT).show();
            finish();
        }else{
            ListAdapter adapter = new ListAdapter(this,R.layout.list_adapter,list);
            listView = (ListView) findViewById(R.id.listView);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    satisInfo = list.get(i);
                    //Bundle bundle = Home.bundle;
                    //Home.satisInfo = satisInfo;
                    //Object o = satisInfo;
                    //Log.e("af",Home.satisInfo.baslangıcSüre);
                    Intent intent = new Intent(ViewDatabase.this,Home.class);
                    intent.putExtra("obje",satisInfo);
                    Log.e("info",""+satisInfo.totalÜcret);
                    startActivity(intent);
                }
            });
        }

        goruntule = (Button) findViewById(R.id.goruntule);
        goruntule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),AdminLogIn.class);
                i.putExtra("val","viewdb");
                startActivity(i);
            }
        });

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
