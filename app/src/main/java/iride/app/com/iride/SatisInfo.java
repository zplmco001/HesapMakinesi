package iride.app.com.iride;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class SatisInfo implements Comparable<SatisInfo>,Serializable{

    String kayitTarihi,müsteriİsim,baslangıcSüre,bitisSüre;
    public int fisNo,adet,totalÜcret,tarife;

    SatisInfo(int fisNo, String kayitTarihi, String müsteriİsim, int adet, int tarife, String baslangıcSüre, String bitisSüre, int totalÜcret){
        this.fisNo=fisNo;
        this.kayitTarihi=kayitTarihi;
        this.müsteriİsim=müsteriİsim;
        this.adet=adet;
        this.tarife=tarife;
        this.baslangıcSüre=baslangıcSüre;
        this.bitisSüre=bitisSüre;
        this.totalÜcret=totalÜcret;

    }

    public int getFisNo() {
        return fisNo;
    }

    public void setFisNo(int fisNo) {
        this.fisNo = fisNo;
    }

    @Override
    public int compareTo(@NonNull SatisInfo satisInfo) {
        return this.fisNo - satisInfo.fisNo;
    }
}
