package iride.app.com.iride;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class Database extends SQLiteOpenHelper {

    Database(Context context) {
        super(context,"database",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String adminTable = "create table admin (admin_name text primary key,password text)";
        sqLiteDatabase.execSQL(adminTable);

        sqLiteDatabase.execSQL("insert into admin (admin_name,password) values ('Admin','admin')");


        String kayıtTable = "create table kayit (fis_no integer,kayit_tarih text,müsteri_isim text" +
                "adet integer,tarife integer,baslangic_sure text,bitis_sure text,ucret integer)";

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exist admintTable");
        sqLiteDatabase.execSQL("drop table if exist kayit");
    }
}
