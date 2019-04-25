package iride.app.com.iride;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by mehmetcanolgun on 22.04.2019.
 */

public class FileWrite {

    private List<SatisInfo> list;

    FileWrite(List<SatisInfo> list){
        this.list = list;
    }

    public void write(){
        Log.e("afs","fa");

        String name = list.get(0).kayitTarihi+".csv";
        name = name.replace('/','.');

        String trftxt="";



        try {
            File file = new File("/storage/emulated/0/iRide Kayıtlar",name);
            Log.e("filenem",file.getName());
            BufferedWriter writer = new BufferedWriter(new FileWriter(file,true));
            writer.write("Fis No;Tarih;Isim;Adet;Tarife;Cikis;Teslim;Ucret");
            Log.e("dir2",Environment.getExternalStorageDirectory().getPath());
            writer.write("\n");
            for(SatisInfo info:list){

                switch (info.tarife){
                    case 0:
                        trftxt="15 DK";
                        break;

                    case 1:
                        trftxt="30 DK";
                        break;

                    case 2:
                        trftxt="45 DK";
                        break;

                    case 3:
                        trftxt="60 DK";
                        break;

                    case 4:
                        trftxt="AÇIK HESAP";
                }

                writer.write(info.fisNo+";"+info.kayitTarihi+";"+info.müsteriİsim+";"+info.adet+";"+
                trftxt+";"+info.baslangıcSüre+";"+info.bitisSüre+";"+info.totalÜcret);
                Log.e("cvs",info.fisNo+";"+info.kayitTarihi+";"+info.müsteriİsim+";"+info.adet+";"+
                        info.tarife+";"+info.baslangıcSüre+";"+info.bitisSüre+";"+info.totalÜcret);
                writer.write("\n");
                Log.e("içeride","yazıldı");

            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("catch","");
        }
    }


    public boolean isOldDay(){
        if (list.size()>0){
            String kayitTarihi = list.get(0).kayitTarihi;
            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
            int day = calendar.get(Calendar.DATE);
            int month = calendar.get(Calendar.MONTH) + 1;
            int year = calendar.get(Calendar.YEAR);

            int modus =2000;

            if(year%modus!=0){
                year=year%modus;
            }else{
                modus+=1000;
                year=year%modus;
            }

            String today;
            if(day<10){

                if(month<10){
                    today = "0"+day+"/0"+month+"/"+year;
                }else{
                    today = "0"+day+"/"+month+"/"+year;
                }

            }else if(month<10){
                today = day+"/0"+month+"/"+year;
            }else{
                today = day+"/"+month+"/"+year;
            }
            Log.e("today",today);
            Log.e("kayit",kayitTarihi);
            if (today.equals(kayitTarihi)){
                return false;
            }else {
                return true;
            }
        }
        else {
            return false;
        }
    }

}
