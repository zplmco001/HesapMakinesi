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


    private EditText currentPass,newPass,newPassAgain;
    private Button save,cancel;

    private String cPass,nPass,nPassAg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);

        currentPass = (EditText) findViewById(R.id.currentpass);
        newPass = (EditText) findViewById(R.id.newpass);
        newPassAgain = (EditText) findViewById(R.id.newpassagain);

        save = (Button) findViewById(R.id.save);
        cancel = (Button) findViewById(R.id.cancel);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cPass=String.valueOf(currentPass.getText());
                nPass=String.valueOf(newPass.getText());
                nPassAg=String.valueOf(newPassAgain.getText());

                if(nPass.length()<4){
                    Toast.makeText(getApplicationContext(),"Şifreniz en az 4 karakter olmak zorundadır!",Toast.LENGTH_SHORT).show();
                }else{
                    DatabaseConnection dc = new DatabaseConnection(getApplicationContext());
                    dc.open();
                    if(!dc.checkCurrentPassword(cPass)){
                        Toast.makeText(getApplicationContext(),"Yanlış şifre girdiniz!",Toast.LENGTH_SHORT).show();
                    }else if(!nPass.equals(nPassAg) ){
                        Toast.makeText(getApplicationContext(),"Yeni şifreyi kontrol edin!",Toast.LENGTH_SHORT).show();
                    }else{
                        dc.changePassword(nPass);
                        Toast.makeText(getApplicationContext(),"Şifre değiştirildi",Toast.LENGTH_SHORT).show();
                        dc.close();
                        finish();
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

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*.5),(int) (height*.85));
    }
}
