package iride.app.com.iride;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_database);


        dc = new DatabaseConnection(this);
        dc.read();
        list = dc.gunlukKayıtlar();
        dc.close();


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
                    startActivity(intent);
                }
            });
        }

    }
}
