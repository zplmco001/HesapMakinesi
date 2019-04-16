package iride.app.com.iride;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Home extends AppCompatActivity {

    private Button buttons[] = new Button[10];
    private Button tarife[] = new Button[5];
    private int tarifeUcret[] = {15,25,35,45};
    private int adet = 0;
    private int tUcret = 0;
    private int selected = -1;
    private int selectedTarife = -1;
    private TextView ucret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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

        ucret = (TextView) findViewById(R.id.ucret);

        Button yonet = (Button)findViewById(R.id.yonet);
        yonet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
            int fiyat = adet*tUcret;
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
                int fiyat = adet*tUcret;
                ucret.setText("Ücret:\t"+fiyat+" TL");
            }
            else{
                ucret.setText("Ücret:\t");
            }
            tarife[index].setBackgroundDrawable(getResources().getDrawable(R.drawable.seleected_round_button));
            if (selectedTarife>=0&&selectedTarife!=index){
                tarife[selectedTarife].setBackgroundDrawable(getResources().getDrawable(R.drawable.round_button));
            }

            selectedTarife = index;

        }
    }
}
