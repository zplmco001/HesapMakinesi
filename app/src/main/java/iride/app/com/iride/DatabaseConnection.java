package iride.app.com.iride;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class DatabaseConnection {

    private SQLiteDatabase sqLiteDatabase;
    private Database database;

    DatabaseConnection(Context context){
        database = new Database(context);
    }


    void open(){
        sqLiteDatabase = database.getWritableDatabase();
    }

    void read(){
        sqLiteDatabase = database.getReadableDatabase();
    }

    void close(){
        database.close();
    }





    void satisEkle(int fisNo,String kayitTarihi,String müsteriİsim,int adet,int tarife,String baslangicSure,String bitisSure,int toplamUcret){

        String query="insert into satis_info (fis_no,kayit_tarih,musteri_isim,adet,tarife,baslangic_sure,bitis_sure,toplam_ucret)" +
                "values ('"+fisNo+"','"+kayitTarihi+"','"+müsteriİsim+"','"+adet+"','"+tarife+"','"+baslangicSure+"','"+bitisSure+"','"+toplamUcret+"')";

        String query2="insert into gunluk_info (fis_no,kayit_tarih,musteri_isim,adet,tarife,baslangic_sure,bitis_sure,toplam_ucret)" +
                "values ('"+fisNo+"','"+kayitTarihi+"','"+müsteriİsim+"','"+adet+"','"+tarife+"','"+baslangicSure+"','"+bitisSure+"','"+toplamUcret+"')";



        sqLiteDatabase.execSQL(query);
        sqLiteDatabase.execSQL(query2);

    }


    void kayitGuncelle(int fisNo,int adet,int tarife,String bitisSure,int ucret){

        /**fis_no,kayit_tarih,musteri_isim,adet,tarife,baslagic_sure,bitis_sure,toplam_ucret*/
        String query = "update gunluk_info set adet='"+adet+"',tarife='"+tarife+"',bitis_sure='"+bitisSure+"',toplam_ucret='"+ucret+"'";
        sqLiteDatabase.execSQL(query);

    }

    void kayitSil(int fisNo){
        String query = "delete from gunluk_info where fis_no='"+fisNo+"'";
        String query2 = "delete from satis_info where fis_no='"+fisNo+"'";
        sqLiteDatabase.execSQL(query);
        sqLiteDatabase.execSQL(query2);

    }


    List<SatisInfo> tumKayıtlar(){

        String columns [] = {"fis_no","kayit_tarih","musteri_isim","adet","tarife","baslangic_sure","bitis_sure","toplam_ucret"};
        Cursor c = sqLiteDatabase.query("satis_info",columns,null,null,null,null,null);
        Log.e("counter",""+c.getCount());

        List<SatisInfo> list = new ArrayList<>();
        c.moveToFirst();
        for(int i=0 ; i<c.getCount(); i++){
            int fis = c.getInt(c.getColumnIndex("fis_no"));
            String tarih = c.getString(c.getColumnIndex("kayit_tarih"));
            String isim = c.getString(c.getColumnIndex("musteri_isim"));
            int adet = c.getInt(c.getColumnIndex("adet"));
            int tarife = c.getInt(c.getColumnIndex("tarife"));
            String bassüre = c.getString(c.getColumnIndex("baslangic_sure"));
            String bitsüre = c.getString(c.getColumnIndex("bitis_sure"));
            int total = c.getInt(c.getColumnIndex("toplam_ucret"));

            list.add(new SatisInfo(fis,tarih,isim,adet,tarife,bassüre,bitsüre,total));
            c.moveToNext();
        }
        c.close();
        return list;

    }



    SatisInfo fisNoSorgu(int fisNo){
            String query = "select * from gunluk_info where fis_no='"+fisNo+"'";

        Cursor c = sqLiteDatabase.rawQuery(query,null);
        if(c.getCount()==0){
            c.close();
            return null;
        }else{

            c.moveToFirst();
            int fis = c.getInt(c.getColumnIndex("fis_no"));
            String tarih = c.getString(c.getColumnIndex("kayit_tarih"));
            String isim = c.getString(c.getColumnIndex("müsteri_isim"));
            int adet = c.getInt(c.getColumnIndex("adet"));
            int tarife = c.getInt(c.getColumnIndex("tarife"));
            String bassüre = c.getString(c.getColumnIndex("baslangic_sure"));
            String bitsüre = c.getString(c.getColumnIndex("bitis_sure"));
            int total = c.getInt(c.getColumnIndex("toplam_ucret"));

            c.close();

            return new SatisInfo(fis,tarih,isim,adet,tarife,bassüre,bitsüre,total);
        }
    }


    /** Kullanıcı adı ve şifre kontrolü fonskisyonları**/

    boolean checkUser(String username,String password){

        String query = "select * from admin where admin_name='"+username+"' and password='"+password+"'";
        Cursor c = sqLiteDatabase.rawQuery(query,null);


        if(c.getCount()==0){
            c.close();
            return false;
        }else{
            c.close();
            return true;
        }
        //sqLiteDatabase.execSQL("select * from admin where admin_name='"+username+"' and password='"+password+"'");
    }

    boolean checkUsername(String username){
        String quary = "select * from admin where admin_name='"+username+"'";
        Cursor c = sqLiteDatabase.rawQuery(quary,null);
        if(c.getCount()==0){
            c.close();
            return true;
        }else{
            c.close();
            return false;
        }
    }

    boolean checkCurrentPassword(String password){
        String query = "select * from admin where password='"+password+"'";
        Cursor c = sqLiteDatabase.rawQuery(query,null);

        if(c.getCount()==0){
            c.close();
            return false;
        }else{
            c.close();
            return true;
        }
    }

    void addUser(String username,String password){
        String query = "insert into admin (admin_name,password) values ('"+username+"','"+password+"')";
        sqLiteDatabase.execSQL(query);
    }


    void changePassword(String username,String password){
        String query="update admin set password='"+password+"' where admin_name='"+username+"'";
        sqLiteDatabase.execSQL(query);
    }

    void changeUserName(String username,String oldname){
        String query="update admin set admin_name='"+username+"' where admin_name='"+oldname+"'";
        sqLiteDatabase.execSQL(query);
    }



}
