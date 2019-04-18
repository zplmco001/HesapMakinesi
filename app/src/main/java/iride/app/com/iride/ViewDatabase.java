package iride.app.com.iride;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ViewDatabase extends AppCompatActivity {

    private List<SatisInfo> list;
    DatabaseConnection dc;
    ListView listView;
    SatisInfo satisInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_database);


        dc = new DatabaseConnection(this);
        dc.read();
        list = dc.gunlukKayıtlar();
        dc.close();


        if(list.size()==1){
            Toast.makeText(this,"Liste boş!",Toast.LENGTH_SHORT).show();
            finish();
        }else{
            ListAdapter adapter = new ListAdapter(this,R.layout.list_adapter,list);
            listView = (ListView) findViewById(R.id.listView);
            listView.setAdapter(adapter);
        }
    }
}
