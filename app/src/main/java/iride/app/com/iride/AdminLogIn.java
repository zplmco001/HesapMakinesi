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
import android.widget.Toast;

import java.sql.SQLOutput;


public class AdminLogIn extends AppCompatActivity {


    private EditText username,password;
    private Button login,cancel;
    private String name,key;
    private boolean checkResult;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_log_in);





        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        login = (Button) findViewById(R.id.login);
        cancel =(Button) findViewById(R.id.cancel);




        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AdminLogIn.this);
                DatabaseConnection dc = new DatabaseConnection(getApplicationContext());

                name=String.valueOf(username.getText());
                key=String.valueOf(password.getText());

                if(name.equals("")){
                    builder.setMessage("Kullanıcı adı giriniz!");
                    builder.show();
                }else if(key.equals("")){
                    builder.setMessage("Şifre giriniz!");
                    builder.show();
                }else{                    dc.open();
                    checkResult=dc.checkUser(name,key);
                    Log.e("dönen sonuc"," "+dc.checkUser(name,key));
                    dc.close();

                    if(checkResult){
                        Intent i = new Intent(getApplicationContext(),AdminPanel.class);
                        Toast.makeText(getApplicationContext(),"Giriş yapıldı!",Toast.LENGTH_SHORT).show();
                        startActivity(i);
                        finish();

                    }else{
                        Toast.makeText(getApplicationContext(),"Kullanıcı adı veya şifre yanlış!",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        if (getWindowManager().getDefaultDisplay().getRotation()%2==0){
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
