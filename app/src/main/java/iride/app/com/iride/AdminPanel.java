package iride.app.com.iride;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class AdminPanel extends AppCompatActivity {

    private Button changePassword;
    private Button changeName;
    private Button addAdmin;
    private Button listDate;
    private Button tariharalik;
    private Button listall;
    private List<SatisInfo> list,tarihlist,listaralik;
    private DatabaseConnection dc;
    private ListView listView;
    private SatisInfo satisInfo;
    private String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        changePassword = (Button) findViewById(R.id.changepass);
        changeName = (Button) findViewById(R.id.changename);
        addAdmin = (Button) findViewById(R.id.addadmin);
        listDate = (Button) findViewById(R.id.listdate);
        tariharalik = (Button) findViewById(R.id.araliklist);
        listall = (Button) findViewById(R.id.listall);
        listView = (ListView) findViewById(R.id.kayitadmin);


        tariharalik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        listall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dc = new DatabaseConnection(getApplicationContext());
                dc.read();
                list = dc.tumKayıtlar();
                dc.close();
                ListAdapter adapter = new ListAdapter(getApplicationContext(),R.layout.list_adapter,list);
                listView.setAdapter(adapter);

            }
        });

        listDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();
                int day=c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AdminPanel.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                        date =mDay+"/"+(mMonth+1)+"/"+mYear;

                        DatabaseConnection dc = new DatabaseConnection(getApplicationContext());
                        dc.read();
                        tarihlist = dc.tarihGetir(date);
                        dc.close();

                        ListAdapter adapter = new ListAdapter(getApplicationContext(),R.layout.list_adapter,tarihlist);
                        listView.setAdapter(adapter);


                    }
                },year,month,day);


                datePickerDialog.show();

            }
        });

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
