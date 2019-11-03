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
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

public class AdminPanel extends AppCompatActivity  {

    private Button changePassword;
    private Button changeName;
    private Button tarifedegis;
    private List<SatisInfo> list,tarihlist;
    private DatabaseConnection dc;
    static ListView listView;
    private String date;
    static TextView totalearn;
    private Toolbar toolbar2;
    Button gunpass;
    Button addpass;
    private Button back;


    /** GEREKSİZ BUTONLAR KALDIRILDI **/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        changePassword = (Button) findViewById(R.id.changepass);
        changeName = (Button) findViewById(R.id.changename);
        tarifedegis=(Button) findViewById(R.id.tarifedegis);
        listView = (ListView) findViewById(R.id.kayitadmin);

        totalearn = (TextView) findViewById(R.id.gettotal);

        dc = new DatabaseConnection(getApplicationContext());
        dc.read();
        totalearn.setText(String.valueOf(dc.toplamKazanc())+" TL");
        list = dc.tumKayıtlar();
        Collections.sort(list);
        dc.close();
        ListAdapter adapter = new ListAdapter(getApplicationContext(),R.layout.list_adapter,list);
        listView.setAdapter(adapter);

        /*toolbar2 = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar2);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        toolbar2.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/
        back = (Button)findViewById(R.id.backadmin);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });




        Calendar c = Calendar.getInstance();
        final int day=c.get(Calendar.DAY_OF_MONTH);
        final int month = c.get(Calendar.MONTH);
        final int year = c.get(Calendar.YEAR);


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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Home.timer.cancel();
    }

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
