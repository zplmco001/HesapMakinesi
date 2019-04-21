package iride.app.com.iride;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class TarieUcretUpdate extends AppCompatActivity {


    private ImageView arttır[] = new ImageView[4];
    private ImageView azalt[] = new ImageView[4];
    private EditText tarife[] = new EditText[4];
    private Button kaydet ;
    private int ücret=0;

    private int tarifeUcret[] = new int[4];
    private final int tarifeZaman[] = {15,30,45,60};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarie_ucret_update);

        arttır[0]= (ImageView) findViewById(R.id.plus1);
        arttır[1]= (ImageView) findViewById(R.id.plus2);
        arttır[2]= (ImageView) findViewById(R.id.plus3);
        arttır[3]= (ImageView) findViewById(R.id.plus4);

        azalt[0] = (ImageView) findViewById(R.id.minus1);
        azalt[1] = (ImageView) findViewById(R.id.minus2);
        azalt[2] = (ImageView) findViewById(R.id.minus3);
        azalt[3] = (ImageView) findViewById(R.id.minus4);

        tarife[0] = (EditText) findViewById(R.id.tarife1);
        tarife[1] = (EditText) findViewById(R.id.tarife2);
        tarife[2] = (EditText) findViewById(R.id.tarife3);
        tarife[3] = (EditText) findViewById(R.id.tarife4);

        kaydet = (Button) findViewById(R.id.tarifeupd);

        for(int i=0 ; i<4; i++){
            arttır[i].setOnClickListener(new UpClick(i));

            azalt[i].setOnClickListener(new DownClick(i));
        }

        kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseConnection dc = new DatabaseConnection(getApplicationContext());
                dc.open();
                int t1=Integer.parseInt(tarife[0].getText().toString());
                int t2=Integer.parseInt(tarife[1].getText().toString());
                int t3=Integer.parseInt(tarife[2].getText().toString());
                int t4=Integer.parseInt(tarife[3].getText().toString());
                dc.tarifeUpdate(t1,t2,t3,t4);
                dc.close();
            }
        });



        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        /*Log.e("default ori",""+getWindowManager().getDefaultDisplay());

        Log.e("orientation",""+getWindowManager().getDefaultDisplay().getOrientation());
        Log.e("mode",""+getWindowManager().getDefaultDisplay().getDisplayId());
        Log.e("rotation",""+getWindowManager().getDefaultDisplay().getRotation());
        Log.e("width",""+getWindowManager().getDefaultDisplay().getWidth());
        Log.e("height",""+getWindowManager().getDefaultDisplay().getHeight());*/

        //if(  && getWindowManager().getDefaultDisplay().getWidth()<getWindowManager().getDefaultDisplay().getHeight())

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

    private class UpClick implements View.OnClickListener{

        private int index ;
        private int fiyat;


        UpClick(int index){
            this.index=index;
        }

        @Override
        public void onClick(View view) {
            if(tarife[index].getText().toString().equals("")){
                tarife[index].setText(String.valueOf(5));
            }else{
                fiyat= Integer.parseInt(tarife[index].getText().toString())+5;
                tarife[index].setText(String.valueOf(fiyat));
            }
        }
    }

    private class DownClick implements View.OnClickListener{

        private int index ;
        private int fiyat;


        DownClick(int index){
            this.index=index;
        }
        @Override
        public void onClick(View view) {
            if(!tarife[index].getText().toString().equals("") && Integer.parseInt(tarife[index].getText().toString())>=5){
                fiyat=Integer.parseInt(tarife[index].getText().toString())-5;
                tarife[index].setText(String.valueOf(fiyat));
            }
        }
    }
}
