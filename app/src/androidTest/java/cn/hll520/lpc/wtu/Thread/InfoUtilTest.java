package cn.hll520.lpc.wtu.Thread;

import android.content.Context;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;

import java.util.List;

import cn.hll520.lpc.xk.Thread.InfoUtil;
import cn.hll520.lpc.xk.data.Info;
import cn.hll520.lpc.xk.data.User;

public class InfoUtilTest {
    Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    InfoUtil infoUtil=new InfoUtil();
    List<Info> infos;

    @Test
    public void getInfo() {
        User user=new User();
        user.setID("1001011");
        infos=infoUtil.getInfo(user);
//        Log.i("msc",f+"");
//        Log.i("msc",infoUtil.getEffinfo());
        Log.i("msc",infos.size()+"");
        Info info=new Info();
//        info.setSendID("1008611");
//        info.setGetID("1001011");
//        info.setVar("wcnmlgb");
//        boolean f=infoUtil.sendInfo(info);
//        Log.i("msc",f+"");
    }
}