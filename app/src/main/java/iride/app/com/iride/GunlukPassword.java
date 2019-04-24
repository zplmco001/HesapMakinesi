package iride.app.com.iride;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class GunlukPassword extends AppCompatActivity {

    private EditText password;
    private Button login;
    private String name,key;
    private boolean checkResult;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gunluk_password);


        img = (ImageView)findViewById(R.id.addlogexitGun);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        password = (EditText) findViewById(R.id.passwordGun);

        login = (Button) findViewById(R.id.loginGun);



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(GunlukPassword.this);
                DatabaseConnection dc = new DatabaseConnection(getApplicationContext());

                key=String.valueOf(password.getText());


                if(key.equals("")){
                    builder.setMessage("Şifre giriniz!");
                    builder.show();
                }else{
                    dc.open();
                    name = "Gunluk";
                    checkResult=dc.checkUser(name,key);
                    Log.e("dönen sonuc"," "+dc.checkUser(name,key));
                    dc.close();


                    if(checkResult){

                        ViewDatabase.code = 256;
                        onBackPressed();


                    }else{
                        Toast.makeText(getApplicationContext(),"Şifre yanlış!",Toast.LENGTH_LONG).show();
                    }
                }
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
