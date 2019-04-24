package iride.app.com.iride;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class AdminPanel extends AppCompatActivity  {

    private Button changePassword;
    private Button changeName;
    private Button listDate;
    private Button tariharalik;
    private Button listall;
    private Button tarifedegis;
    private List<SatisInfo> list,tarihlist;
    private DatabaseConnection dc;
    static ListView listView;
    private String date;
    static TextView totalearn;
    private Toolbar toolbar2;
    Button gunpass;
    Button addpass;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        changePassword = (Button) findViewById(R.id.changepass);
        changeName = (Button) findViewById(R.id.changename);
        listDate = (Button) findViewById(R.id.listdate);
        tariharalik = (Button) findViewById(R.id.araliklist);
        listall = (Button) findViewById(R.id.listall);
        tarifedegis=(Button) findViewById(R.id.tarifedegis);
        listView = (ListView) findViewById(R.id.kayitadmin);

        totalearn = (TextView) findViewById(R.id.gettotal);

        dc = new DatabaseConnection(getApplicationContext());
        dc.read();
        totalearn.setText(String.valueOf(dc.toplamKazanc())+" TL");
        list = dc.tumKayıtlar();
        dc.close();
        ListAdapter adapter = new ListAdapter(getApplicationContext(),R.layout.list_adapter,list);
        listView.setAdapter(adapter);

        toolbar2 = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar2);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        toolbar2.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        final Calendar c = Calendar.getInstance();
        final int day=c.get(Calendar.DAY_OF_MONTH);
        final int month = c.get(Calendar.MONTH);
        final int year = c.get(Calendar.YEAR);


        tariharalik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),TarihAralik.class);
                startActivity(i);
            }
        });


        listall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dc = new DatabaseConnection(getApplicationContext());
                dc.read();
                list = dc.tumKayıtlar();
                totalearn.setText(String.valueOf(dc.toplamKazanc())+" TL");
                dc.close();
                ListAdapter adapter = new ListAdapter(getApplicationContext(),R.layout.list_adapter,list);
                listView.setAdapter(adapter);


            }
        });

        listDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                DatePickerDialog datePickerDialog = new DatePickerDialog(AdminPanel.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                        if(mDay<10){
                            if(mMonth<10){
                                date="0"+mDay+"/0"+(mMonth+1)+"/"+mYear;
                            }else{
                                date="0"+mDay+"/"+(mMonth+1)+"/"+mYear;
                            }
                        }else if(mMonth<10){
                            date=mDay+"/0"+(mMonth+1)+"/"+mYear;
                        }else{
                            date=mDay+"/"+(mMonth+1)+"/"+mYear;
                        }

                        DatabaseConnection dc = new DatabaseConnection(getApplicationContext());
                        dc.read();
                        tarihlist = dc.tarihGetir(date);
                        totalearn.setText(String.valueOf(dc.toplamKazanc(date))+" TL");
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
                /*Intent i = new Intent(getApplicationContext(),PasswordChange.class);
                startActivity(i);*/
                DialogView();

            }
        });

        changeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),AdminNameChange.class);
                startActivity(i);
            }
        });


        tarifedegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),TarieUcretUpdate.class);
                startActivity(i);
            }
        });

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }


    static Dialog dialog;


    public void DialogView(){
        dialog = new Dialog(AdminPanel.this);

        Window window = dialog.getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

        dialog.setTitle("Şifre Değiştir");

        dialog.setContentView(R.layout.pass_dialog);

        addpass = (Button) dialog.findViewById(R.id.goadpassch);
        gunpass = (Button) dialog.findViewById(R.id.gogunpassch);

        addpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),PasswordChange.class);
                startActivity(i);
            }
        });

        gunpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),GunlukPassChange.class);
                startActivity(i);
            }
        });


        dialog.show();
    }
}
