package iride.app.com.iride;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class AdminPanel extends AppCompatActivity {

    private Button changePassword;
    private Button changeName;
    private Button addAdmin;
    private List<SatisInfo> list;
    private DatabaseConnection dc;
    private ListView listView;
    private SatisInfo satisInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        changePassword = (Button) findViewById(R.id.changepass);
        changeName = (Button) findViewById(R.id.changename);
        addAdmin = (Button) findViewById(R.id.addadmin);

        dc = new DatabaseConnection(this);
        dc.read();
        list = dc.tumKayıtlar();
        dc.close();

        if(list.size()==1){
            Toast.makeText(this,"Liste boş!",Toast.LENGTH_SHORT).show();
            finish();
        }else{
            ListAdapter adapter = new ListAdapter(this,R.layout.list_adapter,list);
            listView = (ListView) findViewById(R.id.kayitadmin);
            listView.setAdapter(adapter);
        }



        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),PasswordChange.class);
                startActivity(i);
            }
        });

        changeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),AdminNameChange.class);
                startActivity(i);
            }
        });

        addAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),AddAdmin.class);
                startActivity(i);
            }
        });
    }
}
