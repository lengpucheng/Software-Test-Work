package cn.hll520.lpc.SoftwareTest.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import cn.hll520.lpc.SoftwareTest.provider.InfoListProvider;

public class InfoCard {
    private String id;
    private int count;
    private int news = 0;
    private String var;
    private String time;

    public InfoCard() {
    }

    public InfoCard(Info info) {
        this.id=info.getGetID();
        this.count=1;
        setVar(info.getVar());
        setTime(info.getTime());
    }
    //根据ID获取InfoCard
    public static InfoCard getInfoCardById(Context context, String id) {
        InfoCard infoCard=null;
        Cursor cursor;
        cursor = context.getContentResolver().query(InfoListProvider.URI_CCONTACT, null,
                "id=?", new String[]{id}, null);
        while (cursor.moveToNext()) {
            infoCard = getInfoCardByCu(cursor);
        }
        return infoCard;
    }
    //读取全部的数据
    public static List<InfoCard> getInfoCardList(Context context) {
        List<InfoCard> data = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(InfoListProvider.URI_CCONTACT, null,
                null, null, null);
        while (cursor.moveToNext()) {
            InfoCard infoCard = InfoCard.getInfoCardByCu(cursor);
            data.add(infoCard);
        }
        return data;
    }
    //更新数据库中的该对象
    public boolean upData(Context context){
        int count=0;
        count=context.getContentResolver().update(InfoListProvider.URI_CCONTACT,this.getCv(),
                "id=?",new String[]{this.getId()});
        if(count==0)
            return false;
        return true;
    }

    public static InfoCard getInfoCardByCu(Cursor cursor) {
        InfoCard infoCard = new InfoCard();
        infoCard.setId(cursor.getString(1));
        infoCard.setCount(cursor.getInt(2));
        infoCard.setNews(cursor.getInt(3));
        infoCard.setVar(cursor.getString(4));
        infoCard.setTime(cursor.getString(5));
        return infoCard;
    }

    public ContentValues getCv() {
        ContentValues cv = new ContentValues();
        cv.put("ID", this.getId());
        cv.put("count", this.getCount());
        cv.put("news", this.getNews());
        cv.put("var", this.getVar());
        cv.put("time", this.getTime());
        return cv;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getNews() {
        return news;
    }

    public void setNews(int news) {
        if(news>99)
            news=99;
        this.news = news;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        if(var.length()>16)
            var=var.substring(0,15)+"...";
        this.var = var;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        if(time.length()>9)
            time=time.substring(time.length()-8,time.length()-3);
        this.time = time;
    }

    @Override
    public String toString() {
        return "InfoCard{" +
                "id='" + id + '\'' +
                ", count=" + count +
                ", news=" + news +
                ", var='" + var + '\'' +
                ", time='" + time + '\'' +
                '}';
    }


}
