package iride.app.com.iride;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class Home extends AppCompatActivity {

    private Button buttons[] = new Button[10];
    private Button tarife[] = new Button[5];
    private int tarifeUcret[] = {15,25,35,45};
    private int tarifeZaman[] = {15,30,45,60};
    private int adet = 0;
    private int tUcret = 0;
    private int selected = -1;
    private int selectedTarife = -1;
    private int fiyat;
    private TextView ucret, baslangic,bitis;
    private Timer timer;
    private EditText ekstra,editText2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        editText2 = (EditText) findViewById(R.id.editText2);

        buttons[0] = (Button) findViewById(R.id.buton1);
        buttons[1] = (Button) findViewById(R.id.buton2);
        buttons[2] = (Button) findViewById(R.id.buton3);
        buttons[3] = (Button) findViewById(R.id.buton4);
        buttons[4] = (Button) findViewById(R.id.buton5);
        buttons[5] = (Button) findViewById(R.id.buton6);
        buttons[6] = (Button) findViewById(R.id.buton7);
        buttons[7] = (Button) findViewById(R.id.buton8);
        buttons[8] = (Button) findViewById(R.id.buton9);
        buttons[9] = (Button) findViewById(R.id.buton10);

        for (int i=0;i<buttons.length;i++){
            buttons[i].setOnClickListener(new ButonClick(i));
        }

        tarife[0] = (Button) findViewById(R.id.tarife1);
        tarife[1] = (Button) findViewById(R.id.tarife2);
        tarife[2] = (Button) findViewById(R.id.tarife3);
        tarife[3] = (Button) findViewById(R.id.tarife4);
        tarife[4] = (Button) findViewById(R.id.aciktarife);

        for (int i=0;i<tarife.length;i++){
            tarife[i].setOnClickListener(new TarifeClick(i));
        }

        ekstra = (EditText)findViewById(R.id.ekstra);
        ucret = (TextView) findViewById(R.id.ucret);
        final TextView toplam = (TextView)findViewById(R.id.toplam);
        ekstra.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (!String.valueOf(ekstra.getText()).equals("-")&&charSequence.length()>0){
                    int total = fiyat+Integer.parseInt(String.valueOf(ekstra.getText()));
                    toplam.setText("Toplam: "+total+" TL");
                }
                else{
                    toplam.setText("Toplam: "+fiyat+" TL");
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        Button yonet = (Button)findViewById(R.id.yonet);
        yonet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),AdminLogIn.class);
                startActivity(i);
            }
        });

        final TextView tarih = (TextView)findViewById(R.id.tarih);
        baslangic = (TextView) findViewById(R.id.baslangic);
        bitis = (TextView) findViewById(R.id.bitis);

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);

        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);

        timer = new Timer();
        Task task = new Task(baslangic);
        timer.schedule(task,0,10000);

        tarih.setText(tarih.getText()+" "+day+"/"+month+"/"+year);


        Button yazdir = (Button)findViewById(R.id.yazdir);

        yazdir.setOnClickListener(new View.OnClickListener() {
            int j=0;
            @Override
            public void onClick(View view) {
                DatabaseConnection dc = new DatabaseConnection(getBaseContext());
                dc.open();
                dc.satisEkle(j,(String)tarih.getText(), String.valueOf(editText2.getText()),adet,selectedTarife,(String)baslangic.getText(),
                        (String)bitis.getText(),fiyat);
                j++;
            }
        });


        Button deneme = (Button) findViewById(R.id.button);
        deneme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),ViewDatabase.class);
                startActivity(i);
            }
        });

    }

    public void updateTime(final TextView baslangic){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                String val = (String)baslangic.getText();
                val = val.substring(0,17);
                if (minute<10){
                    baslangic.setText(val+" "+hour+":0"+minute);
                }
                else
                    baslangic.setText(val+" "+hour+":"+minute);

                if(bitis.length()>13&&selectedTarife<4){
                   // int hourF = Integer.parseInt(((String) baslangic.getText()).substring(18,20));
                   // int minuteF = Integer.parseInt(((String) baslangic.getText()).substring(21,23));
                    Log.e("sure",""+hour+""+minute);

                    if ((minute+tarifeZaman[selectedTarife])>=60){
                        minute = (minute+tarifeZaman[selectedTarife])%60;
                        hour = (hour+1)%24;
                    }
                    else {
                        minute = minute+tarifeZaman[selectedTarife];
                    }
                    String valu = (String)bitis.getText();
                    valu = valu.substring(0,13);
                    if (minute<10){
                        bitis.setText(valu+" "+hour+":0"+minute);
                    }
                    else
                        bitis.setText(valu+" "+hour+":"+minute);
                }

            }
        });

    }

    private class ButonClick implements View.OnClickListener{

        private int index;

        ButonClick(int index){
            this.index = index;

        }

        @Override
        public void onClick(View view) {

            adet = index+1;
            fiyat = adet*tUcret;
            ucret.setText("Ücret:\t"+fiyat+" TL");
            buttons[index].setBackgroundDrawable(getResources().getDrawable(R.drawable.seleected_round_button));
            if (selected>=0&&selected!=index){
                buttons[selected].setBackgroundDrawable(getResources().getDrawable(R.drawable.round_button));
            }

            selected = index;

        }
    }

    private class TarifeClick implements View.OnClickListener{

        private int index;

        TarifeClick(int index){

            this.index = index;


        }

        @Override
        public void onClick(View view) {

            if (index<4){
                tUcret = tarifeUcret[index];
                fiyat = adet*tUcret;
                ucret.setText("Ücret:\t"+fiyat+" TL");
                int hour,minute;
                if (baslangic.getText().length()>22){
                    hour = Integer.parseInt(((String) baslangic.getText()).substring(18,20));
                    minute = Integer.parseInt(((String) baslangic.getText()).substring(21,23));
                    Log.e("sure",""+hour+""+minute);
                }
                else{
                    hour = Integer.parseInt(((String) baslangic.getText()).substring(18,19));
                    minute = Integer.parseInt(((String) baslangic.getText()).substring(20,22));
                    Log.e("sure",""+hour+""+minute);
                }



                if ((minute+tarifeZaman[index])>=60){
                    minute = (minute+tarifeZaman[index])%60;
                    hour = (hour+1)%24;
                }
                else {
                    minute = minute+tarifeZaman[index];
                }
                String val = (String)bitis.getText();
                val = val.substring(0,13);
                if (minute<10){
                    bitis.setText(val+" "+hour+":0"+minute);
                }
                else
                    bitis.setText(val+" "+hour+":"+minute);
            }
            else{
                ucret.setText("Ücret:\t");
                bitis.setText("Bitiş Zamanı:");
            }
            tarife[index].setBackgroundDrawable(getResources().getDrawable(R.drawable.seleected_round_button));
            if (selectedTarife>=0&&selectedTarife!=index){
                tarife[selectedTarife].setBackgroundDrawable(getResources().getDrawable(R.drawable.corner_round_button));
            }

            selectedTarife = index;




        }
    }
    class Task extends TimerTask{

        private TextView tv;

        Task(TextView tv){

            this.tv = tv;

        }

        @Override
        public void run() {

            updateTime(tv);
        }
    }
}
