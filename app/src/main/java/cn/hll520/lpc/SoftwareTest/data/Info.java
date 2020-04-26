package cn.hll520.lpc.SoftwareTest.data;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.hll520.lpc.SoftwareTest.provider.InfoListProvider;
import cn.hll520.lpc.SoftwareTest.provider.InfoProvider;

public class Info {
    private String sendID;
    private String getID;
    private int infoClass=0;
    private String var;
    private String time;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Info(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        time=dateFormat.format(new Date());
    }
    //返回单个
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Info getInfo(Cursor cursor){
        Info info=new Info();
        info.setSendID(cursor.getString(1));
        info.setGetID(cursor.getString(2));
        info.setInfoClass(cursor.getInt(3));
        info.setVar(cursor.getString(4));
        info.setTime(cursor.getString(5));
        return info;
    }
    //返回消息表
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<Info> getInfos(Cursor cursor){
        List<Info> infos=new ArrayList<>();
        while (cursor.moveToNext()){
            Info info=Info.getInfo(cursor);
            infos.add(info);
        }
        return infos;
    }
    //根据发送者返回消息表
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<Info> getInfoListBySend(Context context, String sendID){
        List<Info> data=new ArrayList<>();
        Cursor cursor=context.getContentResolver().query(InfoProvider.URI_CCONTACT,null,
                "sendID=?",new String[]{sendID},null);
        data=Info.getInfos(cursor);
        return data;
    }
    //根据聊天对象返回消息
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<Info> getInfosByID(Context context, String ID){
        Cursor cursor=Info.getCursorsByID(context,ID);
        List<Info> data=Info.getInfos(cursor);
        return data;
    }
/*    SELECT * FROM info WHERE sendID='110' AND getID='110' or sendID='110' AND getID='110'
       发送者和接收者   或  接收者和发送者
*/
//   根据对象返回结果集合
    public static Cursor getCursorsByID(Context context,String ID){
        Cursor cursor=null;
        User user=User.getLoginUser(context);
        String MyID=user.getID();
        if(MyID==null)
            MyID="null";
        cursor = context.getContentResolver().query(InfoProvider.URI_CCONTACT, null,
                    "sendID=? AND getID=? or sendID=? AND getID=?", new String[]{ID, MyID, MyID, ID}, null);

        return cursor;
    }


//    转换未cv
    public ContentValues getCv(){
        ContentValues cv=new ContentValues();
        cv.put("sendID",sendID);
        cv.put("getID",getID);
        cv.put("infoClass",infoClass);
        cv.put("var",var);
        cv.put("time",time);
        return cv;
    }

    public boolean sendInfo(Context context){
        context.getContentResolver().insert(InfoProvider.URI_CCONTACT,this.getCv());
        InfoCard card=InfoCard.getInfoCardById(context,this.getGetID());
        if(card!=null){
        card.setVar(var);
        card.setTime(time);
        card.setCount(card.getCount()+1);
        card.upData(context);
        }else {
        card=new InfoCard(this);
        context.getContentResolver().insert(InfoListProvider.URI_CCONTACT,card.getCv());
        }
        return true;
    }

    public boolean getInfoUpCard(Context context){
        InfoCard card=InfoCard.getInfoCardById(context,this.getSendID());
        if(card!=null){
            card.setVar(var);
            card.setTime(time);
            card.setNews(card.getNews()+1);
            card.setCount(card.getCount()+1);
            card.upData(context);
        }else {
            card=new InfoCard();
            card.setId(getSendID());
            card.setVar(var);
            card.setTime(time);
            card.setNews(1);
            card.setCount(1);
            context.getContentResolver().insert(InfoListProvider.URI_CCONTACT,card.getCv());
        }
        return true;
    }

    public String getSendID() {
        return sendID;
    }

    public void setSendID(String sendID) {
        this.sendID = sendID;
    }

    public String getGetID() {
        return getID;
    }

    public void setGetID(String getID) {
        this.getID = getID;
    }

    public int getInfoClass() {
        return infoClass;
    }

    public void setInfoClass(int infoClass) {
        this.infoClass = infoClass;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    @Override
    public String toString() {
        return "Info{" +
                "sendID='" + sendID + '\'' +
                ", getID='" + getID + '\'' +
                ", infoClass=" + infoClass +
                ", var='" + var + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
