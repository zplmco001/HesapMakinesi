package iride.app.com.iride;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class AdminNameChange extends AppCompatActivity {

    private EditText currname,newname;
    private Button acc;
    private String cName,nName;
    private ImageView exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_name_change);

        currname = (EditText) findViewById(R.id.currname);
        newname = (EditText) findViewById(R.id.newname);

        acc = (Button) findViewById(R.id.acc);

        exit = (ImageView) findViewById(R.id.namechexit);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cName = String.valueOf(currname.getText());
                nName = String.valueOf(newname.getText());


                if(nName.length()<2){
                    Toast.makeText(getApplicationContext(),"Geçerli bir kullanıcı adı giriniz!",Toast.LENGTH_SHORT).show();
                }else{
                    DatabaseConnection dc = new DatabaseConnection(getApplicationContext());
                    dc.open();
                    if(!dc.checkUsername(nName)){
                        Toast.makeText(getApplicationContext(),"Böyle bir kullanıcı var!",Toast.LENGTH_SHORT).show();
                    }else{
                        dc.changeUserName(nName,cName);
                        Toast.makeText(getApplicationContext(),"Kullanıcı adı değişti!",Toast.LENGTH_SHORT).show();
                        dc.close();
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
