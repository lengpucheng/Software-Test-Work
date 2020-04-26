package cn.hll520.lpc.SoftwareTest.Thread;

import android.content.ContentValues;
import android.util.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.hll520.lpc.SoftwareTest.app.APP;
import cn.hll520.lpc.SoftwareTest.uilt.DataLink;

public class CheckAppUpdate {
    private DataLink dataLink = DataLink.getLink();
    private Connection connection = dataLink.getConnection(DataLink.linkname, DataLink.linkpass);
    private String effinfo = " ";
    public final static String FLAG = "flag";
    public final static String VS = "vs";  //最新版本
    public final static String VAR = "var";//内容
    public final static String URL = "url";//更新链接
    public final static String TIME = "time";//更新时间
    public final static String STOPVAR = "stopvar";//停止原因
    public final static String FALSE = "false";//不是最新
    public final static String TRUE = "true";//是最新
    public final static String STOP = "stop";//版本过低停用
    public final static String NOUP = "untrue";//不是最新，但不需要更新
    public final static String EFF = "eff";//不是最新，但不需要更新

    //获取错误信息
    public String getEffinfo() {
        return effinfo;
    }

    public ContentValues isChaek(String version) {
        ContentValues cv = new ContentValues();
        int[] size;
        String flag = EFF;
        String vs = "";
        String var = "null";
        String url = "www.hll520.cn";
        String time = "2020-04-04";
        String stpovar = "版本过低，已停止使用，必须下载更新！";
        List<ContentValues> cvs = new ArrayList<>();
        if (connection == null) {
            effinfo = "链接服务器失败！";
            flag = EFF;
            cv.put(FLAG, flag);
            return cv;
        }
        String sql = "SELECT * FROM app";
        try {
            PreparedStatement pres = connection.prepareStatement(sql);
            ResultSet res = pres.executeQuery();
            while (res.next()) {
                switch (res.getString(2)) {
                    case "1001":
                        vs = res.getString(4);
                        size = vesSize(version, vs);
                        flag = TRUE;
                        if (size[0] > 0)
                            break;
                        if (size[0] == 0) {
                            if (size[1] > 0)
                                break;
                            if (size[1] == 0) {
                                if (size[2] > 0)
                                    break;
                                if (size[2] == 0) {
                                    if (size[3] >= 0)
                                        break;
                                    else
                                        flag = NOUP;
                                    break;
                                }
                            }
                        }
                        flag = FALSE;
                        break;
                    case "1002":
                        var = res.getString(4);
                        break;
                    case "1003":
                        url = res.getString(4);
                        break;
                    case "1004":
                        time = res.getString(4);
                        break;
                    case "1005":
                        size = vesSize(version, res.getString(4));
                        for (int i : size)
                            Log.i("effg", i + ",");
                        if (size[0] > 0)
                            break;
                        if(size[0]==0){
                            if (size[1] > 0)
                                break;
                            if (size[1] == 0) {
                                if (size[2] > 0)
                                    break;
                                if (size[2] == 0)
                                    if (size[3] > 0)
                                        break;
                            }
                        }
                        flag = STOP;
                        break;
                    case "1006":
                        stpovar = res.getString(4);
                        if (stpovar == null)
                            stpovar = "暂无内容！";
                        break;
                    case "2001":
                        if (res.getString(4) != null && !"flase".equals(res.getString(4)))
                            APP.isINFO = res.getString(4);
                        break;
                    case "2002":
                        APP.INFO = res.getString(4);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            effinfo = "网络错误";
            flag = EFF;
            cv.put(FLAG, flag);
        }
        cv.put(FLAG, flag);
        cv.put(VS, vs);
        cv.put(VAR, var);
        cv.put(URL, url);
        cv.put(TIME, time);
        cv.put(STOPVAR, stpovar);
        return cv;
    }


    //字符串提取数字并转换为int
    private int strToInt(String str) {
        int vs = 0;
        String vss = "";
        if (str != null && !"".equals(str)) {
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) >= 48 && str.charAt(i) <= 57) {
                    vss += str.charAt(i);
                }
            }
        }
        vs = Integer.parseInt(vss);
        return vs;
    }

    //比较版本号大小
    private int[] vesSize(String A, String B) {
        int[] arry ={0,0,0,0};
        int[] as = vesToInt(A);
        int[] bs = vesToInt(B);
        arry[0] = as[0] - bs[0];
        arry[1] = as[1] - bs[1];
        arry[2] = as[2] - bs[2];
        arry[3] = as[3] - bs[3];
        return arry;
    }

    private int[] vesToInt(String ves) {
        if (ves == null)
            ves = "0.0.0.0";
        int[] verint = {0, 0, 0, 0};
        String[] vess = ves.split("\\.");
        for (int i = 0; i < vess.length; i++)
            verint[i] = strToInt(vess[i]);
        return verint;
    }


}