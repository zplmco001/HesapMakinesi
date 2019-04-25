package iride.app.com.iride;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class ListAdapter extends ArrayAdapter<SatisInfo> {

    private LayoutInflater layoutInflater;
    private List<SatisInfo> satislist;
    private int id;

    public ListAdapter(Context context,int twResourceId,List<SatisInfo> satislist){
        super(context,twResourceId,satislist);
        this.satislist=satislist;
        layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        id=twResourceId;
    }


    public View getView(int position, View convertView, ViewGroup parent){
        convertView=layoutInflater.inflate(id,null);
        convertView.setMinimumHeight(70);

        SatisInfo info = satislist.get(position);

        if(info!=null){
            TextView tw[] = new TextView[8];
            tw[0]=(TextView) convertView.findViewById(R.id.tw1);
            tw[1]=(TextView) convertView.findViewById(R.id.tw2);
            tw[2]=(TextView) convertView.findViewById(R.id.tw3);
            tw[3]=(TextView) convertView.findViewById(R.id.tw4);
            tw[4]=(TextView) convertView.findViewById(R.id.tw5);
            tw[5]=(TextView) convertView.findViewById(R.id.tw6);
            tw[6]=(TextView) convertView.findViewById(R.id.tw7);
            tw[7]=(TextView) convertView.findViewById(R.id.tw8);

            tw[0].setText(String.valueOf(info.fisNo));
            tw[1].setText(info.kayitTarihi);
            tw[2].setText(info.müsteriİsim);
            tw[3].setText(String.valueOf(info.adet));

            switch (info.tarife){
                case 0:
                    tw[4].setText("15 DK");
                    break;

                case 1:
                    tw[4].setText("30 DK");
                    break;

                case 2:
                    tw[4].setText("45 DK");
                    break;

                case 3:
                    tw[4].setText("60 DK");
                    break;

                case 4:
                    tw[4].setText("AÇIK HESAP");
            }

            tw[5].setText(info.baslangıcSüre);
            tw[6].setText(info.bitisSüre);
            tw[7].setText(String.valueOf(info.totalÜcret));

            for (TextView tab : tw){
                tab.setTextSize(36);
            }


        }
        return convertView;
    }
}
