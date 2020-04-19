package cn.hll520.lpc.xk.Thread;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.hll520.lpc.xk.data.People;
import cn.hll520.lpc.xk.data.User;
import cn.hll520.lpc.xk.uilt.DataLink;

public class UseRdsUlit {
    private DataLink dataLink = DataLink.getLink();
    private Connection connection = dataLink.getConnection(DataLink.linkname, DataLink.linkpass);
    private String effinfo;

    //获取错误信息
    public String getEffinfo() {
        return effinfo;
    }

    //登录
    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean login(User user) {
        if (connection == null) {
            effinfo = "链接服务器失败！";
            Log.i("err", "登录链接失败");
            return false;
        } else {
            String sql = "SELECT * FROM user WHERE id=? and password=?";
            try {
                PreparedStatement pres = connection.prepareStatement(sql);
                pres.setString(1, user.getID());
                pres.setString(2, user.getPassword());
                ResultSet res = pres.executeQuery();
                while (res.next()) {
                    user.setID(res.getString(2));
                    user.setUsername(res.getString(3));
                    user.setSafety(res.getInt(5));
                    user.setBm(res.getString(6));
                    user.setPassinfo(res.getString(7));
                    user.setGID(res.getInt(9));
                    user.setLogintime(res.getString(10));
                    user.setRegTime(res.getString(11));
                    upLogintime(user);
                    return true;
                }
            } catch (SQLException e) {
                effinfo = "网络异常";
                Log.i("err", "网络异常");
                e.printStackTrace();
                return false;
            }
        }
        effinfo = "用户名或密码错误";
        Log.i("err", "用户名或密码错误");
        return false;
    }

    //更新登录时间
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void upLogintime(User user) {
        if (connection == null) {
            effinfo = "链接服务器失败！";
            Log.i("err", "登录链接失败");
        }
        String sql = "UPDATE user SET logintime=? WHERE ID=?";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            PreparedStatement pres = connection.prepareStatement(sql);
            String time = dateFormat.format(new Date());
            Log.i("test", "时间是：" + time);
            pres.setString(1, time);
            pres.setString(2, user.getID());
            pres.execute();
        } catch (SQLException e) {
            effinfo = "更新登录时间失败";
            Log.i("err", "网络异常");
            e.printStackTrace();
        }

    }

    //查找消息数值
    private int regSetGID() {
        int GID = -1;
        if (connection == null) {
            effinfo = "链接服务器失败！";
            Log.i("err", "登录链接失败");
        }
        String sql = "SELECT XID FROM userx";
        try {
            PreparedStatement pres = connection.prepareStatement(sql);
            ResultSet res = pres.executeQuery();
            while (res.next()) {
                GID = res.getInt(1);
            }
            return GID;
        } catch (SQLException e) {
            e.printStackTrace();
            effinfo = "网络错误";
            return GID;
        }
    }

    //查重
    public boolean checkID(String ID) {
        if (connection == null) {
            effinfo = "链接服务器失败！";
            Log.i("err", "登录链接失败");
            return false;
        }
        String sql = "SELECT * FROM user WHERE ID=?";
        try {
            PreparedStatement pres = connection.prepareStatement(sql);
            pres.setString(1, ID);
            ResultSet res = pres.executeQuery();
            if (res.next()) {
                effinfo = "学号重复";
                return false;
            }
        } catch (SQLException e) {
            effinfo = "网络异常";
            Log.i("err", "网络异常");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //邀请码验证
    private boolean checakYqm(String yqm) {
        if (connection == null) {
            effinfo = "链接服务器失败！";
            Log.i("err", "登录链接失败");
            return false;
        }
        String sql = "SELECT * FROM yqm WHERE var=?";
        try {
            PreparedStatement pres = connection.prepareStatement(sql);
            pres.setString(1, yqm);
            ResultSet res = pres.executeQuery();
            boolean flag = res.next();
            if (!flag) {
                effinfo = "邀请码不正确";
                return false;
            }
            return true;
        } catch (SQLException e) {
            effinfo = "网络异常";
            Log.i("err", "网络异常");
            e.printStackTrace();
            return false;
        }
    }

    //注册
    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean addNewUser(User user) {
        if (!checkID(user.getID()))
            return false;
        int GID = regSetGID();
        if (GID == -1)
            return false;
        if (connection == null) {
            effinfo = "链接服务器失败！";
            Log.i("err", "登录链接失败");
            return false;
        }
        String sql = "INSERT into user(ID, username, password, safety, bm, passinfo, passkey,GID,regtime) " +
                "VALUES (?,?,?,?,?,?,?,?,?);";
        try {
            PreparedStatement pres = connection.prepareStatement(sql);
            pres.setString(1, user.getID());
            pres.setString(2, user.getUsername());
            pres.setString(3, user.getPassword());
            pres.setInt(4, user.getSafety());
            pres.setString(5, user.getBm());
            pres.setString(6, user.getPassinfo());
            pres.setString(7, user.getPasskey());
            pres.setInt(8, GID);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            pres.setString(9, dateFormat.format(new Date()));
            Log.i("test", pres.toString());
            pres.execute();
            return true;
        } catch (SQLException e) {
            effinfo = "E2注册失败";
            Log.i("err", "网络异常");
            e.printStackTrace();
            Log.i("err", e.toString());
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean regUser(User user, People peo, String yqm) {
        if (!checakYqm(yqm))
            return false;
        if (!addNewUser(user))
            return false;
        if (connection == null) {
            effinfo = "链接服务器失败！";
            Log.i("err", "登录链接失败");
            return false;
        }
        String sql = "INSERT INTO student(ID, name, gender, phone,QQ, ment) VALUES (?,?,?,?,?,?)";
        try {
            PreparedStatement pres = connection.prepareStatement(sql);
            pres.setString(1, peo.getId());
            pres.setString(2, peo.getName());
            pres.setString(3, peo.getGender());
            pres.setString(4, peo.getPhone());
            pres.setString(5, peo.getQQ());
            pres.setString(6, peo.getMent());
            pres.execute();
            return true;
        } catch (SQLException e) {
            effinfo = "更新用户信息失败";
            Log.i("err", "更新用户信息失败");
            Log.i("err", e.toString());
            e.printStackTrace();
            return false;
        }
    }

    //查找password info
    public boolean findInfo(User user) {
        if (connection == null) {
            effinfo = "链接服务器失败！";
            Log.i("err", "登录链接失败");
            return false;
        }
        String sql = "SELECT passinfo,passkey FROM user WHERE ID=?";
        try {
            PreparedStatement pres = connection.prepareStatement(sql);
            pres.setString(1, user.getID());
            ResultSet res = pres.executeQuery();
            while (res.next()) {
                user.setPassinfo(res.getString(1));
                user.setPasskey(res.getString(2));
                return true;
            }
            effinfo = "该ID不存在";
            return false;
        } catch (SQLException e) {
            effinfo = "网络链接失败";
            Log.i("err", "查找失败");
            e.printStackTrace();
            return false;
        }
    }

    //修改密码
    public boolean upPassWord(User user) {
        if (connection == null) {
            effinfo = "链接服务器失败！";
            Log.i("err", "登录链接失败");
            return false;
        }
        String sql = "UPDATE user SET password=? WHERE ID=?";
        try {
            PreparedStatement pres = connection.prepareStatement(sql);
            pres.setString(1, user.getPassword());
            pres.setString(2, user.getID());
            pres.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            effinfo = "网络错误";
        }

        return false;
    }


    //添加人员
    public boolean addPeople(People peo) {
        return false;
    }

    //获取联系人信息
    //SELECT id,name,gender,phone,QQ,birthday,ment,position,campus,college,banji,house FROM student
    public List<People> getContact() {
        List<People> data = new ArrayList<>();
        if (connection == null) {
            effinfo = "链接服务器失败！";
            Log.i("err", "登录链接失败");
            return data;
        } else {
            String sql = "SELECT id,name,gender,phone,QQ,birthday,ment,position,campus,college,banji,house FROM student";
            try {
                PreparedStatement pres = connection.prepareStatement(sql);
                ResultSet res = pres.executeQuery();
                while (res.next()) {
                    People peo = new People();
                    peo.setId(res.getString(1));
                    peo.setName(res.getString(2));
                    peo.setGender(res.getString(3));
                    peo.setPhone(res.getString(4));
                    peo.setQQ(res.getString(5));
                    peo.setBirthday(res.getString(6));
                    peo.setMent(res.getString(7));
                    peo.setPosition(res.getString(8));
                    peo.setCampus(res.getString(9));
                    peo.setCollege(res.getString(10));
                    peo.setBanji(res.getString(11));
                    peo.setHouse(res.getString(12));
                    data.add(peo);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                effinfo = "网络连接错误";
            }
        }
        return data;
    }

}
