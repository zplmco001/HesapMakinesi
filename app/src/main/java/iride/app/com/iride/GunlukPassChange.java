package iride.app.com.iride;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import static iride.app.com.iride.AdminPanel.dialog;

public class GunlukPassChange extends AppCompatActivity {

    private EditText guncurrpass,gunnewpass;
    private Button save;
    private ImageView exit;
    private String newpass,newpassag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gunluk_pass_change);

        guncurrpass = (EditText) findViewById(R.id.guncurrpass);
        gunnewpass = (EditText) findViewById(R.id.gunnewpass);
        save = (Button) findViewById(R.id.savegunpass);
        exit = (ImageView) findViewById(R.id.gunpassexit);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                newpass = String.valueOf(guncurrpass.getText());
                newpassag= String.valueOf(gunnewpass.getText());

                DatabaseConnection dc = new DatabaseConnection(getApplicationContext());
                dc.open();

                if(newpass.length()<4){
                    Toast.makeText(getApplicationContext(),"Şifreniz en az 4 karakter olmak zorundadır!",Toast.LENGTH_SHORT).show();
                }else{
                    if(!newpass.equals(newpassag)){
                        Toast.makeText(getApplicationContext(),"Yeni şifreyi kontrol edin!",Toast.LENGTH_SHORT).show();
                    }else{
                        dc.changePassword("Gunluk",newpass);
                        Toast.makeText(getApplicationContext(),"Şifre değiştirildi",Toast.LENGTH_SHORT).show();
                        dc.close();
                        dialog.dismiss();
                        finish();
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
