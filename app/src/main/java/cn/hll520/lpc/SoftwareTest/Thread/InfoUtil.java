package cn.hll520.lpc.SoftwareTest.Thread;
import android.os.Build;

import androidx.annotation.RequiresApi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.hll520.lpc.SoftwareTest.data.Info;
import cn.hll520.lpc.SoftwareTest.data.User;
import cn.hll520.lpc.SoftwareTest.uilt.DataLink;

public class InfoUtil {
    private DataLink dataLink = DataLink.getLink();
    private Connection connection = dataLink.getConnection(DataLink.linkname, DataLink.linkpass);
    private String effinfo=" ";
    //获取错误信息
    public String getEffinfo() {
        return effinfo;
    }

    //接收消息
    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Info> getInfo(User user){
        List<Info> infos=new ArrayList<>();
        if (connection == null) {
            effinfo = "链接服务器失败！";
            return infos;
        }
        String id=user.getID();
        if(id==null)
            id="null";
        String sql = "SELECT * FROM info WHERE Get_ID=?";
        try {
            PreparedStatement pres = connection.prepareStatement(sql);
            pres.setString(1,id);
            ResultSet res = pres.executeQuery();
            while (res.next()){
                Info info=new Info();
                info.setSendID(res.getString(2));
                info.setGetID(res.getString(3));
                info.setInfoClass(res.getInt(4));
                info.setVar(res.getString(5));
                info.setTime(res.getString(6));
                infos.add(info);
            }
            readInfo(user);
        } catch (SQLException e) {
            e.printStackTrace();
            effinfo="网络链接错误";
           return infos;
        }
        return infos;
    }
    //发送消息
    public boolean sendInfo(Info info){
        if (connection == null) {
            effinfo = "链接服务器失败！";
            return false;
        }
        String sql="INSERT INTO info(Send_ID, Get_ID, infoClass, var, time) " +
                "VALUES (?,?,?,?,? )";
        try {
            PreparedStatement pres = connection.prepareStatement(sql);
            pres.setString(1,info.getSendID());
            pres.setString(2,info.getGetID());
            pres.setInt(3,info.getInfoClass());
            pres.setString(4,info.getVar());
            pres.setString(5,info.getTime());
            pres.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            effinfo="网络链接错误";
            return false;
        }
    }
    //已读消息
    public boolean readInfo(User user){
        if (connection == null) {
            effinfo = "链接服务器失败！";
            return false;
        }
        String id=user.getID();
        if(id==null)
            id="null";
        String sql="DELETE FROM info WHERE Get_ID=?";
        try {
            PreparedStatement pres=connection.prepareStatement(sql);
            pres.setString(1,id);
            pres.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            effinfo="网络链接错误";
            return false;
        }
    }
}
