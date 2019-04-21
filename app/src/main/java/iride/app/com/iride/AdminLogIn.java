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

        //Display id 0: DisplayInfo{"Built-in Screen", uniqueId "local:0", app 2560 x 1704, real 2560 x 1800, largest app 2560 x 2416,
        // smallest app 1800 x 1656, mode 1, defaultMode 1, modes [{id=1, width=1800, height=2560, fps=60.000004}], colorMode 0, supportedColorModes [0],
        // hdrCapabilities android.view.Display$HdrCapabilities@a69d6308, rotation 3, density 320 (320.0 x 320.0) dpi, layerStack 0, appVsyncOff 1000000,
        // presDeadline 16666666, type BUILT_IN, state ON, FLAG_SECURE, FLAG_SUPPORTS_PROTECTED_BUFFERS}, DisplayMetrics{density=2.0, width=2560, height=1704,
        // scaledDensity=2.0, xdpi=320.0, ydpi=320.0}, isValid=true

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        // Log.e("default ori",""+getWindowManager().getDefaultDisplay());

        /*Log.e("orientation",""+getWindowManager().getDefaultDisplay().getOrientation());
        Log.e("mode",""+getWindowManager().getDefaultDisplay().getDisplayId());
        Log.e("rotation",""+getWindowManager().getDefaultDisplay().getRotation());
        Log.e("width",""+getWindowManager().getDefaultDisplay().getWidth());
        Log.e("height",""+getWindowManager().getDefaultDisplay().getHeight());*/

        //if(  && getWindowManager().getDefaultDisplay().getWidth()<getWindowManager().getDefaultDisplay().getHeight())

        if (getWindowManager().getDefaultDisplay().getRotation()%2!=0){
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
