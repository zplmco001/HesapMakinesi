package iride.app.com.iride;

public class SatisInfo {

    public String kayitTarihi,müsteriİsim,baslangıcSüre,bitisSüre;
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
}
