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

    public final static String linkname = "software_test";
    public final static String linkpass = "Software_test";
    //链接RDS数据库
    public Connection getConnection(String name, String password) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://soft-wtu.hll520.cn:3306/software_test_work?useSSL=false";
            return DriverManager.getConnection(url,name,password);
        } catch (Exception e) {
            Log.i("err","链接服务器失败");
            return null;
        }
    }
}
