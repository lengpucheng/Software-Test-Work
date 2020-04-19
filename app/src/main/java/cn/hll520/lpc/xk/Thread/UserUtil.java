package cn.hll520.lpc.xk.Thread;

import android.util.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cn.hll520.lpc.xk.data.People;
import cn.hll520.lpc.xk.data.User;
import cn.hll520.lpc.xk.uilt.DataLink;

public class UserUtil {
    private DataLink dataLink = DataLink.getLink();
    private Connection connection = dataLink.getConnection(DataLink.linkname, DataLink.linkpass);
    private String effinfo = " ";

    //获取错误信息
    public String getEffinfo() {
        return effinfo;
    }

    public boolean checkPassTOkey(User user){
        if (connection == null) {
            effinfo = "链接服务器失败！";
            Log.i("err", "登录链接失败");
            return false;
        }
            String sql = "SELECT passinfo,passkey FROM user WHERE ID=? AND password=?";
        try {
            PreparedStatement pres = connection.prepareStatement(sql);
            pres.setString(1,user.getID());
            pres.setString(2,user.getPassword());
            ResultSet res=pres.executeQuery();
            while(res.next()){
                user.setPassinfo(res.getString(1));
                user.setPasskey(res.getString(2));
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            effinfo="网络错误";
            return false;
        }
        effinfo="密码错误";
        return false;
    }

    public boolean updataUser(User user){
        if (connection == null) {
            effinfo = "链接服务器失败！";
            Log.i("err", "登录链接失败");
            return false;
        }
        String sql="UPDATE user SET passinfo=?,passkey=?  WHERE ID=? AND password=?";
        try {
            PreparedStatement pers=connection.prepareStatement(sql);
            pers.setString(1,user.getPassinfo());
            pers.setString(2,user.getPasskey());
            pers.setString(3,user.getID());
            pers.setString(4,user.getPassword());
            pers.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            effinfo="网络错误";
            return false;
        }
    }

    public boolean updataPeo(People peo){
        if (connection == null) {
            effinfo = "链接服务器失败！";
            Log.i("err", "登录链接失败");
            return false;
        }
        String sql="UPDATE student SET phone=?,QQ=?," +
                "banji=?,birthday=?,campus=?,college=?,house=? WHERE ID=?";
        try {
            PreparedStatement pres=connection.prepareStatement(sql);
            pres.setString(1,peo.getPhone());
            pres.setString(2,peo.getQQ());
            pres.setString(3,peo.getBanji());
            pres.setString(4,peo.getBirthday());
            pres.setString(5,peo.getCampus());
            pres.setString(6,peo.getCollege());
            pres.setString(7,peo.getHouse());
            pres.setString(8,peo.getId());
            pres.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            effinfo="网络错误";
            return false;
        }

    }




}