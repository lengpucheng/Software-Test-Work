package cn.hll520.lpc.SoftwareTest.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.io.Serializable;

import cn.hll520.lpc.SoftwareTest.provider.UserProvider;

public class User implements Serializable {
    private int login = 0;//login,0未登录，1登录
    private String ID;
    private String username;
    private String password;
    private String savaPassword;
    private int safety = 1;
    private String bm;
    private String passinfo = "初始密码是123456";
    private String passkey = "123456";
    private int GID;
    private String logintime = "2020-01-01";
    private String RegTime = "2020-01-01";

    public User() {
    }

    public User(boolean LOGIN) {
        if(LOGIN)
            this.login=1;
    }

    public User(String ID, String username, String password) {
        this.ID = ID;
        this.username = username;
        this.password = password;
    }

    public static User getLoginUser(Context context){
        User user=new User();
       Cursor cursor=context.getContentResolver().query(UserProvider.URI_CCONTACT,null,
                "login=?",new String[]{"1"},null);
        user.index(cursor);
        return user;
    }

    public int getLogin() {
        return login;
    }

    public void setLogin(int login) {
        this.login = login;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSavaPassword() {
        return savaPassword;
    }

    public void setSavaPassword(String password) {
        this.savaPassword = password;
    }

    public int getSafety() {
        return safety;
    }

    public void setSafety(int safety) {
        this.safety = safety;
    }

    public String getBm() {
        return bm;
    }

    public void setBm(String bm) {
        this.bm = bm;
    }

    public String getPassinfo() {
        return passinfo;
    }

    public void setPassinfo(String passinfo) {
        this.passinfo = passinfo;
    }

    public String getPasskey() {
        return passkey;
    }

    public void setPasskey(String passkey) {
        this.passkey = passkey;
    }

    public int getGID() {
        return GID;
    }

    public void setGID(int GID) {
        this.GID = GID;
    }

    public String getLogintime() {
        return logintime;
    }

    public void setLogintime(String logintime) {
        this.logintime = logintime;
    }

    public String getRegTime() {
        return RegTime;
    }

    public void setRegTime(String regTime) {
        RegTime = regTime;
    }

    @Override
    public String toString() {
        return "User{" +
                "login=" + login +
                ", ID='" + ID + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", savaPassword='" + savaPassword + '\'' +
                ", safety=" + safety +
                ", bm='" + bm + '\'' +
                ", passinfo='" + passinfo + '\'' +
                ", passkey='" + passkey + '\'' +
                ", GID=" + GID +
                ", logintime='" + logintime + '\'' +
                ", RegTime='" + RegTime  +
                '}';
    }

    public ContentValues getCv() {
        ContentValues cv = new ContentValues();
        cv.put("login", login);
        cv.put("ID", ID);
        cv.put("username", username);
        cv.put("savaPassword", savaPassword);
        cv.put("safety", safety);
        cv.put("bm", bm);
        cv.put("passinfo", passinfo);
        cv.put("RegTime", RegTime);
        cv.put("GID", GID);
        return cv;
    }

    public void setForCursor(Cursor cursor){
        login=cursor.getInt(1);
        ID=cursor.getString(2);
        username=cursor.getString(3);
        savaPassword=cursor.getString(4);
        safety=cursor.getInt(5);
        bm=cursor.getString(6);
        passinfo=cursor.getString(7);
        RegTime=cursor.getString(8);
        GID=cursor.getInt(9);
    }
    public void index(Cursor cursor){
        while (cursor.moveToNext()){
            login=cursor.getInt(1);
            ID=cursor.getString(2);
            username=cursor.getString(3);
            savaPassword=cursor.getString(4);
            safety=cursor.getInt(5);
            bm=cursor.getString(6);
            passinfo=cursor.getString(7);
            RegTime=cursor.getString(8);
            GID=cursor.getInt(9);
        }
    }
}
