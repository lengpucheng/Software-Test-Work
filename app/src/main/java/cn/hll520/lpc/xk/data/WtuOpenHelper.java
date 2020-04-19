package cn.hll520.lpc.xk.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import androidx.annotation.Nullable;

//本地数据库构造
public class WtuOpenHelper extends SQLiteOpenHelper {
    //联系人列表
    public static final String T_STUDENT="student";
    public static final String DB_NAME="wtuxk";
    public class StudentTable implements BaseColumns{
        public static final String ID="id";
        public static final String NAME="name";
        public static final String GENDER="gender";
        public static final String PHONE="phone";
        public static final String BIRTHDAY="birthday";
        public static final String QQ="QQ";
        public static final String MENT="ment";
        public static final String POSITION="position";
        public static final String CAMPUS="campus";
        public static final String COLLEGE="college";
        public static final String BANJI="banji";
        public static final String HOUSE="house";
        public static final String ROOM="room";
        public static final String IMG="img";
    }
    //登录状态表
    public static final String T_USER="user";
    public class UserTable implements BaseColumns{
        //login,0未登录，1登录
        public static final String LOGIN="login";
        public static final String ID="ID";
        public static final String USERNAME="username";
        public static final String SAVAPASSWORD="savaPassword";
        public static final String SAFETY="safety";
        public static final String BM="bm";
        public static final String PASSINFO="passinfo";
        public static final String REGTIME="RegTime";
        public static final String GID="GID";
    }
    //消息列表
    public static final String T_INFOLIST="infolist";
    public class InfoListTable implements BaseColumns{
        public static final String ID="ID";
        public static final String COUNT="count";
        public static final String NEWS="news";
        public static final String VAR="var";
        public static final String TIME="time";
    }

    //聊天记录
    public static final String T_INFO="info";
    public class InfoTable implements BaseColumns{
        public static final String SENDID="sendID";
        public static final String GETID="getID";
        //消息内容，0文本，1图片
        public static final String INFOCLASS="infoClass";
        public static final String VAR="var";
        public static final String TIME="time";
    }
    public WtuOpenHelper(@Nullable Context context) {
        super(context, "wtuxk.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //联系人表
        String sql="CREATE TABLE "+T_STUDENT+" ("
                + StudentTable._ID+" integer  PRIMARY KEY AUTOINCREMENT, "
                + StudentTable.ID+" text(10) NOT NULL,"
                + StudentTable.NAME+"  text(14) NOT NULL,"
                + StudentTable.GENDER+"  text(4) NOT NULL,"
                + StudentTable.PHONE+"  text(15),"
                + StudentTable.BIRTHDAY+"  text,"
                + StudentTable.QQ+"  text(40),"
                + StudentTable.MENT+"  text(20),"
                + StudentTable.POSITION+"  text(20),"
                + StudentTable.CAMPUS+"  text(30),"
                + StudentTable.COLLEGE+"  text(50),"
                + StudentTable.BANJI+"  text(50),"
                + StudentTable.HOUSE+"  text(50),"
                + StudentTable.ROOM+"  text(10),"
                + StudentTable.IMG+"  text )";
        db.execSQL(sql);
        //用户表
        sql="CREATE TABLE "+T_USER+" ("
                + UserTable._ID+" integer  PRIMARY KEY AUTOINCREMENT,"
                + UserTable.LOGIN+" integer,"
                + UserTable.ID+" text,"
                + UserTable.USERNAME+" text,"
                + UserTable.SAVAPASSWORD+" text,"
                + UserTable.SAFETY+" integer,"
                + UserTable.BM+" text,"
                + UserTable.PASSINFO+" text,"
                + UserTable.REGTIME+" text,"
                + UserTable.GID+" integer )";
        db.execSQL(sql);
        //消息列表
        sql="CREATE TABLE "+T_INFOLIST+" ("
                + InfoListTable._ID+" integer  PRIMARY KEY AUTOINCREMENT,"
                + InfoListTable.ID+" text,"
                + InfoListTable.COUNT+" integer,"
                + InfoListTable.NEWS+" integer,"
                + InfoListTable.VAR+" text,"
                + InfoListTable.TIME+" text )";
        db.execSQL(sql);
        //消息记录
        sql="CREATE TABLE "+T_INFO+" ("
                + InfoTable._ID+" integer  PRIMARY KEY AUTOINCREMENT,"
                +InfoTable.SENDID+" text,"
                +InfoTable.GETID+" text,"
                +InfoTable.INFOCLASS+" integer,"
                +InfoTable.VAR+" text,"
                +InfoTable.TIME+" text )";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
