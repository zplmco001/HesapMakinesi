package iride.app.com.iride;

import android.app.Activity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class Home extends AppCompatActivity implements Runnable{

    private Button buttons[] = new Button[10];
    private Button tarife[] = new Button[5];
    private int tarifeUcret[] = {15,25,35,45};
    private int tarifeZaman[] = {15,30,45,60};
    private int adet = 0;
    private int tUcret = 0;
    private int selected = -1;
    private int selectedTarife = -1;
    private int fiyat;
    private TextView ucret,baslangic,bitis,fis,toplam,tarih;
    private Timer timer;
    private EditText ekstra,editText2;
    private int total,fisNo;
    private AutoCompleteTextView actv;
    private ImageButton arama;
    public SatisInfo info = null;
    private boolean printful = false;



    /**Bluetooth parametreleri**/
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    protected static final String TAG = "TAG";


    BluetoothAdapter mBluetoothAdapter;
    private UUID applicationUUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ProgressDialog mBluetoothConnectProgressDialog;
    private BluetoothSocket mBluetoothSocket;
    BluetoothDevice mBluetoothDevice;
    int printstat;
    /**Bluetooth parametreleri**/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final Button kaydet = (Button) findViewById(R.id.kaydet);

        Button yazdir = (Button)findViewById(R.id.yazdir);

        yazdir.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (editText2.getText().length()>0&&selected>=0&&selectedTarife>=0){

                    print();
                    if (printful){
                        kaydet();
                    }

                    kaydet.setVisibility(View.INVISIBLE);


                }else{
                    Toast.makeText(Home.this,"Lütfen eksik değerleri giriniz!",Toast.LENGTH_LONG).show();
                }

            }
        });


        kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText2.getText().length()>0&&selected>=0&&selectedTarife>=0){
                    kaydet();
                }else{
                    Toast.makeText(Home.this,"Lütfen eksik değerleri giriniz!",Toast.LENGTH_LONG).show();
                }
            }
        });


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

        DatabaseConnection dCon = new DatabaseConnection(getApplicationContext());
        dCon.open();
        tarifeUcret = dCon.getTarife();
        dCon.close();


        /**
         * Tarifeyi set eder.
         */
        for (int i=0;i<tarife.length;i++){
            if (i == 4){
                tarife[4].setText("Açık Hesap");
            }else{
                tarife[i].setText(tarifeZaman[i]+" Dakika "+tarifeUcret[i]+" TL");

            }
            tarife[i].setOnClickListener(new TarifeClick(i));
        }

        DatabaseConnection dc = new DatabaseConnection(getApplicationContext());
        dc.open();
        List<SatisInfo> list = dc.gunlukKayıtlar();

        FileWrite fw = new FileWrite(list);
        if (fw.isOldDay()){
            if (list.size()>0){
                fw.write();
                dc.gunlukTemizle();
            }
        }
        dc.close();


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
                    //}



                }
                else{
                    //if (info == null)
                        total = fiyat;
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
                i.putExtra("val","home");
                startActivity(i);
            }
        });

        tarih = (TextView)findViewById(R.id.tarih);
        baslangic = (TextView) findViewById(R.id.baslangic);
        bitis = (TextView) findViewById(R.id.bitis);

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);

        info = (SatisInfo) getIntent().getSerializableExtra("obje");
        if (info != null){
            Log.e("a",info.baslangıcSüre);
            Log.e("a",info.tarife+"");
            Log.e("a",info.bitisSüre);
            getResult(info);

        }else {
            timer = new Timer();
            final Task task = new Task(baslangic);
            timer.schedule(task,0,10000);
        }


        /**Bluetooth aktif et**/
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Message1", Toast.LENGTH_SHORT).show();
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent,
                        REQUEST_ENABLE_BT);
            } else {
                if (info == null){
                    ListPairedDevices();
                    Intent connectIntent = new Intent(getApplicationContext(),
                            DeviceListActivity.class);
                    startActivityForResult(connectIntent,
                            REQUEST_CONNECT_DEVICE);
                }

            }
        }

        /**Bluetooth aktif et**/



        /**Pushlama memete gönder*/
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
        /*Pushlama memete gönder**/







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
                    kaydet.setVisibility(View.VISIBLE);


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



        hideSoftKeyboard(Home.this);
        new View(this).getWindowToken();

        Button yenile = (Button) findViewById(R.id.yenile);
        yenile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kaydet.setVisibility(View.INVISIBLE);
                initialize();
                info = null;
                timer = new Timer();
                timer.schedule(new Task(baslangic),0,10000);
            }
        });

        if (info == null){
            kaydet.setVisibility(View.INVISIBLE);
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }


    void kaydet(){
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
                        total);
            }


        }else {
            String bitisS = "";
            if(selectedTarife!=4){
                bitisS = String.valueOf(bitis.getText()).substring(14);

            }

            dc.satisEkle(fisNo,String.valueOf(tarih.getText()).substring(7), String.valueOf(editText2.getText()),adet,selectedTarife,String.valueOf(baslangic.getText()).substring(13),
                    bitisS,total);

        }

        dc.close();
        initialize();
        info = null;
        timer = new Timer();
        timer.schedule(new Task(baslangic),0,10000);
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

            baslangic.setText("Çıkış Saati: "+info.baslangıcSüre);
            Log.e("çıkış",String.valueOf(baslangic.getText()));
            bitis.setText("Teslim Saati: "+info.bitisSüre);
            Log.e("teslim",String.valueOf(bitis.getText()));


            ucret.setText("ÜCRET :"+info.totalÜcret+" TL");
            total = info.totalÜcret;
            fiyat = info.totalÜcret;
            //total = fiyat;
            ekstra.setText("");
            toplam.setText("Toplam: "+total+" TL");
            //buttons[info.adet-1].callOnClick();
            //tarife[info.tarife].callOnClick();
            buttons[info.adet-1].setBackgroundDrawable(getResources().getDrawable(R.drawable.seleected_round_button));
            if (selected>=0&&selected!=info.adet-1){
                buttons[selected].setBackgroundDrawable(getResources().getDrawable(R.drawable.round_button));
            }
            selected = info.adet-1;
            adet = info.adet;

            tarife[info.tarife].setBackgroundDrawable(getResources().getDrawable(R.drawable.seleected_round_button));
            if (selectedTarife>=0&&selectedTarife!=info.tarife){
                tarife[selectedTarife].setBackgroundDrawable(getResources().getDrawable(R.drawable.corner_round_button));
            }

            selectedTarife = info.tarife;
            if (selectedTarife<4)
                tUcret = tarifeUcret[selectedTarife];


            toplam.setText("Toplam: "+total+" TL");


    }


    void initialize(){
        setFisNo();
        editText2.setText("");

        if (selected != -1){
            buttons[selected].setBackgroundDrawable(getResources().getDrawable(R.drawable.round_button));
        }
        if (selectedTarife != -1){
            tarife[selectedTarife].setBackgroundDrawable(getResources().getDrawable(R.drawable.corner_round_button));
        }


        bitis.setText("Teslim Saati:");
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
                val = val.substring(0,12);
                if (minute<10){
                    baslangic.setText(val+" "+hour+":0"+minute);
                    Log.e("sıkıntı11",String.valueOf(baslangic.getText()));
                }
                else
                    baslangic.setText(val+" "+hour+":"+minute);
                    Log.e("sıkıntı12",String.valueOf(baslangic.getText()));

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

    /** BLUETOOTH **/

    private void ListPairedDevices() {
        Set<BluetoothDevice> mPairedDevices = mBluetoothAdapter
                .getBondedDevices();
        if (mPairedDevices.size() > 0) {
            for (BluetoothDevice mDevice : mPairedDevices) {
                Log.v(TAG, "PairedDevices: " + mDevice.getName() + "  "
                        + mDevice.getAddress());
            }
        }
    }

    public void print(){
        p1();

        int TIME = 200; //5000 ms (5 Seconds)

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                p2(); //call function!

                printstat = 1;
            }
        }, TIME);
    }

    public void p1(){

        try {
            OutputStream os = mBluetoothSocket
                    .getOutputStream();

            String deneme = "üğışöçİŞĞÜÇÖ";
            String musname = "";
            String bisadet= "";
            String fisnumara = "";
            String cıkıstime = "";
            String teslimtime = "" ;
            String info= "";
            String checktop_status = "";
            String linespr="------------------\n";




            fisnumara = "\n\nFIS NO:"+fis.getText().toString().substring(7)+"\n\n";

            musname =  "ISIM  : "+editText2.getText().toString()+"\n\n";

            bisadet= "ADET  : "+(adet)+"\n\n";

            cıkıstime = "CIKIS SAAT :"+baslangic.getText().toString().substring(12)+"\n";


            teslimtime = "TESLIM SAAT:"+bitis.getText().toString().substring(13)+"\n\n";

            info = "           KIRALAMA DONUSUNDE\n     KIMLIGINIZI ALMAYI UNUTMAYINIZ.\n\n\n";

            checktop_status="\n\n\n";

            Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.fisbrand)).getBitmap();

            byte[] arrayOfByte1 = { 27, 33, 0 };

            byte[] format = { 27, 33, 0 };

            format[2] = ((byte) (0x20 | arrayOfByte1[2]));
// Width





            os.write(POS_PrintBMP(bitmap,384,0));
            os.write(format);
            os.write(fisnumara.getBytes());
            os.write(musname.getBytes());
            os.write(bisadet.getBytes());
            os.write(cıkıstime.getBytes());
            os.write(linespr.getBytes());
            os.write(teslimtime.getBytes());
            byte[] nrml = { 27, 33, 0 };

            os.write(nrml);
            os.write(info.getBytes());
            os.write(checktop_status.getBytes());




            //This is printer specific code you can comment ==== > Start

            // Setting height
            int gs = 29;
            os.write(intToByteArray(gs));
            int h = 104;
            os.write(intToByteArray(h));
            int n = 162;
            os.write(intToByteArray(n));

            // Setting Width
            int gs_width = 29;
            os.write(intToByteArray(gs_width));
            int w = 119;
            os.write(intToByteArray(w));
            int n_width = 2;
            os.write(intToByteArray(n_width));


        } catch (Exception e) {
            Log.e("PrintActivity", "Exe ", e);
        }
    }



    public void p2(){


        try {
            OutputStream os = mBluetoothSocket
                    .getOutputStream();

            int c1=29;
            int c2=86;
            int c3=66;

                    /*String checktop_status = "";
                    os.write(checktop_status.getBytes());*/

            os.write(intToByteArray(c1));
            os.write(intToByteArray(c2));
            os.write(intToByteArray(c3));


            String checktop_status = "\n";


            os.write(checktop_status.getBytes());



            printful = true;


        }catch (NullPointerException e1){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Lütfen Bluetooth Cihazı Bağlayınız.");
            builder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.show();
            printful = false;
        }
        catch (Exception e) {
            Log.e("PrintActivity", "Exe ", e);
        }
    }

    @Override
    public void run() {
        try {
            mBluetoothSocket = mBluetoothDevice
                    .createRfcommSocketToServiceRecord(applicationUUID);
            mBluetoothAdapter.cancelDiscovery();
            mBluetoothSocket.connect();
            mHandler.sendEmptyMessage(0);
        } catch (IOException eConnectException) {
            Log.d(TAG, "CouldNotConnectToSocket", eConnectException);
            closeSocket(mBluetoothSocket);
            return;
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mBluetoothConnectProgressDialog.dismiss();
        }
    };

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        try {
            if (mBluetoothSocket != null)
                mBluetoothSocket.close();
        } catch (Exception e) {
            Log.e("Tag", "Exe ", e);
        }
    }

    private void closeSocket(BluetoothSocket nOpenSocket) {
        try {
            nOpenSocket.close();
            Log.d(TAG, "SocketClosed");
        } catch (IOException ex) {
            Log.d(TAG, "CouldNotCloseSocket");
        }
    }

    public void onActivityResult(int mRequestCode, int mResultCode,
                                 Intent mDataIntent) {
        super.onActivityResult(mRequestCode, mResultCode, mDataIntent);

        switch (mRequestCode) {
            case REQUEST_CONNECT_DEVICE:
                if (mResultCode == Activity.RESULT_OK) {
                    Bundle mExtra = mDataIntent.getExtras();
                    String mDeviceAddress = mExtra.getString("DeviceAddress");
                    Log.v(TAG, "Coming incoming address " + mDeviceAddress);
                    mBluetoothDevice = mBluetoothAdapter
                            .getRemoteDevice(mDeviceAddress);
                    mBluetoothConnectProgressDialog = ProgressDialog.show(this,
                            "Cihaza bağlanıyor...", mBluetoothDevice.getName() + " : "
                                    + mBluetoothDevice.getAddress(), true, false);
                    Thread mBlutoothConnectThread = new Thread(this);
                    mBlutoothConnectThread.start();
                    // pairToDevice(mBluetoothDevice); This method is replaced by
                    // progress dialog with thread
                }
                break;

            case REQUEST_ENABLE_BT:
                if (mResultCode == Activity.RESULT_OK) {
                    ListPairedDevices();
                    Intent connectIntent = new Intent(getApplicationContext(),
                            DeviceListActivity.class);
                    startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE);
                } else {
                    Toast.makeText(getApplicationContext(), "Message", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public static byte intToByteArray(int value) {
        byte[] b = ByteBuffer.allocate(4).putInt(value).array();

        for (int k = 0; k < b.length; k++) {
            System.out.println("Selva  [" + k + "] = " + "0x"
                    + UnicodeFormatter.byteToHex(b[k]));
        }

        return b[3];
    }


    /** BLUETOOTH**/
    private class ButonClick implements View.OnClickListener{

        private int index;

        ButonClick(int index){
            this.index = index;

        }

        @Override
        public void onClick(View view) {

            if (selectedTarife<4) {
                adet = index + 1;
                //fiyat = adet * tUcret;
                if (info == null) {
                    fiyat = adet * tUcret;
                    total = fiyat;
                    Log.e("total1", "" + total);
                } else {
                    total = adet * tUcret;
                    Log.e("total2", "" + total);
                }

                /*if (ekstra.getText().length()==0){
                    total = fiyat;
                }else{
                    total = Integer.parseInt(String.valueOf(ekstra.getText()))+fiyat;
                }*/

                if (info != null) {
                    if (total - info.totalÜcret != 0) {
                        ekstra.setText((total - info.totalÜcret) + "");
                    } else {
                        ekstra.setText("");
                    }
                } else {
                    ucret.setText("Ücret:\t" + fiyat + " TL");
                }
                toplam.setText("TOPLAM: " + total + " TL");

            }



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

            Log.e("index",""+index);
            if (index<4){
                tUcret = tarifeUcret[index];
                if (info == null){
                    fiyat = adet*tUcret;
                    total = fiyat;
                    Log.e("total1",""+total);
                }else {
                    total = adet*tUcret;
                    Log.e("total2",""+total);
                }

                /*if (ekstra.getText().length()==0){
                    total = fiyat;
                }else{
                    total = Integer.parseInt(String.valueOf(ekstra.getText()))+fiyat;
                }*/

                if (info != null){
                    if (total-info.totalÜcret!=0){
                        ekstra.setText((total-info.totalÜcret)+"");
                    }else{
                        ekstra.setText("");
                    }
                }else{
                    ucret.setText("Ücret:\t"+fiyat+" TL");
                }
                toplam.setText("TOPLAM: "+total+" TL");

                int hour,minute;
                if (baslangic.getText().length()>17){
                    hour = Integer.parseInt(((String) baslangic.getText()).substring(13,15));
                    minute = Integer.parseInt(((String) baslangic.getText()).substring(16,18));
                }
                else{
                    hour = Integer.parseInt(((String) baslangic.getText()).substring(13,14));
                    minute = Integer.parseInt(((String) baslangic.getText()).substring(15,17));
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
                    bitis.setText("Teslim Saati:");
                    ucret.setText("Ücret: "+info.totalÜcret+" TL");
                    total = info.totalÜcret;
                    ekstra.setText("");
                    toplam.setText("Toplam: "+total+" TL");
                }
                else{
                    ucret.setText("Ücret:\t");
                    bitis.setText("Teslim Saati:");
                    toplam.setText("Toplam:");
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




    /**Print Logo**/

    public static byte[] POS_PrintBMP(Bitmap var0, int var1, int var2) {
        var1 = (var1 + 7) / 8 * 8;
        int var3 = (var0.getHeight() * var1 / var0.getWidth() + 7) / 8;
        Bitmap var4 = var0;
        if (var0.getWidth() != var1) {
            var4 = resizeImage(var0, var1, var3 * 8);
        }

        return eachLinePixToCmd(thresholdToBWPic(toGrayscale(var4)), var1, var2);
    }

    public static Bitmap resizeImage(Bitmap var0, int var1, int var2) {
        int var3 = var0.getWidth();
        int var4 = var0.getHeight();
        float var5 = (float)var1 / (float)var3;
        float var6 = (float)var2 / (float)var4;
        Matrix var7 = new Matrix();
        var7.postScale(var5, var6);
        return Bitmap.createBitmap(var0, 0, 0, var3, var4, var7, true);
    }

    public static Bitmap toGrayscale(Bitmap var0) {
        int var1 = var0.getHeight();
        Bitmap var2 = Bitmap.createBitmap(var0.getWidth(), var1, Bitmap.Config.ARGB_8888);
        Canvas var3 = new Canvas(var2);
        Paint var4 = new Paint();
        ColorMatrix var5 = new ColorMatrix();
        var5.setSaturation(0.0F);
        var4.setColorFilter(new ColorMatrixColorFilter(var5));
        var3.drawBitmap(var0, 0.0F, 0.0F, var4);
        return var2;
    }


    public static byte[] thresholdToBWPic(Bitmap var0) {
        int[] var1 = new int[var0.getWidth() * var0.getHeight()];
        byte[] var2 = new byte[var0.getWidth() * var0.getHeight()];
        var0.getPixels(var1, 0, var0.getWidth(), 0, 0, var0.getWidth(), var0.getHeight());
        format_K_threshold(var1, var0.getWidth(), var0.getHeight(), var2);
        return var2;
    }

    private static void format_K_threshold(int[] var0, int var1, int var2, byte[] var3) {
        int var4 = 0;
        int var5 = 0;

        int var6;
        int var7;
        for(var6 = 0; var6 < var2; ++var6) {
            for(var7 = 0; var7 < var1; ++var7) {
                var4 += var0[var5] & 255;
                ++var5;
            }
        }

        var4 = var4 / var2 / var1;
        var5 = 0;

        for(var6 = 0; var6 < var2; ++var6) {
            for(var7 = 0; var7 < var1; ++var7) {
                if ((var0[var5] & 255) > var4) {
                    var3[var5] = (byte)0;
                } else {
                    var3[var5] = (byte)1;
                }

                ++var5;
            }
        }
    }




    private static int[] p0;
    private static int[] p1;
    private static int[] p2;
    private static int[] p3;
    private static int[] p4;
    private static int[] p5;
    private static int[] p6;
    private static final byte[] chartobyte;


    static {
        int[] var0 = new int[]{0, 128};
        p0 = var0;
        var0 = new int[]{0, 64};
        p1 = var0;
        var0 = new int[]{0, 32};
        p2 = var0;
        var0 = new int[]{0, 16};
        p3 = var0;
        var0 = new int[]{0, 8};
        p4 = var0;
        var0 = new int[]{0, 4};
        p5 = var0;
        var0 = new int[]{0, 2};
        p6 = var0;
        byte[] var1 = new byte[23];
        var1[1] = (byte)1;
        var1[2] = (byte)2;
        var1[3] = (byte)3;
        var1[4] = (byte)4;
        var1[5] = (byte)5;
        var1[6] = (byte)6;
        var1[7] = (byte)7;
        var1[8] = (byte)8;
        var1[9] = (byte)9;
        var1[17] = (byte)10;
        var1[18] = (byte)11;
        var1[19] = (byte)12;
        var1[20] = (byte)13;
        var1[21] = (byte)14;
        var1[22] = (byte)15;
        chartobyte = var1;
    }



    public static byte[] eachLinePixToCmd(byte[] var0, int var1, int var2) {
        int var3 = var0.length / var1;
        int var4 = var1 / 8;
        byte[] var5 = new byte[(var4 + 8) * var3];
        int var6 = 0;

        for(var1 = 0; var1 < var3; ++var1) {
            int var7 = var1 * (var4 + 8);
            var5[var7 + 0] = (byte)29;
            var5[var7 + 1] = (byte)118;
            var5[var7 + 2] = (byte)48;
            var5[var7 + 3] = (byte)((byte)(var2 & 1));
            var5[var7 + 4] = (byte)((byte)(var4 % 256));
            var5[var7 + 5] = (byte)((byte)(var4 / 256));
            var5[var7 + 6] = (byte)1;
            var5[var7 + 7] = (byte)0;

            for(int var8 = 0; var8 < var4; ++var8) {
                var5[var7 + 8 + var8] = (byte)((byte)(p0[var0[var6]] + p1[var0[var6 + 1]] + p2[var0[var6 + 2]] + p3[var0[var6 + 3]] + p4[var0[var6 + 4]] + p5[var0[var6 + 5]] + p6[var0[var6 + 6]] + var0[var6 + 7]));
                var6 += 8;
            }
        }

        return var5;
    }



}