package cn.hll520.lpc.SoftwareTest.provider;

import android.content.Context;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;

import java.util.List;

import cn.hll520.lpc.SoftwareTest.data.Info;

public class InfoProviderTest {
    Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    @Test
    public void query() {

      List<Info> infos=Info.getInfosByID(appContext,"10086");
      for(int i=0;i<infos.size();i++){
          Log.i("msc",infos.get(i).toString());
      }
    }

    @Test
    public void insert() {
        Info info=new Info();
        info.setSendID("801");
        info.setGetID("801");
        info.setVar("c2c222p");
        appContext.getContentResolver().insert(InfoProvider.URI_CCONTACT,info.getCv());
    }

    @Test
    public void delete() {
        appContext.getContentResolver().delete(InfoProvider.URI_CCONTACT,null,null);
    }

    @Test
    public void update() {

    }
}