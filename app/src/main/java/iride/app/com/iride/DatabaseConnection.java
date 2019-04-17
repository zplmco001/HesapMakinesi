package iride.app.com.iride;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.Map;

import iride.app.com.iride.Database;


public class DatabaseConnection {

    private SQLiteDatabase sqLiteDatabase;
    private Database database;

    DatabaseConnection(Context context){
        database = new Database(context);
    }


    void open(){
        sqLiteDatabase = database.getWritableDatabase();
    }

    void close(){
        database.close();
    }


    boolean checkUser(String username,String password){

        String query = "select * from admin where admin_name='"+username+"' and password='"+password+"'";
        Cursor c = sqLiteDatabase.rawQuery(query,null);


        if(c.getCount()==0){
            return false;
        }else{
            return true;
        }
        //sqLiteDatabase.execSQL("select * from admin where admin_name='"+username+"' and password='"+password+"'");
    }


    boolean checkUsername(String username){
        String quary = "select * from admin where admin_name='"+username+"'";
        Cursor c = sqLiteDatabase.rawQuery(quary,null);
        if(c.getCount()==0){
            return true;
        }else{
            return false;
        }
    }

    boolean checkCurrentPassword(String password){
        String query = "select * from admin where password='"+password+"'";
        Cursor c = sqLiteDatabase.rawQuery(query,null);

        if(c.getCount()==0){
            return false;
        }else{
            return true;
        }
    }

    void addUser(String username,String password){
        String query = "insert into admin (admin_name,password) values ('"+username+"','"+password+"')";
        sqLiteDatabase.execSQL(query);
    }

    void changePassword(String password){
        String query="update admin set password='"+password+"'";
        sqLiteDatabase.execSQL(query);
    }

    void kayitEkle(int fis_no,String kayit_tarih,String musteri_isim,
                   int adet,int tarife ,String baslangic_sure,String bitis_sure,int toplam_ucret){

        String query = "insert into satis_info (fis_no, kayit_tarih, müsteri_isim," +
        "adet, tarife, baslangic_sure, bitis_sure, toplam_ucret) values ('"+fis_no+"','"+kayit_tarih+
                "','"+musteri_isim+"','"+adet+"','"+tarife+"','"+baslangic_sure+"','"+bitis_sure+"','"+toplam_ucret+"')";
        sqLiteDatabase.execSQL(query);

    }

}
