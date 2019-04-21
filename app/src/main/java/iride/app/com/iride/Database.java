package iride.app.com.iride;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class Database extends SQLiteOpenHelper {

    Database(Context context) {
        super(context,"database",null,1);
    }

    Database(){
        super(null,"database",null,1);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String adminTable = "create table admin (admin_name text primary key,password text)";
        sqLiteDatabase.execSQL(adminTable);

        sqLiteDatabase.execSQL("insert into admin (admin_name,password) values ('Admin','admin')");


        String satisTable = "create table satis_info (fis_no integer,kayit_tarih text,musteri_isim text," +
                "adet integer,tarife integer,baslangic_sure text,bitis_sure text,toplam_ucret integer)";

        sqLiteDatabase.execSQL(satisTable);


        String gunlukTable = "create table gunluk_info (fis_no integer,kayit_tarih text,musteri_isim text," +
                "adet integer,tarife integer,baslangic_sure text,bitis_sure text,toplam_ucret integer)";

        sqLiteDatabase.execSQL(gunlukTable);


        String fiyatTable = "create table fiyat_table (tarife15 integer,tarife30 integer,tarife45 integer,tarife60 integer)";
        sqLiteDatabase.execSQL(fiyatTable);

        sqLiteDatabase.execSQL("insert into fiyat_table (tarife15,tarife30,tarife45,tarife60) values ('15','25','35','45')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exist admin");
        sqLiteDatabase.execSQL("drop table if exist satis_info");
        sqLiteDatabase.execSQL("drop table if exist gunluk_info");
        sqLiteDatabase.execSQL("drop table if exist fiyat_table");
    }
}
