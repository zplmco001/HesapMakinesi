package iride.app.com.iride;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class DatabaseConnection {

    private SQLiteDatabase sqLiteDatabase,sqldb;
    private Database database,db;

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



    /** Ücret ve tarifeyi güncelleyecek fonksiyonlar **/

    void tarifeUpdate(int tarife15,int tarife30,int tarife45,int tarife60){
        String query="update fiyat_table set tarife15='"+tarife15+"',tarife30='"+tarife30+"',tarife45='"+tarife45+"',tarife60='"+tarife60+"'";
        sqLiteDatabase.execSQL(query);
    }

    int[] getTarife(){
        String query="select * from fiyat_table";
        Cursor c = sqLiteDatabase.rawQuery(query,null);
        int array [] = new int[4];

        c.moveToFirst();
        array[0]= c.getInt(c.getColumnIndex("tarife15"));
        array[1]= c.getInt(c.getColumnIndex("tarife30"));
        array[2]= c.getInt(c.getColumnIndex("tarife45"));
        array[3]= c.getInt(c.getColumnIndex("tarife60"));
        c.close();

        return array;
    }





    /**Tğm kayıtlarda kullanılan fonksiyonlar**/

    List<SatisInfo> tarihAralikGetir(String bas,String son){

        String query = "select * from satis_info where kayit_tarih between '"+bas+"' and '"+son+"'";
        Cursor c = sqLiteDatabase.rawQuery(query,null);
        List<SatisInfo> list = new ArrayList<>();


        if(c.getCount()==0){
            c.close();
            return list;
        }else{
            c.moveToFirst();
            for(int i=0 ; i<c.getCount(); i++){
                int fis = c.getInt(c.getColumnIndex("fis_no"));
                String trh = c.getString(c.getColumnIndex("kayit_tarih"));
                String isim = c.getString(c.getColumnIndex("musteri_isim"));
                int adet = c.getInt(c.getColumnIndex("adet"));
                int tarife = c.getInt(c.getColumnIndex("tarife"));
                String bassüre = c.getString(c.getColumnIndex("baslangic_sure"));
                String bitsüre = c.getString(c.getColumnIndex("bitis_sure"));
                int total = c.getInt(c.getColumnIndex("toplam_ucret"));

                list.add(new SatisInfo(fis,trh,isim,adet,tarife,bassüre,bitsüre,total));
                c.moveToNext();
            }
            c.close();
            return list;
        }
    }




    List<SatisInfo> tarihGetir(String tarih){
        String query = "select * from satis_info where kayit_tarih='"+tarih+"'";
        Cursor c = sqLiteDatabase.rawQuery(query,null);
        List<SatisInfo> list = new ArrayList<>();

        if(c.getCount()==0){
            c.close();
            return list;
        }else{

            c.moveToFirst();
            for(int i=0 ; i<c.getCount(); i++){
                int fis = c.getInt(c.getColumnIndex("fis_no"));
                String trh = c.getString(c.getColumnIndex("kayit_tarih"));
                String isim = c.getString(c.getColumnIndex("musteri_isim"));
                int adet = c.getInt(c.getColumnIndex("adet"));
                int tarife = c.getInt(c.getColumnIndex("tarife"));
                String bassüre = c.getString(c.getColumnIndex("baslangic_sure"));
                String bitsüre = c.getString(c.getColumnIndex("bitis_sure"));
                int total = c.getInt(c.getColumnIndex("toplam_ucret"));

                list.add(new SatisInfo(fis,trh,isim,adet,tarife,bassüre,bitsüre,total));
                c.moveToNext();
            }
            c.close();
            return list;
        }
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


    int toplamKazanc(){

        String query= "select sum(toplam_ucret) from satis_info";
        Cursor c = sqLiteDatabase.rawQuery(query,null);
        if(c.moveToFirst()){
            return c.getInt(0);
        }else{
            return 0;
        }
    }

    int gunlukKazanc(){

        String query= "select sum(toplam_ucret) from gunluk_info";
        Cursor c = sqLiteDatabase.rawQuery(query,null);
        if(c.moveToFirst()){
            return c.getInt(0);
        }else{
            return 0;
        }
    }

    /**Günlük kayıtlarda kullanılan işlemler**/


    /**Genel tabloya kaydeder günlüğü siler**/
    /*void gunlukKaydet(Context context){

        DatabaseConnection dc = new DatabaseConnection(context);

        dc.open();
        List<SatisInfo> list = dc.gunlukKayıtlar();
        dc.close();


        if(list.size()!=0){
            for(int i=0 ; i<list.size(); i++){
                String query="insert into satis_info (fis_no,kayit_tarih,musteri_isim,adet,tarife,baslangic_sure,bitis_sure,toplam_ucret)" +
                        "values ('"+list.get(i).fisNo+"','"+list.get(i).kayitTarihi+"','"+list.get(i).müsteriİsim+"','"+list.get(i).adet+"','"+list.get(i).tarife+"','"+list.get(i).baslangıcSüre+"','"+list.get(i).bitisSüre+"','"+list.get(i).totalÜcret+"')";
                sqLiteDatabase.execSQL(query);
            }


        }else{
            Log.e("","boş");
        }
    }*/

    void gunlukTemizle(){
        String query = "delete from gunluk_info";
        sqLiteDatabase.execSQL(query);
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
        String query = "update gunluk_info set adet='"+adet+"',tarife='"+tarife+"',bitis_sure='"+bitisSure+"',toplam_ucret='"+ucret+"' where fis_no = '"+fisNo+"'";
        String query2 = "update satis_info set adet='"+adet+"',tarife='"+tarife+"',bitis_sure='"+bitisSure+"',toplam_ucret='"+ucret+"' where fis_no = '"+fisNo+"'";
        sqLiteDatabase.execSQL(query);
        sqLiteDatabase.execSQL(query2);

    }

    void kayitSil(int fisNo,String tarih){
        String query = "delete from gunluk_info where fis_no='"+fisNo+"' and kayit_tarih='"+tarih+"'";
        sqLiteDatabase.execSQL(query);
    }


    List<SatisInfo> gunlukKayıtlar(){

        String columns [] = {"fis_no","kayit_tarih","musteri_isim","adet","tarife","baslangic_sure","bitis_sure","toplam_ucret"};
        Cursor c = sqLiteDatabase.query("gunluk_info",columns,null,null,null,null,null);
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

    List<Integer> fisNos(String tablo){
        String columns[] = {"fis_no"};
        Cursor c = sqLiteDatabase.query(tablo,columns,null,null,null,null,null);
        List<Integer> list = new ArrayList<>();
        c.moveToFirst();
        for (int i=0;i<c.getCount();i++){
            list.add(c.getInt(c.getColumnIndex("fis_no")));
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
            String isim = c.getString(c.getColumnIndex("musteri_isim"));
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



    void changePassword(String username,String password){
        String query="update admin set password='"+password+"' where admin_name='"+username+"'";
        sqLiteDatabase.execSQL(query);
    }

    void changeUserName(String username,String oldname){
        String query="update admin set admin_name='"+username+"' where admin_name='"+oldname+"'";
        sqLiteDatabase.execSQL(query);
    }
}