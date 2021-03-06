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

    /**List<SatisInfo> tarihAralikGetir(String bas,String son){

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
    }**/




    /**List<SatisInfo> tarihGetir(String tarih){
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
    }**/


    List<SatisInfo> tumKayıtlar(){

        String columns [] = {"id","fis_no","kayit_tarih","musteri_isim","adet","tarife","baslangic_sure","bitis_sure","toplam_ucret"};
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
            int id = c.getInt(c.getColumnIndex("id"));

            list.add(new SatisInfo(id,fis,tarih,isim,adet,tarife,bassüre,bitsüre,total));
            c.moveToNext();
        }
        c.close();
        return list;
    }
/**
    int toplamKazanc(String bas,String son){

        String query= "select sum(toplam_ucret) from satis_info where kayit_tarih between '"+bas+"' and '"+son+"'"  ;
        Cursor c = sqLiteDatabase.rawQuery(query,null);
        if(c.moveToFirst()){
            return c.getInt(0);
        }else{
            return 0;
        }
    }

    int toplamKazanc(String tarih){

        String query= "select sum(toplam_ucret) from satis_info where kayit_tarih='"+tarih+"'";
        Cursor c = sqLiteDatabase.rawQuery(query,null);
        if(c.moveToFirst()){
            return c.getInt(0);
        }else{
            return 0;
        }
    }**/

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



    void gunlukTemizle(){
        String query = "delete from gunluk_info";
        sqLiteDatabase.execSQL(query);
    }

    /** GENEL TABLO TEMİZLEME**/

    void genelTemizle(){
        String query = "delete from satis_info";
        sqLiteDatabase.execSQL(query);
    }

    /** GENEL TABLO TEMİZLEME**/

    /** GÜNLÜK TABLO GENELE KOPYALAMA**/

    void gunlukToGenel(){
        String query = "insert into satis_info select * from gunluk_info";
        sqLiteDatabase.execSQL(query);
    }

    /** GÜNLÜK TABLO GENELE KOPYALAMA**/


    void satisEkle(int id,int fisNo,String kayitTarihi,String müsteriİsim,int adet,int tarife,
                   String baslangicSure,String bitisSure,int toplamUcret){

        String query="insert into gunluk_info (id,fis_no,kayit_tarih,musteri_isim,adet,tarife,baslangic_sure,bitis_sure,toplam_ucret)" +
                "values ('"+id+"','"+fisNo+"','"+kayitTarihi+"','"+müsteriİsim+"','"+adet+"','"+tarife+"','"+baslangicSure+"','"+bitisSure+"','"+toplamUcret+"')";

        sqLiteDatabase.execSQL(query);

    }


    void kayitGuncelle(int id,String isim,int fisNo,int adet,int tarife,String baslangicSure,String bitisSure,int ucret){

        /**id,fis_no,kayit_tarih,musteri_isim,adet,tarife,baslagic_sure,bitis_sure,toplam_ucret*/
        String query = "update gunluk_info set musteri_isim='"+isim+"',adet='"+adet+"',baslangic_sure='"+baslangicSure+"',tarife='"+tarife+"',bitis_sure='"+bitisSure+"',toplam_ucret='"+ucret+"' where fis_no = '"+fisNo+"' " +
                " and musteri_isim='"+isim+"' and id='"+id+"'";
        sqLiteDatabase.execSQL(query);

    }

    void kayitSil(int fisNo,String tarih){
        String query = "delete from gunluk_info where fis_no='"+fisNo+"' and kayit_tarih='"+tarih+"'";
        sqLiteDatabase.execSQL(query);
    }

    int getLastID(){
        //String columns [] = {"id","fis_no","kayit_tarih","musteri_isim","adet","tarife","baslangic_sure","bitis_sure","toplam_ucret"};
        try{
            Cursor c = sqLiteDatabase.rawQuery("SELECT MAX(id) AS MAX FROM gunluk_info", null);
            c.moveToFirst();
            int id = c.getInt(c.getColumnIndex("MAX"));

            return id;
        }catch (Exception e){
            return 0;
        }
    }


    List<SatisInfo> gunlukKayıtlar(){

        String columns [] = {"id","fis_no","kayit_tarih","musteri_isim","adet","tarife","baslangic_sure","bitis_sure","toplam_ucret"};
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
            int id = c.getInt(c.getColumnIndex("id"));

            list.add(new SatisInfo(id,fis,tarih,isim,adet,tarife,bassüre,bitsüre,total));
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



    SatisInfo fisNoSorgu(int fisNo,String basSure){
        String query = "select * from gunluk_info where fis_no='"+fisNo+"' and baslangic_sure='"+basSure+"'";

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
            int id = c.getInt(c.getColumnIndex("id"));

            c.close();

            return new SatisInfo(id,fis,tarih,isim,adet,tarife,bassüre,bitsüre,total);
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