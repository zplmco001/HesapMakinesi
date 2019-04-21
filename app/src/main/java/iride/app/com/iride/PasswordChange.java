package iride.app.com.iride;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PasswordChange extends AppCompatActivity {


    private EditText passChangeName,currentPass,newPass,newPassAgain;
    private Button save,cancel;

    private String passChName,cPass,nPass,nPassAg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);

        passChangeName = (EditText) findViewById(R.id.passchangename);
        currentPass = (EditText) findViewById(R.id.currentpass);
        newPass = (EditText) findViewById(R.id.newpass);
        newPassAgain = (EditText) findViewById(R.id.newpassagain);

        save = (Button) findViewById(R.id.save);
        cancel = (Button) findViewById(R.id.cancel);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                passChName=String.valueOf(passChangeName.getText());
                cPass=String.valueOf(currentPass.getText());
                nPass=String.valueOf(newPass.getText());
                nPassAg=String.valueOf(newPassAgain.getText());

                DatabaseConnection dc = new DatabaseConnection(getApplicationContext());
                dc.open();
                if(dc.checkUsername(passChName)){
                    Toast.makeText(getApplicationContext(),"Böyle bir kullanıcı yok!",Toast.LENGTH_SHORT).show();
                }else{
                    if(nPass.length()<4){
                        Toast.makeText(getApplicationContext(),"Şifreniz en az 4 karakter olmak zorundadır!",Toast.LENGTH_SHORT).show();
                    }else{
                        if(!dc.checkCurrentPassword(cPass)){
                            Toast.makeText(getApplicationContext(),"Yanlış şifre girdiniz!",Toast.LENGTH_SHORT).show();
                        }else if(!nPass.equals(nPassAg) ){
                            Toast.makeText(getApplicationContext(),"Yeni şifreyi kontrol edin!",Toast.LENGTH_SHORT).show();
                        }else{
                            dc.changePassword(passChName,nPass);
                            Toast.makeText(getApplicationContext(),"Şifre değiştirildi",Toast.LENGTH_SHORT).show();
                            dc.close();
                            finish();
                        }
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
