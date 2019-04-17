package iride.app.com.iride;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddAdmin extends AppCompatActivity {

    private EditText newName,newPass,newPassAg;
    private Button create,exit;
    private String newAdminName,newPassword,newPasswordAg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_admin);

        newName = (EditText) findViewById(R.id.addname);
        newPass = (EditText) findViewById(R.id.addpass);
        newPassAg = (EditText) findViewById(R.id.addpassagain);

        create = (Button) findViewById(R.id.create);
        exit = (Button) findViewById(R.id.exit);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newAdminName= String.valueOf(newName.getText());
                newPassword = String.valueOf(newPass.getText());
                newPasswordAg = String.valueOf(newPassAg.getText());

                if(newAdminName.length()<2){
                    Toast.makeText(getApplicationContext(),"Geçerli bir isim girin",Toast.LENGTH_SHORT).show();
                }else{
                    DatabaseConnection dc = new DatabaseConnection(getApplicationContext());
                    dc.open();
                    if(!dc.checkUsername(newAdminName)){
                        Toast.makeText(getApplicationContext(),"Kullanıcı adı mevcut!Farklı bir ad girin.",Toast.LENGTH_SHORT).show();
                    }else if(newPassword.length()<4){
                        Toast.makeText(getApplicationContext(),"En az 4 karakter!",Toast.LENGTH_SHORT).show();
                    }else if(!newPassword.equals(newPasswordAg)){
                        Toast.makeText(getApplicationContext(),"Şifreyi kontrol edin!",Toast.LENGTH_SHORT).show();
                    }else{
                        dc.addUser(newAdminName,newPassword);
                        dc.close();
                        Toast.makeText(getApplicationContext(),"Yeni admin eklendi",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        });


        exit.setOnClickListener(new View.OnClickListener() {
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
