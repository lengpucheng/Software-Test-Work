package cn.hll520.lpc.SoftwareTest.uilt;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

public class DataLink {
    private static DataLink link;
    //初始化一个链接
    public static DataLink getLink(){
        if(link==null)
            link=new DataLink();
        return link;
    }
//    public final static String linkname="sys_lyh_dome";
//    public final static String linkpass="Java_kcsj";
    public final static String linkname="qdm201674631";
    public final static String linkpass="guoai1619";
    //链接RDS数据库
    public Connection getConnection(String name, String password) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://qdm201674631.my3w.com:3306/qdm201674631_db?useSSL=false";
//            String url = "jdbc:mysql://lengpucheng.mysql.rds.aliyuncs.com:3306/xsgl?useSSL=false";
            return DriverManager.getConnection(url,name,password);
        } catch (Exception e) {
            Log.i("err","链接服务器失败");
            return null;
        }
    }
}
