package iride.app.com.iride;

import android.app.Activity;
import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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
    private TextView ucret, baslangic,bitis,fis,toplam;
    private Timer timer;
    private EditText ekstra,editText2;
    private int total,fisNo;
    private AutoCompleteTextView actv;
    private ImageButton arama;
    public SatisInfo info = null;
    public static Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        editText2 = (EditText) findViewById(R.id.editText2);
        actv = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView);
        actv.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                Log.e("","aradı");
                return true;
            }
        });

        fis = (TextView) findViewById(R.id.fis);

        setFisNo();

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
        toplam = (TextView)findViewById(R.id.toplam);
        ekstra.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (!String.valueOf(ekstra.getText()).equals("-")&&charSequence.length()>0){
                    //if (info == null){
                        total = fiyat+Integer.parseInt(String.valueOf(ekstra.getText()));
                        toplam.setText("Toplam: "+total+" TL");
                    //}else {

                    //}


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
                DatabaseConnection dc = new DatabaseConnection(getApplicationContext());
                dc.open();
                //dc.gunlukKaydet(getApplicationContext());
                dc.close();
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

        timer = new Timer();
        Task task = new Task(baslangic);
        timer.schedule(task,0,10000);


        /**Pushlama memete gönder
        if(day<10){

            if(month<10){
                tarih.setText(tarih.getText()+" 0"+day+"/0"+month+"/"+year);
            }else{
                tarih.setText(tarih.getText()+" 0"+day+"/"+month+"/"+year);
            }

        }else if(month<10){
            tarih.setText(tarih.getText()+" "+day+"/0"+month+"/"+year);
        }else{
            tarih.setText(tarih.getText()+" "+day+"/"+month+"/"+year);
        }
        Pushlama memete gönder**/


        Button yazdir = (Button)findViewById(R.id.yazdir);

        yazdir.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                DatabaseConnection dc = new DatabaseConnection(getBaseContext());
                dc.open();
                List<Integer> list = dc.fisNos("gunluk_info");
                if (list.contains(fisNo)){

                    if (selectedTarife!=4){
                        dc.kayitGuncelle(fisNo, adet,selectedTarife,
                                String.valueOf(bitis.getText()).substring(14),total);
                    }else {
                        dc.kayitGuncelle(fisNo, adet,selectedTarife,
                               "",
                                Integer.parseInt(String.valueOf(toplam.getText()).substring(8,toplam.getText().length()-3)));
                    }


                }else {
                    String bitisS = "";
                    if(selectedTarife!=4){
                        bitisS = String.valueOf(bitis.getText()).substring(13);

                    }

                    dc.satisEkle(fisNo,String.valueOf(tarih.getText()).substring(7), String.valueOf(editText2.getText()),adet,selectedTarife,String.valueOf(baslangic.getText()).substring(18),
                            bitisS,total);

                }

                dc.close();
                initialize();
                info = null;
                timer = new Timer();
                timer.schedule(new Task(baslangic),0,10000);


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

        arama = (ImageButton)findViewById(R.id.arama);
        arama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    int val = Integer.parseInt(String.valueOf(actv.getText()));
                    DatabaseConnection dc = new DatabaseConnection(getApplicationContext());
                    dc.open();
                    info = dc.fisNoSorgu(val);
                    Log.e("as", "" + info.fisNo);
                    dc.close();

                    getResult(info);


                }
                catch (Exception e){
                    Toast.makeText(Home.this,"SONUÇ BULUNAMADI!",Toast.LENGTH_LONG).show();
                }

                InputMethodManager imm = (InputMethodManager) Home.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                //Find the currently focused view, so we can grab the correct window token from it.
                //If no view currently has focus, create a new one, just so we can grab a window token from it
                if (view == null) {
                    view = new View(Home.this);
                }
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            }
        });

        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.constraint);

        constraintLayout.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftKeyboard(Home.this);
                return false;
            }
        });

       // try {

        info = (SatisInfo) getIntent().getSerializableExtra("obje");
        if (info != null){
            Log.e("a",info.baslangıcSüre);
            Log.e("a",info.tarife+"");
            Log.e("a",info.bitisSüre);
            getResult(info);
        }

        hideSoftKeyboard(Home.this);
        new View(this).getWindowToken();
        //}catch (Exception e){

        //}

    }

    void getResult(SatisInfo info){
        //try{

            editText2.setText(info.müsteriİsim);
            fisNo = info.fisNo;



            if (fisNo<10){
                fis.setText("Fiş No: 0000"+fisNo);
            }else if(fisNo<100){
                fis.setText("Fiş No: 000"+fisNo);
            }
            else if(fisNo<1000){
                fis.setText("Fiş No: 00"+fisNo);
            }
            else if(fisNo<10000){
                fis.setText("Fiş No: 0"+fisNo);
            }

            baslangic.setText("Başlangıç Zamanı: "+info.baslangıcSüre);
            bitis.setText("Bitiş Zamanı: "+info.bitisSüre);

            buttons[info.adet-1].callOnClick();
            tarife[info.tarife].callOnClick();
            ucret.setText("ÜCRET :"+info.totalÜcret+" TL");
            fiyat = info.totalÜcret;
            timer.cancel();

        //}catch (Exception e){
          //  Toast.makeText(Home.this,"Sonuç bulunamadı",Toast.LENGTH_LONG).show();
       // }
    }


    void initialize(){
        setFisNo();
        editText2.setText("");
        buttons[selected].setBackgroundDrawable(getResources().getDrawable(R.drawable.round_button));
        tarife[selectedTarife].setBackgroundDrawable(getResources().getDrawable(R.drawable.corner_round_button));

        bitis.setText("Bitiş Zamanı:");
        ekstra.setText("");
        total = 0;
        toplam.setText("Toplam:");
        ucret.setText("Ücret:");
        adet = 0;
        tUcret = 0;
        selected = -1;
        selectedTarife = -1;
        updateTime(baslangic);

    }

    public void hideSoftKeyboard(Activity activity) {
        /*InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);*/
        InputMethodManager imm = (InputMethodManager) getSystemService(activity.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null)
            imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    void setFisNo(){
        DatabaseConnection dc = new DatabaseConnection(getApplicationContext());
        dc.open();
        List<Integer> list = dc.fisNos("gunluk_info");
        List<Integer> genelList = dc.fisNos("satis_info");


        if (list.size()==0){

            if (genelList.size()!=0){
                fisNo = genelList.get(genelList.size()-1)+1;
            }else{
                fisNo = 1;
            }


        }else{
            if (fisNo == 10000){
                fisNo = 1;
            }
            else{
                fisNo = list.get(list.size()-1)+1;
            }

        }

        ArrayList<String> adaptor = new ArrayList<>();

        for (int i=0;i<list.size();i++){
            if (list.get(i)<10){
                adaptor.add("0000"+list.get(i));
            }else if(list.get(i)<100){
                adaptor.add("000"+list.get(i));
            }
            else if(list.get(i)<1000){
                adaptor.add("00"+list.get(i));
            }
            else if(list.get(i)<10000){
                adaptor.add("0"+list.get(i));
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item, adaptor);
        actv.setAdapter(adapter);


        if (fisNo<10){
            fis.setText("Fiş No: 0000"+fisNo);
        }else if(fisNo<100){
            fis.setText("Fiş No: 000"+fisNo);
        }
        else if(fisNo<1000){
            fis.setText("Fiş No: 00"+fisNo);
        }
        else if(fisNo<10000){
            fis.setText("Fiş No: 0"+fisNo);
        }
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
                if (!(String.valueOf(ekstra.getText()).equals(""))){

                }
                total = fiyat;
                if (info != null){
                    ekstra.setText((fiyat-info.totalÜcret)+"");
                }else{
                    ucret.setText("Ücret:\t"+fiyat+" TL");
                }
                toplam.setText("TOPLAM: "+total+" TL");

                int hour,minute;
                if (baslangic.getText().length()>22){
                    hour = Integer.parseInt(((String) baslangic.getText()).substring(18,20));
                    minute = Integer.parseInt(((String) baslangic.getText()).substring(21,23));
                }
                else{
                    hour = Integer.parseInt(((String) baslangic.getText()).substring(18,19));
                    minute = Integer.parseInt(((String) baslangic.getText()).substring(20,22));
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
                if (info != null){
                    ucret.setText("Ücret: "+info.totalÜcret);
                }
                else{
                    ucret.setText("Ücret:\t");
                    bitis.setText("Bitiş Zamanı:");
                    fiyat = 0;
                }


            }
            tarife[index].setBackgroundDrawable(getResources().getDrawable(R.drawable.seleected_round_button));
            if (selectedTarife>=0&&selectedTarife!=index){
                tarife[selectedTarife].setBackgroundDrawable(getResources().getDrawable(R.drawable.corner_round_button));
            }

            selectedTarife = index;




        }
    }
    private class Task extends TimerTask{

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
