package iride.app.com.iride;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static iride.app.com.iride.AdminPanel.listView;

public class TarihAralik extends AppCompatActivity {

    private ImageView setbastarih,setbittarih;
    private Button listdiftarih;
    private TextView bastarih,bittarih;
    private String trh1,trh2;
    private List<SatisInfo> list = new ArrayList<>();
    private ImageView exit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarih_aralik);

        setbastarih = (ImageView) findViewById(R.id.setbastarih);
        setbittarih = (ImageView) findViewById(R.id.setbittarih);

        listdiftarih = (Button) findViewById(R.id.listdiftarih);

        bastarih = (TextView) findViewById(R.id.bastarih);
        bittarih = (TextView) findViewById(R.id.bittarih);

        exit = (ImageView) findViewById(R.id.tariharaexit);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        final Calendar c = Calendar.getInstance();
        final int day=c.get(Calendar.DAY_OF_MONTH);
        final int month = c.get(Calendar.MONTH);
        final int year = c.get(Calendar.YEAR);

        setbastarih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(TarihAralik.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                        if(mDay<10){
                            if(mMonth<10){
                                trh1="0"+mDay+"/0"+(mMonth+1)+"/"+mYear;
                            }else{
                                trh1="0"+mDay+"/"+(mMonth+1)+"/"+mYear;
                            }
                        }else if(mMonth<10){
                            trh1=mDay+"/0"+(mMonth+1)+"/"+mYear;
                        }else{
                            trh1=mDay+"/"+(mMonth+1)+"/"+mYear;
                        }
                        bastarih.setText(trh1);
                    }
                },year,month,day);

                dialog.show();
            }
        });


        setbittarih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(TarihAralik.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                        if(mDay<10){
                            if(mMonth<10){
                                trh2="0"+mDay+"/0"+(mMonth+1)+"/"+mYear;
                            }else{
                                trh2="0"+mDay+"/"+(mMonth+1)+"/"+mYear;
                            }
                        }else if(mMonth<10){
                            trh2=mDay+"/0"+(mMonth+1)+"/"+mYear;
                        }else{
                            trh2=mDay+"/"+(mMonth+1)+"/"+mYear;
                        }
                        bittarih.setText(trh2);
                    }
                },year,month,day);

                dialog.show();
            }

        });

        listdiftarih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseConnection dc = new DatabaseConnection(getApplicationContext());
                dc.read();
                list = dc.tarihAralikGetir(trh1,trh2);
                Log.e("liste boyutu",""+list.size());
                dc.close();
                ListAdapter adapter = new ListAdapter(getApplicationContext(),R.layout.list_adapter,list);
                listView.setAdapter(adapter);
                finish();
            }
        });


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);



        if (getWindowManager().getDefaultDisplay().getWidth()<getWindowManager().getDefaultDisplay().getHeight()){
            int width = dm.widthPixels;
            int height = dm.heightPixels;

            getWindow().setLayout((int) (width*.85),(int) (height*.5));
            getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.corner_round_button));
        }else{
            int width = dm.widthPixels;
            int height = dm.heightPixels;

            getWindow().setLayout((int) (width*.5),(int) (height*.85));
            getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.corner_round_button));
        }


    }


}
